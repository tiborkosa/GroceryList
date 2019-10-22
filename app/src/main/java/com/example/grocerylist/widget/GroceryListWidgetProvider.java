package com.example.grocerylist.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.grocerylist.MainActivity;
import com.example.grocerylist.R;

/**
 * Implementation of App Widget functionality.
 *
 * This is just a simple widget to open the app
 */
public class GroceryListWidgetProvider extends AppWidgetProvider {

    /**
     * updating the widget
     * @param context app contecxt
     * @param appWidgetManager widget manager
     * @param appWidgetId widget id
     */
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.grocery_list_widget);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 ,intent, 0);

        views.setOnClickPendingIntent(R.id.widget_button, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * when receiving update the widget
     * @param context app context
     * @param intent that stores the data
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        int[] ids = mgr.getAppWidgetIds(new ComponentName(context, GroceryListWidgetProvider.class));

        if(action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){
            onUpdate(context, mgr, ids);
        }

        super.onReceive(context, intent);
    }

    /**
     * update all widgets
     * @param context appcontext
     * @param appWidgetManager widget manager
     * @param appWidgetIds widget ids
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    // not used
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    // not used
    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

