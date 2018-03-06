package com.slt.statistics.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.slt.R;
import com.slt.control.DataProvider;
import com.slt.data.Achievement;
import com.slt.data.Timeline;
import com.slt.definitions.Constants;
import com.slt.statistics.adapter.details_infos_list.ArrayAdapterItem;
import com.slt.statistics.adapter.details_infos_list.ObjectItem;
import com.slt.statistics.data.StatisticsDataModelProvider;
import com.slt.statistics.graphs.BarChartItem;
import com.slt.statistics.graphs.ChartItem;
import com.slt.statistics.graphs.MyAxisValueFormatter;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * adapter, which fills TimeperiodIndividualSportTabFragment with data
 *
 * Created by maciej on 02.01.18.
 */

public class TimeperiodIndividualSportTabFragmentAdapter extends ArrayAdapter
    implements OnChartValueSelectedListener {

    /**
     * distance description for the info box
     */
    public static String DISTANCE = "Distance:";

    /**
     * steps description for the info box
     */
    public static String STEPS = "Steps:";


    /**
     * speed description for the info box
     */
    public static String SPEED =    "Speed:";

    /**
     * duration description for the info box
     */
    public static String DURANCE =  "Duration:";

    /**
     * date description for the info box
     */
    public static String DATE =     "Date:";

    /**
     * adapter for the info box
     */
    private ArrayAdapterItem adapter;

    /**
     * stile of the font
     */
    public Typeface mTfLight =  android.graphics.Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Light.ttf");

    /**
     * bar chart item for the bar chart
     */
    private ChartItem chartItem;

    /**
     * sport selected by the user in StatisticsOverviewFragment
     */
    public int sport = Constants.TIMELINEACTIVITY.UNKNOWN;

    /**
     * time period that will be considered
     */
    public int period = 0;

    /**
     * bar data for the bar chart
     */
    public BarChartItem barData = null;

    /**
     * strings for the info box below the bar chart
     */
    public HashMap<String, String> infos = null;

    /**
     * list with achievements of the user
     */
    public LinkedList<Achievement> achievements = null;

    /**
     * rectangle for clickable area
     */
    protected RectF mOnValueSelectedRectF = new RectF();

    /**
     * bar chart
     */
    private BarChart mChart;



    public TimeperiodIndividualSportTabFragmentAdapter(@NonNull Context context, List list) {
        super(context, 0, list);

        barData = (BarChartItem) list.get(0);
        infos = (HashMap<String, String>) list.get(1);
        achievements = (LinkedList<Achievement>) list.get(2);
    }

    /**
     * prepares the view of the TimeperiodIndividualSportTabFragment by filling its layouts with data
     * @param position of the tam
     * @param convertView
     * @param parent view
     * @return view of fragment filled with data
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        switch (position) {
            case 0:
                return getViewOfBarChart(inflater, position, convertView, parent);
            case 1:
                return getViewOfDetailsText(inflater, position, convertView, parent);
            case 2:
                return getViewOfAchivements(inflater, position, convertView, parent);
            default:
                System.out.println("Error! Too many items.");
        }

        return null;
    }

    /**
     * method fills the layout for the achievement's list with data
     *
     * @param inflater for the view object
     * @param position position of the view in the list
     * @param convertView
     * @param parent view
     * @return view with achievements
     */
    private View getViewOfAchivements(LayoutInflater inflater, int position, View convertView, ViewGroup parent) {
        Achievement a;
        int drawableID;
        View rowView = inflater.inflate(R.layout.details_rowlayout_achivements, parent, false);

        LinearLayout layout = (LinearLayout) rowView.findViewById(R.id.linear);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(10, 10, 10, 10);


        for (int i = 0; i < this.achievements.size(); i++) {
            a = this.achievements.get(i);

            drawableID = a.getDrawableOfAchievement();

            ImageView imageView = new ImageView(getContext());
            imageView.setId(i);
            imageView.setPadding(2, 2, 2, 2);
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getContext().getResources(), drawableID));

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            layout.addView(imageView, layoutParams);
        }


        return rowView;
    }



    /**
     * method fills the layout for the infos with data
     *
     * @param inflater for the view object
     * @param position position of the view in the list
     * @param convertView
     * @param parent view
     * @return view with achievements
     */
    private View getViewOfDetailsText(LayoutInflater inflater, int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.details_infos_list, parent, false);

        List<ObjectItem> objectItemData = new ArrayList<>();

        objectItemData.add(new ObjectItem(SPEED, "select bar in chart"));

        if(this.sport == Constants.TIMELINEACTIVITY.WALKING
                || this.sport == Constants.TIMELINEACTIVITY.ON_FOOT) {
            objectItemData.add(new ObjectItem(STEPS, "select bar in chart"));
        } else
            objectItemData.add(new ObjectItem(DISTANCE, "select bar in chart"));

        objectItemData.add(new ObjectItem(DURANCE, "select bar in chart"));

        // our adapter instance
        adapter = new ArrayAdapterItem(getContext(), objectItemData);

        // create a new ListView, set the adapter and item click listener
        ListView listViewItems = (ListView) rowView.findViewById(R.id.listview_infos); //new ListView(getContext());
        listViewItems.setAdapter(adapter);


        return rowView;
    }



    public void setSport(int sport) {
        this.sport = sport;
    }

    public void setPeriod(int period) {
        this.period = period;
    }


    /**
     * method fills the layout for the bar chart with data
     * @param inflater for the view object
     * @param position position of the view in the list
     * @param convertView
     * @param parent view
     * @return view with achievements
     */
    private View getViewOfBarChart(LayoutInflater inflater, int position, View convertView, ViewGroup parent) {
        IAxisValueFormatter xAxisFormatter = null;
        View rowView = inflater.inflate(R.layout.details_rowlayout_barchart, parent, false);

        chartItem = (ChartItem) getItem(position);

        mChart = (BarChart) rowView.findViewById(R.id.chart1);


        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);

        if(this.period == 0) {
            xAxisFormatter = new IAxisValueFormatter() {

                DateFormat dateFormat = new SimpleDateFormat("hh:mm a");

                @Override
                public String getFormattedValue(float value, AxisBase axis) {

                    long millis = TimeUnit.HOURS.toMillis((long) value);
                    Date d = new Date(millis);

                    return dateFormat.format(d);
                }
            };
        } else if(this.period == 2)
            xAxisFormatter = new IAxisValueFormatter() {

                private String getMonthForInt(int num) {
                    String month = "wrong";
                    DateFormatSymbols dfs = new DateFormatSymbols();
                    String[] months = dfs.getMonths();
                    if (num >= 0 && num <= 11 ) {
                        month = months[num];
                    }
                    return month;
                }

                @Override
                public String getFormattedValue(float value, AxisBase axis) {

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(StatisticsDataModelProvider.dateUsedAsReference);
                    int month = cal.get(Calendar.MONTH);

                    String monthStr = getMonthForInt(month);
                    StringBuffer sb = new StringBuffer();
                    String monthAbbreviation = "";

                    for (int i = 0; i < 3; i++) {
                        sb.append(monthStr.charAt(i));
                    }

                    monthAbbreviation = sb.toString();

                    String appendix = ". " + monthAbbreviation;
                    int dayOfMonth = (int) value;


                    return dayOfMonth == 0 ? "" : dayOfMonth + appendix + "";
                }
            };
        else if(this.period == 1) {// TestDataGenerator_.getXYPairsForWeek_withDates

            final int sportType = this.sport;

            xAxisFormatter = new IAxisValueFormatter() {

                LinkedList<Date> dates = null;

                private String getMonthForInt(int num) {
                    String month = "wrong";
                    DateFormatSymbols dfs = new DateFormatSymbols();
                    String[] months = dfs.getMonths();
                    if (num >= 0 && num <= 11 ) {
                        month = months[num];
                    }
                    return month;
                }

                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    long key;
                    int val;
                    Timeline timeline;

                    if(dates == null) {

                        timeline = DataProvider.getInstance().getUserTimeline();

                        dates = timeline.getDatesOfThisWeek(new Date());

                    }

                    Date dateOfTheDay = dates.get((int) value);

                    // prepare the day and month nr
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dateOfTheDay);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    int month = cal.get(Calendar.MONTH);

                    // prepare abbreviation for the month string
                    String monthStr = getMonthForInt(month);
                    StringBuffer sb = new StringBuffer();
                    String monthAbbreviation = "";

                    for (int i = 0; i < 3; i++) {
                        sb.append(monthStr.charAt(i));
                    }

                    monthAbbreviation = sb.toString();


                    String appendix = ". " + monthAbbreviation;
                    int dayOfMonth = (int) day;


                    return dayOfMonth == 0 ? "" : dayOfMonth + appendix + "";
                }
            };
        }

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTfLight);
        xAxis.setTextColor(Color.WHITE);


        xAxis.setLabelRotationAngle(45.f);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawLabels(true);
        leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawLabels(false);
        rightAxis.setTypeface(mTfLight);
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        XYMarkerView mv = new XYMarkerView(getContext(), xAxisFormatter, this.period, this.sport);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

        mChart.setData((BarData) barData.getmChartData());

        return rowView;
    }

    /**
     * method function: clicked bar will be highlighted and the info about
     * data will appear above the bar, also info boxes will be updated
     *
     * @param e - (x,y)-pair of data
     * @param h - highlight from chart
     */
    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;

        RectF bounds = mOnValueSelectedRectF;
        mChart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = mChart.getPosition(e, YAxis.AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index",
                "low: " + mChart.getLowestVisibleX() + ", high: "
                        + mChart.getHighestVisibleX());

        MPPointF.recycleInstance(position);

        // ----- updating info box


        // convert x and y to data
        double speed =  StatisticsDataModelProvider.getSpeed(e, this.period, this.sport);
        int distance = 0;
        int steps = 0;
        long durance =  StatisticsDataModelProvider.getDuration(e, this.period, this.sport);;

        adapter.getItem(0).setLabelAndVal(SPEED,    String.valueOf( speed * ( 3600 / 1000 ) ) + " km/h");

        String unit = "";

        if(this.sport == Constants.TIMELINEACTIVITY.WALKING
                || this.sport == Constants.TIMELINEACTIVITY.ON_FOOT) {
            steps =  StatisticsDataModelProvider.getSteps(e);

            unit = "steps";

            adapter.getItem(1).setLabelAndVal(STEPS, String.valueOf( steps ) + unit);

        } else {
            unit = "m";
            distance =  StatisticsDataModelProvider.getDistance(e);
            adapter.getItem(1).setLabelAndVal(DISTANCE, String.valueOf( distance ) + unit);

        }

        // update info boxes
        adapter.getItem(2).setLabelAndVal(DURANCE,  String.valueOf( durance ) + " min");

        adapter.notifyDataSetChanged();
    }

    /**
     * method defines, what should be done when no bar is selected. In this case all the info boxes
     * should be updated to the string "select bar in the chart", to motivate user to click on
     * one of the bars
     */
    @Override
    public void onNothingSelected() {
        adapter.getItem(0).setLabelAndVal(SPEED,    "select bar in chart");
        adapter.getItem(1).setLabelAndVal(DISTANCE, "select bar in chart");
        adapter.getItem(2).setLabelAndVal(DURANCE,  "select bar in chart");

        adapter.notifyDataSetChanged();
    }
}
