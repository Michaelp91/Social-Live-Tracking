package com.slt.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.slt.MainProfile;
import com.slt.R;

import com.slt.ViewPagerAdapter;
import com.slt.control.DataProvider;
import com.slt.data.User;
import com.slt.fragments.tabfragments.BikingFragment;
import com.slt.fragments.tabfragments.RunningFragment;
import com.slt.fragments.tabfragments.WalkingFragment;
import com.slt.restapi.OtherRestCalls;

import java.util.LinkedList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class FragmentAchievements extends Fragment  {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    /**
     * Simple progress dialog
     */

    private ProgressDialog dialog ;

    /**
     * Clock for show/hide ActionBar
     */
    private ScheduledThreadPoolExecutor clock = new ScheduledThreadPoolExecutor( 2 );
    private ScheduledFuture hide_timer;

    /**
     * Handler for network transactions
     */
    private Handler handler;




    /**
     * icons for View Pager
     */
    private int[] tabIcons = {
            R.drawable.ic_action_walking,
            R.drawable.ic_action_running,
            R.drawable.ic_action_biking
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.achievements_fragment, container, false);

        initViews(view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        //getActivity().setTitle("Ranking");

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        tabLayout = (TabLayout) view.findViewById(R.id.tablayout);

        //handler to wait for a network response
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                setupViewPager(viewPager);
                tabLayout.setupWithViewPager(viewPager);
                setupTabIcons();
                dialog.dismiss();
                return false;
            }
        });

        handler.post(runnableWalking);
        getActivity().onBackPressed();
        dialog = ProgressDialog.show(getActivity(), "Please Wait...", "", true);




    }




    private void initViews(View v){

        toolbar = (Toolbar) v.findViewById(R.id.toolbar1);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

//
//        actionBar.setDisplayShowCustomEnabled(true);
//        actionBar.setDisplayShowTitleEnabled(false);
//
//        LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflator.inflate(R.layout.button_for_action_bar, null);
//        actionBar.setCustomView(view);

        actionBar.hide();

    }

    private synchronized void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(((AppCompatActivity)getActivity()).getSupportFragmentManager());

        adapter.addFragment(new WalkingFragment(), "Walking");
        adapter.addFragment(new RunningFragment(), "Running");
        adapter.addFragment(new BikingFragment(), "Biking");
        viewPager.setAdapter(adapter);

    }


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    /**
     * Runnable to async load the friends from the server
     */
    public Runnable runnableWalking = new Runnable() {
        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    //retrieve and store friends via rest
                    LinkedList<User> users = new LinkedList<>();
                    OtherRestCalls.retrieveFriends();

                    users.add( DataProvider.getInstance().getOwnUser()  );
                    users.addAll( OtherRestCalls.retrieveFriendsIncludingTimelines() );


                    DataProvider.getInstance().changeFriendList( users );

                    handler.sendEmptyMessage( 0 );

                }
            }).start();

        }
    };


}



