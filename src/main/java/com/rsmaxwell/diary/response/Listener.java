package com.rsmaxwell.diary.response;

import java.util.HashMap;

import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttClientPersistence;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MqttDefaultFilePersistence;
import org.eclipse.paho.mqttv5.common.MqttSubscription;

import com.rsmaxwell.diary.request.handlers.RequestHandler;
import com.rsmaxwell.diary.response.handler.Calculator;
import com.rsmaxwell.diary.response.handler.GetPages;
import com.rsmaxwell.diary.response.handler.Quit;

public class Listener implements AutoCloseable {

	static String clientID_responder = "responder";
	static String clientID_subscriber = "listener";

	private ClientConfig config_responder;
	private ClientConfig config_subscriber;
	private MqttAsyncClient client_responder;
	private MqttAsyncClient client_subscriber;
	private MessageHandler messageHandler;

	public Listener(ClientConfig value) throws Exception {

		if (value.serverUrls.size() <= 0) {
			throw new Exception("No brokers have been specified");
		}

		config_responder = value;
		config_responder.connOpts.setCleanStart(true);

		config_subscriber = value;
		config_subscriber.connOpts.setCleanStart(true);

		HashMap<String, RequestHandler> handlers = new HashMap<String, RequestHandler>();
		handlers.put("calculator", new Calculator());
		handlers.put("getPages", new GetPages());
		handlers.put("quit", new Quit());

		messageHandler = new MessageHandler(handlers);
	}

	public void start() throws Exception {

		String broker = config_responder.serverUrls.get(0);
		MqttClientPersistence persistence = new MqttDefaultFilePersistence();

		client_responder = new MqttAsyncClient(broker, clientID_responder, persistence);
		client_subscriber = new MqttAsyncClient(broker, clientID_subscriber, persistence);

		messageHandler.setClient(client_responder);
		client_subscriber.setCallback(messageHandler);

		System.out.printf(String.format("Connecting to broker: %s as '%s'\n", broker, clientID_responder));
		MqttConnectionOptions connOpts_responder = config_responder.connOpts;
		client_responder.connect(connOpts_responder).waitForCompletion();

		System.out.printf(String.format("Connecting to broker: %s as '%s'\n", broker, clientID_subscriber));
		MqttConnectionOptions connOpts_subscriber = config_subscriber.connOpts;
		client_subscriber.connect(connOpts_subscriber).waitForCompletion();

		System.out.printf(String.format("subscribing to: %s\n", config_subscriber.rpcRequestTopic));
		MqttSubscription subscription = new MqttSubscription(config_subscriber.rpcRequestTopic);
		client_subscriber.subscribe(subscription).waitForCompletion();
	}

	public void close() throws Exception {
		System.out.println("Listener.close()");
		client_responder.disconnect();
		client_subscriber.disconnect();
		System.out.printf("Client %s Disconnected\n", clientID_subscriber);
	}

	public boolean keepRunning() {
		return messageHandler.keepRunning();
	}
}
