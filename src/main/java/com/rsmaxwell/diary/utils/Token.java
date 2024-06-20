package com.rsmaxwell.diary.utils;

import java.util.UUID;

public class Token {

	private String id;

	public Token() {
		id = UUID.randomUUID().toString();
	}

	public String getID() {
		return id;
	}

	public synchronized void waitForResponse() throws InterruptedException {
		wait();
	}

	public synchronized void messageArrived() throws InterruptedException {
		notifyAll();
	}
}
