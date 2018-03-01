package com.slt.fragments.tabfragments;

import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.slt.R;
import com.slt.control.ApplicationController;
import com.slt.control.DataProvider;
import com.slt.control.SharedResources;
import com.slt.data.User;
import com.slt.fragments.FragmentFriendDetails;
import com.slt.fragments.adapters.FriendListAdapter;
import com.slt.restapi.OtherRestCalls;
import com.slt.restapi.TemporaryDB;
import com.slt.restapi.UsefulMethods;

import java.util.ArrayList;
import java.util.LinkedList;

public class WalkingFragment extends Fragment {


    /**
     * Data model with the users we want to show
     */
    ArrayList<User> dataModels;

    /**
     * List view
     */
    ListView listView, mylistView;

    /**
     * The adapter for our list
     */
    private static FriendListAdapter adapter;

    /**
     * Button to search for friends
     */
    private Button SearchButton;

    /**
     * handler for waiting for network reponse
     */
    private Handler handler;

    /**
     * Progress Bar
     */
    private ProgressBar mProgressBar;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.walking_fragment, container, false);

        SharedResources.getInstance().setUser(null);
        mProgressBar =  (ProgressBar) view.findViewById(R.id.all_progress) ;

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        //set handler to wait for network response
        handler = new Handler( new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                afterRetrieval();
                return false;
            }
        });

        //Call Handler to retrieve REST User
        mProgressBar.setVisibility(View.VISIBLE);
        handler.post(runnable);

        //get the friends to show and add them to the adapter
        LinkedList<User> users = DataProvider.getInstance().getOwnUser().getUserList();
        listView=(ListView) view.findViewById(R.id.all_listview);

        mylistView=(ListView) view.findViewById(R.id.my_list);

        dataModels= new ArrayList<>(users);
        adapter= new FriendListAdapter(dataModels, ApplicationController.getContext());

        //add listener to transition to friends detail page
        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                User dataModel= dataModels.get(position);
//                TemporaryDB.getInstance().setChoosedFriend(dataModel);
//                SharedResources.getInstance().setUser(dataModel);
//
////                android.app.Fragment newFragment = new FragmentFriendDetails();
////                FragmentTransaction transaction = getFragmentManager().beginTransaction();
////
////                transaction.replace(R.id.content_main_frame, newFragment);
////                transaction.addToBackStack(null);
////                transaction.commit();
//            }
//        });

    }


    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //retrieve and store friends via rest
                    LinkedList<User> users = new LinkedList<>();
                    users.addAll( OtherRestCalls.retrieveFriends());

                    DataProvider.getInstance().changeFriendList(users);

                    //load images
                    for (User user : DataProvider.getInstance().getUserList()){
                        Bitmap bitmap = UsefulMethods.LoadImage(user);
                        user.setMyImage(bitmap);
                    }

                    handler.sendEmptyMessage(0);

                }
            }).start();

        }
    };


    /**
     * After retrieval remove progress bar
     */
    public void afterRetrieval() {
        dataModels.clear();
        dataModels.addAll(DataProvider.getInstance().getUserList());
        mProgressBar.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
    }
}
