package com.slt.statistics.data;

import android.content.Context;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.slt.statistics.achievements.ImageItem;

import java.util.ArrayList;

/**
 * interface for preparing chart data
 *
 * Created by matze on 06.11.17.
 */
public interface DataSupplier {

    /**
     * method generates data for the pie chart
     * @return data for the pie chart
     */
    public PieData getPieData();

    /**
     * method generates data for the bar chart
     * @param c - activity context
     * @param timePeriod - time period for which we want to generate data
     * @param sport - sport for which data should be generated
     * @return data for the bar chart
     */
    public BarData getBarData(Context c, int timePeriod, int sport);

}
