package com.rodneygeerlings.myflightinfo;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.rodneygeerlings.myflightinfo.R;
import com.rodneygeerlings.myflightinfo.activities.ContactActivity;
import com.rodneygeerlings.myflightinfo.activities.FlightActivity;
import com.rodneygeerlings.myflightinfo.activities.WeatherActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                Button contactInfo = findViewById(R.id.contactInfo);
                Button weatherInfo = findViewById(R.id.weatherInfo);
                Button flightInfo = findViewById(R.id.flightInfo);

                contactInfo.setOnClickListener(this);
                weatherInfo.setOnClickListener(this);
                flightInfo.setOnClickListener(this);

            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.contactInfo:
                        Intent intent = new Intent(this, ContactActivity.class);
                        startActivity(intent);

                        break;
                    case R.id.weatherInfo:
                        Intent intent2 = new Intent(this, WeatherActivity.class);
                        startActivity(intent2);

                        break;
                    case R.id.flightInfo:
                        Intent intent3 = new Intent(this, FlightActivity.class);
                        startActivity(intent3);

                        break;

                }
            }
        }