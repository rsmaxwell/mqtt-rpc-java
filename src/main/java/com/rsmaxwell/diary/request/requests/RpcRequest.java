package com.rsmaxwell.diary.request.requests;

import java.net.HttpURLConnection;
import java.util.Map;

import org.eclipse.paho.mqttv5.common.MqttMessage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsmaxwell.diary.utils.Request;
import com.rsmaxwell.diary.utils.Utilities;

public abstract class RpcRequest {

	Request request;

	private ObjectMapper mapper = new ObjectMapper();

	public void setRequest(Request request) {
		this.request = request;
	}

	public Request getRequest() {
		return request;
	}

	public Map<String, Object> checkReply(String topic, MqttMessage replyMessage) throws Exception {

		String payload = new String(replyMessage.getPayload());

		System.out.printf("message %s, topic: %s, qos: %d\n", payload, topic, replyMessage.getQos());
		System.out.printf("decoding message payload: %s\n", payload);
		TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {
		};

		Map<String, Object> reply = mapper.readValue(payload, typeRef);

		if (reply == null) {
			throw new Exception("discarding message because decoded message was null");
		}

		int code = Utilities.getIntegerFromMap("code", reply);
		if (code != HttpURLConnection.HTTP_OK) {
			String message = Utilities.getStringFromMap("message", reply);
			if (message == null) {
				throw new Exception(String.format("code: %d\n", code));
			} else {
				throw new Exception(String.format("code: %d, message: %s\n", code, message));
			}
		}

		return reply;
	}

	public abstract void handle(Map<String, Object> replyMessage) throws Exception;
}
