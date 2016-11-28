package com.niallod.quickbudget.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.niallod.quickbudget.activities.AddItem;
import com.niallod.quickbudget.activities.EditItem;
import com.niallod.quickbudget.activities.MainActivity;
import com.niallod.quickbudget.R;
import com.niallod.quickbudget.activities.RemoveItem;

/**
 * Widget that is 4 x 1 long
 */
public class QuickBudgetAppWidget extends AppWidgetProvider {

    //actions
    public static String ADD = "ADD";
    public static String DELETE = "DELETE";
    public static String EDIT = "EDIT";
    public static String HOME = "HOME";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        for (int appWidgetId : appWidgetIds) {

                //creating the actions for the widget
                Intent addIntent = new Intent(context, QuickBudgetAppWidget.class);
                addIntent.setAction(ADD);
                Intent removeIntent = new Intent(context, QuickBudgetAppWidget.class);
                removeIntent.setAction(DELETE);
                Intent homeIntent = new Intent(context, QuickBudgetAppWidget.class);
                homeIntent.setAction(HOME);
                Intent editIntent = new Intent(context, QuickBudgetAppWidget.class);
                editIntent.setAction(EDIT);

                PendingIntent addPendingIntent = PendingIntent.getBroadcast(context, 0, addIntent, 0);
                PendingIntent removePendingIntent = PendingIntent.getBroadcast(context, 0, removeIntent, 0);
                PendingIntent homePendingIntent = PendingIntent.getBroadcast(context, 0, homeIntent, 0);
                PendingIntent editPendingIntent = PendingIntent.getBroadcast(context, 0, editIntent, 0);

                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.quick_budget_app_widget);
                views.setOnClickPendingIntent(R.id.widget_add, addPendingIntent);
                views.setOnClickPendingIntent(R.id.widget_remove, removePendingIntent);
                views.setOnClickPendingIntent(R.id.widget_home, homePendingIntent);
                views.setOnClickPendingIntent(R.id.widget_edit, editPendingIntent);

                appWidgetManager.updateAppWidget(appWidgetId, views);
//            updateAppWidget(context, appWidgetManager, appWidgetId);

        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    // respond to a click
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if(intent.getAction().equals(ADD)) {
            Intent i = new Intent(context, AddItem.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else if(intent.getAction().equals(DELETE)) {
            Intent i = new Intent(context, RemoveItem.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else if(intent.getAction().equals(HOME)) {
            Intent i = new Intent(context, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else if(intent.getAction().equals(EDIT)) {
            Intent i = new Intent(context, EditItem.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

}

