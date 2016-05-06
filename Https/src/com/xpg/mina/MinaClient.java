package com.xpg.mina;

import com.xpg.utils.HttpClientUtil;
import com.xpg.utils.HttpURLConnectionUtil;

public class MinaClient {

	public static void main(String[] args) throws Exception {
//		System.out.println(getTokenJson("https://localhost"));
		String url = "https://10.40.11.243";
//		String url = "https://www.baidu.com";
		System.out.println(HttpClientUtil.httpPost(url,"{\"openId\":\"1\"}"));
//		System.out.println(HttpClientUtil.httpGet(url)); 
//		System.out.println(HttpURLConnectionUtil.postTest(url));
	}
}
