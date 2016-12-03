package com.lele.handler;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.lele.MJClient;
import com.lele.action.UIStatus;
import com.lele.activity.HallActivity;
import com.lele.activity.RoomActivity;
import com.lele.entity.User;

public class UIHandler extends Handler {
    private Intent intent;
    private Activity activity;

    public UIHandler(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        intent = new Intent();
        switch (msg.what) {
            case UIStatus.LOGIN_SUCCESS:
                User user = (User) msg.obj;
                MJClient.getInstance().setUser(user);
                intent.setClass(activity, HallActivity.class);
                activity.startActivity(intent);
                break;

            default:
                intent.setClass(activity, RoomActivity.class);
                if (activity.startActivityIfNeeded(intent, -1)) {
                    intent.setAction("refreshRoom");
                    intent.setFlags(msg.what);
                    activity.sendBroadcast(intent);
                } else {
                    activity.startActivity(intent);
                }

                break;
        }
    }
}
