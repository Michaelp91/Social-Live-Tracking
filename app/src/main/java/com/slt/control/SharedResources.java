package com.slt.control;

import android.app.Notification;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.google.android.gms.common.api.GoogleApiClient;
import com.slt.R;

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
     */
    public Notification getForegroundNotification() {

        if(foregroundNotification == null) {
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
