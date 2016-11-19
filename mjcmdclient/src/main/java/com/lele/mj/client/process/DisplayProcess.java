package com.lele.mj.client.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lele.entity.Action;
import com.lele.entity.Cart;
import com.lele.entity.Request;
import com.lele.entity.Response;
import com.lele.entity.Room;
import com.lele.entity.User;

public class DisplayProcess {

	static Logger LOG = LoggerFactory.getLogger(DisplayProcess.class);

	public static void displayRoom(Response response, Room room) throws IOException {
		int myPosition = room.getUsers().indexOf(response.getUser());

		for (int i = myPosition; i < room.getInCartsMap().size(); i++) {
			displayCarts(room, room.getUsers().get(i));
		}

		for (int i = 0; i < myPosition; i++) {
			displayCarts(room, room.getUsers().get(i));
		}
	}

	private static void displayCarts(Room room, User user) throws IOException {
		List<Cart> inCarts = room.getInCartsMap().get(user);

		List<Cart> outCarts = room.getOutCartsMap().get(user);

		List<Cart> meetCarts = room.getMeetCartsMap().get(user);

		LOG.info("In Carts :[Size : {},User :{}]{}" + (user.isYourTurn() ? "GetCart{}" : ""), inCarts.size(), user,
				getCartString(inCarts), (user.isYourTurn() ? room.getCurrentGetCart() : ""));
		LOG.info("Out Carts :[Size : {},User :{}]{}", outCarts.size(), user, getCartString(outCarts));
		LOG.info("Meet Carts :[Size : {},User :{}]{}", meetCarts.size(), user, getCartString(meetCarts));
	}

	private static String getCartString(List<Cart> carts) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < carts.size(); i++) {
			sb.append(carts.get(i).getCartType()).append(carts.get(i).getCode()).append("(").append(i).append("),");
		}
		return sb.toString();
	}

	public static void displayScope(Response response, Room room, IoSession session) throws IOException {
		if (room.getCurrentRound() <= room.getTotalRound()) {
			LOG.info("Round {} is over, the score summary as below:", room.getCurrentRound() - 1);
			for (User user : room.getUsers()) {
				LOG.info("User {},Score {}, InCart{}", user.getId(), user.getScore(), room.getInCartsMap().get(user));
			}
			LOG.info("Continue round {}? (Y/N)", room.getCurrentRound());
			String in = new BufferedReader(new InputStreamReader(System.in)).readLine();
			if ("Y".equals(in)) {
				session.write(new Request(Action.GAME_NEW_ROUND_START, response.getUser(), room));
			}
		} else {
			LOG.info("Game is over, the overall score summary as below:");
			for (User user : room.getUsers()) {
				LOG.info("User {},Score {}, InCart{}", user.getId(), user.getScore(), room.getInCartsMap().get(user));
			}
		}

	}
}
