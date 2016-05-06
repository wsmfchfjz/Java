package com.xpg.utils;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

/**
 * jeff - 16/05/05
 */
public class HttpUtil {

	public static String post(String url, String body,
			Map<String, String> headers) {
		HttpClient httpClient = getHttpClient(url);
		if (httpClient == null) {
			return "HttpClient init fail";
		}
		HttpPost httpPost = new HttpPost(url);
		if (headers != null) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpPost.setHeader(entry.getKey(), entry.getValue());
			}
		}
		String responseContent = "";
		try {
			StringEntity entity = new StringEntity(body, "utf-8");
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			httpPost.setEntity(entity);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			responseContent = EntityUtils.toString(httpResponse.getEntity(),
					"UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpPost.releaseConnection();
		}
		return responseContent;
	}

	public static String get(String url, Map<String, String> headers) {
		HttpClient httpClient = getHttpClient(url);
		if (httpClient == null) {
			return "HttpClient init fail";
		}
		HttpGet httpGet = new HttpGet(url);
		if (headers != null) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpGet.setHeader(entry.getKey(), entry.getValue());
			}
		}
		String responseContent = "";
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			responseContent = EntityUtils.toString(httpResponse.getEntity(),
					"UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpGet.releaseConnection();
		}
		return responseContent;
	}

	public static String httpPost(String url, String body) {
		return post(url, body, null);
	}

	public static String httpGet(String url) {
		return get(url, null);
	}

	private static HttpClient getHttpClient(String url) {
		HttpClient httpClient = null;
		try {
			String protocol = new URL(url).getProtocol();
			if (protocol != null && protocol.toUpperCase().equals("HTTPS")) {
				// 相信自己的CA和所有自签名的证书
				SSLContext sslcontext = SSLContexts
						.custom()
						.loadTrustMaterial(KeyUtil.getClientTrustStore(),
								new TrustSelfSignedStrategy()).build();// 将KeyUtil.getClientTrustStore()改成null，则信任任何证书
				SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
						sslcontext, DO_NOT_VERIFY);// new String[] { "TLSv1" }则只允许使用TLSv1协议
				httpClient = HttpClients.custom().setSSLSocketFactory(sslsf)
						.build();
			} else {
				httpClient = HttpClientBuilder.create().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return httpClient;
	}

	public final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			System.out.println("访问服务器的ip地址为" + hostname);// 10.40.11.243
			return true;
		}
	};

	public static String delete(String url, Map<String, String> headers) {
		// TODO
		return null;
	}

	public static String delete(String url, String body,
			Map<String, String> headers) {
		// TODO
		return null;
	}

	public static String put(String url, Map<String, String> headers) {
		// TODO
		return null;
	}

	public static String put(String url, String body,
			Map<String, String> headers) {
		// TODO
		return null;
	}

}
