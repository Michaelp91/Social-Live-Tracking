package com.slt.control;

import android.annotation.TargetApi;
import android.app.Notification;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.slt.R;
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
     * Stores the instance of the API Client
     */
    private GoogleApiClient myGoogleApiClient;

    /**
     * The notification for the gps and activity services
     */
    private Notification foregroundNotification;

    /**
     * Get the instance of the shared resources
     * @return The instance of the shared resources
     */
    public static SharedResources getInstance() {
        return ourInstance;
    }

    /**
     * Get the instance of the API Client
     * @return The stored instance of the Google API Client
     */
    public GoogleApiClient getMyGoogleApiClient() {
        return myGoogleApiClient;
    }

    /**
     * Set the instance of the Google API Client
     * @param myGoogleApiClient The instance that should be stored
     */
    public void setMyGoogleApiClient(GoogleApiClient myGoogleApiClient) {
        this.myGoogleApiClient = myGoogleApiClient;
    }

    /**
     * Get the foreground notification
     * @return The foreground notification
     * */
    @TargetApi(Build.VERSION_CODES.O)
    public Notification getForegroundNotification() {

        if(foregroundNotification == null) {
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
                int importance = NotificationManager.IMPORTANCE_HIGH;
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

                mNotificationManager.notify(Constants.NOTIFICATION_ID.DATA_PROVIDER_SERVICE, foregroundNotification );

            }
        }

        return foregroundNotification;
    }



    /**
     * Private constructor
     */
    private SharedResources() {
        myGoogleApiClient = null;
    }
}
