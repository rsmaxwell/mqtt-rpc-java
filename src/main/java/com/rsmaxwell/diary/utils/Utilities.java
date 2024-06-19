package com.rsmaxwell.diary.utils;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public abstract class Utilities {

	public static boolean isPresent(String key, Map<String, Object> args) {

		Object obj = args.get(key);
		if (obj == null) {
			return false;
		}

		return true;
	}

	public static String getStringFromMap(String key, Map<String, Object> args) throws Exception {

		boolean present = isPresent(key, args);
		if (!present) {
			throw new Exception(String.format("could not find the key [%s]", key));
		}

		Object obj = args.get(key);
		if ((obj instanceof String) == false) {
			throw new Exception(String.format("unexpected type for key: %s, %s", key, obj.getClass().getSimpleName()));
		}

		return obj.toString();
	}

	public static Integer getIntegerFromMap(String key, Map<String, Object> args) throws Exception {

		boolean present = isPresent(key, args);
		if (!present) {
			throw new Exception(String.format("could not find the key [%s]", key));
		}

		Object obj = args.get(key);
		if ((obj instanceof Integer) == false) {
			throw new Exception(String.format("unexpected type for key: %s, %s", key, obj.getClass().getSimpleName()));
		}

		return (Integer) obj;
	}

	public static Boolean getBooleanFromMap(String key, Map<String, Object> args) throws Exception {

		boolean present = isPresent(key, args);
		if (!present) {
			throw new Exception(String.format("could not find the key [%s]", key));
		}

		Object obj = args.get(key);
		if ((obj instanceof Boolean) == false) {
			throw new Exception(String.format("unexpected type for key: %s, %s", key, obj.getClass().getSimpleName()));
		}

		return (Boolean) obj;
	}

	public static Map<String, Object> success(Object value) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", HttpURLConnection.HTTP_OK);
		result.put("result", value);
		return result;
	}

	public static Map<String, Object> quit() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", HttpURLConnection.HTTP_OK);
		result.put("keepRunning", false);
		return result;
	}

	public static Map<String, Object> badRequest(String message) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", HttpURLConnection.HTTP_BAD_REQUEST);
		result.put("message", message);
		return result;
	}
}
