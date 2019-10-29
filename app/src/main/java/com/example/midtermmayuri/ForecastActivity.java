package com.example.midtermmayuri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ForecastActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    RecyclerView recyclerView;
    TextView tv_title_city;
    String city ="";
    ArrayList<ForecastWeather> data = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        setTitle("Weather Forecast");
        recyclerView = findViewById(R.id.recyclerView);
        tv_title_city = findViewById(R.id.tv_title_for);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        if(getIntent() != null && getIntent().getExtras() != null){
            Log.d("demo", "hiii");
            city =  getIntent().getExtras().getString(CurrentWeatherActivity.CITY_KEY_WEATHER);
            Log.d("demo", city);
            tv_title_city.setText(city);
            if(isConnected()){
                String url = null;
                url = "https://api.openweathermap.org/data/2.5/forecast?q="+city+"&appid=e520d5646b91caf3b1f600bb57ef337f";
                new getAsynForecastWeather(city).execute(url);
                Log.d("demo", url);
            }else{
                Toast.makeText(ForecastActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
            }

        }

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

    private class getAsynForecastWeather extends AsyncTask<String, Void, List<ForecastWeather>> {

        String city;

        public getAsynForecastWeather(String city) {
            this.city = city;
        }

        @Override
        protected List<ForecastWeather> doInBackground(String... params) {
            CurrentWeather cw = null;
            HttpURLConnection connection = null;
            ArrayList<ForecastWeather> result = new ArrayList<>();
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
                    JSONArray weatherArray = root.getJSONArray("list");

                    for (int i = 0; i < weatherArray.length(); i++) {
                        JSONObject jsonObject = weatherArray.getJSONObject(i);
                        JSONObject mainBody = jsonObject.getJSONObject("main");
                        JSONObject weatherBody = jsonObject.getJSONArray("weather").getJSONObject(0);

                        String time = jsonObject.getString("dt_txt");
                        String icon = weatherBody.getString("icon");
                        String temp = mainBody.getString("temp");
                        String humidity = mainBody.getString("humidity");
                        String desc = weatherBody.getString("description");

                        //the time, the weather icon,
                        //temperature, humidity, and the condition

                        ForecastWeather weather = new ForecastWeather(time,icon,temp,humidity,desc);
                        Log.d("demo",weather.toString());
                        result.add(weather);
                    }

                }
            } catch (Exception e) {
                //Handle Exceptions
                Log.d("demo", e.getLocalizedMessage());
            } finally {
                //Close the connections
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<ForecastWeather> forecast) {

            for(ForecastWeather x: forecast){
                data.add(x);
            }

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(ForecastActivity.this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            mAdapter = new ForecastAdaptor(data);
            mRecyclerView.setAdapter(mAdapter);


        }
    }
}
