package com.xpg.mina;

import com.xpg.utils.HttpUtil;
import com.xpg.utils.HttpURLConnectionUtil;

public class MinaClient {

	public static void main(String[] args) throws Exception {
//		System.out.println(getTokenJson("https://localhost"));
		String url = "https://10.40.11.243";
//		String url = "https://www.baidu.com";
		System.out.println(HttpUtil.httpPost(url,"{\"openId\":\"1啊\"}\n\n"));
//		System.out.println(HttpUtil.httpGet(url)); 
//		System.out.println(HttpURLConnectionUtil.postTest(url));
	}
}
