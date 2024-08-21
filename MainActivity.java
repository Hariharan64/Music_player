package com.example.qradmin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private EditText phoneNumberEditText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(v -> {
            String phoneNumber = phoneNumberEditText.getText().toString().trim();
            if (!phoneNumber.isEmpty()) {
                // Generate a unique key for each user
                String userId = mDatabase.push().getKey();
                if (userId != null) {
                    // Create a new User object
                    User user = new User(phoneNumber);

                    // Store the user with the unique ID
                    mDatabase.child("users").child(userId).setValue(user)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Phone number saved with ID: " + userId, Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Failed to save phone number.", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            } else {
                Toast.makeText(MainActivity.this, "Phone number cannot be empty.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
