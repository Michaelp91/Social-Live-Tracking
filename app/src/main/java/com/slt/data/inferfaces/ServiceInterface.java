package com.slt.data.inferfaces;

import android.location.Location;

import com.google.android.gms.location.DetectedActivity;

import java.util.Date;

/**
 * Created by Thorsten on 11.11.2017.
 */

public interface ServiceInterface {
    public int updateActivity(DetectedActivity activity, Date timestamp);
    public int updatePosition(Location location, Date timestamp);
}
