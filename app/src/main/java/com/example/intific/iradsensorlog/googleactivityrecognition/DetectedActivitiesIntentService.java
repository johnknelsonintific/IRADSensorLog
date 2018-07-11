package com.example.intific.iradsensorlog.googleactivityrecognition;

/**
 * Created by 205314 on 7/11/18.
 */

import android.app.IntentService;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.util.Log;
import android.widget.Toast;

import com.example.intific.iradsensorlog.utils.Utils;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;

/**
 *  IntentService for handling incoming intents that are generated as a result of requesting
 *  activity updates using
 *  {@link com.google.android.gms.location.ActivityRecognitionApi#requestActivityUpdates}.
 */
public class DetectedActivitiesIntentService extends IntentService {

    protected static final String TAG = "DetectedActivitiesIS";

    /**
     * This constructor is required, and calls the super IntentService(String)
     * constructor with the name for a worker thread.
     */
    public DetectedActivitiesIntentService() {
        // Use the TAG to name the worker thread.
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Handles incoming intents.
     * @param intent The Intent is provided (inside a PendingIntent) when requestActivityUpdates()
     *               is called.
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            if (ActivityTransitionResult.hasResult(intent)) {
                ActivityTransitionResult result = ActivityTransitionResult.extractResult(intent);
                for (ActivityTransitionEvent event : result.getTransitionEvents()) {

                    Toast.makeText(this, event.getTransitionType() + "-" + event.getActivityType(), Toast.LENGTH_LONG).show();
                    //7 for walking and 8 for running
                    Log.i(TAG, "Activity Type " + event.getActivityType());

                    // 0 for enter, 1 for exit
                    Log.i(TAG, "Transition Type " + event.getTransitionType());
                }
            }
        }
    }
}