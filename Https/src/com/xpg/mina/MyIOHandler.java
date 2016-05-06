package com.xpg.mina;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;

/**
 * Created by Loftor on 2014/8/15.
 */
public class MyIOHandler implements IoHandler {

    @Override
    public void exceptionCaught(IoSession arg0, Throwable arg1)
            throws Exception {
        arg1.printStackTrace();

    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {

        String str = message.toString();

        if (str.trim().equalsIgnoreCase("quit")) {
            session.close(true);
            return;
        }
//        Date date = new Date();
//        session.write(date.toString());
//        System.out.println("Message written...");
    	
        response(session,"{\"message\":\"Hello,client!奥奥\"}");
        System.out.println("接受到的消息:" + str );

    }
    
    public static void response(IoSession sessionk, String body) {
        IoSession ioSSN = sessionk;
        if (ioSSN != null) {
            body += "\r\n";
            try {
                body = new String(body.getBytes(), "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("HTTP/1.1 200 OK \r\n");
                sb.append("Content-Length: " + body.getBytes().length + " \r\n");
                sb.append("Content-Type: text/json \r\n");
                sb.append("Connection: keep-alive \r\n");
                sb.append("\r\n");
                sb.append(body);
                IoBuffer buf = IoBuffer.allocate(sb.toString().length()).setAutoExpand(true);
                try {
                    buf.put(sb.toString().getBytes("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                buf.flip();
                ioSSN.write(buf);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                ioSSN.close(true);
            }

        }
    }

    @Override
    public void messageSent(IoSession arg0, Object arg1) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("发送信息:" + arg1.toString());
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("IP:" + session.getRemoteAddress().toString() + "断开连接");
    }

	@Override
	public void sessionCreated(IoSession session) throws Exception {
        System.out.println("IP:" + session.getRemoteAddress().toString());
		SocketSessionConfig cfg = (SocketSessionConfig) session.getConfig();   
        cfg.setReceiveBufferSize(512 * 1024);   
        cfg.setReadBufferSize(512 * 1024);   
        cfg.setKeepAlive(true);   
        cfg.setSoLinger(0); //重要设置，解决 tcp wait 问题
	}

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("IDLE " + session.getIdleCount(status));

    }

    @Override
    public void sessionOpened(IoSession arg0) throws Exception {
        // TODO Auto-generated method stub

    }
}