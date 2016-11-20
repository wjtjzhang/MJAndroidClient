package com.lele.activity;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lele.MJClient;
import com.lele.handler.MinaClientSessionHandler;
import com.lele.handler.UIHandler;

public class MainActivity extends Activity {

	private static final long CONNECT_TIMEOUT = 100000;
	private static final String HOST = "192.168.3.21";
	private static final int PORT = 2017;

	Handler handler = new UIHandler(this);
	private IoSession session;

	private String android_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		android_id = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
		
		Button localLogin = (Button) this.findViewById(R.id.localLogin);
		localLogin.setOnClickListener(new LoginOnClickListener());
		new Thread(networkTask).start();
	}

	private void connectMJServer() {
		NioSocketConnector connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
		connector.setHandler(new MinaClientSessionHandler(handler));
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));

		for (;;) {
			try {
				ConnectFuture future = connector.connect(new InetSocketAddress(InetAddress.getByName(HOST), PORT));
				future.awaitUninterruptibly();
				session = future.getSession();
				break;
			} catch (RuntimeIoException e) {
				Toast.makeText(MainActivity.this, "error:"+e.getMessage(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
				Toast.makeText(MainActivity.this, "error:"+e.getMessage(), Toast.LENGTH_LONG).show();
			}

		}
	}

	private class LoginOnClickListener implements View.OnClickListener {
		public void onClick(View v) {
			if (session != null) {
				try {
					MJClient.getInstance().login(android_id);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(MainActivity.this, "Server1 is not available,try again.", Toast.LENGTH_LONG).show();
				new Thread(networkTask).start();
			}
		}
	}

	Runnable networkTask = new Runnable() {
		@Override
		public void run() {
			connectMJServer();
		}

	};
}
