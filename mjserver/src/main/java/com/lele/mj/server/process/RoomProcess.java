package com.lele.mj.server.process;

import java.io.IOException;

import org.apache.mina.core.session.IoSession;

import com.lele.dao.UserDao;
import com.lele.entity.Action;
import com.lele.entity.ErrorCode;
import com.lele.entity.Request;
import com.lele.entity.Response;
import com.lele.entity.Room;
import com.lele.entity.User;
import com.lele.mj.server.MJServer;

public class RoomProcess {

	private static Room room;

	public static void createRoom(Request request, IoSession session, Action nextAction) throws IOException {
		if (hasRoomCart(request.getUser())) {
			room = new Room(generateRoomId());
			/*
			 * while (MJServer.rooms.contains(room)) { room = new
			 * Room(generateRoomId()); }
			 */
			room.setRoomOwner(request.getUser());
			MJServer.rooms.add(room);
			session.write(new Response(nextAction, request.getUser(), room));
		} else {
			session.write(new Response(Action.ERROR, request.getUser(), ErrorCode.ROOM_CART_NOT_ENOUGH));
		}

	}

	private static boolean hasRoomCart(User user) {
		UserDao dao = new UserDao();
		User dbUser = dao.getUser(user.getId());
		if (dbUser != null && dbUser.getRoomCart() > 0) {
			return true;
		}
		return false;
	}

	public static void joinRoom(Request request, IoSession session) throws IOException {
		room = MJServer.getRoom((Room) request.getObject());
		if (room == null) {
			session.write(new Response(Action.HALL, request.getUser(), ErrorCode.ROOM_NOT_FOUND));
			return;
		}

		if (!room.getUsers().contains(request.getUser())) {
			if (room.getUsers().size() == 4) {
				session.write(new Response(Action.HALL, request.getUser(), ErrorCode.ROOM_IS_FULL));
			} else {
				room.getUsers().add(request.getUser());
				if (room.getUsers().size() == 4) {
					GameProcess.initGame(request);
				} else {
					MJServer.notifyRoomMates(new Response(Action.GAME_WAIT, request.getUser(), room), room);
				}
			}
		}

		if (room.getUsers().contains(request.getUser())) {
			for (User user : room.getUsers()) {
				if (user.equals(request.getUser())){
					user.setOnLive(true);
				}
			}
			
			if (room.getUsers().size() == 4) {
				MJServer.notifyRoomMates(new Response(Action.GAME_PUT_CART, request.getUser(), room), room);
			} else {
				MJServer.notifyRoomMates(new Response(Action.GAME_WAIT, request.getUser(), room), room);
			}
		}
	}

	private static int generateRoomId() {
		return 111;
	}

	public static void startNewRound(Request request) throws IOException {
		room = MJServer.getRoom((Room) request.getObject());
		room.setReadyNumberOfNextRound(room.getReadyNumberOfNextRound() + 1);
		if (room.getReadyNumberOfNextRound() < 4) {
			MJServer.notifyRoomMates(new Response(Action.GAME_WAIT_FOR_NEXT_ROUND, request.getUser(), room), room);
		} else {
			GameProcess.initGame(request);
			room.setReadyNumberOfNextRound(0);
		}
	}
}
