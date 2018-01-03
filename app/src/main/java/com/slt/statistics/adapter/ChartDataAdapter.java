package com.slt.statistics.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.slt.R;
import com.slt.fragments.global.FragmentThree;
import com.slt.statistics.GeneralViewOfStatistics;
import com.slt.statistics.Sport;
import com.slt.statistics.ViewStatistics;
import com.slt.statistics.graphs.ChartItem;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by matze on 08.11.17.
 */
public class ChartDataAdapter extends ArrayAdapter<ChartItem> {

    public static View rowView = null;


    public ChartDataAdapter(Context context, List<ChartItem> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        String[] values = new String[]{"Walking", "Running", "Biking"};
        //View rowView;

        if(ViewStatistics.getSelectedSportStatistics() != Sport.NONE) {
            // switch activity
        }

        // apply styling
        // holder.chart.setValueTypeface(mTf);
        if (position > 0) {
            rowView = inflater.inflate(R.layout.rowlayout_linechart, parent, false);



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

        } else {
            ChartItem chartItem = getItem(position);

            rowView = chartItem.getView(position, convertView, getContext());
        }

        rowView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Inform the user the button has been clicked
                Toast.makeText(getContext().getApplicationContext(), "Button1 clicked.", Toast.LENGTH_SHORT).show();

                // start new activity with tabs and details
                viewStatisticsDetails();
            }
        });

        return rowView;
    }

    private View getViewOfGaming(LayoutInflater inflater, View convertView, ViewGroup parent) {
        return null;
    }

    private View getViewOfAchivements(LayoutInflater inflater, View convertView, ViewGroup parent) {
        return null;
    }

    private View getViewOfDetailsText(LayoutInflater inflater, View convertView, ViewGroup parent) {
        return null;
    }

    private View getViewOfLineChart(LayoutInflater inflater, View convertView, ViewGroup parent) {
        return null;
    }

    private void viewStatisticsDetails() {
        Intent intent = new Intent(  getContext()  , ViewStatistics.class);

        getContext().startActivity(intent);
    }

    private SpannableString generateCenterText() {
        SpannableString s = new SpannableString("Revenues\nQuarters 2015");
        s.setSpan(new RelativeSizeSpan(2f), 0, 8, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 8, s.length(), 0);
        return s;
    }

    /**
     * generates less data (1 DataSet, 4 values)
     * @return
     */
    protected PieData generatePieData() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Regular.ttf");

        int count = 4;

        ArrayList<PieEntry> entries1 = new ArrayList<PieEntry>();

        for(int i = 0; i < count; i++) {
            entries1.add(new PieEntry((float) ((Math.random() * 60) + 40), "Quarter " + (i+1)));
        }

        PieDataSet ds1 = new PieDataSet(entries1, "Quarterly Revenues 2015");
        ds1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(12f);

        PieData d = new PieData(ds1);
        d.setValueTypeface(tf);

        return d;
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
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
