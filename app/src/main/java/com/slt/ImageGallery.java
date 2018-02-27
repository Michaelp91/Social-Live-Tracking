package com.slt;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.slt.control.SharedResources;

import java.util.LinkedList;
import java.util.zip.Inflater;

public class ImageGallery extends AppCompatActivity {

    private LinkedList<Bitmap> segmentBitmaps;
    private LinearLayout ll_gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);
        ll_gallery = (LinearLayout) findViewById(R.id.ll_gallery);
        segmentBitmaps = SharedResources.getInstance().getSegmentBitmaps();
        ShowImages();
    }

    private void ShowImages() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        ImageView[] ivPics = new ImageView[2];
        int pos = 2;
        boolean bothImageViewsViewed = false;
        LinearLayout llayout = null;
        for(Bitmap bmp: segmentBitmaps) {

            if(pos == 2) {

                if(llayout != null)
                    ll_gallery.addView(llayout);

                llayout = (LinearLayout) inflater.inflate(R.layout.row_image_gallery, null);
                ivPics = new ImageView[2];
                ivPics[0] = (ImageView) llayout.findViewById(R.id.iv_pic1);
                ivPics[1] = (ImageView) llayout.findViewById(R.id.iv_pic2);
                pos = 0;
                ivPics[pos].setImageBitmap(bmp);
                pos++;
            } else {
                ivPics[pos].setImageBitmap(bmp);
                pos++;
            }

        }

        if(llayout != null)
            ll_gallery.addView(llayout);

    }
}
