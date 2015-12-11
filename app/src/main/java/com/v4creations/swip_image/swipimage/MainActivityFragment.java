package com.v4creations.swip_image.swipimage;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;


public class MainActivityFragment extends Fragment implements View.OnTouchListener {
    private static final String TAG = "MainActivityFragment";
    private static final String IMAGE_PATH_START = "images/DSC_0";
    private static final String IMAGE_PATH_END = ".jpg";
    private static final int IMAGE_START_INDEX = 395;
    private static final int IMAGE_END_INDEX = 418;
    private MainActivity activity;
    private ImageView imageView;
    private Drawable[] imageDrawables;
    private int currentIndex;
    private float baseX;
    private int calculatedSwipeThreshold;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        activity = (MainActivity) getActivity();
        initView(v);
        return v;
    }

    private void initView(View v) {
        v.setOnTouchListener(this);
        imageView = (ImageView) v.findViewById(R.id.iv);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new ImageLoader().execute();
    }

    private void start3d() {
        currentIndex = 0;
        calculatedSwipeThreshold = getSwipeTreshold();
        loadImage();
    }

    private void loadImage() {
        imageView.setImageDrawable(imageDrawables[currentIndex]);
    }

    private int getDisplayWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                baseX = event.getX();
                return true;
            case MotionEvent.ACTION_MOVE:
                float xDelta = event.getX() - baseX;
                if(xDelta > calculatedSwipeThreshold){
                    move((int)(xDelta/calculatedSwipeThreshold));
                    baseX = event.getX();
                }else if(xDelta < -calculatedSwipeThreshold){
                    move((int)(xDelta/calculatedSwipeThreshold));
                    baseX = event.getX();
                }
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                return true;
        }
        return true;
    }

    private void move(int step) {
        currentIndex -= step;
        if(currentIndex >= imageDrawables.length){
            currentIndex -= imageDrawables.length;
        }else if(currentIndex < 0){
            currentIndex = imageDrawables.length + currentIndex;
        }
        loadImage();
    }

    private int getSwipeTreshold() {
        return getDisplayWidth()/imageDrawables.length;
    }

    private class ImageLoader extends AsyncTask<Void, Float, Drawable[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            activity.showProgress();
        }

        private Drawable getImageDrawable(int id) {
            Drawable result = null;
            try {
                InputStream ims = activity.getAssets().open(IMAGE_PATH_START + id + IMAGE_PATH_END);
                result = Drawable.createFromStream(ims, null);
            } catch (IOException ex) {
            }
            return result;
        }

        @Override
        protected Drawable[] doInBackground(Void... voids) {
            Drawable[] drawables = new Drawable[IMAGE_END_INDEX - IMAGE_START_INDEX + 1];
            for (int i = IMAGE_START_INDEX; i <= IMAGE_END_INDEX; i++) {
                Drawable d = getImageDrawable(i);
                if (d == null) {
                    drawables = null;
                    break;
                } else
                    drawables[i - IMAGE_START_INDEX] = d;
            }
            return drawables;
        }

        @Override
        protected void onPostExecute(Drawable[] drawables) {
            super.onPostExecute(drawables);
            if (drawables == null) {
                activity.showToast("Faied to load images");
            } else {
                imageDrawables = drawables;
                start3d();
            }
            activity.hideProgress();
        }
    }

    private void log(String message) {
        Log.e(TAG, message);
    }
}
