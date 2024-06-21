package com.rsmaxwell.rpc.response.handlers;

import java.util.Map;

import com.rsmaxwell.rpc.utils.Response;

public class GetPages extends RequestHandler {

	public Response handleRequest(Map<String, Object> args) throws Exception {
		System.out.println("getPages: handleRequest");
		return success("[ 'one', 'two', 'three' ]");
	}
}
