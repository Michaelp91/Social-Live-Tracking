package com.slt.statistics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.slt.R;
import com.slt.statistics.adapter.StatisticsOverviewAdapter;
import com.slt.statistics.data.DataObjectsCollection;
import com.slt.statistics.graphs.ChartItem;
import com.slt.statistics.graphs.LineChartItem;
import com.slt.statistics.graphs.PieChartItem;

import java.util.ArrayList;


public class StatisticsOverview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_view_of_statistics);
        super.setTitle("Summaries");
        ListView l = (ListView) findViewById(R.id.list_in_Frag);

        ArrayList<ChartItem> list = new ArrayList<ChartItem>();

        PieData pieData = DataObjectsCollection.dataSupplier.getPieData();

        list.add(new PieChartItem(pieData, getApplicationContext()));

        LineData lineData;
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

            lineData = DataObjectsCollection.dataSupplier.getLineData(
                    getApplicationContext(), i, sport);

            list.add(new LineChartItem(lineData, getApplicationContext()));
        }

        StatisticsOverviewAdapter cda = new StatisticsOverviewAdapter(getApplicationContext(), list);
        l.setAdapter(cda);
    }






}
