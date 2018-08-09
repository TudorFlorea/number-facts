package dev.tudorflorea.numberfacts.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import dev.tudorflorea.numberfacts.R;
import dev.tudorflorea.numberfacts.data.Fact;
import dev.tudorflorea.numberfacts.data.FactFactory;
import dev.tudorflorea.numberfacts.ui.MainActivity;

public class FactIntentService extends IntentService {


    public FactIntentService() {
        super("FactIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Fact randomFact = FactFactory.RandomTriviaFact();

        //Toast.makeText(getApplicationContext(), "IN_SERVICE", Toast.LENGTH_SHORT).show();

        Log.e("Intent", "RUNNING PEND INTENT");
        Log.e("Intent", randomFact.getText());
        //Intent broadcastIntent = new Intent();
        //broadcastIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        //broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        //broadcastIntent.putExtra("msg", randomFact);
        //LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());

        ComponentName factsWidget = new ComponentName(getApplicationContext(), NumberFactsWidgetProvider.class);

        int[] widgetIds = appWidgetManager.getAppWidgetIds(factsWidget);

        for (int widgetId : widgetIds) {


            RemoteViews remoteViews = new RemoteViews(getApplicationContext().getApplicationContext().getPackageName(),
                        R.layout.widget_facts);

            Log.e("2", "_IN_RECEIVED");
                //Toast.makeText(context, "_IN_RECEIVED", Toast.LENGTH_SHORT).show();
            remoteViews.setTextViewText(R.id.widget_fact_tv, randomFact.getText());

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtra("widget_fact", randomFact);
            PendingIntent activityIntent = PendingIntent.getActivity(getApplicationContext(), 0, i, 0);

            remoteViews.setOnClickPendingIntent(R.id.widget_fact_tv, activityIntent);

            AppWidgetManager.getInstance(getApplicationContext()).updateAppWidget(factsWidget, remoteViews);

        }



    }
}
