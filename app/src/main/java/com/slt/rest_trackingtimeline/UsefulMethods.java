package com.slt.rest_trackingtimeline;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.slt.rest_trackingtimeline.data.FullTimeLine;
import com.slt.rest_trackingtimeline.data.Image;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Usman Ahmad on 07.01.2018.
 */

public class UsefulMethods {
    public static void UploadImageView(ImageView imageView) {
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) imageView.getDrawable());
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageInByte = baos.toByteArray();
        Image imageObj = new Image();
        imageObj.imageByteArray = imageInByte;

        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.uploadPicture(imageObj);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String json = response.body().toString();
                //Type listType = new TypeToken<List<Test>>() {}.getType();
                Singleton test = new Gson().fromJson(json, Singleton.class);
                FullTimeLine responses = test.getResponses();
                TemporaryDB.getInstance().setLocationEntries(responses.locationEntries);
                TemporaryDB.getInstance().setTimelineDays(responses.timelinedays);
                TemporaryDB.getInstance().setTimeline(responses.timeline);
                TemporaryDB.getInstance().setTimeLineSegments(responses.timelinesegments);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                boolean debug = true;
            }
        });
    }
}
