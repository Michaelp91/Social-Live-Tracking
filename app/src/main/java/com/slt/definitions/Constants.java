package com.slt.definitions;

import com.slt.R;

/**
 * Constants to use in the classes
 */
public class Constants {
    /**
     * Action notifications for the application
     */

    public interface TIMELINEACTIVITY {
        public static int WALKING = 7;
        public static int RUNNING = 8;

    }

    public interface ACTION {
        public static String MAIN_ACTION = "com.slt.definitions.action.main";
        public static String START_DATA_PROVIDER_ACTION = "com.slt.definitions.action.startforeground";
        public static String STOP_DATA_PROVIDER_ACTION = "com.slt.definitions.action.stopforeground";
    }

    /**
     * Intents used to communicate changes in the application
     */
    public interface INTENT{
        public static String ADDRESS_RESOLVED = "com.slt.definitions.intent.address.resolved";
        public static String ADDRESS_MESSAGE = "com.slt.definitions.intent.address.message";
        public static String PLACE_RESOLVED = "com.slt.definitions.intent.place.resolved";
        public static String PLACE_MESSAGE = "com.slt.definitions.intent.place.message";
        //timeline intents
        public static String TIMELINE_INTENT_OWN_ACHIEVEMENT_UPDATE = "com.slt.definitions.intent.timeline.own.achievements";
        public static String TIMELINE_INTENT_OTHER_ACHIEVEMENT_UPDATE = "com.slt.definitions.intent.timeline.other.achievements";
        public static String TIMELINE_INTENT_DAY_OWN_INSERT = "com.slt.definitions.intent.timelineday.day.own.insert";
        public static String TIMELINE_INTENT_DAY_OTHER_INSERT = "com.slt.definitions.intent.timelineday.day.other.insert";
        //timelineday intents
        public static String TIMELINE_DAY_INTENT_OWN_ACHIEVEMENT_UPDATE = "com.slt.definitions.intent.timelineday.own.achievements";
        public static String TIMELINE_DAY_INTENT_OTHER_ACHIEVEMENT_UPDATE = "com.slt.definitions.intent.timelineday.other.achievements";
        public static String TIMELINE_DAY_INTENT_OWN_SEGMENTS_CHANGED = "com.slt.definitions.intent.timelineday.own.segment.changed";
        public static String TIMELINE_DAY_INTENT_OTHER_SEGMENTS_CHANGED = "com.slt.definitions.intent.timelineday.other.segment.changed";
        //timelinesegment intents
        public static String TIMELINE_SEGMENT_INTENT_OWN_ACHIEVEMENT_UPDATE = "com.slt.definitions.intent.timelinesegment.own.achievements";
        public static String TIMELINE_SEGMENT_INTENT_OTHER_ACHIEVEMENT_UPDATE = "com.slt.definitions.intent.timelinesegment.other.achievements";
        public static String TIMELINE_SEGMENT_INTENT_OWN_INFO_CHANGED = "com.slt.definitions.intent.timelinesegment.own.info.changed";
        public static String TIMELINE_SEGMENT_INTENT_OTHER_INFO_CHANGED = "com.slt.definitions.intent.timelinesegment.other.info.changed";
        public static String TIMELINE_SEGMENT_INTENT_OWN_LOCATIONPOINTS_CHANGED = "com.slt.definitions.intent.timelinesegment.own.locationpoints.changed";
        public static String TIMELINE_SEGMENT_INTENT_OTHER_LOCATIONPOINTS_CHANGED = "com.slt.definitions.intent.timelinesegment.other.locationpoints.changed";
        //dataprovider intents
        public static String DATA_PROVIDER_INTENT_FRIENDS_CHANGED = "com.slt.definitions.intent.dataprovider.friends.changed";
        //user intents
        public static String USER_INTENT_OTHER_DATA_CHANGED = "com.slt.definitions.intent.user.changed";
    }


    /**
     * Intents Extras used to communicate changes in the application
     */
    public interface INTENT_EXTRAS{
        public static String USERID = "com.slt.definitions.intentextras.userid";
        public static String ID = "com.slt.definitions.intentextras.id";
        public static String TIMELINE_DAY_DATE = "com.slt.definitions.intentextras.timelineday.date";
        public static String TIMELINE_SEGMENT_DATE = "com.slt.definitions.intentextras.timelinesegment.date";
    }


    /**
     * Notification IDs for the foreground service
     */
    public interface NOTIFICATION_ID {
        public static int DATA_PROVIDER_SERVICE = 101;
        public static  String id = "my_channel_101";
        public static  String visibleID = "GPS & Activity Channel";
        public static  String description = "GPS & Activity  Notifications";
    }

    /**
     * Settings for the location service
     */
    public interface LOCATION {
        public static int ACCURRACY = 100;
        public static int MIN_UPDATE_INTERVAL = 3000;
        public static int UPDATE_INTERVAL = 5000;
    }

    /**
     * Achievements for the application
     *
     * SPORT_ENDURANCE_SEGMENT = more than 60 minutes at a single activity
     * SPORT_DISTANCE_SEGMENT = more than 10 kilometers in a single activity
     * SPORT_STEPS_SEGMENT = more than 10000 steps in a single activity
     * SPORT_DAY_DURATION = more than 60 minutes of sport on a Day
     * SPORT_WEEK_STREAK = SPORT_DAY_DURATION achievement on all days of a week
     * SPORT_MONTH_STREAK = SPORT_DAY_DURATION on all days of a month
     */
    public interface ACHIEVEMENT {
        public static int  SPORT_ENDURANCE_SEGMENT = 0;
        public static int  SPORT_DISTANCE_SEGMENT = 1;
        public static int  SPORT_STEPS_SEGMENT = 2;
        public static int  SPORT_DAY_DURATION = 3;
        public static int  SPORT_DAY_DISTANCE = 4;
        public static int  SPORT_DAY_STEPS= 5;
        public static int  SPORT_WEEK_STREAK = 6;
        public static int  SPORT_MONTH_STREAK =7;
    };

    /**
     * todo include new drawable for diversity
     */
    public interface ACHIVEMENTS_DRAWABLE {
        public static int  SPORT_ENDURANCE_SEGMENT = R.drawable.running_cup;
        public static int  SPORT_DISTANCE_SEGMENT = R.drawable.running_cup;
        public static int  SPORT_STEPS_SEGMENT = R.drawable.running_cup;
        public static int  SPORT_DAY_DURATION = R.drawable.running_cup;
        public static int  SPORT_DAY_DISTANCE = R.drawable.running_cup;
        public static int  SPORT_DAY_STEPS= R.drawable.running_cup;
        public static int  SPORT_WEEK_STREAK = R.drawable.running_cup;
        public static int  SPORT_MONTH_STREAK = R.drawable.running_cup;
    }

    /**
     * Definitions for the achievements
     */
    public interface ACHIEVEMENT_DEFINITIONS {
        public static int  SPORT_ENDURANCE_SEGMENT_MINUTES = 3600;
        public static double SPORT_DISTANCE_SEGMENT_METER = 10000;
        public static int SPORT_STEPS_SEGMENT = 100000;
        public static int SPORT_DAY_DURATION_MINUTES = 3600;
        public static int SPORT_DAY_DISTANCE_METERS = 10000;
        public static int SPORT_DAY_STEPS_COUNT = 10000;
    };
}
