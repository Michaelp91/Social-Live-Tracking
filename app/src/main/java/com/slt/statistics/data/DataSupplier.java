package com.slt.statistics.data;

import com.jjoe64.graphview.series.DataPoint;
import com.slt.statistics.TimePeriod;

/**
 * Created by matze on 06.11.17.
 */

public interface DataSupplier {

    public DataPoint[] getData(TimePeriod timePeriod);

}
