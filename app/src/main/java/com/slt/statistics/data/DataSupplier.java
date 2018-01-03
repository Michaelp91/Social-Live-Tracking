package com.slt.statistics.data;

import android.content.Context;

import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.jjoe64.graphview.series.DataPoint;
import com.slt.statistics.TimePeriod;
import com.slt.statistics.achievements.ImageItem;

import java.util.ArrayList;

/**
 * Created by matze on 06.11.17.
 */

public interface DataSupplier {

    public PieData getPieData(int timePeriod, String walking);

    public LineData getLineData(Context context, int timePeriod, String walking);

    public ArrayList<ImageItem> getImageItems(Context context, String achivement);
}
