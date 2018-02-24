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
import com.slt.R;
import com.slt.control.DataProvider;
import com.slt.data.Timeline;
import com.slt.data.TimelineDay;
import com.slt.data.TimelineSegment;
import com.slt.statistics.Sport;

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
import java.util.stream.Collectors;

/**
 * Created by matze on 02.01.18.
 */

public class TestDataGenerator_toBeRemoved {

    public static Sport sportTypeOfLineData = Sport.NONE;
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

        for (int i = 0; i < 4; i++) {
            entries.add(new PieEntry((float) ((Math.random() * 70) + 30), "Quarter " + (i + 1)));
        }

        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData cd = new PieData(d);
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    public static LineData generateDataLine(Context context, int timePeriod, Sport sportType) {

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


    public static BarData getBarData(Context context, int timePeriod, Sport sportType) {

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
        yVals1 = prepareXYPairs(timePeriod);

        BarDataSet xyValuesSet;

        xyValuesSet = new BarDataSet(yVals1, "STEPS " +
                TestDataGenerator_toBeRemoved.dayXyPairsListSize + ", " +
                 ",\n steps = " +
                tls.getUserSteps() + ", duration = "
                + tls.getDuration()
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

    public static int getSpeed(int timePeriod, Sport sport, int x, int y) {
        // todo
        return 10;
    }

    public static int getDistance(int timePeriod, Sport sport, int x, int y) {
        // todo
        return 10;
    }

    public static int getDurance(int timePeriod, Sport sport, int x, int y) {
        // todo
        return 10;
    }

    public static int getTime(int timePeriod, Sport sport, int x, int y) {
        // todo
        return 10;
    }

    public static int getDate(int timePeriod, Sport sport, int x, int y) {
        // todo
        return 10;
    }


    public static ArrayList<BarEntry> prepareXYPairs(int timePeriod) {

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

                xyPairs = getXYPairsForDay(xAxisMaxSize, timeline.getTimelineDay(timeline.getHistorySize() - 1));
                break;
            case 1:
                // calculate dates of the timelineDay of the current week
                dates = timeline.getDatesOfThisWeek(currDate);
                // get timelinedays from the db
                days = timeline.getDaysOfWeekOrMonth(currDate, 0);

                xyPairs = getXYPairsForWeek( dates, days);
                break;
            case 2:
                days = timeline.getDaysOfWeekOrMonth(currDate, 1);

                // init calender
                java.util.Date date = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);

                // get the number of days in the current month
                xAxisMaxSize = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

                xyPairs = getXYPairsForMonth(xAxisMaxSize, days);
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


            xyReturnValues.add(new BarEntry(key, 20// todo val
            ));
        }

        return xyReturnValues;
    }

    public static boolean done = false;
    public static int dayXyPairsListSize = -1;
    public static TimelineSegment tls = null;

    private static LinkedHashMap<Integer, Integer> getXYPairsForDay(int xAxisMaxSize, TimelineDay timelineDay) {

        TimelineSegment segment;
        LinkedList<TimelineSegment> segmentList = timelineDay.getMySegments();
        Date startTime;
        Calendar calendar = Calendar.getInstance();
        int segmentStartingHour;
        HashMap<Integer, Integer> xyReturnPairs_unsorted = new HashMap<>();
        ArrayList<Integer> addadHours = new ArrayList<>();

        // --- todo remove
        dayXyPairsListSize = segmentList.size();
        if(dayXyPairsListSize >= 0)
            tls = segmentList.get(0);
        // --- end todo remove

        for (int i = 0; i < segmentList.size(); i++) {
            segment = segmentList.get(i);

            startTime = segment.getStartTime();
            calendar.setTime(startTime);

            segmentStartingHour = calendar.get(Calendar.HOUR_OF_DAY);

            // todo dif of user activities
            xyReturnPairs_unsorted.put(segmentStartingHour, segment.getUserSteps());

            addadHours.add(segmentStartingHour);
        }

        for (int hour = 0; hour < xAxisMaxSize; hour++) {
            if( ! addadHours.contains(hour) ) {
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



    //public static int str = 0;



    private static LinkedHashMap<Integer, Integer> getXYPairsForMonth(
            int xAxisMaxSize, LinkedList<TimelineDay> days
    ) {
        HashMap<Integer, Integer> xyPairsForAndMonth = new HashMap<>();
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        int key, val;
        TimelineDay timelineDay;
        ArrayList<Integer> addedDays = new ArrayList<>();

        for (int i = 0; i < days.size(); i++) {
            timelineDay = days.get(i);
            cal.setTime(date);
            key = cal.get(Calendar.DAY_OF_MONTH);
            // todo diff of user activities
            val = timelineDay.getSteps();

            xyPairsForAndMonth.put(key, val);
            addedDays.add(key);
        }

        for (int i = 0; i < xAxisMaxSize; i++) {
            if( ! addedDays.contains(i+1) ) {
                xyPairsForAndMonth.put(i+1, 20); // todo replace 20 by 0, 20 is just for testing
            }
        }

        // sort by keys befor return
        return sortHashMapByKeys(xyPairsForAndMonth);
    }

    private static LinkedHashMap<Integer, Integer> getXYPairsForWeek(
             LinkedList<Date> dates, LinkedList<TimelineDay> days) {

        TimelineDay timelineDay;
        Date date = null;
        HashMap<Integer, Integer> xyPairsForAndWeek = new HashMap<>();
        int key, val;
        boolean found = false;
        Calendar cal = Calendar.getInstance();

        for (int j = 0; j < dates.size(); j++) {
            date = dates.get(j);
            found = false;

            // find timelineDay with the date
            for (int i = 0; i < days.size(); i++) {
                timelineDay = days.get(i);

                if (timelineDay.isSameDay(date)) {
                    found = true;
                    // todo switch-case with other then steps
                    cal.setTime(date);
                    key = cal.get(Calendar.DAY_OF_MONTH);
                    // todo diff of user activities
                    val = timelineDay.getSteps();
                    xyPairsForAndWeek.put(key, val);

                    break;
                }

            }

            if( ! found ) {
                // add date with val = 0
                cal.setTime(date);
                key = cal.get(Calendar.DAY_OF_MONTH);
                val = 0;
                xyPairsForAndWeek.put(key, val);
            }
        }

        // sort by keys befor return
        return sortHashMapByKeys(xyPairsForAndWeek);
    }


}

