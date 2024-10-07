package com.example.resumescreening;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class DropDown extends AppCompatActivity {

    private Spinner religionSpinner;
    private Spinner classificationSpinner;
    private static final String TAG = "DropDownActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initializing the Spinners from the layout file

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
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when nothing is selected
            }
        });
    }

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
