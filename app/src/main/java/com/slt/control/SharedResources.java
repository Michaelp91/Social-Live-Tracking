package com.slt.control;

import android.annotation.TargetApi;
import android.app.Notification;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.ImageView;
import android.widget.RemoteViews;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.slt.R;
import com.slt.data.TimelineSegment;
import com.slt.data.User;
import com.slt.definitions.Constants;

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
     * @param navProfilePhoto The Image view that should be stored
     */
    public void setNavProfilePhoto(ImageView navProfilePhoto) {
        this.navProfilePhoto = navProfilePhoto;
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
     * Get the instance of the API Client
     *
     * @return The stored instance of the Google API Client
     */
    public GoogleApiClient getMyGoogleApiClient() {
        return myGoogleApiClient;
    }

    /**
     * User to pass along between segments
     */
    private User myUser;

    /**
     * Set the instance of the Google API Client
     *
     * @param myGoogleApiClient The instance that should be stored
     */
    public void setMyGoogleApiClient(GoogleApiClient myGoogleApiClient) {
        this.myGoogleApiClient = myGoogleApiClient;
    }

    /**
     * Store a timeline segment that should be shared between segments
     * @param onclickedTimelineSegment The Timeline segment to store
     */
    public void setOnClickedTimelineSegmentForDetails(TimelineSegment onclickedTimelineSegment) {
        this.onclickedTimelineSegment = onclickedTimelineSegment;
    }

    /**
     * Get the stored timeline segment
     * @return The stored timeline segment
     */
    public TimelineSegment getOnClickedTimelineSegmentForDetails() {
        return onclickedTimelineSegment;
    }

    /**
     * Get the foreground notification
     *
     * @return The foreground notification
     */
    @TargetApi(Build.VERSION_CODES.O)
    public Notification getForegroundNotification() {

        if (foregroundNotification == null) {
            if (Build.VERSION.SDK_INT < 26) {
                RemoteViews notificationView = new RemoteViews(ApplicationController.getContext().getPackageName(), R.layout.foreground);
                Bitmap icon = BitmapFactory.decodeResource(ApplicationController.getContext().getResources(), ic_launcher);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(ApplicationController.getContext());
                builder.setContentTitle("Timeline");
                builder.setTicker("Location & Activity Tracking Active");
                builder.setContentText("Location & Activity Tracking");
                builder.setSmallIcon(ic_launcher);
                builder.setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false));
                builder.setContent(notificationView);
                builder.setOngoing(true);
                foregroundNotification = builder.build();
            } else {

                NotificationManager mNotificationManager =
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

                foregroundNotification = new Notification.Builder(ApplicationController.getContext().getApplicationContext(), id)
                        .setContentTitle("Timeline")
                        .setTicker("Location & Activity Tracking Active")
                        .setContentText("Location & Activity Tracking")
                        .setSmallIcon(ic_launcher)
                        .setBadgeIconType(R.mipmap.ic_launcher)
                        .setNumber(5)
                        .setSmallIcon(ic_launcher)
                        .setAutoCancel(true)
                        .setOngoing(true)
                        .build();

                mNotificationManager.notify(Constants.NOTIFICATION_ID.DATA_PROVIDER_SERVICE, foregroundNotification);

            }
        }

        return foregroundNotification;
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

    /**
     * Private constructor
     */
    private SharedResources() {
        myGoogleApiClient = null;
        foregroundNotification = null;
        myUser = null;

    }
}
