package com.lele.mj.client.handler;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lele.entity.Response;
import com.lele.mj.client.MJClient;

public class MinaClientSessionHandler implements IoHandler {
	Logger LOG = LoggerFactory.getLogger(MinaClientSessionHandler.class);

	public void sessionCreated(IoSession session) throws Exception {
		LOG.info("sessionCreated");

	}

	public void sessionOpened(IoSession session) throws Exception {
		LOG.info("sessionOpened");

	}

	public void sessionClosed(IoSession session) throws Exception {
		LOG.info("sessionClosed");

	}

	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		LOG.info("sessionIdle");

	}

	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		LOG.info("exceptionCaught{}", cause.getMessage());

	}

	public void messageReceived(IoSession session, Object message) throws Exception {
		LOG.info("Message Received - {}", message);
		Response response = (Response) message;
		MJClient.serviceMap.get(response.getAction()).process(response, session);

	}

	public void messageSent(IoSession session, Object message) throws Exception {
		LOG.info("messageSent");

	}

	public void inputClosed(IoSession session) throws Exception {
		LOG.info("inputClosed");

	}

}
