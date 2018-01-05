package com.slt.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.slt.control.ApplicationController;
import com.slt.control.DataProvider;
import com.slt.data.User;
import com.slt.utils.Constants;
import com.slt.utils.UniversalImageLoader;


import com.slt.R;

import org.w3c.dom.Text;


public class FragmentEditSettings extends Fragment implements ChangePasswordDialog.Listener {

    private static final String TAG = "EditSettingsFragment";
    private SharedPreferences mSharedPreferences;
    private String mToken;
    private String mEmail;
    private View view;
    private EditText usernameEditText;
    private EditText lastNameEditText;
    private EditText foreNameEditText;
    private EditText ageEditText;
    private EditText cityEditText;

    private ChangePasswordDialog.Listener mListener;

    private ImageView mProfilePhoto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        //return inflater.inflate(R.layout.edit_settings_fragment, container, false);
        view = inflater.inflate(R.layout.edit_settings_fragment, container, false);
        mProfilePhoto =  (ImageView) view.findViewById(R.id.profile_image);
        this.mListener = this;
        this.ageEditText = (EditText) view.findViewById(R.id.et_age);
        this.usernameEditText = (EditText) view.findViewById(R.id.et_username);
        this.lastNameEditText = (EditText) view.findViewById(R.id.et_last_name);
        this.foreNameEditText = (EditText) view.findViewById(R.id.et_forename);
        this.cityEditText = (EditText) view.findViewById(R.id.et_city);



        //init for the password change dialog
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext());
        mToken = mSharedPreferences.getString(Constants.TOKEN,"");
        mEmail = mSharedPreferences.getString(Constants.EMAIL,"");

        //initImageLoader();

        User ownUser = DataProvider.getInstance().getOwnUser();


        this.ageEditText.setText(Integer.toString(ownUser.getMyAge()));
        this.usernameEditText.setText(ownUser.getUserName());
        this.lastNameEditText.setText(ownUser.getLastName());
        this.foreNameEditText.setText(ownUser.getForeName());
        this.cityEditText.setText(ownUser.getMyCity());

        setProfileImage(ownUser.getMyImage());

        Button pwd = (Button) view.findViewById(R.id.et_password);
        pwd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ChangePasswordDialog fragment = new ChangePasswordDialog();
                fragment.setListener(mListener);

                Bundle bundle = new Bundle();
                bundle.putString(Constants.EMAIL, mEmail);
                bundle.putString(Constants.TOKEN, mToken);
                fragment.setArguments(bundle);

                fragment.show(getFragmentManager(), ChangePasswordDialog.TAG);

            }});

        return view;
    }


    private void initImageLoader(){
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(getActivity());
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void setProfileImage(Bitmap img){
        Log.d(TAG, "setProfileImage: setting profile image.");
        //check if we have a picture to show, if not default is shown
        if(img == null) {
            Bitmap image = BitmapFactory.decodeResource(ApplicationController.getContext().getResources(), R.drawable.profile_pic);
            this.mProfilePhoto.setImageBitmap(image);
        }
        else {
            this.mProfilePhoto.setImageBitmap(img);
        }

     //   String imgURL = "www.androidcentral.com/sites/androidcentral.com/files/styles/xlarge/public/article_images/2016/08/ac-lloyd.jpg?itok=bb72IeLf";
     //   UniversalImageLoader.setImage(imgURL, mProfilePhoto, null, "https://");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Edit Settings");
    }

    @Override
    public void onPasswordChanged() {

        Snackbar.make(view.findViewById(R.id.activity_profile),"Password Changed Successfully !",Snackbar.LENGTH_SHORT).show();
    }

}

