package com.example.intific.iradsensorlog.googleactivityrecognition;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionRequest;
import com.google.android.gms.location.ActivityTransitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 * Created by John Nelson on 7/11/18.
 */

public class GoogleActivityRecognition {
    // Static tags
    protected static final String TAG = "GoogleActivityRecognition";

    // Context
    Context context;

    // Listener so that caller can be alerted to activity changes
    GoogleActivityRecognizedListener activityRecognizedListener;

    // Google activity recognition
    ActivityRecognitionClient activityRecognitionClient;

    // Intent which we'll configure to be received by DetectedActivitiesIntentService
    PendingIntent myPendingIntent;

    public GoogleActivityRecognition(Context context, GoogleActivityRecognizedListener activityRecognizedListener){
        this.context = context;
        this.activityRecognizedListener = activityRecognizedListener;
        activityRecognitionClient = ActivityRecognition.getClient(context);
    }

    public void registerForActivityTransitions(){
        /* kick off the activity recognition requests */
        // List the activity transitions we want to catch
        List<ActivityTransition> transitions = new ArrayList<>();

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.STILL)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build());

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.STILL)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                        .build());

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.WALKING)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build());

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.WALKING)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                        .build());

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.RUNNING)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build());

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.RUNNING)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                        .build());

        // Request updates to be sent to our DetectedActivitiesIntentService via a pending intent
        ActivityTransitionRequest request = new ActivityTransitionRequest(transitions);
        Intent activityTransitionIntent = new Intent(context, DetectedActivitiesIntentService.class);
        myPendingIntent = PendingIntent.getService(context, 0, activityTransitionIntent, FLAG_UPDATE_CURRENT);
        Task<Void> task = activityRecognitionClient.requestActivityTransitionUpdates(request, myPendingIntent);

        task.addOnSuccessListener(
                result -> {
                    // Handle success
                    Log.i(TAG, "succesful registration for activity updates");
                }
        );

        task.addOnFailureListener(
                e -> {
                    // Handle error
                    Log.e(TAG, "failed to register for activity updates");
                }
        );
    }

    //TODO unregister from activity updates
    public void unregisterFromActivityTransitions(){
        Task<Void> task =
                ActivityRecognition.getClient(context).removeActivityTransitionUpdates(myPendingIntent);

        task.addOnSuccessListener(
                result -> myPendingIntent.cancel());

        task.addOnFailureListener(
                e -> Log.e("MYCOMPONENT", e.getMessage()));
    }

    public interface GoogleActivityRecognizedListener {
        void activityRecognized(Integer activityInteger);
    }
}
