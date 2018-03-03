package com.slt.statistics.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.slt.R;
import com.slt.statistics.achievements.ImageItem;

import java.util.ArrayList;

/**
 * Created by matze on 06.11.17.
 */

public class MainDataSupplier implements DataSupplier {

    PieData pieData = null;
    LineData lineData = null;
    BarData barData = null;


    @Override
    public PieData getPieData() {
        if (pieData == null)
            pieData = StatisticsDataModelProvider.generateDataPie(1);

        return pieData;
    }



    @Override
    public BarData getBarData(Context c,  int timePeriod, int sport) {
        // if (lineData == null)
        barData = StatisticsDataModelProvider.getBarData(c, timePeriod, sport);

        return barData;
    }




}
