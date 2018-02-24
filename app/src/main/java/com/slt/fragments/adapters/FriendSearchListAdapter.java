package com.slt.fragments.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.slt.R;
import com.slt.control.ApplicationController;
import com.slt.control.DataProvider;
import com.slt.data.User;
import com.slt.restapi.OtherRestCalls;

import java.util.ArrayList;

/**
 * Adapter Class to show the friends for the search page
 */
public class FriendSearchListAdapter extends ArrayAdapter<User> implements View.OnClickListener{

    /**
     * The data we want to show
     */
    private ArrayList<User> dataSet;

    /**
     * The context of the view
     */
    Context mContext;

    /**
     * The position of the last element
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
         * The Picture.
         */
        ImageView picture;
        /**
         * The Add button.
         */
        Button addButton;
    }

    /**
     * Instantiates a new Friend search list adapter.
     *
     * @param data    Rhe data we want to show
     * @param context The context of the view
     */
    public FriendSearchListAdapter(ArrayList<User> data, Context context) {
        super(context, R.layout.friend_search_listitem, data);
        this.dataSet = data;
        this.mContext=context;

    }

    /**
     * Overwritten onClick Method, adds a friend if the button is clicked
     * @param v The view
     */
    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        User dataModel=(User)object;
        ViewHolder viewHolder = new FriendSearchListAdapter.ViewHolder();

        //see of the event is from the button
        switch (v.getId())
        {
            case R.id.friend_search_btn_add:
                Button btn = (Button) v;
                if(btn.getText().toString().compareTo("Add") == 0)
                {
                    DataProvider.getInstance().getOwnUser().addFriend(dataModel);
                    btn.setText("Remove");
                    Snackbar.make(v, "Friend added." , Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                } else {
                    DataProvider.getInstance().getOwnUser().deleteFriend(dataModel);
                    btn.setText("Add");
                    Snackbar.make(v, "Friend removed" , Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
            }

            //REST Call to update UserList
            OtherRestCalls.updateUser(true);
            break;
        }
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
        FriendSearchListAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        //check if we already have a view, if not create it and get the elements
        if (convertView == null) {

            viewHolder = new FriendSearchListAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.friend_search_listitem, parent, false);

            viewHolder.txtUserName = (TextView) convertView.findViewById(R.id.friends_search_item_username);
            viewHolder.txtName  = (TextView) convertView.findViewById(R.id.friends_search_item_name);
            viewHolder.txtEmail  = (TextView) convertView.findViewById(R.id.friends_search_item_email);
            viewHolder.picture = (ImageView) convertView.findViewById(R.id.friends_search_item_image);
            viewHolder.addButton = (Button) convertView.findViewById(R.id.friend_search_btn_add);

            Boolean isFriend = false;

            //retrieve if the user is a friend
            for(User user : DataProvider.getInstance().getOwnUser().getUserList()){
                if(dataModel.getEmail().equals(user.getEmail())){
                    isFriend = true;
                }
            }

            //if he is a friend modify the button
            if(isFriend){
                viewHolder.addButton.setText("Remove");
            }

            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            //if we already have the view
            viewHolder = (FriendSearchListAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }

        //add an animation
        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        //add the data to the view
        viewHolder.txtUserName.setText(dataModel.getUserName());
        String name = dataModel.getForeName() + " " + dataModel.getLastName();
        viewHolder.txtName.setText(name);
        viewHolder.txtEmail.setText(dataModel.getEmail());
        viewHolder.addButton.setOnClickListener(this);
        viewHolder.addButton.setTag(position);

        //if we have an image show it
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
