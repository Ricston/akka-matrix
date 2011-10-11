/*
 * Copyright (c) Ricston Ltd  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.ricston.akka.matrix;

import java.util.ArrayList;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import com.ricston.akka.matrix.msgs.JobMsg.ComputeJobMsg;
import com.ricston.akka.matrix.msgs.JobMsg.ReadJobMsg;
import com.ricston.akka.matrix.msgs.ResultMsg.ComputeResultMsg;
import com.ricston.akka.matrix.msgs.ResultMsg.ReadFailedMsg;
import com.ricston.akka.matrix.msgs.ResultMsg.ReadResultMsg;
import com.ricston.akka.matrix.msgs.WorkerMsg;

public class WorkerActor extends UntypedActor {

	final ActorRef manager;
	final WorkerMsg registerSelf;

	public WorkerActor(ActorRef manager) {
		this.manager = manager;
		registerSelf = new WorkerMsg(getContext());
	}

	protected void handleComputeMsg(final ComputeJobMsg job) {
		List<Double> resultRow = new ArrayList<Double>();

		for (int i = 0, matrix2RowLength = job.matrix2.data.head().length(); i < matrix2RowLength; i++) {
			double sum = 0.0;
			for (int j = 0, matrix2ColumnLength = job.matrix2.data.length(); j < matrix2ColumnLength; j++) {
				sum += job.matrix1.data.index(job.nextRow).index(j)
						* job.matrix2.data.index(j).index(i);
			}
			resultRow.add(i, sum);
		}

		// Create a new Job with an updated resultMatrix.
		// This new Job will be needed whether or not the
		// matrix computation has finished.
		ComputeJobMsg newJob = job.nextJob(resultRow);

		// Check whether the newJob needs any computation.
		if (newJob.isJobComplete()) {
			// The result matrix has been computed so send
			// the manager a JobComplete message.
			manager.tell(new ComputeResultMsg(newJob, System.nanoTime()));
		} else {
			// The result matrix has not been computed yet
			// so send another Job to the manager.
			manager.tell(newJob);
		}
	}

	protected void handleReadMsg(final ReadJobMsg job) {
		MatrixFile mF = MatrixFile.readMatrixFile(job.filename);
		if (mF != null) {
			// Send a ComputeJobMsg to process the matrices in the file.
			manager.tell(new ComputeJobMsg(job.filename, System.nanoTime(),
					new Matrix(mF.getMatrix1()), new Matrix(mF.getMatrix2()),
					Matrix.getEmptyMatrix()));
			// Send a ReadResultMsg to signal the end of the ReadJobMsg.
			manager.tell(new ReadResultMsg(job, System.nanoTime()));
		} else {
			manager.tell(new ReadFailedMsg(job, System.nanoTime()));
		}
	}

	@Override
	public void onReceive(final Object obj) throws Exception {

		if (obj instanceof ComputeJobMsg) {
			handleComputeMsg((ComputeJobMsg) obj);
		} else if (obj instanceof ReadJobMsg) {
			handleReadMsg((ReadJobMsg) obj);
		}

		// This WorkerActor is ready for more work so register it with
		// the manager.
		manager.tell(registerSelf);
	}

	@Override
	public void preStart() {
		super.preStart();
		manager.tell(registerSelf);
	}

}
