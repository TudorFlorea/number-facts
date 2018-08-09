package dev.tudorflorea.numberfacts.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import dev.tudorflorea.numberfacts.R;
import dev.tudorflorea.numberfacts.data.Fact;
import dev.tudorflorea.numberfacts.data.FactFactory;

public class UpdateService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Fact randomFact = FactFactory.RandomTriviaFact();

        Log.e("Intent", "RUNNING Service");
        Log.e("Intent", randomFact.getText());
        //Intent broadcastIntent = new Intent();
        //broadcastIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        //broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        //broadcastIntent.putExtra("msg", randomFact);
        //LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());

        ComponentName factsWidget = new ComponentName(getApplicationContext(), NumberFactsWidgetProvider.class);

        int[] widgetIds = appWidgetManager.getAppWidgetIds(factsWidget);

        Log.e("widgetIds", String.valueOf(widgetIds));
        for (int widgetId : widgetIds) {

                RemoteViews remoteViews = new RemoteViews(getApplicationContext().getApplicationContext().getPackageName(),
                        R.layout.widget_facts);

                Log.e("2", "_IN_RECEIVED");
                //Toast.makeText(context, "_IN_RECEIVED", Toast.LENGTH_SHORT).show();
                remoteViews.setTextViewText(R.id.widget_fact_tv, "aa");

                AppWidgetManager.getInstance(getApplicationContext()).updateAppWidget(factsWidget, remoteViews);

        }

        return super.onStartCommand(intent, flags, startId);
    }
}
