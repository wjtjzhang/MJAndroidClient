package com.lele.mj.client;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lele.entity.Action;
import com.lele.entity.Request;
import com.lele.entity.Room;
import com.lele.entity.User;
import com.lele.mj.action.ChatAction;
import com.lele.mj.action.RoomAction;
import com.lele.mj.client.service.ErrorService;
import com.lele.mj.client.service.GameService;
import com.lele.mj.client.service.HallService;
import com.lele.mj.client.service.LoginService;
import com.lele.mj.client.service.Service;

public class MJClient {

	Logger LOG = LoggerFactory.getLogger(MJClient.class);

	public static Room room;

	public static Map<Action, Service> serviceMap = new ConcurrentHashMap<Action, Service>();

	static {
		Service gameService = new GameService();
		serviceMap.put(Action.GAME_CREATE_ROOM, gameService);
		serviceMap.put(Action.GAME_JOIN_ROOM, gameService);
		serviceMap.put(Action.GAME_WAIT, gameService);
		serviceMap.put(Action.GAME_NEW_ROUND_START, gameService);
		serviceMap.put(Action.GAME_WAIT_FOR_NEXT_ROUND, gameService);
		serviceMap.put(Action.GAME_INIT, gameService);
		serviceMap.put(Action.GAME_PUT_CART, gameService);
		serviceMap.put(Action.GAME_MEET, gameService);
		serviceMap.put(Action.GAME_WIN, gameService);
		serviceMap.put(Action.HALL, new HallService());
		serviceMap.put(Action.ERROR, new ErrorService());
		serviceMap.put(Action.LOGIN, new LoginService());
	}

	public static void login(IoSession session, User user) throws IOException, ClassNotFoundException {
		Request request = new Request(Action.LOGIN, user, null);
		session.write(request);
	}

	public void startChat(IoSession session, User user) throws IOException {
		ChatAction.startChat(user, session);
	}

	public static void createARoom(IoSession session, User user) throws IOException {
		RoomAction.createARoom(session, user);
	}

	public static void joinARoom(IoSession session, User user, Room room) throws IOException {
		RoomAction.joinARoom(session, user, room);
	}
}
