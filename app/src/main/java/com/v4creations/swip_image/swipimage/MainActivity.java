package com.v4creations.swip_image.swipimage;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
        progress.setMessage("Please wait...");
        progress.setCancelable(false);
    }

    public void showProgress(){
        progress.show();
    }

    public void hideProgress(){
        progress.dismiss();
    }
    public void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
