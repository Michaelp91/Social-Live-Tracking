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
import android.widget.ListView;

import com.google.ads.mediation.customevent.CustomEventAdapter;
import com.slt.R;
import com.slt.control.ApplicationController;
import com.slt.control.DataProvider;
import com.slt.control.SharedResources;
import com.slt.data.User;
import com.slt.fragments.adapters.FriendListAdapter;

import java.util.ArrayList;
import java.util.LinkedList;


public class FragmentFriends extends Fragment {

    ArrayList<User> dataModels;
    ListView listView;
    private static FriendListAdapter adapter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.friends_fragment, container, false);
        SharedResources.getInstance().setUser(null);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
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

