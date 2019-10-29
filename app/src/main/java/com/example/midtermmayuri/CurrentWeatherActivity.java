package com.example.midtermmayuri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class CurrentWeatherActivity extends AppCompatActivity {

    TextView tv_title;
    TextView tv_temp;
    TextView tv_temp_max;
    TextView tv_temp_min;
    TextView tv_desc;
    TextView tv_humid;
    TextView tv_wind;
    Button bt_forecast;
    ImageView im_icon;
    public static final String CITY_KEY_WEATHER = "City_weather";
    String city = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_weather);

        setTitle("Current Weather");

        tv_title = findViewById(R.id.tv_title);
        tv_temp = findViewById(R.id.tv_temp_value);
        tv_temp_max = findViewById(R.id.tv_temp_maxvalue);
        tv_temp_min = findViewById(R.id.tv_temp_minvalue);
        tv_desc = findViewById(R.id.tv_desc_value);
        tv_humid = findViewById(R.id.tv_humid_value);
        tv_wind = findViewById(R.id.tv_wind_value);
        bt_forecast = findViewById(R.id.bt_forecast);
        im_icon = findViewById(R.id.im_icon);


        if(getIntent() != null && getIntent().getExtras() != null){
            Log.d("demo", "hiii");
           city =  getIntent().getExtras().getString(MainActivity.CITY_KEY);
            Log.d("demo", city);
            if(isConnected()){
                String url = null;
                url = "https://api.openweathermap.org/data/2.5/weather?q="+ city+"&appid=e520d5646b91caf3b1f600bb57ef337f";
                new getAsynCurrentWeather(city).execute(url);
                Log.d("demo", url);
            }else{
                Toast.makeText(CurrentWeatherActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
            }

        }

        bt_forecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CurrentWeatherActivity.this, ForecastActivity.class);
                intent.putExtra(CITY_KEY_WEATHER,city );
                startActivity(intent);

            }
        });

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    private class getAsynCurrentWeather extends AsyncTask<String, Void, CurrentWeather> {

        String city;

        public getAsynCurrentWeather(String city) {
            this.city = city;
        }

        @Override
        protected CurrentWeather doInBackground(String... params) {
            CurrentWeather cw = null;
            HttpURLConnection connection = null;
            //ArrayList<Music> result = new ArrayList<>();
            try {
                URL url = new URL(params[0]);
                Log.d("demo","hiiii2");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                Log.d("demo","outside connection");
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.d("demo","inside connection");
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject root = new JSONObject(json);
                    JSONObject weatherBody = root.getJSONArray("weather").getJSONObject(0);
                    JSONObject mainBody = root.getJSONObject("main");
                    JSONObject windBody = root.getJSONObject("wind");

                    String desc = weatherBody.getString("description");
                    String icon = weatherBody.getString("icon");
                    String temp = mainBody.getString("temp");
                    String tempMax = mainBody.getString("temp_max");
                    String tempMin = mainBody.getString("temp_min");
                    String windSpeed = windBody.getString("speed");
                    String humidity = mainBody.getString("humidity");
//City, String temp, String tempMin, String tempMax, String desc, String humidity, String windSpeed
                    cw = new CurrentWeather(city,temp, tempMin,tempMax,desc,humidity,windSpeed,icon);

                    Log.d("demo", cw.toString());

                }
            } catch (Exception e) {
                //Handle Exceptions
                Log.d("demo", e.getLocalizedMessage());
            } finally {
                //Close the connections
            }
            return cw;
        }

        @Override
        protected void onPostExecute(CurrentWeather currentWeather) {

            if(currentWeather != null){
                tv_title.setText(currentWeather.City);
                tv_temp.setText(currentWeather.temp+" F");
                tv_temp_max.setText(currentWeather.tempMax+" F");
                tv_temp_min.setText(currentWeather.tempMin+" F");
                tv_desc.setText(currentWeather.desc);
                Log.d("demo",currentWeather.humidity );
                tv_humid.setText(currentWeather.humidity+" %");
                tv_wind.setText(currentWeather.windSpeed+" miles/hr");
                String iconUrl = "http://openweathermap.org/img/wn/"+currentWeather.icon+"@2x.png";
                Picasso.get().load(iconUrl).into(im_icon);
            }else{
                Log.d("demo", "value is null");
            }



        }
    }
}
