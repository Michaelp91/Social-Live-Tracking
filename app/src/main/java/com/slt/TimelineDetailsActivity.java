package com.slt;

import android.app.Activity;
import android.app.Dialog;
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

//TODO: Test Timeline Details
public class TimelineDetailsActivity extends AppCompatActivity {
    private GoogleMap googleMap;
    MapView mMapView;
    private float zoomLevel = 17.0f;
    private LocationEntry entryStart = null;

    private LinkedList<LocationEntry> locationEntries;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private LinkedList<Bitmap> bmps;
    private TimelineSegment choosedTimelineSegment;
    private ImageView iv_pic1, iv_pic2;

    /**
     * Progress Bar
     */
    private ProgressBar mProgressBar;
    private Activity context;
    private TextView tv_usercomments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timelinedetails);

        choosedTimelineSegment = SharedResources.getInstance().getOnClickedTimelineSegmentForDetails();
        entryStart = SharedResources.getInstance().getEntryStart();
        mMapView = (MapView) findViewById(R.id.mapView);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
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
                mProgressBar.setVisibility(View.VISIBLE);
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
                        comments += comment;
                        choosedTimelineSegment.addStrUserComment(comment);


                        final String finalComments = comments;

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
               // googleMap.getUiSettings().setZoomControlsEnabled(true);
                LocationEntry last = null;
                LocationEntry first = entryStart;
                if (locationEntries.size() >= 2) {
                    first = locationEntries.getFirst();
                    last = locationEntries.getLast();




                    for (LocationEntry entry : locationEntries) {
                            LatLng start = new LatLng(entry.getLatitude(), entry.getLongitude());
                            LatLng end = new LatLng(last.getLatitude(), last.getLongitude());


                            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(end));
                            addLines(start, end);
                        last = entry;
                    }

                    /*
                    LatLng start = new LatLng(first.getLatitude(), first.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(start).icon( BitmapDescriptorFactory.fromResource( R.mipmap.ic_start ) )
                            .title( "START" ));

                    LatLng end = new LatLng(last.getLatitude(), last.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(end).icon(BitmapDescriptorFactory.fromResource( R.mipmap.ic_finish ))
                            .title("FINISH"));
                            */


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
        TextView tvPlace = (TextView) findViewById(R.id.tv_place);
        TextView tvAddress = (TextView) findViewById(R.id.tv_address);
        TextView tvSteps = (TextView) findViewById(R.id.tv_steps);
        TextView tvStepsTitle = (TextView) findViewById(R.id.tv_steps_title);

        String duration = (choosedTimelineSegment.getActiveTime()/ (60*1000)) + "min / " +  (choosedTimelineSegment.getDuration()/ (60*1000)) + "min. ";

        tvDauer.setText(duration);
        tvPlace.setText(choosedTimelineSegment.getPlace());
        tvAddress.setText(choosedTimelineSegment.getAddress());

        String activity = "";
        if (choosedTimelineSegment.getMyActivity() != null) {
            switch (choosedTimelineSegment.getMyActivity().getType()) {
                case DetectedActivity.RUNNING:
                    activity = "Running";
                    tvSteps.setText(choosedTimelineSegment.getUserSteps() + " Steps");
                    break;

                case DetectedActivity.IN_VEHICLE:
                    activity = "In Vehicle";
                    tvSteps.setVisibility(View.GONE);
                    tvStepsTitle.setVisibility(View.GONE);
                    break;
                case DetectedActivity.ON_BICYCLE:
                    activity = "On Bicycle";
                    break;
                case DetectedActivity.ON_FOOT:
                    activity = "On Foot";
                    tvSteps.setText(choosedTimelineSegment.getUserSteps() + " Steps");
                    break;

                case DetectedActivity.STILL:
                    activity = "Still";
                    tvSteps.setVisibility(View.GONE);
                    tvStepsTitle.setVisibility(View.GONE);
                    break;
                case DetectedActivity.TILTING:
                    activity = "Tilting";
                    tvSteps.setVisibility(View.GONE);
                    tvStepsTitle.setVisibility(View.GONE);
                    break;
                case DetectedActivity.UNKNOWN:
                    activity = "Unknown";
                    tvSteps.setVisibility(View.GONE);
                    tvStepsTitle.setVisibility(View.GONE);
                    break;


                case DetectedActivity.WALKING:
                    activity = "Walking";
                    tvSteps.setText(choosedTimelineSegment.getUserSteps() + " Steps");
                    break;

                default:
                    activity = "Undefined";
                    tvSteps.setVisibility(View.GONE);
                    tvStepsTitle.setVisibility(View.GONE);
                    break;
            }
        }

        tvAktivitaet.setText(activity);

        String dist = String.format( "%.2f", choosedTimelineSegment.getActiveDistance()) + "m";


        tvStrecke.setText( dist);

        mProgressBar.setVisibility(View.VISIBLE);
        AddUserComments(choosedTimelineSegment.getStrUserComments());
        new Thread(new Runnable() {
            @Override
            public void run() {
                DownloadImages();
            }
        }).start();
    }

    private void AddUserComments(LinkedList<String> strUserComments) {


        String comments = (strUserComments.size() > 0)? "": "No Comments Available";

        for(String u: strUserComments) {
            comments += u +  "\n";
            boolean debug = true;
        }

        tv_usercomments.setText(comments);

    }


    private void DownloadImages() {

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
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

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

    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private Bitmap resizeImage(byte[] input, int width, int height) {
        Bitmap original = BitmapFactory.decodeByteArray(input , 0, input.length);
        Bitmap resized = Bitmap.createScaledBitmap(original, width, height, true);

        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.JPEG, 100, blob);

        return resized;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public Drawable resizeImage(int imageResource) {// R.drawable.icon
        // Get device dimensions
        Display display = getWindowManager().getDefaultDisplay();
        double deviceWidth = display.getWidth();

        BitmapDrawable bd = (BitmapDrawable) this.getResources().getDrawable(
                imageResource);
        double imageHeight = bd.getBitmap().getHeight();
        double imageWidth = bd.getBitmap().getWidth();

        double ratio = deviceWidth / imageWidth;
        int newImageHeight = (int) (imageHeight * ratio);

        Bitmap bMap = BitmapFactory.decodeResource(getResources(), imageResource);
        Drawable drawable = new BitmapDrawable(this.getResources(),
                getResizedBitmap2(bMap, newImageHeight, (int) deviceWidth));

        return drawable;
    }

    /************************ Resize Bitmap *********************************/
    public Bitmap getResizedBitmap2(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }

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

    private void uploadImage(final Bitmap bitmap) {
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
                        mProgressBar.setVisibility(View.GONE);
                    }
                });

            }
        }).start();
    }

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
                mProgressBar.setVisibility(View.VISIBLE);
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

                uploadImage(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

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

/*
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    */
}
