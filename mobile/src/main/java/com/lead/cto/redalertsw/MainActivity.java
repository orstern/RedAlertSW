package com.lead.cto.redalertsw;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class MainActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks {
    public static final String TAG = "MainActivityMobile";

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    private GoogleApiClient mApiClient;
    private static final String START_ACTIVITY = "/start_activity";

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "548355374500";

    TextView mDisplay;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;

    String regid;

    String[] cities;

    public static ArrayList<String> userCities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();

        initGoogleApiClient();

        // Check device for Play Services APK. If check succeeds, proceed with
        //  GCM registration.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
            }


        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }

        // Filling Spinner (Combo Box) with cities
        //Spinner spinner = (Spinner) findViewById(R.id.cities_spinner);
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
        //        this, R.array.cities_array, android.R.layout.simple_spinner_item);
       // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       // spinner.setAdapter(adapter);

        // Filling Spinnner (Combo Box) with cities
        //MultiSelectionSpinner spinner = (MultiSelectionSpinner) findViewById(R.id.cities_spinner);
        //spinner.setItems(getResources().getStringArray(R.array.cities_array));

        // Filling auto complete text field with cities
        // Get a reference to the AutoCompleteTextView in the layout
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.txtCities);
        // Get the string array
        cities = getResources().getStringArray(R.array.cities_array);
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> txtCitiesAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities);
        textView.setAdapter(txtCitiesAdapter);

        ListView listView = (ListView) findViewById(R.id.lstCities);
        ArrayAdapter<String> grdCitiesAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, userCities);

        listView.setAdapter(grdCitiesAdapter);
    }

    // You need to do the Play Services APK check here too.
    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    public void btnAddCityOnClick(View v) {

        // Get the city the user inputted
        String strCity = ((AutoCompleteTextView) findViewById(R.id.txtCities)).getText().toString();

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
            }
            else {
                new AlertDialog.Builder(MainActivity.this)
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
            new AlertDialog.Builder(MainActivity.this)
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

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }
    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        /*try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }*/

        return 1;
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the registration ID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

//            @Override
//            protected void onPostExecute(Object msg) {
//                mDisplay.append(msg.toString() + "\n");
//            }

        }.execute(null, null, null);
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
//        HttpClient httpclient = new DefaultHttpClient();
//        HttpPost httppost = new HttpPost("192.168.20.176:9000");
//
//        try {
//            // Add your data
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//            nameValuePairs.add(new BasicNameValuePair("regId", regid));
//            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//            // Execute HTTP Post Request
//            HttpResponse response = httpclient.execute(httppost);
//
//        } catch (ClientProtocolException e) {
//            // TODO Auto-generated catch block
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//        }
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.apply();
    }


    /*******************************************************************************************/

    private void initGoogleApiClient() {
        mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .build();

        mApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        sendMessage( START_ACTIVITY, "" );
    }

    private void sendMessage( final String path, final String text ) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mApiClient, node.getId(), path, text.getBytes() ).await();
                }

                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {

                        //mEditText.setText( "" );
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mApiClient.disconnect();
    }




    @Override
    public void onConnectionSuspended(int i) {

    }
}
