package com.lele.mjclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.lele.activity.R;

import com.lele.mj.client.MJClient;

public class HallActivity extends Activity implements OnClickListener {

	private Button createRoomButton;
	private Button joinRoomButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hall);

		createRoomButton = (Button) this.findViewById(R.id.createRoomButton);
		joinRoomButton = (Button) this.findViewById(R.id.joinRoomButton);

		createRoomButton.setOnClickListener(this);
		joinRoomButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.createRoomButton:
			MJClient.getInstance().createARoom();
			break;

		case R.id.joinRoomButton:
			MJClient.getInstance().joinARoom(111);
			break;
		default:
			break;
		}
	}
}
