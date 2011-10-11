/*
 * Copyright (c) Ricston Ltd  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.ricston.akka.matrix;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.ricston.akka.matrix.msgs.JobMsg.ComputeJobMsg;
import com.ricston.akka.matrix.msgs.ResultMsg;
import com.ricston.akka.matrix.msgs.ResultMsg.ComputeResultMsg;

public class ManagerTestMultiplicationActor extends AbstractManagerActor {

	final List<List<Double>> expectedResult;
	final CountDownLatch latch;

	public ManagerTestMultiplicationActor(CountDownLatch latch) {
		this.expectedResult = getExpectedResult();
		this.latch = latch;
	}

	@Override
	public void onReceive(Object obj) throws Exception {
		super.onReceive(obj);

	}

	@Override
	protected void onJobComplete(ResultMsg job) {
		try {
			assertTrue(job instanceof ComputeResultMsg);
			assertThat(expectedResult,
					is(((ComputeResultMsg) job).job.resultMatrix.getData()));
			latch.countDown();

		} catch (AssertionError e) {

			throw new AssertionError(e);
		}

	}

	@Override
	public void preStart() {
		super.preStart();
		startTest();
	}

	public void startTest() {
		Matrix matrix1 = getMatrix1();
		Matrix matrix2 = getMatrix2();

		getContext().tell(
				new ComputeJobMsg(null, System.nanoTime(), matrix1, matrix2,
						Matrix.getEmptyMatrix()));
	}

	protected static List<List<Double>> getExpectedResult() {

		List<List<Double>> matrix = new ArrayList<List<Double>>();

		List<Double> row1 = new ArrayList<Double>();
		row1.add(0, 23.0);
		row1.add(1, 7.0);
		row1.add(2, 32.0);

		List<Double> row2 = new ArrayList<Double>();
		row2.add(0, 11.0);
		row2.add(1, 9.0);
		row2.add(2, 24.0);

		List<Double> row3 = new ArrayList<Double>();
		row3.add(0, 41.0);
		row3.add(1, 17.0);
		row3.add(2, 64.0);

		List<Double> row4 = new ArrayList<Double>();
		row4.add(0, 24.0);
		row4.add(1, 22.0);
		row4.add(2, 56.0);

		matrix.add(0, row1);
		matrix.add(1, row2);
		matrix.add(2, row3);
		matrix.add(3, row4);

		return matrix;
	}

	protected static Matrix getMatrix1() {

		List<List<Double>> matrix = new ArrayList<List<Double>>();

		List<Double> row1 = new ArrayList<Double>();
		row1.add(0, 2.0);
		row1.add(1, 3.0);

		List<Double> row2 = new ArrayList<Double>();
		row2.add(0, 4.0);
		row2.add(1, 1.0);

		List<Double> row3 = new ArrayList<Double>();
		row3.add(0, 6.0);
		row3.add(1, 5.0);

		List<Double> row4 = new ArrayList<Double>();
		row4.add(0, 10.0);
		row4.add(1, 2.0);

		matrix.add(0, row1);
		matrix.add(1, row2);
		matrix.add(2, row3);
		matrix.add(3, row4);

		return new Matrix(matrix);

	}

	protected static Matrix getMatrix2() {

		List<List<Double>> matrix = new ArrayList<List<Double>>();

		List<Double> row1 = new ArrayList<Double>();
		row1.add(0, 1.0);
		row1.add(1, 2.0);
		row1.add(2, 4.0);

		List<Double> row2 = new ArrayList<Double>();
		row2.add(0, 7.0);
		row2.add(1, 1.0);
		row2.add(2, 8.0);

		matrix.add(0, row1);
		matrix.add(1, row2);

		return new Matrix(matrix);

	}

}
