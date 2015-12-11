package com.v4creations.swipe_image;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.please_wait));
        progress.setCancelable(false);
    }

    public void showProgress() {
        progress.show();
    }

    public void hideProgress() {
        progress.dismiss();
    }

    public void showToast(int s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
