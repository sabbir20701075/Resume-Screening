package com.example.resumescreening;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    private EditText signupName, signupUsername, signupEmail, signupPassword;
    private TextView loginRedirectText;
    private Button signupButton;
    private Spinner religionSpinner, classificationSpinner;
    private static final String TAG = "SignUpActivity";

    private FirebaseDatabase database;
    private DatabaseReference usersReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersReference = database.getReference("users");

        // Initialize UI components
        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupButton = findViewById(R.id.signup_button);
        religionSpinner = findViewById(R.id.signup_religion_spinner);
        classificationSpinner = findViewById(R.id.classification_spinner);

        // Setting up the religion spinner
        ArrayAdapter<CharSequence> religionAdapter = ArrayAdapter.createFromResource(this,
                R.array.religions_array, android.R.layout.simple_spinner_item);
        religionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        religionSpinner.setAdapter(religionAdapter);

        // Set the listener for the religion spinner
        religionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Selected religion position: " + position);
                populateClassificationSpinner(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Realtime Username Validation
        signupUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    checkUsernameAvailability(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });
    }

    // Realtime Username Availability Check
    private void checkUsernameAvailability(String username) {
        usersReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    signupUsername.setError("Username already exists");
                } else {
                    signupUsername.setError(null); // Username available
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "checkUsernameAvailability:cancelled", databaseError.toException());
            }
        });
    }

    // Sign up function
    private void signUp() {
        // Validate input fields
        final String name = signupName.getText().toString();
        final String email = signupEmail.getText().toString();
        final String username = signupUsername.getText().toString();
        final String password = signupPassword.getText().toString();
        final String religion = religionSpinner.getSelectedItem().toString();

        if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the username already exists in the database
        usersReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(SignUpActivity.this, "Username already exists, please choose another one.", Toast.LENGTH_SHORT).show();
                } else {
                    // Username does not exist, proceed with account creation
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (user != null) {
                                            // Send email verification
                                            user.sendEmailVerification()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                // Store user data
                                                                HelperClass helperClass = new HelperClass(name, email, username, password, religion);
                                                                usersReference.child(username).setValue(helperClass);
                                                                Toast.makeText(SignUpActivity.this, "Verification email sent to " + email, Toast.LENGTH_SHORT).show();

                                                                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                                                                startActivity(intent);
                                                            } else {
                                                                Toast.makeText(SignUpActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SignUpActivity.this, "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Populate classification spinner based on religion selection
    private void populateClassificationSpinner(int position) {
        ArrayAdapter<CharSequence> classificationAdapter;
        switch (position) {
            case 0: // Muslim
                classificationAdapter = ArrayAdapter.createFromResource(this,
                        R.array.muslim_classifications, android.R.layout.simple_spinner_item);
                break;
            case 1: // Hindu
                classificationAdapter = ArrayAdapter.createFromResource(this,
                        R.array.hindu_classifications, android.R.layout.simple_spinner_item);
                break;
            case 2: // Christian
                classificationAdapter = ArrayAdapter.createFromResource(this,
                        R.array.christian_classifications, android.R.layout.simple_spinner_item);
                break;
            default:
                classificationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{});
                break;
        }
        classificationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classificationSpinner.setAdapter(classificationAdapter);
    }
}
