package com.lele.mj.server.process;

import com.lele.dao.UserDao;
import com.lele.entity.Action;
import com.lele.entity.ErrorCode;
import com.lele.entity.Request;
import com.lele.entity.Response;
import com.lele.entity.Room;
import com.lele.entity.User;
import com.lele.mj.server.MJServer;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RoomProcess {

    static Logger LOG = LoggerFactory.getLogger(RoomProcess.class);

    private static Room room;

    public static void createRoom(Request request, IoSession session, Action nextAction) throws IOException {
        if (hasRoomCart(request.getUser())) {
            room = new Room(generateRoomId());
            /*
             * while (MJServer.rooms.contains(room)) { room = new
			 * Room(generateRoomId()); }
			 */
            room.setRoomOwner(request.getUser());
            MJServer.rooms.add(room);
            session.write(new Response(nextAction, request.getUser(), room));
        } else {
            session.write(new Response(Action.ERROR, request.getUser(), ErrorCode.ROOM_CART_NOT_ENOUGH));
        }

    }

    private static boolean hasRoomCart(User user) {
        UserDao dao = new UserDao();
        User dbUser = dao.getUser(user.getId());
        if (dbUser != null && dbUser.getRoomCart() > 0) {
            return true;
        }
        return false;
    }

    public static void joinRoom(Request request, IoSession session) throws IOException {
        room = MJServer.getRoom((Room) request.getObject());
        if (room == null) {
            session.write(new Response(Action.HALL, request.getUser(), ErrorCode.ROOM_NOT_FOUND));
            return;
        }

        if (!room.getUsers().contains(request.getUser())) {
            if (room.getUsers().size() == 4) {
                session.write(new Response(Action.HALL, request.getUser(), ErrorCode.ROOM_IS_FULL));
            } else {
                User user = request.getUser();
                user.setOnLive(true);
                user.setRoomId(room.getId());
                room.getUsers().add(user);
                if (room.getUsers().size() == 4) {
                    GameProcess.initGame(request);
                } else {
                    MJServer.notifyRoomMates(Action.GAME_WAIT, room);
                }
            }
        } else if (room.getUsers().contains(request.getUser())) {
            for (User user : room.getUsers()) {
                if (user.equals(request.getUser())) {
                    user.setOnLive(true);
                }
            }

            if (room.getUsers().size() == 4) {
                MJServer.notifyRoomMates(Action.GAME_PUT_CART, room);
            } else {
                MJServer.notifyRoomMates(Action.GAME_WAIT, room);
            }
        }
    }

    private static int generateRoomId() {
        return 111;
    }

    public static void startNewRound(Request request) throws IOException {
        room = MJServer.getRoom((Room) request.getObject());
        room.setReadyNumberOfNextRound(room.getReadyNumberOfNextRound() + 1);
        if (room.getReadyNumberOfNextRound() < 4) {
            MJServer.notifyRoomMates(Action.GAME_WAIT_FOR_NEXT_ROUND, room);
        } else {
            GameProcess.initGame(request);
            room.setReadyNumberOfNextRound(0);
        }
    }
}
