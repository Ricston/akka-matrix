/*
 * Copyright (c) Ricston Ltd  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.ricston.akka.matrix;

import akka.actor.Actors;

import com.ricston.akka.matrix.msgs.AllJobsMsg;
import com.ricston.akka.matrix.msgs.ResultMsg;
import com.ricston.akka.matrix.msgs.ResultMsg.ComputeResultMsg;
import com.ricston.akka.matrix.msgs.ResultMsg.ReadFailedMsg;

public class ManagerActor extends AbstractManagerActor {

	protected long jobCount = 0L;

	public ManagerActor() {
		super();
	}
	
	public ManagerActor(int numberOfActors) {
		super(numberOfActors);
	}

	protected void tryShutdown() {
		jobCount--;
		if (jobCount == 0) {
			Actors.registry().shutdownAll();
		}
	}

	@Override
	public void onReceive(Object obj) throws Exception {

		super.onReceive(obj);

		if (obj instanceof ComputeResultMsg) {
			tryShutdown();
		} else if (obj instanceof AllJobsMsg) {
			jobCount = ((AllJobsMsg) obj).getNumberOfJobs();
			((AllJobsMsg) obj).scheduleJobs(getContext());
		} else if (obj instanceof ReadFailedMsg) {
			tryShutdown();
		}

	}

	@Override
	protected void onJobComplete(ResultMsg msg) throws Exception {
		System.out.println(msg.describeResult());

		// Perform action associated with the result received.
		msg.action();
	}

	@Override
	public void postStop() {
		super.postStop();
		// (finishTime - job.startTime) / 1.0e9
		System.out.println("The manager spent " + ((System.nanoTime() - managerStartTime) / 1.0e9) + " seconds alive. " +
				"Number of actors = " + numberOfActors + ".");
	}
}
