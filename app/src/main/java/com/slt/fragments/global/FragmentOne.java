package com.slt.fragments.global;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.mikephil.charting.data.LineData;
import com.slt.R;
import com.slt.data.Achievement;
import com.slt.statistics.achievements.Tupeln_AchievementImage_and_Info;
import com.slt.statistics.adapter.DetailsDataAdapter;
import com.slt.statistics.graphs.LineChartItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


/**
 * Created by Anu on 22/04/17.
 */
public class FragmentOne extends Fragment {

    public LineData lineData = null;
    public HashMap<String, String> infos = null;
    public LinkedList<Achievement> achievements = null;


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


        ArrayList<Object> list = new ArrayList<>();

        // chart
        //LineData lineData = DataObjectsCollection.dataSupplier.getLineData(getContext().getApplicationContext(), 2, "walking");

        list.add(new LineChartItem(this.lineData, getContext().getApplicationContext()));

        // infos
        list.add(this.infos);

        // achivements
        //LinkedList< LinkedList<Tupeln_AchievementImage_and_Info> > achievements = new ArrayList<>();
        list.add(this.achievements);

        // todo create adapter for the details within time period
        DetailsDataAdapter adapter = new DetailsDataAdapter(getContext(), list);

        l.setAdapter(adapter);

        return viewGroup;
    }

    public void setLineData(LineData lineData) {
        this.lineData = lineData;
    }

    public void setInfos(HashMap<String, String> infos) {
        this.infos = infos;
    }

    public void setAchievements(LinkedList<Achievement> achievements) {
        this.achievements = achievements;
    }
}