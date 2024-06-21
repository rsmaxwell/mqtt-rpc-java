package com.rsmaxwell.rpc.response.handler;

import java.util.Map;

import com.rsmaxwell.rpc.utils.Utilities;

public class Quit implements RequestHandler {

	public Map<String, Object> handleRequest(Map<String, Object> args) throws Exception {
		System.out.println("quit.handleRequest");
		return Utilities.quit();
	}
}
