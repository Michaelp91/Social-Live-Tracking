package com.slt.statistics.graphs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.slt.R;
import com.slt.data.Timeline;

/**
 * class for objects bundling chart data and other additional meta data
 * userful for the developer like typeface or type of chart data (e.g. bar or line data)
 * @autor matze
 */
public class PieChartItem extends ChartItem {

    /**
     * typeface for the chart data
     */
    private Typeface mTf;

    /**
     * text showed in the middle of the pie chart
     */
    private SpannableString mCenterText;

    public PieChartItem(ChartData<?> cd, Context c) {
        super(cd);

        mTf = Typeface.createFromAsset(c.getAssets(), "OpenSans-Regular.ttf");
        mCenterText = generateCenterText();
    }

    @Override
    public Typeface getmTf() {
        return this.mTf;
    }

    @Override
    public int getItemType() {
        return TYPE_PIECHART;
    }

    @Override
    public View getView(int position, View convertView, Context c) {

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(c).inflate(
                    R.layout.rowlayout_piechart, null);
            holder.chart = (PieChart) convertView.findViewById(R.id.chart1);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // apply styling
        holder.chart.getDescription().setEnabled(false);
        holder.chart.setHoleRadius(52f);
        holder.chart.setTransparentCircleRadius(57f);
        holder.chart.setCenterText(mCenterText);
        holder.chart.setCenterTextTypeface(mTf);
        holder.chart.setCenterTextSize(9f);
        holder.chart.setUsePercentValues(true);
        holder.chart.setExtraOffsets(5, 10, 50, 10);

        mChartData.setValueFormatter(new PercentFormatter());
        mChartData.setValueTypeface(mTf);
        mChartData.setValueTextSize(11f);
        mChartData.setValueTextColor(Color.WHITE);
        // set data
        holder.chart.setData((PieData) mChartData);


        Legend l = holder.chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // do not forget to refresh the chart
        holder.chart.animateY(900);

        return convertView;
    }



    private SpannableString generateCenterText() {
        SpannableString s = new SpannableString("Overview for " + Timeline.getMonthForInt());
        return s;
    }

    private static class ViewHolder {
        PieChart chart;
    }
}
