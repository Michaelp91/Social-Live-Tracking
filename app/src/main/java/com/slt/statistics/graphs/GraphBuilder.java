package com.slt.statistics.graphs;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.slt.R;
import com.slt.statistics.Activity;
import com.slt.statistics.TimePeriod;
import com.slt.statistics.data.BikingData;
import com.slt.statistics.data.DataSupplier;
import com.slt.statistics.data.RunningData;
import com.slt.statistics.data.WalkingData;

import static com.slt.statistics.Activity.*;

/**
 * Created by matze on 06.11.17.
 */

public class GraphBuilder {

    //private LineChart mChart;

    public void setGraphSeries(Activity activity, TimePeriod timePeriod, GraphView graph) {

        DataSupplier dataSupplier = null;

        switch (activity) {
            case WALKING:
                dataSupplier = new WalkingData();
                break;
            case RUNNING:
                dataSupplier = new RunningData();
                break;
            case BIKING:
                dataSupplier = new BikingData();
                break;
            default:
                System.err.println("Error! Activity not recognised.");
                return;
        }





    }

}
