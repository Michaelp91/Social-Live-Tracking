package com.slt.statistics.data;

import com.jjoe64.graphview.series.DataPoint;
import com.slt.statistics.TimePeriod;

/**
 * Created by matze on 06.11.17.
 */

public class WalkingData implements DataSupplier {

    @Override
    public DataPoint[] getData(TimePeriod timePeriod) {
        DataPoint[] dataPoints = new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        };


        return dataPoints;

    }
}
