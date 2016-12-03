package com.lele.mj.server.service;

import java.io.IOException;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lele.dao.UserDao;
import com.lele.entity.Action;
import com.lele.entity.Request;
import com.lele.entity.Response;
import com.lele.entity.User;
import com.lele.mj.server.MJServer;

public class LoginService implements Service {

	Logger LOG = LoggerFactory.getLogger(LoginService.class);

	public void process(Request request, IoSession session) throws IOException, ClassNotFoundException {
		boolean loginSuccess;
		User user = request.getUser();
		if (isValidLogin(user)) {
			user.setRoomCart(20);
			LOG.info("User {} login successfuly.", user);
			MJServer.sessions.put(user, session);
			loginSuccess = true;
		} else {
			LOG.info("User {} login fail.", user);
			loginSuccess = false;
		}
		session.write(new Response(Action.LOGIN, user, loginSuccess));
	}

	private boolean isValidLogin(User user) {
		UserDao dao = new UserDao();
		User dbUser = dao.getUser(user.getId());
		if (dbUser == null) {
			dao.insert(user);
		}
		return true;
	}
}
