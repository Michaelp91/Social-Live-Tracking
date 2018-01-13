package com.slt.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.ads.mediation.customevent.CustomEventAdapter;
import com.slt.R;
import com.slt.control.ApplicationController;
import com.slt.control.DataProvider;
import com.slt.control.SharedResources;
import com.slt.data.User;
import com.slt.fragments.adapters.FriendListAdapter;
import com.slt.utils.Constants;

import java.util.ArrayList;
import java.util.LinkedList;


public class FragmentFriends extends Fragment {

    public static final String TAG = FragmentFriends.class.getSimpleName();

    ArrayList<User> dataModels;
    ListView listView;
    private static FriendListAdapter adapter;

    private Button SearchButton;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.friends_fragment, container, false);
        SharedResources.getInstance().setUser(null);
        //Change to Search Friends Fragment
        SearchButton = (Button) view.findViewById(R.id.btn_search_friends);
        SearchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Fragment newFragment = new FragmentSearchFriends();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentFrame, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }});

        return view;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Friends and Co.");



        LinkedList<User> users = DataProvider.getInstance().getOwnUser().getUserList();

        listView=(ListView) view.findViewById(R.id.friends_listview);

        dataModels= new ArrayList<>(users);

        adapter= new FriendListAdapter(dataModels, ApplicationController.getContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                User dataModel= dataModels.get(position);
                SharedResources.getInstance().setUser(dataModel);

                Fragment newFragment = new FragmentFriendDetails();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.fragmentFrame, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });




    }
}

