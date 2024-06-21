package com.rsmaxwell.rpc.request;

import org.eclipse.paho.mqttv5.client.MqttAsyncClient;

public class HandlerOptions {

	MqttAsyncClient client;
	String responseTopicFormat;
	String clientID;

	public HandlerOptions(MqttAsyncClient client, String responseTopicFormat, String clientID) {
		this.client = client;
		this.responseTopicFormat = String.format(responseTopicFormat, clientID);
		this.clientID = clientID;
	}

}
