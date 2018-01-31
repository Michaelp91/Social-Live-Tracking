package com.slt;

import android.*;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slt.control.ApplicationController;
import com.slt.control.DataProvider;
import com.slt.control.SharedResources;
import com.slt.fragments.ChangePasswordDialog;
import com.slt.fragments.FragmentAchievements;
import com.slt.fragments.FragmentEditSettings;
import com.slt.fragments.FragmentFriends;
import com.slt.fragments.FragmentLiveMap;
import com.slt.fragments.FragmentSummaries;
import com.slt.fragments.FragmentTimeline;
import com.slt.fragments.LoginFragment;
import com.slt.fragments.RegisterFragment;
import com.slt.fragments.ResetPasswordDialog;
import com.slt.fragments.ResetPasswordFragment;
import com.slt.model.Response;
import com.slt.model.User;
import com.slt.network.NetworkUtil;
import com.slt.services.ActivityService;
import com.slt.services.LocationService;
import com.slt.statistics.GeneralViewOfStatistics;
import com.slt.statistics.ViewStatistics;
import com.slt.utils.Constants;

import java.io.IOException;
import java.sql.Time;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Main Profile Class, contains the main activity for our application
 */
public class MainProfile extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<Status>, NavigationView.OnNavigationItemSelectedListener {

    /**
     * Definitions for the permissions that have to be requested by the user
     */
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 2;

    /**
     * Update interval for the activity detection
     */
    private static final int ACTIVITY_UPDATE_INTERVAL_MILLISECONDS = 100;

    /**
     * TAG for the Logger
     */
    public static final String TAG = MainProfile.class.getSimpleName();

    //creating fragment object
    Fragment fragment;

    /**
     * Stores the application context
     */
    Context context;

    private ImageView mProfilePhoto;
    private TextView mUsername;

    private SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragment = null;
        context = this;
        setContentView(R.layout.activity_main_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        super.setTitle("Social Live Tracking");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);
        mProfilePhoto = (ImageView) view.findViewById(R.id.profile_image);

        if (DataProvider.getInstance().getOwnUser().getMyImage() == null) {
            Bitmap image = BitmapFactory.decodeResource(ApplicationController.getContext().getResources(), R.drawable.profile_pic);
            this.mProfilePhoto.setImageBitmap(image);
        } else {
            this.mProfilePhoto.setImageBitmap(DataProvider.getInstance().getOwnUser().getMyImage());
        }


        SharedResources.getInstance().setNavProfilePhoto(mProfilePhoto);

        mUsername = (TextView) view.findViewById(R.id.tv_username);
        SharedResources.getInstance().setNavUsername(mUsername);

        mUsername.setText(DataProvider.getInstance().getOwnUser().getUserName());
        this.setProfileImage(DataProvider.getInstance().getOwnUser().getMyImage());


        if (SharedResources.getInstance().getMyGoogleApiClient() == null) {
            //Create GoogleAPI from main activity to be able to better react to faults
            SharedResources.getInstance().setMyGoogleApiClient(new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(ActivityRecognition.API)
                    .build());
        }
        SharedResources.getInstance().getMyGoogleApiClient().connect();

        //check if we have the camera permission
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 0);
        }


        //check the needed permission for location and request if needed
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }

        //check the needed permission for gps and request if needed
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }


        checkSettings();

        // show a message to inform the user if step detection is not supported
        if(!isVersionWithStepSensor()){
            android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(context);
            dialog.setMessage("Step Detection not supported!");
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                }
            });
            dialog.show();
        }
    }

    /**
     * Check and ask the user to set network and location services to on
     */
    private void checkSettings() {
        LocationManager locationManager = null;
        boolean gps_enabled = false, network_enabled = false;


        if (locationManager == null)
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!gps_enabled) {

                android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(context);
                dialog.setMessage("Location not enabled!");
                dialog.setPositiveButton("Location settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(myIntent);
                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
                dialog.show();
            }
        } catch (Exception ex) {
            Log.i(TAG, "Exception when trying to check GPS Settings");
        }

        try {
            ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            network_enabled = conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                    || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED;

            if (!network_enabled) {

                android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(context);
                dialog.setMessage("Network not enabled!");
                dialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
                        context.startActivity(myIntent);
                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
                dialog.show();

            }
        } catch (Exception ex) {
            Log.i(TAG, "Exception when trying to check Network Settings");
        }
    }


    /**
     * Set the picture for the nav drawer
     *
     * @param img The image to set as a bitmap
     */
    private void setProfileImage(Bitmap img) {
        Log.d(TAG, "setProfileImage: setting profile image.");
        //check if we have a picture to show, if not default is shown
        if (img == null) {
            Bitmap image = BitmapFactory.decodeResource(ApplicationController.getContext().getResources(), R.drawable.profile_pic);
            this.mProfilePhoto.setImageBitmap(image);
        } else {
            this.mProfilePhoto.setImageBitmap(img);
        }
    }


    /**
     * Procedure to logout the user
     */
    private void logout() {
        MainProfile.this.finish();
    }


    private void showSnackBarMessage(String message) {

        Snackbar.make(findViewById(R.id.nav_header_main), message, Snackbar.LENGTH_SHORT).show();

    }

    /**
     * On Activity Destroy do the cleanup of the services, notification and Data Provider
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            DataProvider.getInstance().clearData();

            //Disconnect Activity Listener if App has been stopped
            if(SharedResources.getInstance().getMyGoogleApiClient() != null) {
                if (SharedResources.getInstance().getMyGoogleApiClient().isConnected()) {
                    ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(
                            SharedResources.getInstance().getMyGoogleApiClient(),
                            getActivityDetectionPendingIntent()
                    ).setResultCallback(this);
                }
            }

            LocationService.shouldContinue = false;

            SharedResources.getInstance().removeNotification();
        }
    }


    /**
     * Check if the os version supports step sensing
     *
     * @return True if the device has support for Step Sensing
     */
    private boolean isVersionWithStepSensor() {
        // BEGIN_INCLUDE(iskitkatsensor)
        // Require at least Android KitKat
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        // Check that the device supports the step counter and detector sensors
        PackageManager packageManager = this.getPackageManager();
        return currentApiVersion >= android.os.Build.VERSION_CODES.KITKAT
                && packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)
                && packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR);
    }


    /**
     * Check the result of the request of permissions
     *
     * @param requestCode  The code of the requested permissions
     * @param permissions  The permissions we asked for
     * @param grantResults The results of the request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        //Check which permissions were requestd
        switch (requestCode) {

            //for Location access
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Denied Permission");
                    builder.setMessage("Since location access has not been granted the App will not work.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            Log.i(TAG, "Coarse loaction denied");
                        }

                    });
                    builder.show();
                }
                return;
            }

            //for GPS access
            case PERMISSION_REQUEST_FINE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Denied Permission");
                    builder.setMessage("Since location access has not been granted the App will not work.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            Log.i(TAG, "Fine location permission denied");
                        }

                    });
                    builder.show();
                }
            }
        }
    }


    /**
     * If service has been connected
     *
     * @param bundle - Bundle containing information if needed
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Connected");

        //Check if API Client is connected, should be the case in this callback
        if (!SharedResources.getInstance().getMyGoogleApiClient().isConnected()) {
            Toast.makeText(this, "GoogleApiClient not yet connected", Toast.LENGTH_SHORT).show();
        } else {
            //Add request for activity updates to the client
            ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(SharedResources.getInstance()
                    .getMyGoogleApiClient(), ACTIVITY_UPDATE_INTERVAL_MILLISECONDS, getActivityDetectionPendingIntent())
                    .setResultCallback(this);
        }

    }

    /**
     * Overwritten default connectionSuspended method
     *
     * @param i - Suspend type
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
    }

    /**
     * Overwritten default connectionResult method
     *
     * @param connectionResult Result of the connection try
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    /**
     * Overwritten default onResume method
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Overwritten default onPause method
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Overwritten onStart method, starts Activity Listener and Location Service
     */
    @Override
    protected void onStart() {
        super.onStart();

        //connect API client
        SharedResources.getInstance().getMyGoogleApiClient().connect();
        LocationService.shouldContinue = true;

        //Start Location Service
        Intent serviceIntent = new Intent(this, LocationService.class);
        startService(serviceIntent);

    }

    /**
     * Overwritten onStop Method, disconnects Activity API Client
     */
    @Override
    protected void onStop() {
        super.onStop();


    }

    /**
     * Method gets all pending intents for the activity recognition service
     *
     * @return The pending intents for the Activity Service
     */
    private PendingIntent getActivityDetectionPendingIntent() {
        Intent intent = new Intent(this, ActivityService.class);

        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Checks the result of adding activity recognition
     *
     * @param status The status of the activity recognition
     */
    public void onResult(@NonNull Status status) {
        if (status.isSuccess()) {
            Log.e(TAG, "Successfully added activity detection.");

        } else {
            Log.e(TAG, "Error: " + status.getStatusMessage());
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
        getMenuInflater().inflate(R.menu.main_profile, menu);
        return true;
    }



    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_timeline:
                //TODO: Not sure which Activity, please ask Usman/Thorsten; TimelineActivit1y or TimelineDetailsActivity
                fragment = new FragmentTimeline();
                break;
            case R.id.nav_summaries:
                //fragment = new FragmentSummaries();
                Intent Summaries = new Intent(MainProfile.this, GeneralViewOfStatistics.class);
                startActivity(Summaries);
                break;
            case R.id.nav_friends:
                fragment = new FragmentFriends();
                break;
            case R.id.nav_livemap:
                fragment = new FragmentLiveMap();
                break;
            case R.id.nav_achievements:
                fragment = new FragmentAchievements();
                break;
            case R.id.nav_edit:
                fragment = new FragmentEditSettings();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();

            ft.replace(R.id.content_main_frame, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_btn_logout) {
            DataProvider.getInstance().clearData();

            //Disconnect Activity Listener if App has been stopped
            if (SharedResources.getInstance().getMyGoogleApiClient().isConnected()) {
                ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(
                        SharedResources.getInstance().getMyGoogleApiClient(),
                        getActivityDetectionPendingIntent()
                ).setResultCallback(this);
            }

            LocationService.shouldContinue = false;
            SharedResources.getInstance().setMyGoogleApiClient(null);

            SharedResources.getInstance().removeNotification();
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        return true;

    }

}
