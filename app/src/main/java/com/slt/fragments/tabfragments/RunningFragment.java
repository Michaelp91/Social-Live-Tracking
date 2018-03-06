package com.slt.fragments.tabfragments;

import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.slt.R;
import com.slt.control.ApplicationController;
import com.slt.control.DataProvider;
import com.slt.control.SharedResources;
import com.slt.data.User;
import com.slt.fragments.adapters.RankingListAdapter;
import com.slt.restapi.OtherRestCalls;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;


public class RunningFragment extends Fragment {

    public RunningFragment(){

    }
    /**
     * Tag for the logger
     */
    public static final String TAG = RunningFragment.class.getSimpleName();

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
        View view = inflater.inflate(R.layout.running_fragment, container, false);
        SharedResources.getInstance().setUser(null);

        mProgressBar =  (ProgressBar) view.findViewById(R.id.running_progress) ;

        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        mProgressBar.setVisibility(View.VISIBLE);


        //create a linked list for the users
        LinkedList<User> users = DataProvider.getInstance().getOwnUser().getUserList();

        //init the elements
        listView=(ListView) view.findViewById(R.id.running_listview);
        mylistView = (ListView) view.findViewById( R.id.myrunning_list );


        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(),
                R.array.settings_array, R.layout.spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = (Spinner) view.findViewById(R.id.spinnerRunning);
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

    /**
     * After data was retrieved, search for fitting users
     */
    public void afterRetrieval() {

        //new ArrayList for data compare
        ArrayList<User> dataCompare = new ArrayList<>(  );

        dataModels.clear();
        myData.clear();

        dataCompare.addAll(DataProvider.getInstance().getUserList());

        //Distance compare for Month or Week
        if (getChoosePeriod() == 0) {
            //Distance compare
            Collections.sort(dataCompare, new Comparator<User>() {
                @Override
                public int compare(User s1, User s2) {
                    return Double.compare(s2.getMyTimeline().getActiveDistanceForMonth( 8 ), s1.getMyTimeline().getActiveDistanceForMonth( 8 ));
                } } );
        }

        else if (getChoosePeriod() == 1){
            //Distance compare
            Collections.sort(dataCompare, new Comparator<User>() {
                @Override
                public int compare(User s1, User s2) {
                    return Double.compare(s2.getMyTimeline().getActiveDistanceForWeek( 8 ), s1.getMyTimeline().getActiveDistanceForWeek( 8 ));
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

    public int getChoosePeriod() {
        return choosePeriod;
    }

    public void setChoosePeriod(int choosePeriod) {
        this.choosePeriod = choosePeriod;
    }

    public void refreshAdapter(){

        adapter= new RankingListAdapter(dataModels, ApplicationController.getContext(),8,getChoosePeriod());
        ownadapter = new RankingListAdapter(myData, ApplicationController.getContext(),8,getChoosePeriod());

        listView.setAdapter(adapter);
        mylistView.setAdapter( ownadapter );
    }


}


