package com.example.caloriecounter;

import android.os.Bundle;
import android.view.View;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Second extends AppCompatActivity {

    EditText height;
    Button calculateButton;
    EditText weight;
    EditText age;
    EditText gender;
    String total = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        calculateButton = findViewById(R.id.id_calculateCalories);
        height = findViewById(R.id.id_height);
        weight = findViewById(R.id.id_weight);
        age = findViewById(R.id.id_age);
        gender = findViewById(R.id.id_gender);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                total = height.getText() + "/" + weight.getText() + "/" + age.getText() + "/" + gender.getText();

                Intent sendInfoBack = new Intent();
                sendInfoBack.putExtra(MainActivity.IntentCodeX,total);
                setResult(RESULT_OK,sendInfoBack);
                finish();
            }
        });
    }

}