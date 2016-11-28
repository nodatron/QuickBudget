package com.niallod.quickbudget.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.niallod.quickbudget.R;
import com.niallod.quickbudget.activities.AddItem;

/**
 * Add Widget for the app that brings the use to the add item activity
 */
public class QuickBudgetAddWidget extends AppWidgetProvider {

    // action name
    public static String ADD = "ADD";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        for (int appWidgetId : appWidgetIds) {

            //creating an action for the widget as it cant use onclick listeners
            Intent addIntent = new Intent(context, QuickBudgetAppWidget.class);
            addIntent.setAction(ADD);
            PendingIntent addPendingIntent = PendingIntent.getBroadcast(context, 0, addIntent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.quick_budget_add_widget);
            views.setOnClickPendingIntent(R.id.add_widget_button, addPendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    //whent he widget is clicked method is called
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if(intent.getAction().equals(ADD)) {
            Intent i = new Intent(context, AddItem.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
