package com.niallod.quickbudget.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.niallod.quickbudget.R;
import com.niallod.quickbudget.activities.EditItem;
import com.niallod.quickbudget.activities.RemoveItem;

/**
 * edit widget functionality
 */
public class QuickBudgetEditWidget extends AppWidgetProvider {

    //name of action
    public static String EDIT = "EDIT";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            //setting up the action
            Intent editIntent = new Intent(context, QuickBudgetEditWidget.class);
            editIntent.setAction(EDIT);
            PendingIntent editPendingAction = PendingIntent.getBroadcast(context, 0, editIntent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.quick_budget_edit_widget);
            views.setOnClickPendingIntent(R.id.edit_widget_button, editPendingAction);
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

        if(intent.getAction().equals(EDIT)) {
            Intent i = new Intent(context, EditItem.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}

