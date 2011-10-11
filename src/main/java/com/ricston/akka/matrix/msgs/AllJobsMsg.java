/*
 * Copyright (c) Ricston Ltd  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.ricston.akka.matrix.msgs;

import java.util.Arrays;
import java.util.List;

import akka.actor.ActorRef;

import com.ricston.akka.matrix.msgs.JobMsg.ReadJobMsg;

public class AllJobsMsg {

	private final List<String> filesToProcess;

	public AllJobsMsg(List<String> filesToProcess) {
		this.filesToProcess = filesToProcess;
	}

	public AllJobsMsg(String[] filesToProcess) {
		this.filesToProcess = Arrays.asList(filesToProcess);
	}

	public void scheduleJobs(ActorRef actor) {
		for (String file : filesToProcess) {
			actor.tell(new ReadJobMsg(file, System.nanoTime()));
		}
	}

	public int getNumberOfJobs() {
		return filesToProcess.size();
	}

}
