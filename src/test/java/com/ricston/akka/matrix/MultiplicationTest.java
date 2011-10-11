/*
 * Copyright (c) Ricston Ltd  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.ricston.akka.matrix;

import static akka.actor.Actors.actorOf;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;
import akka.actor.Actors;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

public class MultiplicationTest extends TestCase {
	
	public void testMultiplication() throws InterruptedException {
				
		final CountDownLatch latch = new CountDownLatch(1);
		
		
		actorOf(new UntypedActorFactory() {

			public UntypedActor create() {
				
				return new ManagerTestMultiplicationActor(latch);
			}
		}).start();	
		
		if(!latch.await(5000, TimeUnit.MILLISECONDS)) {
			fail("Multiplication test failed.");
		}
		
		Actors.registry().shutdownAll();
	}
	
}
