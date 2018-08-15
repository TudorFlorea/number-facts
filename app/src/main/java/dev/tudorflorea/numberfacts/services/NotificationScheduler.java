package dev.tudorflorea.numberfacts.services;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

public class NotificationScheduler {

    private static final int FACT_NOTIFICATION_INTERVAL_MINUTES = 1; //For test purposes
    private static final int FACT_NOTIFICATION_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(FACT_NOTIFICATION_INTERVAL_MINUTES));
    private static final int FACT_NOTIFICATION_FLEXTIME_SECONDS = FACT_NOTIFICATION_INTERVAL_SECONDS;

    private static final String FACT_NOTIFICATION_JOB_TAG = "fact_job_tag";

    private static boolean sInitialized;

    synchronized public static void scheduleFactNotification(@NonNull final Context context) {

        if (sInitialized) return;

        Driver driver = new GooglePlayDriver(context);

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job notificationJob = dispatcher.newJobBuilder()
                .setService(NotificationFirebaseJobService.class)
                .setRecurring(true)
                .setTag(FACT_NOTIFICATION_JOB_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(10,
                        10 + 5))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(notificationJob);
        sInitialized = true;
    }

    public static void cancelFactNotification(Context context) {

        Driver driver = new GooglePlayDriver(context);

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        dispatcher.cancel(FACT_NOTIFICATION_JOB_TAG);

        sInitialized = false;

    }

}
