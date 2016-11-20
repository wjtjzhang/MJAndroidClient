package com.lele.mj.server.service;

import java.io.IOException;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lele.entity.Request;
import com.lele.entity.User;
import com.lele.mj.server.MJServer;

public class ChatService implements Service {

	Logger LOG = LoggerFactory.getLogger(ChatService.class);

	public void process(Request request, IoSession session) throws IOException {
		String message = request.getObject().toString();
		if (!"null".equals(message)) {
			LOG.info("Receive message :{}", message);
			publishMessage(message, session);
		}
	}

	private void publishMessage(String message, IoSession mySession) throws IOException {
		/*for (User user : MJServer.sessions.keySet()) {
			IoSession session = MJServer.sessions.get(user);
			if (!session.equals(mySession)) {
				session.write(message);
			}
		}*/
	}

}
