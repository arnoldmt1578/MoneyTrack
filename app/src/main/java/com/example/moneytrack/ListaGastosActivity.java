package com.example.moneytrack;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class ListaGastosActivity extends AppCompatActivity {

    private TextView tvGastos;

    private EditText etIdActualizar;
    private EditText etNuevoMonto;
    private EditText etNuevaDescripcion;
    private Button btnActualizar;

    private EditText etIdEliminar;
    private Button btnEliminar;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_gastos);

        tvGastos = findViewById(R.id.tvGastos);

        etIdActualizar = findViewById(R.id.etIdActualizar);
        etNuevoMonto = findViewById(R.id.etNuevoMonto);
        etNuevaDescripcion = findViewById(R.id.etNuevaDescripcion);
        btnActualizar = findViewById(R.id.btnActualizar);

        etIdEliminar = findViewById(R.id.etIdEliminar);
        btnEliminar = findViewById(R.id.btnEliminar);

        dbHelper = new DatabaseHelper(this);

        mostrarGastos();

        btnActualizar.setOnClickListener(v -> actualizarGasto());

        btnEliminar.setOnClickListener(v -> prepararEliminacion());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (dbHelper != null) {
            mostrarGastos();
        }
    }

    private void mostrarGastos() {

        Cursor cursor = dbHelper.obtenerGastos();

        if (cursor == null) {
            tvGastos.setText("No se pudieron cargar los gastos.");
            return;
        }

        if (cursor.getCount() == 0) {
            tvGastos.setText("No hay gastos registrados.");
            cursor.close();
            return;
        }

        StringBuilder lista = new StringBuilder();

        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            double monto = cursor.getDouble(1);
            String fecha = cursor.getString(2);
            String descripcion = cursor.getString(3);

            if (descripcion == null || descripcion.trim().isEmpty()) {
                descripcion = "Sin descripción";
            }

            lista.append("ID: ")
                    .append(id)
                    .append("\n");

            lista.append("Monto: RD$ ")
                    .append(String.format(
                            Locale.getDefault(),
                            "%.2f",
                            monto
                    ))
                    .append("\n");

            lista.append("Fecha: ")
                    .append(fecha)
                    .append("\n");

            lista.append("Descripción: ")
                    .append(descripcion)
                    .append("\n");

            lista.append("--------------------------------\n\n");
        }

        cursor.close();

        tvGastos.setText(lista.toString());
    }

    private void actualizarGasto() {

        String idTexto =
                etIdActualizar.getText().toString().trim();

        String montoTexto =
                etNuevoMonto.getText().toString().trim();

        String descripcion =
                etNuevaDescripcion.getText().toString().trim();

        if (idTexto.isEmpty()) {
            etIdActualizar.setError("Ingrese el ID del gasto");
            etIdActualizar.requestFocus();
            return;
        }

        if (montoTexto.isEmpty()) {
            etNuevoMonto.setError("Ingrese el nuevo monto");
            etNuevoMonto.requestFocus();
            return;
        }

        int idGasto;

        try {
            idGasto = Integer.parseInt(idTexto);
        } catch (NumberFormatException e) {
            etIdActualizar.setError("Ingrese un ID válido");
            etIdActualizar.requestFocus();
            return;
        }

        if (idGasto <= 0) {
            etIdActualizar.setError("El ID debe ser mayor que cero");
            etIdActualizar.requestFocus();
            return;
        }

        double monto;

        try {
            monto = Double.parseDouble(montoTexto);
        } catch (NumberFormatException e) {
            etNuevoMonto.setError("Ingrese un monto válido");
            etNuevoMonto.requestFocus();
            return;
        }

        if (monto <= 0) {
            etNuevoMonto.setError(
                    "El monto debe ser mayor que cero"
            );
            etNuevoMonto.requestFocus();
            return;
        }

        if (descripcion.length() > 150) {
            etNuevaDescripcion.setError(
                    "La descripción no puede superar los 150 caracteres"
            );
            etNuevaDescripcion.requestFocus();
            return;
        }

        boolean actualizado = dbHelper.actualizarGasto(
                idGasto,
                monto,
                descripcion
        );

        if (actualizado) {

            Toast.makeText(
                    this,
                    "Gasto actualizado correctamente",
                    Toast.LENGTH_SHORT
            ).show();

            etIdActualizar.setText("");
            etNuevoMonto.setText("");
            etNuevaDescripcion.setText("");

            mostrarGastos();

        } else {

            Toast.makeText(
                    this,
                    "No existe un gasto con ese ID",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void prepararEliminacion() {

        String idTexto =
                etIdEliminar.getText().toString().trim();

        if (idTexto.isEmpty()) {
            etIdEliminar.setError("Ingrese el ID del gasto");
            etIdEliminar.requestFocus();
            return;
        }

        int idGasto;

        try {
            idGasto = Integer.parseInt(idTexto);
        } catch (NumberFormatException e) {
            etIdEliminar.setError("Ingrese un ID válido");
            etIdEliminar.requestFocus();
            return;
        }

        if (idGasto <= 0) {
            etIdEliminar.setError("El ID debe ser mayor que cero");
            etIdEliminar.requestFocus();
            return;
        }

        mostrarConfirmacionEliminacion(idGasto);
    }

    private void mostrarConfirmacionEliminacion(int idGasto) {

        new AlertDialog.Builder(this)
                .setTitle("Eliminar gasto")
                .setMessage(
                        "¿Está seguro de que desea eliminar el gasto con ID "
                                + idGasto + "?"
                )
                .setPositiveButton(
                        "Eliminar",
                        (dialog, which) -> eliminarGasto(idGasto)
                )
                .setNegativeButton(
                        "Cancelar",
                        (dialog, which) -> dialog.dismiss()
                )
                .show();
    }

    private void eliminarGasto(int idGasto) {

        boolean eliminado = dbHelper.eliminarGasto(idGasto);

        if (eliminado) {

            Toast.makeText(
                    this,
                    "Gasto eliminado correctamente",
                    Toast.LENGTH_SHORT
            ).show();

            etIdEliminar.setText("");

            mostrarGastos();

        } else {

            Toast.makeText(
                    this,
                    "No existe un gasto con ese ID",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}