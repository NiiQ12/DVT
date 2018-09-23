package com.example.nicky.dvt_assessment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public static ConstraintLayout constraintLayout;
    public static Window window;

    public static TextView txtTemp, txtWeather, txtCurrentDay;
    public static TextView txtTempMin, txtTempCurrent, txtTempMax;
    public static TextView txtDay1, txtDay2, txtDay3, txtDay4, txtDay5;
    public static TextView txtDay1Temp, txtDay2Temp, txtDay3Temp, txtDay4Temp, txtDay5Temp;

    public static ImageView imgDay1Weather, imgDay2Weather, imgDay3Weather, imgDay4Weather, imgDay5Weather;
    public static ImageView imgBackground;

    public static String cityName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        window = this.getWindow();
        constraintLayout = findViewById(R.id.constraintLayout);

        txtTemp = findViewById(R.id.txtTemp);
        txtWeather = findViewById(R.id.txtWeather);
        txtCurrentDay = findViewById(R.id.txtCurrentDay);

        txtTempMin = findViewById(R.id.txtTempMin);
        txtTempCurrent = findViewById(R.id.txtTempCurrent);
        txtTempMax = findViewById(R.id.txtTempMax);

        txtDay1 = findViewById(R.id.txtDay1);
        txtDay2 = findViewById(R.id.txtDay2);
        txtDay3 = findViewById(R.id.txtDay3);
        txtDay4 = findViewById(R.id.txtDay4);
        txtDay5 = findViewById(R.id.txtDay5);

        imgDay1Weather = findViewById(R.id.imgDay1Weather);
        imgDay2Weather = findViewById(R.id.imgDay2Weather);
        imgDay3Weather = findViewById(R.id.imgDay3Weather);
        imgDay4Weather = findViewById(R.id.imgDay4Weather);
        imgDay5Weather = findViewById(R.id.imgDay5Weather);

        txtDay1Temp = findViewById(R.id.txtDay1Temp);
        txtDay2Temp = findViewById(R.id.txtDay2Temp);
        txtDay3Temp = findViewById(R.id.txtDay3Temp);
        txtDay4Temp = findViewById(R.id.txtDay4Temp);
        txtDay5Temp = findViewById(R.id.txtDay5Temp);

        imgBackground = findViewById(R.id.imgBackground);

        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        }
        else
        {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            try
            {
                cityName = getLocation(location.getLatitude(), location.getLongitude());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(this, "City not found!", Toast.LENGTH_LONG).show();
            }

            Timer timer = new Timer();

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    BackgroundActivity backgroundActivity = new BackgroundActivity();
                    backgroundActivity.execute();
                }
            };

            timer.schedule(timerTask, 0l, 1000*5*60);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                try
                {
                    cityName = getLocation(location.getLatitude(), location.getLongitude());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(this, "City not found!", Toast.LENGTH_LONG).show();
                }

                Timer timer = new Timer();

                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        BackgroundActivity backgroundActivity = new BackgroundActivity();
                        backgroundActivity.execute();
                    }
                };

                timer.schedule(timerTask, 0l, 1000*5*60);
            }
            else
            {
                System.exit(0);
            }
        }
    }

    private String getLocation(double lat, double lon)
    {
        String cityName = "";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(lat, lon, 10);

            if (addresses.size() > 0)
            {
                for (Address adr : addresses)
                {
                    if (adr.getLocality() != null && adr.getLocality().length() > 0)
                    {
                        cityName = adr.getLocality();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cityName;
    }
}
