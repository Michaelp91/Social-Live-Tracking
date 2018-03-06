package com.slt.fragments.tabfragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;


import com.slt.R;
import com.slt.control.ApplicationController;
import com.slt.control.DataProvider;
import com.slt.control.SharedResources;
import com.slt.data.User;

import com.slt.fragments.FragmentAchievements;
import com.slt.fragments.adapters.RankingListAdapter;
import com.slt.restapi.OtherRestCalls;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;


public class WalkingFragment extends Fragment {

    public WalkingFragment(){

    }

    // TODO: Rename and change types and number of parameters
    public static WalkingFragment newInstance() {
        return new WalkingFragment();
    }

    /**
     * Tag for the logger
     */
    public static final String TAG = WalkingFragment.class.getSimpleName();

    /**
     * The data we want to show
     */
    ArrayList<User> dataModels, myData;

    /**
     * List view for the users
     */
    ListView listView, mylistView;

    /**
     * Adapter for the userlist
     */
    private static RankingListAdapter adapter, ownadapter;


    /**
     * Handler for network transactions
     */
    private Handler handler;

    /**
     * Progress Bar
     */
    private ProgressBar mProgressBar;


    /**
     * Simple progress dialog
     */

    private ProgressDialog dialog ;

    /**
     * spinner for choose Month or Week
     */
    private Spinner spinner;

    /**
     * value for period
     */
    private int choosePeriod;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.walking_fragment, container, false);
        SharedResources.getInstance().setUser(null);

        mProgressBar =  (ProgressBar) view.findViewById(R.id.walking_progress) ;


        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

//        //handler to wait for a network response
//        handler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//
//                afterRetrieval();
//                return false;
//            }
//        });

        mProgressBar.setVisibility(View.VISIBLE);


//        handler.post(runnableWalking);
//        dialog = ProgressDialog.show(getActivity(), "Please Wait...", "", true);

        //create a linked list for the users
        LinkedList<User> users = DataProvider.getInstance().getOwnUser().getUserList();

        //init the elements
        listView=(ListView) view.findViewById(R.id.walking_listview);
        mylistView = (ListView) view.findViewById( R.id.mywalking_list );


        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(),
                R.array.settings_array, R.layout.spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = (Spinner) view.findViewById(R.id.spinnerWalking);
        spinner.setAdapter(adapter1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                choosePeriod = Integer.valueOf( position );
                setChoosePeriod( choosePeriod );

                refreshAdapter();
                afterRetrieval();




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                choosePeriod = 0;
                setChoosePeriod( choosePeriod );
            }
        });



        //add lists for the data
        dataModels= new ArrayList<>(users);
        myData = new ArrayList<>( users );


        //init adapters
        refreshAdapter();

        afterRetrieval();



    }


//    /**
//     * Runnable to async load the friends from the server
//     */
//    public Runnable runnableWalking = new Runnable() {
//        @Override
//        public void run() {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//
//                        //retrieve and store friends via rest
//                        LinkedList<User> users = new LinkedList<>();
//                        OtherRestCalls.retrieveFriends();
//
//                        users.add( DataProvider.getInstance().getOwnUser()  );
//                        users.addAll( OtherRestCalls.retrieveFriendsIncludingTimelines() );
//
//                        DataProvider.getInstance().changeFriendList( users );
//
//                        handler.sendEmptyMessage( 0 );
//
//                }
//            }).start();
//
//        }
//    };

    /**
     * After data was retrieved, search for fitting users
     */
    public void afterRetrieval() {

        //new ArrayList for data compare
        ArrayList<User> dataCompare = new ArrayList<>(  );

//        dialog.dismiss();

        dataModels.clear();
        myData.clear();

        dataCompare.addAll(DataProvider.getInstance().getUserList());


        //Distance compare for Month or Week
        if (getChoosePeriod() == 0) {
            //Distance compare
            Collections.sort(dataCompare, new Comparator<User>() {
                @Override
                public int compare(User s1, User s2) {
                    return Double.compare(s2.getMyTimeline().getActiveDistanceForMonth( 7 ), s1.getMyTimeline().getActiveDistanceForMonth( 7 ));
                } } );
        }

        else if (getChoosePeriod() == 1){
            //Distance compare
            Collections.sort(dataCompare, new Comparator<User>() {
                @Override
                public int compare(User s1, User s2) {
                    return Double.compare(s2.getMyTimeline().getActiveDistanceForWeek( 7 ), s1.getMyTimeline().getActiveDistanceForWeek( 7 ));
                } } );
        }

        //init int for rank number
        int i = 1;

        //assign a rank to the user
        for (User user : dataCompare ){
            user.setRank( i );
            i++;
        }

        dataModels.addAll(dataCompare);
        myData.add( DataProvider.getInstance().getOwnUser() );



        mProgressBar.setVisibility(View.GONE);

        adapter.notifyDataSetChanged();
        ownadapter.notifyDataSetChanged();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public int getChoosePeriod() {
        return choosePeriod;
    }

    public void setChoosePeriod(int choosePeriod) {
        this.choosePeriod = choosePeriod;
    }

    //refresh adapters
    public void refreshAdapter(){

        adapter= new RankingListAdapter(dataModels, ApplicationController.getContext(),7,getChoosePeriod());
        ownadapter = new RankingListAdapter(myData, ApplicationController.getContext(),7,getChoosePeriod());

        listView.setAdapter(adapter);
        mylistView.setAdapter( ownadapter );
    }

}


