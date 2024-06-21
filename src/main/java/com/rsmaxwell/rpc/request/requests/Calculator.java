package com.rsmaxwell.rpc.request.requests;

import com.rsmaxwell.rpc.utils.Request;
import com.rsmaxwell.rpc.utils.Response;

public class Calculator extends RpcRequest {

	public Calculator(String operation, int param1, int param2) {
		Request request = new Request("calculator");
		request.put("operation", operation);
		request.put("param1", param1);
		request.put("param2", param2);
		setRequest(request);
	}

	@Override
	public void handle(Response response) throws Exception {
		int result = response.getInteger("result");
		System.out.printf("result: %d\n", result);
	}
}
