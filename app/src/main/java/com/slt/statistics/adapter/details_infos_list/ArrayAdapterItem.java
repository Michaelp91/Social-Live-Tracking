package com.slt.statistics.adapter.details_infos_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.slt.R;

import java.util.List;

/**
 * item for the array adapeter for the infobox in each tab fragment
 *
 */
public class ArrayAdapterItem extends ArrayAdapter<ObjectItem> {

    Context mContext;
    List<ObjectItem> data = null;

    public ArrayAdapterItem(Context mContext, List<ObjectItem> data) {

        super(mContext, 0, data);

        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View rowView = inflater.inflate(R.layout.details_rowlayout_texts, parent, false);


        // object item based on the position
        ObjectItem objectItem = data.get(position);

        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView textViewItem = (TextView) rowView.findViewById(R.id.infos_1);
        textViewItem.setText(objectItem.label);

        textViewItem = (TextView) rowView.findViewById(R.id.infos_2);
        textViewItem.setText(objectItem.valueOfLabel);

        return rowView;

    }

}