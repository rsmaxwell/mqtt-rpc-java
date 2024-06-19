package com.rsmaxwell.diary.request.handlers;

import java.util.Map;

public interface RequestHandler {

	Map<String, Object> handleRequest(Map<String, Object> args) throws Exception;
}
