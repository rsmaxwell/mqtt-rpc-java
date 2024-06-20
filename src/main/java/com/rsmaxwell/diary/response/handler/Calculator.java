package com.rsmaxwell.diary.response.handler;

import java.util.Map;

import com.rsmaxwell.diary.utils.Utilities;

public class Calculator implements RequestHandler {

	@Override
	public Map<String, Object> handleRequest(Map<String, Object> args) throws Exception {
		System.out.println("calculator.handleRequest");

		try {
			String operation = Utilities.getStringFromMap("operation", args);
			int param1 = Utilities.getIntegerFromMap("param1", args);
			int param2 = Utilities.getIntegerFromMap("param2", args);

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

			return Utilities.success(value);
		} catch (Exception e) {

			return Utilities.badRequest(e.getMessage());
		}
	}
}
