package com.slt.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.slt.MainActivity;
import com.slt.R;
import com.slt.model.Response;
import com.slt.model.User;
import com.slt.network.NetworkUtil;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.slt.utils.Validation.validateEmail;
import static com.slt.utils.Validation.validateFields;

/**
 *
 */
public class ResetPasswordFragment extends Fragment {

    /**
     *
     */
    public interface Listener {
        /**
         *
         * @param message
         */
        void onPasswordReset(String message);
    }

    /**
     *
     */
    public static final String TAG = ResetPasswordFragment.class.getSimpleName();

    /**
     *
     */
    private EditText mEtEmail;

    /**
     *
     */
    private EditText mEtToken;

    /**
     *
     */
    private EditText mEtPassword;

    /**
     *
     */
    private Button mBtResetPassword;

    /**
     *
     */
    private TextView mTvMessage;

    /**
     *
     */
    private TextInputLayout mTiEmail;

    /**
     *
     */
    private TextInputLayout mTiToken;

    /**
     *
     */
    private TextInputLayout mTiPassword;

    /**
     *
     */
    private ProgressBar mProgressBar;

    /**
     *
     */
    private CompositeSubscription mSubscriptions;

    /**
     *
     */
    private String mEmail;

    /**
     *
     */
    private boolean isInit = true;

    /**
     *
     */
    private Listener mListner;

    /**
     * Overwritten onCreateViewMethod, intializes the elements
     * @param inflater Inflater for the layout
     * @param container The View Group
     * @param savedInstanceState The saved instance state
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password,container,false);
        mSubscriptions = new CompositeSubscription();
        initViews(view);
        return view;
    }

    /**
     * Initializes the elements
     * @param v The view
     */
    private void initViews(View v) {

        mEtEmail = (EditText) v.findViewById(R.id.et_email);
        mEtToken = (EditText) v.findViewById(R.id.et_token);
        mEtPassword = (EditText) v.findViewById(R.id.et_password);
        mBtResetPassword = (Button) v.findViewById(R.id.btn_reset_password);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progress);
        mTvMessage = (TextView) v.findViewById(R.id.tv_message);
        mTiEmail = (TextInputLayout) v.findViewById(R.id.ti_email);
        mTiToken = (TextInputLayout) v.findViewById(R.id.ti_token);
        mTiPassword = (TextInputLayout) v.findViewById(R.id.ti_password);

        mBtResetPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isInit) resetPasswordInit();
                else resetPasswordFinish();
            }
        });
    }

    /**
     * Overwritten onAttack Method
     * @param context The context of the view
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListner = (MainActivity)context;
    }

    /**
     * Resets all the fields
     */
    private void setEmptyFields() {
        mTiEmail.setError(null);
        mTiToken.setError(null);
        mTiPassword.setError(null);
        mTvMessage.setText(null);
    }

    /**
     * Setes the token
     * @param token The token
     */
    public void setToken(String token) {
        mEtToken.setText(token);
    }

    /**
     * Reset the password
     */
    private void resetPasswordInit() {

        setEmptyFields();
        mEmail = mEtEmail.getText().toString();
        int err = 0;

        //check if email is valid
        if (!validateEmail(mEmail)) {

            err++;
            mTiEmail.setError("Email Should be Valid !");
        }

        if (err == 0) {

            mProgressBar.setVisibility(View.VISIBLE);
            resetPasswordInitProgress(mEmail);
        }
    }

    /**
     * Finish resetting the passwords
     */
    private void resetPasswordFinish() {

        setEmptyFields();
        String token = mEtToken.getText().toString();
        String password = mEtPassword.getText().toString();

        int err = 0;

        //check if token is empty
        if (!validateFields(token)) {

            err++;
            mTiToken.setError("Token Should not be empty !");
        }

        //check if email is empty
        if (!validateFields(password)) {

            err++;
            mTiEmail.setError("Password Should not be empty !");
        }

        //start the reset process
        if (err == 0) {
            mProgressBar.setVisibility(View.VISIBLE);
            User user = new User();
            user.setPassword(password);
            user.setToken(token);
            resetPasswordFinishProgress(user);
        }
    }

    /**
     * Call the network for the password reset
     * @param email The email of the user
     */
    private void resetPasswordInitProgress(String email) {

        mSubscriptions.add(NetworkUtil.getRetrofit().resetPasswordInit(email)
                //on successful reset
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Response>() {
                    @Override
                    public void call(Response response) {

                        mProgressBar.setVisibility(View.GONE);

                        if (isInit) {

                            isInit = false;
                            showMessage(response.getMessage());
                            mTiEmail.setVisibility(View.GONE);
                            mTiToken.setVisibility(View.VISIBLE);
                            mTiPassword.setVisibility(View.VISIBLE);

                        } else {

                            mListner.onPasswordReset(response.getMessage());
                            //dismiss();
                        }
                    }
                }, new Action1<Throwable>(){
                    @Override
                    public void call(Throwable error) {

                        //on error
                        mProgressBar.setVisibility(View.GONE);

                        if (error instanceof HttpException) {

                            Gson gson = new GsonBuilder().create();

                            try {

                                String errorBody = ((HttpException) error).response().errorBody().string();
                                Response response = gson.fromJson(errorBody,Response.class);
                                showMessage(response.getMessage());

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {

                            showMessage("Network Error !");
                        }
                    }
                } ));
    }

    /**
     * Finish the rest password process
     * @param user The user to reset the password for
     */
    private void resetPasswordFinishProgress(User user) {

        mSubscriptions.add(NetworkUtil.getRetrofit().resetPasswordFinish(mEmail,user)
                //on successful change
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Response>() {
                    @Override
                    public void call(Response response) {

                        mProgressBar.setVisibility(View.GONE);

                        if (isInit) {

                            isInit = false;
                            showMessage(response.getMessage());
                            mTiEmail.setVisibility(View.GONE);
                            mTiToken.setVisibility(View.VISIBLE);
                            mTiPassword.setVisibility(View.VISIBLE);

                        } else {

                            mListner.onPasswordReset(response.getMessage());

                        }
                    }
                }, new Action1<Throwable>(){
                    @Override
                    public void call(Throwable error) {
                        // on error
                        mProgressBar.setVisibility(View.GONE);

                        if (error instanceof HttpException) {

                            Gson gson = new GsonBuilder().create();

                            try {

                                String errorBody = ((HttpException) error).response().errorBody().string();
                                Response response = gson.fromJson(errorBody,Response.class);
                                showMessage(response.getMessage());

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {

                            showMessage("Network Error !");
                        }
                    }
                } ));
    }

    /**
     * Handle the network response
     * @param response The response
     */
    private void handleResponse(Response response) {

        mProgressBar.setVisibility(View.GONE);
        if (isInit) {
            isInit = false;
            showMessage(response.getMessage());
            mTiEmail.setVisibility(View.GONE);
            mTiToken.setVisibility(View.VISIBLE);
            mTiPassword.setVisibility(View.VISIBLE);

        } else {
            mListner.onPasswordReset(response.getMessage());
        }
    }

    /**
     * Handle network errors
     * @param error The network error
     */
    private void handleError(Throwable error) {

        mProgressBar.setVisibility(View.GONE);

        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                showMessage(response.getMessage());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            showMessage("Network Error !");
        }
    }

    /**
     * Shows a message to the user
     * @param message The message to show
     */
    private void showMessage(String message) {

        mTvMessage.setVisibility(View.VISIBLE);
        mTvMessage.setText(message);

    }

    /**
     * Overwritten onDestroy Method, simply unsubscribes
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }
}
