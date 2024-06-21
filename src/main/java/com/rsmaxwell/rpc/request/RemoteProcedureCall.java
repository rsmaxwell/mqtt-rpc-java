package com.rsmaxwell.rpc.request;

import java.util.WeakHashMap;

import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.MqttSubscription;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsmaxwell.rpc.utils.Adapter;
import com.rsmaxwell.rpc.utils.Response;
import com.rsmaxwell.rpc.utils.Token;

public class RemoteProcedureCall {

	static int qos = 0;
	static String clientID = "requester";
	static WeakHashMap<String, MqttMessage> replies = new WeakHashMap<>();
	static WeakHashMap<String, Token> tokens = new WeakHashMap<>();
	static private ObjectMapper mapper = new ObjectMapper();

	private HandlerOptions options;

	public RemoteProcedureCall(HandlerOptions options) throws MqttException {
		this.options = options;
	}

	// Subscribe to the response topic
	public void subscribe() throws Exception {
		String responseTopic = String.format(options.responseTopicFormat, options.clientID);
		MqttSubscription subscription = new MqttSubscription(responseTopic);
		System.out.printf("subscribing to: %s\n", responseTopic);
		options.client.subscribe(subscription).waitForCompletion();
	}

	public Token request(PublishOptions publishOptions) throws Exception {

		Token token = new Token();
		String correlID = token.getID();

		MqttMessage message = new MqttMessage(publishOptions.request);
		MqttProperties properties = new MqttProperties();
		properties.setResponseTopic(options.responseTopicFormat);
		properties.setCorrelationData(correlID.getBytes());
		message.setProperties(properties);
		message.setQos(qos);
		message.setRetained(false);

		System.out.printf(String.format("Publishing: %s to topic: %s with qos: %d\n", new String(publishOptions.request), publishOptions.topic, qos));
		System.out.printf(String.format("    replyTopic: %s\n", options.responseTopicFormat));
		options.client.publish(publishOptions.topic, message).waitForCompletion();
		System.out.println("Message published");

		tokens.put(correlID, token);
		return token;
	}

	public MqttCallback getAdapter() {
		Adapter adapter = new Adapter() {

			@Override
			public void messageArrived(String topic, MqttMessage reply) throws Exception {
				System.out.println("RemoteProcedureCall.Adapter.messageArrived");

				MqttProperties properties = reply.getProperties();
				byte[] corrationData = properties.getCorrelationData();
				String correlID = new String(corrationData);

				System.out.printf("correlID: %s\n", correlID);

				Token token = tokens.get(correlID);
				if (token == null) {
					System.out.printf("Discarding reply because token is null\n");
					return;
				}

				replies.put(correlID, reply);
				token.completed();
			}
		};

		return adapter;
	}

	public Response waitForResponse(Token token) throws Exception {

		token.waitForCompletion();

		String correlID = token.getID();
		MqttMessage reply = replies.get(correlID);

		byte[] payload = reply.getPayload();
		return mapper.readValue(payload, Response.class);
	}
}
