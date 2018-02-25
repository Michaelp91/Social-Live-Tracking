package com.slt.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.slt.MainActivity;
import com.slt.MainProfile;
import com.slt.control.ApplicationController;
import com.slt.control.DataProvider;
import com.slt.control.SharedResources;
import com.slt.data.Timeline;
import com.slt.data.TimelineDay;
import com.slt.data.TimelineSegment;
import com.slt.data.User;
import com.slt.restapi.OtherRestCalls;
import com.slt.restapi.RetrieveOperations;
import com.slt.restapi.UsefulMethods;
import com.slt.utils.Constants;
import com.slt.utils.UniversalImageLoader;


import com.slt.R;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;


/**
 * The Class for the edit settings fragment
 */
public class FragmentEditSettings extends Fragment implements ChangePasswordDialog.Listener {

    private Activity context;

    /**
     * TAG for the debugger
     */
    private static final String TAG = "EditSettingsFragment";

    /**
     * Element for the shared preferences
     */
    private SharedPreferences mSharedPreferences;

    /**
     * Element for the token
     */
    private String mToken;

    /**
     * Element for the email
     */
    private String mEmail;

    /**
     * Element for the view
     */
    private View view;

    /**
     * Element for the username
     */
    private EditText usernameEditText;

    /**
     * element for the last name
     */
    private EditText lastNameEditText;

    /**
     * Element for the forename
     */
    private EditText foreNameEditText;

    /**
     * Element for the age
     */
    private EditText ageEditText;

    /**
     * Element for the city
     */
    private EditText cityEditText;

    /**
     * Element for the email
     */
    private EditText emailEditText;

    /**
     * Our application user
     */
    private User ownUser;

    /**
     * The image of the user
     */
    private Bitmap bitmap;

    /**
     * Options for the user to select image
     */
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;

    /**
     * Listener for the button to open password change dialog
     */
    private ChangePasswordDialog.Listener mListener;

    /**
     * The image view of the user
     */
    private ImageView mProfilePhoto;

    private Button mDeleteUser;

    /**
     * Overwritten onCreateView
     * @param inflater The layout inflater
     * @param container The View Group
     * @param savedInstanceState The saved instance state
     * @return The created view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.edit_settings_fragment, container, false);

        context = this.getActivity();

        return view;
    }

    /**
     * Remove all user changes
     */
    private void undoChanges() {
        this.ageEditText.setText(Integer.toString(ownUser.getMyAge()));
        this.usernameEditText.setText(ownUser.getUserName());
        this.lastNameEditText.setText(ownUser.getLastName());
        this.foreNameEditText.setText(ownUser.getForeName());
        this.cityEditText.setText(ownUser.getMyCity());

        setProfileImage(ownUser.getMyImage());
    }

    /**
     * Updates the data object
     */
    private void storeChanges() {
        //add data to the page
        int age = Integer.parseInt(this.ageEditText.getText().toString());
        this.ownUser.setMyAge(age);
        this.ownUser.setForeName(this.foreNameEditText.getText().toString());
        this.ownUser.setLastName(this.lastNameEditText.getText().toString());
        this.ownUser.setMyCity(this.cityEditText.getText().toString());
        this.ownUser.setUserName(this.usernameEditText.getText().toString());

        //if we have a bitmap
        if(bitmap != null) {
            ownUser.setMyImage(bitmap);

            //REST Call to update user in DB
            String photo = ownUser.getEmail().replace('@', '_').replace('.', '_');
            photo += ".jpeg";
            ownUser.setMyImageName(photo);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean uploadSuccessfull = UsefulMethods.UploadImageView(bitmap, ownUser.getMyImageName());

                    if (!uploadSuccessfull) {
                        Toast.makeText(ApplicationController.getContext(), "Image Upload failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }).start();
        }
        OtherRestCalls.updateUser(false);

        SharedResources.getInstance().getNavUsername().setText(DataProvider.getInstance().getOwnUser().getUserName());

        //if we now do not have an image any more use defaut
        if (bitmap == null) {
            Bitmap image = BitmapFactory.decodeResource(ApplicationController.getContext().getResources(), R.drawable.profile_pic);
            SharedResources.getInstance().getNavProfilePhoto().setImageBitmap(image);
        } else {
            SharedResources.getInstance().getNavProfilePhoto().setImageBitmap(DataProvider.getInstance().getOwnUser().getMyImage());
        }

        this.setProfileImage(DataProvider.getInstance().getOwnUser().getMyImage());

        Toast.makeText(ApplicationController.getContext(), "Userdata updated", Toast.LENGTH_SHORT).show();
    }

    /**
     * Select image from camera or gallery
     */
    private void selectImage() {
        try {
            //check if permission is set
            if (ContextCompat.checkSelfPermission(view.getContext(),
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                //show user dialog with options
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(view.getContext());
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                //if permissions not set inform user
                Toast.makeText(ApplicationController.getContext(), "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(ApplicationController.getContext(), "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * Overwritten Method onActivityResult
     * @param requestCode The request code
     * @param resultCode The result code
     * @param data The intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if image was selected from camera
        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                //get the image
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                //resize the image
                float aspectRatio = bitmap.getWidth() /
                        (float) bitmap.getHeight();
                int width = 800;
                int height = Math.round(width / aspectRatio);
                bitmap = getResizedBitmap(bitmap, width, height);

                //compress the picture -> reduces the quality to 90%
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                byte[] BYTE = bytes.toByteArray();

                //get a compressed bitmap
                this.bitmap = BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);

                //rotate image if required since we lost the image data in the last step
                this.bitmap = rotateImageIfRequired(this.bitmap, ApplicationController.getContext(), selectedImage);

                Log.e(TAG, "Picked from Camera");

                //add image to data
                mProfilePhoto.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            //if image was picked from the gallery
            Uri selectedImage = data.getData();
            try {
                //get image
                bitmap = MediaStore.Images.Media.getBitmap(ApplicationController.getContext().getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                //rescale the image
                float aspectRatio = bitmap.getWidth() /
                        (float) bitmap.getHeight();
                int width = 800;
                int height = Math.round(width / aspectRatio);
                bitmap = getResizedBitmap(bitmap, width, height);

                //compress the picture -> reduces the quality to 90%
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                byte[] BYTE = bytes.toByteArray();

                //get a new bitmap
                this.bitmap = BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);

                //rotate image if required since we lost the image data in the last step
                this.bitmap = rotateImageIfRequired(this.bitmap, ApplicationController.getContext(), selectedImage);

                Log.e(TAG, "Picked from Gallery");
                mProfilePhoto.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Rotate image if required
     *
     * @param img           The bitmap to rotate
     * @param context       The context
     * @param selectedImage The selected image URI
     * @return The bitmap rotated if needed
     * @throws IOException IO exception for errors
     */
    public static Bitmap rotateImageIfRequired(Bitmap img, Context context, Uri selectedImage) throws IOException {

        //if the scheme is content
        if (selectedImage.getScheme().equals("content")) {
            //get information
            String[] projection = { MediaStore.Images.ImageColumns.ORIENTATION };
            Cursor c = context.getContentResolver().query(selectedImage, projection, null, null, null);
            if (c.moveToFirst()) {
                //get rotation and rotate image as needed
                final int rotation = c.getInt(0);
                c.close();
                return rotateImage(img, rotation);
            }
            return img;
        } else {
            //get exif information
            ExifInterface ei = new ExifInterface(selectedImage.getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Log.i( TAG, " orientation: " + orientation);

            //get the orientation from the data and rotate as required
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        }
    }

    /**
     * Rotates the bitmap
     * @param img The bitmap to totate
     * @param degree The degree we want to rotate ot
     * @return The rotated image
     */
    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }


    /**
     * Gets resized bitmap
     *
     * @param bm        the bitmap
     * @param newWidth  the new width
     * @param newHeight the new height
     * @return the resized bitmap
     */
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matix
        Matrix matrix = new Matrix();

        //resize the bitmap
        matrix.postScale(scaleWidth, scaleHeight);

        //create a new bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap( bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    /**
     * Method to show a profile image
     * @param img
     */
    private void setProfileImage(Bitmap img) {
        Log.d(TAG, "setProfileImage: setting profile image.");
        //check if we have a picture to show, if not default is shown
        if (img == null) {
            Bitmap image = BitmapFactory.decodeResource(ApplicationController.getContext().getResources(), R.drawable.profile_pic);
            this.mProfilePhoto.setImageBitmap(image);
        } else {
            this.mProfilePhoto.setImageBitmap(img);
        }
    }

    /**
     * Overwritten onViewCreated Method, initializes the elements
     * @param view The view
     * @param savedInstanceState The saved instance state
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Edit Settings");

        //get an initialize the elements
        view = view;
        mProfilePhoto = (ImageView) view.findViewById(R.id.profile_image);
        this.mListener = this;
        this.ageEditText = (EditText) view.findViewById(R.id.et_age);
        this.usernameEditText = (EditText) view.findViewById(R.id.et_username);
        this.lastNameEditText = (EditText) view.findViewById(R.id.et_last_name);
        this.foreNameEditText = (EditText) view.findViewById(R.id.et_forename);
        this.cityEditText = (EditText) view.findViewById(R.id.et_city);
        this.emailEditText = (EditText) view.findViewById(R.id.et_email);
        this.mDeleteUser = (Button) view.findViewById(R.id.btn_deleteUser);

        //init for the password change dialog
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext());
        mToken = mSharedPreferences.getString(Constants.TOKEN, "");
        mEmail = mSharedPreferences.getString(Constants.EMAIL, "");

        this.ownUser = DataProvider.getInstance().getOwnUser();

        //set the shown information
        this.ageEditText.setText(Integer.toString(ownUser.getMyAge()));
        this.usernameEditText.setText(ownUser.getUserName());
        this.lastNameEditText.setText(ownUser.getLastName());
        this.foreNameEditText.setText(ownUser.getForeName());
        this.cityEditText.setText(ownUser.getMyCity());
        this.emailEditText.setText(ownUser.getEmail());

        //show the photo if we have one
        setProfileImage(ownUser.getMyImage());

        //add a listener to handle a click on the change password button and show a dialog
        Button pwd = (Button) view.findViewById(R.id.et_password);
        pwd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ChangePasswordDialog fragment = new ChangePasswordDialog();
                fragment.setListener(mListener);

                Bundle bundle = new Bundle();
                bundle.putString(Constants.EMAIL, mEmail);
                bundle.putString(Constants.TOKEN, mToken);
                fragment.setArguments(bundle);

                fragment.show(getFragmentManager(), ChangePasswordDialog.TAG);

            }
        });

        Button picture = (Button) view.findViewById(R.id.btn_change_photo);

        //add listener to show a select image dialog
        picture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                selectImage();
            }
        });

        Button save = (Button) view.findViewById(R.id.btn_save);

        //add a listener to store the changes
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                storeChanges();
            }
        });

        Button undo = (Button) view.findViewById(R.id.btn_undo);

        //add a listener to undo the changes
        undo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                undoChanges();
            }
        });


        mDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        });

    }

    private void deleteUser() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Timeline t = DataProvider.getInstance().getUserTimeline();

                LinkedList<TimelineDay> timelineDays = t.getTimelineDays();

                for(TimelineDay t_d: timelineDays) {
                    LinkedList<TimelineSegment> timelineSegments = t_d.getMySegments();
                    for(TimelineSegment t_s: timelineSegments) {
                        OtherRestCalls.deleteLocationEntriesByTimelineSegment(t_s);
                    }

                    OtherRestCalls.deleteTimelineSegmentByTimelineDay(t_d);
                }
                    OtherRestCalls.deleteTimelineDayByTimeline(t);
                    OtherRestCalls.deleteTimeline(DataProvider.getInstance().getOwnUser());
                    OtherRestCalls.deleteUser_Functionalities(DataProvider.getInstance().getOwnUser());
                    OtherRestCalls.deleteUsers(DataProvider.getInstance().getOwnUser());
                    ArrayList<User> allUsers = RetrieveOperations.getInstance().retrieveAllUserLists();

                    for(User u: allUsers) {
                        u.deleteFriend(DataProvider.getInstance().getOwnUser());
                        OtherRestCalls.updateUser(true);
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "The User is deleted successfully", Toast.LENGTH_SHORT).show();
                            context.finish();
                        }
                    });
            }
        }).start();
    }

    /**
     * Overwritten onPasswordChangedMethod,simply show a feedback to the user
     */
    @Override
    public void onPasswordChanged() {

        Snackbar.make(view.findViewById(R.id.activity_profile), "Password Changed Successfully !", Snackbar.LENGTH_SHORT).show();
    }

}

