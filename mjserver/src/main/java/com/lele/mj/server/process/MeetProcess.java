package com.lele.mj.server.process;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lele.common.ScoreManager;
import com.lele.entity.Action;
import com.lele.entity.Cart;
import com.lele.entity.CartStatus;
import com.lele.entity.MeetType;
import com.lele.entity.Request;
import com.lele.entity.Response;
import com.lele.entity.Room;
import com.lele.entity.User;
import com.lele.mj.server.MJServer;

public class MeetProcess {

	private static final int PENG_NUMBER = 2;
	private static final int GANG_NUMBER = 3;

	public static void handleMeetRequestWithPengGang(Request request, Action nextAction)
			throws IOException {
		Room room = (Room) request.getObject();
		MJServer.rooms.remove(room);
		MJServer.rooms.add(room);

		switch (request.getUser().getMeetType()) {
		case PENG:
			handleMeet(room, request.getUser(), PENG_NUMBER, room.getCurrentPutCart());
			break;

		case PUT_MING_GANG:
			handleMeet(room, request.getUser(), GANG_NUMBER, room.getCurrentPutCart());
			ScoreManager.calclatePutMingGang(room, request.getUser());
			CartProcess.getAndProcessNewCart(request.getUser(), room);
			break;

		case GET_MING_GANG:
			handleGetMingGang(room, request.getUser(), GANG_NUMBER);
			ScoreManager.calclateGetMingGang(room, request.getUser());
			CartProcess.getAndProcessNewCart(request.getUser(), room);
			break;

		case AN_GANG:
			handleMeet(room, request.getUser(), GANG_NUMBER, room.getCurrentGetCart());
			ScoreManager.calclateGetAnGang(room, request.getUser());
			CartProcess.getAndProcessNewCart(request.getUser(), room);
			break;

		default:
			break;
		}

		MJServer.notifyRoomMates(nextAction, room);
	}

	public static boolean processMeetRequestWithPutCart(Request request, Room room) {
		resetUserTurn(room.getUsers());
		for (User user : room.getUsers()) {
			if (!user.equals(request.getUser())) {
				List<Cart> carts = room.getInCartsMap().get(user);

				int count = 0;
				for (int i = 0; i < carts.size(); i++) {
					if (carts.get(i).equals(room.getCurrentPutCart())) {
						count++;
					}
				}

				if (count == PENG_NUMBER) {
					user.setYourTurn(true);
					user.setMeetType(MeetType.PENG);
					user.setMeetTriggerUser(request.getUser());
					return true;
				}

				if (count == GANG_NUMBER) {
					user.setYourTurn(true);
					user.setMeetType(MeetType.PUT_MING_GANG);
					user.setMeetTriggerUser(request.getUser());
					return true;
				}
			}
		}
		return false;
	}

	private static void resetUserTurn(List<User> users) {
		for (User user : users) {
			user.setYourTurn(false);
		}
	}

	private static void handleMeet(Room room, User user, int meetNumber, Cart targetCart) {
		int count = 0;
		Map<Integer, Cart> tempCarts = new HashMap<Integer, Cart>();
		for (int i = 0; i < room.getInCartsMap().get(user).size(); i++) {
			Cart inCart = room.getInCartsMap().get(user).get(i);
			if (inCart.equals(targetCart)) {
				if (count < meetNumber) {
					inCart.setCartStatus(CartStatus.MEET);
					inCart.setMeetType(user.getMeetType());
					tempCarts.put(i, inCart);
					count++;
				}
			}
		}

		moveCartsFromInQueueToMeetQueue(room, user, tempCarts);

		room.getMeetCartsMap().get(user).add(targetCart);
		if (user.getMeetTriggerUser() != null) {
			room.getOutCartsMap().get(user.getMeetTriggerUser()).remove(targetCart);
		}
		user.setMeetType(MeetType.NONE);
		user.setMeetTriggerUser(null);
	}

	private static void moveCartsFromInQueueToMeetQueue(Room room, User user, Map<Integer, Cart> tempCarts) {
		room.getMeetCartsMap().get(user).addAll(tempCarts.values());
		for (int index:tempCarts.keySet()) {
			room.getInCartsMap().get(user).remove(index);
			//TODO some problem here caused can't remove the items correctly.
		}
	}

	private static void handleGetMingGang(Room room, User user, int gangNumber) {
		room.getCurrentGetCart().setCartStatus(CartStatus.MEET);
		room.getCurrentGetCart().setMeetType(MeetType.GET_MING_GANG);
		room.getMeetCartsMap().get(user).add(room.getCurrentGetCart());
		room.getInCartsMap().get(user).remove(room.getCurrentGetCart());
	}
}
