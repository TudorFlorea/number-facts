package dev.tudorflorea.numberfacts.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import dev.tudorflorea.numberfacts.R;
import dev.tudorflorea.numberfacts.data.Fact;
import dev.tudorflorea.numberfacts.ui.MainActivity;

public class NotificationUtils {

    private static final int NOTIFICATION_ID = 100;
    private static final String NOTIFICATION_CHANNEL_ID = "notification_channel_id";
    private static final String NOTIFICATION_CHANNEL_NAME = "notification_channel";
    private static final String NOTIFICATION_HEADING = "Did you know?";


    private static void clearAll(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
    }

    public static void randomFactNotification(Context context, Fact fact) {

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        NOTIFICATION_CHANNEL_ID,
                        NOTIFICATION_CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_HIGH
                );

                manager.createNotificationChannel(channel);
            }

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(
                            context,
                            NOTIFICATION_CHANNEL_ID)
                            .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                            .setSmallIcon(R.drawable.ic_pi_48)
                            .setLargeIcon(getNotificationIcon(context))
                            .setContentTitle(NOTIFICATION_HEADING)
                            .setContentText(fact.getText())
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(fact.getText()))
                            .setDefaults(Notification.DEFAULT_VIBRATE)
                            .setContentIntent(contentIntent(context, fact))
                            .setAutoCancel(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                    && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
            }

            manager.notify(NOTIFICATION_ID, notificationBuilder.build());
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }





    }

    private static PendingIntent contentIntent(Context context, Fact fact) {

        Intent startActivityIntent = new Intent(context, MainActivity.class);
        startActivityIntent.putExtra(context.getResources().getString(R.string.intent_fact_extra), fact);
        return PendingIntent.getActivity(context, 0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    private static Bitmap getNotificationIcon(Context context) {

        Resources resources = context.getResources();

        return BitmapFactory.decodeResource(resources, R.drawable.numberfacts_logo_transparent);
    }

}
