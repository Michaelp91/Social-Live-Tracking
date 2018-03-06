package com.slt.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.slt.R;
import com.slt.TimelineFriend;
import com.slt.control.ApplicationController;
import com.slt.control.DataProvider;
import com.slt.control.SharedResources;
import com.slt.data.User;
import com.slt.restapi.OtherRestCalls;
import com.slt.restapi.TemporaryDB;
import com.slt.statistics.StatisticsOverviewFragment;

/**
 * The friend details fragment
 */

public class FragmentFriendDetails  extends Fragment  {


    /**
     * Tag for the logger
     */
    private static final String TAG = "FragmentFriendDetails";

    /**
     * Element for the username
     */
    private EditText usernameEditText;

    /**
     * Element for the last name
     */
    private EditText lastNameEditText;

    /**
     * Element for the forename
     */
    private EditText foreNameEditText;

    /**
     * Element for the age
     */
    private EditText ageEditText;

    /**
     * Element for the city
     */
    private EditText cityEditText;

    /**
     * Element for the email
     */
    private EditText emailEditText;

    /**
     * Friend we want to show
     */
    private User shownUser;

    /**
     * Element for the image of the user
     */
    private ImageView mProfilePhoto;

    /**
     * Bitmap of the userimage
     */
    private Bitmap bitmap;

    /**
     * manager for switching fragments
     */
    private FragmentManager fragmentManager_v4 = null;


    /**
     * Overwritten onCreateViewMethod, intializes the elements
     * @param inflater Inflater for the layout
     * @param container The View Group
     * @param savedInstanceState The saved instance state
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_details, container, false);

        //get the elements
        mProfilePhoto =  (ImageView) view.findViewById(R.id.friend_detail_profile_image);
        this.ageEditText = (EditText) view.findViewById(R.id.friend_detail_et_age);
        this.usernameEditText = (EditText) view.findViewById(R.id.friend_detail_et_username);
        this.lastNameEditText = (EditText) view.findViewById(R.id.friend_detail_et_last_name);
        this.foreNameEditText = (EditText) view.findViewById(R.id.friend_detail_et_forename);
        this.cityEditText = (EditText) view.findViewById(R.id.friend_detail_et_city);
        this.emailEditText = (EditText) view.findViewById(R.id.friend_detail_et_email);

        //get the user to show
        this.shownUser = SharedResources.getInstance().getSelectedUser();

        //if use has a image, show it
        if(shownUser.getMyImage() == null) {
            Bitmap image = BitmapFactory.decodeResource(ApplicationController.getContext().getResources(), R.drawable.profile_pic);
            this.mProfilePhoto.setImageBitmap(image);
        }
        else {
            this.mProfilePhoto.setImageBitmap(shownUser.getMyImage());
        }

        //init the remove user button
        Button remove = (Button) view.findViewById(R.id.friend_detail_btn_remove_user);
        remove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                removeUser();
            }
        });

        //init the statistics button
        Button statistics = (Button) view.findViewById(R.id.friend_detail_btn_statistics);
        statistics.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showStatistics();
            }
        });

        //init the show timeline button
        Button timeline = (Button) view.findViewById(R.id.friend_detail_btn_show_timeline);
        timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeline();
            }
        });

        ;

        return view;
    }

    /**
     * Remove the user from the friends
     */
    private void removeUser() {
        //remove friend from data
        DataProvider.getInstance().getOwnUser().deleteFriend(this.shownUser);
        SharedResources.getInstance().setUser(null);

        //REST Call to update UserList
        OtherRestCalls.updateUser(true); //TODO: Include Friends Update or not? I think so.

        //transaction back to previous page
        Fragment newFragment = new FragmentFriends();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.content_main_frame, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    /**
     * Show the statistics of the user
     */
    private void showStatistics(){
         DataProvider.getInstance().setOwnUser(shownUser);
         DataProvider.getInstance().syncTimelineToUser();



        android.support.v4.app.Fragment fragment123 = null;
        fragment123 = new StatisticsOverviewFragment();

        android.support.v4.app.FragmentTransaction fragmentTransaction =
                this.fragmentManager_v4.beginTransaction();

        fragmentTransaction.replace(R.id.content_main_frame, fragment123);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }


    /**
     * Show the timeline of the user
     */
    private void showTimeline() {
        Intent i = new Intent(this.getActivity(), TimelineFriend.class);
        startActivity(i);
    }

    /**
     * Overwritten onViewCreated Method,
     * @param view The view
     * @param savedInstanceState The saved instance state
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Friend Details");

        //init the elements with the data
        this.ageEditText.setText(Integer.toString(shownUser.getMyAge()));
        this.usernameEditText.setText(shownUser.getUserName());
        this.lastNameEditText.setText(shownUser.getLastName());
        this.foreNameEditText.setText(shownUser.getForeName());
        this.cityEditText.setText(shownUser.getMyCity());
        this.emailEditText.setText(shownUser.getEmail());


    }

    public FragmentManager getFragmentManager_v4() {
        return fragmentManager_v4;
    }

    public void setFragmentManager_v4(FragmentManager fragmentManager_v4) {
        this.fragmentManager_v4 = fragmentManager_v4;
    }
}
