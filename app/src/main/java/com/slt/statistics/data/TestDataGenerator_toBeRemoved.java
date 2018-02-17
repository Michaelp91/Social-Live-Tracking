package com.slt.statistics.data;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.slt.R;
import com.slt.control.AchievementCalculator;
import com.slt.control.DataProvider;
import com.slt.data.Timeline;
import com.slt.data.TimelineDay;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;

/**
 * Created by matze on 02.01.18.
 */

public class TestDataGenerator_toBeRemoved {

    public static LineData dayLineData = null;
    public static LineData weekLineData = null;
    public static LineData monthLineData = null;


    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    public static PieData generateDataPie(int cnt) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        for (int i = 0; i < 4; i++) {
            entries.add(new PieEntry((float) ((Math.random() * 70) + 30), "Quarter " + (i+1)));
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
    public static LineData generateDataLine(Context context, int timePeriod, String sportType) {

        ArrayList<Entry> e1 = new ArrayList<Entry>();
        int xAxisMaxSize = -1;

        if(timePeriod == 0 && dayLineData != null)
            return dayLineData;
        else if(timePeriod == 1 && weekLineData != null)
            return weekLineData;
        else if(timePeriod == 2 && monthLineData != null)
            return monthLineData;

        /*Date current = new Date();
                Timeline timeline = DataProvider.getInstance().getUserTimeline();
                LinkedList<TimelineDay> daysOfMonth = timeline.getDaysOfWeekOrMonth(current, 1);

         */

        // set the size of the xAxis depending on the time period
        switch(timePeriod) {
            case 0:
                xAxisMaxSize = 24;
                break;
            case 1:
                xAxisMaxSize = 7;
                break;
            case 2:

                // init calender
                java.util.Date date= new Date();
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
}
