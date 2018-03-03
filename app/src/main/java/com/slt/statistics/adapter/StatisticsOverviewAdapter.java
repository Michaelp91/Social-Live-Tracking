package com.slt.statistics.adapter;


import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.slt.R;
import com.slt.definitions.Constants;
import com.slt.statistics.IndividualStatistics;
import com.slt.statistics.graphs.ChartItem;
import com.slt.statistics.graphs.MyAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * class should be used to fill StatisticsOverviewFragment objects with data
 *
 * Created by matze on 08.11.17.
 */
public class StatisticsOverviewAdapter extends ArrayAdapter<ChartItem> {

    /**
     * view of a row from the list shoed in the fragment filled with data
     */
    public static View rowView = null;
    /**
     * bar chart showed in the list
     */
    BarChart mChart;
    /**
     * manager for fragment transactions (notice the version v4)
     */
    public static FragmentManager fragmentManager = null;


    public StatisticsOverviewAdapter(Context context, List<ChartItem> objects, FragmentManager fragmentManager) {
        super(context, 0, objects);
        this.fragmentManager = fragmentManager;
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
            rowView.setTag(position);


            ChartItem chartItem = getItem(position);

            mChart = (BarChart) rowView.findViewById(R.id.chart1);

            mChart.setDrawBarShadow(false);
            mChart.setDrawValueAboveBar(false);

            mChart.getDescription().setEnabled(false);

            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            mChart.setMaxVisibleValueCount(60);

            // scaling can now only be done on x- and y-axis separately
            mChart.setPinchZoom(false);

            mChart.setDrawGridBackground(false);

            IAxisValueFormatter xAxisFormatter;


            xAxisFormatter = new IAxisValueFormatter() {

                private SimpleDateFormat mFormat = new SimpleDateFormat(".");

                @Override
                public String getFormattedValue(float value, AxisBase axis) {

                    String appendix = ".";
                    int dayOfMonth = (int) value;



                    return dayOfMonth == 0 ? "" : dayOfMonth + appendix + "";
                }
            };

            XAxis xAxis = mChart.getXAxis();
             xAxis.setLabelRotationAngle(45.f);
            xAxis.setTextColor(Color.WHITE);

            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setGranularity(1f); // only intervals of 1 day
            xAxis.setValueFormatter(xAxisFormatter);

            IAxisValueFormatter custom = new MyAxisValueFormatter();

            YAxis leftAxis = mChart.getAxisLeft();
            leftAxis.setTextColor(Color.WHITE);
            leftAxis.setLabelCount(4, false);
            leftAxis.setValueFormatter(custom);
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            leftAxis.setSpaceTop(15f);
            leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            YAxis rightAxis = mChart.getAxisRight();
            rightAxis.setDrawLabels(false);
            rightAxis.setTextColor(Color.WHITE);
            rightAxis.setDrawGridLines(false);

            // / Set the marker to the chart
            chartItem.getmChartData().setDrawValues(false);
            mChart.setData((BarData) chartItem.getmChartData());

            // image:
            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

            // info label with the name of activity
            TextView textView = (TextView) rowView.findViewById(R.id.activity_name);


            System.out.println("position! " + position);
            switch (position) {
                case 1:
                    textView.setText("Walking");
                    imageView.setImageResource(R.drawable.walking);
                    break;
                case 2:
                    textView.setText("Running");
                    imageView.setImageResource(R.drawable.running);
                    break;
                case 3:
                    textView.setText("Biking");
                    imageView.setImageResource(R.drawable.biking);
                    break;
                default:
                    System.err.println("Position not recognised.");
            }

            // button to details
            Button buttonToDetails = (Button) rowView.findViewById(R.id.button_to_details);

            buttonToDetails.setTag(position);

            buttonToDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = (Integer) v.getTag();
                    int sport;
                    switch (position) {
                        case 1:
                            sport = Constants.TIMELINEACTIVITY.WALKING;
                            break;
                        case 2:
                            sport = Constants.TIMELINEACTIVITY.RUNNING;
                            break;
                        case 3:
                            sport = Constants.TIMELINEACTIVITY.ON_BICYCLE;
                            break;
                        default:
                            sport = Constants.TIMELINEACTIVITY.UNKNOWN;
                            System.err.println("No such activity.");
                            break;
                    }


                    // start new activity with tabs and details
                    FragmentManager fm = StatisticsOverviewAdapter.fragmentManager;

                    if (fm != null) {
                        FragmentTransaction ft = fm.beginTransaction();

                        IndividualStatistics individualStatistics = new IndividualStatistics();

                        individualStatistics.setSelectedSportStatistics(sport);

                        ft.replace(R.id.content_main_frame, individualStatistics);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                }

            });

        } else { // draw pie chart
            ChartItem chartItem = getItem(position);

            rowView = chartItem.getView(position, convertView, getContext());
        }

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
