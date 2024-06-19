package com.rsmaxwell.diary.response;

import java.util.ArrayList;

import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;

public class ClientConfig {

	public ArrayList<String> serverUrls;
	public MqttConnectionOptions connOpts;
	public String rpcRequestTopic;

	public ClientConfig(String server, String topic, String username, String password) {
		serverUrls = new ArrayList<String>();
		serverUrls.add(server);
		this.rpcRequestTopic = topic;

		connOpts = new MqttConnectionOptions();
		connOpts.setUserName(username);
		connOpts.setPassword(password.getBytes());
	}
}
