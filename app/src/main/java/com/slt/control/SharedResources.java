package com.slt.control;

import android.annotation.TargetApi;
import android.app.Notification;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.ImageView;
import android.widget.RemoteViews;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.DetectedActivity;
import com.slt.R;
import com.slt.data.LocationEntry;
import com.slt.data.TimelineDay;
import com.slt.data.TimelineSegment;
import com.slt.data.User;
import com.slt.definitions.Constants;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Locale;

import static com.slt.R.mipmap.ic_launcher;

/**
 * Stores the instance of the Google API so every activity can acess it as needed
 */
public class SharedResources {
    /*
    * Tag for the Logger
    */
    private static final String TAG = "SharedResources";

    /**
     * Stores the instance of the shared resources
     */
    private static final SharedResources ourInstance = new SharedResources();

    /**
     * Used to communicate timeline segments between fragments
     */
    private TimelineSegment onclickedTimelineSegment;

    /**
     * Bitmaps to show all the images from the activity
     */
    private LinkedList<Bitmap> segmentBitmaps;

    /**
     * Used to show the start Location on the map View
     */
    private LocationEntry entryStart;

    /**
     * Stores the instance of the API Client
     */
    private GoogleApiClient myGoogleApiClient;

    /**
     * The notification for the gps and activity services
     */
    private Notification foregroundNotification;

    /**
     * Username in the nav bar for updates
     */
    private TextView navUsername;

    /**
     * Profile in the nav bar for updates
     */
    private ImageView navProfilePhoto;
    /**
     * User to pass along between segments
     */
    private User myUser;
    /**
     * Our notification Manager
     */
    private NotificationManager mNotificationManager;
    /**
     * Notification Compat Builder
     */
    private NotificationCompat.Builder cBuilder = null;
    /**
     * Normal notification Builder
     */
    private Notification.Builder builder = null;

    /**
     * Private constructor
     */
    private SharedResources() {
        myGoogleApiClient = null;
        foregroundNotification = null;
        myUser = null;

    }

    /**
     * Get the instance of the shared resources
     *
     * @return The instance of the shared resources
     */
    public static SharedResources getInstance() {
        return ourInstance;
    }

    /**
     * Get the nav bar TextView for the username
     *
     * @return The TextView of the username
     */
    public TextView getNavUsername() {
        return navUsername;
    }

    /**
     * Set the TextView of the nav bar for the username
     *
     * @param navUsername
     */
    public void setNavUsername(TextView navUsername) {
        this.navUsername = navUsername;
    }

    /**
     * Get the ImageView for the Profile Photo in the nav bar
     *
     * @return
     */
    public ImageView getNavProfilePhoto() {
        return this.navProfilePhoto;
    }

    /**
     * Set the Image View for the Profile Photo in the nav bar
     *
     * @param navProfilePhoto The Image view that should be stored
     */
    public void setNavProfilePhoto(ImageView navProfilePhoto) {
        this.navProfilePhoto = navProfilePhoto;
    }

    /**
     * Get the instance of the API Client
     *
     * @return The stored instance of the Google API Client
     */
    public GoogleApiClient getMyGoogleApiClient() {
        return myGoogleApiClient;
    }

    /**
     * Set the instance of the Google API Client
     *
     * @param myGoogleApiClient The instance that should be stored
     */
    public void setMyGoogleApiClient(GoogleApiClient myGoogleApiClient) {
        this.myGoogleApiClient = myGoogleApiClient;
    }

    /**
     *
     * @return entryStart
     */
    public LocationEntry getEntryStart() {
        return entryStart;
    }

    /**
     *
     * @param entryStart set this attribute
     */
    public void setEntryStart(LocationEntry entryStart) {
        this.entryStart = entryStart;
    }

    /**
     * Get the stored timeline segment
     *
     * @return The stored timeline segment
     */
    public TimelineSegment getOnClickedTimelineSegmentForDetails() {
        return onclickedTimelineSegment;
    }

    /**
     * Store a timeline segment that should be shared between segments
     *
     * @param onclickedTimelineSegment The Timeline segment to store
     */
    public void setOnClickedTimelineSegmentForDetails(TimelineSegment onclickedTimelineSegment) {
        this.onclickedTimelineSegment = onclickedTimelineSegment;
    }

    public LinkedList<Bitmap> getSegmentBitmaps() {
        return segmentBitmaps;
    }

    public void setSegmentBitmaps(LinkedList<Bitmap> segmentBitmaps) {
        this.segmentBitmaps = segmentBitmaps;
    }

    /**
     * Get the foreground notification
     *
     * @return The foreground notification
     */
    @TargetApi(Build.VERSION_CODES.O)
    public Notification getForegroundNotification() {

        //if we do not have a notification yet
        if (foregroundNotification == null) {
            //if old android version used, switch to older notification system
            if (Build.VERSION.SDK_INT < 26) {
                mNotificationManager =
                        (NotificationManager) ApplicationController.getContext()
                                .getSystemService(ApplicationController.getContext().NOTIFICATION_SERVICE);

                RemoteViews notificationView = new RemoteViews(ApplicationController.getContext().getPackageName(), R.layout.foreground);
                Bitmap icon = BitmapFactory.decodeResource(ApplicationController.getContext().getResources(), ic_launcher);

                //create Buider and set properties
                cBuilder = new NotificationCompat.Builder(ApplicationController.getContext(), Constants.NOTIFICATION_ID.id);

                foregroundNotification = cBuilder.setContentTitle("Timeline")
                        .setTicker("Location & Activity Tracking Active")
                        .setContentText("Location & Activity Tracking")
                        .setSmallIcon(ic_launcher)
                        .setBadgeIconType(R.mipmap.ic_launcher)
                        .setVisibility(Notification.VISIBILITY_PUBLIC)
                        .setSmallIcon(ic_launcher)
                        .setAutoCancel(true)
                        .setNumber(0)
                        .setOngoing(true)
                        .build();

                //set notification to foreground
                mNotificationManager.notify(Constants.NOTIFICATION_ID.DATA_PROVIDER_SERVICE, foregroundNotification);

            } else {

                mNotificationManager =
                        (NotificationManager) ApplicationController.getContext()
                                .getSystemService(ApplicationController.getContext().NOTIFICATION_SERVICE);

                // The id of the channel.
                String id = Constants.NOTIFICATION_ID.id;

                // The user-visible name of the channel.
                CharSequence name = Constants.NOTIFICATION_ID.visibleID;

                // The user-visible description of the channel.
                String description = Constants.NOTIFICATION_ID.description;
                int importance = NotificationManager.IMPORTANCE_MIN;
                NotificationChannel mChannel = new NotificationChannel(id, name, importance);

                // Configure the notification channel.
                mChannel.setDescription(description);
                mChannel.enableLights(true);
                mNotificationManager.createNotificationChannel(mChannel);

                //create notification and set properties
                builder = new Notification.Builder(ApplicationController.getContext().getApplicationContext(), id);

                foregroundNotification = builder
                        .setContentTitle("Timeline")
                        .setContentText("Location & Activity Tracking")
                        .setSmallIcon(ic_launcher)
                        .setBadgeIconType(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setNumber(0)
                        .setOngoing(true)
                        .build();

                //set notification to foreground
                mNotificationManager.notify(Constants.NOTIFICATION_ID.DATA_PROVIDER_SERVICE, foregroundNotification);
            }
        }

        return foregroundNotification;
    }


    /**
     * Changes the Detected Activity to a custom string
     *
     * @param activity The activity to decode
     * @return The activity as a string
     */
    private String activityToString(DetectedActivity activity) {
        String ret = "Undefined";

        switch (activity.getType()) {
            case DetectedActivity.IN_VEHICLE:
                ret = "In Vehicle";
                break;
            case DetectedActivity.ON_BICYCLE:
                ret = "On Bicycle";
                break;
            case DetectedActivity.ON_FOOT:
                ret = "On Foot";
                break;
            case DetectedActivity.RUNNING:
                ret = "Running";
                break;
            case DetectedActivity.STILL:
                ret = "Still";
                break;
            case DetectedActivity.TILTING:
                ret = "Tilting";
                break;
            case DetectedActivity.UNKNOWN:
                ret = "Unknown";
                break;
            case DetectedActivity.WALKING:
                ret = "Walking";
                break;
        }
        return ret;
    }



    /* Updates the notification to show changes in the data
     *
     * @param location The GPS location
     * @param data     The data of the last segment
     * @param date     The date of the last change
     * @param activity The activity that was detected
     */
    void updateNotification(Location location, TimelineDay data, java.util.Date date, DetectedActivity activity) {

        //retrive data and build information strings
        String locationString = "Location: Unknown";
        String segmentDataHeader = "Activity: " + "None";
        String segmentData = "Start: " + "None";
        String segmentDataTwo = "End: " + "None";
        String activityData = "Detected Activity: None";

        String segment2DataHeader = "Dur: " + 0.0 + "s";
        String segment2Data = "Steps: " + 0;
        String segment2DataTwo = "Distance: " + 0 + "m";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        // check if a activity is set
        if (activity != null) {
            activityData = ", Act: " + this.activityToString(activity);
        }

        //check if a location is set
        if (location != null) {
            locationString = "Location: " + location.getLatitude() + "-Lat, " + location.getLongitude() + "-Long";
        }

        //check if we have segment data available
        if (data != null) {

            String sDate = "None";

            //check if last segment available
            if (data.getMySegments().getLast() != null) {
                if (data.getMySegments().getLast().getLocationPoints().getFirst() != null) {
                    sDate = simpleDateFormat.format(data.getMySegments().getLast().getLocationPoints().getFirst().getMyEntryDate());
                }

                String eDate = "None";
                if (data.getMySegments().getLast().getLocationPoints().getLast() != null) {
                    eDate = simpleDateFormat.format(data.getMySegments().getLast().getLocationPoints().getLast().getMyEntryDate());
                }

                String distance = String.format("%.2f",
                        (data.getMySegments().getLast().getActiveDistance()
                                + data.getMySegments().getLast().getInactiveDistance()));

                segmentDataHeader = "Activity: " + this.activityToString(data.getMySegments().getLast().getMyActivity());
                segmentData = "Start: " + sDate;
                segmentDataTwo = "End: " + eDate;
                segment2DataHeader = "Dur: " + Double.toString(data.getMySegments().getLast().getDuration() / 1000) + "s";
                segment2Data = "Steps: " + Integer.toString(data.getMySegments().getLast().getUserSteps());
                segment2DataTwo = " Distance: " + distance + "m";
            }


            String dateString = simpleDateFormat.format(date) + " - Current Activity:";


            //select which notification type we have depending on the android version
            if (cBuilder != null) {

                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

                //Sets a title for the Inbox in expanded layout
                inboxStyle.setBigContentTitle(dateString);

                //add aditional information
                inboxStyle.addLine(locationString);
                inboxStyle.addLine(segmentDataHeader);
                inboxStyle.addLine(segmentData);
                inboxStyle.addLine(segmentDataTwo);
                inboxStyle.addLine(segment2DataHeader);
                inboxStyle.addLine(segment2Data);
                inboxStyle.addLine(segment2DataTwo);

                cBuilder.setStyle(inboxStyle);

                foregroundNotification = cBuilder.build();
            }

            if (builder != null) {

                Notification.InboxStyle inboxStyle = new Notification.InboxStyle();

                //Sets a title for the Inbox in expanded layout
                inboxStyle.setBigContentTitle(dateString);

                //add aditional information
                inboxStyle.addLine(locationString);
                inboxStyle.addLine(segmentDataHeader);
                inboxStyle.addLine(segmentData);
                inboxStyle.addLine(segmentDataTwo);
                inboxStyle.addLine(segment2DataHeader);
                inboxStyle.addLine(segment2Data);
                inboxStyle.addLine(segment2DataTwo);

                builder.setStyle(inboxStyle);


                foregroundNotification = builder.build();
            }


            mNotificationManager.notify(Constants.NOTIFICATION_ID.DATA_PROVIDER_SERVICE, foregroundNotification);
        }
    }


    /**
     * Use to remove notification bar
     */
    @TargetApi(Build.VERSION_CODES.O)
    public void removeNotification() {
        if (foregroundNotification != null) {
            if (Build.VERSION.SDK_INT < 26) {
                //Deleted if services are disconnected

            } else {

                // Android >26 also exolicity delete bar
                NotificationManager mNotificationManager =
                        (NotificationManager) ApplicationController.getContext()
                                .getSystemService(ApplicationController.getContext().NOTIFICATION_SERVICE);

                mNotificationManager.deleteNotificationChannel(Constants.NOTIFICATION_ID.id);
                mNotificationManager.cancelAll();
            }

            foregroundNotification = null;
        }
    }

    /**
     * Can be used to pass along a User between fragments
     *
     * @param user The user to set
     */




    public synchronized void setUser(User user) {
        this.myUser = user;
    }

    /**
     * Returns the user to show further data fpr
     *
     * @return The user that was set, null if none was set
     */
    public synchronized User getSelectedUser() {
        return this.myUser;
    }
}
