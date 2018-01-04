package com.slt.fragments.global;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.data.LineData;
import com.slt.R;
import com.slt.statistics.achievements.Achievement;
import com.slt.statistics.adapter.DetailsDataAdapter;
import com.slt.statistics.data.DataObjectsCollection;
import com.slt.statistics.graphs.LineChartItem;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Anu on 22/04/17.
 */
public class FragmentOne extends Fragment {


    public FragmentOne() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_one, container, false);


        ListView l = (ListView) viewGroup.findViewById(R.id.list_in_Frag);
        //String[] values = new String[] { "Walking", "Running", "Biking" };

        // activity name:
        TextView activityName_TextView = (TextView) viewGroup.findViewById(R.id.activity_name);
        activityName_TextView.setText("Activity name todo");

        ArrayList<Object> list = new ArrayList<>();

        // chart
        LineData lineData = DataObjectsCollection.dataSupplier.getLineData(getContext().getApplicationContext(), 2, "walking");

        list.add(new LineChartItem(lineData, getContext().getApplicationContext()));

        // infos
        HashMap<String, String> infos = new HashMap<>();

        for (int i = 0; i < 5; i++) {
            infos.put("Blah " + i + ":", "blah " + i);
        }

        list.add(infos);

        // achivements
        ArrayList<Achievement> achievements = new ArrayList<>();

        list.add(achievements);

        // todo create adapter for the details within time perdiod
        DetailsDataAdapter adapter = new DetailsDataAdapter(getContext(), list);

        l.setAdapter(adapter);


        return viewGroup;
    }




}