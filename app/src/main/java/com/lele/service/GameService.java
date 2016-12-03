package com.lele.service;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lele.entity.Action;
import com.lele.entity.Request;
import com.lele.entity.Response;
import com.lele.entity.Room;
import com.lele.MJClient;
import com.lele.action.UIStatus;

import org.apache.mina.core.session.IoSession;

import java.io.IOException;

public class GameService implements Service {
    public void process(Response response, IoSession session, Handler handler) throws IOException {
        Room room = ((Room) response.getObject());
        MJClient.getInstance().setUser(response.getUser());
        MJClient.getInstance().setRoom(room);
        Message msg = handler.obtainMessage();
        switch (response.getAction()) {
            case GAME_CREATE_ROOM:
                Log.i("", "Room have been created successfully - " + room);
                session.write(new Request(Action.GAME_JOIN_ROOM, response.getUser(), room));
                break;
            case GAME_WAIT:
                msg.what = UIStatus.GAME_WAIT;
                handler.obtainMessage();
                handler.sendMessage(msg);
                Log.i("", "Join room sucessfully, wait for other players " + room.getUsers().size() + "/4..."
                        + room);
                break;

            case GAME_PUT_CART:
                Log.i("", "play game...");
                if (!room.isRoundOver()) {
                    msg.what = UIStatus.GAME_PUT_CART;
                    handler.sendMessage(msg);
                } else {
                    msg.what = UIStatus.WIN_DIALOG;
                    handler.sendMessage(msg);
                    Log.i("", "game is over and no one win. caculate scope...");
                }
                break;

            case GAME_WIN:
                Log.i("", "game is over and someone win. caculate scope...");
                msg.what = UIStatus.WIN_DIALOG;
                handler.sendMessage(msg);
                break;

            case GAME_WAIT_FOR_NEXT_ROUND:
                msg.what = UIStatus.GAME_WAIT_FOR_NEXT_ROUND;
                handler.sendMessage(msg);
                Log.i("", "Wait for other players back to next round.");
                break;
            default:
                break;
        }
    }
}