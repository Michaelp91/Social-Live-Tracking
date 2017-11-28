package com.slt.timelineres;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.slt.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Usman Ahmad on 31.10.2017.
 */

public class ExpandableListAdapter_Timeline extends BaseExpandableListAdapter {
    private Context _context;
    private List<TimelineHeader> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<TimelineHeader, List<Route>> _listDataChild;

    public ExpandableListAdapter_Timeline(Context context, List<TimelineHeader> listDataHeader,
                                 HashMap<TimelineHeader, List<Route>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        //final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.timeline_child, null);
        }

        TimelineHeader key = _listDataHeader.get(groupPosition);
        List<Route> contents = _listDataChild.get(key);
        Route r = contents.get(childPosition);
        Node startNode = r.getStart();
        Node endNode = r.getDestination();

        String station_start = startNode.getStation();
        String address_start = startNode.getAddress();
        String station_end = endNode.getStation();
        String address_end = endNode.getAddress();
        String activity = r.getActivity();
        float duration = r.getDuration();
        float distance = r.getDistance();

        if(r.getPreviousRoute() != null) {
            LinearLayout ll_circle_start = (LinearLayout)convertView.findViewById(R.id.startcircle);
            ll_circle_start.setVisibility(View.GONE);
            station_start = "";
            address_start = "";
        } else {
            LinearLayout ll_circle_start = (LinearLayout)convertView.findViewById(R.id.startcircle);
            ll_circle_start.setVisibility(View.VISIBLE);
        }



        TextView tvStation_start = (TextView) convertView.findViewById(R.id.tv_station);
        TextView tvAddress_start = (TextView) convertView.findViewById(R.id.tv_anschrift);
        TextView tvStation_end = (TextView) convertView.findViewById(R.id.tv_endstation);
        TextView tvAddress_end = (TextView) convertView.findViewById(R.id.tv_endanschrift);
        TextView tvlaufdaten = (TextView) convertView.findViewById(R.id.tv_laufdaten);

        TextView tvActivity = (TextView) convertView.findViewById(R.id.tv_aktivitaet);

        tvActivity.setText(activity);

        tvStation_start.setText(station_start);
        tvAddress_start.setText(address_start);

        tvStation_end.setText(station_end);
        tvAddress_end.setText(address_end);

        tvlaufdaten.setText(Float.toString(duration) + " min\n\n" + Float.toString(distance) + " km");



        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.timeline_header, null);
        }


        TimelineHeader obj = _listDataHeader.get(groupPosition);


        TextView tvDatum = (TextView) convertView
                .findViewById(R.id.tv_datum);
        tvDatum.setTypeface(null, Typeface.BOLD);
        tvDatum.setText(obj.getDatum());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
