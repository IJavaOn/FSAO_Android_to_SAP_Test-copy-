package com.sap.copy.mdui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.sap.copy.R;

public class CreateNotification extends AppCompatActivity {

    private EditText editTextReporter;
    private EditText editTextEquipment;
    //  private RadioGroup radiogroup;
    private EditText editTextDescription;
    private Button button_clear;
    private Button button_create;
    private ImageButton back1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notification);


            editTextReporter = findViewById(R.id.editTextReporter);
            editTextEquipment = findViewById(R.id.editTextEquipment);

            // radiogroup = findViewById(R.id.radiogroup);

            editTextDescription = findViewById(R.id.editTextDescription);
            button_clear = findViewById(R.id.button_clear);
            button_create = findViewById(R.id.button_create);


            back1 = findViewById(R.id.back1);
            back1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CreateNotification.this, HomePage.class);
                    startActivity(intent);
                }
            });


            button_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //to get string value
                    String text = editTextReporter.getText().toString();
                    String text1 = editTextEquipment.getText().toString();
                    String text2 = editTextDescription.getText().toString();

                    //to clear out values
                    editTextReporter.setText("");
                    editTextEquipment.setText("");
                    editTextDescription.setText("");
                }
            });

            button_create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //to get string value
                    String text = editTextReporter.getText().toString();
                    String text1 = editTextEquipment.getText().toString();
                    String text2 = editTextDescription.getText().toString();

                    //navigate to MyNotifications page
                    Intent intent = new Intent(CreateNotification.this,DisplayNotification.class);
                    intent.putExtra("reporter",text);
                    intent.putExtra("equipment",text1);
                    intent.putExtra("description",text2);
                    startActivity(intent);

                }
            });


        }
}