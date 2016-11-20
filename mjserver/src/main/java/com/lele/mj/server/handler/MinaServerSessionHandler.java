package com.lele.mj.server.handler;

import com.lele.entity.Action;
import com.lele.entity.Request;
import com.lele.entity.Room;
import com.lele.entity.User;
import com.lele.mj.server.MJServer;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinaServerSessionHandler implements IoHandler {
    Logger LOG = LoggerFactory.getLogger(MinaServerSessionHandler.class);

    public void sessionCreated(IoSession session) throws Exception {
        LOG.info("Session Created - {}", session);
    }

    public void sessionOpened(IoSession session) throws Exception {
        LOG.info("sessionOpened");

    }

    public void sessionClosed(IoSession session) throws Exception {
        LOG.info("sessionClosed {}", session);
    }

    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        LOG.info("sessionIdle");

    }

    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        LOG.info("exceptionCaught {}", cause.getMessage());

    }

    public void messageReceived(IoSession session, Object message) throws Exception {
        LOG.info("Message Received - {}", message);
        Request request = (Request) message;
        MJServer.serviceMap.get(request.getAction()).process(request, session);

    }

    public void messageSent(IoSession session, Object message) throws Exception {
        LOG.info("Message Sent - {}", message);

    }

    public void inputClosed(IoSession session) throws Exception {
        LOG.info("inputClosed");
        session.closeNow();

    }

}
