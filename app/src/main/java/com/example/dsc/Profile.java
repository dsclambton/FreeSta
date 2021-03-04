package com.example.dsc;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class Profile extends AppCompatActivity {
    ImageView coverPhoto, profile;
    EditText name, gender, location, phone;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_profile );

        coverPhoto = findViewById( R.id.cover );
        profile = findViewById(R.id.picture);
        name = findViewById(R.id.name);
        gender = findViewById(R.id.gender);
        location = findViewById(R.id.location);
        phone = findViewById(R.id.phone);
        save = findViewById(R.id.save);
    }
}