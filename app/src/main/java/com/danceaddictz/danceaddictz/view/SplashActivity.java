package com.danceaddictz.danceaddictz.view;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.danceaddictz.danceaddictz.R;
import com.danceaddictz.danceaddictz.common.AddictzConstants;
import com.danceaddictz.danceaddictz.common.AddictzPreferences;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
*/
        TextView tx = (TextView)findViewById(R.id.title1);
        TextView tx2 = (TextView)findViewById(R.id.title2);
        // TextView tx1 = (TextView)findViewById(R.id.progress_msg);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),
                "fonts/Mistral.ttf");
        tx.setTypeface(custom_font);
        // tx1.setTypeface(custom_font);
        tx2.setTypeface(custom_font);


        new BackgroundSplashTask().execute();
    }


    /**
     * Async Task: can be used to load DB, images during which the splash screen
     * is shown to user
     */
    private class BackgroundSplashTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result) {
                startActivity(HomeActivity.makeIntent(SplashActivity.this));

            } else {
                startActivity(LoginActivity.makeIntent(SplashActivity.this));
            }

            finish();
        }


        @Override
        protected Boolean doInBackground(Void... params) {

            try {

                Thread.sleep(1000);
                // Session Manager
                //make a network call and check if user session is active
                return !TextUtils.isEmpty(AddictzPreferences.getString(AddictzConstants.AUTH_KEY, SplashActivity.this));

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return false;
        }
    }

}
