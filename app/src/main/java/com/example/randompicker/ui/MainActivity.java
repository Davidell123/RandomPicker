package com.example.randompicker.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.randompicker.R; // Asigura-te ca importul R corespunde numelui pachetului tau!
import com.example.randompicker.data.ApiClient;

public class MainActivity extends AppCompatActivity {

    private ApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializam clasa care se ocupa de conexiunea cu serverul
        apiClient = new ApiClient();

        Spinner spinnerCategory = findViewById(R.id.spinnerCategory);
        Button btnPickRandom = findViewById(R.id.btnPickRandom);
        TextView tvResult = findViewById(R.id.tvResult);
        EditText etNewIdea = findViewById(R.id.etNewIdea);
        Button btnAddIdea = findViewById(R.id.btnAddIdea);

        String[] categories = {"Mancare", "Filme", "Activitati"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapter);

        btnPickRandom.setOnClickListener(v -> {
            String selectedCategory = spinnerCategory.getSelectedItem().toString();
            tvResult.setText("Se incarca...");

            // Folosim metoda curata din ApiClient
            apiClient.extrageRandom(selectedCategory, new ApiClient.ApiCallback() {
                @Override
                public void onSuccess(String result) {
                    runOnUiThread(() -> tvResult.setText(result));
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> tvResult.setText(error));
                }
            });
        });

        btnAddIdea.setOnClickListener(v -> {
            String newIdea = etNewIdea.getText().toString().trim();
            String selectedCategory = spinnerCategory.getSelectedItem().toString();

            if (newIdea.isEmpty()) {
                Toast.makeText(MainActivity.this, "Scrie o idee mai intai!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Folosim metoda curata din ApiClient
            apiClient.adaugaIdee(selectedCategory, newIdea, new ApiClient.ApiCallback() {
                @Override
                public void onSuccess(String result) {
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                        etNewIdea.getText().clear();
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show());
                }
            });
        });
    }
}