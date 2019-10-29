package com.example.midtermmayuri;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ForecastAdaptor extends RecyclerView.Adapter<ForecastAdaptor.ViewHolder> {
    ArrayList<ForecastWeather> data;

    public ForecastAdaptor(ArrayList<ForecastWeather> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ForecastWeather weather = data.get(position);

        holder.texttemp.setText(weather.temp + " F");
        Date inputDate;
        Date outputDate = null;
        String formattedDateString = null;
        String prettyTimeString;
                //2019-10-22 03:00:00
        SimpleDateFormat convertToDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat newDateFormat = new SimpleDateFormat("hh:mm a");
        try {
            inputDate=convertToDate.parse(weather.time);
            formattedDateString=newDateFormat.format(inputDate);
            outputDate=newDateFormat.parse(formattedDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        PrettyTime prettyTime=new PrettyTime();
        prettyTimeString=prettyTime.format(outputDate);
        holder.textTime.setText("At " +formattedDateString);


        //holder.textTime.setText(weather.time);
        holder.textHumid.setText(weather.humid +" %");
        holder.textDesc.setText(weather.condition);
        String url = "http://openweathermap.org/img/wn/"+weather.icon+"@2x.png";
        Picasso.get().load(url).into(holder.im_icon);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView texttemp, textTime, textHumid, textDesc;
        ImageView im_icon;
        ForecastWeather weather;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            texttemp = itemView.findViewById(R.id.tv_temp_for);
            textTime = itemView.findViewById(R.id.tv_time_for);
            textHumid = itemView.findViewById(R.id.tv_humid_for);
            textDesc = itemView.findViewById(R.id.tv_desc_for);
            im_icon = itemView.findViewById(R.id.im_icon_for);

        }
    }
}
