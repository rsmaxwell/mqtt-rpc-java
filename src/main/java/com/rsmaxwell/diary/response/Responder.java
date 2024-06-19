package com.rsmaxwell.diary.response;

import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class Responder {

	static int qos = 0;
	static volatile boolean keepRunning = true;

	public static void main(String[] args) throws Exception {

		Options options = new Options();
		Option serverOption = createOption("s", "server", "mqtt server", "URL of MQTT server", false);
		Option topicOption = createOption("t", "requestTopic", "request topic", "topic name for requests", false);
		Option usernameOption = createOption("u", "username", "Username", "Username for the MQTT server", true);
		Option passwordOption = createOption("p", "password", "Password", "Password for the MQTT server", true);
		options.addOption(serverOption).addOption(topicOption).addOption(usernameOption).addOption(passwordOption);

		CommandLineParser commandLineParser = new DefaultParser();
		CommandLine commandLine = commandLineParser.parse(options, args);
		String server = commandLine.hasOption("h") ? commandLine.getOptionValue(serverOption) : "tcp://127.0.0.1:1883";
		String topic = commandLine.hasOption("t") ? commandLine.getOptionValue(topicOption) : "request";
		String username = commandLine.getOptionValue(usernameOption);
		String password = commandLine.getOptionValue(passwordOption);

		ClientConfig config = new ClientConfig(server, topic, username, password);

		Publisher publisher = new Publisher(config);
		publisher.publish();

		try (Listener listener = new Listener(config)) {
			listener.start();

			// Wait till quit request received
			while (listener.keepRunning()) {
				TimeUnit.SECONDS.sleep(1);
			}
		}
		System.out.println("exiting");
	}

	static Option createOption(String shortName, String longName, String argName, String description, boolean required) {
		return Option.builder(shortName).longOpt(longName).argName(argName).desc(description).hasArg().required(required).build();
	}
}
