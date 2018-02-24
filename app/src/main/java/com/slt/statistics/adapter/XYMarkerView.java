package com.slt.statistics.adapter;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.slt.R;
import com.slt.statistics.Sport;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
public class XYMarkerView extends MarkerView {

    private TextView tvContent;
    private IAxisValueFormatter xAxisValueFormatter;

    private DecimalFormat format;
    private int period = -1;

    public XYMarkerView(Context context, IAxisValueFormatter xAxisValueFormatter, int period) {
        super(context, R.layout.custom_marker_view);

        this.period = period;
        this.xAxisValueFormatter = xAxisValueFormatter;
        tvContent = (TextView) findViewById(R.id.tvContent);
        format = new DecimalFormat("###");
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        String  str = ": ";
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int month = cal.get(Calendar.MONTH);

        switch(this.period) {
            case 0:
                str = " Uhr: ";
                break;
            case 1:
                str = ". " + getMonthForInt(month) + ": ";
                break;
            case 2:
                str = ". " + getMonthForInt(month) + ": ";
                break;
            default:
                str = ": ";
                System.err.println("No such period.");
        }

        tvContent.setText(xAxisValueFormatter.getFormattedValue(e.getX(), null) + str + (int) e.getY());


        super.refreshContent(e, highlight);
    }

    private String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
