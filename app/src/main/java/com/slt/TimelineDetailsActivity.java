package com.slt;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.slt.control.ApplicationController;
import com.slt.control.DataProvider;
import com.slt.control.SharedResources;
import com.slt.data.LocationEntry;
import com.slt.data.TimelineSegment;
import com.slt.data.User;
import com.slt.restapi.OtherRestCalls;
import com.slt.restapi.UsefulMethods;
import com.slt.restapi.data.Constants;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Show the Details of the specific Timeline Segment
 */
public class TimelineDetailsActivity extends AppCompatActivity {

    /**
     * googleMap Object for viewing the Google Map
     */
    private GoogleMap googleMap;

    /**
     * mMapView
     */
    MapView mMapView;

    /**
     * zoomLevel for GoogleMap
     */
    private float zoomLevel = 17.0f;

    /**
     * location entries of the choosed timeline segment
     */
    private LinkedList<LocationEntry> locationEntries;

    /**
     * Constants for checking the mode of inserting an Image(Camera Mode or Gallery Mode)
     */
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;

    /**
     * Bitmaps of all viewed Images
     */
    private LinkedList<Bitmap> bmps;

    /**
     * choosed timeline segment
     */
    private TimelineSegment choosedTimelineSegment;

    /**
     * Imageviews for showing the first two pictures
     */
    private ImageView iv_pic1, iv_pic2;

    /**
     * Activity context
     */
    private Activity context;

    /**
     * Text View for showing the user comments
     */
    private TextView tv_usercomments;


    /**
     * progress dialog
     */
    private ProgressDialog progressDialog;

    /**
     * Constant
     */
    private final String NO_COMMENTS_AVAILABLE = "No Comments Available";

    /**
     * overwritten onCreate Method
     * @param savedInstanceState the saved Instance State
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timelinedetails);



        choosedTimelineSegment = SharedResources.getInstance().getOnClickedTimelineSegmentForDetails();
        mMapView = (MapView) findViewById(R.id.mapView);
        iv_pic1 = (ImageView) findViewById(R.id.iv_pic1);
        iv_pic2 = (ImageView) findViewById(R.id.iv_pic2);
        ImageView iv_editActivity = (ImageView) findViewById(R.id.iv_editActivity);
        tv_usercomments = (TextView) findViewById(R.id.tv_usercomments);
        context = this;

        Button addNewPicture = (Button) findViewById(R.id.btn_addPicture);
        Button viewMorePictures = (Button) findViewById(R.id.btn_viewPicture);
        Button addComments = (Button) findViewById(R.id.btn_addComment);

        mMapView.onCreate(savedInstanceState);

        addNewPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        iv_editActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog commentDialog = new Dialog(context);
                commentDialog.requestWindowFeature((int) Window.FEATURE_NO_TITLE);
                commentDialog.setContentView(R.layout.popup_activity);

                final Spinner spinner = (Spinner) commentDialog.findViewById(R.id.sp_activity);
                Button btnOk = (Button) commentDialog.findViewById(R.id.btn_ok);

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String IN_VEHICLE = "In Vehicle";
                        final String On_Bicycle = "On Bicycle";
                        final String On_Foot = "On Foot";
                        final String Walking = "Walking";
                        final String Running = "Running";

                        String strActivity = spinner.getSelectedItem().toString();
                        int intActivity = choosedTimelineSegment.getMyActivity().getType();

                        switch (strActivity) {
                            case IN_VEHICLE:
                                intActivity = 0;
                                break;
                            case On_Bicycle:
                                intActivity = 1;
                                break;

                            case On_Foot:
                                intActivity = 2;
                                break;

                            case Walking:
                                intActivity = 7;
                                break;

                            case Running:
                                intActivity = 8;
                                break;
                        }




                        User user = DataProvider.getInstance().getOwnUser();
                        DetectedActivity detectedActivity = new DetectedActivity(intActivity, 100);
                        choosedTimelineSegment.setMyActivity(detectedActivity);


                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                OtherRestCalls.updateTimelineSegmentForActivity(choosedTimelineSegment);

                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        commentDialog.hide();
                                        Toast.makeText(context, "The Activity is Changed Successfully", Toast.LENGTH_SHORT).show();
                                        context.onBackPressed();
                                    }
                                });
                            }
                        }).start();

                    }
                });

                commentDialog.show();
            }
        });

        addComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog commentDialog = new Dialog(context);
                commentDialog.requestWindowFeature((int) Window.FEATURE_NO_TITLE);
                commentDialog.setContentView(R.layout.popup_usercomment);

                final EditText editText = (EditText) commentDialog.findViewById(R.id.et_titel);
                Button btnOk = (Button) commentDialog.findViewById(R.id.btn_ok);

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        User user = DataProvider.getInstance().getOwnUser();
                        String email = user.getEmail();
                        String comment = "- " + email + " : " + editText.getText().toString() + "\n";
                        String comments = tv_usercomments.getText().toString();
                        comments = (comments.equals(NO_COMMENTS_AVAILABLE))?"": comments;
                        comments += comment;
                        choosedTimelineSegment.addStrUserComment(comment);


                        final String finalComments = comments;

                        tv_usercomments.setText("");
                        tv_usercomments.setText(finalComments);


                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                OtherRestCalls.updateTimelineSegmentForComments(choosedTimelineSegment);

                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "Adding Comment successfully", Toast.LENGTH_SHORT).show();
                                        commentDialog.hide();
                                    }
                                });
                            }
                        }).start();

                    }
                });

                commentDialog.show();
            }
        });

        viewMorePictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedResources.getInstance().setSegmentBitmaps(bmps);
                Intent i = new Intent(context, ImageGallery.class);
                startActivity(i);
            }
        });

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        locationEntries = choosedTimelineSegment.getLocationPoints();

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                LocationEntry last = null;

                if (locationEntries.size() >= 2) {
                    LocationEntry first = locationEntries.getFirst();

                    for (LocationEntry entry : locationEntries) {
                        if(last != null){

                            LatLng start = new LatLng(entry.getLatitude(), entry.getLongitude());
                            LatLng end = new LatLng(last.getLatitude(), last.getLongitude());


                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(end));
                            addLines(start, end);
                        } else {
                            LatLng start = new LatLng(entry.getLatitude(), entry.getLongitude());
                            googleMap.addMarker(new MarkerOptions().position(start).title(""));
                        }
                        last = entry;
                    }

                    LatLng start = new LatLng(first.getLatitude(), first.getLongitude());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(start));
                    Marker m = googleMap.addMarker(new MarkerOptions().position(start).icon( BitmapDescriptorFactory.fromResource( R.mipmap.ic_start ) ).title("START"));
                    m.showInfoWindow();

                    LatLng end = new LatLng(last.getLatitude(), last.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(end).icon(BitmapDescriptorFactory.fromResource( R.mipmap.ic_finish ))
                            .title("FINISH"));
                    ZoomCamera(start, end);


                } else {
                    LatLng start = new LatLng(locationEntries.get(0).getLatitude(), locationEntries.get(0).getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(start).icon( BitmapDescriptorFactory.fromResource( R.mipmap.ic_start ) ).title(""));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, zoomLevel));
                    Toast.makeText(getApplicationContext(), "Only one or no Locationpoints available!", Toast.LENGTH_LONG).show();
                }
            }
        });



        TextView tvAktivitaet = (TextView) findViewById(R.id.tv_aktivitaet);
        TextView tvDauer = (TextView) findViewById(R.id.tv_dauer);
        TextView tvStrecke = (TextView) findViewById(R.id.tv_strecke);

        String duration = Long.toString(choosedTimelineSegment.getDuration());

        tvDauer.setText(duration);

        String activity = "";
        if (choosedTimelineSegment.getMyActivity() != null) {
            switch (choosedTimelineSegment.getMyActivity().getType()) {
                case DetectedActivity.RUNNING:
                    activity = "Running";
                    break;

                case DetectedActivity.IN_VEHICLE:
                    activity = "In Vehicle";
                    break;
                case DetectedActivity.ON_BICYCLE:
                    activity = "On Bicycle";
                    break;
                case DetectedActivity.ON_FOOT:
                    activity = "On Foot";
                    break;

                case DetectedActivity.STILL:
                    activity = "Still";
                    break;
                case DetectedActivity.TILTING:
                    activity = "Tilting";
                    break;
                case DetectedActivity.UNKNOWN:
                    activity = "Unknown";
                    break;


                case DetectedActivity.WALKING:
                    activity = "Walking";
                    break;

                default:
                    activity = "Undefined";
                    break;
            }
        }

        tvAktivitaet.setText(activity);

        String dist = String.format( "%.2f", choosedTimelineSegment.getActiveDistance()) + "m";


        tvStrecke.setText( dist);

        AddUserComments(choosedTimelineSegment.getStrUserComments());
        new Thread(new Runnable() {
            @Override
            public void run() {
                DownloadImages();
            }
        }).start();
    }

    /**
     * Method for viewing the User Comments
     * @param strUserComments all comments to be viewed
     */
    private void AddUserComments(LinkedList<String> strUserComments) {


        String comments = (strUserComments.size() > 0)? "": "No Comments Available";

        for(String u: strUserComments) {
            comments += u +  "\n";
            boolean debug = true;
        }

        tv_usercomments.setText(comments);

    }


    /**
     * Downloads all the images from the Server
     */
    private void DownloadImages() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = ProgressDialog.show(context, "Bitte warten Sie...", "", true);
            }
        });

        bmps = (bmps == null)? new LinkedList<Bitmap>():bmps;
        ArrayList<Integer> numbersInUse = new ArrayList<>();
        for(String image: choosedTimelineSegment.getMyImages()) {
            Bitmap bmp = UsefulMethods.LoadImage(image);

            if(bmp != null) {

                bmps = (bmps == null)? new LinkedList<Bitmap>():bmps;
                bmps.add(bmp);
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ShowImages();
            }
        });
    }

    /**
     * Show the first two Images
     */
    private void ShowImages() {
        for(Bitmap bmp: bmps) {
            if(iv_pic1.getVisibility() == View.GONE) {
                iv_pic1.setImageBitmap(bmp);
                iv_pic1.setVisibility(View.VISIBLE);
            } else if (iv_pic2.getVisibility() == View.GONE) {
                iv_pic2.setImageBitmap(bmp);
                iv_pic2.setVisibility(View.VISIBLE);
            }
        }

        progressDialog.hide();
    }

    /**
     * resize Image for lower resolution
     * @param input image data as byte array
     * @param width required width of the image
     * @param height required height of the image
     * @return the resized bitmap
     */
    private Bitmap resizeImage(byte[] input, int width, int height) {
        Bitmap original = BitmapFactory.decodeByteArray(input , 0, input.length);
        Bitmap resized = Bitmap.createScaledBitmap(original, width, height, true);

        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.JPEG, 100, blob);

        return resized;
    }

    /**
     * choose the image selecting mode
     */
    private void selectImage() {
        try {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(ApplicationController.getContext(), "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(ApplicationController.getContext(), "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * upload the image to the server
     * @param bitmap bitmap to be uploaded
     */
    private void uploadImage(final Bitmap bitmap) {
        progressDialog = ProgressDialog.show(context, "Bitte warten Sie...", "", true);
        Toast.makeText(this, "Image is uploaded successfully.", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Integer imageId = bmps.size() + 1;
                String title = choosedTimelineSegment.getID() + "_" + imageId.toString();

                final boolean uploaded = UsefulMethods.UploadImageView(bitmap, title + ".png");
                choosedTimelineSegment.addImage(title);
                final boolean timelinesegmentUpdated = OtherRestCalls.updateTimelineSegmentForImages(choosedTimelineSegment);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.hide();
                    }
                });

            }
        }).start();
    }

    /**
     * Overwritten onActivityResult Method
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                //compress the picture -> reduces the quality to 90%
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                byte[] BYTE = bytes.toByteArray();
                bitmap = resizeImage(BYTE, 100, 100);

                //this.bitmap = BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);

                //this.bitmap = rotateImageIfRequired(this.bitmap, ApplicationController.getContext(), selectedImage);

                bmps.add(bitmap);
                ShowImages();

                uploadImage(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(ApplicationController.getContext().getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                //compress the picture -> reduces the quality to 90%
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                byte[] BYTE = bytes.toByteArray();
                bitmap = resizeImage(BYTE, 100, 100);

                //this.bitmap = BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);

                //this.bitmap = rotateImageIfRequired(this.bitmap, ApplicationController.getContext(), selectedImage);
                bmps = (bmps == null)? new LinkedList<Bitmap>(): bmps;

                bmps.add(bitmap);
                ShowImages();
                uploadImage(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method for zooming in, Google Map
     * @param start start Location
     * @param end end Location
     */
    private void ZoomCamera(final LatLng start, final LatLng end) {

        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {

            @Override
            public void onMapLoaded() {
                if(end != null) {
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(start);
                    builder.include(end);
                    LatLngBounds bounds = builder.build();

                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
                    googleMap.animateCamera(cu, new GoogleMap.CancelableCallback(){
                        public void onCancel(){}
                        public void onFinish(){
                            CameraUpdate zout = CameraUpdateFactory.zoomBy((float) -3.0);
                            googleMap.animateCamera(zout);
                        }
                    });
                }
            }
        });
    }

    /**
     * Add lines in the Map
     * @param start start Location
     * @param end end Location
     */
    private void addLines(LatLng start, LatLng end) {
        googleMap.addPolyline((new PolylineOptions())
                .add(start, end).width(5).color(Color.GRAY)
                .geodesic(true));
        // move camera to zoom on map
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start,
                10));


        //Src: https://stackoverflow.com/questions/34357660/calculating-the-distance-between-two-markers-in-android
        Location markerLocation = new Location("");
        markerLocation.setLatitude(start.latitude);
        markerLocation.setLongitude(start.longitude);

        Location distanceLocation = new Location("");
        distanceLocation.setLatitude(end.latitude);
        distanceLocation.setLongitude(end.longitude);


    }
}
