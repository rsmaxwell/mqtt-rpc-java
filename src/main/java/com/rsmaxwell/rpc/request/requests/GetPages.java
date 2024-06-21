package com.rsmaxwell.rpc.request.requests;

import java.util.Map;

import com.rsmaxwell.rpc.utils.Request;
import com.rsmaxwell.rpc.utils.Utilities;

public class GetPages extends RpcRequest {

	public GetPages() {
		request = new Request("getPages");
		setRequest(request);
	}

	@Override
	public void handle(Map<String, Object> reply) throws Exception {
		String result = Utilities.getStringFromMap("result", reply);
		System.out.printf("result: %s\n", result);
	}
}
