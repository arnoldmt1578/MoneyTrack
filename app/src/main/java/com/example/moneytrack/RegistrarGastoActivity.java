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

    private Button btnGuardar;
    private EditText etMonto;
    private EditText etDescripcion;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_gasto);

        etMonto = findViewById(R.id.etMonto);
        etDescripcion = findViewById(R.id.etDescripcion);
        btnGuardar = findViewById(R.id.btnGuardar);

        dbHelper = new DatabaseHelper(this);

        btnGuardar.setOnClickListener(v -> guardarGasto());
    }

    private void guardarGasto() {

        String montoTexto = etMonto.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();

        if (montoTexto.isEmpty()) {
            etMonto.setError("Debe ingresar un monto");
            etMonto.requestFocus();
            return;
        }

        double monto;

        try {
            monto = Double.parseDouble(montoTexto);
        } catch (NumberFormatException e) {
            etMonto.setError("Ingrese un monto válido");
            etMonto.requestFocus();
            return;
        }

        if (monto <= 0) {
            etMonto.setError("El monto debe ser mayor que cero");
            etMonto.requestFocus();
            return;
        }

        if (descripcion.length() > 150) {
            etDescripcion.setError(
                    "La descripción no puede superar los 150 caracteres"
            );
            etDescripcion.requestFocus();
            return;
        }

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
            etMonto.requestFocus();

        } else {
            Toast.makeText(
                    this,
                    "Error al guardar el gasto",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}