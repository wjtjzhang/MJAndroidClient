package com.lele.mj.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.log4j.PropertyConfigurator;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lele.mj.server.handler.MinaServerSessionHandler;

public class MinaServer {

	static Logger LOG = LoggerFactory.getLogger(MinaServer.class);

	private static final int PORT = 2017;

	public static void main(String[] args) throws IOException {
		PropertyConfigurator.configure("log4j.properties");

		IoAcceptor acceptor = new NioSocketAcceptor();
		acceptor.setHandler(  new MinaServerSessionHandler() );
		acceptor.getFilterChain().addLast("codec",
	            new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		acceptor.bind( new InetSocketAddress(PORT) );

		LOG.info("MJ Server is started at port {}", PORT);
	}

}
