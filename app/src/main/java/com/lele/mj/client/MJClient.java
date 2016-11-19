package com.lele.mj.client;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;

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

	public static Room room;
	public static IoSession session;
	public static User user;

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

	public static void login(String userId) throws IOException, ClassNotFoundException {
		Request request = new Request(Action.LOGIN, new User(userId), null);
		session.write(request);
	}

	public void startChat() {
		try {
			ChatAction.startChat(user, session);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createARoom() {
		try {
			RoomAction.createARoom(session, user);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void joinARoom(int roomId) {
		try {
			RoomAction.joinARoom(session, user, new Room(roomId));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	synchronized public static void send(Action gamePutCart) {
		session.write(new Request(gamePutCart, user, room));
	}
}
