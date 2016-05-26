package com.danceaddictz.danceaddictz.view;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.danceaddictz.danceaddictz.R;
import com.danceaddictz.danceaddictz.common.AddictzConstants;
import com.danceaddictz.danceaddictz.common.AddictzPreferences;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;
    private GoogleApiClient mGoogleApiClient;

    public static Intent makeIntent(Context context){

        Intent intent = new Intent(context,HomeActivity.class);
        // Closing all the Activities
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
         toolbar = (Toolbar) findViewById(R.id.toolbar);

        // coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle(getResources().getString(R.string.app_name));

        String name = AddictzPreferences.getString(AddictzConstants.NAME, this);
        if(name==null || name.length()==0)
            name = "DanceAddict";

        toolbar.setSubtitle("Welcome, " + name);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(HomeActivity.this)
                .enableAutoManage(HomeActivity.this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
                        // be available.
                        Log.d(TAG, "onConnectionFailed:" + connectionResult);
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_camera) {
                    // Handle the camera action
                } else if (id == R.id.nav_gallery) {

                } else if (id == R.id.nav_slideshow) {

                } else if (id == R.id.nav_manage) {

                } else if (id == R.id.nav_share) {

                } else if (id == R.id.nav_send) {

                } else if(id == R.id.nav_logout){
                    logout();
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        View header = navigationView.getHeaderView(0);

        TextView nameNTV = (TextView) header.findViewById(R.id.nav_header_name);
        nameNTV.setText(name);

        String email = AddictzPreferences.getString(AddictzConstants.EMAIL, this);
        if(email!=null || email.length()>0){
            TextView emailNTV = (TextView) header.findViewById(R.id.nav_header_email);
            emailNTV.setText(email);
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /*if (id == R.id.action_logout) {
            logout();
            return true;
        }else */if(id == R.id.action_search){
            Snackbar.make(coordinatorLayout, "Search", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(HomeActivity.this, "Shwetha is searching...", Toast.LENGTH_LONG).show();
                        }
                    }).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        if (AddictzPreferences.getInt(AddictzConstants.LOGIN_TYPE, HomeActivity.this)
                == AddictzConstants.LOGIN_TYPES.GOOGLE.getId()){
            //google sign out
            signOutGoogle();
        }else if (AddictzPreferences.getInt(AddictzConstants.LOGIN_TYPE, HomeActivity.this)
                == AddictzConstants.LOGIN_TYPES.FB.getId()){
            //fb signout
            signoutFB();
        }else{
            //addictz signout
            //TODO: Signout from backend

            clearAccAndGoToLogin();
        }


    }

    private void signoutFB() {
        LoginManager.getInstance().logOut();

        clearAccAndGoToLogin();
    }

    private void signOutGoogle() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                       clearAccAndGoToLogin();
                    }
                });
    }

    private void clearAccAndGoToLogin(){

        AddictzPreferences.removeData(this);
        startActivity(LoginActivity.makeIntent(HomeActivity.this));
        finish();
    }

}
