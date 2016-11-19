package com.lele.mj.client.service;

import java.io.IOException;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lele.entity.ErrorCode;
import com.lele.entity.Response;

public class ErrorService implements Service {

	Logger LOG = LoggerFactory.getLogger(ErrorService.class);

	public void process(Response response, IoSession session) throws IOException {
		ErrorCode errorCode = (ErrorCode) response.getObject();
		switch (errorCode) {
		case ROOM_CART_NOT_ENOUGH:
			LOG.info("Room cart is not enough, please buy room cart.");
			break;
		case USER_NOT_FOUND:
			LOG.info("The user id is not found.");
			break;
		case ROOM_IS_FULL:
			LOG.info("The room is full.");
			break;
		case ROOM_NOT_FOUND:
			LOG.info("The room is not exist.");
			break;

		default:
			break;
		}
	}

}
