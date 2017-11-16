package com.slt.statistics.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.slt.R;
import com.slt.statistics.graphs.ChartItem;
import com.slt.statistics.graphs.LineChartItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matze on 08.11.17.
 */
public class ChartDataAdapter extends ArrayAdapter<ChartItem> {

    public ChartDataAdapter(Context context, List<ChartItem> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        String[] values = new String[] { "Walking", "Running", "Biking" };
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);

        /////////////////

        // apply styling
        // holder.chart.setValueTypeface(mTf);
        ChartItem chartItem = getItem(position);

        LineChart chart = (LineChart) rowView.findViewById(R.id.chart1);
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(chartItem.getmTf());
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(chartItem.getmTf());
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setTypeface(chartItem.getmTf());
        rightAxis.setLabelCount(5, false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        // set data
        chart.setData((LineData) chartItem.getmChartData());
        chart.setData((LineData) chartItem.getmChartData());

        // do not forget to refresh the chart
        // holder.chart.invalidate();
        chart.animateX(500);



        // image:
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        System.out.println("position! " + position);
        switch (position) {
            case 0:
                imageView.setImageResource(R.drawable.walking);
                break;
            case 1:
                imageView.setImageResource(R.drawable.running);
                break;
            case 2:
                imageView.setImageResource(R.drawable.biking);
                break;
            default:
                System.err.println("Position not recognised.");
        }


        TextView textView = (TextView) rowView.findViewById(R.id.label);
        String s = values[position];
        textView.setText(s);


        return rowView;
    }

    @Override
    public int getItemViewType(int position) {
        // return the views type
        return getItem(position).getItemType();
    }

    @Override
    public int getViewTypeCount() {
        return 3; // we have 3 different item-types
    }


}
