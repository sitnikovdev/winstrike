package ru.prsolution.winstrike.ui.start;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;

import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.common.utils.TinyDB;
import ru.prsolution.winstrike.ui.login.SignInActivity;
import ru.prsolution.winstrike.ui.guides.GuideActivity;

public class SplashActivity extends AppCompatActivity {
    private TinyDB tinyDB;
    private Intent mainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.ac_splash);

        LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.animation_view);
        animationView.setAnimation("data.json");
        animationView.loop(true);
        animationView.playAnimation();


        tinyDB = new TinyDB(this);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if (!tinyDB.getBoolean("activity_executed")) {
                     mainIntent = new Intent(SplashActivity.this, GuideActivity.class);
                     tinyDB.putBoolean("activity_executed",true);
                } else {
//                    mainIntent = new Intent(SplashActivity.this, LocationActivity.class);
                    mainIntent = new Intent(SplashActivity.this, SignInActivity.class);
                }
                startActivity(mainIntent);
                finish();
            }
        }, 4000L);

}

}

