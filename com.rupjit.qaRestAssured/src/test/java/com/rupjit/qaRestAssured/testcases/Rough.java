package com.rupjit.qaRestAssured.testcases;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.rupjit.qaRestAssured.utils.JsonParserUtil;
import com.rupjit.qaRestAssured.utils.PayloadBuilder;
import com.rupjit.qaRestAssured.utils.RestUtils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Rough {

	@Test
	public static void test1() throws MalformedURLException {
		URL baseUrl=new URL("https://restful-booker.herokuapp.com/");
		String basePath="booking";
		HashMap<String,String> headers=new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		String payload="{\"firstname\" : \"Jim\",\r\n" + 
				"    \"lastname\" : \"Brown\",\r\n" + 
				"    \"totalprice\" : 111,\r\n" + 
				"    \"depositpaid\" : true,\r\n" + 
				"    \"bookingdates\" : {\r\n" + 
				"        \"checkin\" : \"2018-01-01\",\r\n" + 
				"        \"checkout\" : \"2019-01-01\"\r\n" + 
				"    },\r\n" + 
				"    \"additionalneeds\" : \"Breakfast\"}";
		Response response=RestUtils.callPostMethod(baseUrl, basePath,headers, payload);
		System.out.println("Response is = "+response.asString());
		
		//validate return code
		response.then().statusCode(200);
		String responseObj=response.asString();
		String value=(String) JsonParserUtil.getjsonPathValue(responseObj, "booking.firstname");
		System.out.println("value="+value);
	}
	
	@Test
	public static void test2() {
		String json="{\"employees\":[    \r\n" + 
				"    {\"name\":\"Ram\", \"email\":\"ram@gmail.com\", \"age\":23},    \r\n" + 
				"    {\"name\":\"Shyam\", \"email\":\"shyam23@gmail.com\", \"age\":28},  \r\n" + 
				"    {\"name\":\"John\", \"email\":\"john@gmail.com\", \"age\":33},    \r\n" + 
				"    {\"name\":\"Bob\", \"email\":\"bob32@gmail.com\", \"age\":41}   \r\n" + 
				"]}";
		
		String value=(String) JsonParserUtil.getjsonPathValue(json,"employees[1].name");
		System.out.println("value="+value);
	}
	
	@Test
	public static void test3() {
		String json="{\"employees\":[    \r\n" + 
				"    {\"name\":\"Ram\", \"email\":\"ram@gmail.com\", \"age\":23},    \r\n" + 
				"    {\"name\":\"Shyam\", \"email\":\"shyam23@gmail.com\", \"age\":28},  \r\n" + 
				"    {\"name\":\"John\", \"email\":\"john@gmail.com\", \"age\":33},    \r\n" + 
				"    {\"name\":\"Bob\", \"email\":\"bob32@gmail.com\", \"age\":41}   \r\n" + 
				"]}";
		PayloadBuilder pb=new PayloadBuilder(json);
		String abc=pb.getPayload();
		System.out.println(abc);
		System.out.println(pb.get("/employees/2/name"));
		pb.addOrUpdate("/employees/4", "{\"name\":\"test4\",\"email\":\"test4@gmail.com\",\"age\":23}");
		System.out.println(pb.getPayload());
	}
}
