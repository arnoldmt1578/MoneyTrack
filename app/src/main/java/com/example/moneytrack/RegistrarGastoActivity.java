package com.example.moneytrack;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegistrarGastoActivity extends AppCompatActivity {

    Button btnGuardar;
    EditText etMonto;
    EditText etDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_gasto);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.getWritableDatabase();

        etMonto = findViewById(R.id.etMonto);
        etDescripcion = findViewById(R.id.etDescripcion);
        btnGuardar = findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(v -> {

            String montoTexto = etMonto.getText().toString().trim();
            String descripcion = etDescripcion.getText().toString().trim();

            if (montoTexto.isEmpty()) {
                Toast.makeText(this, "Debe ingresar un monto", Toast.LENGTH_SHORT).show();
                return;
            }

            double monto = Double.parseDouble(montoTexto);

            String fecha = new SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
            ).format(new Date());

            boolean insertado = dbHelper.insertarGasto(
                    monto,
                    fecha,
                    descripcion
            );

            if (insertado) {

                Toast.makeText(
                        this,
                        "Gasto guardado correctamente",
                        Toast.LENGTH_SHORT
                ).show();

                etMonto.setText("");
                etDescripcion.setText("");

            } else {

                Toast.makeText(
                        this,
                        "Error al guardar el gasto",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}