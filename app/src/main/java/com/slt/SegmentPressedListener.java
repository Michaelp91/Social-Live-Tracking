package com.slt;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Usman Ahmad on 02.03.2018.
 */

public class SegmentPressedListener implements View.OnTouchListener{

    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Code goes here
                break;
        }
        return false;
    }
}
