package com.xpg.mina;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import javax.net.ssl.SSLContext;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringCodecFactory;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.xpg.utils.KeyUtil;

public class MinaServer {
	
	private static final int PORT= 443;
	
	public void startServer() throws Exception{
		
		IoAcceptor acceptor = new NioSocketAcceptor();
		SSLContext serverSSLContext = KeyUtil.getServerSSLContext();
		SslFilter sslFilter = new SslFilter(serverSSLContext);
		acceptor.getFilterChain().addLast("sslFilter", sslFilter);
		
		acceptor.getFilterChain().addLast( "logger", new LoggingFilter() );
        acceptor.getFilterChain().addLast( "codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
        
        acceptor.setHandler(  new MyIOHandler() );

        acceptor.getSessionConfig().setReadBufferSize( 2048 );
        acceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 10 );
        
        acceptor.bind(new InetSocketAddress(PORT));
	}
	
	public static void main(String[] args) throws Exception {
		new MinaServer().startServer();
	}
	
}
