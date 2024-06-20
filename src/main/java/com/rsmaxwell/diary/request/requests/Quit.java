package com.rsmaxwell.diary.request.handlers;

import java.util.Map;

import org.eclipse.paho.mqttv5.common.MqttMessage;

import com.rsmaxwell.diary.utils.Request;
import com.rsmaxwell.diary.utils.Utilities;

public class QuitHandler extends RequestResponse {

	public QuitHandler() {
		request = new Request("quit");
		setRequest(request);
	}

	@Override
	public void messageArrived(String topic, MqttMessage replyMessage) throws Exception {
		System.out.println("Quit.messageArrived");

		Map<String, Object> reply = checkReply(topic, replyMessage);

		String result = Utilities.getStringFromMap("result", reply);
		System.out.printf("result: %s\n", result);
	}
}
