package com.slt.statistics.data;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.location.DetectedActivity;
import com.slt.control.DataProvider;
import com.slt.data.Timeline;
import com.slt.data.TimelineDay;
import com.slt.data.TimelineSegment;
import com.slt.definitions.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * class responsible for preparing data for the statistic's gui
 * by getting them from DataProvider-class and puting them into data object
 * ready to be shown
 *
 * Created by matze on 02.01.18.
 */

public class StatisticsDataModelProvider {


    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    public static PieData generateDataPie() {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        ////////////////
        Timeline timeline = DataProvider.getInstance().getUserTimeline();

        int distanceWalking = (int) timeline.getActiveDistanceForMonth(Constants.TIMELINEACTIVITY.WALKING);
        int distanceRunning = (int) timeline.getActiveDistanceForMonth(Constants.TIMELINEACTIVITY.RUNNING);
        int distanceBiking = (int) timeline.getActiveDistanceForMonth(Constants.TIMELINEACTIVITY.ON_BICYCLE);

        ////////////////

        entries.add(new PieEntry(distanceWalking, "Walking"));
        entries.add(new PieEntry(distanceRunning, "Running"));
        entries.add(new PieEntry(distanceBiking, "Biking"));

        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData cd = new PieData(d);


        return cd;
    }


    /**
     * method prepares data for the bar chart for given time period and sport type
     *
     * @param context
     * @param timePeriod - time period considered in the bar chart
     * @param sportType - sport type considered in the bar chart
     * @return data for the bar chart of the given time period and
     * sport type
     */
    public static BarData getBarData(Context context, int timePeriod, int sportType) {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        Typeface mTfLight =  android.graphics.Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf");

        // prepare y-axis values
        yVals1 = prepareXYPairs(timePeriod, sportType);

        BarDataSet xyValuesSet;

        xyValuesSet = new BarDataSet(yVals1, "DISTANCE "
        );

        xyValuesSet.setValueTextColor(Color.WHITE);

        xyValuesSet.setDrawIcons(false);

        xyValuesSet.setColors(ColorTemplate.MATERIAL_COLORS);
        ////////////////////
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(xyValuesSet);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setValueTypeface(mTfLight);
        data.setBarWidth(0.9f);

        return data;
    }

    /**
     * method provides speed for entry of the bar clicked in the bar chart
     *
     * @param e - entry in the chart that has been clicked
     * @param timePeriod - time period of the bar chart
     * @param sport - sport of the chart diagram
     *
     * @return speed for the clicked bar
     */
    public static double getSpeed(Entry e, int timePeriod, int sport) {

        // calculate speed
        switch(timePeriod) {
            case 0:
                return getSpeedForHour(e, sport);
            case 1:
                return getSpeedForDayOfWeek(e, sport);
            case 2:
                return getSpeedForDayOfMonth(e, sport);
            default:
                System.err.println("No such time period");
        }

        return 0;
    }

    /**
     * method provides speed for a day of the month
     *
     * @param e entry in the chart that has been clicked
     * @param sport - sport of the chart diagram
     *
     * @return speed for the day with index x
     */
    private static double getSpeedForDayOfMonth(Entry e, int sport) {
        int x = (int) e.getX();

        TimelineDay day = getTimelineDayFromWholeTimelineWithDayNumber(x);

        if(day != null)
            return day.getAvarageSpeedForThisDay(sport);
        else
            return 0;
    }

    private static TimelineDay getTimelineDayFromWholeTimelineWithDayNumber(int x) {
        Timeline timeline = DataProvider.getInstance().getUserTimeline();

        Calendar c = Calendar.getInstance();   // this takes current date
        c.setTime(new Date());
        c.set(Calendar.DAY_OF_MONTH, x);

        Date beginingOfTheMonthDate = c.getTime();

        TimelineDay day = timeline.getTimelineDay(beginingOfTheMonthDate);

        return day;
    }

    /**
     * method calculates the date of the day of the month with the given index
     * @param dayNr - index of the day of the month
     * @return date of the day of the month with the given index
     */
    private static Date getDateOfMonthWithDayNr(int dayNr) {
        int dayIndex = dayNr;
        Calendar c = Calendar.getInstance();   // this takes current date
        c.set(Calendar.DAY_OF_MONTH, 1);
        Calendar currCal = Calendar.getInstance();
        currCal.setTime(new Date());


        c.set(Calendar.DAY_OF_MONTH, dayIndex);
        c.set(Calendar.MONTH, currCal.get(Calendar.MONTH));
        c.set(Calendar.YEAR, currCal.get(Calendar.YEAR));

        Date selectedDate = c.getTime();

        return selectedDate;
    }

    /**
     * method provides speed for a day of the week
     *
     * @param e entry in the chart that has been clicked
     * @param sport - sport of the chart diagram
     *
     * @return speed for the day with index x
     */
    private static double getSpeedForDayOfWeek(Entry e, int sport) {

        int dayIndex = (int) e.getX();

        Timeline timeline = DataProvider.getInstance().getUserTimeline();

        LinkedList<Date> datesWeek = timeline.getDatesOfThisWeek(new Date());

        Date beginingOfTheWeekDate = datesWeek.get(0);

        LinkedList<TimelineDay> daysWeek = timeline.getDaysOfWeekOrMonth(beginingOfTheWeekDate, 0);

        TimelineDay day = getTimelineDayFromWholeTimelineWithDayNumber(dayIndex);

        if( day != null  && daysWeek.contains(day) )
            return day.getAvarageSpeedForThisDay(sport);
        else
            return 0;
    }


    /**
     * method provides distance of the clicked bar
     *
     * @param e - entry of the bar that has been clicked
     * @return distance of the clicked bar
     */
    public static int getDistance(Entry e) {
        return (int) e.getY();
    }

    /**
     * method provides duration of the sport's activity of the bar clicked
     * in the chart
     *
     * @param e - entry of the bar that has been clicked
     * @param timePeriod - time period of the chart diagram
     * @param sport - sport type of the chart
     * @return duration of the sport's activity associated with the clicked bar
     */
    public static long getDuration(Entry e, int timePeriod, int sport) {

        // sum up all active times of the segments for given entry
        switch(timePeriod) {
            case 0:
                return getDurationForHour(e, sport);
            case 1:
                return getDuranceForDayOfWeek(e, sport);
            case 2:
                return getDuranceForDayOfMonth(e, sport);
            default:
                System.err.println("No such time period");
        }

        return 0;
    }

    /**
     * method provides duration of the sport's activity of the bar clicked
     * in the chart. The time period considered by the method is a month.
     *
     * @param e - entry of the bar that has been clicked
     * @param sport - sport type of the chart
     * @return duration of the sport's activity associated with the clicked bar
     */
    private static long getDuranceForDayOfMonth(Entry e, int sport) {

        int x = (int) e.getX();

        TimelineDay day = getTimelineDayFromWholeTimelineWithDayNumber(x);

        if(day != null)
            return day.getDuration(sport);
        else
            return 0;
    }

    /**
     * method provides duration of the sport's activity of the bar clicked
     * in the chart. The time period considered by the method is a week.
     *
     * @param e - entry of the bar that has been clicked
     * @param sport - sport type of the chart
     * @return duration of the sport's activity associated with the clicked bar
     */
    private static long getDuranceForDayOfWeek(Entry e, int sport) {

        Timeline timeline = DataProvider.getInstance().getUserTimeline();

        LinkedList<Date> datesWeek = timeline.getDatesOfThisWeek(new Date());

        Date beginingOfTheWeekDate = datesWeek.get(0);

        LinkedList<TimelineDay> daysWeek = timeline.getDaysOfWeekOrMonth(beginingOfTheWeekDate, 0);

        int dayIndex = (int) e.getX();

        TimelineDay day = getTimelineDayFromWholeTimelineWithDayNumber(dayIndex);

        if( day != null  && daysWeek.contains(day) )
            return day.getDuration(sport);
        else
            return 0;
    }

    /**
     * method provides duration of the sport's activity of the bar clicked
     * in the chart. The time period considered by the method are 24h of day.
     *
     * @param e - entry of the bar that has been clicked
     * @param sport - sport type of the chart
     * @return duration of the sport's activity associated with the clicked bar
     */
    private static long getDurationForHour(Entry e, int sport) {

        int x = (int) e.getX();

        Timeline timeline = DataProvider.getInstance().getUserTimeline();

        TimelineDay curr = timeline.getTimelineDays().getLast();

        Date date = new Date();

        if( ! curr.isSameDay(date))
            return 0;

        int hour = x;

        long durance = curr.getDurationForHour(hour, sport);

        return durance;
    }


    /**
     * return the date of the bar clicked in the chart
     *
     * @param timePeriod - time period considered in the chart
     * @return date of the bar clicked in the chart
     */
    public static Date getDate(Entry e, int timePeriod) {

        int x = (int) e.getX();

        // get date of the x entry
        switch(timePeriod) {
            case 0:
                return new Date();
            case 1:
                return getDateOfWeekWithDayNr(x);
            case 2:
                return getDateOfMonthWithDayNr(x);
            default:
                System.err.println("No such time period");
        }

        return null;
    }

    /**
     * method calculates the date of the day of the week with the given index
     * @param x - index of the day of the week
     * @return date of the day of the week with the given index
     */
    private static Date getDateOfWeekWithDayNr(int x) {
        Timeline timeline = DataProvider.getInstance().getUserTimeline();

        LinkedList<Date> datesWeek = timeline.getDatesOfThisWeek(new Date());

        return datesWeek.get(x);
    }

    /**
     * method provides speed for an hour of the day
     *
     * @param e entry in the chart that has been clicked
     * @param sport - sport of the chart diagram
     *
     * @return speed for the day with index x
     */
    public static double getSpeedForHour(Entry e, int sport) {

        int x = (int) e.getX();

        Timeline timeline = DataProvider.getInstance().getUserTimeline();

        TimelineDay currDay = timeline.getTimelineDays().getLast();

        Date date = new Date();

        if( ! currDay.isSameDay(date))
            return 0;

        int hour = x;

        double speedHour = currDay.getAvarageSpeedForThis_Hour(hour, sport);

        return speedHour;
    }

    /**
     * method prepares pairs of x/y for the bar chart of the given time period and
     * sport type
     * @param timePeriod - time period considered in the bar chart
     * @param sportType - sport type considered in the bar chart
     * @return pairs of x/y for the bar chart of the given time period and
     * sport type
     */
    public static ArrayList<BarEntry> prepareXYPairs(int timePeriod, int sportType) {

        ArrayList<BarEntry> xyReturnValues = new ArrayList<BarEntry>();
        int xAxisMaxSize = 0;
        Timeline timeline = DataProvider.getInstance().getUserTimeline();
        Date currDate = new Date();
        LinkedList<Date> dates = new LinkedList<>();
        int val,key;


        // prepare y-axis values
        LinkedList<TimelineDay> days = new LinkedList<>();
        HashMap<Integer, Integer> xyPairs = new HashMap<>();


        switch (timePeriod) {
            case 0:

                // size of x-axis for 24hours
                xAxisMaxSize = 24;

                xyPairs = getXYPairsForDay(timeline.getTimelineDay(timeline.getHistorySize() - 1), sportType);
                break;
            case 1:
                // calculate dates of the timelineDay of the current week
                dates = timeline.getDatesOfThisWeek(currDate);
                // get timelinedays from the db
                days = timeline.getDaysOfWeekOrMonth(currDate, 0);

                xyPairs = getXYPairsForWeek( dates, days, sportType);
                break;
            case 2:
                days = timeline.getDaysOfWeekOrMonth(currDate, 1);

                // init calender
                java.util.Date date = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);

                // get the number of days in the current month
                xAxisMaxSize = cal.getActualMaximum(Calendar.DAY_OF_MONTH);


                xyPairs = getXYPairsForMonth(xAxisMaxSize, days, sportType);
                break;
            default:
                System.err.println("No such time period.");
        }


        Iterator it = xyPairs.entrySet().iterator();

        // add x- and y-values to the return-set
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();

            key = (int) pair.getKey();
            val = (int) pair.getValue();


            xyReturnValues.add(new BarEntry(key, val
            ));
        }

        return xyReturnValues;
    }


    /**
     * method prepares pairs of x/y for the bar chart of the given day and
     * sport type
     * @param timelineDay - day considered in the bar chart
     * @param sportType - sport type considered in the bar chart
     * @return pairs of x/y for the bar chart of the given day and
     * sport type
     */
    private static LinkedHashMap<Integer, Integer> getXYPairsForDay(TimelineDay timelineDay, int sportType) {

        TimelineSegment segment;
        LinkedList<TimelineSegment> segmentList = timelineDay.filterDaySegmentsForActivity(sportType);
        Date startTime;
        Calendar calendar = Calendar.getInstance();
        int segmentStartingHour;
        HashMap<Integer, Integer> xyReturnPairs_unsorted = new HashMap<>();
        ArrayList<Integer> addedHours = new ArrayList<>();
        int sum = 0;
        int distance = 0;
        DetectedActivity detectedActivity = new DetectedActivity(sportType, 100);

        for (int i = 0; i < segmentList.size(); i++) {
            segment = segmentList.get(i);


            startTime = segment.getStartTime();
            calendar.setTime(startTime);

            segmentStartingHour = calendar.get(Calendar.HOUR_OF_DAY);

            distance =  (int) timelineDay.getActiveDistance(detectedActivity);

            if(xyReturnPairs_unsorted.containsKey(segmentStartingHour)) {

                sum = xyReturnPairs_unsorted.get(segmentStartingHour) + distance;
                // add steps from new segment to the old sum
                xyReturnPairs_unsorted.put(segmentStartingHour, sum);
            } else
                xyReturnPairs_unsorted.put(segmentStartingHour, distance);

            if( ! addedHours.contains(segmentStartingHour))
                addedHours.add(segmentStartingHour);
        }

        // for 24 h
        for (int hour = 0; hour < 24; hour++) {
            if( ! addedHours.contains(hour) ) {
                xyReturnPairs_unsorted.put(hour, 0);
            }
        }

        // sort by keys befor return
        return sortHashMapByKeys(xyReturnPairs_unsorted);
    }




    /**
     * sorts the given hashmap by keys
     * @param unsortedHashMap
     * @return hashMap sorted by keys
     */
    private static LinkedHashMap<Integer, Integer> sortHashMapByKeys(HashMap<Integer, Integer> unsortedHashMap) {
        TreeMap<Integer, Integer> sorted = new TreeMap<>(unsortedHashMap);
        Set<Map.Entry<Integer, Integer>> mappings = sorted.entrySet();

        LinkedHashMap<Integer, Integer> sortedReturn = new LinkedHashMap<>();

        for(Map.Entry<Integer, Integer> sortedEntry : mappings)
            sortedReturn.put(sortedEntry.getKey(), sortedEntry.getValue());

        return sortedReturn;
    }

    /**
     * method prepares pairs of x/y for the bar chart of the month and
     * sport type
     *
     * @param xAxisMaxSize - amount of days inn the month
     * @param days - days of the month
     * @param sportType - sport type considered in the bar chart
     * @return pairs of x/y for the bar chart of the given month and
     * sport type
     */
    private static LinkedHashMap<Integer, Integer> getXYPairsForMonth(
            int xAxisMaxSize, LinkedList<TimelineDay> days,
            int sportType) {
        HashMap<Integer, Integer> xyPairsForMonth = new HashMap<>();
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        int key, val;
        TimelineDay timelineDay;
        ArrayList<Integer> addedDays = new ArrayList<>();
        DetectedActivity detectedActivity = new DetectedActivity(sportType, 100);

        for (int i = 0; i < days.size(); i++) {
            timelineDay = days.get(i);
            date = timelineDay.getMyDate();
            cal.setTime(date);
            key = cal.get(Calendar.DAY_OF_MONTH);

            val = (int) timelineDay.getActiveDistance(detectedActivity);

            xyPairsForMonth.put(key, val);

            if( ! addedDays.contains(key))
                addedDays.add(key);
        }

        for (int i = 0; i < xAxisMaxSize; i++) {
            if( ! addedDays.contains(i+1) ) {
                xyPairsForMonth.put(i+1, 0);
            }
        }

        // sort by keys befor return
        return sortHashMapByKeys(xyPairsForMonth);
    }


    /**
     * method prepares pairs of x/y for the bar chart of the week and
     * sport type
     * @param dates - dates of the week
     * @param days - days of the week
     * @param sportType - sport type considered in the bar chart
     * @return pairs of x/y for the bar chart of the given week and
     * sport type
     */
    private static LinkedHashMap<Integer, Integer> getXYPairsForWeek(
            LinkedList<Date> dates, LinkedList<TimelineDay> days, int sportType) {

        TimelineDay timelineDay;
        Date date = null;
        HashMap<Integer, Integer> xyPairsForAndWeek = new HashMap<>();
        int key, val;
        boolean found = false;
        DetectedActivity detectedActivity = new DetectedActivity(sportType, 100);


        for (int j = 0; j < dates.size(); j++) {
            date = dates.get(j);
            found = false;

            // find timelineDay with the date
            for (int i = 0; i < days.size(); i++) {
                timelineDay = days.get(i);

                if (timelineDay.isSameDay(date)) {
                    found = true;

                    //cal.setTime(date);
                    key = xyPairsForAndWeek.size();

                    val = (int) timelineDay.getActiveDistance(detectedActivity);

                    xyPairsForAndWeek.put(key, val);

                    break;
                }

            }

            if( ! found ) {
                // add date with val = 0
                key = xyPairsForAndWeek.size();
                val = 0;
                xyPairsForAndWeek.put(key, val);
            }
        }

        // sort by keys befor return
        return sortHashMapByKeys(xyPairsForAndWeek);
    }


}

