package com.example.familyx.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.familyx.R;
import com.example.familyx.databinding.ActivitySignInBinding;
import com.example.familyx.utilities.Constants;
import com.example.familyx.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {

    PreferenceManager preferenceManager;
    private ActivitySignInBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        progressBarSpinner();
    }

    private void setListeners(){
        binding.textCreateNewAccount.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));
        binding.buttonSignIn.setOnClickListener(v -> {
            if(isValidSignUpDetails()){
                signIn();
            }
        });
    }

    private void progressBarSpinner(){
        ProgressBar spinner = new android.widget.ProgressBar(
                getApplicationContext(),
                null,
                android.R.attr.progressBarStyle);

        spinner.getIndeterminateDrawable().setColorFilter(212121, android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    private void signIn() {
        loading(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_PHONE, binding.inputPhone.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null
                            && task.getResult().getDocumentChanges().size() > 0){
                          DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                          preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                          preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                          preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                          preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                              Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                              intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                              startActivity(intent);
                    }else {
                        loading(false);
                        showToast("Unable to sign in");
                    }
                });

    }

    private void loading(Boolean isVisible){
        if(isVisible){
            binding.buttonSignIn.setVisibility(View.INVISIBLE);
            binding.progressBarr.setVisibility(View.VISIBLE);
        }else{
            binding.progressBarr.setVisibility(View.INVISIBLE);
            binding.buttonSignIn.setVisibility(View.VISIBLE);
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
                .show();
    }

    private Boolean isValidSignUpDetails() {
        if (binding.inputPhone.getText().toString().trim().isEmpty()) {
            showToast("Enter Phone");
            return false;
        } else if (!Patterns.PHONE.matcher(binding.inputPhone .getText().toString()).matches()) {
            showToast("Enter valid phone");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter Password");
            return false;
        }else{
            return true;
        }
    }
}