package com.example.moneytrack;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrarGastoActivity extends AppCompatActivity {

    Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_gasto);

        btnGuardar = findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(v ->
                Toast.makeText(this, "Gasto guardado correctamente", Toast.LENGTH_SHORT).show()
        );
    }
}