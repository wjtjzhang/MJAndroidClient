package com.lele.mj.server.service;

import java.io.IOException;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lele.entity.Action;
import com.lele.entity.Request;
import com.lele.mj.server.process.GameProcess;
import com.lele.mj.server.process.MeetProcess;
import com.lele.mj.server.process.RoomProcess;

public class GameService implements Service {

	Logger LOG = LoggerFactory.getLogger(GameService.class);

	public void process(Request request, IoSession session) throws IOException {
		switch (request.getAction()) {
		case GAME_CREATE_ROOM:
			RoomProcess.createRoom(request, session, Action.GAME_CREATE_ROOM);
			break;

		case GAME_JOIN_ROOM:
			RoomProcess.joinRoom(request, session);
			break;
		case GAME_PUT_CART:
			GameProcess.handlePutCart(request, Action.GAME_PUT_CART);
			break;

		case GAME_MEET:
			MeetProcess.handleMeetRequestWithPengGang(request, Action.GAME_PUT_CART);
			break;

		case GAME_WIN:
			GameProcess.handleWin(request, Action.GAME_WIN);
			break;
			
		case GAME_NEW_ROUND_START:
			RoomProcess.startNewRound(request);
			
			break;

		default:
			break;
		}
		
	}

}
