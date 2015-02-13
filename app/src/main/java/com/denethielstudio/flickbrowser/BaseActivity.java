package com.denethielstudio.flickbrowser;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by neopoliticatv on 12/02/15.
 */
public class BaseActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    public static final String FLICKR_QUERY = "FLICKR_QUERY";

    protected Toolbar activateToolbar(){
        if(mToolbar == null){
            mToolbar = (Toolbar) findViewById(R.id.app_bar);
            if(mToolbar != null){
                setSupportActionBar(mToolbar);
            }
        }
        return mToolbar;
    }

    protected Toolbar activateToolBarWithHomeEnabled(){
        activateToolbar();
        if(mToolbar != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        return mToolbar;
    }
}
