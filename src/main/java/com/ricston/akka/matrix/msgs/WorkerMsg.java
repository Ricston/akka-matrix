/*
 * Copyright (c) Ricston Ltd  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.ricston.akka.matrix.msgs;

import akka.actor.ActorRef;

public class WorkerMsg {
	
	public final ActorRef worker;
	
	public WorkerMsg(ActorRef worker) {
		this.worker = worker;
	}

}
