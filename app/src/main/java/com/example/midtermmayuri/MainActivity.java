package com.example.midtermmayuri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ListView listView;
    TextView tv_inst;
    public static final String CITY_KEY = "City";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<String> list = new ArrayList<>();
        listView = findViewById(R.id.listview);
        tv_inst = findViewById(R.id.tv_inst);
        tv_inst.setText(getResources().getString(R.string.inst_value));

        setTitle("Select City");

        String jsonString = loadJSONFromAsset(MainActivity.this);
        Log.d("demo", jsonString);

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray listarray = jsonObject.getJSONArray("data");
            for (int i = 0; i < listarray.length(); i++) {
                JSONObject object = listarray.getJSONObject(i);
                String city = object.getString("city");
                String country = object.getString("country");
                list.add(city+','+country);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                        android.R.id.text1, list);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("demo", "Clicked item " + position + ", color " + list.get(position));

                Intent intent = new Intent(MainActivity.this, CurrentWeatherActivity.class);
                intent.putExtra(CITY_KEY,list.get(position));
                startActivity(intent);
            }
        });

    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("cities.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}
