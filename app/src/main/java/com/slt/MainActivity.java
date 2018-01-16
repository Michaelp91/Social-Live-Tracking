package com.slt;

import com.slt.fragments.ResetPasswordFragment;
import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.slt.fragments.LoginFragment;
import com.slt.fragments.ResetPasswordDialog;


/**
 * The Main Activity used for the login procedure
 */
public class MainActivity extends AppCompatActivity implements ResetPasswordFragment.Listener  {
    /**
     * Tag for logger
     */
    public static final String TAG = MainActivity.class.getSimpleName();

    /**
     * Definitions for the permissions that have to be requested by the user
     */
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 2;

    /**
     * Update interval for the activity detection
     */
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
    }


    private void loadFragment(){

        if (mLoginFragment == null) {

            mLoginFragment = new LoginFragment();
        }
        getFragmentManager().beginTransaction().replace(R.id.fragmentFrame,mLoginFragment,LoginFragment.TAG).commit();
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
