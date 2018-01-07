package com.slt;


import com.slt.fragments.FragmentFriends;
import com.slt.fragments.FragmentSearchFriends;
import com.slt.services.LocationService;
import com.slt.services.ActivityService;
import com.slt.control.SharedResources;


import android.Manifest;
import android.content.pm.PackageManager;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.pm.ActivityInfoCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.slt.fragments.LoginFragment;
import com.slt.fragments.ResetPasswordDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.os.Build;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;

/**
 * The Main Activity
 */
public class MainActivity extends AppCompatActivity implements ResetPasswordDialog.Listener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {
    /**
     * Tag for logger
     */
    public static final String TAG = MainActivity.class.getSimpleName();
    /**
     * Definitions for the permissions that have to be requested by the user
     */
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 2;

    private static final int ACTIVITY_UPDATE_INTERVAL_MILLISECONDS = 1000;
    private Activity myActivity;
    private LoginFragment mLoginFragment;
    private ResetPasswordDialog mResetPasswordDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myActivity = this;

        if (savedInstanceState == null) {

            loadFragment();
        }

        //Create GoogleAPI from main activity to be able to better react to faults
        SharedResources.getInstance().setMyGoogleApiClient(new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(ActivityRecognition.API)
                .build());

        SharedResources.getInstance().getMyGoogleApiClient().connect();

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){

        }else{
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.CAMERA}, 0);
        }

        // Check if android 23 or greater for location permission requet
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            //check if user already given permission for location coarse
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                //if not request permission via a dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("The App needs a location to work");
                builder.setMessage("Please grant location access so this App can detect the position.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    public void onDismiss(DialogInterface dialog) {
                        ActivityCompat.requestPermissions(myActivity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }

            //check if user already given permission for location fine
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                //if not request permission via a dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This App needs access to the GPS Position to work properly");
                builder.setMessage("Please grant GPS access to the App.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        ActivityCompat.requestPermissions(myActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                PERMISSION_REQUEST_FINE_LOCATION);
                    }
                });
                builder.show();
            }
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


    private void loadFragment(){

        if (mLoginFragment == null) {

            mLoginFragment = new LoginFragment();
        }
        getFragmentManager().beginTransaction().replace(R.id.fragmentFrame,mLoginFragment,LoginFragment.TAG).commit();
        //TODO Remove later
      //  getFragmentManager().beginTransaction().replace(R.id.fragmentFrame,new FragmentSearchFriends(),LoginFragment.TAG).commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String data = intent.getData().getLastPathSegment();
        Log.d(TAG, "onNewIntent: "+data);

        mResetPasswordDialog = (ResetPasswordDialog) getFragmentManager().findFragmentByTag(ResetPasswordDialog.TAG);

        if (mResetPasswordDialog != null)
            mResetPasswordDialog.setToken(data);
    }

    @Override
    public void onPasswordReset(String message) {

        showSnackBarMessage(message);
    }

    private void showSnackBarMessage(String message) {

        Snackbar.make(findViewById(R.id.activity_main),message,Snackbar.LENGTH_SHORT).show();

    }

    /**
     *  Check the result of the request of permissions
     * @param requestCode The code of the requested permissions
     * @param permissions The permissions we asked for
     * @param grantResults The results of the request
     */
    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String permissions[], @NonNull int[] grantResults){

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
            ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(SharedResources.getInstance().getMyGoogleApiClient(), ACTIVITY_UPDATE_INTERVAL_MILLISECONDS, getActivityDetectionPendingIntent()).setResultCallback(this);
        }

    }

    /**
     * Overwritten default connectionSuspended method
     * @param i - Suspend type
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        SharedResources.getInstance().getMyGoogleApiClient().connect();
    }

    /**
     * Overwritten default connectionResult method
     * @param connectionResult Result of the connection try
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    /**
     * Overwritten onStart method, starts Activity Listener and Location Service
     */
    @Override
    protected void onStart() {
        super.onStart();

        //connect API client
        SharedResources.getInstance().getMyGoogleApiClient().connect();

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

        //Disconnect Activity Listener if App has been stopped
        if (SharedResources.getInstance().getMyGoogleApiClient().isConnected()) {
            SharedResources.getInstance().getMyGoogleApiClient().disconnect();
        }
    }

    /**
     * Method gets all pending intents for the activity recognition service
     * @return The pending intents for the Activity Service
     */
    private PendingIntent getActivityDetectionPendingIntent() {
        Intent intent = new Intent(this, ActivityService.class);

        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Checks the result of adding activity recognition
     * @param status The status of the activity recognition
     */
    public void onResult(@NonNull Status status) {
        if (status.isSuccess()) {
            Log.e(TAG, "Successfully added activity detection.");

        } else {
            Log.e(TAG, "Error: " + status.getStatusMessage());
        }
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
}
