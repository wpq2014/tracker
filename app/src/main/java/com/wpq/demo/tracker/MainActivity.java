package com.wpq.demo.tracker;

import android.os.Bundle;

import com.wpq.tracker.expose.Tracker;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Tracker.event1("小coder", 3);
        Tracker.event1(123, "恰饭");
        Tracker.event2(new HashMap<String, Object>());
    }
}
