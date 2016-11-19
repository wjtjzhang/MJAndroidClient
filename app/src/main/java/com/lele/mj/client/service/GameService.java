package com.lele.mj.client.service;

import java.io.IOException;

import org.apache.mina.core.session.IoSession;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lele.entity.Action;
import com.lele.entity.Request;
import com.lele.entity.Response;
import com.lele.entity.Room;
import com.lele.mj.client.MJClient;
import com.lele.mj.common.UIStatus;

public class GameService implements Service {
	Message msg = new Message();

	public void process(Response response, IoSession session, Handler handler) throws IOException {
		switch (response.getAction()) {
		case GAME_CREATE_ROOM:
			if (response.getObject() instanceof Room) {
				MJClient.room = ((Room) response.getObject());
				Log.i("", "Room have been created successfully - " + MJClient.room);
				session.write(new Request(Action.GAME_JOIN_ROOM, response.getUser(), MJClient.room));
			}
			break;
		case GAME_WAIT:
			MJClient.room = ((Room) response.getObject());
			msg.what = UIStatus.GAME_WAIT;
			handler.sendMessage(msg);
			Log.i("", "Join room sucessfully, wait for other players " + MJClient.room.getUsers().size() + "/4..."
					+ MJClient.room);
			break;

		case GAME_PUT_CART:
			Log.i("", "play game...");
			MJClient.room = ((Room) response.getObject());
			if (!MJClient.room.isRoundOver()) {
				msg.what = UIStatus.GAME_PUT_CART;
				handler.sendMessage(msg);
			} else {
				msg.what = UIStatus.DISPLAY_SCORE;
				handler.sendMessage(msg);
				Log.i("", "game is over and no one win. caculate scope...");
			}
			break;

		case GAME_WIN:
			Log.i("", "game is over and someone win. caculate scope...");
			MJClient.room = ((Room) response.getObject());
			msg.what = UIStatus.DISPLAY_SCORE;
			handler.sendMessage(msg);
			break;

		case GAME_WAIT_FOR_NEXT_ROUND:
			MJClient.room = ((Room) response.getObject());
			msg.what = UIStatus.GAME_WAIT_FOR_NEXT_ROUND;
			handler.sendMessage(msg);
			Log.i("", "Wait for other players back to next round.");
			break;
		default:
			break;
		}
	}
}
