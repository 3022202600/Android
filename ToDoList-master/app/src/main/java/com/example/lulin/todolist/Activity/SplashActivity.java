package com.example.lulin.todolist.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.example.lulin.todolist.R;
import com.example.lulin.todolist.Receiver.NetworkReceiver;
import com.example.lulin.todolist.Service.AlarmService;
import com.example.lulin.todolist.Utils.FileUtils;
import com.example.lulin.todolist.Utils.NetWorkUtils;
import com.example.lulin.todolist.Utils.SPUtils;
import com.example.lulin.todolist.Bean.User;

import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import site.gemus.openingstartanimation.NormalDrawStrategy;
import site.gemus.openingstartanimation.OpeningStartAnimation;

public class SplashActivity extends BasicActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1500;
    private static final String APP_ID = "927c56abf64644a9b9a7424df058ec20";
    private NetworkReceiver networkReceiver;
    private FileUtils fileUtils;
    private static final String KEY_VIBRATE = "vibrator";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super. onCreate(savedInstanceState);
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},10);
        //复制assets下的资源文件到sd卡
        fileUtils = new FileUtils();
        fileUtils.copyData(getApplicationContext());
        SPUtils.put(this, "isFocus", false);


        new Thread(new Runnable() {
            @Override
            public void run() {
                if (NetWorkUtils.isNetworkConnected(getApplication())) {
                    Bmob.initialize(getApplication(), APP_ID);

                }

                User user = new User();
                user.setUsername("" + System.currentTimeMillis());
                user.setPassword("" + System.currentTimeMillis());
                user.signUp(new SaveListener<User>() {

                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null) {
                            Log.e("dd", "");
                        } else {
                            Log.e("dd", e.toString());
                        }
                    }
                });

            }
        }).start();


        Resources res = this.getResources();
        startService(new Intent(this, AlarmService.class));
        OpeningStartAnimation openingStartAnimation = new OpeningStartAnimation.Builder(this)
                .setDrawStategy(new NormalDrawStrategy()) //设置动画效果
                .setAppIcon(res.getDrawable(R.drawable.ic_launcher)) //设置图
//                .setColorOfAppIcon() //设置绘制图标线条的颜色
//                .setAppName("Do it") //设置app名称
                .setColorOfAppName(R.color.icon_color) //设置app名称颜色
                .setAppStatement("生命不息，奋斗不止") //设置一句话描述
                .setColorOfAppStatement(R.color.icon_color) // 设置一句话描述的颜色
                .create();
        openingStartAnimation.show(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                if (User.getCurrentUser(User.class) == null) {
                    Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }

            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
