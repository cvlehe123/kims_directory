package com.example.charlie.directory;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class Directory extends ActionBarActivity {
    private static final String TAG = "Directory";
    private  static SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "uXsrr08tw31RhuIQO8LRpW3b6aKfvUYGf4h6NCev", "SXUM4CuMPN2i4tDRTBIRXbE0KE9ZGqaHDUBqmqfs");


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Business");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, com.parse.ParseException e) {
                Business[] array = new Business[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    ParseObject object = list.get(i);
                    Business business = new Business();
                    business.title = object.getString("title");
                    business.address = object.getString("address");
                    business.phone = object.getString("phone");
                   // business.vignette = object.getString("description");


                    array[i] = business;

                }
                for (int i = 0; i <Array.getLength(array); i++) {
                    Business currentBusiness = array[i];
                    Log.d(TAG, currentBusiness.title);
                }
                ListView listView = (ListView)findViewById(R.id.charleston_directory);
                listView.setAdapter(new TaskAdapter(array));


            }



        });
    }

    private class TaskAdapter extends ArrayAdapter<Business> {
        TaskAdapter(Business[] businesses){
            super(Directory.this, R.layout.business_list_row,R.id.business_name, businesses);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView =  super.getView(position, convertView, parent);
            Business task = getItem(position);
            TextView taskName = (TextView)convertView.findViewById(R.id.business_name);
            TextView businessDescription = (TextView)convertView.findViewById(R.id.business_descripton);
            taskName.setText(task.title);
            businessDescription.setText(task.vignette);

            return convertView;
        }
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_directory, menu);
            mSearchView = (SearchView)menu.findItem(R.id.search_view).getActionView();
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.d(TAG,newText);
                    return true;
                }
            });
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_view) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
