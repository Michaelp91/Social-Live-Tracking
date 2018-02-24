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
 * The adapter show a short list of friends for the timeline view of the friends
 */
public class LiveMapListAdapter extends ArrayAdapter<User> implements View.OnClickListener{

    /**
     * The data we want to show
     */
    private ArrayList<User> dataSet;

    /**
     *  The context of the view
     */
    private Context mContext;

    /**
     * The last position of the data
     */
    private int lastPosition = -1;

    // View lookup cache
    private static class ViewHolder {
        /**
         * The user name.
         */
        TextView txtUserName;
        /**
         * The email
         */
        TextView txtEmail;
        /**
         * The Picture
         */
        ImageView picture;
    }

    /**
     * Instantiates a new Live map list adapter.
     *
     * @param data    The data we want to show
     * @param context The context of the view
     */
    public LiveMapListAdapter(ArrayList<User> data, Context context) {
        super(context, R.layout.live_map_listitem, data);
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

        //if we do not have a view yet, create it and get the elements
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
            //if we already have the view
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        //add an animation
        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        //get and add the data to the view
        viewHolder.txtUserName.setText(dataModel.getUserName());
        viewHolder.txtEmail.setText(dataModel.getEmail());

        //if we have an Image show it
        if(dataModel.getMyImage() == null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap image = BitmapFactory.decodeResource(ApplicationController.getContext().getResources(), R.drawable.profile_pic, options);
            viewHolder.picture.setImageBitmap(image);
        }
        else {
            viewHolder.picture.setImageBitmap(dataModel.getMyImage());
        }

        // Return the completed view to render on screen
        return convertView;
    }
}