package com.lele.mj.client.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lele.entity.Response;
import com.lele.entity.Room;
import com.lele.mj.client.MJClient;

public class LoginService implements Service {

	Logger LOG = LoggerFactory.getLogger(LoginService.class);

	public void process(Response response, IoSession session) throws IOException {
		switch (response.getAction()) {
		case LOGIN:
			if (response.getObject().equals(true)) {
				LOG.info("Create Room or Join Room? (C) or RoomId");
				String in = new BufferedReader(new InputStreamReader(System.in)).readLine();
				if ("C".equalsIgnoreCase(in)) {
					MJClient.getInstance().createARoom();
				} else {
					MJClient.getInstance().joinARoom(Integer.valueOf(in).intValue());
				}
			} else {
				LOG.info("Login failed!");
			}
			break;
		default:
			break;
		}
	}
}
