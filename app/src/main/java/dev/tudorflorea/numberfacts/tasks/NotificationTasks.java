package dev.tudorflorea.numberfacts.tasks;

import android.content.Context;

public class NotificationTasks {

    public static final String ACTION_FACT_NOTIFICATION = "fact-notification";

    public static void executeTask(Context context, String action) {

        if (action.equals(ACTION_FACT_NOTIFICATION)) {
            sendFactNotification(context);
        }

    }

    private static void sendFactNotification(Context context) {



    }

}
