package com.slt.statistics.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.slt.MainActivity;
import com.slt.R;
import com.slt.statistics.ViewStatistics;
import com.slt.statistics.achievements.DetailsActivity;
import com.slt.statistics.achievements.GridViewAdapter;
import com.slt.statistics.achievements.ImageItem;
import com.slt.statistics.adapter.details_infos_list.ArrayAdapterItem;
import com.slt.statistics.adapter.details_infos_list.ObjectItem;
import com.slt.statistics.data.DataObjectsCollection;
import com.slt.statistics.graphs.ChartItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by matze on 02.01.18.
 */

public class DetailsDataAdapter  extends ArrayAdapter {
    private GridView gridView;

    public DetailsDataAdapter(@NonNull Context context, List list) {
        super(context, 0, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        switch (position) {
            case 0:
                return getViewOfLineChart(inflater, position, convertView, parent);
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

        for (int i = 0; i < 10; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setId(i);
            imageView.setPadding(2, 2, 2, 2);
                           imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getContext().getResources(), R.drawable.running_cup));

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

       /* HashMap<String, String> infos = (HashMap<String, String>) getItem(position);

        TextView textView_infos_1 = (TextView) rowView.findViewById(R.id.infos_1);
        String info = "Blah 0:";
        textView_infos_1.setText(info);

        TextView textView_infos_2 = (TextView) rowView.findViewById(R.id.infos_2);
        info = infos.get("Blah 0:");
        textView_infos_2.setText(info);*/

        // add your items, this can be done programatically
        // your items can be from a database
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


    private View getViewOfLineChart(LayoutInflater inflater, int position, View convertView, ViewGroup parent) {

        View rowView = inflater.inflate(R.layout.details_rowlayout_linechart, parent, false);

        ChartItem chartItem = (ChartItem) getItem(position);

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

        return rowView;
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
