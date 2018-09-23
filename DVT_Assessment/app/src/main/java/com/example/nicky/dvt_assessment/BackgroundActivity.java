package com.example.nicky.dvt_assessment;

import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.view.Window;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BackgroundActivity extends AsyncTask<Void, Void, Void> {
    private String data, currentDay, currentTemp, currentTime, currentWeather, minTemp, maxTemp, temp, weather, time;
    private Date date;

    private String[] nextFiveTemps = new String[5];
    private String[] nextFiveDays = new String[5];
    private int[] nextFiveWeatherIcons = new int[5];

    private int[] weatherIcons = { R.drawable.icon_clear, R.drawable.icon_rainy, R.drawable.icon_cloudy };

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?q=" + MainActivity.cityName.toLowerCase() + "&units=metric&APPID=3203c1f37f597d1c471007ff9f143e7f");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = bufferedReader.readLine()) != null)
            {
                data += line;
            }

            data = data.substring(data.indexOf("temp") + 6, data.length());
            currentTemp = data.substring(0, data.indexOf('.')) + "°";

            data = data.substring(data.indexOf("temp_min") + 10, data.length());
            minTemp = data.substring(0, data.indexOf('.')) + "°";

            data = data.substring(data.indexOf("temp_max") + 10, data.length());
            maxTemp = data.substring(0, data.indexOf('.')) + "°";

            data = data.substring(data.indexOf("main") + 7, data.length());
            currentWeather = data.substring(0, data.indexOf('\"'));

            data = data.substring(data.indexOf("dt_txt") + 20, data.length());
            currentTime = data.substring(0,2);

            int count = 0;

            Date today = new Date();
            currentDay = new SimpleDateFormat("EEEE").format(today);

            Calendar cDateToCheck = Calendar.getInstance();
            cDateToCheck.setTime(today);

            cDateToCheck.add(Calendar.DAY_OF_YEAR, 1);

            for (int i = 0; i < 39; i++)
            {
                data = data.substring(data.indexOf("temp\":") + 6, data.length());
                temp = data.substring(0, data.indexOf('.'));

                data = data.substring(data.indexOf("main") + 7, data.length());
                weather = data.substring(0, data.indexOf('\"'));

                data = data.substring(data.indexOf("dt_txt") + 9, data.length());
                date = new SimpleDateFormat("yyyy-MM-dd").parse(data.substring(0,10));

                data = data.substring(11, data.length());
                time = data.substring(0,2);

                Calendar cDate = Calendar.getInstance();
                cDate.setTime(date);

                if (cDate.get(Calendar.DAY_OF_YEAR) == cDateToCheck.get(Calendar.DAY_OF_YEAR) && time.equals("12"))
                {
                    nextFiveTemps[count] = temp + "°";

                    nextFiveDays[count] = new SimpleDateFormat("EEEE").format(date);

                    switch (weather)
                    {
                        case "Clear":
                            nextFiveWeatherIcons[count] = weatherIcons[0];
                            break;
                        case "Rain":
                            nextFiveWeatherIcons[count] = weatherIcons[1];
                            break;
                        case "Clouds":
                            nextFiveWeatherIcons[count] = weatherIcons[2];
                            break;
                    }

                    cDateToCheck.add(Calendar.DAY_OF_YEAR, 1);
                    count++;
                }
            }

            if (nextFiveDays[4] == null)
            {
                nextFiveTemps[4] = temp + "°";

                nextFiveDays[4] = new SimpleDateFormat("EEEE").format(date);

                switch (weather)
                {
                    case "Clear":
                        nextFiveWeatherIcons[4] = weatherIcons[0];
                        break;
                    case "Rain":
                        nextFiveWeatherIcons[4] = weatherIcons[1];
                        break;
                    case "Clouds":
                        nextFiveWeatherIcons[4] = weatherIcons[2];
                        break;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        MainActivity.txtTemp.setText(currentTemp);

        switch (currentWeather)
        {
            case "Clear":
                if (Integer.parseInt(currentTime) >= 6 && Integer.parseInt(currentTime) < 18)
                {
                    MainActivity.txtWeather.setText("SUNNY");
                    MainActivity.imgBackground.setImageResource(R.drawable.forest_sunny);

                    MainActivity.constraintLayout.setBackgroundColor(Color.parseColor("#47AB2F"));
                    MainActivity.window.setStatusBarColor(Color.argb(255,255,213,164));
                }
                else
                {
                    MainActivity.txtWeather.setText("CLEAR");
                    MainActivity.imgBackground.setImageResource(R.drawable.forest_sunny_night);

                    MainActivity.constraintLayout.setBackgroundColor(Color.parseColor("#121C22"));
                    MainActivity.window.setStatusBarColor(Color.argb(255,18,33,47));
                }
                break;
            case "Rain":
                if (Integer.parseInt(currentTime) >= 6 && Integer.parseInt(currentTime) < 18)
                {
                    MainActivity.txtWeather.setText("RAINY");
                    MainActivity.imgBackground.setImageResource(R.drawable.forest_rainy);

                    MainActivity.constraintLayout.setBackgroundColor(Color.parseColor("#57575D"));
                    MainActivity.window.setStatusBarColor(Color.argb(255,115,115,115));
                }
                else
                {
                    MainActivity.txtWeather.setText("RAINY");
                    MainActivity.imgBackground.setImageResource(R.drawable.forest_rainy_night);

                    MainActivity.constraintLayout.setBackgroundColor(Color.parseColor("#161A21"));
                    MainActivity.window.setStatusBarColor(Color.argb(255,27,33,41));
                }
                break;
            case "Clouds":
                if (Integer.parseInt(currentTime) >= 6 && Integer.parseInt(currentTime) < 18)
                {
                    MainActivity.txtWeather.setText("CLOUDY");
                    MainActivity.imgBackground.setImageResource(R.drawable.forest_cloudy);

                    MainActivity.constraintLayout.setBackgroundColor(Color.parseColor("#54717A"));
                    MainActivity.window.setStatusBarColor(Color.argb(255,97,132,149));
                }
                else
                {
                    MainActivity.txtWeather.setText("CLOUDY");
                    MainActivity.imgBackground.setImageResource(R.drawable.forest_cloudy_night);

                    MainActivity.constraintLayout.setBackgroundColor(Color.parseColor("#121E27"));
                    MainActivity.window.setStatusBarColor(Color.argb(255,19,32,43));
                }
                break;
        }

        MainActivity.txtCurrentDay.setText(currentDay);

        MainActivity.txtTempMin.setText(minTemp);
        MainActivity.txtTempMax.setText(maxTemp);
        MainActivity.txtTempCurrent.setText(currentTemp);

        MainActivity.txtDay1.setText(nextFiveDays[0]);
        MainActivity.txtDay2.setText(nextFiveDays[1]);
        MainActivity.txtDay3.setText(nextFiveDays[2]);
        MainActivity.txtDay4.setText(nextFiveDays[3]);
        MainActivity.txtDay5.setText(nextFiveDays[4]);

        MainActivity.imgDay1Weather.setImageResource(nextFiveWeatherIcons[0]);
        MainActivity.imgDay2Weather.setImageResource(nextFiveWeatherIcons[1]);
        MainActivity.imgDay3Weather.setImageResource(nextFiveWeatherIcons[2]);
        MainActivity.imgDay4Weather.setImageResource(nextFiveWeatherIcons[3]);
        MainActivity.imgDay5Weather.setImageResource(nextFiveWeatherIcons[4]);

        MainActivity.txtDay1Temp.setText(nextFiveTemps[0]);
        MainActivity.txtDay2Temp.setText(nextFiveTemps[1]);
        MainActivity.txtDay3Temp.setText(nextFiveTemps[2]);
        MainActivity.txtDay4Temp.setText(nextFiveTemps[3]);
        MainActivity.txtDay5Temp.setText(nextFiveTemps[4]);
    }
}
