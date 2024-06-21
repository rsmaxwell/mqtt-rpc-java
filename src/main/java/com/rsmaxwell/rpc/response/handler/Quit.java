package com.rsmaxwell.rpc.response.handler;

import java.util.Map;

import com.rsmaxwell.rpc.utils.Response;

public class Quit extends RequestHandler {

	public Response handleRequest(Map<String, Object> args) throws Exception {
		System.out.println("quit.handleRequest");
		return quit();
	}
}
