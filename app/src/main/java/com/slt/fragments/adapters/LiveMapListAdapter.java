package com.slt.fragments.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.slt.R;
import com.slt.control.ApplicationController;
import com.slt.data.User;

import java.util.ArrayList;

/**
 * Created by Thorsten on 06.01.2018.
 */
public class LiveMapListAdapter extends ArrayAdapter<User> implements View.OnClickListener{


    private ArrayList<User> dataSet;
    private Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtUserName;
        TextView txtEmail;
        ImageView picture;
    }


    public LiveMapListAdapter(ArrayList<User> data, Context context) {
        super(context, R.layout.live_map_listitem, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        User dataModel=(User)object;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User dataModel = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.live_map_listitem, parent, false);

            viewHolder.txtUserName = (TextView) convertView.findViewById(R.id.live_map_list_item_username);
            viewHolder.txtEmail  = (TextView) convertView.findViewById(R.id.live_map_list_item_email);
            viewHolder.picture = (ImageView) convertView.findViewById(R.id.live_map_list_item_image);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtUserName.setText(dataModel.getUserName());
        viewHolder.txtEmail.setText(dataModel.getEmail());


        if(dataModel.getMyImage() == null) {
            Bitmap image = BitmapFactory.decodeResource(ApplicationController.getContext().getResources(), R.drawable.profile_pic);
            viewHolder.picture.setImageBitmap(image);
        }
        else {
            viewHolder.picture.setImageBitmap(dataModel.getMyImage());
        }

        // Return the completed view to render on screen
        return convertView;
    }
}