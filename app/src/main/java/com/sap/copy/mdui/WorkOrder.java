package com.sap.copy.mdui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sap.copy.R;

public class WorkOrder extends AppCompatActivity {

    private ImageButton navback1;
    private ImageView navigate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_order);

        navback1 = findViewById(R.id.navback1);
        navigate = findViewById(R.id.navigate);



        navback1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorkOrder.this, HomePage.class);
                startActivity(intent);
            }
        });

        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Intent intent = new Intent(WorkOrder.this, Navigate.class);

                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),Navigate.class);
                startActivity(intent);

                //underline after click
               // navigate.setPaintFlags(navigate.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


            }
        });


    }
}