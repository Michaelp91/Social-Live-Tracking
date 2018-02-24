package com.slt.fragments;

import android.app.DialogFragment;
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
import com.slt.ProfileActivity;
import com.slt.R;
import com.slt.model.Response;
import com.slt.model.User;
import com.slt.network.NetworkUtil;
import com.slt.utils.Constants;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.slt.utils.Validation.validateFields;

/**
 * Class for the Change password dialog
 */
public class ChangePasswordDialog extends DialogFragment {

    /**
     * Listener to get password changes
     */
    public interface Listener {

        /**
         * On password changed.
         */
        void onPasswordChanged();
    }

    /**
     *  TAG for the class
     */
    public static final String TAG = ChangePasswordDialog.class.getSimpleName();

    /**
     * Element for the old password
     */
    private EditText mEtOldPassword;

    /**
     * Element for the new password
     */
    private EditText mEtNewPassword;

    /**
     * Element for the change password button
     */
    private Button mBtChangePassword;

    /**
     * Element for the cancel button
     */
    private Button mBtCancel;

    /**
     * Element to show a message
     */
    private TextView mTvMessage;

    /**
     * Element for the text input of the old password
     */
    private TextInputLayout mTiOldPassword;

    /**
     * Element for the text input of the new password
     */
    private TextInputLayout mTiNewPassword;

    /**
     * Progressbar
     */
    private ProgressBar mProgressBar;

    /**
     * Subscriptions
     */
    private CompositeSubscription mSubscriptions;

    /*
     * String to store the token
     */
    private String mToken;

    /**
     * String to store the email
     */
    private String mEmail;

    /**
     * Listener for button clicks
     */
    private Listener mListener;

    /**
     * Overwritten onCreateView Method, initializes the data
     * @param inflater Inflater for the elements
     * @param container The ViewGroup
     * @param savedInstanceState The saved instance state
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_change_password,container,false);
        mSubscriptions = new CompositeSubscription();
        getData();
        initViews(view);
        return view;
    }

    /**
     * Set the listener
     *
     * @param listener the listener
     */
    public void setListener(ChangePasswordDialog.Listener listener){
        this.mListener = listener;
    }

    /**
     * Get the data from the bundle
     */
    private void getData() {
        Bundle bundle = getArguments();
        mToken = bundle.getString(Constants.TOKEN);
        mEmail = bundle.getString(Constants.EMAIL);
    }

    /**
     * Overwritten onAttack Method
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /**
     * Initializes the view
     * @param v The view to initialize
     */
    private void initViews(View v) {

        mEtOldPassword = (EditText) v.findViewById(R.id.et_old_password);
        mEtNewPassword = (EditText) v.findViewById(R.id.et_new_password);
        mTiOldPassword = (TextInputLayout) v.findViewById(R.id.ti_old_password);
        mTiNewPassword = (TextInputLayout) v.findViewById(R.id.ti_new_password);
        mTvMessage = (TextView) v.findViewById(R.id.tv_message);
        mBtChangePassword = (Button) v.findViewById(R.id.btn_change_password);
        mBtCancel = (Button) v.findViewById(R.id.btn_cancel);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progress);

        //set the listeners
        mBtChangePassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
             changePassword();
            }
        });
        mBtCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * The change password method
     */
    private void changePassword() {

        setError();

        //get the entered passwords
        String oldPassword = mEtOldPassword.getText().toString();
        String newPassword = mEtNewPassword.getText().toString();

        int err = 0;

        //check if passwords were entered
        if (!validateFields(oldPassword)) {

            err++;
            mTiOldPassword.setError("Password should not be empty !");
        }

        if (!validateFields(newPassword)) {

            err++;
            mTiNewPassword.setError("Password should not be empty !");
        }

        //if no errors, show in progress and start network request
        if (err == 0) {
            User user = new User();
            user.setPassword(oldPassword);
            user.setNewPassword(newPassword);
            changePasswordProgress(user);
            mProgressBar.setVisibility(View.VISIBLE);

        }
    }

    /**
     * Set on error
     */
    private void setError() {

        mTiOldPassword.setError(null);
        mTiNewPassword.setError(null);
    }

    /**
     * Set the network interactions
     * @param user The user to change the password for
     */
    private void changePasswordProgress(User user) {

        mSubscriptions.add(NetworkUtil.getRetrofit(mToken).changePassword(mEmail,user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())

         .subscribe(new Action1<Response>() {
            @Override
            public void call(Response response) {
                handleResponse(response);

            }
        }, new Action1<Throwable>(){
            @Override
            public void call(Throwable error) {
                handleError(error);
            }
        } ));
    }

    /**
     * Handles the response from the server
     * @param response The response
     */
    private void handleResponse(Response response) {

        mProgressBar.setVisibility(View.GONE);
        mListener.onPasswordChanged();
        dismiss();
    }

    /**
     * Handles the network errors
     * @param error
     */
    private void handleError(Throwable error) {

        mProgressBar.setVisibility(View.GONE);

        //if it is a http exceptions
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

            showMessage("Network Error!");
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
     * Overwritten onDestory Method, simply unsubscribes
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }
}
