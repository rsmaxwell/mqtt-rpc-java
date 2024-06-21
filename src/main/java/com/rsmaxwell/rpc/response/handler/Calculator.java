package com.rsmaxwell.rpc.response.handler;

import java.util.Map;

import com.rsmaxwell.rpc.utils.Response;

public class Calculator extends RequestHandler {

	@Override
	public Response handleRequest(Map<String, Object> args) throws Exception {
		System.out.println("calculator.handleRequest");

		try {
			String operation = Response.getStringFromMap("operation", args);
			int param1 = Response.getIntegerFromMap("param1", args);
			int param2 = Response.getIntegerFromMap("param2", args);

			int value = 0;

			switch (operation) {
			case "add":
				value = param1 + param2;
			case "mul":
				value = param1 * param2;
			case "div":
				value = param1 / param2;
			case "sub":
				value = param1 - param2;
			}

			return success(value);
		} catch (Exception e) {

			return badRequest(e.getMessage());
		}
	}
}
