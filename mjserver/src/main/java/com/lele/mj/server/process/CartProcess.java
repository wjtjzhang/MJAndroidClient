package com.lele.mj.server.process;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lele.common.InCartComparator;
import com.lele.common.MJWinValidation;
import com.lele.entity.Cart;
import com.lele.entity.MeetType;
import com.lele.entity.Room;
import com.lele.entity.User;

public class CartProcess {

	static Logger LOG = LoggerFactory.getLogger(CartProcess.class);
	
	public static void getAndProcessNewCart(User user, Room room) {
		user.setYourTurn(true);
		if (room.getNextCartIndex() < room.carts.size()) {
			getNewCart(user, room);
			processWin(user, room);
			processAnGang(user, room);
			processMingGang(user, room);
		} else {
			LOG.info("Game is over and no one win.");
			room.setRoundOver(true);
		}
	}

	private static void processAnGang(User user, Room room) {
		List<Cart> carts = room.getInCartsMap().get(user);

		int count = 0;
		for (int i = 0; i < carts.size(); i++) {
			if (carts.get(i).equals(room.getCurrentGetCart())) {
				count++;
			}
		}

		if (count == 4) {
			user.setMeetType(MeetType.AN_GANG);
		}
	}

	private static void processMingGang(User user, Room room) {
		List<Cart> carts = room.getMeetCartsMap().get(user);

		int count = 0;
		for (int i = 0; i < carts.size(); i++) {
			if (carts.get(i).equals(room.getCurrentGetCart())) {
				count++;
			}
		}

		if (count == 4) {
			user.setMeetType(MeetType.GET_MING_GANG);
		}
	}
	
	private static void getNewCart(User user, Room room) {
		room.getInCartsMap().get(user).sort(new InCartComparator());
		room.getInCartsMap().get(user).add(room.carts.get(room.getNextCartIndex()));
		room.setCurrentGetCart(room.carts.get(room.getNextCartIndex()));
		room.setNextCartIndex(room.getNextCartIndex() + 1);
	}

	private static void processWin(User user, Room room) {
		boolean isWin = MJWinValidation.isWin(room.getInCartsMap().get(user));
		if (isWin) {
			user.setWin(true);
		}
	}
}
