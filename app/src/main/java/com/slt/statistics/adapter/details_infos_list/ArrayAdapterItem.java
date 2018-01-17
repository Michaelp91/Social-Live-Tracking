package com.slt.statistics.adapter.details_infos_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.slt.R;

import java.util.List;

// here's our beautiful adapter
public class ArrayAdapterItem extends ArrayAdapter<ObjectItem> {

    static int blah = 0;
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
        /*
         * The convertView argument is essentially a "ScrapView" as described is Lucas post
         * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
         * It will have a non-null value when ListView is asking you recycle the row layout.
         * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
         */
        //if(convertView==null){
            // inflate the layout
            //convertView = inflater.inflate(layoutResourceId, parent, false);
        View rowView = inflater.inflate(R.layout.details_rowlayout_texts, parent, false);

        //}

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