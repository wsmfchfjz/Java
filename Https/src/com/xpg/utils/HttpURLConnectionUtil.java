package com.xpg.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpURLConnectionUtil {

	private static HttpURLConnectionUtil httpUtil;

	public static HttpURLConnectionUtil getInstance() {
		if (httpUtil == null) {
			httpUtil = new HttpURLConnectionUtil();
		}
		return httpUtil;
	}

	private HttpURLConnectionUtil() {
	}

	public enum RequestType {
		GET, POST
	}

	/**
	 * 设置 head 云端服务器需要的参数
	 * 
	 * @param connection
	 * @throws ProtocolException
	 */
	public HttpURLConnection getConnection(RequestType type, String urlStr) throws Exception {
		URL url = new URL(urlStr);

		HttpURLConnection connection = null;

		// 关键代码
		// ignore https certificate validation |忽略 https 证书验证
		if (url.getProtocol().toUpperCase().equals("HTTPS")) {
//			trustAllHosts();
//			HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
//			https.setHostnameVerifier(DO_NOT_VERIFY);
//			connection = https;
			
			HttpsURLConnection httpsConn = (HttpsURLConnection)url.openConnection();
            //设置套接工厂 
//            httpsConn.setSSLSocketFactory(KeyUtil.getClientSSLContext().getSocketFactory());
            httpsConn.setHostnameVerifier(DO_NOT_VERIFY);
            connection = httpsConn;
		} else {
			connection = (HttpURLConnection) url.openConnection();
		}
		connection.setDoInput(true);
		if (type == RequestType.POST) {
			connection.setDoOutput(true);
		}
		connection.setRequestMethod(type.name());

		// connection.setRequestProperty("Content-Length", "3");
		
		return connection;
	}

	public static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		// Android use X509 cert
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			System.out.println(hostname);//10.40.11.243
			return true;
		}
	};

	/**
	 * http 请求
	 * 
	 * @param urlStr
	 * @return
	 */
	public String request(RequestType type, String urlStr, String data,HttpURLConnection connection) {
		StringBuffer result = null;
		BufferedReader reader = null;
		try {
			if (type == RequestType.POST) {
				writeData(connection, data);
			}
			// System.out.println(connection.getResponseCode());
			// reader = new BufferedReader(new InputStreamReader(connection
			// .getInputStream()));
			// String lines;
			// result = new StringBuffer();
			// while ((lines = reader.readLine()) != null) {
			// lines = new String(lines.getBytes(), "utf-8");
			// result.append(lines);
			// }
			int status = connection.getResponseCode();

			// InputStream is = connection.getInputStream();
			InputStream is = null;
			System.out.println("status:" + status);
			if (status >= 400)
				is = connection.getErrorStream();
			else
				is = connection.getInputStream();
			byte[] bytes = new byte[4096];
			int size = 0;
			result = new StringBuffer();
			while ((size = is.read(bytes)) > 0) {
				String str = new String(bytes, 0, size, "UTF-8");
				result.append(str);
			}
			is.close();
			// System.out.println("getResponseCode:"+connection.getResponseCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if (connection != null) {
					connection.disconnect();
				}
			} catch (IOException e) {
			}
		}

		return result != null ? result.toString() : null;
	}

	/**
	 * 写入 http post 数据
	 * 
	 * @param connection
	 * @param data
	 * @throws IOException
	 */
	private void writeData(HttpURLConnection connection, String data) throws IOException {
		DataOutputStream out = new DataOutputStream(connection.getOutputStream());
		out.writeBytes(data);
		out.flush();
		out.close();
	}
	
	public static String postTest(String urlStr){
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
