package com.example.newweather.layout;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.he.coolweather2.R;
import com.example.he.coolweather2.activity_weather;
import com.example.he.coolweather2.gson.Weather;
import com.example.he.coolweather2.service.WidgetService;
import com.example.he.coolweather2.util.Utility;


/**
 * Implementation of App Widget functionality.
 */
public class Widget extends AppWidgetProvider {

    private static final String TAG = "Widget";
    public static boolean hasWidget = false;

    private static ComponentName componentName;
    private static RemoteViews view;


    /**
     * 更新桌面小部件   无用
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     */
    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String weatherString = preferences.getString("weather", null);
        if (weatherString != null) {
            final Weather weather = Utility.handleWeatherResponse(weatherString);
            if (weather != null) {
                show(weather, views);
            }
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * 更新桌面小部件  有用
     *
     * @param context
     * @param appWidgetManager
     * @param cn
     * @param t
     */
    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, ComponentName cn, String t) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String weatherString = preferences.getString("weather", null);
        if (weatherString != null) {
            final Weather weather = Utility.handleWeatherResponse(weatherString);
            if (weather != null) {
                show(weather, views);
            }
        }
        componentName = cn;
        view = views;
        appWidgetManager.updateAppWidget(cn, views);
    }


    private static void show(Weather weather, RemoteViews views) {
        String degree = weather.now.temperature + "°";
        views.setTextViewText(R.id.widget_T, degree);
        String weatherInfo = weather.now.more.info;
        views.setTextViewText(R.id.widget_weather, weatherInfo);
        String cityName = weather.basic.cityName;
        views.setTextViewText(R.id.widget_city, cityName);
        //根据天气选择相应的图片
        if (weatherInfo.equals("晴")) {
            views.setImageViewResource(R.id.widget_image, R.drawable.sun);
        } else if (weatherInfo.equals("阴") || weatherInfo.equals("多云")) {
            views.setImageViewResource(R.id.widget_image, R.drawable.cloudy);
        } else if (weatherInfo.equals("雨") || weatherInfo.equals("小雨") || weatherInfo.equals("中雨") || weatherInfo.equals("大雨") || weatherInfo.equals("暴雨")) {
            views.setImageViewResource(R.id.widget_image, R.drawable.rain);
        } else if (weatherInfo.equals("雪") || weatherInfo.equals("小雪") || weatherInfo.equals("中雪") || weatherInfo.equals("大雪")) {
            views.setImageViewResource(R.id.widget_image, R.drawable.snow);
        }

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            Intent intent2 = new Intent(context, activity_weather.class);
            PendingIntent openWeatherActivity = PendingIntent.getActivity(context, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            views.setOnClickPendingIntent(R.id.widget_image, openWeatherActivity);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        appWidgetManager.updateAppWidget(componentName, view);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Intent intent = new Intent(context, WidgetService.class);
        context.startService(intent);
        hasWidget = true;
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        hasWidget = false;
        context.stopService(new Intent(context, WidgetService.class));
    }
}

