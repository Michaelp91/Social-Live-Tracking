package com.slt.fragments.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.util.Log;
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
 * List Adapter for showing a friend
 */
public class FriendListAdapter extends ArrayAdapter<User> implements View.OnClickListener{

    /**
     * The data that should be shown
     */
    private ArrayList<User> dataSet;

    /**
     * The context of the view
     */
    Context mContext;

    /**
     * The last position in the list
     */
    private int lastPosition = -1;

    // View lookup cache
    private static class ViewHolder {
        /**
         * The user name.
         */
        TextView txtUserName;
        /**
         * The name.
         */
        TextView txtName;
        /**
         * The email.
         */
        TextView txtEmail;
        /**
         * The age.
         */
        TextView txtAge;
        /**
         * The Picture.
         */
        ImageView picture;
    }


    /**
     * Instantiates a new Friend list adapter.
     *
     * @param data    The data that should be shown
     * @param context The context of the view
     */
    public FriendListAdapter(ArrayList<User> data, Context context) {
        super(context, R.layout.friends_listitem, data);
        this.dataSet = data;
        this.mContext=context;
    }

    /**
     * Overwritten onClick Method, does nothing
     * @param v The view
     */
    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        User dataModel=(User)object;
    }


    /**
     * Overwritten getView Method, adds the data to the list item
     * @param position The position of the item
     * @param convertView The view
     * @param parent The ViewGroup
     * @return The created View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User dataModel = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        //if we do not have a view create it and get the resources
        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.friends_listitem, parent, false);

            viewHolder.txtUserName = (TextView) convertView.findViewById(R.id.friends_list_item_username);
            viewHolder.txtName  = (TextView) convertView.findViewById(R.id.friends_list_item_name);
            viewHolder.txtEmail  = (TextView) convertView.findViewById(R.id.friends_list_item_email);
            viewHolder.txtAge  = (TextView) convertView.findViewById(R.id.friends_list_item_age);
            viewHolder.picture = (ImageView) convertView.findViewById(R.id.friends_list_item_image);

            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            //get the view
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        //add a animation for scrolling
        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        //add the data
        viewHolder.txtUserName.setText(dataModel.getUserName());
        String name = dataModel.getForeName() + " " + dataModel.getLastName();
        viewHolder.txtName.setText(name);
        viewHolder.txtEmail.setText(dataModel.getEmail());
        viewHolder.txtAge.setText(Integer.toString(dataModel.getMyAge()));

        //if we have a picture show it
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