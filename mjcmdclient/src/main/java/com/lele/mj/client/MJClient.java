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

	private Room room;
	private IoSession session;
	private User user;

	private Map<Action, Service> serviceMap = new ConcurrentHashMap<Action, Service>();

	private static MJClient instance = null;

	private MJClient() {
	}

	public static MJClient getInstance() {
		if (instance == null) {
			synchronized (MJClient.class) {
				if (instance == null) {
					instance = new MJClient();
					instance.registerServices();
				}
			}
		}
		return instance;
	}

	private void registerServices() {
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
		serviceMap.put(Action.LIVE, gameService);
	}

	public void login() throws IOException, ClassNotFoundException {
		Request request = new Request(Action.LOGIN, user, null);
		session.write(request);
	}

	public void startChat() {
		try {
			ChatAction.startChat(user, session);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createARoom() {
		try {
			RoomAction.createARoom(session, user);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void joinARoom(int roomId) {
		try {
			user.setRoomId(roomId);
			RoomAction.joinARoom(session, user, new Room(roomId));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	synchronized public void send(Action gamePutCart) {
		session.write(new Request(gamePutCart, user, room));
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public User getUser() {
		return user;
	}

	public Map<Action, Service> getServiceMap() {
		return serviceMap;
	}

	public void setServiceMap(Map<Action, Service> serviceMap) {
		this.serviceMap = serviceMap;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public IoSession getSession() {
		return session;
	}

	public void setSession(IoSession session) {
		this.session = session;
	}
}
