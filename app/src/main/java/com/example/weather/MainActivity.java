package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView resultText;
    @SuppressLint("SetTextI18n")
    public void getWeather(View view){
        resultText.setText("Please Wait, we are getting the information");

        try {
            String encodedCityName = URLEncoder.encode(editText.getText().toString(),"UTF-8");
        } catch (Exception e) {
            resultText.setText("404 NOT FOUND");
            e.printStackTrace();
        }
        DownloadLoadTask task = new DownloadLoadTask();
        task.execute("https://openweathermap.org/data/2.5/weather?q=" + editText.getText().toString() + "&appid=439d4b804bc8187953eb36d2a8c26a02");

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);

    }


    public class DownloadLoadTask extends AsyncTask<String,Void,String > {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection connection = null;

            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader((in));
                int data = reader.read();

                while(data!=-1){
                    char current = (char)data ;
                    result +=current;
                    data=reader.read();
                }
                return result;


            } catch (Exception e){
                resultText.setText("404 NOT FOUND");
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);

                String weatherInfo = jsonObject.getString("weather");

                Log.i("weather content", weatherInfo);

                JSONArray arrweather = new JSONArray(weatherInfo);
                String message = "";

                for(int i=0;i<arrweather.length();i++){
                    JSONObject jsonPart = arrweather.getJSONObject(i);

                    String main = jsonPart.getString("main");
                    String description = jsonPart.getString("description");

                    if(!main.equals("")  && !description.equals("")){
                        message += main + ":" + description;
                    }
                }
/*

                String mainInfo = jsonObject.getString("main");


                JSONArray arrmain = new JSONArray(mainInfo);

                for(int i=0;i<arrmain.length();i++){
                    JSONObject jsonPart = arrmain.getJSONObject(i);

                    String temp = jsonPart.getString("temp");
                    String pressure = jsonPart.getString("pressure");
                    String humidity = jsonPart.getString("humidity");

                    if(!temp.equals("")  && !pressure.equals("") && !humidity.equals("")){
                        message += "temperature" + ":" + temp;
                        message += "pressure" + ":" + pressure;
                        message += "humidity" + ":" + humidity;
                    }
                }
/*
                String windInfo = jsonObject.getString("wind");

/*

                JSONArray arrwind = new JSONArray(windInfo);

                for(int i=0;i<arrwind.length();i++){
                    JSONObject jsonPart = arrwind.getJSONObject(i);

                    String speed = jsonPart.getString("speed");
                    String deg = jsonPart.getString("deg");

                    if(!speed.equals("")  && !deg.equals("")){
                        message += "speed" + ":" + speed;
                        message += "degree" + ":" + deg;
                    }
                }*/




                if(!message.equals("")){
                    resultText.setText(message);
                }else{
                    resultText.setText("404 NOT FOUND");
                }
            } catch (Exception e) {
                resultText.setText("404 NOT FOUND");
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        resultText = findViewById(R.id.textView2);

    }
}
