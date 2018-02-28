package com.slt.statistics.data;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.location.DetectedActivity;
import com.slt.R;
import com.slt.control.DataProvider;
import com.slt.data.Timeline;
import com.slt.data.TimelineDay;
import com.slt.data.TimelineSegment;
import com.slt.definitions.Constants;
import com.slt.statistics.adapter.TimeperiodIndividualSportTabFragmentAdapter;

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
 * Created by matze on 02.01.18.
 */

public class TestDataGenerator_toBeRemoved {

    public static int sportTypeOfLineData = Constants.TIMELINEACTIVITY.UNKNOWN;
    public static LineData dayLineData = null;
    public static LineData weekLineData = null;
    public static LineData monthLineData = null;
    private static BarData dayBarData;
    private static BarData monthBarData;
    private static BarData weekBarData;


    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    public static PieData generateDataPie(int cnt) {

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
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    public static LineData generateDataLine(Context context, int timePeriod, int sportType) {

        ArrayList<Entry> e1 = new ArrayList<Entry>();
        int xAxisMaxSize = -1;

        // if sporttype has not changed, check if the data has already been calculated
        if (sportTypeOfLineData == sportType) {

            if (timePeriod == 0 && dayLineData != null)
                return dayLineData;
            else if (timePeriod == 1 && weekLineData != null)
                return weekLineData;
            else if (timePeriod == 2 && monthLineData != null)
                return monthLineData;

        }
        /*Date current = new Date();
                Timeline timeline = DataProvider.getInstance().getUserTimeline();
                LinkedList<TimelineDay> daysOfMonth = timeline.getDaysOfWeekOrMonth(current, 1);

         */

        // set the size of the xAxis depending on the time period
        switch (timePeriod) {
            case 0:
                xAxisMaxSize = 24;
                break;
            case 1:
                xAxisMaxSize = 7;
                break;
            case 2:

                // init calender
                java.util.Date date = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);

                // get the number of days in the current month
                xAxisMaxSize = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

                break;
            default:
                System.err.print("No such time period.");
        }

        for (int i = 0; i < xAxisMaxSize; i++) {
            e1.add(new Entry(i + 1, (int) (Math.random() * 65) + 40));
        }

        LineDataSet d1 = new LineDataSet(e1, "New DataSet " + timePeriod + ", (1)");
        d1.setLineWidth(2.5f);

        d1.setColor(Color.rgb(0, 153, 204));
        d1.setDrawCircles(false);
        d1.setDrawValues(false);
        d1.setDrawFilled(true);

        // gradient color under the linechart
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.fade_red);
        d1.setFillDrawable(drawable);

        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        sets.add(d1);

        LineData cd = new LineData(sets);

        switch (timePeriod) {
            case 0:
                dayLineData = cd;
                break;
            case 1:
                weekLineData = cd;
                break;
            case 2:
                monthLineData = cd;
                break;
            default:
                System.err.print("No such time period.");
        }
        return cd;
    }


    public static BarData getBarData(Context context, int timePeriod, int sportType) {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        int xAxisMaxSize = -1;
        Typeface mTfLight =  android.graphics.Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf");


        // if sporttype has not changed, check if the data has already been calculated
        if (sportTypeOfLineData == sportType) {

            if (timePeriod == 0 && dayBarData != null)
                return dayBarData;
            else if (timePeriod == 1 && weekBarData != null)
                return weekBarData;
            else if (timePeriod == 2 && monthBarData != null)
                return monthBarData;
        }

       /* int start = 1;
        int count = 10;
        int range =50;

        for (int i = (int) start; i < start + count + 1; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult);


            yVals1.add(new BarEntry(i, val+10));

        }*/

        // prepare y-axis values
        yVals1 = prepareXYPairs(timePeriod, sportType);

        BarDataSet xyValuesSet;

        /*int steps = -1;

        if(timePeriod != 0) {
    Calendar cal = Calendar.getInstance();
    Date date = new Date();
    TimelineDay d;
    int dayOfMonth;
    Date date1;
    for (int i = 0; i < daysGLOBAL.size(); i++) {
        d = daysGLOBAL.get(i);

        cal.setTime(d.getMyDate());

        dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        if (dayOfMonth == 23) {

            steps = d.getSteps();
        }

    }

}*/
        xyValuesSet = new BarDataSet(yVals1, "DISTANCE "
        );

        xyValuesSet.setDrawIcons(false);

        xyValuesSet.setColors(ColorTemplate.MATERIAL_COLORS);
        // xyValuesSet.setBarBorderColor(Color.BLUE);
        ////////////////////
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(xyValuesSet);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        //data.setDrawValues(true);
        data.setValueTypeface(mTfLight);
        data.setBarWidth(0.9f);

        return data;
    }

    public static double getSpeed(Entry e, int timePeriod, int sport, int x, int y) {

        // calculate speed
        switch(timePeriod) {
            case 0:
                return getSpeedForHour(e, sport, x, y);
            case 1:
                return getSpeedForDayOfWeek(e, sport, x, y);
            case 2:
                return getSpeedForDayOfMonth(e, sport, x, y);
            default:
                System.err.println("No such time period");
        }

        return 0;
    }

    private static double getSpeedForDayOfMonth(Entry e, int sport, int x, int y) {


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

    public static Date getDateOfMonthWithDayNr(int dayNr) {
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

    private static double getSpeedForDayOfWeek(Entry e, int sport, int x, int y) {
        Timeline timeline = DataProvider.getInstance().getUserTimeline();

        LinkedList<Date> datesWeek = timeline.getDatesOfThisWeek(new Date());

        Date beginingOfTheWeekDate = datesWeek.get(0);

        LinkedList<TimelineDay> daysWeek = timeline.getDaysOfWeekOrMonth(beginingOfTheWeekDate, 0);

        int dayIndex = x;

        TimelineDay day = getTimelineDayFromWholeTimelineWithDayNumber(dayIndex);

        if( day != null  && daysWeek.contains(day) )
            return day.getAvarageSpeedForThisDay(sport);
        else
            return 0;
    }


    public static int getDistance(int timePeriod, int sport, int x, int y) {
        return y;
    }

    public static long getDuration(Entry e, int timePeriod, int sport, int x, int y) {
        // sum up all active times of the segments for given entry
        switch(timePeriod) {
            case 0:
                return getDurationForHour(e, sport, x, y);
            case 1:
                return getDuranceForDayOfWeek(e, sport, x, y);
            case 2:
                return getDuranceForDayOfMonth(e, sport, x, y);
            default:
                System.err.println("No such time period");
        }

        return 0;
    }

    private static long getDuranceForDayOfMonth(Entry e, int sport, int x, int y) {
        TimelineDay day = getTimelineDayFromWholeTimelineWithDayNumber(x);

       /* if(day != null) {
            TimelineSegment se;
            for (int i = 0; i < day.getMySegments().size(); i++) {
                se = day.getMySegments().get(i);

                if(se.getUserSteps() == 500)
                    TimeperiodIndividualSportTabFragmentAdapter.DURANCE =
                            String.valueOf(se.getID());

            }


        }*/

        if(day != null)
            return day.getDuration(sport);
        else
            return 0;
    }

    private static long getDuranceForDayOfWeek(Entry e, int sport, int x, int y) {
        Timeline timeline = DataProvider.getInstance().getUserTimeline();

        LinkedList<Date> datesWeek = timeline.getDatesOfThisWeek(new Date());

        Date beginingOfTheWeekDate = datesWeek.get(0);

        LinkedList<TimelineDay> daysWeek = timeline.getDaysOfWeekOrMonth(beginingOfTheWeekDate, 0);

        int dayIndex = x;

        TimelineDay day = getTimelineDayFromWholeTimelineWithDayNumber(dayIndex);

        if( day != null  && daysWeek.contains(day) )
            return day.getDuration(sport);
        else
            return 0;
    }

    private static long getDurationForHour(Entry e, int sport, int x, int y) {

        Timeline timeline = DataProvider.getInstance().getUserTimeline();

        TimelineDay curr = timeline.getTimelineDays().getLast();

        Date date = new Date();

        if( ! curr.isSameDay(date))
            return 0;

        int hour = x;

        long durance = curr.getDurationForHour(hour, sport);

        return durance;
    }





    public static Date getDate(int timePeriod, int sport, int x, int y) {
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

    public static Date getDateOfWeekWithDayNr(int x) {
        Timeline timeline = DataProvider.getInstance().getUserTimeline();

        LinkedList<Date> datesWeek = timeline.getDatesOfThisWeek(new Date());

        return datesWeek.get(x);
    }

    public static double getSpeedForHour(Entry e, int sport, int x, int y) {

        Timeline timeline = DataProvider.getInstance().getUserTimeline();

        TimelineDay currDay = timeline.getTimelineDays().getLast();

        Date date = new Date();

        if( ! currDay.isSameDay(date))
            return 0;

        int hour = x;

        double speedHour = currDay.getAvarageSpeedForThis_Hour(hour, sport);

        return speedHour;
    }

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

                xyPairs = getXYPairsForDay(xAxisMaxSize, timeline.getTimelineDay(timeline.getHistorySize() - 1), sportType);
                break;
            case 1:
                // calculate dates of the timelineDay of the current week
                dates = timeline.getDatesOfThisWeek(currDate);
                // get timelinedays from the db
                days = timeline.getDaysOfWeekOrMonth(currDate, 0);

                daysGLOBAL = days;
                xyPairs = getXYPairsForWeek( dates, days, sportType);
                break;
            case 2:
                days = timeline.getDaysOfWeekOrMonth(currDate, 1);

                daysGLOBAL = days;
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

    public static LinkedList<TimelineDay> daysGLOBAL = new LinkedList<>();

    private static LinkedHashMap<Integer, Integer> getXYPairsForDay(int xAxisMaxSize, TimelineDay timelineDay, int sportType) {

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

        for (int hour = 0; hour < xAxisMaxSize; hour++) {
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



    private static LinkedHashMap<Integer, Integer> getXYPairsForWeek(
            LinkedList<Date> dates, LinkedList<TimelineDay> days, int sportType) {

        TimelineDay timelineDay;
        Date date = null;
        HashMap<Integer, Integer> xyPairsForAndWeek = new HashMap<>();
        int key, val;
        boolean found = false;
        Calendar cal = Calendar.getInstance();
        ArrayList<Integer> addedDays = new ArrayList<>();
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
                //cal.setTime(date);
                key = xyPairsForAndWeek.size();
                val = 0;
                xyPairsForAndWeek.put(key, val);
            }
        }



        // sort by keys befor return
        return sortHashMapByKeys(xyPairsForAndWeek);
    }


}

