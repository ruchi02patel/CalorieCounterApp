package com.example.caloriecounter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    Button addItem;
    Button enterData;
    TextView calorieIntake;
    TextView calorieNum;
    int caloriesX=0;
    String bodyInput;
    final String TAG = "COUNTERTAG";
    int counter = 0;
    static final int NUMBER_CODE = 12345;
    static final String INTENT_CODE = "CODE";
    static final String IntentCodeX = "CODEX";
    String[] arr = new String[4];
    double r;
    Button reset;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data.hasExtra("CODE")){
            caloriesX +=data.getIntExtra("CODE",0);
        }
        else if (data.hasExtra(IntentCodeX))
        {
            bodyInput = data.getStringExtra(IntentCodeX);
            arr = bodyInput.split("/");
            if (arr[3].toUpperCase().equals("M"))
            {
                r = 66+(6.2*(Integer.parseInt(arr[1])))+(12.7*(Integer.parseInt(arr[0])))-(6.76*(Integer.parseInt(arr[2])));
            }
            else
            {
                r = 655.1+(4.35*(Integer.parseInt(arr[1])))+(4.7*(Integer.parseInt(arr[0])))-(4.7*(Integer.parseInt(arr[2])));

            }
            calorieIntake.setText("Recommended Daily Calorie Intake: "+(int)r);
        }

        Log.d("heyyy", caloriesX+" ");
        calorieNum.setText(caloriesX+"");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calorieNum = findViewById(R.id.id_calorieNum);
        addItem = findViewById(R.id.id_addItem);
        reset = findViewById(R.id.id_resetCalories);
        enterData = findViewById(R.id.id_calculate);
        calorieIntake = findViewById(R.id.id_calorieIntake);

        Log.d("inoncreate", caloriesX+"");
        calorieIntake.setVisibility(View.VISIBLE);

        enterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToOpen = new Intent(MainActivity.this, Second.class);
                startActivityForResult(intentToOpen, NUMBER_CODE);
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToOpen2 = new Intent(MainActivity.this, text.class);
                startActivityForResult(intentToOpen2, NUMBER_CODE);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caloriesX = 0;
                calorieNum.setText(caloriesX+"");
            }
        });

    }
}

