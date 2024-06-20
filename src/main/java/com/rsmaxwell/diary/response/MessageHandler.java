package com.rsmaxwell.diary.response;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsmaxwell.diary.response.handler.RequestHandler;
import com.rsmaxwell.diary.utils.Adapter;
import com.rsmaxwell.diary.utils.Request;
import com.rsmaxwell.diary.utils.Utilities;

public class MessageHandler extends Adapter implements MqttCallback {

	private MqttAsyncClient client;
	private HashMap<String, RequestHandler> handlers;
	private ObjectMapper mapper = new ObjectMapper();
	private boolean keepRunning = true;

	public MessageHandler(HashMap<String, RequestHandler> handlers) {
		this.handlers = handlers;
	}

	public void setClient(MqttAsyncClient client) {
		this.client = client;
	}

	public void messageArrived(String topic, MqttMessage requestMessage) throws Exception {
		System.out.println("messageArrived");
		System.out.println("topic: " + topic);
		System.out.println("qos: " + requestMessage.getQos());
		System.out.println("message content: " + new String(requestMessage.getPayload()));

		try {
			MqttProperties requestProperties = requestMessage.getProperties();
			if (requestProperties == null) {
				System.out.println("discarding message with no properties");
				return;
			}

			String responseTopic = requestProperties.getResponseTopic();
			if (responseTopic == null) {
				System.out.println("discarding message with no responseTopic");
				return;
			}

			if (responseTopic.length() <= 0) {
				System.out.println("discarding message with empty responseTopic");
				return;
			}

			System.out.println("responseTopic: " + responseTopic);

			String payload = new String(requestMessage.getPayload());

			Request request = null;
			try {
				System.out.println("decoding message payload: " + payload);
				request = mapper.readValue(payload, Request.class);
				System.out.println("decoded message payload");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("discarding message because message could not be decoded");
				return;
			}

			if (request == null) {
				System.out.println("discarding message because there was no request");
				return;
			}

			if (request.getFunction() == null) {
				System.out.println("discarding message with no function");
				return;
			}

			if (request.getFunction().length() <= 0) {
				System.out.println("discarding message with empty function");
				return;
			}

			System.out.println("function: " + request.getFunction());

			RequestHandler handler = handlers.get(request.getFunction());

			if (handler == null) {
				System.out.println("discarding message with unexpected function");
				return;
			}

			Map<String, Object> result = null;
			try {
				result = handler.handleRequest(request.args);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("discarding message because the message could not be handled");
				return;
			}

			if (result == null) {
				System.out.println("discarding message because result was null");
				return;
			}

			System.out.println("result: " + result.toString());

			boolean found = Utilities.isPresent("keepRunning", result);
			if (found) {
				boolean value = Utilities.getBooleanFromMap("keepRunning", result);
				if (value == false) {
					System.out.println("quitting");
					keepRunning = false;
					return;
				}
			}

			System.out.println("encoding result");
			byte[] response = null;
			try {
				response = mapper.writeValueAsBytes(result);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("discarding message because the response could not be encoded");
				return;
			}

			if (response == null) {
				System.out.println("discarding message because response was null");
				return;
			}

			MqttProperties responseProperties = new MqttProperties();
			responseProperties.setCorrelationData(requestMessage.getProperties().getCorrelationData());

			int qos = 0;
			MqttMessage responseMessage = new MqttMessage(response);
			responseMessage.setProperties(responseProperties);
			responseMessage.setQos(qos);
			responseMessage.setRetained(false);

			System.out.printf(String.format("Publishing: %s to topic: %s with qos: %d\n", new String(response), responseTopic, qos));
			client.publish(responseTopic, responseMessage).waitForCompletion();
			System.out.println(String.format("publish complete\n"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean keepRunning() {
		return keepRunning;
	}
}
