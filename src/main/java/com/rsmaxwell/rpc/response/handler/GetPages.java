package com.rsmaxwell.rpc.response.handler;

import java.util.Map;

import com.rsmaxwell.rpc.utils.Utilities;

public class GetPages implements RequestHandler {

	public Map<String, Object> handleRequest(Map<String, Object> args) throws Exception {
		System.out.println("getPages: handle");
		return Utilities.success("[ 'one', 'two', 'three' ]");
	}
}
