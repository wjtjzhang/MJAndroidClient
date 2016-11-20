package com.lele.action;

import java.io.IOException;

import org.apache.mina.core.session.IoSession;

import com.lele.entity.Action;
import com.lele.entity.Request;
import com.lele.entity.Room;
import com.lele.entity.User;

public class RoomAction {

	public static void createARoom(IoSession session, User user) throws IOException {
		Request request = new Request(Action.GAME_CREATE_ROOM, user, null);
		session.write(request);
	}

	public static void joinARoom(IoSession session, User user, Room room) throws IOException {
		Request request = new Request(Action.GAME_JOIN_ROOM, user, room);
		session.write(request);
	}

}
