package com.rsmaxwell.rpc.utils;

import java.util.UUID;

public class Token {

	private String id;

	public Token() {
		id = UUID.randomUUID().toString();
	}

	public String getID() {
		return id;
	}

	public synchronized void waitForCompletion() throws InterruptedException {
		wait();
	}

	public synchronized void completed() throws InterruptedException {
		notifyAll();
	}
}
