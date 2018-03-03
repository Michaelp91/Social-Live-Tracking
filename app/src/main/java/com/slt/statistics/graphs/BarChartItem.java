
package com.slt.statistics.graphs;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;

import com.github.mikephil.charting.data.ChartData;

/**
 * class for objects bundling chart data and other additional meta data
 * userful for the developer like typeface or type of chart data (e.g. bar or line data)
 * @autor matze
 */
public class BarChartItem extends ChartItem {

    /**
     * typeface for the chart data
     */
    private Typeface mTf;

    public BarChartItem(ChartData<?> cd, Context c) {
        super(cd);

        mTf = Typeface.createFromAsset(c.getAssets(), "OpenSans-Regular.ttf");
    }

    public Typeface getmTf() {
        return this.mTf;
    }

    @Override
    public int getItemType() {
        return TYPE_BARCHART;
    }

    @Override
    public View getView(int position, View convertView, Context c) {
        return convertView;
    }


}
