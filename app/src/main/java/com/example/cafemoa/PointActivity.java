package com.example.cafemoa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PointActivity extends AppCompatActivity {
    TextView pointCount = null;
    Button pointUp = null;
    Button pointDown = null;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);
    }

  
}
