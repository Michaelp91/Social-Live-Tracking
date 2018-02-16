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
import com.slt.data.Timeline;
import com.slt.data.TimelineDay;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by matze on 02.01.18.
 */

public class TestDataGenerator_toBeRemoved {



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
        //Timeline timeline =
        //LinkedList<TimelineDay> week = this.getDaysOfWeekOrMonth(current, 0);



        switch(timePeriod) {
            case 0:
                xAxisMaxSize = 24;
                break;
            case 1:
                xAxisMaxSize = 7;
                break;
            case 2:
                xAxisMaxSize = 30;

                break;
            default:
                System.err.print("No such time period.");
        }

        for (int i = 0; i < xAxisMaxSize; i++) {
            e1.add(new Entry(i, (int) (Math.random() * 65) + 40));
        }

        LineDataSet d1 = new LineDataSet(e1, "New DataSet " + timePeriod + ", (1)");
        d1.setLineWidth(2.5f);

        d1.setColor(Color.rgb(0, 153, 204));
        d1.setDrawCircles(false);
        //d1.setCircleRadius(4.5f);
        // d1.setHighLightColor(Color);
        d1.setDrawValues(false);
        d1.setDrawFilled(true);


        // gradient color under the linechart
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.fade_red);
        d1.setFillDrawable(drawable);

        ArrayList<Entry> e2 = new ArrayList<Entry>();

        for (int i = 0; i < xAxisMaxSize; i++) {
            e2.add(new Entry(i, e1.get(i).getY() - 30));
        }

        LineDataSet d2 = new LineDataSet(e2, "New DataSet " + timePeriod + ", (2)");
        d2.setLineWidth(2.5f);
        d2.setCircleRadius(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(false);

        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        sets.add(d1);
        // sets.add(d2);

        LineData cd = new LineData(sets);
        return cd;
    }
}
