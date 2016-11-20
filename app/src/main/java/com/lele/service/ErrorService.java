package com.lele.service;

import java.io.IOException;

import org.apache.mina.core.session.IoSession;

import android.os.Handler;
import android.util.Log;

import com.lele.entity.ErrorCode;
import com.lele.entity.Response;

public class ErrorService implements Service {

	public void process(Response response, IoSession session, Handler handler) throws IOException {
		ErrorCode errorCode = (ErrorCode) response.getObject();
		switch (errorCode) {
		case ROOM_CART_NOT_ENOUGH:
			Log.i("", "Room cart is not enough, please buy room cart.");
			break;
		case USER_NOT_FOUND:
			Log.i("", "The user id is not found.");
			break;
		case ROOM_IS_FULL:
			Log.i("", "The room is full.");
			break;
		case ROOM_NOT_FOUND:
			Log.i("", "The room is not exist.");
			break;

		default:
			break;
		}
	}

}
