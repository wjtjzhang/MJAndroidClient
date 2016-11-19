package com.lele.mj.client.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lele.entity.Action;
import com.lele.entity.Cart;
import com.lele.entity.CartStatus;
import com.lele.entity.MeetType;
import com.lele.entity.Request;
import com.lele.entity.Response;
import com.lele.entity.Room;
import com.lele.entity.User;

public class GameProcess {

	static Logger LOG = LoggerFactory.getLogger(GameProcess.class);

	public static void handleAction(Response response, Room room, IoSession session) throws IOException {
		for (User user : room.getUsers()) {
			if (user.equals(response.getUser()) && user.isYourTurn()) {
				String in = null;

				if (user.isWin() && !user.getMeetType().equals(MeetType.AN_GANG)) {
					LOG.info("Win(W) cart : {} ?", room.getCurrentGetCart());
					in = new BufferedReader(new InputStreamReader(System.in)).readLine();
					session.write(new Request(Action.GAME_WIN, response.getUser(), room));
					break;
				} else if (user.isWin() && user.getMeetType().equals(MeetType.AN_GANG)) {
					LOG.info("Win(W) or Gang(G) cart : {} ?", room.getCurrentGetCart());
					in = new BufferedReader(new InputStreamReader(System.in)).readLine();
					if ("W".equals(in)) {
						user.setMeetType(MeetType.NONE);
						session.write(new Request(Action.GAME_WIN, response.getUser(), room));
					} else {
						user.setMeetType(MeetType.AN_GANG);
						session.write(new Request(Action.GAME_MEET, user, room));
					}
					break;
				}

				switch (user.getMeetType()) {
				case PENG:
					LOG.info("Peng(P) cart : {} ?", room.getCurrentPutCart());
					in = new BufferedReader(new InputStreamReader(System.in)).readLine();
					if ("P".equals(in)) {
						user.setMeetType(MeetType.PENG);

						session.write(new Request(Action.GAME_MEET, user, room));
					} else {
						user.setMeetType(MeetType.NONE);
						session.write(new Request(Action.GAME_PUT_CART, response.getUser(), room));
					}
					break;

				case PUT_MING_GANG:
					LOG.info("Peng(P) or Gang(G) cart : {} ?", room.getCurrentPutCart());
					in = new BufferedReader(new InputStreamReader(System.in)).readLine();
					if ("P".equals(in)) {
						user.setMeetType(MeetType.PENG);
						session.write(new Request(Action.GAME_MEET, user, room));
					} else if ("G".equals(in)) {
						user.setMeetType(MeetType.PUT_MING_GANG);
						session.write(new Request(Action.GAME_MEET, user, room));
					} else {
						user.setMeetType(MeetType.NONE);
						session.write(new Request(Action.GAME_PUT_CART, response.getUser().getMeetTriggerUser(), room));
					}
					break;
				case GET_MING_GANG:
					LOG.info("Gang(G) cart : {} ?", room.getCurrentPutCart());
					in = new BufferedReader(new InputStreamReader(System.in)).readLine();
					if ("G".equals(in)) {
						user.setMeetType(MeetType.GET_MING_GANG);
						session.write(new Request(Action.GAME_MEET, user, room));
					}
					break;

				case AN_GANG:
					LOG.info("Gang(G) cart : {} ?", room.getCurrentGetCart());
					in = new BufferedReader(new InputStreamReader(System.in)).readLine();
					if ("G".equals(in)) {
						user.setMeetType(MeetType.AN_GANG);
						session.write(new Request(Action.GAME_MEET, user, room));
					}
					break;

				case NONE:
					putCart(response, room, session, Action.GAME_PUT_CART);
					break;

				default:
					break;
				}
			}
		}
	}

	private static void putCart(Response response, Room room, IoSession session, Action nextAction) throws IOException {
		if (isYourTurn(response, room)) {
			LOG.info("Your turn to play.");
			String in = new BufferedReader(new InputStreamReader(System.in)).readLine();
			Cart cart = room.getInCartsMap().get(response.getUser()).get(Integer.valueOf(in).intValue());
			cart.setCartStatus(CartStatus.OUT);
			cart.setPlayTime(System.currentTimeMillis());
			room.setCurrentPutCart(cart);

			room.getOutCartsMap().get(response.getUser()).add(cart);
			room.getInCartsMap().get(response.getUser()).remove(cart);
			session.write(new Request(nextAction, response.getUser(), room));
		}
	}

	private static boolean isYourTurn(Response response, Room room) {
		for (User user : room.getUsers()) {
			if (user.equals(response.getUser()) && user.isYourTurn() && user.getMeetType() == MeetType.NONE) {
				return true;
			}
		}
		return false;
	}
}
