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
 * Created by nodat on 28/11/2016.
 */

public class QuickBudgetAddWidget extends AppWidgetProvider {


    public static String ADD = "ADD";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        for (int appWidgetId : appWidgetIds) {

            Intent addIntent = new Intent(context, QuickBudgetAppWidget.class);
            addIntent.setAction(ADD);
            PendingIntent addPendingIntent = PendingIntent.getBroadcast(context, 0, addIntent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.quick_budget_add_widget);
            views.setOnClickPendingIntent(R.id.add_widget_button, addPendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

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
