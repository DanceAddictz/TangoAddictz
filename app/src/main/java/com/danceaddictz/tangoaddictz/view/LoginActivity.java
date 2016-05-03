package com.danceaddictz.tangoaddictz.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.danceaddictz.tangoaddictz.R;

public class LoginActivity extends AppCompatActivity {

    int[] gal = {R.drawable.login_bg_one, R.drawable.login_bg_two, R.drawable.login_bg_three};
    int count = 1;
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
        setContentView(R.layout.activity_login);
/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
*/


        final ImageView bg = (ImageView) findViewById(R.id.img_background);
        final Animation zoomout = AnimationUtils.loadAnimation(this, R.anim.zoomout);
        bg.setAnimation(zoomout);

        zoomout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                bg.setImageResource(gal[count++]);
                if(count == gal.length)
                    count = 0;

                bg.startAnimation(zoomout);
            }
        });

        Button btn = (Button) findViewById(R.id.btn_signin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(HomeActivity.makeIntent(LoginActivity.this));
            }
        });

    }

}
