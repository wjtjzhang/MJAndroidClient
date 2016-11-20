package com.lele.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.mina.core.session.IoSession;

import com.lele.entity.Action;
import com.lele.entity.Request;
import com.lele.entity.User;

public class ChatAction {

	public static void startChat(User user, IoSession session) throws IOException {
		while (true) {
			String inputMessage = new BufferedReader(new InputStreamReader(System.in)).readLine();
			Request request = new Request(Action.CHAT, user, "[" + user.getId() + "]:" + inputMessage);
			session.write(request);
			if ("bye".equals(inputMessage)) {
				break;
			}
		}
		session.closeNow();
	}
}
