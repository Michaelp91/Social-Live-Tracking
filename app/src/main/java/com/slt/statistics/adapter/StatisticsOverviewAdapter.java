package com.slt.statistics.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.slt.R;
import com.slt.control.DataProvider;
import com.slt.data.Timeline;
import com.slt.statistics.Sport;
import com.slt.statistics.IndividualStatistics;
import com.slt.statistics.graphs.ChartItem;
import com.slt.statistics.graphs.DayAxisValueFormatter;
import com.slt.statistics.graphs.MyAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by matze on 08.11.17.
 */
public class StatisticsOverviewAdapter extends ArrayAdapter<ChartItem>
        {

    public static View rowView = null;
    BarChart mChart;




    public StatisticsOverviewAdapter(Context context, List<ChartItem> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View.OnClickListener listener;
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        String[] values = new String[]{"Walking", "Running", "Biking"};


        // draw general view of sports a pie chart, sports overview
        if (position > 0) { // draw rows
            rowView = inflater.inflate(R.layout.rowlayout_linechart, parent, false);


            ChartItem chartItem = getItem(position);

            mChart = (BarChart) rowView.findViewById(R.id.chart1);
            //mChart.setOnChartValueSelectedListener(this);

            mChart.setDrawBarShadow(false);
            mChart.setDrawValueAboveBar(true);

            mChart.getDescription().setEnabled(false);

            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            mChart.setMaxVisibleValueCount(60);

            // scaling can now only be done on x- and y-axis separately
            mChart.setPinchZoom(false);

            mChart.setDrawGridBackground(false);
            // mChart.setDrawYLabels(false);

            IAxisValueFormatter xAxisFormatter;

            if(position == 1) {
                xAxisFormatter = new IAxisValueFormatter() {

                    private SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm");

                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {

                        long millis = TimeUnit.HOURS.toMillis((long) value);
                        return mFormat.format(new Date(millis));
                    }
                };
            } else
                xAxisFormatter = new DayAxisValueFormatter(mChart);

            XAxis xAxis = mChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
           // xAxis.setTypeface(mTfLight);
            xAxis.setDrawGridLines(false);
            xAxis.setGranularity(1f); // only intervals of 1 day
            xAxis.setLabelCount(7);
            xAxis.setValueFormatter(xAxisFormatter);

            IAxisValueFormatter custom = new MyAxisValueFormatter();

            YAxis leftAxis = mChart.getAxisLeft();
            //leftAxis.setTypeface(mTfLight);
            leftAxis.setLabelCount(8, false);
            leftAxis.setValueFormatter(custom);
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            leftAxis.setSpaceTop(15f);
            leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            YAxis rightAxis = mChart.getAxisRight();
            rightAxis.setDrawGridLines(false);
           // rightAxis.setTypeface(mTfLight);
            rightAxis.setLabelCount(8, false);
            rightAxis.setValueFormatter(custom);
            rightAxis.setSpaceTop(15f);
            rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            Legend l = mChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setForm(Legend.LegendForm.SQUARE);
            l.setFormSize(9f);
            l.setTextSize(11f);
            l.setXEntrySpace(4f);
            // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
            // "def", "ghj", "ikl", "mno" });
            // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
            // "def", "ghj", "ikl", "mno" });

           // XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
            //mv.setChartView(mChart); // For bounds control
            //  mChart.setMarker(mv); // Set the marker to the chart

            mChart.setData((BarData) chartItem.getmChartData());
/*
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
            chart.animateX(500);*/

            // image:
            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

            System.out.println("position! " + position);
            switch (position) {
                case 1:
                    imageView.setImageResource(R.drawable.walking);
                    break;
                case 2:
                    imageView.setImageResource(R.drawable.running);
                    break;
                case 3:
                    imageView.setImageResource(R.drawable.biking);
                    break;
                default:
                    System.err.println("Position not recognised.");
            }

            ////////////////////////
            TextView textView = (TextView) rowView.findViewById(R.id.label);
            String s = values[position - 1];
            textView.setText(s);


            ////////////////////////
            TextView textView_infos_1 = (TextView) rowView.findViewById(R.id.infos_1);
            String info = "Something 1: " + "15 km";
            textView_infos_1.setText(info);

            TextView textView_infos_2 = (TextView) rowView.findViewById(R.id.infos_2);
            info = "Something 2: " + "15 km";
            textView_infos_2.setText(info);

            TextView textView_infos_3 = (TextView) rowView.findViewById(R.id.infos_3);
            info = "Something 3: " + "15 km";
            textView_infos_3.setText(info);

            rowView.setTag(position);

        } else { // draw pie chart
            ChartItem chartItem = getItem(position);

            rowView = chartItem.getView(position, convertView, getContext());
        }


        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                Sport sport;
                switch (position) {
                    case 1:
                        sport = Sport.WALKING;
                        break;
                    case 2:
                        sport = Sport.RUNNING;
                        break;
                    case 3:
                        sport = Sport.BIKING;
                        break;
                    default:
                        sport = Sport.NONE;
                        break;
                }

                IndividualStatistics.setSelectedSportStatistics(sport);
                Timeline timeline = DataProvider.getInstance().getUserTimeline();

                //Inform the user which listitem has been clicked
                Toast.makeText(getContext().getApplicationContext(), "Button1 clicked: " +
                                IndividualStatistics.getSelectedSportStatistics() +
                                ", timelineID = " +
                                timeline.getAchievementsListForMonth().size()
                        , Toast.LENGTH_SHORT).show();

                // start new activity with tabs and details
                viewIndividualStatistics();
            }
        });

        return rowView;
    }



    private void viewIndividualStatistics() {
        Intent intent = new Intent(getContext(), IndividualStatistics.class);

        getContext().startActivity(intent);
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
