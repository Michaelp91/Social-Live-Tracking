package com.slt.fragments;

import android.Manifest;
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
import com.slt.control.ApplicationController;
import com.slt.control.DataProvider;
import com.slt.control.SharedResources;
import com.slt.data.User;
import com.slt.restapi.OtherRestCalls;
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
import java.util.Date;
import java.util.Locale;


public class FragmentEditSettings extends Fragment implements ChangePasswordDialog.Listener {

    private static final String TAG = "EditSettingsFragment";
    private SharedPreferences mSharedPreferences;
    private String mToken;
    private String mEmail;
    private View view;
    private EditText usernameEditText;
    private EditText lastNameEditText;
    private EditText foreNameEditText;
    private EditText ageEditText;
    private EditText cityEditText;
    private EditText emailEditText;
    private User ownUser;

    private Bitmap bitmap;

    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;

    private ChangePasswordDialog.Listener mListener;

    private ImageView mProfilePhoto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.edit_settings_fragment, container, false);

        return view;
    }


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
        //TODO might have to add exceptions for handling malformatted strings
        int age = Integer.parseInt(this.ageEditText.getText().toString());
        this.ownUser.setMyAge(age);
        this.ownUser.setForeName(this.foreNameEditText.getText().toString());
        this.ownUser.setLastName(this.lastNameEditText.getText().toString());
        this.ownUser.setMyCity(this.cityEditText.getText().toString());
        this.ownUser.setUserName(this.usernameEditText.getText().toString());


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
        OtherRestCalls.updateUser(false); //TODO: Include Friends Update or not? I don't think so

        SharedResources.getInstance().getNavUsername().setText(DataProvider.getInstance().getOwnUser().getUserName());

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
     * Select image from camera and gallery
     */
    private void selectImage() {
        try {
            if (ContextCompat.checkSelfPermission(view.getContext(),
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
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
                Toast.makeText(ApplicationController.getContext(), "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(ApplicationController.getContext(), "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();


                float aspectRatio = bitmap.getWidth() /
                        (float) bitmap.getHeight();
                int width = 800;
                int height = Math.round(width / aspectRatio);

                bitmap = getResizedBitmap(bitmap, width, height);

                //compress the picture -> reduces the quality to 90%
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                byte[] BYTE = bytes.toByteArray();

                this.bitmap = BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);

                this.bitmap = rotateImageIfRequired(this.bitmap, ApplicationController.getContext(), selectedImage);

                Log.e(TAG, "Picked from Camera");

                mProfilePhoto.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(ApplicationController.getContext().getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();



                float aspectRatio = bitmap.getWidth() /
                        (float) bitmap.getHeight();
                int width = 800;
                int height = Math.round(width / aspectRatio);

                bitmap = getResizedBitmap(bitmap, width, height);

                //compress the picture -> reduces the quality to 90%
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                byte[] BYTE = bytes.toByteArray();

                this.bitmap = BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);

                this.bitmap = rotateImageIfRequired(this.bitmap, ApplicationController.getContext(), selectedImage);

                Log.e(TAG, "Picked from Gallery");
                mProfilePhoto.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static Bitmap rotateImageIfRequired(Bitmap img, Context context, Uri selectedImage) throws IOException {

        if (selectedImage.getScheme().equals("content")) {
            String[] projection = { MediaStore.Images.ImageColumns.ORIENTATION };
            Cursor c = context.getContentResolver().query(selectedImage, projection, null, null, null);
            if (c.moveToFirst()) {
                final int rotation = c.getInt(0);
                c.close();
                return rotateImage(img, rotation);
            }
            return img;
        } else {
            ExifInterface ei = new ExifInterface(selectedImage.getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Log.i( TAG, " orientation: " + orientation);

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

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap( bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Edit Settings");

        view = view;
        mProfilePhoto = (ImageView) view.findViewById(R.id.profile_image);
        this.mListener = this;
        this.ageEditText = (EditText) view.findViewById(R.id.et_age);
        this.usernameEditText = (EditText) view.findViewById(R.id.et_username);
        this.lastNameEditText = (EditText) view.findViewById(R.id.et_last_name);
        this.foreNameEditText = (EditText) view.findViewById(R.id.et_forename);
        this.cityEditText = (EditText) view.findViewById(R.id.et_city);
        this.emailEditText = (EditText) view.findViewById(R.id.et_email);

        //init for the password change dialog
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext());
        mToken = mSharedPreferences.getString(Constants.TOKEN, "");
        mEmail = mSharedPreferences.getString(Constants.EMAIL, "");


        this.ownUser = DataProvider.getInstance().getOwnUser();


        this.ageEditText.setText(Integer.toString(ownUser.getMyAge()));
        this.usernameEditText.setText(ownUser.getUserName());
        this.lastNameEditText.setText(ownUser.getLastName());
        this.foreNameEditText.setText(ownUser.getForeName());
        this.cityEditText.setText(ownUser.getMyCity());
        this.emailEditText.setText(ownUser.getEmail());

        setProfileImage(ownUser.getMyImage());

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

        picture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                selectImage();
            }
        });

        Button save = (Button) view.findViewById(R.id.btn_save);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                storeChanges();
            }
        });

        Button undo = (Button) view.findViewById(R.id.btn_undo);
        undo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                undoChanges();
            }
        });

    }

    @Override
    public void onPasswordChanged() {

        Snackbar.make(view.findViewById(R.id.activity_profile), "Password Changed Successfully !", Snackbar.LENGTH_SHORT).show();
    }

}

