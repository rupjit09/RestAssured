package com.rupjit.qaRestAssured.utils;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestUtils {

	public static Response callPostMethod(URL baseUrl,String basePath, HashMap<String,String> headers, String payload) {
		RequestSpecification reqSpec=RestAssured.given().log().all();
		reqSpec.baseUri(baseUrl.toString());
		reqSpec.basePath(basePath);
		reqSpec.headers(headers); 
		reqSpec.body(payload);
		
	//hit the rest call now
		Response response= reqSpec.post();
		return response;
	}
	
	public static Response callMultiPartPostMethod(URL baseUrl,String basePath, HashMap<String,String> headers, String payload,String multipartControlName,File multiPartDataFileLocation) {
		RequestSpecification reqSpec=RestAssured.given().log().all();
		reqSpec.baseUri(baseUrl.toString());
		reqSpec.basePath(basePath);
		reqSpec.headers(headers); 
		reqSpec.body(payload);
		reqSpec.multiPart(multipartControlName, multiPartDataFileLocation);
		
	//hit the rest call now
		Response response= reqSpec.post();
		return response;
	}
	
	public static Response callGetMethod(URL baseUrl,String basePath, HashMap<String,String> headers) {
		RequestSpecification reqSpec=RestAssured.given().log().all();
		reqSpec.baseUri(baseUrl.toString());
		reqSpec.basePath(basePath);
		reqSpec.headers(headers);
		Response response=reqSpec.get();
		return response;
		
	}
	
	public static Response callPutMethod(URL baseUrl,String basePath,HashMap<String,String> headers,String payload) {
		RequestSpecification reqSpec=RestAssured.given().log().all();
		reqSpec.baseUri(baseUrl.toString());
		reqSpec.basePath(basePath);
		reqSpec.headers(headers);
		reqSpec.body(payload);
		
		Response response=reqSpec.put();
		return response;
		
	}
	
	public static Response callDeleteMethod(URL baseUrl,String basePath,HashMap<String,String> headers,String payload) {
		RequestSpecification reqSpec=RestAssured.given().log().all();
		reqSpec.baseUri(baseUrl.toString());
		reqSpec.basePath(basePath);
		reqSpec.headers(headers);
		reqSpec.body(payload);
		
		Response response=reqSpec.delete();
		return response;
		
	}
	
}
