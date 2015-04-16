package com.lead.cto.redalertsw;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class GcmIntentService extends IntentService {//implements GoogleApiClient.ConnectionCallbacks, MessageApi.MessageListener, NodeApi.NodeListener, DataApi.DataListener{
    private static final String TAG = "GcmIntentService";
    public static int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;


    NotificationCompat.Builder builder;

    private static final String START_ACTIVITY_PATH = "/start-activity";
    private static final String redAlertHebrew = "צבע אדום";

    public GcmIntentService() {
        super("GcmIntentService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {

            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {

                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                // Post notification of received message.
                String strAlertInCities = parseMessage(extras.getString("cities"));
                String strTitle = extras.getString("title");
                String strRelevantCities = getRelevantCities(strAlertInCities);
                //if (strRelevantCities.length() > 0) {
                    sendNotification(strRelevantCities, strTitle);
                //}

            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }



    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String strMsg, String strTitle) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create Pending intent that start SMS send
        Intent intentSendSms = new Intent();
        intentSendSms.setClass(this, ActionHandlerService.class);
        intentSendSms.setAction(ActionHandlerService.SEND_SMS);
        PendingIntent piSendSms = PendingIntent.getService(this, 0, intentSendSms, PendingIntent.FLAG_ONE_SHOT);

        // Create Pending intent that start reporting missile fall
        Intent intentReportMissileFall = new Intent();
        intentReportMissileFall.setClass(this, ActionHandlerService.class);
        intentReportMissileFall.setAction(ActionHandlerService.REPORT_MISSILE_FALL);
        PendingIntent piReportMissileFall = PendingIntent.getService(this, 0, intentReportMissileFall, PendingIntent.FLAG_ONE_SHOT);

        // Create Pending intent that start reporting missile fall
        Intent intentNavigateToSafePlace = new Intent();
        intentNavigateToSafePlace.setClass(this, ActionHandlerService.class);
        intentNavigateToSafePlace.setAction(ActionHandlerService.NAVIGATE_TO_SAFE_PLACE);
        PendingIntent piNavigateToSafePlace = PendingIntent.getService(this, 0, intentNavigateToSafePlace, PendingIntent.FLAG_ONE_SHOT);




        // Create background to the actions in the smartwatch
        Bitmap background = BitmapFactory.decodeResource(getResources(),
                R.drawable.sirenbackground);

        List<NotificationCompat.Action> actions = new ArrayList<NotificationCompat.Action>();
        actions.add(new NotificationCompat.Action(R.drawable.navigate, getString(R.string.navigate_to_safe_place), piNavigateToSafePlace));
        actions.add(new NotificationCompat.Action(R.drawable.sosicon, getString(R.string.get_help), piSendSms));
        actions.add(new NotificationCompat.Action(R.drawable.missile, getString(R.string.report_missile_fall), piReportMissileFall));


        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender()
                        .setBackground(background)
                        .addActions(actions);
                        //.addActions(actions);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(redAlertHebrew)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(strTitle + ": " + strMsg))
                        .setContentText(strTitle + ": " + strMsg)
                        .setVibrate(new long[]{0, 500, 250, 500, 250, 500, 250, 500 })
                        .setLights(Color.RED, 3000, 3000)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setAutoCancel(true)
                        .setGroup("RedAlertActivity")
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setTicker(redAlertHebrew + " " + strTitle + ": " + strMsg)
                        .extend(wearableExtender);


        mNotificationManager.notify(NOTIFICATION_ID++, mBuilder.build());
        wakeScreen();
    }

    private String parseMessage(String strCities) {
        String strSubstringCities = strCities.substring(1, strCities.length() -1);
        String[] arrCities = strSubstringCities.split(",");
        String parsedMessage = "";
        for (int i = 0; i < arrCities.length; i++) {
            parsedMessage += arrCities[i].substring(1, arrCities[i].length()-1);

            // Add ", " to all the cities except the last one
            if (i != arrCities.length -1) {
                parsedMessage += ", ";
            }
        }

        return parsedMessage;
    }

    private void wakeScreen() {
        PowerManager.WakeLock screenOn = ((PowerManager)getSystemService(POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "RedAlertActivity");
        screenOn.acquire(10000);
    }

    private String getRelevantCities(String strAlertInCities) {
        String strRelevantCities = "";
        for(String city: CitiesActivity.userCities) {
            if (strAlertInCities.contains(city)) {
                strRelevantCities += city + ", ";
            }
        }

        if (strRelevantCities.length() > 2) {
            strRelevantCities = strRelevantCities.substring(0, strRelevantCities.length() -2);
        }

        return strRelevantCities;
    }


    /*******************************************************************************************/


    private Collection<String> getNodes() {
        HashSet<String> results = new HashSet<String>();
        NodeApi.GetConnectedNodesResult nodes =
                Wearable.NodeApi.getConnectedNodes(MainActivity.mGoogleApiClient).await();

        for (Node node : nodes.getNodes()) {
            results.add(node.getId());
        }

        return results;
    }

    private class StartWearableActivityTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... args) {
            Collection<String> nodes = getNodes();
            for (String node : nodes) {
                sendStartActivityMessage(node);
            }
            return null;
        }
    }

    private void sendStartActivityMessage(String node) {
        Wearable.MessageApi.sendMessage(
                MainActivity.mGoogleApiClient, node, START_ACTIVITY_PATH, new byte[0]).setResultCallback(
                new ResultCallback<MessageApi.SendMessageResult>() {
                    @Override
                    public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                        if (!sendMessageResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Failed to send message with status code: "
                                    + sendMessageResult.getStatus().getStatusCode());
                        }
                    }
                }
        );
    }
}