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

/**
 *
 */
public class FragmentSearchFriends extends Fragment {

    /**
     * Tag for the logger
     */
    public static final String TAG = FragmentSearchFriends.class.getSimpleName();

    /**
     * The data we want to show
     */
    ArrayList<User> dataModels;

    /**
     * List view for the users
     */
    ListView listView;

    /**
     * Adapter for the userlist
     */
    private static FriendSearchListAdapter adapter;

    /**
     * Element for the search text
     */
    private TextView enteredText;

    /**
     * Button to search users
     */
    private Button searchButton;

    /**
     * RadioGroup for the options
     */
    private RadioGroup radioGroup;

    /**
     * Handler for network transactions
     */
    private Handler handler;

    /**
     * Progress Bar
     */
    private ProgressBar mProgressBar;

    /**
     * List of the retrieved users
     */
    private LinkedList<User> retrievedUsers;

    /**
     * Overwritten onCreateViewMethod, intializes the elements
     * @param inflater Inflater for the layout
     * @param container The View Group
     * @param savedInstanceState The saved instance state
     * @return The created view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_search, container, false);
        retrievedUsers = new LinkedList<>();
        return view;
    }


    /**
     * Overwritten onViewCreated Method
     * @param view The view
     * @param savedInstanceState The saved instance state
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Find Friends");

        //create a linked list for the users
        LinkedList<User> users = new LinkedList<>();

        //init the elements
        listView=(ListView) view.findViewById(R.id.friends_search_listview);
        searchButton=(Button) view.findViewById(R.id.friend_search_btn_search);
        radioGroup =(RadioGroup) view.findViewById(R.id.friends_search_options);
        enteredText = (TextView) view.findViewById(R.id.friends_search_t_text_input);
        mProgressBar =  (ProgressBar) view.findViewById(R.id.search_progress) ;

        //handler to wait for a network response
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

        //add lists for the data
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

    /**
     * After data was retrieved, search for fitting users
     */
    public void afterRetrieval() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        String text = null;
        retrievedUsers.clear();

        //check for mode to search for fitting users
        switch (selectedId){
            case R.id.friends_search_option_email:
                //by email
                text = enteredText.getText().toString();
                retrievedUsers = DataProvider.getInstance().getUserByEMail(text);
                break;
            case R.id.friends_search_option_nearby:
                //by distance
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
                //by name
                text = enteredText.getText().toString();
                retrievedUsers = DataProvider.getInstance().getUserByName(text);


                adapter.notifyDataSetChanged();
                break;

            case R.id.friends_search_option_username:
                //by username
                text = enteredText.getText().toString();
                retrievedUsers = DataProvider.getInstance().getUserByUsername(text);

                adapter.notifyDataSetChanged();
                break;
        }

        handler.post(runnableImages);

    }

    /**
     *  Shows meassage to the user
     * @param message The message to show
     */
    private void showSnackBarMessage(String message) {

        if (getView() != null) {

            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        }
    }
}
