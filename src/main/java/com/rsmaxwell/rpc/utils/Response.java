package com.rsmaxwell.rpc.utils;

import java.util.HashMap;

public class Response extends HashMap<String, Object> {

	public Response() {
		super();
	}

	public String getString(String key) throws Exception {
		return Utilities.getString(this, key);
	}

	public Integer getInteger(String key) throws Exception {
		return Utilities.getInteger(this, key);
	}

	public Boolean getBoolean(String key) throws Exception {
		return Utilities.getBoolean(this, key);
	}
}
