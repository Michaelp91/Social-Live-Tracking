package com.slt.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.slt.R;
import com.slt.control.ApplicationController;
import com.slt.control.DataProvider;
import com.slt.data.TimelineDay;
import com.slt.data.User;
import com.slt.fragments.adapters.LiveMapListAdapter;
import com.slt.restapi.OtherRestCalls;
import com.slt.restapi.RetrieveOperations;
import com.slt.restapi.UsefulMethods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Usman Ahmad on 20.01.2018.
 *
 * show all the friends in the live map
 */

public class FragmentLiveMap extends Fragment {

    /**
     * googleMap Object for the Map View
     */
    private GoogleMap googleMap;

    /**
     * mMapView
     */
    MapView mMapView;

    /**
     * list view
     */
    ListView listView;

    /**
     * adapter for notify changes
     */
    private static LiveMapListAdapter adapter;

    /**
     * handler for retrieving data from the server for every 2 sec.
     */
    public Handler handler = new Handler();

    /**
     * list of user friends
     */
    private ArrayList<User> restUsers;

    /**
     * list of user friends
     */
    private ArrayList<User> list_friends = new ArrayList<>();


    /**
     * hashmap for storing the viewed friends
     */
    private HashMap<String, User> h_viewedFriends = new HashMap<>();


    /**
     * check if the friend is not viewed
     * @param id current friend with object id
     * @return true if the friend is not viewed, otherwise false
     */
    public boolean FriendIsNotViewed(String id) {
        return (h_viewedFriends.get(id) == null)? true:false;
    }

    /**
     * overwritten onCreateView Method
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_livemap, container, false);



        return view;

    }

    /**
     * runnable for retrieving friends in every 2 sec.
     */
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
      /* do what you need to do */
            new Thread(new Runnable() {
                @Override
                public void run() {
                    restUsers = OtherRestCalls.retrieveFriends();

                    ShowFriends();
                    handler.postDelayed(runnable, 2000);

                }
            }).start();

      /* and here comes the "trick" */
        }
    };

    /**
     * display all the friends
     */
    private void ShowFriends() {
        final ArrayList<Marker> markers = new ArrayList<>();

                restUsers = OtherRestCalls.retrieveFriends();
                //list_friends.clear();
                //list_friends.addAll(restUsers);

                try {




                }
                    catch (NullPointerException e){

                }

                for(final User u: restUsers) {


                    if(FriendIsNotViewed(u.getID())) {
                        h_viewedFriends.put(u.getID(), u);
                        list_friends.add(u);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });

                        Location location = u.getLastLocation();
                        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        //final Bitmap bmp = UsefulMethods.LoadImage(u);
                        final Bitmap bmp = null;

                        try {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Marker marker = null;

                                    if (bmp != null)
                                        marker = googleMap.addMarker(new MarkerOptions().position(latLng)
                                                .title(u.getEmail()).icon(BitmapDescriptorFactory.fromBitmap(bmp)));
                                    else
                                        marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(u.getEmail()).icon(BitmapDescriptorFactory.fromResource(R.drawable.pegman)));

                                    markers.add(marker);
                                    boolean debug = true;
                                }
                            });
                        } catch (NullPointerException e) {

                        }
                    }

                }
    }


    /**
     * overwritten onViewCreated
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Live Map");
        getActivity().onBackPressed();

        listView=(ListView) view.findViewById(R.id.live_map_listview);



        adapter= new LiveMapListAdapter(list_friends, ApplicationController.getContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                User user = list_friends.get(position);

                Location ownLocation = user.getLastLocation();

                if(ownLocation != null) {
                    LatLng ownLatLng = new LatLng(ownLocation.getLatitude(), ownLocation.getLongitude());

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ownLatLng,
                            15));
                }



            }
        });

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                User ownUser = DataProvider.getInstance().getOwnUser();
                Location ownLocation = ownUser.getLastLocation();

                if(ownLocation != null) {
                    LatLng ownLatLng = new LatLng(ownLocation.getLatitude(), ownLocation.getLongitude());

                    googleMap.addMarker(new MarkerOptions().position(ownLatLng).title("You").snippet("and snippet")
                            .icon(BitmapDescriptorFactory.fromResource( R.drawable.pegman )));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ownLatLng,
                            10));


                }

                handler.postDelayed(runnable, 2000);
            }
        });
    }

}
