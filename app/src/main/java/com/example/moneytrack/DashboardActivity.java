package com.example.moneytrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    Button btnRegistrarGasto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnRegistrarGasto = findViewById(R.id.btnRegistrarGasto);

        btnRegistrarGasto.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, RegistrarGastoActivity.class);
            startActivity(intent);
        });
    }
}