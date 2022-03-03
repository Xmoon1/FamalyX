package com.example.familyx.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;

import com.example.familyx.databinding.ActivityInterlocuterBinding;
import com.example.familyx.models.User;
import com.example.familyx.utilities.Constants;
import com.example.familyx.utilities.PreferenceManager;

public class InterlocutorActivity extends AppCompatActivity {

    ActivityInterlocuterBinding binding;
    private User receiverUser;
    PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInterlocuterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());

        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.imageBack2.setOnClickListener(v -> onBackPressed());


    }

}