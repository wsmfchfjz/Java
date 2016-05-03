package com.xpg.socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

import com.xpg.utils.KeyUtil;

public class SocketServer {

    private int server_port = 10010;
    private int MAX_BUF_SIZE = 1024;

    // 启动一个ssl server socket
    // 配置了证书, 所以不会抛出异常
    public void sslSocketServer() throws Exception {

    	SSLContext serverSSLContext = KeyUtil.getServerSSLContext();
    	
        // 监听和接收客户端连接
        SSLServerSocketFactory factory = serverSSLContext.getServerSocketFactory();
        SSLServerSocket server = (SSLServerSocket) factory
                .createServerSocket(server_port);
        System.out.println("I am ready for client's connection");

        while (true) {
            Socket client = server.accept();
            System.out.println(client.getRemoteSocketAddress());

            new Thread(() -> {
                while (true) {
                    try {
                        execute(client);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }

    private void execute(Socket client) throws Exception {
        if (client == null || client.isClosed()) {
            return;
        }
        // 向客户端发送接收到的字节序列
        OutputStream output = client.getOutputStream();

        // 当一个普通 socket 连接上来, 这里会抛出异常
        // Exception in thread "main" javax.net.ssl.SSLException: Unrecognized
        // SSL message, plaintext connection?
        InputStream input = client.getInputStream();
        byte[] buf = new byte[MAX_BUF_SIZE];
        int len = input.read(buf);
        String message = new String(buf, 0, len);
        System.out.println("received: " + message);
        output.write(buf, 0, len);
        output.flush();
        if (message.equals("exit")) {
            output.close();
            input.close();
            client.close();
        }
    }

    public static void main(String[] args) throws Exception {
    	SocketServer server = new SocketServer();
        server.sslSocketServer();
    }
}
