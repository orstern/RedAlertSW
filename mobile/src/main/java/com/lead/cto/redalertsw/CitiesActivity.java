package com.lead.cto.redalertsw;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ofir on 4/15/2015.
 */
public class CitiesActivity extends Activity {

    String[] cities;

    public static ArrayList<String> userCities = new ArrayList<>();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cities_layout);

        // Filling auto complete text field with cities
        // Get a reference to the AutoCompleteTextView in the layout
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.txtSelectedCity);

        // Get the string array
        cities = getResources().getStringArray(R.array.all_cities_array);

        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> txtCitiesAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities);
        textView.setAdapter(txtCitiesAdapter);

        // Loading user cities
        //Set<String> stringSet = new HashSet<String>();
        //SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        //sharedPref.getStringSet("SaveUserCitiesArray", stringSet);
        //userCities.addAll(stringSet);

        ListView listView = (ListView) findViewById(R.id.lstCities);
        ArrayAdapter<String> lstCitiesAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, userCities);

        listView.setAdapter(lstCitiesAdapter);
    }

    public void btnAddCityOnClick(View v) {

        // Get the city the user inputted
        String strCity = ((AutoCompleteTextView) findViewById(R.id.txtSelectedCity)).getText().toString();

        // Removing whitespace
        strCity = strCity.replaceAll("\\s+$", "");

        boolean bWasFound = false;

        // Check if city exists
        for (String strCurrCity : cities) {
            if (strCity.equals(strCurrCity)) {
                bWasFound = true;
            }
        }

        // If city doesn't exist we show an error message, else add it to GridView
        if (bWasFound) {

            // Adding city to grid
            if (!userCities.contains(strCity)) {
                userCities.add(strCity);

                ListView listView = (ListView) findViewById(R.id.lstCities);

                ArrayAdapter<String> grdCitiesAdapter = new ArrayAdapter<String>(
                        this, android.R.layout.simple_list_item_1, userCities);

                listView.setAdapter(grdCitiesAdapter);

                ((AutoCompleteTextView) findViewById(R.id.txtSelectedCity)).setText("");

                // Add city to preferences
                //SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                //SharedPreferences.Editor editor = sharedPref.edit();
                //editor.putStringSet("SaveUserCitiesArray", new HashSet<>(userCities));
                //editor.commit();
            }
            else {
                new AlertDialog.Builder(CitiesActivity.this)
                        .setTitle(getResources().getString(R.string.city_already_added_title))
                        .setMessage(getResources().getString(R.string.city_already_added_message))
                        .setCancelable(false)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // whatever...
                            }
                        }).create().show();
            }
        }
        else {
            new AlertDialog.Builder(CitiesActivity.this)
                    .setTitle(getResources().getString(R.string.city_not_exist_title))
                    .setMessage(getResources().getString(R.string.city_not_exist_message))
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // whatever...
                        }
                    }).create().show();
        }

    }
}
