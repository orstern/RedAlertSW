package com.lead.cto.redalertsw;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ActionHandlerService extends IntentService {

    public static final String SEND_SMS = "com.lead.cto.redalertsw.action.SEND_SMS";
    public static final String REPORT_MISSILE_FALL = "com.lead.cto.redalertsw.action.REPORT_MISSILE_FALL";
    public static final String NAVIGATE_TO_SAFE_PLACE = "com.lead.cto.redalertsw.action.NAVIGATE_TO_SAFE_PLACE";



    public ActionHandlerService() {
        super("ActionHandlerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (SEND_SMS.equals(action)) {
                handleSendSms();

            } else if (REPORT_MISSILE_FALL.equals(action)) {
                handleReportMissileFall();

            } else if (NAVIGATE_TO_SAFE_PLACE.equals(action)) {
                handleNavigateToSafePlace();
            }
        }
    }


    private void handleSendSms() {
       EmergencyContactsActivity.SendSMSToContacts("Emergency SMS");
    }


    private void handleReportMissileFall() {

    }

    private void handleNavigateToSafePlace() {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=City+Hall,+Tel+Aviv+Israel&mode=w");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }
}
