package com.slt.statistics.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.slt.R;
import com.slt.statistics.Sport;
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
            pieData = TestDataGenerator_toBeRemoved.generateDataPie(1);

        return pieData;
    }

    @Override
    public LineData getLineData(Context context, int timePeriod, Sport sport) {
       // if (lineData == null)
            lineData = TestDataGenerator_toBeRemoved.generateDataLine(context, timePeriod, sport);

        return lineData;
    }

    @Override
    public BarData getBarData(Context c,  int timePeriod, Sport sport) {
        // if (lineData == null)
        barData = TestDataGenerator_toBeRemoved.getBarData(c, timePeriod, sport);

        return barData;
    }

    @Override
    public ArrayList<ImageItem> getImageItems(Context context, String achivement) {


      final ArrayList<ImageItem> imageItems = new ArrayList<>();
        //TypedArray imgs = context.getResources().obtainTypedArray(R.array.image_ids);
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(R.drawable.running_cup);
        ids.add(R.drawable.walking_cup);
        ids.add(R.drawable.running_cup);
        ids.add(R.drawable.walking_cup);
        ids.add(R.drawable.running_cup);
        ids.add(R.drawable.walking_cup);
        for (int i = 0; i < ids.size(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), ids.get(i));
            imageItems.add(new ImageItem(bitmap, "Image#" + i));
        }
        return imageItems;
    //  return new ArrayList<>();
    }


}
