package com.slt.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.slt.R;
import com.slt.control.ApplicationController;
import com.slt.control.DataProvider;
import com.slt.control.SharedResources;
import com.slt.data.User;
import com.slt.fragments.adapters.FriendListAdapter;
import com.slt.fragments.adapters.FriendSearchListAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Thorsten on 07.01.2018.
 */

public class FragmentSearchFriends extends Fragment {

    ArrayList<User> dataModels;
    ListView listView;
    private static FriendSearchListAdapter adapter;
    private TextView enteredText;
    private Button searchButton;
    private RadioGroup radioGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_friends_search, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Find Friends");

        LinkedList<User> users = new LinkedList<>();

        listView=(ListView) view.findViewById(R.id.friends_search_listview);

        searchButton=(Button) view.findViewById(R.id.friend_search_btn_search);
        radioGroup =(RadioGroup) view.findViewById(R.id.friends_search_options);
        enteredText = (TextView) view.findViewById(R.id.friends_search_t_text_input);

        dataModels= new ArrayList<>(users);

        adapter= new FriendSearchListAdapter(dataModels, ApplicationController.getContext());
        listView.setAdapter(adapter);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();
                String text = null;

                switch (selectedId){
                    case R.id.friends_search_option_email:
                        text = enteredText.getText().toString();
                        dataModels.clear();
                        //TODO validation of string
                        dataModels.addAll(DataProvider.getInstance().getUserByEMail(text));

                        adapter.notifyDataSetChanged();
                        break;
                    case R.id.friends_search_option_nearby:
                         text = enteredText.getText().toString();
                         Double distance = Double.parseDouble(text);

                         //TODO validate double
                        dataModels.clear();

                        dataModels.addAll(DataProvider.getInstance().getNearbyUsers(distance));

                        adapter.notifyDataSetChanged();
                        break;
                    case R.id.friends_search_option_name:
                        text = enteredText.getText().toString();
                        dataModels.clear();
                        //TODO validation of string
                        dataModels.addAll(DataProvider.getInstance().getUserByName(text));

                        adapter.notifyDataSetChanged();
                        break;

                    case R.id.friends_search_option_username:
                        text = enteredText.getText().toString();
                        dataModels.clear();
                        //TODO validation of string
                        dataModels.addAll(DataProvider.getInstance().getUserByUsername(text));

                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        });


    }
}
