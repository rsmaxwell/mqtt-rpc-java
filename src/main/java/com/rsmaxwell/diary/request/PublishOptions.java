package com.rsmaxwell.diary.request;

public class PublishOptions {

	String topic;
	byte[] request;

	public PublishOptions(String topic, byte[] request) {
		this.topic = topic;
		this.request = request;
	}

}