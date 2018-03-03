package com.slt.statistics.data;

import android.content.Context;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.slt.statistics.achievements.ImageItem;

import java.util.ArrayList;

/**
 * Created by matze on 06.11.17.
 */

public interface DataSupplier {

    public PieData getPieData();

    public BarData getBarData(Context c, int timePeriod, int sport);

}
