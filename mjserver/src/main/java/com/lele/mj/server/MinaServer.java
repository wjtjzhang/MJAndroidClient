package com.lele.mj.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.lele.mj.server.handler.MinaServerSessionHandler;

public class MinaServer {

	private static final int PORT = 2017;

	public static void main(String[] args) throws IOException {
		IoAcceptor acceptor = new NioSocketAcceptor();
		acceptor.setHandler(  new MinaServerSessionHandler() );
		acceptor.getFilterChain().addLast("codec",
	            new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		acceptor.bind( new InetSocketAddress(PORT) );
	}

}
