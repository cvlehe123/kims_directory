package com.example.charlie.directory;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.List;


public class Directory extends ActionBarActivity {
    private static final String TAG = "Directory";

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
                    business.vignette = object.getString("vignette");


                    array[i] = business;

                }
                for (int i = 0; i <Array.getLength(array); i++) {
                    Business currentBusiness = array[i];
                    Log.d(TAG, currentBusiness.title);
                }
                ListView listView = (ListView)findViewById(R.id.charleston_directory);
                listView.setAdapter(new ArrayAdapter<String>());


            }
            class NewClass extends ArrayAdapter<String>{
                NewClass(Business[] businesses){
                    super(Directory.this, android.R.layout.simple_list_item_1, businesses);
                }
            }



        });
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_directory, menu);
        return true;

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
}
