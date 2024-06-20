package com.rsmaxwell.diary.request.requests;

import java.util.Map;

import com.rsmaxwell.diary.utils.Request;
import com.rsmaxwell.diary.utils.Utilities;

public class Calculator extends RpcRequest {

	public Calculator(String operation, int param1, int param2) {
		request = new Request("calculator");
		request.put("operation", operation);
		request.put("param1", param1);
		request.put("param2", param2);
		setRequest(request);
	}

	@Override
	public void handle(Map<String, Object> reply) throws Exception {
		int result = Utilities.getIntegerFromMap("result", reply);
		System.out.printf("result: %d\n", result);
	}
}
