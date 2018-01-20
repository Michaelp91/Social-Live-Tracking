package com.slt.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.slt.control.DataProvider;
import com.slt.data.User;
import com.slt.restapi.OtherRestCalls;
import com.slt.restapi.UsefulMethods;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Usman Ahmad on 20.01.2018.
 */

public class FragmentLiveMap extends Fragment {
    private GoogleMap googleMap;
    MapView mMapView;

    private static final LatLng DARMSTADT_NORD = new LatLng(50.0042304, 9.0658932);
    private static final LatLng WILLYBRANDTPLATZ = new LatLng(49.9806625, 9.1355554);
    private ArrayList<User> list_friends = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_livemap, container, false);

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
                LatLng ownLatLng = new LatLng(ownLocation.getLatitude(), ownLocation.getLongitude());

                googleMap.addMarker(new MarkerOptions().position(ownLatLng).title("You").snippet("and snippet")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ownLatLng,
                        10));

                ShowFriends();
            }
        });

        return view;

    }

    private void ShowFriends() {
        final ArrayList<Marker> markers = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                list_friends = OtherRestCalls.retrieveFriends();
                for(final User u: list_friends) {
                    Location location = u.getLastLocation();
                    final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    final Bitmap bmp = UsefulMethods.LoadImage(u);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Marker marker = null;

                            if(bmp != null)
                              marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(u.getEmail()).icon(BitmapDescriptorFactory.fromBitmap(bmp)));
                            else
                                marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(u.getEmail()));

                            markers.add(marker);
                            boolean debug = true;
                        }
                    });

                }
            }
        }).start();


    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Friends and Co.");
    }

}
