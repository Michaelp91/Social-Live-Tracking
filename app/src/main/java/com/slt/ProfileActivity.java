package com.slt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slt.fragments.ChangePasswordDialog;
import com.slt.model.Response;
import com.slt.model.User;
import com.slt.network.NetworkUtil;
import com.slt.utils.Constants;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import android.view.View.OnClickListener;
public class ProfileActivity extends AppCompatActivity implements ChangePasswordDialog.Listener {

    /**
     * Tag for the logger
     */
    public static final String TAG = ProfileActivity.class.getSimpleName();

    /**
     * Element for the name
     */
    private TextView mTvName;

    /**
     * Element for the email
     */
    private TextView mTvEmail;

    /**
     * Element for the date
     */
    private TextView mTvDate;

    /**
     * The change password button
     */
    private Button mBtChangePassword;

    /**
     * The logout Button
     */
    private Button mBtLogout;

    /**
     * Progress Bar
     */
    private ProgressBar mProgressbar;

    /**
     * The shared preferences
     */
    private SharedPreferences mSharedPreferences;

    /**
     * The token of the user
     */
    private String mToken;

    /**
     * The email address of the user
     */
    private String mEmail;

    /**
     * The subscriptions
     */
    private CompositeSubscription mSubscriptions;

    /**
     * Overwritten onCreate Method intits the views
     * @param savedInstanceState The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mSubscriptions = new CompositeSubscription();

        initViews();
        initSharedPreferences();
        loadProfile();

    }

    /**
     * Init the text fields with the data and adds a button listener
     */
    private void initViews() {

        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvEmail = (TextView) findViewById(R.id.tv_email);
        mTvDate = (TextView) findViewById(R.id.tv_date);
        mBtChangePassword = (Button) findViewById(R.id.btn_change_password);
        mBtLogout = (Button) findViewById(R.id.btn_logout);
        mProgressbar = (ProgressBar) findViewById(R.id.progress);

        mBtChangePassword.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ChangePasswordDialog fragment = new ChangePasswordDialog();

                Bundle bundle = new Bundle();
                bundle.putString(Constants.EMAIL, mEmail);
                bundle.putString(Constants.TOKEN, mToken);
                fragment.setArguments(bundle);

                fragment.show(getFragmentManager(), ChangePasswordDialog.TAG);
            }
        });

        mBtLogout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(Constants.EMAIL,"");
                editor.putString(Constants.TOKEN,"");
                editor.apply();
                finish();
            }
        });
    }

    /**
     * Initialize the shared preferences
     */
    private void initSharedPreferences() {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = mSharedPreferences.getString(Constants.TOKEN,"");
        mEmail = mSharedPreferences.getString(Constants.EMAIL,"");
    }

    /**
     * Logout for the user
     */
    private void logout() {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.EMAIL,"");
        editor.putString(Constants.TOKEN,"");
        editor.apply();
        finish();
    }

    /**
     * Shows the change password dialog to the user
     */
    private void showDialog(){

        ChangePasswordDialog fragment = new ChangePasswordDialog();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.EMAIL, mEmail);
        bundle.putString(Constants.TOKEN, mToken);
        fragment.setArguments(bundle);

        fragment.show(getFragmentManager(), ChangePasswordDialog.TAG);
    }

    /**
     * Load the profile from the server
     */
    private void loadProfile() {

        mSubscriptions.add(NetworkUtil.getRetrofit(mToken).getProfile(mEmail)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        mProgressbar.setVisibility(View.GONE);
                        mTvName.setText(user.getName());
                        mTvEmail.setText(user.getEmail());
                        mTvDate.setText(user.getCreated_at());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable error) {

                        mProgressbar.setVisibility(View.GONE);

                        if (error instanceof HttpException) {

                            Gson gson = new GsonBuilder().create();

                            try {

                                String errorBody = ((HttpException) error).response().errorBody().string();
                                Response response = gson.fromJson(errorBody, Response.class);
                                showSnackBarMessage(response.getMessage());

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {

                            showSnackBarMessage("Network Error !");
                        }
                    }
                } ));
    }

    /**
     * Handles the response, show the results
     * @param user
     */
    private void handleResponse(User user) {
        mProgressbar.setVisibility(View.GONE);
        mTvName.setText(user.getName());
        mTvEmail.setText(user.getEmail());
        mTvDate.setText(user.getCreated_at());

    }

    /**
     *  Method to handle errors
     * @param error The error to handle
     */
    private void handleError(Throwable error) {

        mProgressbar.setVisibility(View.GONE);

        //if error is http exception, show message to the user
        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {
                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                showSnackBarMessage(response.getMessage());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showSnackBarMessage("Network Error !");
        }
    }

    /**
     * Shows message to user
     * @param message The message to show
     */
    private void showSnackBarMessage(String message) {
        Snackbar.make(findViewById(R.id.activity_profile),message,Snackbar.LENGTH_SHORT).show();

    }

    /**
     * Overwritten Method onDestroy, unsubscibe
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }

    /**
     * Overwritten Method onPasswordChanged show message to user
     */
    @Override
    public void onPasswordChanged() {

        showSnackBarMessage("Password Changed Successfully !");
    }
}

