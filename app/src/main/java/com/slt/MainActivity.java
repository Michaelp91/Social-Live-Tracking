package com.slt;

import com.slt.control.SharedResources;
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
     * Activity
     */
    private Activity myActivity;

    /**
     * The login fragment to load
     */
    private LoginFragment mLoginFragment;

    /**
     * The reset Password Dialog that can be shown
     */
    private ResetPasswordDialog mResetPasswordDialog;

    /**
     * Overwritten onCreate Method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myActivity = this;

        if (savedInstanceState == null) {
            loadFragment();
        }
    }

    /**
     * Loads the login fragment
     */
    private void loadFragment(){

        if (mLoginFragment == null) {
            mLoginFragment = new LoginFragment();
        }
        getFragmentManager().beginTransaction().replace(R.id.fragmentFrame,mLoginFragment,LoginFragment.TAG).commit();
    }

    /**
     * Overwritten onNewIntent Method to shown show the reset password dialog
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String data = intent.getData().getLastPathSegment();
        Log.d(TAG, "onNewIntent: "+data);

        mResetPasswordDialog = (ResetPasswordDialog) getFragmentManager().findFragmentByTag(ResetPasswordDialog.TAG);

        if (mResetPasswordDialog != null)
            mResetPasswordDialog.setToken(data);
    }

    /**
     * Overwritten onPasswordReset Method, just shows a message
     * @param message
     */
    @Override
    public void onPasswordReset(String message) {
        showSnackBarMessage(message);
    }

    /**
     * Shows a message to the user
     * @param message Message to show
     */
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
