package com.lead.cto.redalertsw;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;

/**
 * Created by ofir on 4/15/2015.
 */
public class EmergencyContactsActivity  extends Activity{

    private static final int PICK_CONTACT = 1234;

    public static ArrayList<String> userEmergencyContacts = new ArrayList<>();
    public static ArrayList<String> userEmergencyContactsNumbers = new ArrayList<>();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.emergency_contacts_layout);

        // Set contacts list
        ListView listView = (ListView) findViewById(R.id.lstContacts);
        ArrayAdapter<String> lstCitiesAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, userEmergencyContacts);

        listView.setAdapter(lstCitiesAdapter);
    }

    public void btnAddContactsOnClick(View v) {

        // Allow user to pick contact
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT);
    }

    public void SendSMSToContacts(String strMessage) {

        SmsManager smsManager = SmsManager.getDefault();

        // Looping through phone numbers and sending SMS
        for (String strCurrPhoneNumber : this.userEmergencyContactsNumbers) {
            smsManager.sendTextMessage(strCurrPhoneNumber, null, strMessage, null, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == PICK_CONTACT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {


                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(column);

                this.userEmergencyContactsNumbers.add(number);

                // Getting display name
                String[] projectionDisplayName = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                cursor = getContentResolver()
                        .query(contactUri, projectionDisplayName, null, null, null);
                cursor.moveToFirst();

                column = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                this.userEmergencyContacts.add(cursor.getString(column));

                // Set contacts list
                ListView listView = (ListView) findViewById(R.id.lstContacts);
                ArrayAdapter<String> lstCitiesAdapter = new ArrayAdapter<String>(
                        this, android.R.layout.simple_list_item_1, userEmergencyContacts);

                listView.setAdapter(lstCitiesAdapter);
            }
        }
    }




}
