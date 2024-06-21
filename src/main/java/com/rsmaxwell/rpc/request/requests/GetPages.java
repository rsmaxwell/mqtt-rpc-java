package com.rsmaxwell.rpc.request.requests;

import com.rsmaxwell.rpc.utils.Request;
import com.rsmaxwell.rpc.utils.Response;

public class GetPages extends RpcRequest {

	public GetPages() {
		request = new Request("getPages");
		setRequest(request);
	}

	@Override
	public void handle(Response response) throws Exception {
		String result = Response.getStringFromMap("result", response);
		System.out.printf("result: %s\n", result);
	}
}
