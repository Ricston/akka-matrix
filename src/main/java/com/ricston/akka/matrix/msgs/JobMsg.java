/*
 * Copyright (c) Ricston Ltd  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.ricston.akka.matrix.msgs;

import com.ricston.akka.matrix.Matrix;

public abstract class JobMsg {

	public final String filename;
	public final Long startTime;

	public JobMsg(String filename, Long startTime) {
		this.filename = filename;
		this.startTime = startTime;
	}

	public static class ReadJobMsg extends JobMsg {
		public ReadJobMsg(String filename, Long startTime) {
			super(filename, startTime);
		}
	}

	public static class ComputeJobMsg extends JobMsg {

		public final Matrix matrix1, matrix2, resultMatrix;
		public final Integer nextRow;

		public ComputeJobMsg(String filename, Long startTime, Matrix matrix1,
				Matrix matrix2, Matrix resultMatrix) {
			// If a Job is being created from outside of Job,
			// then nextRow starts out as the last row in matrix1.
			this(filename, startTime, matrix1, matrix2, resultMatrix,
					matrix1.data.length() - 1);
		}

		private ComputeJobMsg(String filename, Long startTime, Matrix matrix1,
				Matrix matrix2, Matrix resultMatrix, int nextRow) {
			super(filename, startTime);
			this.matrix1 = matrix1;
			this.matrix2 = matrix2;
			this.nextRow = nextRow;
			this.resultMatrix = resultMatrix;
		}

		public ComputeJobMsg nextJob(java.util.List<Double> computedRow) {
			return new ComputeJobMsg(filename, startTime, matrix1, matrix2,
					resultMatrix.addRow(computedRow), nextRow - 1);
		}

		public boolean isJobComplete() {
			return nextRow < 0;
		}

	}

}
