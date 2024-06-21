package com.rsmaxwell.rpc.request.requests;

import com.rsmaxwell.rpc.utils.Request;
import com.rsmaxwell.rpc.utils.Response;

public class Quit extends RpcRequest {

	public Quit() {
		Request request = new Request("quit");
		setRequest(request);
	}

	@Override
	public void handle(Response response) throws Exception {
		System.out.println("Quit.handle");
	}
}
