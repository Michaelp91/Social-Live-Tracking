package com.slt.statistics;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.slt.R;
import com.slt.statistics.adapter.StatisticsOverviewAdapter;
import com.slt.statistics.data.DataObjectsCollection;
import com.slt.statistics.graphs.BarChartItem;
import com.slt.statistics.graphs.ChartItem;
import com.slt.statistics.graphs.PieChartItem;
import java.util.ArrayList;


public class StatisticsOverview extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_general_view_of_statistics, container, false);

        TextView text = viewGroup.findViewById(R.id.hallo);
        text.setText("aisodjfiasdf");

               // super.setTitle("Summaries");
        ListView l = (ListView) viewGroup.findViewById(R.id.list_in_Frag);

        ArrayList<ChartItem> list = new ArrayList<ChartItem>();

        PieData pieData = DataObjectsCollection.dataSupplier.getPieData();

        list.add(new PieChartItem(pieData, getActivity().getApplicationContext()));

        LineData lineData;
        BarData barData;
        Sport sport = Sport.NONE;

        // list with gener view of each sport
        for (int i = 0; i < 3; i++) {

            switch(i) {
                case 0:
                    sport = Sport.WALKING;
                    break;
                case 1:
                    sport = Sport.RUNNING;
                    break;
                case 2:
                    sport = Sport.BIKING;
                    break;
                default:
                    System.err.println("No such sport!");

            }

          //  lineData = DataObjectsCollection.dataSupplier.getLineData(
              //     getApplicationContext(), i, sport);
            barData = DataObjectsCollection.dataSupplier.getBarData(getActivity().getApplicationContext(),i, sport);

            list.add(
                    // new LineChartItem(lineData,
                    new BarChartItem(barData,
                            getActivity().getApplicationContext()));
        }

        StatisticsOverviewAdapter cda = new StatisticsOverviewAdapter(getActivity().getApplicationContext(), list, getFragmentManager());
        l.setAdapter(cda);

        return viewGroup;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }








}
