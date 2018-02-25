package com.slt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.slt.control.DataProvider;
import com.slt.data.TimelineSegment;

import java.util.LinkedList;

public class SegmentViewActivity extends AppCompatActivity {

    private LinkedList<TimelineSegment> choosedTimelineSegments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segment_view);
        this.choosedTimelineSegments = DataProvider.getInstance().getChoosedTimelineSegments();
    }
}
