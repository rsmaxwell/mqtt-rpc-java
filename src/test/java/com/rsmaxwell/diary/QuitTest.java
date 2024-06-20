package com.rsmaxwell.diary;

import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttClientPersistence;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MqttDefaultFilePersistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsmaxwell.diary.request.HandlerOptions;
import com.rsmaxwell.diary.request.PublishOptions;
import com.rsmaxwell.diary.request.RemoteProcedureCall;
import com.rsmaxwell.diary.request.requests.Quit;
import com.rsmaxwell.diary.request.requests.RpcRequest;
import com.rsmaxwell.diary.utils.Token;

public class QuitTest {

	static int qos = 0;
	static volatile boolean keepRunning = true;

	static private ObjectMapper mapper = new ObjectMapper();

	public static void main(String[] args) throws Exception {

		Option serverOption = createOption("s", "server", "mqtt server", "URL of MQTT server", false);
		Option usernameOption = createOption("u", "username", "Username", "Username for the MQTT server", true);
		Option passwordOption = createOption("p", "password", "Password", "Password for the MQTT server", true);

		// @formatter:off
		Options options = new Options();
		options.addOption(serverOption)
			   .addOption(usernameOption)
			   .addOption(passwordOption);
		// @formatter:on

		CommandLineParser commandLineParser = new DefaultParser();
		CommandLine commandLine = commandLineParser.parse(options, args);
		String server = commandLine.hasOption("h") ? commandLine.getOptionValue(serverOption) : "tcp://127.0.0.1:1883";
		String username = commandLine.getOptionValue(usernameOption);
		String password = commandLine.getOptionValue(passwordOption);

		String clientID = "requester";
		String requestTopic = "request";
		int qos = 0;

		MqttClientPersistence persistence = new MqttDefaultFilePersistence();
		MqttAsyncClient client = new MqttAsyncClient(server, clientID, persistence);
		MqttConnectionOptions connOpts = new MqttConnectionOptions();
		connOpts.setUserName(username);
		connOpts.setPassword(password.getBytes());

		HandlerOptions handlerOptions = new HandlerOptions(client, "response/%s", clientID);
		RemoteProcedureCall h = new RemoteProcedureCall(handlerOptions);

		client.setCallback(h.getAdapter());

		// Connect
		System.out.printf("Connecting to broker: %s as '%s'\n", server, clientID);
		client.connect(connOpts).waitForCompletion();
		System.out.printf("Client %s connected\n", clientID);

		// Subscribe to the responseTopic
		h.subscribe();

		RpcRequest handler = new Quit();
		byte[] request = mapper.writeValueAsBytes(handler.getRequest());
		PublishOptions publishOptions = new PublishOptions(requestTopic, request);
		Token token = h.request(publishOptions);

		// Wait for the response to arrive
		Map<String, Object> response = h.waitForResponse(token);
		handler.handle(response);

		// Disconnect
		client.disconnect();
		System.out.printf("Client %s disconnected\n", clientID);

		System.out.println("exiting");
	}

	static Option createOption(String shortName, String longName, String argName, String description, boolean required) {
		return Option.builder(shortName).longOpt(longName).argName(argName).desc(description).hasArg().required(required).build();
	}
}
