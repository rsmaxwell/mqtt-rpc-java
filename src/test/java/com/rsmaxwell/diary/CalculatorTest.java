package com.rsmaxwell.diary.request;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import com.rsmaxwell.diary.request.handlers.CalculatorHandler;
import com.rsmaxwell.diary.request.handlers.RequestResponse;
import com.rsmaxwell.diary.response.ClientConfig;

public class Calculator {

	static int qos = 0;
	static volatile boolean keepRunning = true;

	public static void main(String[] args) throws Exception {

		Option serverOption = createOption("s", "server", "mqtt server", "URL of MQTT server", false);
		Option topicOption = createOption("t", "requestTopic", "request topic", "topic name for requests", false);
		Option usernameOption = createOption("u", "username", "Username", "Username for the MQTT server", true);
		Option passwordOption = createOption("p", "password", "Password", "Password for the MQTT server", true);
		Option operationOption = createOption("o", "operation", "Operation", "Operation ( mul/add/sub/div )", true);
		Option param1Option = createOption("a", "param1", "Param1", "Parameter 1", true);
		Option param2Option = createOption("b", "param2", "Param2", "Parameter 2", true);

		// @formatter:off
		Options options = new Options();
		options.addOption(serverOption)
			   .addOption(topicOption)
			   .addOption(usernameOption)
			   .addOption(passwordOption)
			   .addOption(operationOption)
			   .addOption(param1Option)
			   .addOption(param2Option);
		// @formatter:on

		CommandLineParser commandLineParser = new DefaultParser();
		CommandLine commandLine = commandLineParser.parse(options, args);
		String server = commandLine.hasOption("h") ? commandLine.getOptionValue(serverOption) : "tcp://127.0.0.1:1883";
		String topic = commandLine.hasOption("t") ? commandLine.getOptionValue(topicOption) : "request";
		String username = commandLine.getOptionValue(usernameOption);
		String password = commandLine.getOptionValue(passwordOption);
		String operation = commandLine.getOptionValue(operationOption);
		String A = commandLine.getOptionValue(param1Option);
		String B = commandLine.getOptionValue(param2Option);

		int param1 = Integer.parseInt(A);
		int param2 = Integer.parseInt(B);

		ClientConfig config = new ClientConfig(server, topic, username, password);

		RemoteProcedureCall rpc = new RemoteProcedureCall(config);
		RequestResponse handler = new CalculatorHandler(operation, param1, param2);

		rpc.performRequest(handler);

		System.out.println("exiting");
	}

	static Option createOption(String shortName, String longName, String argName, String description, boolean required) {
		return Option.builder(shortName).longOpt(longName).argName(argName).desc(description).hasArg().required(required).build();
	}
}
