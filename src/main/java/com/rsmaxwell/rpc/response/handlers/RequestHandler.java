package com.rsmaxwell.rpc.response.handlers;

import java.net.HttpURLConnection;
import java.util.Map;

import com.rsmaxwell.rpc.utils.Response;

public abstract class RequestHandler {

	public abstract Response handleRequest(Map<String, Object> args) throws Exception;

	public static Response success(Object value) {
		Response result = new Response();
		result.put("code", HttpURLConnection.HTTP_OK);
		result.put("result", value);
		return result;
	}

	public static Response quit() {
		Response result = new Response();
		result.put("code", HttpURLConnection.HTTP_OK);
		result.put("keepRunning", false);
		return result;
	}

	public static Response badRequest(String message) {
		Response result = new Response();
		result.put("code", HttpURLConnection.HTTP_BAD_REQUEST);
		result.put("message", message);
		return result;
	}

	public static Response internalError(String message) {
		Response result = new Response();
		result.put("code", HttpURLConnection.HTTP_INTERNAL_ERROR);
		result.put("message", message);
		return result;
	}
}
