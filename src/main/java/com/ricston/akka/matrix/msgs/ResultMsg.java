/*
 * Copyright (c) Ricston Ltd  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.ricston.akka.matrix.msgs;

import java.io.IOException;

import com.ricston.akka.matrix.MatrixFile;
import com.ricston.akka.matrix.msgs.JobMsg.ComputeJobMsg;
import com.ricston.akka.matrix.msgs.JobMsg.ReadJobMsg;

public abstract class ResultMsg {
	
	public final Long finishTime;
	
	public ResultMsg(Long finishTime) {
		this.finishTime = finishTime;
	}

	public abstract String describeResult();

	public void action() throws Exception {
		// Override as necessary.
	}

	public static class ComputeResultMsg extends ResultMsg {
		public final ComputeJobMsg job;		

		public ComputeResultMsg(ComputeJobMsg job, Long finishTime) {
			super(finishTime);
			this.job = job;
		}

		public String describeResult() {
			return String.format(
					"Took %s seconds to compute matrix multiplication for "
							+ "the matrices in %s.",
					(finishTime - job.startTime) / 1.0e9, job.filename);
		}

		@Override
		public void action() throws IOException {
			MatrixFile.writeMatrixFile(job.filename, job.matrix1.getData(),
					job.matrix2.getData(), job.resultMatrix.getData());
		}
	}

	public static class ReadResultMsg extends ResultMsg {
		public final ReadJobMsg job;

		public ReadResultMsg(ReadJobMsg job, Long finishTime) {
			super(finishTime);
			this.job = job;
		}

		public String describeResult() {
			return String.format("Took %s seconds to read the matrices in %s.",
					(finishTime - job.startTime) / 1.0e9, job.filename);
		}
	}
	
	public static class ReadFailedMsg extends ResultMsg {
		public final ReadJobMsg job;
		
		public ReadFailedMsg(ReadJobMsg job, Long finishTime) {
			super(finishTime);
			this.job = job;
		}

		@Override
		public String describeResult() {
			return String.format("Failed to read %s.", job.filename);
		}
	}

}
