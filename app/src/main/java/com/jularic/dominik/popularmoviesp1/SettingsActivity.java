package com.jularic.dominik.popularmoviesp1;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    Toolbar mToolbar;
    private View.OnClickListener toolbarBackButtonListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            NavUtils.navigateUpFromSameTask(SettingsActivity.this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        if(mToolbar != null){
            mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            mToolbar.setTitle("Settings");
            mToolbar.setNavigationOnClickListener(toolbarBackButtonListener);
        }

    }
}
