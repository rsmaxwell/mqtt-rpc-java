package com.rsmaxwell.diary.request.handlers;

import java.util.Map;

import org.eclipse.paho.mqttv5.common.MqttMessage;

import com.rsmaxwell.diary.utils.Request;
import com.rsmaxwell.diary.utils.Utilities;

public class CalculatorHandler extends RequestResponse {

	public CalculatorHandler(String operation, int param1, int param2) {
		request = new Request("calculator");
		request.put("operation", operation);
		request.put("param1", param1);
		request.put("param2", param2);
		setRequest(request);
	}

	@Override
	public void messageArrived(String topic, MqttMessage replyMessage) throws Exception {
		System.out.println("Calculator.messageArrived");

		Map<String, Object> reply = checkReply(topic, replyMessage);

		int result = Utilities.getIntegerFromMap("result", reply);
		System.out.printf("result: %d\n", result);
	}
}
