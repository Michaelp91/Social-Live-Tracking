package com.slt.statistics.graphs;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;

import com.github.mikephil.charting.data.ChartData;

/**
 * baseclass of the chart-listview items
 * @author matze
 *
 */
public abstract class ChartItem {

    /**
     * id of bar chart
     */
    protected static final int TYPE_BARCHART = 0;

    /**
     * id of line chart
     */
    protected static final int TYPE_LINECHART = 1;

    /**
     * id of pie chart
     */
    protected static final int TYPE_PIECHART = 2;
    
    protected ChartData<?> mChartData;
    
    public ChartItem(ChartData<?> cd) {
        this.mChartData = cd;      
    }

    public ChartData<?> getmChartData() {
        return this.mChartData;
    }

    public abstract Typeface getmTf();
    
    public abstract int getItemType();
    
    public abstract View getView(int position, View convertView, Context c);
}
