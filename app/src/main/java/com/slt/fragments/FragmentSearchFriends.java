package com.slt.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
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
import com.slt.restapi.UsefulMethods;

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

    private LinkedList<User> retrievedUsers;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_search, container, false);
        retrievedUsers = new LinkedList<>();
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
                if(msg.arg1 == 0){
                    afterRetrieval();
                }
                if(msg.arg1 == 1) {
                    dataModels.clear();
                    dataModels.addAll(retrievedUsers);

                    adapter.notifyDataSetChanged();
                    mProgressBar.setVisibility(View.GONE);
                }

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

                    Message msg = new Message();
                    msg.arg1 = 0;
                    handler.sendMessage(msg );

                }
            }).start();
        }
    };

    /**
     * Runnable to async load all users from the server
     */
    public Runnable runnableImages = new Runnable() {
        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //REST Call to retrieve the current User List
                        for (User user : retrievedUsers){
                            Bitmap bitmap = UsefulMethods.LoadImage(user);

                            user.setMyImage(bitmap);
                        }

                    Message msg = new Message();
                    msg.arg1 = 1;
                    handler.sendMessage(msg );

                }
            }).start();
        }
    };

    public void afterRetrieval() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        String text = null;
        retrievedUsers.clear();

        switch (selectedId){
            case R.id.friends_search_option_email:
                text = enteredText.getText().toString();
                retrievedUsers = DataProvider.getInstance().getUserByEMail(text);
                break;
            case R.id.friends_search_option_nearby:
                text = enteredText.getText().toString();
                dataModels.clear();

                try {
                    Double distance = Double.parseDouble(text);
                    retrievedUsers = DataProvider.getInstance().getNearbyUsers(distance);

                } catch (NumberFormatException exception){
                    showSnackBarMessage("Input could not be processed, Should be XX.X");
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.friends_search_option_name:
                text = enteredText.getText().toString();
                retrievedUsers = DataProvider.getInstance().getUserByName(text);


                adapter.notifyDataSetChanged();
                break;

            case R.id.friends_search_option_username:
                text = enteredText.getText().toString();
                retrievedUsers = DataProvider.getInstance().getUserByUsername(text);

                adapter.notifyDataSetChanged();
                break;
        }

        handler.post(runnableImages);

    }


    private void showSnackBarMessage(String message) {

        if (getView() != null) {

            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        }
    }
}
