package com.example.moneytrack;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegistroUsuarioActivity extends AppCompatActivity {

    private EditText etNombreRegistro;
    private EditText etCorreoRegistro;
    private EditText etContrasenaRegistro;
    private EditText etConfirmarContrasena;
    private Button btnCrearCuenta;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        etNombreRegistro = findViewById(R.id.etNombreRegistro);
        etCorreoRegistro = findViewById(R.id.etCorreoRegistro);
        etContrasenaRegistro = findViewById(R.id.etContrasenaRegistro);
        etConfirmarContrasena = findViewById(R.id.etConfirmarContrasena);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);

        dbHelper = new DatabaseHelper(this);

        btnCrearCuenta.setOnClickListener(v -> registrarUsuario());
    }

    private void registrarUsuario() {

        String nombre = etNombreRegistro.getText().toString().trim();
        String correo = etCorreoRegistro.getText().toString().trim();
        String contrasena = etContrasenaRegistro.getText().toString().trim();
        String confirmarContrasena =
                etConfirmarContrasena.getText().toString().trim();

        if (nombre.isEmpty()) {
            etNombreRegistro.setError("Ingrese su nombre");
            etNombreRegistro.requestFocus();
            return;
        }

        if (correo.isEmpty()) {
            etCorreoRegistro.setError("Ingrese su correo");
            etCorreoRegistro.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            etCorreoRegistro.setError("Ingrese un correo válido");
            etCorreoRegistro.requestFocus();
            return;
        }

        if (contrasena.isEmpty()) {
            etContrasenaRegistro.setError("Ingrese una contraseña");
            etContrasenaRegistro.requestFocus();
            return;
        }

        if (contrasena.length() < 6) {
            etContrasenaRegistro.setError(
                    "La contraseña debe tener al menos 6 caracteres"
            );
            etContrasenaRegistro.requestFocus();
            return;
        }

        if (!contrasena.equals(confirmarContrasena)) {
            etConfirmarContrasena.setError("Las contraseñas no coinciden");
            etConfirmarContrasena.requestFocus();
            return;
        }

        if (dbHelper.existeCorreo(correo)) {
            etCorreoRegistro.setError("Este correo ya está registrado");
            etCorreoRegistro.requestFocus();
            return;
        }

        String fechaRegistro = new SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
        ).format(new Date());

        boolean registrado = dbHelper.insertarUsuario(
                nombre,
                correo,
                contrasena,
                fechaRegistro
        );

        if (registrado) {
            Toast.makeText(
                    this,
                    "Cuenta creada correctamente",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
        } else {
            Toast.makeText(
                    this,
                    "No se pudo crear la cuenta",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}