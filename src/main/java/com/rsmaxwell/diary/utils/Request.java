package com.rsmaxwell.diary.utils;

import java.util.HashMap;
import java.util.Map;

public class Request {

	String function;
	public Map<String, Object> args = new HashMap<String, Object>();

	public Request() {
	}

	public Request(String function) {
		this.function = function;
	}

	public Object put(String key, Object value) {
		return args.put(key, value);
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public Map<String, Object> getArgs() {
		return args;
	}

	public void setArgs(Map<String, Object> args) {
		this.args = args;
	}
}
