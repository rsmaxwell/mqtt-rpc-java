package com.rsmaxwell.diary.request;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttClientPersistence;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MqttDefaultFilePersistence;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.MqttSubscription;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsmaxwell.diary.request.handlers.RequestResponse;
import com.rsmaxwell.diary.response.ClientConfig;

public class RemoteProcedureCall {

	static String clientID = "requester";

	private ObjectMapper mapper = new ObjectMapper();
	private ClientConfig config;

	public RemoteProcedureCall(ClientConfig value) {
		config = value;
		config.connOpts.setCleanStart(true);
	}

	public void performRequest(RequestResponse handler) throws Exception {

		String broker = config.serverUrls.get(0);
		MqttClientPersistence persistence = new MqttDefaultFilePersistence();
		String replyTopic = String.format("response/%s", clientID);
		int qos = 0;

		MqttAsyncClient client = new MqttAsyncClient(broker, clientID, persistence);
		MqttConnectionOptions connOpts = config.connOpts;

		client.setCallback(handler);

		// Connect
		System.out.printf(String.format("Connecting to broker: %s as '%s'\n", broker, clientID));
		client.connect(connOpts).waitForCompletion();
		System.out.printf("Client %s connected\n", clientID);

		// Subscribe to the reply topic
		MqttSubscription subscription = new MqttSubscription(replyTopic);
		System.out.println("subscribing to: " + replyTopic);
		client.subscribe(subscription).waitForCompletion();

		// Publish the request to the request topic
		byte[] content = mapper.writeValueAsBytes(handler.getRequest());
		MqttMessage message = new MqttMessage(content);
		MqttProperties properties = new MqttProperties();
		properties.setResponseTopic(replyTopic);
		properties.setCorrelationData(UUID.randomUUID().toString().getBytes());
		message.setProperties(properties);
		message.setQos(qos);
		message.setRetained(false);

		System.out.printf(String.format("Publishing: %s to topic: %s with qos: %d\n", new String(content), config.rpcRequestTopic, qos));
		System.out.printf(String.format("    replyTopic: %s\n", replyTopic));
		client.publish(config.rpcRequestTopic, message).waitForCompletion();
		System.out.println("Message published");

		System.out.printf("Wait for the reply to arrive\n");
		TimeUnit.SECONDS.sleep(5);

		// Disconnect
		client.disconnect();
		System.out.printf("Client %s disconnected\n", clientID);
	}
}
