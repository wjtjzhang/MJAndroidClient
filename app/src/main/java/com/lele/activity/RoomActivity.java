package com.lele.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lele.MJClient;
import com.lele.action.UIStatus;
import com.lele.entity.Action;
import com.lele.entity.Cart;
import com.lele.entity.CartStatus;
import com.lele.entity.CartType;
import com.lele.entity.MeetType;
import com.lele.entity.User;
import com.lele.util.BitmapUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RoomActivity extends Activity {
    private View selectedCart;
    private Locale locale = Locale.CHINA;
    private LruCache<String, Bitmap> memoryCache;
    private MyBroadcastReceiver broadcastReceiver;
    private Point winSize;
    private boolean isRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        broadcastReceiver = new MyBroadcastReceiver();
        registerBroadcastReceiver(broadcastReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            initWinSize();
            loadImageIntoCache();
            displayRoom();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initWinSize() {
        WindowManager windowManager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        winSize = new Point();
        windowManager.getDefaultDisplay().getSize(winSize);
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerBroadcastReceiver(broadcastReceiver);
    }

    private void registerBroadcastReceiver(BroadcastReceiver broadcastReceiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("refreshRoom");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void loadImageIntoCache() throws IOException {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };

        for (int i = 1; i <= 9; i++) {
            for (CartType type : CartType.values()) {
                loadImageFromDisk(i, type);
            }
        }
    }

    private void loadImageFromDisk(int index, CartType type) throws IOException {
        String key = type.toString().toLowerCase(locale) + index;
        Bitmap bitmap = BitmapFactory.decodeStream(this.getAssets().open(key + ".png"));
        memoryCache.put(key, bitmap);
    }

    private Bitmap getBitmapFromCache(Cart cart) throws IOException {
        String key = cart.getCartType().toString().toLowerCase(locale) + cart.getCode();
        Bitmap bitmap = memoryCache.get(key);
        if (bitmap == null) {
            loadImageFromDisk(cart.getCode(), cart.getCartType());
        }
        return bitmap;
    }

    private ImageView loadCartImageData(Cart cart, float numberOfImage) throws IOException {
        ImageView imageView = new ImageView(this);
        Bitmap bitmap = getBitmapFromCache(cart);
        imageView.setImageBitmap(getScaleBitmapX(bitmap, numberOfImage, 0));
        imageView.setTag(cart);
        return imageView;
    }

    private void displayRoom() throws IOException {
        Log.i("RoomActivity", "Display room start...");
        if (MJClient.getInstance().getRoom() != null) {
            displayRoomInfo();
            displayPlayerHeaderInfo();

            List<User> users = MJClient.getInstance().getRoom().getUsers();
            if (users.size() == 4) {
                if (isRefresh) {
                    if (users.get(0).isYourTurn()) {
                        displayMine();
                    } else if (users.get(1).isYourTurn()) {
                        displayPlayer2();
                    } else if (users.get(2).isYourTurn()) {
                        displayPlayer3();
                    } else if (users.get(3).isYourTurn()) {
                        displayPlayer4();
                    }
                } else {
                    displayMine();
                    displayPlayer2();
                    displayPlayer3();
                    displayPlayer4();
                    isRefresh = true;
                }
            }
        }
    }

    private void displayPlayerHeaderInfo() {
        Log.i("RoomActivity", "Display player header info...");
        List<User> users = reOrderUsers();

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            ImageView headerImagePlaceHolder = getImagePlaceHolder(i);
            try {
                byte[] userIcon = null;

                if (users.size() == 4) {
                    user.setIcon(null);
                    FileInputStream fis = openFileInput(user.getId());
                    fis.read(userIcon);
                    fis.close();
                } else {
                    userIcon = user.getIcon();
                    FileOutputStream fos = openFileOutput(user.getId(), Context.MODE_PRIVATE);
                    fos.write(user.getIcon());
                    fos.close();
                }
                Bitmap bitmap = BitmapUtil.bytes2Bimap(userIcon);
                headerImagePlaceHolder.setImageBitmap(BitmapUtil.getScaleBitmap(bitmap, winSize.y / 10.0f));
            } catch (Exception e) {
                headerImagePlaceHolder.setImageBitmap(BitmapUtil.getScaleBitmap(BitmapUtil.createCircleImage(BitmapFactory.decodeResource(getResources(), R.drawable.default_header)), winSize.y / 10.0f));
            }
        }
    }

    private List<User> reOrderUsers() {
        List<User> users = new ArrayList<User>();
        int myPosition = MJClient.getInstance().getRoom().getUsers().indexOf(MJClient.getInstance().getUser());

        for (int i = myPosition; i < MJClient.getInstance().getRoom().getUsers().size(); i++) {
            users.add(MJClient.getInstance().getRoom().getUsers().get(i));
        }

        for (int i = 0; i < myPosition; i++) {
            users.add(MJClient.getInstance().getRoom().getUsers().get(i));
        }

        MJClient.getInstance().getRoom().getUsers().clear();
        MJClient.getInstance().getRoom().getUsers().addAll(users);
        return users;
    }

    private ImageView getImagePlaceHolder(int position) {
        ImageView imageView = null;
        switch (position) {
            case 0:
                imageView = (ImageView) this.findViewById(R.id.myHeaderInfo);
                break;
            case 1:
                imageView = (ImageView) this.findViewById(R.id.player2HeaderInfo);
                break;
            case 2:
                imageView = (ImageView) this.findViewById(R.id.player3HeaderInfo);
                break;
            case 3:
                imageView = (ImageView) this.findViewById(R.id.player4HeaderInfo);
                break;
            default:
                break;
        }
        return imageView;
    }

    private void displayRoomInfo() {
        Log.i("RoomActivity", "Display room info...");
        TextView time = (TextView) this.findViewById(R.id.timeNow);
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm", locale);
        time.setText(format.format(date) + "");
        TextView roomNo = (TextView) this.findViewById(R.id.roomNo);
        roomNo.setText(MJClient.getInstance().getRoom().getId() + "");
    }

    private void displayPlayer4() throws IOException {
        Log.i("RoomActivity", "Display player4 info...");
        final LinearLayout player4InCartsLayout = (LinearLayout) this.findViewById(R.id.player4InCarts);
        player4InCartsLayout.removeAllViews();
        List<Cart> player4InCarts = MJClient.getInstance().getRoom().getInCartsMap().get(MJClient.getInstance().getRoom().getUsers().get(3));

        List<Cart> player4MeetCarts = MJClient.getInstance().getRoom().getMeetCartsMap().get(MJClient.getInstance().getRoom().getUsers().get(3));
        int index = 0;
        while (index < player4MeetCarts.size()) {
            Cart cart = player4MeetCarts.get(index);
            if (cart.getMeetType() == MeetType.PENG) {
                for (int j = index; j < index + 3; j++) {
                    ImageView cartImage = new ImageView(this);
                    Bitmap bitmap = BitmapFactory.decodeStream(this.getAssets().open(
                            player4MeetCarts.get(j).getCartType().toString().toLowerCase(locale)
                                    + player4MeetCarts.get(j).getCode() + ".png"));
                    cartImage.setImageBitmap(getScaleBitmapY(bitmap, 22f, 90));
                    player4InCartsLayout.addView(cartImage);
                }
                index = index + 3;
            } else if (cart.getMeetType() == MeetType.GET_MING_GANG || cart.getMeetType() == MeetType.PUT_MING_GANG) {
                for (int j = index; j < index + 3; j++) {
                    ImageView cartImage = new ImageView(this);
                    cartImage.setId(j);
                    Bitmap bitmap = BitmapFactory.decodeStream(this.getAssets().open(
                            player4MeetCarts.get(j).getCartType().toString().toLowerCase(locale)
                                    + player4MeetCarts.get(j).getCode() + ".png"));
                    cartImage.setImageBitmap(getScaleBitmapY(bitmap, 22f, 90));
                    player4InCartsLayout.addView(cartImage);
                }

                final ImageView cartImage = new ImageView(this);
                Bitmap bitmap = BitmapFactory.decodeStream(this.getAssets().open(
                        player4MeetCarts.get(index + 3).getCartType().toString().toLowerCase(locale)
                                + player4MeetCarts.get(index + 3).getCode() + ".png"));
                cartImage.setImageBitmap(getScaleBitmapY(bitmap, 22f, 90));

                RelativeLayout relativeLayout = (RelativeLayout) this.findViewById(R.id.mainId);
                relativeLayout.addView(cartImage);
                final ImageView lastImage = (ImageView) player4InCartsLayout.getChildAt(player4InCartsLayout
                        .getChildCount() - 2);
                lastImage.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            public void onGlobalLayout() {
                                cartImage.setX(lastImage.getX() + player4InCartsLayout.getX() + 10);
                                cartImage.setY(lastImage.getY() + player4InCartsLayout.getY() - 10);
                            }
                        });

                index = index + 4;
            } else {
                for (int j = index; j < index + 3; j++) {
                    ImageView cartImage = new ImageView(this);
                    Bitmap bitmap = BitmapFactory.decodeStream(this.getAssets().open("player3_bg.png"));
                    cartImage.setImageBitmap(getScaleBitmapY(bitmap, 22f, 90));
                    player4InCartsLayout.addView(cartImage);
                }

                final ImageView cartImage = new ImageView(this);
                Bitmap bitmap = BitmapFactory.decodeStream(this.getAssets().open(
                        player4MeetCarts.get(index + 3).getCartType().toString().toLowerCase(locale)
                                + player4MeetCarts.get(index + 3).getCode() + ".png"));
                cartImage.setImageBitmap(getScaleBitmapY(bitmap, 22f, 90));

                RelativeLayout relativeLayout = (RelativeLayout) this.findViewById(R.id.mainId);
                relativeLayout.addView(cartImage);
                final ImageView lastImage = (ImageView) player4InCartsLayout.getChildAt(player4InCartsLayout
                        .getChildCount() - 2);
                lastImage.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            public void onGlobalLayout() {
                                cartImage.setX(lastImage.getX() + player4InCartsLayout.getX() + 10);
                                cartImage.setY(lastImage.getY() + player4InCartsLayout.getY() - 10);
                            }
                        });

                index = index + 4;
            }
        }

        for (int i = 0; i < player4InCarts.size(); i++) {
            ImageView cartImage = new ImageView(this);
            Bitmap bitmap = BitmapFactory.decodeStream(this.getAssets().open("player4_bg.png"));
            cartImage.setImageBitmap(getScaleBitmapY(bitmap, 29f, 0));
            player4InCartsLayout.addView(cartImage);
        }

        ImageView blankImage = new ImageView(this);
        blankImage.setMinimumHeight(20);

        if (MJClient.getInstance().getRoom().getUsers().get(3).isYourTurn()) {
            player4InCartsLayout.addView(blankImage, player4InCarts.size() - 1);
        }

        LinearLayout player4OutCartsLayout1 = (LinearLayout) this.findViewById(R.id.player4OutCarts1);
        LinearLayout player4OutCartsLayout2 = (LinearLayout) this.findViewById(R.id.player4OutCarts2);
        List<Cart> player4OutCarts = MJClient.getInstance().getRoom().getOutCartsMap().get(MJClient.getInstance().getRoom().getUsers().get(3));
        player4OutCartsLayout1.removeAllViews();
        player4OutCartsLayout2.removeAllViews();
        for (int i = 0; i < player4OutCarts.size(); i++) {
            ImageView cartImage = new ImageView(this);
            Cart cart = player4OutCarts.get(i);
            Bitmap bitmap = BitmapFactory.decodeStream(this.getAssets().open(
                    cart.getCartType().toString().toLowerCase(locale) + cart.getCode() + ".png"));
            cartImage.setImageBitmap(getScaleBitmapY(bitmap, 15f, 90));
            if (player4OutCartsLayout1.getChildCount() < 10) {
                player4OutCartsLayout1.addView(cartImage);
            } else {
                player4OutCartsLayout2.addView(cartImage);
            }
        }
    }

    private void displayPlayer3() throws IOException {
        Log.i("RoomActivity", "Display player3 info...");
        final LinearLayout player3InCartsLayout = (LinearLayout) this.findViewById(R.id.player3InCarts);
        player3InCartsLayout.removeViewsInLayout(1, player3InCartsLayout.getChildCount() - 1);

        List<Cart> player3InCarts = MJClient.getInstance().getRoom().getInCartsMap().get(MJClient.getInstance().getRoom().getUsers().get(2));
        for (int i = 0; i < player3InCarts.size(); i++) {
            ImageView cartImage = new ImageView(this);
            Bitmap bitmap = BitmapFactory.decodeStream(this.getAssets().open("player3_bg.png"));
            cartImage.setImageBitmap(getScaleBitmapX(bitmap, 30f, 0));
            player3InCartsLayout.addView(cartImage);
        }

        ImageView blankImage = new ImageView(this);
        blankImage.setMinimumWidth(20);
        if (MJClient.getInstance().getRoom().getUsers().get(2).isYourTurn()) {
            player3InCartsLayout.addView(blankImage, 2);
        }

        List<Cart> player3MeetCarts = MJClient.getInstance().getRoom().getMeetCartsMap().get(MJClient.getInstance().getRoom().getUsers().get(3));
        int index = 0;
        while (index < player3MeetCarts.size()) {
            Cart cart = player3MeetCarts.get(index);
            if (cart.getMeetType() == MeetType.PENG) {
                for (int j = index; j < index + 3; j++) {
                    ImageView cartImage = new ImageView(this);
                    Bitmap bitmap = BitmapFactory.decodeStream(this.getAssets().open(
                            player3MeetCarts.get(j).getCartType().toString().toLowerCase(locale)
                                    + player3MeetCarts.get(j).getCode() + ".png"));
                    cartImage.setImageBitmap(getScaleBitmapX(bitmap, 30f, 180));
                    player3InCartsLayout.addView(cartImage);
                }
                index = index + 3;
            } else if (cart.getMeetType() == MeetType.GET_MING_GANG || cart.getMeetType() == MeetType.PUT_MING_GANG) {
                for (int j = index; j < index + 3; j++) {
                    ImageView cartImage = new ImageView(this);
                    cartImage.setId(j);
                    Bitmap bitmap = BitmapFactory.decodeStream(this.getAssets().open(
                            player3MeetCarts.get(j).getCartType().toString().toLowerCase(locale)
                                    + player3MeetCarts.get(j).getCode() + ".png"));
                    cartImage.setImageBitmap(getScaleBitmapX(bitmap, 30f, 180));
                    player3InCartsLayout.addView(cartImage);
                }

                final ImageView cartImage = new ImageView(this);
                Bitmap bitmap = BitmapFactory.decodeStream(this.getAssets().open(
                        player3MeetCarts.get(index + 3).getCartType().toString().toLowerCase(locale)
                                + player3MeetCarts.get(index + 3).getCode() + ".png"));
                cartImage.setImageBitmap(getScaleBitmapX(bitmap, 30f, 180));

                RelativeLayout relativeLayout = (RelativeLayout) this.findViewById(R.id.mainId);
                relativeLayout.addView(cartImage);
                final ImageView lastImage = (ImageView) player3InCartsLayout.getChildAt(player3InCartsLayout
                        .getChildCount() - 2);
                lastImage.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            public void onGlobalLayout() {
                                cartImage.setX(lastImage.getX() + player3InCartsLayout.getX() - 10);
                                cartImage.setY(lastImage.getY() + player3InCartsLayout.getY() - 10);
                            }
                        });

                index = index + 4;
            } else {
                for (int j = index; j < index + 3; j++) {
                    ImageView cartImage = new ImageView(this);
                    Bitmap bitmap = BitmapFactory.decodeStream(this.getAssets().open("player3_bg.png"));
                    cartImage.setImageBitmap(getScaleBitmapX(bitmap, 30f, 180));
                    player3InCartsLayout.addView(cartImage);
                }

                final ImageView cartImage = new ImageView(this);
                Bitmap bitmap = BitmapFactory.decodeStream(this.getAssets().open(
                        player3MeetCarts.get(index + 3).getCartType().toString().toLowerCase(locale)
                                + player3MeetCarts.get(index + 3).getCode() + ".png"));
                cartImage.setImageBitmap(getScaleBitmapX(bitmap, 30f, 180));

                RelativeLayout relativeLayout = (RelativeLayout) this.findViewById(R.id.mainId);
                relativeLayout.addView(cartImage);
                final ImageView lastImage = (ImageView) player3InCartsLayout.getChildAt(player3InCartsLayout
                        .getChildCount() - 2);
                lastImage.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            public void onGlobalLayout() {
                                cartImage.setX(lastImage.getX() + player3InCartsLayout.getX() - 10);
                                cartImage.setY(lastImage.getY() + player3InCartsLayout.getY() - 10);
                            }
                        });

                index = index + 4;
            }
        }

        LinearLayout player3OutCartsLayout1 = (LinearLayout) this.findViewById(R.id.player3OutCarts1);
        LinearLayout player3OutCartsLayout2 = (LinearLayout) this.findViewById(R.id.player3OutCarts2);
        List<Cart> player3OutCarts = MJClient.getInstance().getRoom().getOutCartsMap().get(MJClient.getInstance().getRoom().getUsers().get(2));
        player3OutCartsLayout1.removeAllViews();
        player3OutCartsLayout2.removeAllViews();
        for (int i = 0; i < player3OutCarts.size(); i++) {
            ImageView cartImage = new ImageView(this);
            Cart cart = player3OutCarts.get(i);
            Bitmap bitmap = BitmapFactory.decodeStream(this.getAssets().open(
                    cart.getCartType().toString().toLowerCase(locale) + cart.getCode() + ".png"));
            cartImage.setImageBitmap(getScaleBitmapY(bitmap, 15f, 180));
            if (player3OutCartsLayout1.getChildCount() < 10) {
                player3OutCartsLayout1.addView(cartImage);
            } else {
                player3OutCartsLayout2.addView(cartImage);
            }
        }
    }

    private void displayPlayer2() throws IOException {
        Log.i("RoomActivity", "Display player2 info...");
        final LinearLayout player2InCartsLayout = (LinearLayout) this.findViewById(R.id.player2Incarts);
        player2InCartsLayout.removeViewsInLayout(1, player2InCartsLayout.getChildCount() - 1);

        List<Cart> player2InCarts = MJClient.getInstance().getRoom().getInCartsMap().get(MJClient.getInstance().getRoom().getUsers().get(1));
        for (int i = 0; i < player2InCarts.size(); i++) {
            ImageView cartImage = new ImageView(this);
            Bitmap bitmap = BitmapFactory.decodeStream(this.getAssets().open("player2_bg.png"));
            cartImage.setImageBitmap(getScaleBitmapY(bitmap, 29f, 0));
            player2InCartsLayout.addView(cartImage);
        }

        ImageView blankImage = new ImageView(this);
        blankImage.setMinimumHeight(20);
        if (MJClient.getInstance().getRoom().getUsers().get(1).isYourTurn()) {
            player2InCartsLayout.addView(blankImage, 2);
        }

        List<Cart> player2MeetCarts = MJClient.getInstance().getRoom().getMeetCartsMap().get(MJClient.getInstance().getRoom().getUsers().get(1));
        int index = 0;
        while (index < player2MeetCarts.size()) {
            Cart cart = player2MeetCarts.get(index);
            if (cart.getMeetType() == MeetType.PENG) {
                for (int j = index; j < index + 3; j++) {
                    ImageView cartImage = new ImageView(this);
                    Bitmap bitmap = BitmapFactory.decodeStream(this.getAssets().open(
                            player2MeetCarts.get(j).getCartType().toString().toLowerCase(locale)
                                    + player2MeetCarts.get(j).getCode() + ".png"));
                    cartImage.setImageBitmap(getScaleBitmapY(bitmap, 22f, 270));
                    player2InCartsLayout.addView(cartImage);
                }
                index = index + 3;
            } else if (cart.getMeetType() == MeetType.GET_MING_GANG || cart.getMeetType() == MeetType.PUT_MING_GANG) {
                for (int j = index; j < index + 3; j++) {
                    ImageView cartImage = new ImageView(this);
                    Bitmap bitmap = BitmapFactory.decodeStream(this.getAssets().open(
                            player2MeetCarts.get(j).getCartType().toString().toLowerCase(locale)
                                    + player2MeetCarts.get(j).getCode() + ".png"));
                    cartImage.setImageBitmap(getScaleBitmapY(bitmap, 22f, 270));
                    player2InCartsLayout.addView(cartImage);
                }

                final ImageView cartImage = new ImageView(this);
                Bitmap bitmap = BitmapFactory.decodeStream(this.getAssets().open(
                        player2MeetCarts.get(index + 3).getCartType().toString().toLowerCase(locale)
                                + player2MeetCarts.get(index + 3).getCode() + ".png"));
                cartImage.setImageBitmap(getScaleBitmapY(bitmap, 22f, 270));

                RelativeLayout relativeLayout = (RelativeLayout) this.findViewById(R.id.mainId);
                relativeLayout.addView(cartImage);
                final ImageView lastImage = (ImageView) player2InCartsLayout.getChildAt(player2InCartsLayout
                        .getChildCount() - 2);
                lastImage.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            public void onGlobalLayout() {
                                cartImage.setX(lastImage.getX() + player2InCartsLayout.getX() + 10);
                                cartImage.setY(lastImage.getY() + player2InCartsLayout.getY() - 10);
                            }
                        });

                index = index + 4;
            } else {
                for (int j = index; j < index + 3; j++) {
                    ImageView cartImage = new ImageView(this);
                    Bitmap bitmap = BitmapFactory.decodeStream(this.getAssets().open("player3_bg.png"));
                    cartImage.setImageBitmap(getScaleBitmapY(bitmap, 22f, 270));
                    player2InCartsLayout.addView(cartImage);
                }

                final ImageView cartImage = new ImageView(this);
                Bitmap bitmap = BitmapFactory.decodeStream(this.getAssets().open(
                        player2MeetCarts.get(index + 3).getCartType().toString().toLowerCase(locale)
                                + player2MeetCarts.get(index + 3).getCode() + ".png"));
                cartImage.setImageBitmap(getScaleBitmapY(bitmap, 22f, 270));

                RelativeLayout relativeLayout = (RelativeLayout) this.findViewById(R.id.mainId);
                relativeLayout.addView(cartImage);
                final ImageView lastImage = (ImageView) player2InCartsLayout.getChildAt(player2InCartsLayout
                        .getChildCount() - 2);
                lastImage.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            public void onGlobalLayout() {
                                cartImage.setX(lastImage.getX() + player2InCartsLayout.getX() + 10);
                                cartImage.setY(lastImage.getY() + player2InCartsLayout.getY() - 10);
                            }
                        });

                index = index + 4;
            }
        }

        LinearLayout player2OutCartsLayout1 = (LinearLayout) this.findViewById(R.id.player2OutCarts1);
        LinearLayout player2OutCartsLayout2 = (LinearLayout) this.findViewById(R.id.player2OutCarts2);
        List<Cart> player2OutCarts = MJClient.getInstance().getRoom().getOutCartsMap().get(MJClient.getInstance().getRoom().getUsers().get(1));
        player2OutCartsLayout1.removeAllViews();
        player2OutCartsLayout2.removeAllViews();
        for (int i = 0; i < player2OutCarts.size(); i++) {
            ImageView cartImage = new ImageView(this);
            Cart cart = player2OutCarts.get(i);
            Bitmap bitmap = BitmapFactory.decodeStream(this.getAssets().open(
                    cart.getCartType().toString().toLowerCase(locale) + cart.getCode() + ".png"));
            cartImage.setImageBitmap(getScaleBitmapY(bitmap, 15f, 270));
            if (player2OutCartsLayout1.getChildCount() < 10) {
                player2OutCartsLayout1.addView(cartImage);
            } else {
                player2OutCartsLayout2.addView(cartImage);
            }
        }
    }

    private void displayMine() throws IOException {
        Log.i("RoomActivity", "Display my info...");
        final LinearLayout myInCartsLayout = (LinearLayout) this.findViewById(R.id.myInCarts);
        myInCartsLayout.removeAllViews();

        List<Cart> myMeetCarts = MJClient.getInstance().getRoom().getMeetCartsMap().get(MJClient.getInstance().getUser());
        int index = 0;
        while (index < myMeetCarts.size()) {
            Cart cart = myMeetCarts.get(index);
            if (cart.getMeetType() == MeetType.PENG) {
                for (int j = index; j < index + 3; j++) {
                    ImageView cartImage = loadCartImageData(cart, 22f);
                    myInCartsLayout.addView(cartImage);

                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cartImage.getLayoutParams();
                    params.gravity = Gravity.BOTTOM;
                    cartImage.setLayoutParams(params);
                }
                index = index + 3;
            } else if (cart.getMeetType() == MeetType.GET_MING_GANG || cart.getMeetType() == MeetType.PUT_MING_GANG) {
                for (int j = index; j < index + 3; j++) {
                    ImageView cartImage = loadCartImageData(cart, 22f);
                    myInCartsLayout.addView(cartImage);

                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cartImage.getLayoutParams();
                    params.gravity = Gravity.BOTTOM;
                    cartImage.setLayoutParams(params);
                }

                final ImageView cartImage = loadCartImageData(cart, 22f);

                RelativeLayout relativeLayout = (RelativeLayout) this.findViewById(R.id.mainId);
                relativeLayout.addView(cartImage);
                final ImageView lastImage = (ImageView) myInCartsLayout.getChildAt(myInCartsLayout.getChildCount() - 2);
                lastImage.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            public void onGlobalLayout() {
                                cartImage.setX(lastImage.getX() + myInCartsLayout.getX() - 10);
                                cartImage.setY(lastImage.getY() + myInCartsLayout.getY() - 10);
                            }
                        });

                index = index + 4;
            } else {
                for (int j = index; j < index + 3; j++) {
                    ImageView cartImage = loadCartImageData(cart, 22f);
                    Bitmap bitmap = BitmapFactory.decodeStream(this.getAssets().open("player3_bg.png"));
                    cartImage.setImageBitmap(getScaleBitmapX(bitmap, 22f, 0));
                    myInCartsLayout.addView(cartImage);

                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cartImage.getLayoutParams();
                    params.gravity = Gravity.BOTTOM;
                    cartImage.setLayoutParams(params);
                }

                final ImageView cartImage = loadCartImageData(cart, 22f);

                RelativeLayout relativeLayout = (RelativeLayout) this.findViewById(R.id.mainId);
                relativeLayout.addView(cartImage);
                final ImageView lastImage = (ImageView) myInCartsLayout.getChildAt(myInCartsLayout.getChildCount() - 2);
                lastImage.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            public void onGlobalLayout() {
                                cartImage.setX(lastImage.getX() + myInCartsLayout.getX() - 10);
                                cartImage.setY(lastImage.getY() + myInCartsLayout.getY() - 10);
                            }
                        });

                index = index + 4;
            }
        }

        boolean isActionRequired = handleAction();

        List<Cart> myInCarts = MJClient.getInstance().getRoom().getInCartsMap().get(MJClient.getInstance().getUser());
        for (int i = 0; i < myInCarts.size(); i++) {
            Cart cart = myInCarts.get(i);
            ImageView cartImage = loadCartImageData(cart, 17f);
            if (MJClient.getInstance().getUser().isYourTurn() && !isActionRequired) {
                cartImage.setOnClickListener(new CartOnClickListener());
            } else {
                cartImage.setOnClickListener(null);
            }

            myInCartsLayout.addView(cartImage);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cartImage.getLayoutParams();
            params.gravity = Gravity.BOTTOM;
            cartImage.setLayoutParams(params);
        }

        ImageView blankImage = new ImageView(this);
        blankImage.setMinimumWidth(30);
        if (MJClient.getInstance().getUser().isYourTurn() && !isActionRequired && MJClient.getInstance().getRoom().getCurrentGetCart() != null) {
            myInCartsLayout.addView(blankImage, myInCartsLayout.getChildCount() - 1);
        }

        LinearLayout myOutCartsLayout1 = (LinearLayout) this.findViewById(R.id.myOutCarts1);
        LinearLayout myOutCartsLayout2 = (LinearLayout) this.findViewById(R.id.myOutCarts2);
        myOutCartsLayout1.removeAllViews();
        myOutCartsLayout2.removeAllViews();
        List<Cart> myOutCarts = MJClient.getInstance().getRoom().getOutCartsMap().get(MJClient.getInstance().getUser());
        for (int i = 0; i < myOutCarts.size(); i++) {
            Cart cart = myOutCarts.get(i);
            ImageView cartImage = loadCartImageData(cart, 22f);
            // cartImage.setImageBitmap(getScaleBitmapY(cartImage.getDrawingCache(),
            // 15f, 0));
            if (myOutCartsLayout1.getChildCount() < 10) {
                myOutCartsLayout1.addView(cartImage);
            } else {
                myOutCartsLayout2.addView(cartImage);
            }
        }
    }

    private boolean handleAction() throws IOException {
        boolean isActionRequired = true;
        LinearLayout actionsLayout = (LinearLayout) this.findViewById(R.id.actions);
        actionsLayout.removeViewsInLayout(1, actionsLayout.getChildCount() - 1);

        if (MJClient.getInstance().getUser().isYourTurn()) {
            if (MJClient.getInstance().getUser().isWin() && !MJClient.getInstance().getUser().getMeetType().equals(MeetType.AN_GANG)) {
                ImageView winImage = new ImageView(this);
                winImage.setTag(MeetType.WIN);
                winImage.setOnClickListener(new ActionOnClickListener());
                winImage.setImageBitmap(getScaleBitmapX(BitmapFactory.decodeStream(this.getAssets().open("win.png")),
                        17, 0));
                actionsLayout.addView(winImage);

            } else if (MJClient.getInstance().getUser().isWin() && MJClient.getInstance().getUser().getMeetType().equals(MeetType.AN_GANG)) {
                ImageView winImage = new ImageView(this);
                winImage.setTag(MeetType.WIN);
                Bitmap bitmap = BitmapFactory.decodeStream(this.getAssets().open("win.png"));
                winImage.setOnClickListener(new ActionOnClickListener());
                winImage.setImageBitmap(getScaleBitmapX(bitmap, 17, 0));
                actionsLayout.addView(winImage);

                ImageView gangImage = new ImageView(this);
                winImage.setTag(MeetType.AN_GANG);
                gangImage.setOnClickListener(new ActionOnClickListener());
                gangImage.setImageBitmap(getScaleBitmapX(BitmapFactory.decodeStream(this.getAssets().open("gang.png")),
                        17, 0));
                actionsLayout.addView(gangImage);
            } else {
                switch (MJClient.getInstance().getUser().getMeetType()) {
                    case PENG:
                        ImageView pengImage = new ImageView(this);
                        pengImage.setTag(MeetType.PENG);
                        pengImage.setOnClickListener(new ActionOnClickListener());
                        pengImage.setImageBitmap(getScaleBitmapX(BitmapFactory.decodeStream(this.getAssets().open("peng.png")),
                                17, 0));
                        actionsLayout.addView(pengImage);
                        break;

                    case PUT_MING_GANG:
                        ImageView putPengImage = new ImageView(this);
                        putPengImage.setTag(MeetType.PENG);
                        putPengImage.setOnClickListener(new ActionOnClickListener());
                        putPengImage.setImageBitmap(getScaleBitmapX(
                                BitmapFactory.decodeStream(this.getAssets().open("peng.png")), 17, 0));
                        actionsLayout.addView(putPengImage);

                        ImageView putGangImage = new ImageView(this);
                        putGangImage.setTag(MeetType.PUT_MING_GANG);
                        putGangImage.setOnClickListener(new ActionOnClickListener());
                        putGangImage.setImageBitmap(getScaleBitmapX(
                                BitmapFactory.decodeStream(this.getAssets().open("gang.png")), 17, 0));
                        actionsLayout.addView(putGangImage);
                        break;
                    case GET_MING_GANG:
                        ImageView getGangImage = new ImageView(this);
                        getGangImage.setTag(MeetType.GET_MING_GANG);
                        getGangImage.setOnClickListener(new ActionOnClickListener());
                        getGangImage.setImageBitmap(getScaleBitmapX(
                                BitmapFactory.decodeStream(this.getAssets().open("gang.png")), 17, 0));
                        actionsLayout.addView(getGangImage);
                        break;

                    case AN_GANG:
                        ImageView anGangImage = new ImageView(this);
                        anGangImage.setTag(MeetType.AN_GANG);
                        anGangImage.setOnClickListener(new ActionOnClickListener());
                        anGangImage.setImageBitmap(getScaleBitmapX(
                                BitmapFactory.decodeStream(this.getAssets().open("gang.png")), 17, 0));
                        actionsLayout.addView(anGangImage);
                        break;

                    default:
                        isActionRequired = false;
                        break;
                }
            }
        }
        return isActionRequired;
    }

    private Bitmap getScaleBitmapX(Bitmap bitmap, float numberForFullScreen, float rotateDegree) {
        float scaleWidth = 1, scaleHeight = 1;
        float scale = (winSize.x / numberForFullScreen / bitmap.getWidth() * 1.0f);
        scaleWidth = scaleWidth * scale;
        scaleHeight = scaleHeight * scale;
        Matrix matrix = new Matrix();
        matrix.setRotate(rotateDegree);
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private Bitmap getScaleBitmapY(Bitmap bitmap, float numberForFullScreen, float rotateDegree) {
        float scaleWidth = 1, scaleHeight = 1;
        float scale = (winSize.y / numberForFullScreen / bitmap.getHeight() * 1.0f);
        scaleWidth = scaleWidth * scale;
        scaleHeight = scaleHeight * scale;
        Matrix matrix = new Matrix();
        matrix.setRotate(rotateDegree);
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private void displayRoundWinDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View round_win_dialog = inflater.inflate(R.layout.round_win_dialog, (ViewGroup) findViewById(R.id.round_win_dialog));

        setRoundWinDetail(inflater, (LinearLayout) round_win_dialog.findViewById(R.id.me), MJClient.getInstance().getRoom().getUsers().get(0));
        setRoundWinDetail(inflater, (LinearLayout) round_win_dialog.findViewById(R.id.player2), MJClient.getInstance().getRoom().getUsers().get(1));
        setRoundWinDetail(inflater, (LinearLayout) round_win_dialog.findViewById(R.id.player3), MJClient.getInstance().getRoom().getUsers().get(2));
        setRoundWinDetail(inflater, (LinearLayout) round_win_dialog.findViewById(R.id.player4), MJClient.getInstance().getRoom().getUsers().get(3));

        AlertDialog.Builder builder = new AlertDialog.Builder(RoomActivity.this);
        builder.setView(round_win_dialog);
        builder.setTitle("Score for this round:");
        builder.setPositiveButton("Next round.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                MJClient.getInstance().send(Action.GAME_NEW_ROUND_START);

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(getWindowManager().getDefaultDisplay().getWidth() * 95 / 100, getWindowManager().getDefaultDisplay().getHeight() * 95 / 100);
    }

    private void setRoundWinDetail(LayoutInflater inflater, LinearLayout viewById, User user) {
        View label_value = inflater.inflate(R.layout.round_win_label_value, (ViewGroup) findViewById(R.id.round_win_label_value));
        ImageView winUser = (ImageView) label_value.findViewById(R.id.winUser);
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(this.getAssets().open(
                    user.getId() + ".png"));
            winUser.setImageBitmap(bitmap);
        } catch (IOException e) {
            winUser.setBackgroundResource(R.drawable.default_header);
        }
        TextView winDetail = (TextView) label_value.findViewById(R.id.winDetail);
        winDetail.setText(user.getScoreDetail());

        TextView winScore = (TextView) label_value.findViewById(R.id.winScore);
        winScore.setText(user.getScore());

        if (user.isWin()) {
            ImageView winIcon = (ImageView) label_value.findViewById(R.id.winIcon);
            winIcon.setImageResource(R.drawable.win_icon);
        }
    }

    private void displayFinalWinDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View final_win_dialog = inflater.inflate(R.layout.final_win_dialog, (ViewGroup) findViewById(R.id.final_win_dialog));

        setFinalWinDetail(inflater, (LinearLayout) final_win_dialog.findViewById(R.id.me), MJClient.getInstance().getRoom().getUsers().get(0));
        setFinalWinDetail(inflater, (LinearLayout) final_win_dialog.findViewById(R.id.player2), MJClient.getInstance().getRoom().getUsers().get(1));
        setFinalWinDetail(inflater, (LinearLayout) final_win_dialog.findViewById(R.id.player3), MJClient.getInstance().getRoom().getUsers().get(2));
        setFinalWinDetail(inflater, (LinearLayout) final_win_dialog.findViewById(R.id.player4), MJClient.getInstance().getRoom().getUsers().get(3));

        AlertDialog.Builder builder = new AlertDialog.Builder(RoomActivity.this);
        builder.setView(final_win_dialog);
        builder.setTitle("Final Score:");
        builder.setPositiveButton("Share", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(getWindowManager().getDefaultDisplay().getWidth() * 95 / 100, getWindowManager().getDefaultDisplay().getHeight() * 95 / 100);
    }

    private void setFinalWinDetail(LayoutInflater inflater, LinearLayout layout, User user) {
        layout.removeAllViews();

        View label_value = inflater.inflate(R.layout.final_win_label_value, (ViewGroup) findViewById(R.id.final_win_label_value));
        TextView zmValue = (TextView) label_value.findViewById(R.id.zmValue);
        zmValue.setText("" + user.getZmValue());

        TextView jpValue = (TextView) label_value.findViewById(R.id.jpValue);
        jpValue.setText("" + user.getJpValue());

        TextView dpValue = (TextView) label_value.findViewById(R.id.dpValue);
        dpValue.setText("" + user.getDpValue());

        TextView agValue = (TextView) label_value.findViewById(R.id.agValue);
        agValue.setText("" + user.getAgValue());

        TextView mgValue = (TextView) label_value.findViewById(R.id.mgValue);
        mgValue.setText("" + user.getMgValue());

        layout.addView(label_value);
    }


    private class CartOnClickListener implements View.OnClickListener {
        public void onClick(View cartImage) {
            if (selectedCart == null) {
                cartImage.setTop(cartImage.getTop() - 20);
                selectedCart = cartImage;
            } else if (!selectedCart.equals(cartImage)) {
                selectedCart.setTop(selectedCart.getTop() + 20);
                cartImage.setTop(cartImage.getTop() - 20);
                selectedCart = cartImage;
            } else if (selectedCart.equals(cartImage)) {
                Cart cart = (Cart) cartImage.getTag();
                cart.setCartStatus(CartStatus.OUT);
                cart.setPlayTime(System.currentTimeMillis());
                MJClient.getInstance().getRoom().setCurrentPutCart(cart);
                MJClient.getInstance().getRoom().getInCartsMap().get(MJClient.getInstance().getUser()).remove(cart);
                MJClient.getInstance().getRoom().getOutCartsMap().get(MJClient.getInstance().getUser()).add(cart);

                MJClient.getInstance().send(Action.GAME_PUT_CART);
                try {
                    displayMine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                selectedCart = null;
            }
        }
    }

    private class ActionOnClickListener implements View.OnClickListener {
        public void onClick(View v) {
            MeetType meetType = (MeetType) v.getTag();
            switch (meetType) {
                case WIN:
                    MJClient.getInstance().send(Action.GAME_WIN);
                    break;
                default:
                    MJClient.getInstance().getUser().setMeetType(meetType);
                    MJClient.getInstance().send(Action.GAME_MEET);
                    MJClient.getInstance().getRoom().setCurrentGetCart(null);
                    break;
            }
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context content, Intent intent) {
            try {
                Log.i("RoomActivity", "Broad receive message...");
                switch (intent.getFlags()) {
                    case UIStatus.WIN_DIALOG:
                        if (MJClient.getInstance().getRoom().getCurrentRound() >= 8) {
                            displayFinalWinDialog();
                        }
                        reOrderUsers();
                        displayRoundWinDialog();
                        break;
                    default:
                        displayRoom();
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
