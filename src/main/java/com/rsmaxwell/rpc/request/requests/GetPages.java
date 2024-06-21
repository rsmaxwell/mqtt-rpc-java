package com.rsmaxwell.rpc.request.requests;

import com.rsmaxwell.rpc.utils.Request;
import com.rsmaxwell.rpc.utils.Response;

public class GetPages extends RpcRequest {

	public GetPages() {
		Request request = new Request("getPages");
		setRequest(request);
	}

	@Override
	public void handle(Response response) throws Exception {
		String result = response.getString("result");
		System.out.printf("result: %s\n", result);
	}
}
