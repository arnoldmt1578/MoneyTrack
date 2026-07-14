package com.example.moneytrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvBienvenida;
    private TextView tvCantidadGastos;
    private TextView tvTotalGastado;

    private Button btnRegistrarGasto;
    private Button btnVerGastos;

    private Button btnCerrarSesion;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvBienvenida = findViewById(R.id.tvBienvenida);
        tvCantidadGastos = findViewById(R.id.tvCantidadGastos);
        tvTotalGastado = findViewById(R.id.tvTotalGastado);

        String nombreUsuario = getIntent().getStringExtra("nombre_usuario");

        if (nombreUsuario != null && !nombreUsuario.trim().isEmpty()) {
            tvBienvenida.setText("Bienvenido, " + nombreUsuario);
        } else {
            tvBienvenida.setText("Bienvenido");
        }

        btnRegistrarGasto = findViewById(R.id.btnRegistrarGasto);
        btnVerGastos = findViewById(R.id.btnVerGastos);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        dbHelper = new DatabaseHelper(this);

        btnRegistrarGasto.setOnClickListener(v -> {
            Intent intent = new Intent(
                    DashboardActivity.this,
                    RegistrarGastoActivity.class
            );
            startActivity(intent);
        });

        btnVerGastos.setOnClickListener(v -> {
            Intent intent = new Intent(
                    DashboardActivity.this,
                    ListaGastosActivity.class
            );
            startActivity(intent);
        });

        btnCerrarSesion.setOnClickListener(v -> {

            Intent intent = new Intent(
                    DashboardActivity.this,
                    MainActivity.class
            );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarResumen();
    }

    private void actualizarResumen() {

        int cantidadGastos = dbHelper.obtenerCantidadGastos();
        double totalGastado = dbHelper.obtenerTotalGastos();

        tvCantidadGastos.setText(
                String.valueOf(cantidadGastos)
        );

        tvTotalGastado.setText(
                String.format(
                        Locale.getDefault(),
                        "RD$ %.2f",
                        totalGastado
                )
        );
    }
}