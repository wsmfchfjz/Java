package com.xpg.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;


@SuppressWarnings("deprecation")
public class HttpClientUtil {
	
	public static final String CONTENT_TYPE = "application/json; charset=UTF-8";
	
    /**
     * 该post方法没有设置header
     */
    public static String post(String url, String body) {
        PostMethod post = new PostMethod(url);
        post.setRequestBody(body);
        // 指定请求内容的类型
        post.setRequestHeader("Content-type", CONTENT_TYPE);
        HttpClient httpclient = new HttpClient();// 创建 HttpClient 的实例
        String res;
        try {
            httpclient.executeMethod(post);
            res = post.getResponseBodyAsString();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        post.releaseConnection();// 释放连接
        return res;
    }
    
    /**
     * 该post方法没有设置header
     */
    public static String httpsPost(String url, String body) {
        PostMethod post = new PostMethod(url);
        post.setRequestBody(body);
        // 指定请求内容的类型
        post.setRequestHeader("Content-type", CONTENT_TYPE);
        HttpClient httpclient = new HttpClient();// 创建 HttpClient 的实例
        String res;
        try {
            httpclient.executeMethod(post);
            res = post.getResponseBodyAsString();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        post.releaseConnection();// 释放连接
        return res;
    }
    
//    public class WebClientDevWrapper {
//    	 
//        public HttpClient wrapClient(HttpClient base) throws Exception {
//            SSLContext ctx = SSLContext.getInstance("TLS");
//            X509TrustManager tm = new X509TrustManager() {
//                @Override
//                public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
//                }
//     
//                @Override
//                public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
//                }
//     
//                @Override
//                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                    return new java.security.cert.X509Certificate[0];
//                }
//            };
//            ctx.init(null, new TrustManager[]{tm}, null);
//            
//    		SSLSocketFactory ssf = new SSLSocketFactory(ctx);
//            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//            
//            ClientConnectionManager ccm = base.getConnectionManager();
//            SchemeRegistry sr = ccm.getSchemeRegistry();
//            sr.register(new Scheme("https", ssf, 443));
//            return new DefaultHttpClient(ccm, base.getParams());
//        }
//    }
    
}
