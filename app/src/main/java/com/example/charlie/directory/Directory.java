package com.example.charlie.directory;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


//declare class variables
public class Directory extends ActionBarActivity {
    private static final String TAG = "Directory";
    private  static SearchView mSearchView;
    Business[] mArray;
    Business[] mSearchResults;
    ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "uXsrr08tw31RhuIQO8LRpW3b6aKfvUYGf4h6NCev", "SXUM4CuMPN2i4tDRTBIRXbE0KE9ZGqaHDUBqmqfs");


        //fetch data from parse
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Business");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, com.parse.ParseException e) {
                mArray = new Business[list.size()];
                mSearchResults = mArray; //resets search results

                /*for loop fetches every cell in database */
                for (int i = 0; i < list.size(); i++) {
                    ParseObject object = list.get(i);
                    Business business = new Business();
                    business.title = object.getString("title");
                    business.address = object.getString("address");
                    business.phone = object.getString("phoneNumber");
                    business.vignette = object.getString("description");
                    business.website = object.getString("website");


                    mArray[i] = business;

                }
                /*ASK CHARLIE TO EXPLAIN THIS FOR LOOP */
                for (int i = 0; i <Array.getLength(mArray); i++) {
                    Business currentBusiness = mArray[i];
                    Log.d(TAG, currentBusiness.title);
                }
                mListView = (ListView)findViewById(R.id.charleston_directory);
                mListView.setAdapter(new TaskAdapter(mSearchResults));//ASK CHARLIE TO EXPLAIN


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
            //TextView businessDescription = (TextView)convertView.findViewById(R.id.business_descripton);
            //TextView businessWebsite = (TextView)convertView.findViewById(R.id.business_website);
            TextView businessPhone = (TextView)convertView.findViewById(R.id.business_phone);
            TextView businessAddress = (TextView)convertView.findViewById(R.id.business_address);

            taskName.setText(task.title);
            //businessDescription.setText(task.vignette);
            //businessWebsite.setText(task.website);
            businessPhone.setText(task.phone);
            businessAddress.setText(task.address);

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
                    Log.d(TAG,query);

                    loadListFromSearch(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    return false;
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

    public void loadListFromSearch (String query) {
        List<Business> businessList = new ArrayList<Business>();

        for (int i = 0; i< Array.getLength(mArray); i++){
            if (mArray[i].title.toLowerCase().contains(query.toLowerCase())) {
                Log.d(TAG, "title checks out"+mArray[i].title);
                businessList.add(mArray[i]);

            }else if (mArray[i].phone.toLowerCase().contains(query.toLowerCase())) {
                Log.d(TAG, "phone number checks out");
                businessList.add(mArray[i]);

            }else if (mArray[i].address.toLowerCase().contains(query.toLowerCase())) {
                Log.d(TAG, "address checks out");
                businessList.add(mArray[i]);

            }else if (mArray[i].vignette.toLowerCase().contains(query.toLowerCase())) {
                Log.d(TAG, "description checks out");
                businessList.add(mArray[i]);

            }else if (mArray[i].website.toLowerCase().contains(query.toLowerCase())){
                Log.d(TAG, "website checks out"+mArray[i].website);
                businessList.add(mArray[i]);



            }


        }

        if (businessList.size() == 0) {
            Log.d(TAG, "Sorry no results found");
            Toast.makeText(Directory.this,"Search completed. No results found",Toast.LENGTH_LONG)
                    .show();
        }else {
            mSearchResults = new Business[businessList.size()];//creates new business list
            mSearchResults = businessList.toArray(mSearchResults);//converts list back into an array
            mListView.setAdapter(new TaskAdapter(mSearchResults)); //reloads for a fresh search
        }
    }
}
