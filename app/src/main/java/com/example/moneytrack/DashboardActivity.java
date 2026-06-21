package com.example.moneytrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    Button btnRegistrarGasto;
    Button btnVerGastos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnRegistrarGasto = findViewById(R.id.btnRegistrarGasto);
        btnVerGastos = findViewById(R.id.btnVerGastos);

        btnRegistrarGasto.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, RegistrarGastoActivity.class);
            startActivity(intent);
        });

        btnVerGastos.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ListaGastosActivity.class);
            startActivity(intent);
        });
    }
}