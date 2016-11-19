package com.lele.mj.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.lele.entity.User;
import com.lele.mj.client.handler.MinaClientSessionHandler;

public class MinaClient {

	private static final long CONNECT_TIMEOUT = 100000;
	private static final String HOST = "192.168.3.17";
	private static final int PORT = 2017;

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		NioSocketConnector connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
		connector.setHandler(new MinaClientSessionHandler());

		connector.getFilterChain().addLast("codec",
	            new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));

		IoSession session;
		for (;;) {
	        try {
				ConnectFuture future = connector.connect(new InetSocketAddress(InetAddress.getByName(HOST), PORT));
	            future.awaitUninterruptibly();
	            session = future.getSession();
	            User user=new User("111");
	            session.write(user);
	            break;
	        } catch (RuntimeIoException e) {
	            e.printStackTrace();
	        }
	    }
		
		User user = new User(args[0]);
		user.setPassword(args[1]);
		MJClient.login(session, user);
	}
}
