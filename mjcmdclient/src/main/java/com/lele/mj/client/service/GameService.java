package com.lele.mj.client.service;

import com.lele.entity.Action;
import com.lele.entity.Request;
import com.lele.entity.Response;
import com.lele.entity.Room;
import com.lele.mj.client.process.DisplayProcess;
import com.lele.mj.client.process.GameProcess;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GameService implements Service {

    Logger LOG = LoggerFactory.getLogger(GameService.class);

    public void process(Response response, IoSession session) throws IOException {
        Room room = ((Room) response.getObject());
        switch (response.getAction()) {
            case GAME_CREATE_ROOM:
                if (response.getObject() instanceof Room) {
                    LOG.info("Room {} have been created successfully.", room);
                    session.write(new Request(Action.GAME_JOIN_ROOM, response.getUser(), room));
                }
                break;
            case GAME_WAIT:
                room = ((Room) response.getObject());
                LOG.info("Join room sucessfully, wait for other players {}/4...", room.getUsers().size());
                break;

            case GAME_WAIT_FOR_NEXT_ROUND:
                room = ((Room) response.getObject());
                LOG.info("Wait for other players back to next round.");
                break;

            case GAME_PUT_CART:
                LOG.info("play game...");
                room = ((Room) response.getObject());
                if (!room.isRoundOver()) {
                    DisplayProcess.displayRoom(response, room);
                    GameProcess.handleAction(response, room, session);
                } else {
                    LOG.info("game is over and no one win. caculate scope...");
                    DisplayProcess.displayScope(response, room, session);
                }
                break;

            case GAME_WIN:
                LOG.info("game is over and someone win. caculate scope...");
                room = ((Room) response.getObject());
                DisplayProcess.displayScope(response, room, session);

            case LIVE:
                LOG.info("One user is off live.");
                room = ((Room) response.getObject());
                DisplayProcess.displayRoom(response, room);

            default:
                break;
        }
    }
}
