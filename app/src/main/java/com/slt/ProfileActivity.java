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

    public static final String TAG = ProfileActivity.class.getSimpleName();

    private TextView mTvName;
    private TextView mTvEmail;
    private TextView mTvDate;
    private Button mBtChangePassword;
    private Button mBtLogout;



    private ProgressBar mProgressbar;

    private SharedPreferences mSharedPreferences;
    private String mToken;
    private String mEmail;

    private CompositeSubscription mSubscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mSubscriptions = new CompositeSubscription();



        initViews();
        initSharedPreferences();
        loadProfile();

    }

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


    private void initSharedPreferences() {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = mSharedPreferences.getString(Constants.TOKEN,"");
        mEmail = mSharedPreferences.getString(Constants.EMAIL,"");
    }

    private void logout() {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.EMAIL,"");
        editor.putString(Constants.TOKEN,"");
        editor.apply();
        finish();
    }

    private void showDialog(){

        ChangePasswordDialog fragment = new ChangePasswordDialog();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.EMAIL, mEmail);
        bundle.putString(Constants.TOKEN, mToken);
        fragment.setArguments(bundle);

        fragment.show(getFragmentManager(), ChangePasswordDialog.TAG);
    }



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

    private void handleResponse(User user) {
        mProgressbar.setVisibility(View.GONE);
        mTvName.setText(user.getName());
        mTvEmail.setText(user.getEmail());
        mTvDate.setText(user.getCreated_at());

    }

    private void handleError(Throwable error) {

        mProgressbar.setVisibility(View.GONE);

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

    private void showSnackBarMessage(String message) {

        Snackbar.make(findViewById(R.id.activity_profile),message,Snackbar.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }

    @Override
    public void onPasswordChanged() {

        showSnackBarMessage("Password Changed Successfully !");
    }





}

