package com.slt.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slt.R;
import com.slt.control.DataProvider;
import com.slt.data.Timeline;
import com.slt.model.Response;
import com.slt.model.User;
import com.slt.network.NetworkUtil;
import com.slt.restapi.DataUpdater;
import com.slt.restapi.OtherRestCalls;
import com.slt.restapi.Singleton;
import com.slt.restapi.data.REST_User;
import com.slt.restapi.data.REST_User_Functionalities;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.slt.utils.Validation.validateEmail;
import static com.slt.utils.Validation.validateFields;

public class RegisterFragment extends Fragment {

    public static final String TAG = RegisterFragment.class.getSimpleName();

    private EditText mEtName;
    private EditText mEtEmail;
    private EditText mEtPassword;
    private Button   mBtRegister;
    private TextView mTvLogin;
    private TextInputLayout mTiName;
    private TextInputLayout mTiEmail;
    private TextInputLayout mTiPassword;
    private ProgressBar mProgressbar;

    private CompositeSubscription mSubscriptions;
    private RegisterFragment context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register,container,false);
        mSubscriptions = new CompositeSubscription();
        initViews(view);
        return view;
    }

    private void initViews(View v) {

        mEtName = (EditText) v.findViewById(R.id.et_name);
        mEtEmail = (EditText) v.findViewById(R.id.et_email);
        mEtPassword = (EditText) v.findViewById(R.id.et_password);
        mBtRegister = (Button) v.findViewById(R.id.btn_register);
        mTvLogin = (TextView) v.findViewById(R.id.tv_login);
        mTiName = (TextInputLayout) v.findViewById(R.id.ti_name);
        mTiEmail = (TextInputLayout) v.findViewById(R.id.ti_email);
        mTiPassword = (TextInputLayout) v.findViewById(R.id.ti_password);
        mProgressbar = (ProgressBar) v.findViewById(R.id.progress);

        mBtRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setError();

                String name = mEtName.getText().toString();
                String email = mEtEmail.getText().toString();
                String password = mEtPassword.getText().toString();

                int err = 0;

                if (!validateFields(name)) {

                    err++;
                    mTiName.setError("Name should not be empty !");
                }

                if (!validateEmail(email)) {

                    err++;
                    mTiEmail.setError("Email should be valid !");
                }

                if (!validateFields(password)) {

                    err++;
                    mTiPassword.setError("Password should not be empty !");
                }

                if (err == 0) {

                    User user = new User();
                    user.setName(name);
                    user.setEmail(email);
                    user.setPassword(password);

                    mProgressbar.setVisibility(View.VISIBLE);
                    registerProcess(user);

                } else {

                    showSnackBarMessage("Enter Valid Details !");
                }
            }
        });
        mTvLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                LoginFragment fragment = new LoginFragment();
                ft.replace(R.id.fragmentFrame, fragment, LoginFragment.TAG);
                ft.commit();
            }
        });
    }

    private void register() {

        setError();

        String name = mEtName.getText().toString();
        String email = mEtEmail.getText().toString();
        String password = mEtPassword.getText().toString();

        int err = 0;

        if (!validateFields(name)) {

            err++;
            mTiName.setError("Name should not be empty !");
        }

        if (!validateEmail(email)) {

            err++;
            mTiEmail.setError("Email should be valid !");
        }

        if (!validateFields(password)) {

            err++;
            mTiPassword.setError("Password should not be empty !");
        }

        if (err == 0) {

            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);

            mProgressbar.setVisibility(View.VISIBLE);
            registerProcess(user);

        } else {

            showSnackBarMessage("Enter Valid Details !");
        }
    }

    private void setError() {

        mTiName.setError(null);
        mTiEmail.setError(null);
        mTiPassword.setError(null);
    }

    private void registerProcess(User user) {

        context = this;

        mSubscriptions.add(NetworkUtil.getRetrofit().register(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
         .subscribe(new Action1<Response>() {
            @Override
            public void call(Response response) {
                mProgressbar.setVisibility(View.GONE);

                //REST Call Create new User, timeline and store him in the DB and locally
                OtherRestCalls.createUser_Functionalities(response.getMessage(), context);
                //TODO: needs new timeline
                DataUpdater.getInstance().Start();
                Timeline timeline = new Timeline();
                DataUpdater.getInstance().setTimeline(timeline);

            }
        }, new Action1<Throwable>(){
            @Override
            public void call(Throwable error) {
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
        } ));
    }

    private void handleResponse(Response response) {

        mProgressbar.setVisibility(View.GONE);
        showSnackBarMessage(response.getMessage());
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

    public void showSnackBarMessage(String message) {

        if (getView() != null) {

            Snackbar.make(getView(),message,Snackbar.LENGTH_SHORT).show();
        }
    }

    private void goToLogin(){

        Fragment newFragment = new LoginFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragmentFrame, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }
}
