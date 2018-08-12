package dev.tudorflorea.numberfacts.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import dev.tudorflorea.numberfacts.R;
import dev.tudorflorea.numberfacts.ui.MainActivity;

public class NumberFactsWidgetProvider extends AppWidgetProvider{

    public static final String ACTION_NEW_FACT = "dev.tudforflorea.numberfacts.widget.WIDGET_CLICK";

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(),
                R.layout.widget_facts
        );

        Intent intent = new Intent(context, FactIntentService.class);

        PendingIntent newFactIntent =  PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_fact_btn, newFactIntent);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {


            RemoteViews remoteViews = new RemoteViews(
                    context.getPackageName(),
                    R.layout.widget_facts
            );

            Intent intent = new Intent(context, FactIntentService.class);

            PendingIntent newFactIntent =  PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget_fact_btn, newFactIntent);

            Intent configIntent = new Intent(context, MainActivity.class);

            PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);

            remoteViews.setOnClickPendingIntent(R.id.widget_fact_tv, configPendingIntent);


            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);


        }



    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            ComponentName factsWidget = new ComponentName(context.getApplicationContext(), NumberFactsWidgetProvider.class);

            int[] widgetIds = appWidgetManager.getAppWidgetIds(factsWidget);

            for (int widgetId : widgetIds) {

                if(intent.hasExtra("msg")) {
                    RemoteViews remoteViews = new RemoteViews(context.getApplicationContext().getPackageName(),
                            R.layout.widget_facts);

                    remoteViews.setTextViewText(R.id.widget_fact_tv, "aa");

                    AppWidgetManager.getInstance(context).updateAppWidget(factsWidget, remoteViews);
                }

            }



        }

    }


}
