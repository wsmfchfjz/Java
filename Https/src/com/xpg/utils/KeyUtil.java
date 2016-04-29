package com.xpg.utils;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class KeyUtil {

	private final static String KST_URL = "./src/allPay.keystore";
	private final static String KST_PASSWORD = "ap_kst_go4xpg";
	private final static String CST_PASSWORD = "ap_crt_go4xpg";

	private final static String TST_URL = "./src/allPay.truststore";
	private final static String TST_PASSWORD = "ap_tst_go4xpg";

	public static SSLContext getServerSSLContext() throws Exception {

		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

		keyStore.load(new FileInputStream(KST_URL), KST_PASSWORD.toCharArray());

		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
				.getDefaultAlgorithm());
		kmf.init(keyStore, CST_PASSWORD.toCharArray());

		SSLContext context = SSLContext.getInstance("SSL");
		context.init(kmf.getKeyManagers(), null, new SecureRandom());
		return context;
	}
	
	public static SSLContext getClientSSLContext() throws Exception {

		SSLContext context = SSLContext.getInstance("SSL");

		context.init(null, new TrustManager[] { new MyX509TrustManager() },
				new SecureRandom());

		return context;
	}

	private static class MyX509TrustManager implements X509TrustManager {
		/*
		 * The default X509TrustManager returned by SunX509. We'll delegate
		 * decisions to it, and fall back to the logic in this class if the
		 * default X509TrustManager doesn't trust it.
		 */
		X509TrustManager sunJSSEX509TrustManager;

		MyX509TrustManager() throws Exception {
			// create a "default" JSSE X509TrustManager.
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream(TST_URL), TST_PASSWORD.toCharArray());
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(
					"SunX509", "SunJSSE");
			tmf.init(ks);
			TrustManager tms[] = tmf.getTrustManagers();
			/*
			 * Iterate over the returned trustmanagers, look for an instance of
			 * X509TrustManager. If found, use that as our "default" trust
			 * manager.
			 */
			for (int i = 0; i < tms.length; i++) {
				if (tms[i] instanceof X509TrustManager) {
					sunJSSEX509TrustManager = (X509TrustManager) tms[i];
					return;
				}
			}
			/*
			 * Find some other way to initialize, or else we have to fail the
			 * constructor.
			 */
			throw new Exception("Couldn't initialize");
		}

		/*
		 * Delegate to the default trust manager.
		 */
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			try {
				sunJSSEX509TrustManager.checkClientTrusted(chain, authType);
			} catch (CertificateException excep) {
				// do any special handling here, or rethrow exception.
			}
		}

		/*
		 * Delegate to the default trust manager.
		 */
		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			try {
				sunJSSEX509TrustManager.checkServerTrusted(chain, authType);
			} catch (CertificateException excep) {
				/*
				 * Possibly pop up a dialog box asking whether to trust the cert
				 * chain.
				 */
			}
		}

		/*
		 * Merely pass this through.
		 */
		public X509Certificate[] getAcceptedIssuers() {
			return sunJSSEX509TrustManager.getAcceptedIssuers();
		}
	}

}
