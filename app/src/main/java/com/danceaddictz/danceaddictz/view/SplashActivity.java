package com.danceaddictz.danceaddictz.view;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.danceaddictz.danceaddictz.R;
import com.danceaddictz.danceaddictz.common.AddictzConstants;
import com.danceaddictz.danceaddictz.common.AddictzPreferences;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(SplashActivity.this)
                .enableAutoManage(SplashActivity.this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
                        // be available.
                        Log.d(TAG, "onConnectionFailed:" + connectionResult);
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


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
                if (AddictzPreferences.getInt(AddictzConstants.LOGIN_TYPE, SplashActivity.this)
                        == AddictzConstants.LOGIN_TYPES.GOOGLE.getId()){

                    OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
                    return opr.isDone();


                }else if (AddictzPreferences.getInt(AddictzConstants.LOGIN_TYPE, SplashActivity.this)
                        == AddictzConstants.LOGIN_TYPES.FB.getId()){
                    AccessToken accessToken = AccessToken.getCurrentAccessToken();
                    Profile.fetchProfileForCurrentAccessToken();
                    Profile profile = Profile.getCurrentProfile();
                    return profile != null;
                }else
                    return !TextUtils.isEmpty(AddictzPreferences.getString(AddictzConstants.AUTH_KEY, SplashActivity.this));

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return false;
        }
    }

}
