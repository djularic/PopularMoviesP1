package com.jularic.dominik.popularmoviesp1;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class DetailActivity extends AppCompatActivity {

    Toolbar mToolbar;
    private View.OnClickListener toolbarBackButtonListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            NavUtils.navigateUpFromSameTask(DetailActivity.this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if(mToolbar != null){
            mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            mToolbar.setNavigationOnClickListener(toolbarBackButtonListener);
        }
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().add(R.id.activity_detail_container, new DetailActivityFragment()).commit();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean handled = true;
        //int filterOption = Filter.NONE;
        switch (id){
            case R.id.action_settings:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                break;
            default:
                handled = false;
                break;
        }
        return handled;
    }
}
