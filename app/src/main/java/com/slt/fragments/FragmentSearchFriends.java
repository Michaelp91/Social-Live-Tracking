package com.slt.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.slt.R;
import com.slt.control.ApplicationController;
import com.slt.control.DataProvider;
import com.slt.data.User;
import com.slt.fragments.adapters.FriendSearchListAdapter;
import com.slt.restapi.RetrieveOperations;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Thorsten on 07.01.2018.
 */

public class FragmentSearchFriends extends Fragment {

    public static final String TAG = FragmentSearchFriends.class.getSimpleName();

    ArrayList<User> dataModels;
    ListView listView;
    private static FriendSearchListAdapter adapter;
    private TextView enteredText;
    private Button searchButton;
    private RadioGroup radioGroup;

    private Handler handler;
    private ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

        mProgressBar =  (ProgressBar) view.findViewById(R.id.search_progress) ;

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                afterRetrieval();
                return false;
            }
        });

        dataModels= new ArrayList<>(users);

        adapter= new FriendSearchListAdapter(dataModels, ApplicationController.getContext());
        listView.setAdapter(adapter);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressBar.setVisibility(View.VISIBLE);
                handler.post(runnable);
            }
        });
    }

    /**
     * Runnable to async load all users from the server
     */
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    LinkedList<User> users = new LinkedList<>();
                    //REST Call to retrieve the current User List
                    ArrayList<User> aUsers = RetrieveOperations.getInstance().retrieveAllUserLists();
                    if(aUsers != null) {
                        users.addAll(aUsers);
                    }

                    DataProvider.getInstance().updateAllUsers(users);

                    handler.sendEmptyMessage(0);

                }
            }).start();
        }
    };

    public void afterRetrieval() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        String text = null;

        switch (selectedId){
            case R.id.friends_search_option_email:
                text = enteredText.getText().toString();
                dataModels.clear();
                dataModels.addAll(DataProvider.getInstance().getUserByEMail(text));

                adapter.notifyDataSetChanged();
                break;
            case R.id.friends_search_option_nearby:
                text = enteredText.getText().toString();
                dataModels.clear();

                try {
                    Double distance = Double.parseDouble(text);
                    dataModels.addAll(DataProvider.getInstance().getNearbyUsers(distance));

                } catch (NumberFormatException exception){
                    showSnackBarMessage("Input could not be processed, Should be XX.X");
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.friends_search_option_name:
                text = enteredText.getText().toString();
                dataModels.clear();
                dataModels.addAll(DataProvider.getInstance().getUserByName(text));

                adapter.notifyDataSetChanged();
                break;

            case R.id.friends_search_option_username:
                text = enteredText.getText().toString();
                dataModels.clear();
                dataModels.addAll(DataProvider.getInstance().getUserByUsername(text));

                adapter.notifyDataSetChanged();
                break;
        }

        mProgressBar.setVisibility(View.GONE);
    }


    private void showSnackBarMessage(String message) {

        if (getView() != null) {

            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        }
    }
}
