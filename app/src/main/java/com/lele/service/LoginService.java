package com.lele.service;

import java.io.IOException;

import org.apache.mina.core.session.IoSession;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lele.entity.Response;
import com.lele.action.UIStatus;

public class LoginService implements Service {

	public void process(Response response, IoSession session,Handler handler) throws IOException {
		switch (response.getAction()) {
		case LOGIN:
			if (response.getObject().equals(true)) {
				Message msg=new Message();
				msg.obj=response.getUser();
				msg.what=UIStatus.LOGIN_SUCCESS;
				handler.handleMessage(msg);
			} else {
				Log.i("", "Login failed!");
			}
			break;
		default:
			break;
		}
	}
}
