package com.example.weather;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.adapter.DaysMoreAdapter;
import com.example.weather.data.DaysData;
import com.example.weather.util.SpfUtil;
import com.facebook.stetho.Stetho;
import com.google.gson.Gson;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.view.QWeather;

import java.util.ArrayList;
import java.util.List;


public class DaysMoreActivity extends AppCompatActivity implements View.OnClickListener {
    private String LocationID;
    private List<DaysData> daysDataList = new ArrayList<>();
    private DaysMoreAdapter daysMoreAdapter;
    private RecyclerView daysMore;

    public static final String KEY_WALLPAPER = "wallpaper_id";
    public static final String KEY_IMAGEPATH = "image_path";
    public static final String WALLPAPER_CHOOSE = "sourceofwallpaper";
    public static final int SOUCREOFLOCAL = 0;
    public static final int SOUCREOFSYSTEM = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daysmore);
        Stetho.initializeWithDefaults(this);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        Intent intent = getIntent();
        changeBackGround();
        LocationID = intent.getStringExtra("id");
        daysMore = findViewById(R.id.daysMore);
        findViewById(R.id.btn_back).setOnClickListener(this);
        getDaysMore();
    }

    private void changeBackGround() {

        int sourceofwallpaper = SpfUtil.getIntWithDefault(this, WALLPAPER_CHOOSE, SOUCREOFSYSTEM);
        if (sourceofwallpaper == 0) {
            String imagePath = SpfUtil.getString(this, KEY_IMAGEPATH);
            if (imagePath != null) {

                LinearLayout layout = findViewById(R.id.daysmore);
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                Drawable drawable = new BitmapDrawable(bitmap);
                layout.setBackground(drawable);
            }
        } else {
            int wallpaper = SpfUtil.getIntWithDefault(this, KEY_WALLPAPER, R.drawable.wallpaper0);
            LinearLayout layout = findViewById(R.id.daysmore);
            layout.setBackgroundResource(wallpaper);
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_back) {
            Intent intent = new Intent(DaysMoreActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }


    public void getDaysMore() {
        QWeather.getWeather7D(DaysMoreActivity.this, LocationID, new QWeather.OnResultWeatherDailyListener() {

            private static final String TAG = "hefengMore";

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError", e);
                System.out.println("Rain DaysMore Error:" + new Gson());
            }

            @Override
            public void onSuccess(WeatherDailyBean weatherDailyBean) {
                Log.i(TAG, "onSuccess" + new Gson().toJson(weatherDailyBean).replaceAll("https", "h22ps"));

                if (Code.OK == weatherDailyBean.getCode()) {
                    List<WeatherDailyBean.DailyBean> dailyBeanList = weatherDailyBean.getDaily();
                    for (int i = 0; i < dailyBeanList.size(); i++) {
                        WeatherDailyBean.DailyBean dailyBean = dailyBeanList.get(i);
                        String fxDate = dailyBean.getFxDate();
                        String tempMax = dailyBean.getTempMax();
                        String tempMin = dailyBean.getTempMin();
                        String iconDay = dailyBean.getIconDay();
                        String iconNight = dailyBean.getIconNight();
                        String wind360Day = dailyBean.getWind360Day();
                        String windDirDay = dailyBean.getWindDirDay();
                        String windScaleDay = dailyBean.getWindScaleDay();
                        String windSpeedDay = dailyBean.getWindSpeedDay();
                        String wind360Night = dailyBean.getWind360Night();
                        String windDirNight = dailyBean.getWindDirNight();
                        String windScaleNight = dailyBean.getWindScaleNight();
                        String windSpeedNight = dailyBean.getWindSpeedNight();
                        String humidity = dailyBean.getHumidity();
                        String precip = dailyBean.getPrecip();
                        String pressure = dailyBean.getPressure();
                        String cloud = dailyBean.getCloud();
                        String uvIndex = dailyBean.getUvIndex();
                        String vis = dailyBean.getVis();
                        String textDay = dailyBean.getTextDay();
                        String textNight = dailyBean.getTextNight();
                        DaysData daysData = new DaysData(fxDate, tempMax, tempMin, iconDay, textDay, iconNight, textNight, wind360Day, windDirDay, windScaleDay, windSpeedDay, wind360Night, windDirNight, windScaleNight, windSpeedNight, humidity, precip, pressure, cloud, uvIndex, vis);
                        daysDataList.add(daysData);
                    }
                    Log.d(TAG, "onSuccess: " + daysDataList);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            daysMoreAdapter = new DaysMoreAdapter(daysDataList);
                            daysMore.setAdapter(daysMoreAdapter);
                            daysMore.setLayoutManager(new LinearLayoutManager(DaysMoreActivity.this, RecyclerView.HORIZONTAL, false));
                        }
                    });
                } else {
                    Log.i(TAG, "failed code: " + weatherDailyBean.getCode());
                }
            }
        });
    }



}