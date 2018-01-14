package com.slt.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.slt.R;
import com.slt.control.ApplicationController;
import com.slt.control.DataProvider;
import com.slt.control.SharedResources;
import com.slt.data.User;

/**
 * Created by Thorsten on 06.01.2018.
 */

public class FragmentFriendDetails  extends Fragment  {

    private static final String TAG = "FragmentFriendDetails";
    private EditText usernameEditText;
    private EditText lastNameEditText;
    private EditText foreNameEditText;
    private EditText ageEditText;
    private EditText cityEditText;
    private EditText emailEditText;
    private User shownUser;
    private ImageView mProfilePhoto;
    private Bitmap bitmap;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_details, container, false);

        mProfilePhoto =  (ImageView) view.findViewById(R.id.friend_detail_profile_image);

        this.ageEditText = (EditText) view.findViewById(R.id.friend_detail_et_age);
        this.usernameEditText = (EditText) view.findViewById(R.id.friend_detail_et_username);
        this.lastNameEditText = (EditText) view.findViewById(R.id.friend_detail_et_last_name);
        this.foreNameEditText = (EditText) view.findViewById(R.id.friend_detail_et_forename);
        this.cityEditText = (EditText) view.findViewById(R.id.friend_detail_et_city);
        this.emailEditText = (EditText) view.findViewById(R.id.friend_detail_et_email);

        this.shownUser = SharedResources.getInstance().getSelectedUser();

        if(shownUser.getMyImage() == null) {
            Bitmap image = BitmapFactory.decodeResource(ApplicationController.getContext().getResources(), R.drawable.profile_pic);
            this.mProfilePhoto.setImageBitmap(image);
        }
        else {
            this.mProfilePhoto.setImageBitmap(shownUser.getMyImage());
        }

        Button remove = (Button) view.findViewById(R.id.friend_detail_btn_remove_user);
        remove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                removeUser();
            }
        });

        Button statistics = (Button) view.findViewById(R.id.friend_detail_btn_statistics);
        statistics.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showStatistics();
            }
        });

        Button timeline = (Button) view.findViewById(R.id.friend_detail_btn_show_timeline);
        statistics.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showStatistics();
            }
        });

        return view;
    }


    private void removeUser() {

        DataProvider.getInstance().getOwnUser().deleteFriend(this.shownUser);
        SharedResources.getInstance().setUser(null);

        Fragment newFragment = new FragmentFriends();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.content_main_frame, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showStatistics(){
        //TODO add transition to statistics
    }

    private void showTimeline() {
        //TODO add transition to timeline
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Friend Details");

        this.ageEditText.setText(Integer.toString(shownUser.getMyAge()));
        this.usernameEditText.setText(shownUser.getUserName());
        this.lastNameEditText.setText(shownUser.getLastName());
        this.foreNameEditText.setText(shownUser.getForeName());
        this.cityEditText.setText(shownUser.getMyCity());
        this.emailEditText.setText(shownUser.getEmail());


    }

}
