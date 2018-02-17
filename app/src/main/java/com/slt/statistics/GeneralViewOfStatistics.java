package com.slt.statistics;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.widget.ListView;

import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.slt.DrawerUtil;
import com.slt.R;
import com.slt.statistics.adapter.ChartDataAdapter;
import com.slt.statistics.data.DataObjectsCollection;
import com.slt.statistics.data.DataSupplier;
import com.slt.statistics.data.MainDataSupplier;
import com.slt.statistics.graphs.ChartItem;
import com.slt.statistics.graphs.LineChartItem;
import com.slt.statistics.graphs.PieChartItem;

import java.util.ArrayList;


public class GeneralViewOfStatistics extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_view_of_statistics);
        super.setTitle("Summaries");
        ListView l = (ListView) findViewById(R.id.list_in_Frag);

        ArrayList<ChartItem> list = new ArrayList<ChartItem>();

        PieData pieData = DataObjectsCollection.dataSupplier.getPieData(1, "walking");

        list.add(new PieChartItem(pieData, getApplicationContext()));

        LineData lineData;

        // 3 items
        for (int i = 0; i < 3; i++) {
            lineData = DataObjectsCollection.dataSupplier.getLineData(
                    getApplicationContext(), i, "walking");

            list.add(new LineChartItem(lineData, getApplicationContext()));
        }

        ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), list);
        l.setAdapter(cda);
    }






}
