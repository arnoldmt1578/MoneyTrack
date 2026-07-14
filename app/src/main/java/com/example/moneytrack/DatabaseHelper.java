package com.example.moneytrack;

import android.database.Cursor;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MoneyTrack.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public boolean insertarGasto(double monto, String fecha, String descripcion) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("monto", monto);
        values.put("fecha", fecha);
        values.put("descripcion", descripcion);

        long resultado = db.insert("Gasto", null, values);

        return resultado != -1;
    }

    public Cursor obtenerGastos() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT id_gasto, monto, fecha, descripcion FROM Gasto ORDER BY id_gasto DESC",
                null
        );
    }

    public boolean eliminarGasto(int idGasto) {

        SQLiteDatabase db = this.getWritableDatabase();

        int resultado = db.delete(
                "Gasto",
                "id_gasto = ?",
                new String[]{String.valueOf(idGasto)}
        );

        return resultado > 0;
    }

    public boolean actualizarGasto(int idGasto, double monto, String descripcion) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("monto", monto);
        values.put("descripcion", descripcion);

        int resultado = db.update(
                "Gasto",
                values,
                "id_gasto = ?",
                new String[]{String.valueOf(idGasto)}
        );

        return resultado > 0;
    }

    public boolean insertarUsuario(
            String nombre,
            String correo,
            String contrasena,
            String fechaRegistro
    ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("correo", correo);
        values.put("contrasena", contrasena);
        values.put("fecha_registro", fechaRegistro);

        long resultado = db.insert("Usuario", null, values);

        return resultado != -1;
    }

    public boolean validarUsuario(String correo, String contrasena) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM Usuario WHERE correo = ? AND contrasena = ?",
                new String[]{correo, contrasena}
        );

        boolean existe = cursor.getCount() > 0;

        cursor.close();

        return existe;
    }

    public String obtenerNombreUsuario(String correo) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT nombre FROM Usuario WHERE correo = ?",
                new String[]{correo}
        );

        String nombre = "";

        if (cursor.moveToFirst()) {
            nombre = cursor.getString(0);
        }

        cursor.close();

        return nombre;
    }

    public boolean existeCorreo(String correo) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id_usuario FROM Usuario WHERE correo = ?",
                new String[]{correo}
        );

        boolean existe = cursor.moveToFirst();

        cursor.close();

        return existe;
    }

    public int obtenerCantidadGastos() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM Gasto",
                null
        );

        int cantidad = 0;

        if (cursor.moveToFirst()) {
            cantidad = cursor.getInt(0);
        }

        cursor.close();

        return cantidad;
    }

    public double obtenerTotalGastos() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT SUM(monto) FROM Gasto",
                null
        );

        double total = 0;

        if (cursor.moveToFirst() && !cursor.isNull(0)) {
            total = cursor.getDouble(0);
        }

        cursor.close();

        return total;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // TABLA USUARIO
        String CREATE_TABLE_USUARIO =
                "CREATE TABLE Usuario (" +
                        "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "nombre TEXT NOT NULL," +
                        "correo TEXT NOT NULL UNIQUE," +
                        "contrasena TEXT NOT NULL," +
                        "fecha_registro TEXT NOT NULL" +
                        ")";

        // TABLA CATEGORIA
        String CREATE_TABLE_CATEGORIA =
                "CREATE TABLE Categoria (" +
                        "id_categoria INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "nombre_categoria TEXT NOT NULL," +
                        "tipo TEXT NOT NULL," +
                        "descripcion TEXT," +
                        "estado INTEGER NOT NULL" +
                        ")";

        // TABLA INGRESO
        String CREATE_TABLE_INGRESO =
                "CREATE TABLE Ingreso (" +
                        "id_ingreso INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "monto REAL NOT NULL," +
                        "fecha TEXT NOT NULL," +
                        "descripcion TEXT," +
                        "id_usuario INTEGER," +
                        "id_categoria INTEGER," +
                        "FOREIGN KEY(id_usuario) REFERENCES Usuario(id_usuario)," +
                        "FOREIGN KEY(id_categoria) REFERENCES Categoria(id_categoria)" +
                        ")";

        // TABLA GASTO
        String CREATE_TABLE_GASTO =
                "CREATE TABLE Gasto (" +
                        "id_gasto INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "monto REAL NOT NULL," +
                        "fecha TEXT NOT NULL," +
                        "descripcion TEXT," +
                        "id_usuario INTEGER," +
                        "id_categoria INTEGER," +
                        "FOREIGN KEY(id_usuario) REFERENCES Usuario(id_usuario)," +
                        "FOREIGN KEY(id_categoria) REFERENCES Categoria(id_categoria)" +
                        ")";

        db.execSQL(CREATE_TABLE_USUARIO);
        db.execSQL(CREATE_TABLE_CATEGORIA);
        db.execSQL(CREATE_TABLE_INGRESO);
        db.execSQL(CREATE_TABLE_GASTO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS Gasto");
        db.execSQL("DROP TABLE IF EXISTS Ingreso");
        db.execSQL("DROP TABLE IF EXISTS Categoria");
        db.execSQL("DROP TABLE IF EXISTS Usuario");

        onCreate(db);
    }
}
