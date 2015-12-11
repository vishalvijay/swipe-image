package com.v4creations.swip_image.swipimage;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;


public class MainActivityFragment extends Fragment implements View.OnTouchListener {
    private static String TAG = "MainActivityFragment";
    private static final int VIDEO_NAME = R.raw.test;
    private VideoView mVideoView;
    private MainActivity activity;

    private float x1, x2;
    static final int MIN_DISTANCE = 150;

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
        mVideoView = (VideoView) v.findViewById(R.id.btHello);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadAndPlayVideo();
    }

    private void loadAndPlayVideo() {
        activity.showProgress();
        try {
            mVideoView.setMediaController(null);
            mVideoView.setVideoURI(getVideoUri());
        } catch (Exception e) {
            activity.hideProgress();
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        mVideoView.requestFocus();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                activity.hideProgress();
                mVideoView.start();
                mVideoView.pause();
            }
        });

    }

    private Uri getVideoUri() {
        String videoSource = "android.resource://" + getActivity().getPackageName() + "/" + VIDEO_NAME;
        return Uri.parse(videoSource);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    if (x2 > x1) {
                        swipeRight();
                    } else {
                        swipeLeft();
                    }
                }
                break;
        }
        return true;
    }

    private void swipeRight() {
        logPosition();
        int newPosition = mVideoView.getCurrentPosition() + getSeekValue();
        int duration = mVideoView.getDuration();
        if (newPosition > duration)
            newPosition = newPosition % duration;
        mVideoView.seekTo(newPosition);
    }

    private void swipeLeft() {
        logPosition();
        int newPosition = mVideoView.getCurrentPosition() - getSeekValue();
        if (newPosition < 0)
            newPosition += mVideoView.getDuration();
        mVideoView.seekTo(newPosition);
    }

    private int getSeekValue() {
        return mVideoView.getDuration() / 10;
    }

    private void logPosition() {
        Log.e(TAG, "Current position : " + mVideoView.getCurrentPosition()+", Duration : " + mVideoView.getDuration()+", Seek value : " + getSeekValue());
    }

}
