package com.example.moneytrack;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ListaGastosActivity extends AppCompatActivity {

    TextView tvGastos;

    EditText etIdEliminar;
    Button btnEliminar;

    EditText etIdActualizar;
    EditText etNuevoMonto;
    EditText etNuevaDescripcion;
    Button btnActualizar;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_gastos);

        tvGastos = findViewById(R.id.tvGastos);
        etIdEliminar = findViewById(R.id.etIdEliminar);
        btnEliminar = findViewById(R.id.btnEliminar);

        etIdActualizar = findViewById(R.id.etIdActualizar);
        etNuevoMonto = findViewById(R.id.etNuevoMonto);
        etNuevaDescripcion = findViewById(R.id.etNuevaDescripcion);
        btnActualizar = findViewById(R.id.btnActualizar);

        dbHelper = new DatabaseHelper(this);

        mostrarGastos();

        btnEliminar.setOnClickListener(v -> {

            String idTexto = etIdEliminar.getText().toString().trim();

            if (idTexto.isEmpty()) {
                Toast.makeText(this, "Debe ingresar el ID del gasto", Toast.LENGTH_SHORT).show();
                return;
            }

            int idGasto = Integer.parseInt(idTexto);

            boolean eliminado = dbHelper.eliminarGasto(idGasto);

            if (eliminado) {
                Toast.makeText(this, "Gasto eliminado correctamente", Toast.LENGTH_SHORT).show();
                etIdEliminar.setText("");
                mostrarGastos();
            } else {
                Toast.makeText(this, "No se encontró un gasto con ese ID", Toast.LENGTH_SHORT).show();
            }
        });

        btnActualizar.setOnClickListener(v -> {

            String idTexto = etIdActualizar.getText().toString().trim();
            String montoTexto = etNuevoMonto.getText().toString().trim();
            String descripcion = etNuevaDescripcion.getText().toString().trim();

            if (idTexto.isEmpty() || montoTexto.isEmpty()) {
                Toast.makeText(this, "Complete ID y monto", Toast.LENGTH_SHORT).show();
                return;
            }

            int idGasto = Integer.parseInt(idTexto);
            double monto = Double.parseDouble(montoTexto);

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
                        "No se encontró el ID indicado",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void mostrarGastos() {

        Cursor cursor = dbHelper.obtenerGastos();

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

            lista.append("ID: ").append(id).append("\n");
            lista.append("Monto: RD$ ").append(monto).append("\n");
            lista.append("Fecha: ").append(fecha).append("\n");
            lista.append("Descripción: ").append(descripcion).append("\n");
            lista.append("-------------------------\n");
        }

        cursor.close();

        tvGastos.setText(lista.toString());
    }
}