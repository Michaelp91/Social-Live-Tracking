package com.slt.restapi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.slt.TimelineActivity;
import com.slt.data.User;
import com.slt.restapi.data.FullTimeLine;
import com.slt.restapi.data.Image;
import com.slt.restapi.data.REST_User_Functionalities;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Usman Ahmad on 07.01.2018.
 */

public class UsefulMethods {

    public static Bitmap LoadImage(User user) {
        if (user.getMyImageName() == null) {
            return null;
        }

        if(!user.getMyImageName().equals("")  ) {

            REST_User_Functionalities r_u_f = TemporaryDB.getInstance().h_users.get(user);
            Image imageObj = new Image();
            imageObj.filename = r_u_f.myImage;

            Endpoints api = RetroClient.getApiService();
            Call<JsonObject> call = api.downloadPicture(imageObj);

            JsonObject jsonObject = null;
            try {
                jsonObject = call.execute().body();
            } catch (Exception e) {
                return null;
            }

            if (jsonObject != null) {
                String json = jsonObject.toString();
                Image image = new Gson().fromJson(json, Image.class);
                byte[] decodedString = Base64.decode(image.string, Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                return bmp;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static boolean UploadImageView(Bitmap bitmap, String imagename) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageInByte = baos.toByteArray();
        Image imageObj = new Image();
        imageObj.imageByteArray = imageInByte;
        imageObj.filename = imagename;

        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.uploadPicture(imageObj);

        JsonObject jsonObject = null;
        try {
            jsonObject =  call.execute().body();
        } catch (Exception e) {
            return false; //Request is not Successfull
        }

        return true;
    }
}
