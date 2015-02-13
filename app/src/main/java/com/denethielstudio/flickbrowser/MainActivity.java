package com.denethielstudio.flickbrowser;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements SearchView.OnQueryTextListener {

    private static final String LOG_TAG = "MainActivity";
    private List<Photo> mPhotoList = new ArrayList<Photo>();
    private RecyclerView mRecyclerView;
    private FlickrRecyclerViewAdapter flickrRecyclerViewAdapter;

    private SearchView mSearchView;
    private TextView mStatusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Prueba
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        //
        setContentView(R.layout.activity_main);
        activateToolbar();
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        sharedPref.edit().putString(FLICKR_QUERY,"android,lollipop").commit();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        flickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter(MainActivity.this,new ArrayList<Photo>());

        mRecyclerView.setAdapter(flickrRecyclerViewAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,mRecyclerView,new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this,"Normal Tap",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(MainActivity.this,"Long Tap",Toast.LENGTH_LONG).show();


            }
        }));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        setupSearchView(searchItem);
        return true;
    }

    private void setupSearchView(MenuItem searchItem){
        if(isAlwaysExpanded()){
            mSearchView.setIconifiedByDefault(false);
        }else{
            searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        }

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if(searchManager != null){
            List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();
            SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
            for(SearchableInfo inf : searchables){
                if(inf.getSuggestAuthority() != null && inf.getSuggestAuthority().startsWith("applications")){
                    info = inf;
                }
                mSearchView.setSearchableInfo(info);
            }
            mSearchView.setOnQueryTextListener(this);

        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPref.edit().putString(FLICKR_QUERY,s).commit();
        ProcessPhotos processPhotos = new ProcessPhotos(s,true);
        processPhotos.execute();
        mSearchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    protected boolean isAlwaysExpanded(){
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
            String query = getSavedPreferencesData(FLICKR_QUERY);
            if(query.length()>0){
                ProcessPhotos processPhotos = new ProcessPhotos(query,true);
                processPhotos.execute();
            }
    }

    private String getSavedPreferencesData(String key){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPref.getString(key, "");
    }

    public class ProcessPhotos extends GetFlickrJsonData{
        public ProcessPhotos(String seachCriteria, boolean matchAll){
            super(seachCriteria,matchAll);
        }

        public void execute(){
            super.execute();
            ProcessData processData = new ProcessData();
            processData.execute();
        }

        public class ProcessData extends DownloadJsonData{
            protected void onPostExecute(String webData){
                super.onPostExecute(webData);
                flickrRecyclerViewAdapter.loadNewData(getPhotos());
            }
        }
    }
}
