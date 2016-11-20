package com.lele.mj.server;

import com.lele.entity.Action;
import com.lele.entity.Environment;
import com.lele.entity.Response;
import com.lele.entity.Room;
import com.lele.entity.User;
import com.lele.mj.server.service.ChatService;
import com.lele.mj.server.service.GameService;
import com.lele.mj.server.service.LoginService;
import com.lele.mj.server.service.Service;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MJServer {

    public static Environment environment = Environment.DEV;
    public static Map<User, IoSession> sessions = new ConcurrentHashMap<User, IoSession>();
    public static Map<Action, Service> serviceMap = new ConcurrentHashMap<Action, Service>();
    public static List<Room> rooms = new ArrayList<Room>();
    static Logger LOG = LoggerFactory.getLogger(MJServer.class);

    static {
        Collections.synchronizedCollection(rooms);
        Service gameService = new GameService();
        serviceMap.put(Action.CHAT, new ChatService());
        serviceMap.put(Action.LOGIN, new LoginService());
        serviceMap.put(Action.GAME_CREATE_ROOM, gameService);
        serviceMap.put(Action.GAME_JOIN_ROOM, gameService);
        serviceMap.put(Action.GAME_WAIT, gameService);
        serviceMap.put(Action.GAME_NEW_ROUND_START, gameService);
        serviceMap.put(Action.GAME_INIT, gameService);
        serviceMap.put(Action.GAME_PUT_CART, gameService);
        serviceMap.put(Action.GAME_MEET, gameService);
        serviceMap.put(Action.GAME_WIN, gameService);

    }

    public static Room getRoom(Room inputRoom) {
        for (Room room : rooms) {
            if (room.getId() == inputRoom.getId()) {
                return room;
            }
        }
        return null;
    }

    public static void notifyRoomMates(Action action, Room room) throws IOException {
        LOG.info("Notify users {}", room.getUsers());
        for (User user : room.getUsers()) {
            IoSession session = MJServer.sessions.get(user);
            Response newResponse = new Response(action, user, room);
            LOG.info("Sending newResponse {}", newResponse);
            session.write(newResponse);
        }
    }

}
