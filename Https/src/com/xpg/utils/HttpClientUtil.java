package com.xpg.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

/**
 * 只支持 Content-type = application/json; charset=UTF-8;
 * jeff - 16/05/05
 */
public class HttpClientUtil {

	private static final String CONTENT_TYPE = "application/json; charset=UTF-8";
	
	public static String post(String url, String body, Map<String, String> headers) {
		HttpClient httpClient = getHttpClient(url);
		if(httpClient == null){
			return "HttpClient init fail";
		}
		HttpPost httpPost = new HttpPost(url);
		if(headers != null){
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
		if(httpClient == null){
			return "HttpClient init fail";
		}
		HttpGet httpGet = new HttpGet(url);
		if(headers != null){
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
	
	public static String delete(String url, Map<String, String> headers) {
		HttpClient httpClient = getHttpClient(url);
		if(httpClient == null){
			return "HttpClient init fail";
		}
		HttpDelete httpDelet = new HttpDelete(url);
		if(headers != null){
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpDelet.setHeader(entry.getKey(), entry.getValue());
			}
		}
		String responseContent = "";
		try {
			HttpResponse httpResponse = httpClient.execute(httpDelet);
			responseContent = EntityUtils.toString(httpResponse.getEntity(),
					"UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpDelet.releaseConnection();
		}
		return responseContent;
	}
	
	
	
	public static String httpPost(String url, String body) {
		return post(url, body, null);
	}

	public static String httpGet(String url) {
		return get(url, null);
	}
	
	@SuppressWarnings("deprecation")
	private static HttpClient getHttpClient(String url){
		HttpClient httpClient = null;
		try {
			if (new URL(url).getProtocol().toUpperCase().equals("HTTPS")) {
				// 相信自己的CA和所有自签名的证书
				SSLContext sslcontext = SSLContexts
						.custom()
						.loadTrustMaterial(KeyUtil.getClientTrustStore(),
								new TrustSelfSignedStrategy()).build();//将KeyUtil.getClientTrustStore()改成null，则信任任何证书
				// 只允许使用TLSv1协议
				SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
						sslcontext,
						new String[] { "TLSv1" },
						null,
						SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
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

}
