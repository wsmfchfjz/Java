package com.xpg.mina;

import java.net.HttpURLConnection;

import com.xpg.utils.HttpURLConnectionUtil;
import com.xpg.utils.HttpURLConnectionUtil.RequestType;

public class MinaClient {

	public static void main(String[] args) {
		System.out.println(getTokenJson("https://localhost"));
	}
	
	private static String getTokenJson(String urlStr){
		String tokenJson = null;
		try {
			HttpURLConnection connection = HttpURLConnectionUtil.getInstance().getConnection(RequestType.POST, urlStr);
			
			connection.setRequestProperty("Accept-Charset", "utf-8");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Type", "application/json");
			
			connection.setConnectTimeout(8000);
			connection.setReadTimeout(8000);
			connection.connect();
			
			tokenJson = HttpURLConnectionUtil.getInstance().request(RequestType.POST, urlStr, "{\"openId\":\"1\"}\n", connection);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tokenJson;
	}
}
