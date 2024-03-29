package com.slt.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;
import com.slt.R;
import com.slt.data.Achievement;
import com.slt.statistics.Sport;
import com.slt.statistics.adapter.TimeperiodIndividualSportTabFragmentAdapter;
import com.slt.statistics.graphs.BarChartItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


/**
 * Created by Matze
 */
public class TimeperiodIndividualSportTabFragment extends Fragment {

    public Sport sport = null;
    public int period = 0;
    public LineData lineData = null;
    public HashMap<String, String> infos = null;
    public LinkedList<Achievement> achievements = null;
    public BarData barData;


    public TimeperiodIndividualSportTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_for_sport_tab, container, false);


        ListView l = (ListView) viewGroup.findViewById(R.id.list_in_Frag);


        ArrayList<Object> listWithData = new ArrayList<>();

        // chart
        listWithData.add(new BarChartItem(this.barData, getContext().getApplicationContext()));

        // infos
        listWithData.add(this.infos);

        // achivements
        listWithData.add(this.achievements);

        // todo create adapter for the details within time period
        TimeperiodIndividualSportTabFragmentAdapter adapter = new TimeperiodIndividualSportTabFragmentAdapter(getContext(), listWithData);

        adapter.setPeriod(this.period);

        adapter.setSport(this.sport);

        l.setAdapter(adapter);

        return viewGroup;
    }

    public void setLineData(LineData lineData) {
        this.lineData = lineData;
    }

    public void setBarData(BarData barData) {
        this.barData = barData;
    }

    public void setInfos(HashMap<String, String> infos) {
        this.infos = infos;
    }

    public void setAchievements(LinkedList<Achievement> achievements) {
        this.achievements = achievements;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}