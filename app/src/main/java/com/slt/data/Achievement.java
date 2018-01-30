package com.slt.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.slt.R;
import com.slt.definitions.Constants;

import java.util.Calendar;
import java.util.Date;

/**
 * Class to store a achievement of the user
 */
public class Achievement {
    /*
    * Tag for the Logger
    */
    private static final String TAG = "Tupeln_AchievementImage_and_Info";

    /**
     * Stores our achievement as an Integer
     */
    private int achievement;

    /**
     * The Date the achievement was achieved
     */
    private Date timestamp;

    /**
     * Database ID
     */
    private String ID;

    /**
     * Constructor to initialize the Class
     * @param achievement The achievement that has to be stored
     * @param timestamp The Date the achievement was achieved
     */
    public Achievement(int achievement, Date timestamp) {
        this.achievement = achievement;
        this.timestamp = timestamp;
        this.ID = null;
    }

    /**
     * Returns the our achievment
     * @return The achievement
     */
    public int getAchievement() {
        return achievement;
    }


    /**
     * Returns the timestamp for the achievement
     * @return The Date of the achievement
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Retrieve the database ID
     * @return The database ID
     */
    public String getID() {
        return ID;
    }

    /**
     * Set the Datatbase ID
     * @param ID The new Database ID
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     * Compare if the date give is on the same day as the TimelineDay
     * @param date The date we want to use to compare
     * @return True if it is the same day, false if not
     */
    public boolean isSameDay(Date date){
        //check if we have a day to compare
        if(date == null){
            Log.i(TAG, "isSameDay: date is null");
            return false;
        }

        //truncate the date to the day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date truncatedDate = calendar.getTime();

        return timestamp.compareTo(truncatedDate) == 0;
    }

    /**
     * method returns the image that is appropriate for the particular type of achievement
     * @return drawable id of the image of the achievement
     */
    public int getDrawableOfAchievement() {
        int type = this.achievement;

        switch(type) {
            case Constants.ACHIEVEMENT.SPORT_ENDURANCE_SEGMENT:
                return Constants.ACHIVEMENTS_DRAWABLE.SPORT_ENDURANCE_SEGMENT;
            case Constants.ACHIEVEMENT.SPORT_DISTANCE_SEGMENT:
                return Constants.ACHIVEMENTS_DRAWABLE.SPORT_DISTANCE_SEGMENT;
            case Constants.ACHIEVEMENT.SPORT_STEPS_SEGMENT:
                return Constants.ACHIVEMENTS_DRAWABLE.SPORT_STEPS_SEGMENT;
            case Constants.ACHIEVEMENT.SPORT_DAY_DURATION:
                return Constants.ACHIVEMENTS_DRAWABLE.SPORT_DAY_DURATION;
            case Constants.ACHIEVEMENT.SPORT_DAY_DISTANCE:
                return Constants.ACHIVEMENTS_DRAWABLE.SPORT_DAY_DISTANCE;
            case Constants.ACHIEVEMENT.SPORT_DAY_STEPS:
                return Constants.ACHIVEMENTS_DRAWABLE.SPORT_DAY_STEPS;
            case Constants.ACHIEVEMENT.SPORT_WEEK_STREAK:
                return Constants.ACHIVEMENTS_DRAWABLE.SPORT_WEEK_STREAK;
            case Constants.ACHIEVEMENT.SPORT_MONTH_STREAK:
                return Constants.ACHIVEMENTS_DRAWABLE.SPORT_MONTH_STREAK;
            default:
                System.err.println("Unknown achievement type.");
                break;
        }

        return 0;
    }
}
