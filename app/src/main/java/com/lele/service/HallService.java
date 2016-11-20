package com.lele.service;

import java.io.IOException;

import org.apache.mina.core.session.IoSession;

import android.os.Handler;
import android.util.Log;

import com.lele.entity.Response;

public class HallService implements Service {

	public void process(Response response, IoSession session, Handler handler) throws IOException {
		switch (response.getAction()) {
		case HALL:
			Log.i("", response.getObject().toString());
			break;
		default:
			break;

		}

	}
}
