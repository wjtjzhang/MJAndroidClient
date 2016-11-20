package com.lele.handler;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lele.entity.User;
import com.lele.MJClient;
import com.lele.action.UIStatus;
import com.lele.activity.HallActivity;
import com.lele.activity.RoomActivity;

public class UIHandler extends Handler {
	private Intent intent;
	private Activity activity;
	private boolean isGameStart = false;

	public Activity getActivity() {
		return activity;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		switch (msg.what) {
		case UIStatus.LOGIN_SUCCESS:
			intent = new Intent();
			User user = (User) msg.obj;
			MJClient.getInstance().setUser(user);
			intent.setClass(activity, HallActivity.class);
			activity.startActivity(intent);
			break;

		case UIStatus.GAME_WAIT:
			intent = new Intent();
			intent.setClass(activity, RoomActivity.class);
			activity.startActivity(intent);
			break;

		case UIStatus.GAME_PUT_CART:
			Log.i("", "isGameStart = " + isGameStart);
			if (isGameStart) {
				intent = new Intent();
				intent.setAction("refreshRoom");
				activity.sendBroadcast(intent);
			} else {
				intent = new Intent();
				intent.setClass(activity, RoomActivity.class);
				activity.startActivity(intent);
				isGameStart = true;
			}
			break;

		default:
			break;
		}
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public UIHandler(Activity activity) {
		this.activity = activity;
	}
}
