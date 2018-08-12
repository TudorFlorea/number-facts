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

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());

        ComponentName factsWidget = new ComponentName(getApplicationContext(), NumberFactsWidgetProvider.class);

        int[] widgetIds = appWidgetManager.getAppWidgetIds(factsWidget);
        for (int widgetId : widgetIds) {

                RemoteViews remoteViews = new RemoteViews(getApplicationContext().getApplicationContext().getPackageName(),
                        R.layout.widget_facts);
                remoteViews.setTextViewText(R.id.widget_fact_tv, "aa");
                AppWidgetManager.getInstance(getApplicationContext()).updateAppWidget(factsWidget, remoteViews);

        }

        return super.onStartCommand(intent, flags, startId);
    }
}
