package com.slt.fragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.slt.R;
import com.slt.data.User;
import java.util.ArrayList;



/**
 * List Adapter for showing a ranking
 */
public class RankingListAdapter extends ArrayAdapter<User> {

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


    /**
     * value for id of activity
     */
    int id;

    /**
     * value for the period of distance we want to show *Month/Week*
     */
    int period;



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
        TextView txtAchievement;
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
     * @param data The data that should be shown
     * @param context The context of the view
     * @param id The ID of activity
     * @param period The period of
     *
     */
    public RankingListAdapter(ArrayList<User> data, Context context, int id, int period) {
        super(context, R.layout.ranking_listitem, data);
        this.dataSet = data;
        this.mContext=context;
        this.id = id;
        this.period = period;


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
            convertView = inflater.inflate(R.layout.ranking_listitem, parent, false);

            viewHolder.txtUserName = (TextView) convertView.findViewById(R.id.ranking_list_item_username);
            viewHolder.txtName  = (TextView) convertView.findViewById(R.id.ranking_list_item_name);
            viewHolder.txtAchievement  = (TextView) convertView.findViewById(R.id.ranking_list_item_achievement);
            viewHolder.txtAge  = (TextView) convertView.findViewById(R.id.ranking_list_item_place);
            viewHolder.picture = (ImageView) convertView.findViewById(R.id.ranking_list_item_image);

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

        //Choose period for Active Distance
        if (period == 0) {
            viewHolder.txtAchievement.setText( String.valueOf( dataModel.getMyTimeline().getActiveDistanceForMonth( id ) ) );
        }

        else if (period == 1){
            viewHolder.txtAchievement.setText( String.valueOf( dataModel.getMyTimeline().getActiveDistanceForWeek( id ) ) );
        }
        viewHolder.txtAge.setText(String.valueOf(dataModel.getRank()));

        //if user have a picture show it
        if(dataModel.getMyImage() != null){
            viewHolder.picture.setImageBitmap(dataModel.getMyImage());
        } else
        {
//            Bitmap image = decodeSampledBitmapFromResource(ApplicationController.getContext().getResources(), R.drawable.profile_pic_small,96,96);
//            viewHolder.picture.setImageBitmap(image);
              viewHolder.picture.setImageResource( R.drawable.profile_pic_small );
        }

        // Return the completed view to render on screen
        return convertView;
    }
}