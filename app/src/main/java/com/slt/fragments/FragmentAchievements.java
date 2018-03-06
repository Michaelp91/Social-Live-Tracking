package com.slt.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.slt.R;
import com.slt.ViewPagerAdapter;
import com.slt.control.DataProvider;
import com.slt.data.User;
import com.slt.fragments.tabfragments.BikingFragment;
import com.slt.fragments.tabfragments.RunningFragment;
import com.slt.fragments.tabfragments.WalkingFragment;
import com.slt.restapi.OtherRestCalls;

import java.util.LinkedList;


/**
 * Fragment to show the ranking list of user's friends
 *
 */
public class FragmentAchievements extends Fragment  {


    /**
     * toolbar configurations
     */
    private Toolbar toolbar;

    /**
     * TabLayout configurations
     */
    private TabLayout tabLayout;

    /**
     * view pager configurations
     */
    private ViewPager viewPager;

    /**
     * Simple progress dialog
     */
    private ProgressDialog dialog ;

    /**
     * Handler for network transactions
     */
    private Handler handler;

    /**
     * icons set for ViewPager
     */
    private int[] tabIcons = {
            R.drawable.ic_action_walking,
            R.drawable.ic_action_running,
            R.drawable.ic_action_biking
    };

    /**
     * Overwritten onCreate Method
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }

    /**
     * Overwritten onCreateView Method
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.achievements_fragment, container, false);

        initViews(view);

        return view;
    }

    /**
     * Overwritten onViewCreated Method
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //you can set the title for your toolbar here for different fragments different titles
        //getActivity().setTitle("Ranking");


        //init viewPager
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        //init TabLayout
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

        //post runnable
        handler.post(runnableWalking);
        //onBackPressed() method
        getActivity().onBackPressed();

        // progress dialog configuration
        dialog = ProgressDialog.show(getActivity(), "Please Wait...", "", true);

    }

    /**
     * Initializes the view
     * @param v The view to initialize
     */
    private void initViews(View v){

        toolbar = (Toolbar) v.findViewById(R.id.toolbar1);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        // hide action bar
        actionBar.hide();

    }

    /**
     * ViewPager setup
     * @param viewPager The ViewPager to initialize
     */
    private synchronized void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(((AppCompatActivity)getActivity()).getSupportFragmentManager());

        //adding fragments to the adapter
        adapter.addFragment(new WalkingFragment(), "Walking");
        adapter.addFragment(new RunningFragment(), "Running");
        adapter.addFragment(new BikingFragment(), "Biking");
        viewPager.setAdapter(adapter);

    }

    /**
     * Icons customize for ViewPager
     */
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

                    try {
                        //retrieve and store friends via rest
                        LinkedList<User> users = new LinkedList<>();
                        OtherRestCalls.retrieveFriends();

                        users.add(DataProvider.getInstance().getOwnUser());
                        users.addAll(OtherRestCalls.retrieveFriendsIncludingTimelines());

                        DataProvider.getInstance().changeFriendList(users);
                    }catch(Exception e) {

                    }

                    handler.sendEmptyMessage( 0 );

                }
            }).start();

        }
    };


}



