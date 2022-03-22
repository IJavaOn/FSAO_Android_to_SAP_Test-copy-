package com.sap.copy.mdui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sap.copy.R;

import java.text.DateFormat;
import java.util.Calendar;

public class HomePage extends AppCompatActivity {

    private ImageButton imageButtoncrt;
    private ImageButton mynoti;
    private ImageButton work;
    private ImageButton test;
    private ImageButton test2;
    private ImageButton TestUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        imageButtoncrt = findViewById(R.id.imageButtoncrt);
        mynoti = findViewById(R.id.mynoti);
        work = findViewById(R.id.work);
        test = findViewById(R.id.test);
        test2 = findViewById(R.id.test2);
        TestUI = findViewById(R.id.TestUI);

        //date formatted value to dis
        Calendar calendar= Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());

        //  Date currentTime = Calendar.getInstance().getTime();
        // String currentDate = DateFormat.getTimeInstance(DateFormat.FULL).format(currentTime);

        TextView textViewDate = findViewById(R.id.textViewDate);
        textViewDate.setText(currentDate);

        //CreateNotification Button
        imageButtoncrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, CreateNotification.class);
                startActivity(intent);
            }
        });

        //NotficationList Button
        mynoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this,NotificationList.class);
                startActivity(intent);
            }
        });

        //Workorder Button
        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this,WorkOrder.class);
                startActivity(intent);
            }
        });

        //Test Button
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this,Test.class);
                startActivity(intent);
            }
        });

        //Test2 Button
        test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, Test2.class);
                startActivity(intent);
            }
        });

        //TestUI Button
        TestUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, TestUI.class);
                startActivity(intent);
            }
        });


    }
}