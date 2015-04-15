package com.lead.cto.redalertsw;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.SyncStateContract;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    private static final String redAlertHebrew = "צבע אדום";

    // Data layer constants (todo: MOVE THIS ARABIC LOOKING CODE INTO A CONSTANTS FILE)
    private static final String ALERT_MODE_PATH = "/alert_mode";
    private static final String ALERT_MODE_KEY = "alert.mode.key";
    private boolean ALERT_MODE = false;


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
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                //TODO: Add function that check the location and start alarm if necessary

                // Post notification of received message.
                String strAlertInCities = parseMessage(extras.getString("cities"));
                String strRelevantCities = getRelevantCities(strAlertInCities);
                if (strRelevantCities.length() > 0 && !ALERT_MODE) {
                    ALERT_MODE = true;


                    // Set the data item
                    PutDataMapRequest dataMapRequest = PutDataMapRequest.create(ALERT_MODE_PATH);
                    dataMapRequest.getDataMap().putBoolean(ALERT_MODE_KEY,ALERT_MODE);
                    PutDataRequest putDataRequest = dataMapRequest.asPutDataRequest();
                    Wearable.DataApi.putDataItem(MainActivity.mApiClient, putDataRequest);

                    // Send notification
                    sendNotification(redAlertHebrew + ": " + strRelevantCities);
                }

            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }



    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);


        Intent push = new Intent();
        push.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        push.setClass( this, MainActivity.class );
        PendingIntent pi = PendingIntent.getActivity( this, 0, push, PendingIntent.FLAG_ONE_SHOT );


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.common_signin_btn_icon_dark)
                        .setContentTitle(redAlertHebrew)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg)
                        .setContentInfo(msg)
                .setVibrate(new long[] {1000, 1000})
                .setLights(Color.RED, 1, 1)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .setFullScreenIntent(pi, true);

        wakeScreen();


        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
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
        PowerManager.WakeLock screenOn = ((PowerManager)getSystemService(POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "RedAlert");
        screenOn.acquire(10000);
    }

    private String getRelevantCities(String strAlertInCities) {
        String strRelevantCities = "";
        for(String city: MainActivity.userCities) {
            if (strAlertInCities.contains(city)) {
                strRelevantCities += city + ", ";
            }
        }

        if (strRelevantCities.length() > 2) {
            strRelevantCities = strRelevantCities.substring(0, strRelevantCities.length() -2);
        }

        return strRelevantCities;
    }
}