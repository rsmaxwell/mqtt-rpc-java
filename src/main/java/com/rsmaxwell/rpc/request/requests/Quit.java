package com.rsmaxwell.rpc.request.requests;

import java.util.Map;

import com.rsmaxwell.rpc.utils.Request;

public class Quit extends RpcRequest {

	public Quit() {
		request = new Request("quit");
		setRequest(request);
	}

	@Override
	public void handle(Map<String, Object> reply) throws Exception {
		System.out.println("Quit.handle");
	}
}
