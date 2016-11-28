package com.niallod.quickbudget.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.niallod.quickbudget.R;
import com.niallod.quickbudget.activities.AddItem;
import com.niallod.quickbudget.activities.RemoveItem;

/**
 * Delete Widget
 */
public class QuickBudgetDeleteWidget extends AppWidgetProvider {

    //action name
    public static String DELETE = "DELETE";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            //setting up the action on the widget
            Intent delIntent = new Intent(context, QuickBudgetAppWidget.class);
            delIntent.setAction(DELETE);
            PendingIntent delPendingAction = PendingIntent.getBroadcast(context, 0, delIntent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.quick_budget_delete_widget);
            views.setOnClickPendingIntent(R.id.del_widget_button, delPendingAction);
            appWidgetManager.updateAppWidget(appWidgetId, views);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    //widget is clicked so respond
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if(intent.getAction().equals(DELETE)) {
            Intent i = new Intent(context, RemoveItem.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}

