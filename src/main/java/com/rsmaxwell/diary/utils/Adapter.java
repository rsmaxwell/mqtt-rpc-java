package com.rsmaxwell.diary.utils;

import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;

public class Adapter implements MqttCallback {

	public void messageArrived(String topic, MqttMessage replyMessage) throws Exception {
		System.out.println("messageArrived");
	}

	public void disconnected(MqttDisconnectResponse disconnectResponse) {
		System.out.println("disconnected");
	}

	public void mqttErrorOccurred(MqttException exception) {
		System.out.println("mqttErrorOccurred");
	}

	public void deliveryComplete(IMqttToken token) {
		System.out.println("deliveryComplete");
	}

	public void connectComplete(boolean reconnect, String serverURI) {
		System.out.println("connectComplete");
	}

	public void authPacketArrived(int reasonCode, MqttProperties properties) {
		System.out.println("authPacketArrived");
	}
}
