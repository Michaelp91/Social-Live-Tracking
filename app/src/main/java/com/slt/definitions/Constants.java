package com.slt.definitions;

/**
 * Constants to use in the classes
 */
public class Constants {
    /**
     * Action notifications for the application
     */
    public interface ACTION {
        public static String MAIN_ACTION = "com.iptk.timeline.action.main";
        public static String START_DATA_PROVIDER_ACTION = "com.iptk.timeline.action.startforeground";
        public static String STOP_DATA_PROVIDER_ACTION = "com.iptk.timeline.action.stopforeground";
    }

    /**
     * Intents used to communicate changes in the application
     */
    public interface INTENT{
        public static String ADDRESS_RESOLVED = "com.iptk.timeline.interfaces.address.resolved";
        public static String ADDRESS_MESSAGE = "com.iptk.timeline.interfaces.address.message";
        public static String PLACE_RESOLVED = "com.iptk.timeline.interfaces.place.resolved";
        public static String PLACE_MESSAGE = "com.iptk.timeline.interfaces.place.message";
    }

    /**
     * Notification IDs for the foreground service
     */
    public interface NOTIFICATION_ID {
        public static int DATA_PROVIDER_SERVICE = 101;
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
