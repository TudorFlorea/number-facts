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

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());

        ComponentName factsWidget = new ComponentName(getApplicationContext(), NumberFactsWidgetProvider.class);

        int[] widgetIds = appWidgetManager.getAppWidgetIds(factsWidget);

        for (int widgetId : widgetIds) {


            RemoteViews remoteViews = new RemoteViews(getApplicationContext().getApplicationContext().getPackageName(),
                        R.layout.widget_facts);

            remoteViews.setTextViewText(R.id.widget_fact_tv, randomFact.getText());

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtra(getResources().getString(R.string.intent_fact_extra), randomFact);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent activityIntent = PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

            remoteViews.setOnClickPendingIntent(R.id.widget_fact_tv, activityIntent);

            AppWidgetManager.getInstance(getApplicationContext()).updateAppWidget(factsWidget, remoteViews);

        }



    }
}
