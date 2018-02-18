package com.slt.statistics.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
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
import com.slt.data.Achievement;
import com.slt.statistics.Sport;
import com.slt.statistics.adapter.details_infos_list.*;
// macht momentan Fehler bei mir:
//import com.slt.statistics.achievements.DetailsActivity;
import com.slt.statistics.graphs.BarChartItem;
import com.slt.statistics.graphs.ChartItem;
import com.slt.statistics.graphs.DayAxisValueFormatter;
import com.slt.statistics.graphs.MyAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by matze on 02.01.18.
 */

public class TimeperiodIndividualSportTabFragmentAdapter extends ArrayAdapter
    implements OnChartValueSelectedListener {
    private GridView gridView;
    public Sport sport = null;
    public int period = 0;
    //public LineChartItem lineData = null;
    public BarChartItem barData = null;
    public HashMap<String, String> infos = null;
    public LinkedList<Achievement> achievements = null;
    protected RectF mOnValueSelectedRectF = new RectF();
    BarChart mChart;



    public TimeperiodIndividualSportTabFragmentAdapter(@NonNull Context context, List list) {
        super(context, 0, list);

       // lineData = (LineChartItem) list.get(0);
        barData = (BarChartItem) list.get(0);
        infos = (HashMap<String, String>) list.get(1);
        achievements = (LinkedList<Achievement>) list.get(2);
    }

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
            case 3:
                return getViewOfGaming(inflater, position, convertView, parent);
            default:
                System.out.println("Error! Too many items.");
        }


        return null;
    }


    private View getViewOfGaming(LayoutInflater inflater, int position, View convertView, ViewGroup parent) {
        return null;
    }

    private View getViewOfAchivements(LayoutInflater inflater, int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.details_rowlayout_achivements, parent, false);

        LinearLayout layout = (LinearLayout) rowView.findViewById(R.id.linear);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(10, 10, 10, 10);


       // LinkedList<Tupeln_AchievementImage_and_Info> achievements = listAchievements;//AchievementCalculator.getOwnUserAchievements(0);

        Achievement a;
        int drawableID;

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

        layout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext().getApplicationContext(), "Button1 clicked.", Toast.LENGTH_SHORT).show();

                    }
                }
        );
        /*gridView = (GridView) rowView.findViewById(R.id.gridView);
        GridViewAdapter gridAdapter = new GridViewAdapter(getContext(), R.layout.grid_item_layout, DataObjectsCollection.dataSupplier.getImageItems(getContext(), "walking" )); //getData());
        gridView.setAdapter(gridAdapter);*/

        /*gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                // Create intent
                Intent intent = new Intent(, DetailsActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("image", item.getImage());

                //Start details activity
                getContext().startActivity(intent);
            }
        });*/


        return rowView;
    }


    private View getViewOfDetailsText(LayoutInflater inflater, int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.details_infos_list, parent, false);

        /*HashMap<String, String> infos = (HashMap<String, String>) getItem(position);

        TextView textView_infos_1 = (TextView) rowView.findViewById(R.id.infos_1);
        String info = "Blah 0:";
        textView_infos_1.setText(info);

        TextView textView_infos_2 = (TextView) rowView.findViewById(R.id.infos_2);
        info = infos.get("Blah 0:");
        textView_infos_2.setText(info);*/

        List<ObjectItem> ObjectItemData = new ArrayList<>();

        ObjectItemData.add(new ObjectItem("91", "Mercury"));
        ObjectItemData.add(new ObjectItem("92", "Watson"));
        ObjectItemData.add(new ObjectItem("93", "Nissan"));
        ObjectItemData.add(new ObjectItem("94", "Puregold"));
        ObjectItemData.add(new ObjectItem("95", "SM"));


        // our adapter instance
        ArrayAdapterItem adapter = new ArrayAdapterItem(getContext(), ObjectItemData);

        // create a new ListView, set the adapter and item click listener
        ListView listViewItems = (ListView) rowView.findViewById(R.id.listview_infos); //new ListView(getContext());
        listViewItems.setAdapter(adapter);


        return rowView;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    private View getViewOfBarChart(LayoutInflater inflater, int position, View convertView, ViewGroup parent) {
        IAxisValueFormatter xAxisFormatter;
        View rowView = inflater.inflate(R.layout.details_rowlayout_linechart, parent, false);

        ChartItem chartItem = (ChartItem) getItem(position);

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
        // mChart.setDrawYLabels(false);

        if(this.period == 0) {
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
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
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
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        mChart.setData((BarData) chartItem.getmChartData());

        return rowView;
    }

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
    }

    @Override
    public void onNothingSelected() {

    }

 /*   // Prepare some dummy data for gridview
    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            imageItems.add(new ImageItem(bitmap, "Image#" + i));
        }
        return imageItems;
    }*/
}
