package com.example.moneytrack;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etCorreo;
    private EditText etContrasena;
    private Button btnLogin;
    private Button btnRegistrarse;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCorreo = findViewById(R.id.etCorreo);
        etContrasena = findViewById(R.id.etContrasena);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);

        dbHelper = new DatabaseHelper(this);

        btnLogin.setOnClickListener(v -> iniciarSesion());

        btnRegistrarse.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    RegistroUsuarioActivity.class
            );
            startActivity(intent);
        });
    }

    private void iniciarSesion() {

        String correo = etCorreo.getText().toString().trim();
        String contrasena = etContrasena.getText().toString().trim();

        if (correo.isEmpty()) {
            etCorreo.setError("Ingrese su correo");
            etCorreo.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            etCorreo.setError("Ingrese un correo válido");
            etCorreo.requestFocus();
            return;
        }

        if (contrasena.isEmpty()) {
            etContrasena.setError("Ingrese su contraseña");
            etContrasena.requestFocus();
            return;
        }

        boolean usuarioValido = dbHelper.validarUsuario(
                correo,
                contrasena
        );

        if (usuarioValido) {

            Toast.makeText(
                    this,
                    "Inicio de sesión correcto",
                    Toast.LENGTH_SHORT
            ).show();

            String nombreUsuario = dbHelper.obtenerNombreUsuario(correo);

            Intent intent = new Intent(
                    MainActivity.this,
                    DashboardActivity.class
            );

            intent.putExtra("nombre_usuario", nombreUsuario);

            startActivity(intent);
            finish();

        } else {

            Toast.makeText(
                    this,
                    "Correo o contraseña incorrectos",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}