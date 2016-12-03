package com.lele.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lele.MJClient;
import com.lele.util.BitmapUtil;

public class HallActivity extends Activity implements OnClickListener {

    private Button createRoomButton;
    private Button joinRoomButton;
    private int roomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall);

        createRoomButton = (Button) this.findViewById(R.id.createRoomButton);
        joinRoomButton = (Button) this.findViewById(R.id.joinRoomButton);

        createRoomButton.setOnClickListener(this);
        joinRoomButton.setOnClickListener(this);

        ImageView myIcon = (ImageView) this.findViewById(R.id.myIcon);
        myIcon.setImageBitmap(BitmapUtil.getScaleBitmap(BitmapUtil.bytes2Bimap(MJClient.getInstance().getUser().getIcon()), getWindow().getWindowManager().getDefaultDisplay().getHeight() / 4));

        TextView roomCart = (TextView) this.findViewById(R.id.roomCart);
        roomCart.setText(MJClient.getInstance().getUser().getRoomCart() + "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.createRoomButton:
                MJClient.getInstance().createARoom();
                break;

            case R.id.joinRoomButton:
                displayJoinRoomDialog();
                break;
            default:
                break;
        }
    }

    private void displayJoinRoomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HallActivity.this);
        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
        builder.setView(editText);
        builder.setTitle("Input room number:");
        builder.setPositiveButton("Join", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                roomNumber = Integer.valueOf(editText.getText().toString()).intValue();
                MJClient.getInstance().joinARoom(roomNumber);
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
