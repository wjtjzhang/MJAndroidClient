package com.lele.mj.client.service;

import java.io.IOException;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lele.entity.Action;
import com.lele.entity.Request;
import com.lele.entity.Response;
import com.lele.entity.Room;
import com.lele.mj.client.MJClient;
import com.lele.mj.client.process.DisplayProcess;
import com.lele.mj.client.process.GameProcess;

public class GameService implements Service {

	Logger LOG = LoggerFactory.getLogger(GameService.class);

	public void process(Response response, IoSession session) throws IOException {
		switch (response.getAction()) {
		case GAME_CREATE_ROOM:
			if (response.getObject() instanceof Room) {
				MJClient.room = ((Room) response.getObject());
				LOG.info("Room {} have been created successfully.", MJClient.room);
				session.write(new Request(Action.GAME_JOIN_ROOM, response.getUser(), MJClient.room));
			}
			break;
		case GAME_WAIT:
			MJClient.room = ((Room) response.getObject());
			LOG.info("Join room sucessfully, wait for other players {}/4...", MJClient.room.getUsers().size());
			break;

		case GAME_WAIT_FOR_NEXT_ROUND:
			MJClient.room = ((Room) response.getObject());
			LOG.info("Wait for other players back to next round.");
			break;

		case GAME_PUT_CART:
			LOG.info("play game...");
			MJClient.room = ((Room) response.getObject());
			if (!MJClient.room.isRoundOver()) {
				DisplayProcess.displayRoom(response, MJClient.room);
				GameProcess.handleAction(response, MJClient.room, session);
			} else {
				LOG.info("game is over and no one win. caculate scope...");
				DisplayProcess.displayScope(response, MJClient.room, session);
			}
			break;

		case GAME_WIN:
			LOG.info("game is over and someone win. caculate scope...");
			MJClient.room = ((Room) response.getObject());
			DisplayProcess.displayScope(response, MJClient.room, session);

		default:
			break;
		}
	}
}
