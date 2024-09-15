package com.example.contextmonitoringapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Symptoms extends AppCompatActivity {

    private Spinner dropdownSpinner;
    private LinearLayout stackedItemsLayout;
    private Button uploadSymptomsButton;
    private DatabaseHelper databaseHelper;
    private int heartBeatCount = 0;
    private int respiratoryCount = 0;

    private RatingBar[] symptomRatingBars = new RatingBar[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_symptoms);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dropdownSpinner = findViewById(R.id.dropdownSpinner);
        Button addButton = findViewById(R.id.addButton);
        stackedItemsLayout = findViewById(R.id.stackedItemsLayout);
        uploadSymptomsButton = findViewById(R.id.uploadSymptoms);
        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        heartBeatCount = intent.getIntExtra("heartBeats", 0);
        respiratoryCount = intent.getIntExtra("respiratoryBeats", 0);

        String[] options = {"Cough", "Cold", "Fever", "Sore Throat", "Running Nose", "Headache", "Watery eyes", "Tiredness", "Breathlessness", "Nausea"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownSpinner.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedItem = dropdownSpinner.getSelectedItem().toString();
                addStackedItemWithRating(selectedItem);
            }
        });

        uploadSymptomsButton.setOnClickListener(v -> saveAllDataToDatabase());
    }

    private void addStackedItemWithRating(String selectedItem) {
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView textView = new TextView(this);
        textView.setText(selectedItem);
        textView.setTextSize(16f);
        textView.setPadding(0, 0, 16, 0);

        RatingBar ratingBar = new RatingBar(this);
        ratingBar.setNumStars(5);
        ratingBar.setStepSize(1f);
        ratingBar.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        itemLayout.addView(textView);
        itemLayout.addView(ratingBar);

        stackedItemsLayout.addView(itemLayout);

        int symptomIndex = stackedItemsLayout.getChildCount() - 1;
        if (symptomIndex < 10) {
            symptomRatingBars[symptomIndex] = ratingBar;
        }
    }

    private void saveAllDataToDatabase() {

        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        int[] symptoms = new int[10];
        for (int i = 0; i < symptomRatingBars.length; i++) {
            if (symptomRatingBars[i] != null) {
                symptoms[i] = (int) symptomRatingBars[i].getRating();
            }
        }

        databaseHelper.insertHealthData(currentTime, symptoms, heartBeatCount, respiratoryCount);
        Toast.makeText(this, "Health data saved successfully", Toast.LENGTH_SHORT).show();
    }
}