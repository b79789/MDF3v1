package com.brianstacks.widgetapp.CollectionWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.brianstacks.widgetapp.DetailsFragment;
import com.brianstacks.widgetapp.DetailsViewActivity;
import com.brianstacks.widgetapp.EnterDataFragment;
import com.brianstacks.widgetapp.EnteredData;
import com.brianstacks.widgetapp.MainActivity;
import com.brianstacks.widgetapp.R;

/**
 * Created by Brian Stacks
 * on 1/19/15
 * for FullSail.edu.
 */
 public class CollectionWidgetProvider extends AppWidgetProvider {

    public static final String ACTION_VIEW_DETAILS = "com.brianstacks.android.ACTION_VIEW_DETAILS";
    public static final String EXTRA_ITEM = "com.brianstacks.android.CollectionWidgetProvider.EXTRA_ITEM";
    public static String WIDGET_BUTTON = "com.brianstacks.android.WIDGET_BUTTON";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        for (int widgetId : appWidgetIds) {


            Intent intent = new Intent(context, CollectionWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

            RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.my_widget_layout);
            widgetView.setRemoteAdapter(R.id.article_list, intent);
            widgetView.setEmptyView(R.id.article_list, R.id.empty);

            Intent detailIntent = new Intent(ACTION_VIEW_DETAILS);
            PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, detailIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            widgetView.setPendingIntentTemplate(R.id.article_list, pIntent);


            Intent intent2 = new Intent(WIDGET_BUTTON);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
            widgetView.setOnClickPendingIntent(R.id.addThis, pendingIntent);

            appWidgetManager.updateAppWidget(widgetId, widgetView);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.article_list);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(ACTION_VIEW_DETAILS)) {
            EnteredData article = (EnteredData)intent.getSerializableExtra(EXTRA_ITEM);
            if(article != null) {
                // Handle the click here.
                // Maybe start a details activity?
                    Intent details = new Intent(context, DetailsViewActivity.class);
                    details.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    details.putExtra(DetailsViewActivity.ARG_Name, article);

                    context.startActivity(details);

            }
        }else if (intent.getAction().equals(WIDGET_BUTTON)) {

            Intent details = new Intent(context, MainActivity.class);
            details.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(details);
        }else

        super.onReceive(context, intent);
    }
}
