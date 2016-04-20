package com.mikarney.jernil.shoutout;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */

public class AppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        // Construct the RemoteViews object

        //views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, AppWidgetProvider.class);
            intent.setAction("use_custom_class");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.imageView3, pendingIntent);
            new WidgetBroadcastReceiver();
            updateAppWidget(context, appWidgetManager, appWidgetId);

        }

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String action = intent.getAction();
        String actionName = "use_custom_class";

        if (actionName.equals(action)) {
            MainActivity mc = new MainActivity();
            mc.send_sms();
        }
    }
    @Override
    public void onEnabled(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        // Construct the RemoteViews object

        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


