package com.hand.basicObject;


import lombok.Data;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import okhttp3.Response;
/**
 * @author Administrator
 */

@Data
public class APIResponse {
	private  int statusCode;
	private  String body;
	public APIResponse() {

	}
}
