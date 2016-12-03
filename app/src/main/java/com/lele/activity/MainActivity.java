package com.lele.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.lele.MJClient;
import com.lele.entity.User;
import com.lele.handler.MinaClientSessionHandler;
import com.lele.handler.UIHandler;
import com.lele.util.BitmapUtil;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class MainActivity extends Activity {

    private static final long CONNECT_TIMEOUT = 100000;
    private static final String HOST = "192.168.3.21";
    private static final int PORT = 2017;

    Handler handler = new UIHandler(this);
    private IoSession session;
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            connectMJServer();
        }

    };
    private String android_id;
    private Bitmap myHeaderBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android_id = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);

        Button localLogin = (Button) this.findViewById(R.id.localLogin);
        localLogin.setOnClickListener(new LoginOnClickListener());

        ImageView myHeaderIcon = (ImageView) this.findViewById(R.id.myHeaderIcon);
        myHeaderBitmap = BitmapUtil.createCircleImage(BitmapFactory.decodeResource(getResources(), R.drawable.default_header));
        myHeaderIcon.setImageBitmap(BitmapUtil.getScaleBitmap(myHeaderBitmap, localLogin.getMinHeight()));
        myHeaderIcon.setOnClickListener(new MyHeaderIconOnClickListener());

        new Thread(networkTask).start();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to quit?");
        builder.setTitle("Reminder:");
        builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                MainActivity.this.finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void connectMJServer() {
        NioSocketConnector connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
        connector.setHandler(new MinaClientSessionHandler(handler));
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));

        for (; ; ) {
            try {
                ConnectFuture future = connector.connect(new InetSocketAddress(InetAddress.getByName(HOST), PORT));
                future.awaitUninterruptibly();
                session = future.getSession();
                break;
            } catch (RuntimeIoException e) {
                Toast.makeText(MainActivity.this, "error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "error:" + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            try {
                myHeaderBitmap = BitmapUtil.createCircleImage(BitmapFactory.decodeStream(cr.openInputStream(uri)));

                ImageView myHeaderIcon = (ImageView) findViewById(R.id.myHeaderIcon);
                myHeaderIcon.setImageBitmap(myHeaderBitmap);
            } catch (FileNotFoundException e) {
                Toast.makeText(MainActivity.this, "Can not find the image.", Toast.LENGTH_LONG).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private class LoginOnClickListener implements View.OnClickListener {
        public void onClick(View v) {
            if (session != null) {
                try {
                    User user = new User(android_id);
                    user.setIcon(BitmapUtil.bitmap2Bytes(myHeaderBitmap));
                    MJClient.getInstance().login(user);
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

    private class MyHeaderIconOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
        }
    }
}
