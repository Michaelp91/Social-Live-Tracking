package com.slt.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.slt.R;
import com.slt.statistics.Activity;
import com.slt.statistics.TimePeriod;
import com.slt.statistics.graphs.GraphBuilder;

public class MySimpleArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public MySimpleArrayAdapter(Context context, String[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String activityString = values[position];

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        //TextView textView2 = (TextView) rowView.findViewById(R.id.secondLine);

     /*   GraphView graph = (GraphView) rowView.findViewById(R.id.);
        Activity[] activities = new Activity[] {Activity.WALKING, Activity.RUNNING, Activity.BIKING};
        GraphBuilder graphBuilder = new GraphBuilder();
        TimePeriod timePeriod = TimePeriod.TODAY; // todo

        graphBuilder.setGraphSeries(activities[position], timePeriod, graph);
*/
        ///////////////////////////////



        ///////////////////////////////

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
        //textView2.setText("blah blah");
        // change the icon for Windows and iPhone


        return rowView;
    }
}
