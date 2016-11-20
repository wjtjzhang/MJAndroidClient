package com.lele.mj.client.handler;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import android.os.Handler;
import android.util.Log;

import com.lele.entity.Response;
import com.lele.mj.client.MJClient;

public class MinaClientSessionHandler implements IoHandler {
	private Handler handler;

	public MinaClientSessionHandler(Handler handler) {
		this.handler=handler;
	}

	public MinaClientSessionHandler() {
		// TODO Auto-generated constructor stub
	}

	public void sessionCreated(IoSession session) throws Exception {
		MJClient.getInstance().setSession(session);
		Log.i("sessionCreated","sessionCreated");

	}

	public void sessionOpened(IoSession session) throws Exception {
		Log.i("sessionOpened","sessionOpened");

	}

	public void sessionClosed(IoSession session) throws Exception {
		Log.i("sessionClosed","sessionClosed");

	}

	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		Log.i("sessionIdle","sessionIdle");

	}

	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		Log.i("exceptionCaught - ", cause.getMessage());

	}

	public void messageReceived(IoSession session, Object message) throws Exception {
		Log.i("Message Received - ",message.toString());
		Response response = (Response) message;
		MJClient.getInstance().getServiceMap().get(response.getAction()).process(response, session, handler);

	}

	public void messageSent(IoSession session, Object message) throws Exception {
		Log.i("messageSent","messageSent");

	}

	public void inputClosed(IoSession session) throws Exception {
		Log.i("inputClosed","inputClosed");
		session.closeNow();
	}

}
