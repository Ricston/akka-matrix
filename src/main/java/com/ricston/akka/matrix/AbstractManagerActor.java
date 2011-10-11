/*
 * Copyright (c) Ricston Ltd  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.ricston.akka.matrix;

import static akka.actor.Actors.actorOf;

import java.util.LinkedList;
import java.util.Queue;

import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

import com.ricston.akka.matrix.msgs.JobMsg;
import com.ricston.akka.matrix.msgs.ResultMsg;
import com.ricston.akka.matrix.msgs.WorkerMsg;

public abstract class AbstractManagerActor extends UntypedActor {

	protected Queue<WorkerMsg> idleWorkers;
	protected Queue<JobMsg> pendingJobs;
	protected final int numberOfActors;
	protected final long managerStartTime;

	public AbstractManagerActor() {
		this(Runtime.getRuntime().availableProcessors());
	}

	public AbstractManagerActor(int numberOfActors) {
		idleWorkers = new LinkedList<WorkerMsg>();
		pendingJobs = new LinkedList<JobMsg>();
		this.numberOfActors = numberOfActors;
		managerStartTime = System.nanoTime();
	}

	protected abstract void onJobComplete(ResultMsg msg) throws Exception;

	protected void processJob() {
		// If there is at least one job and at least one idle worker.
		if (!(idleWorkers.isEmpty() || pendingJobs.isEmpty())) {
			// Send a Job to a Worker for processing.
			idleWorkers.remove().worker.tell(pendingJobs.remove());
		}
	}

	@Override
	public void onReceive(Object obj) throws Exception {
		if (obj instanceof JobMsg) {
			pendingJobs.add((JobMsg) obj);
			processJob();
		} else if (obj instanceof WorkerMsg) {
			idleWorkers.add((WorkerMsg) obj);
			processJob();
		} else if (obj instanceof ResultMsg) {
			onJobComplete((ResultMsg) obj);

		}
	}

	@Override
	public void preStart() {
		super.preStart();	
		
		// Start the actors which will handle multiplication.
		for (int i = 0; i < this.numberOfActors; i++) {
			actorOf(new UntypedActorFactory() {

				public UntypedActor create() {

					return new WorkerActor(getContext());
				}

			}).start();

		}
	}
}
