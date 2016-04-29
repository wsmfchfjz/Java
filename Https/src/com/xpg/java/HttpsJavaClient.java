package com.xpg.java;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Scanner;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import com.xpg.utils.KeyUtil;

public class HttpsJavaClient {

    private OutputStream output = null;
    private InputStream input = null;
    private SSLSocket socket = null;
    private String server_ip = "localhost";
    private int server_port = 10010;
    private int MAX_BUF_SIZE = 1024;

    public void sslSocket2() throws Exception {
    	SSLContext clientSSLContext = KeyUtil.getClientSSLContext();

        SSLSocketFactory factory = clientSSLContext.getSocketFactory();
        socket = (SSLSocket) factory.createSocket(server_ip, server_port);
        System.out.println("connect server success!");

        output = socket.getOutputStream();
        input = socket.getInputStream();

        output.write("test".getBytes());
        System.out.println("sent: test");
        output.flush();

        byte[] buf = new byte[MAX_BUF_SIZE];
        int len = input.read(buf);
        System.out.println("received:" + new String(buf, 0, len));
    }

    public void sender() {
        new Thread(() -> {
            while (true) {
                Scanner input_text = new Scanner(System.in);
                String message = new String(input_text.next());

                try {
                    if (output != null) {
                        output.write(message.getBytes());
                        output.flush();
                        byte[] buf = new byte[MAX_BUF_SIZE];
                        int len = input.read(buf);
                        String rev_message = new String(buf, 0, len);
                        System.out.println("received:" + rev_message);
                        if (rev_message.equals("exit")) {
                            output.close();
                            output = null;
                            socket.close();
                        }
                    } else {
                        System.out.println("connection lost");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }


    
    public static void main(String[] args) throws Exception {
    	HttpsJavaClient client = new HttpsJavaClient();
        client.sslSocket2();
        client.sender();
    }
}
