package com.rsmaxwell.rpc.response.handler;

import java.util.Map;

public interface RequestHandler {

	Map<String, Object> handleRequest(Map<String, Object> args) throws Exception;
}
