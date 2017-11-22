package com.slt.definitions;

public class Constants {
    public interface ACTION {
        public static String MAIN_ACTION = "com.iptk.timeline.action.main";
        public static String START_DATA_PROVIDER_ACTION = "com.iptk.timeline.action.startforeground";
        public static String STOP_DATA_PROVIDER_ACTION = "com.iptk.timeline.action.stopforeground";
    }

    public interface INTENT{
        public static String ADDRESS_RESOLVED = "com.iptk.timeline.interfaces.address.resolved";
        public static String ADDRESS_MESSAGE = "com.iptk.timeline.interfaces.address.message";
        public static String PLACE_RESOLVED = "com.iptk.timeline.interfaces.place.resolved";
        public static String PLACE_MESSAGE = "com.iptk.timeline.interfaces.place.message";
    }

    public interface NOTIFICATION_ID {
        public static int DATA_PROVIDER_SERVICE = 101;
    }

    public interface LOCATION {
        public static int ACCURRACY = 100;
        public static int MIN_UPDATE_INTERVAL = 3000;
        public static int UPDATE_INTERVAL = 5000;
    }
}
