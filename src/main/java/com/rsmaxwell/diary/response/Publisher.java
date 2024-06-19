package com.rsmaxwell.diary.response;

import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttClientPersistence;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MqttDefaultFilePersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;

import com.rsmaxwell.diary.utils.Adapter;

public class Publisher {

	static String clientID = "publisher";

	private ClientConfig config;

	public Publisher(ClientConfig value) {
		config = value;
		config.connOpts.setCleanStart(true);
	}

	public void publish() throws MqttException {

		String broker = config.serverUrls.get(0);
		MqttClientPersistence persistence = new MqttDefaultFilePersistence();
		String content = "[ \"one\", \"two\", \"three\" ]";
		String topic = "diaries/pages";
		int qos = 0;

		MqttAsyncClient client = new MqttAsyncClient(broker, clientID, persistence);
		MqttConnectionOptions connOpts = config.connOpts;

		Adapter adapter = new Adapter();
		client.setCallback(adapter);

		System.out.printf(String.format("Connecting to broker: %s as '%s'\n", broker, clientID));
		client.connect(connOpts).waitForCompletion();

		System.out.printf(String.format("Publishing: %s to topic: %s with qos: %d\n", content, topic, qos));
		MqttMessage message = new MqttMessage(content.getBytes());
		message.setQos(qos);
		client.publish(topic, message).waitForCompletion();

		System.out.printf("Disconnecting clientid: %s\n", clientID);
		client.disconnect();
	}

}
