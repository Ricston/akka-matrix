/*
 * Copyright (c) Ricston Ltd  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.ricston.akka.matrix;

import static fj.Show.doubleShow;
import static fj.Show.listShow;

import java.util.ArrayList;

import fj.F;
import fj.data.Java;
import fj.data.List;

public class Matrix {

	protected final F<List<Double>, F<List<List<Double>>, List<List<Double>>>> consFun = List.cons();
	
	public final List<List<Double>> data;
	
	public Matrix(java.util.List<java.util.List<Double>> lstlstDouble) {
				
		List<List<Double>> temp = List.nil();
		
		for(int i = lstlstDouble.size() - 1; i > -1; i--) {
			java.util.List<Double> row = lstlstDouble.get(i);			
			List<Double> immutableRow = List.list(row.toArray(new Double[row.size()]));
			temp = consFun.f(immutableRow).f(temp);
		}
		this.data = temp;
	}
	
	public Matrix(List<List<Double>> data) {
		this.data = data;
	}
	
	public static Matrix getEmptyMatrix() {
		List<List<Double>> temp = List.nil();
		return new Matrix(temp);
	}
	
	public Matrix addRow(java.util.List<Double> row) {
		return new Matrix(consFun.f(List.list(row.toArray(new Double[row.size()]))).f(data));
	}
	
	public java.util.List<java.util.List<Double>> getData() {
		
		java.util.List<java.util.List<Double>> returnList = new ArrayList<java.util.List<Double>>(data.length());
		
		java.util.List<List<Double>> matrix = Java.<List<Double>>List_ArrayList().f(data);
		
		// Convert every FJ list in matrix to a Java list
		// and add to returnList
		for(List<Double> fjList : matrix) {
			java.util.List<Double> row = Java.<Double>List_ArrayList().f(fjList);
			returnList.add(row);
		}
		
		return returnList;		
	}

	public String toString() {
		return List.asString(listShow(listShow(doubleShow)).show(data).toList());
	}
}
