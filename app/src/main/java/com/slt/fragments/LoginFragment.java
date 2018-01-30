package com.slt.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slt.MainProfile;
import com.slt.R;
import com.slt.control.DataProvider;
import com.slt.data.Timeline;
import com.slt.model.Response;
import com.slt.network.NetworkUtil;
import com.slt.restapi.DataUpdater;
import com.slt.restapi.OtherRestCalls;
import com.slt.restapi.RetrieveOperations;
import com.slt.restapi.UsefulMethods;
import com.slt.statistics.GeneralViewOfStatistics;
import com.slt.utils.Constants;
import com.slt.TimelineActivity;
import com.slt.data.User;
import com.slt.network.RetrofitInterface;
import com.slt.restapi.TemporaryDB;
import com.slt.restapi.data.REST_User_Functionalities;

import android.view.View.OnClickListener;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.slt.utils.Validation.validateEmail;
import static com.slt.utils.Validation.validateFields;

public class LoginFragment extends Fragment {

    /**
     * Tag for the logger
     */
    public static final String TAG = LoginFragment.class.getSimpleName();

    /*
     * Name of the used shared preferences
     */
    private static final String SPF_NAME = "timelinelogin";

    private EditText mEtEmail;
    private EditText mEtPassword;
    private Button mBtViewStatistics;
    private Button mBtLogin;
    private TextView mTvRegister;
    private TextView mTvForgotPassword;
    private TextInputLayout mTiEmail;
    private TextInputLayout mTiPassword;
    private ProgressBar mProgressBar;
    private CheckBox checkBox;

    private CompositeSubscription mSubscriptions;
    private SharedPreferences mSharedPreferences;
    private LoginFragment context;

    public Handler handler;
    String threadEmail;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mSubscriptions = new CompositeSubscription();
        initViews(view);
        initSharedPreferences();
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                afterRetrival();
                return false;
            }
        });
        threadEmail = null;
        return view;
    }

    private void initViews(View v) {
        //mBtViewStatistics = (Button) v.findViewById(R.id.btn_statistics);
        mEtEmail = (EditText) v.findViewById(R.id.et_email);
        mEtPassword = (EditText) v.findViewById(R.id.et_password);
        mBtLogin = (Button) v.findViewById(R.id.btn_login);
        mTiEmail = (TextInputLayout) v.findViewById(R.id.ti_email);
        mTiPassword = (TextInputLayout) v.findViewById(R.id.ti_password);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progress);
        mTvRegister = (TextView) v.findViewById(R.id.tv_register);
        mTvForgotPassword = (TextView) v.findViewById(R.id.tv_forgot_password);
        checkBox = (CheckBox) v.findViewById(R.id.saveLoginCheckBox);


        try {
            SharedPreferences sharedPref = getActivity().getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
            if (sharedPref.contains(Constants.STORE_BOX)) {
                boolean checked = sharedPref.getBoolean(Constants.STORE_BOX, false);

                if (checked) {
                    checkBox.setChecked(true);
                    String login = sharedPref.getString(Constants.LOGIN, "");
                    String pwd = sharedPref.getString(Constants.PASSWORD, "");
                    this.mEtEmail.setText(login);
                    this.mEtPassword.setText(pwd);

                }
            }
        } catch (Exception e) {
            //do nothing on exception except log it
            Log.i(TAG, "Failure when retrieving shared preferences");
        }



        mBtLogin.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                threadEmail = null;
                setError();
                String email = mEtEmail.getText().toString();
                String password = mEtPassword.getText().toString();


                int err = 0;

                if (!validateEmail(email)) {

                    err++;
                    mTiEmail.setError("Email should be valid !");
                }

                if (!validateFields(password)) {

                    err++;
                    mTiPassword.setError("Password should not be empty !");
                }

                if (err == 0) {
                    threadEmail = email;
                    loginProcess(email, password);
                    mProgressBar.setVisibility(View.VISIBLE);

                } else {

                    showSnackBarMessage("Enter Valid Details !");
                }

            }
        });


        mTvRegister.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Fragment newFragment = new RegisterFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentFrame, newFragment);

                //This way we can press the back button and come one page back
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        mTvForgotPassword.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Fragment newFragment = new ResetPasswordFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentFrame, newFragment);

                //This way we can press the back button and come one page back:
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void initSharedPreferences() {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    private void viewStatistics() {
        Intent intent = new Intent( getActivity()  , GeneralViewOfStatistics.class);

        startActivity(intent);

    }

    private void setError() {

        mTiEmail.setError(null);
        mTiPassword.setError(null);
    }

    private void loginProcess(String email, String password) {
        context = this;

        RetrofitInterface interfaceObj = NetworkUtil.getRetrofit(email, password);
        mSubscriptions.add(NetworkUtil.getRetrofit(email, password).login()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Response>() {
                    @Override
                    public void call(Response response) {
                        mProgressBar.setVisibility(View.GONE);

                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putString(Constants.TOKEN, response.getToken());
                        editor.putString(Constants.EMAIL, response.getMessage());
                        editor.apply();


                        String email = mEtEmail.getText().toString();
                        String password = mEtPassword.getText().toString();

                        //Store login if checkbox was selected, if not remove setting from shared prferences
                        if (checkBox.isChecked()) {
                            SharedPreferences sharedPref = getActivity().getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor neditor = sharedPref.edit();
                            neditor.putString(Constants.LOGIN, email);
                            neditor.putString(Constants.PASSWORD, password);
                            neditor.putBoolean(Constants.STORE_BOX, true);
                            neditor.commit();
                        } else {
                            SharedPreferences sharedPref = getActivity().getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor neditor = sharedPref.edit();
                            neditor.putString(Constants.LOGIN, null);
                            neditor.putString(Constants.PASSWORD, null);
                            neditor.putBoolean(Constants.STORE_BOX, false);

                            mEtEmail.setText(null);
                            mEtPassword.setText(null);
                        }

                        //Call Handler to retrieve REST User
                        mProgressBar.setVisibility(View.VISIBLE);
                        handler.post(runnable);

                        }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable error) {

                        mProgressBar.setVisibility(View.GONE);

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
                }));
    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (threadEmail != null) {
                        User user = OtherRestCalls.retrieveUser_Functionalities(threadEmail);

                        if (user != null) {
                            DataProvider.getInstance().setOwnUser(user);

                            Bitmap bitmap = UsefulMethods.LoadImage(user);

                            DataProvider.getInstance().getOwnUser().setMyImage(bitmap);

                            Timeline timeline = RetrieveOperations.getInstance().getCompleteTimeline();
                            DataProvider.getInstance().getOwnUser().setTimeline(timeline);
                            DataProvider.getInstance().syncTimelineToUser();

                            DataUpdater.getInstance().Start();

                        } else {
                            showSnackBarMessage("Error retrieving User!");
                        }
                        handler.sendEmptyMessage(0);
                    }
                }
            }).start();
        }
    };

    private void afterRetrival() {
        mProgressBar.setVisibility(View.GONE);

        Intent intent = new Intent(getActivity(), MainProfile.class);
        startActivity(intent);
    }


    public void openTimelineActivity() {
        Intent intent = new Intent(getActivity(), TimelineActivity.class);
        REST_User_Functionalities r_u_f = TemporaryDB.getInstance().getAppUser();
        intent.putExtra(com.slt.restapi.data.Constants.USERID, r_u_f._id);
        startActivity(intent);
    }

    private void showSnackBarMessage(String message) {

        if (getView() != null) {

            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }
}
