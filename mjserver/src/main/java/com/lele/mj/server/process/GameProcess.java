package com.lele.mj.server.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lele.common.ScoreManager;
import com.lele.dao.UserDao;
import com.lele.entity.Action;
import com.lele.entity.Cart;
import com.lele.entity.CartType;
import com.lele.entity.MeetType;
import com.lele.entity.Request;
import com.lele.entity.Response;
import com.lele.entity.Room;
import com.lele.entity.User;
import com.lele.mj.server.MJServer;

public class GameProcess {

	static Logger LOG = LoggerFactory.getLogger(GameProcess.class);

	public static void handlePutCart(Request request, Action nextAction) throws IOException {
		Room room = (Room) request.getObject();
		MJServer.rooms.remove(room);
		MJServer.rooms.add(room);
		if (!MeetProcess.processMeetRequestWithPutCart(request, room)) {
			nextPlayer(request, room);
		}

		MJServer.notifyRoomMates(new Response(nextAction, request.getUser(), room), room);
	}

	private static void nextPlayer(Request request, Room room) {
		User currentUser = request.getUser();
		if (request.getUser().getMeetTriggerUser() != null) {
			currentUser = request.getUser().getMeetTriggerUser();
		}

		for (int i = 0; i < room.getUsers().size(); i++) {
			User user = room.getUsers().get(i);
			if (user.equals(currentUser)) {
				user.setYourTurn(false);

				User nextUser;
				if (i < 3) {
					nextUser = room.getUsers().get(i + 1);
				} else {
					nextUser = room.getUsers().get(0);
				}

				CartProcess.getAndProcessNewCart(nextUser, room);
				request.getUser().setMeetTriggerUser(null);
			}
		}
	}

	public static void handleWin(Request request, Action nextAction) throws IOException {
		Room room = (Room) request.getObject();
		MJServer.rooms.remove(room);
		MJServer.rooms.add(room);
		room.setCurrentRound(room.getCurrentRound() + 1);
		if (room.getCurrentRound() == 2) {
			UserDao dao = new UserDao();
			User dbUser = dao.getUser(room.getRoomOwner().getId());
			dbUser.setRoomCart(dbUser.getRoomCart() - 1);
			dao.updateRoomCart(dbUser);
		}
		ScoreManager.calclateWin(room);
		MJServer.notifyRoomMates(new Response(nextAction, request.getUser(), room), room);
	}

	public static void handleOver(Request request,Action nextAction) throws IOException {
		Room room = (Room) request.getObject();
		MJServer.rooms.remove(room);
		MJServer.rooms.add(room);
		MJServer.notifyRoomMates(new Response(nextAction, request.getUser(), room), room);
	}

	public static void initGame(Request request) throws IOException {
		Room room = MJServer.getRoom((Room) request.getObject());
		if (room.carts.isEmpty()) {
			// initCarts();
			mockCarts(room);
		}

		initRoom(room);
		initUsers(room);
		dispatchCarts(room);
		MJServer.notifyRoomMates(new Response(Action.GAME_PUT_CART, request.getUser(), room), room);
	}

	private static void initUsers(Room room) {
		for (int i = 0; i < room.getUsers().size(); i++) {
			User user = room.getUsers().get(i);
			user.setWin(false);
			user.setMeetTriggerUser(null);
			user.setMeetType(MeetType.NONE);
			user.setYourTurn(false);
		}
	}

	private static void initRoom(Room room) {
		room.setNextCartIndex(52);
		room.setRoundOver(false);
		room.setCurrentPutCart(null);
		room.setCurrentGetCart(null);
	}

	private static void dispatchCarts(Room room) {
		for (int i = 0; i < room.getUsers().size(); i++) {
			User user = room.getUsers().get(i);
			room.getInCartsMap().put(user, new ArrayList<Cart>(room.carts.subList(i * 13, (i + 1) * 13)));
			room.getOutCartsMap().put(user, new ArrayList<Cart>());
			room.getMeetCartsMap().put(user, new ArrayList<Cart>());
		}

		generateHost(room);
	}

	private static void initCarts(Room room) {
		for (CartType cartType : CartType.values()) {
			for (int j = 1; j < 10; j++) {
				room.carts.add(new Cart(cartType, j));
				room.carts.add(new Cart(cartType, j));
				room.carts.add(new Cart(cartType, j));
				room.carts.add(new Cart(cartType, j));
			}
		}

		Collections.shuffle(room.carts);
	}

	private static void generateHost(Room room) {
		Random random = new Random();
		int host = random.nextInt(4);
		User hostUser = room.getUsers().get(0);
		CartProcess.getAndProcessNewCart(hostUser, room);
		LOG.info("Host £º{}", hostUser);
	}

	private static void mockCarts(Room room) {
		room.carts.add(new Cart(CartType.W, 1));
		room.carts.add(new Cart(CartType.W, 1));
		room.carts.add(new Cart(CartType.W, 1));
		room.carts.add(new Cart(CartType.W, 2));
		room.carts.add(new Cart(CartType.W, 2));
		room.carts.add(new Cart(CartType.W, 2));
		room.carts.add(new Cart(CartType.W, 3));
		room.carts.add(new Cart(CartType.W, 3));
		room.carts.add(new Cart(CartType.W, 3));
		room.carts.add(new Cart(CartType.W, 4));
		room.carts.add(new Cart(CartType.W, 4));
		room.carts.add(new Cart(CartType.W, 4));
		room.carts.add(new Cart(CartType.W, 5));

		room.carts.add(new Cart(CartType.S, 1));
		room.carts.add(new Cart(CartType.S, 1));
		room.carts.add(new Cart(CartType.S, 1));
		room.carts.add(new Cart(CartType.S, 2));
		room.carts.add(new Cart(CartType.S, 2));
		room.carts.add(new Cart(CartType.S, 2));
		room.carts.add(new Cart(CartType.S, 3));
		room.carts.add(new Cart(CartType.S, 3));
		room.carts.add(new Cart(CartType.S, 3));
		room.carts.add(new Cart(CartType.S, 4));
		room.carts.add(new Cart(CartType.S, 4));
		room.carts.add(new Cart(CartType.S, 4));
		room.carts.add(new Cart(CartType.S, 5));

		room.carts.add(new Cart(CartType.T, 1));
		room.carts.add(new Cart(CartType.T, 1));
		room.carts.add(new Cart(CartType.T, 1));
		room.carts.add(new Cart(CartType.T, 2));
		room.carts.add(new Cart(CartType.T, 2));
		room.carts.add(new Cart(CartType.T, 2));
		room.carts.add(new Cart(CartType.T, 3));
		room.carts.add(new Cart(CartType.T, 3));
		room.carts.add(new Cart(CartType.T, 3));
		room.carts.add(new Cart(CartType.T, 4));
		room.carts.add(new Cart(CartType.T, 4));
		room.carts.add(new Cart(CartType.T, 4));
		room.carts.add(new Cart(CartType.T, 5));

		room.carts.add(new Cart(CartType.W, 1));
		room.carts.add(new Cart(CartType.W, 2));
		room.carts.add(new Cart(CartType.W, 3));
		room.carts.add(new Cart(CartType.W, 4));
		room.carts.add(new Cart(CartType.S, 1));
		room.carts.add(new Cart(CartType.S, 2));
		room.carts.add(new Cart(CartType.S, 3));
		room.carts.add(new Cart(CartType.S, 4));
		room.carts.add(new Cart(CartType.T, 1));
		room.carts.add(new Cart(CartType.T, 2));
		room.carts.add(new Cart(CartType.T, 3));
		room.carts.add(new Cart(CartType.T, 4));
		room.carts.add(new Cart(CartType.T, 5));
		
		room.carts.add(new Cart(CartType.T, 5));
		room.carts.add(new Cart(CartType.T, 5));
		room.carts.add(new Cart(CartType.W, 5));
		room.carts.add(new Cart(CartType.W, 5));
		room.carts.add(new Cart(CartType.W, 5));
		room.carts.add(new Cart(CartType.S, 5));
		room.carts.add(new Cart(CartType.S, 5));
		room.carts.add(new Cart(CartType.S, 5));
		/*
		 * for (int i = 6; i <= 9; i++) { for (CartType type :
		 * CartType.values()) { room.carts.add(new Cart(type, i)); } }
		 */

	}
}
