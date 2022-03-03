package com.example.familyx.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;

import com.example.familyx.R;
import com.example.familyx.adapters.ChatAdapter;
import com.example.familyx.adapters.UsersAdapter;
import com.example.familyx.databinding.ActivityProfileBinding;
import com.example.familyx.databinding.ActivitySettingsBinding;
import com.example.familyx.models.User;
import com.example.familyx.utilities.Constants;
import com.example.familyx.utilities.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;
    BottomNavigationView bottomNavigationView;
    String encodedImage;
    PreferenceManager preferenceManager;
    public User receiverUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        getUserInfo();
        getUsers();

        binding.name.setText(preferenceManager.getString(Constants.KEY_NAME));
        binding.phone.setText(preferenceManager.getString(Constants.KEY_PHONE));

        binding.bottomNavigationViewSettings.setSelectedItemId(R.id.settings);
        binding.bottomNavigationViewSettings.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.settings:
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.calls:
                        startActivity(new Intent(getApplicationContext(), CallsActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    private void getUsers(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                        List<User> users = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            User user = new User();
                            user.phone = queryDocumentSnapshot.getString(Constants.KEY_PHONE);
                            binding.phone.setText(queryDocumentSnapshot.getString(Constants.KEY_PHONE));

                        }
                });
    }



    private void getUserInfo() {
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.imageInfoButton.setImageBitmap(bitmap);
    }

}