package com.rupjit.qaRestAssured.utils;

import io.restassured.path.json.JsonPath;

public class JsonParserUtil {

	public static Object getjsonPathValue(String jsonObjectAsString,String path) {
		JsonPath  jp=new JsonPath(jsonObjectAsString);
		Object value=jp.getString(path);
		return value;
		
	}
}
