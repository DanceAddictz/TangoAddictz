package com.danceaddictz.danceaddictz.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.danceaddictz.danceaddictz.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class LoginActivity extends AppCompatActivity {

    int[] gal = {R.drawable.login_bg_one, R.drawable.login_bg_two, R.drawable.login_bg_three};
    int count = 1;
    Animation zoomin;
    Animation zoomout;
    CallbackManager callbackManager;


    public static Intent makeIntent(Context context){

        Intent intent = new Intent(context,LoginActivity.class);
        // Closing all the Activities
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        return intent;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
*/

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_login);

        final ImageView bg = (ImageView) findViewById(R.id.img_background);

        zoomout = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
        bg.setAnimation(zoomout);

        zoomout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if(animation == zoomout) {

                    if (count == gal.length)
                        count = 0;

                    /*int i = gal[count++];
                    bg.setImageResource(i);*/

                    Glide.with(LoginActivity.this)
                            .load(gal[count++])
                            .into(bg);


                    bg.startAnimation(zoomin);
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
                return;
            }
        });

        zoomin = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        zoomin.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(animation == zoomin) {

                    if (count == gal.length)
                        count = 0;

                    Glide.with(LoginActivity.this)
                            .load(gal[count++])
                            .into(bg);
                    //bg.setImageResource(i);


                    bg.startAnimation(zoomout);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        Glide.with(LoginActivity.this)
                .load(R.drawable.login_bg_one)
                .into(bg);

       /* Button btn = (Button) findViewById(R.id.btn_signin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(HomeActivity.makeIntent(LoginActivity.this));
            }
        });*/

    }

}
