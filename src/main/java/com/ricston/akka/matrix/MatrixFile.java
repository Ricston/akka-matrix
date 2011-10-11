/*
 * Copyright (c) Ricston Ltd  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.ricston.akka.matrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MatrixFile {

	private List<List<Double>> matrix1;
	private List<List<Double>> matrix2;
	private List<List<Double>> result;
	
	public MatrixFile(List<List<Double>> matrix1, List<List<Double>> matrix2, List<List<Double>> result) {
		this.matrix1 = matrix1;
		this.matrix2 = matrix2;
		this.result = result;
	}

	public List<List<Double>> getMatrix1() {
		return matrix1;
	}

	public void setMatrix1(List<List<Double>> matrix1) {
		this.matrix1 = matrix1;
	}

	public List<List<Double>> getMatrix2() {
		return matrix2;
	}

	public void setMatrix2(List<List<Double>> matrix2) {
		this.matrix2 = matrix2;
	}

	public List<List<Double>> getResult() {
		return result;
	}

	public void setResult(List<List<Double>> result) {
		this.result = result;
	}
	
	public static void writeMatrixFile(String filename,
			List<List<Double>> matrix1, List<List<Double>> matrix2,
			List<List<Double>> matrixResult) throws IOException {

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		MatrixFile mF = new MatrixFile(matrix1, matrix2, matrixResult);

		FileUtils.writeStringToFile(new File(filename), gson.toJson(mF));
	}
	
	public static MatrixFile readMatrixFile(String filename) {
		Gson gson = new Gson();
		MatrixFile mF = null;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			
			mF = gson.fromJson(br, MatrixFile.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return mF;
	}

}
