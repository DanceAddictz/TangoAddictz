package com.danceaddictz.danceaddictz.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.danceaddictz.danceaddictz.R;
import com.danceaddictz.danceaddictz.common.AddictzConstants;
import com.danceaddictz.danceaddictz.common.AddictzPreferences;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    int[] gal = {R.drawable.login_bg_one, R.drawable.login_bg_two, R.drawable.login_bg_three};

    private static final int RC_SIGN_IN = 9001;

    int count = 1;
    Animation zoomin;
    Animation zoomout;
    CallbackManager callbackManager;
    private ProgressDialog mProgressDialog;

    private  GoogleApiClient mGoogleApiClient;
    private CoordinatorLayout coordinatorLayout;

    private boolean signInFlag = false;
    private LoginButton mFbLoginButton;


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

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinator_layout);


        //google sign in init
        initGoogleSignIn();

        //facebook sign in init
        initFBSignIn();

        final ImageView bg = (ImageView) findViewById(R.id.img_background);

        zoomout = AnimationUtils.loadAnimation(this, R.anim.zoomout);
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

        zoomin = AnimationUtils.loadAnimation(this, R.anim.zoomin);
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

    private void initFBSignIn() {
        mFbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);

        mFbLoginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_about_me"));
        callbackManager = CallbackManager.Factory.create();


        // Callback registration
        mFbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                Log.v("facebook response is", response.toString());
                                Profile profile = Profile.getCurrentProfile();
                                if (profile != null) {

                                    AddictzPreferences.putInt(AddictzConstants.LOGIN_TYPE, AddictzConstants.LOGIN_TYPES.FB.getId(), LoginActivity.this);
                                    AddictzPreferences.putString(AddictzConstants.FACEBOOK_ID, profile.getId(), LoginActivity.this);
                                    AddictzPreferences.putString(AddictzConstants.NAME, profile.getName(), LoginActivity.this);
                                    AddictzPreferences.putString(AddictzConstants.FIRST_NAME, profile.getFirstName(), LoginActivity.this);
                                    AddictzPreferences.putString(AddictzConstants.LAST_NAME, profile.getLastName(), LoginActivity.this);
                                    AddictzPreferences.putString(AddictzConstants.PHOTO_URL, profile.getProfilePictureUri(100,100).toString(), LoginActivity.this);

                                }
                               /* JSONObject fbGraphObject = response.getJSONObject();
//                                JSONObject fbGraphObject = fbJsonData.getJSONObject("graphObject");

                                Gson gson = new Gson();

                                try {
                                    FbResponseData fbResData = gson.fromJson(object.toString(), FbResponseData.class);

                                    String email = fbResData.getEmail();
                                    String fbId = fbResData.getId();
                                    String gender = fbResData.getGender();
//                                    String fbName = fbResData.getName();
                                    int age_range = fbResData.getAge_range().getMin();


//                                    int firstSpace = fbName.indexOf(" "); // detect the first space character
//                                    String firstName = fbName.substring(0, firstSpace);  // get everything upto the first space character
//                                    String lastName = fbName.substring(firstSpace).trim();
                                    AppPreferences.putString(AppConstants.EMAIL, email, LoginSignupActivity.this);
                                    AppPreferences.putString(AppConstants.FACEBOOK_ID, fbId, LoginSignupActivity.this);
                                    AppPreferences.putString(AppConstants.GENDER, gender, LoginSignupActivity.this);
                                    AppPreferences.putInt(AppConstants.AGE_GROUP, age_range, LoginSignupActivity.this);
                                    AppPreferences.putInt(AppConstants.LOGIN_TYPE, AppConstants.LOGIN_TYPES.FB.getId(), LoginSignupActivity.this);

                                    ArrayMap<String, String> stringStringArrayMap = new ArrayMap<>();
                                    stringStringArrayMap.put("facebook_id", fbId);

                                    //register with Shopsup
                                    presenter.loginWithFacebook(stringStringArrayMap);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }*/
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,age_range");
                request.setParameters(parameters);
                request.executeAsync();

                startActivity(HomeActivity.makeIntent(LoginActivity.this));

            }

            @Override
            public void onCancel() {
                // App code
                Log.v("LoginActivity facebook", "cancel");
                Toast.makeText(LoginActivity.this, "Cancelled log in through Facebook", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                if (exception != null && exception.getCause() != null) {
                    Log.v("LoginActivity facebook", exception.getCause().toString());
                    Toast.makeText(LoginActivity.this, "Failed to login through Facebook", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initGoogleSignIn(){

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
                        // be available.
                        Log.d(TAG, "onConnectionFailed:" + connectionResult);
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customize sign-in button. The sign-in button can be displayed in
        // multiple sizes and color schemes. It can also be contextually
        // rendered based on the requested scopes. For example. a red button may
        // be displayed when Google+ scopes are requested, but a white button
        // may be displayed when only basic profile is requested. Try adding the
        // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
        // difference.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());

         setGooglePlusButtonText( signInButton, "Log in with Google");

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    /**
     * Google sign in
     */
    private void signIn() {
        signInFlag = true;
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    /**
     * Google sign in handle
     * @param result
     */
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(this, "Welcome " + acct.getDisplayName(), Toast.LENGTH_LONG).show();

            AddictzPreferences.putInt(AddictzConstants.LOGIN_TYPE, AddictzConstants.LOGIN_TYPES.GOOGLE.getId(), this);

            String gId = acct.getId();
            String firstName = acct.getDisplayName();

            AddictzPreferences.putString(AddictzConstants.NAME, firstName, this);
            AddictzPreferences.putString(AddictzConstants.EMAIL, acct.getEmail(), this);
            AddictzPreferences.putString(AddictzConstants.USER_NAME, acct.getEmail(), this);
            AddictzPreferences.putString(AddictzConstants.GOOGLE_ID, gId, this);
            AddictzPreferences.putString(AddictzConstants.AUTH_KEY, acct.getIdToken(), this);
            AddictzPreferences.putString(AddictzConstants.PHOTO_URL, acct.getPhotoUrl().toString(), this);


            startActivity(HomeActivity.makeIntent(LoginActivity.this));


            //TODO: Register with our backend
            //instantiate a profile model class and then update the backend database


        } else {
            Snackbar.make(((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0), "Failed to login", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            signIn();
                        }
                    }).show();
        }
    }



    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    protected void setGooglePlusButtonText(SignInButton signInButton,
                                           String buttonText) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setTextSize(15);
                tv.setTypeface(null, Typeface.NORMAL);
                tv.setText(buttonText);
                return;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if(signInFlag) {
            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
            if (opr.isDone()) {
                // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                // and the GoogleSignInResult will be available instantly.
                Log.d(TAG, "Got cached sign-in");
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            } else {
                // If the user has not previously signed in on this device or the sign-in has expired,
                // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                // single sign-on will occur in this branch.
                showProgressDialog();
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(GoogleSignInResult googleSignInResult) {
                        hideProgressDialog();
                        handleSignInResult(googleSignInResult);
                    }
                });
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if(resultCode == RESULT_OK){
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }else{
                if (callbackManager != null) {
                    callbackManager.onActivityResult(requestCode, resultCode, data);
                }
            }
        }else{

        }

    }

}
