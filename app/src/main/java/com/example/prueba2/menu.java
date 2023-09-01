package com.example.prueba2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapShader;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class menu extends AppCompatActivity {

    TextView textViewNombre, tvNombre;
    SharedPreferences sharedPreferences;
    LinearLayout linearLayoutCerrar, linearLayoutDashboard;
    ImageView imageViewListar, imageViewCrear, imageViewLlamada, imageViewGuia;
    private BitmapShader shader;
    private Paint paint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        textViewNombre = findViewById(R.id.tvNombre);

        tvNombre = findViewById(R.id.tvNombre);

        sharedPreferences = getSharedPreferences("sesion", MODE_PRIVATE);
        String nombre = sharedPreferences.getString("nombre", "");
        nombre = nombre.substring(0, 1).toUpperCase() + nombre.substring(1).toLowerCase();
        String apellido = sharedPreferences.getString("apellido", "");
        apellido = apellido.substring(0, 1).toUpperCase() + apellido.substring(1).toLowerCase();
        tvNombre.setText(nombre + " " + apellido);

        String imagen = sharedPreferences.getString("imagen", "");
        Log.d("Imagen drecha", imagen);
        if(imagen.equals("noimg.jpg") || imagen.equals("")){
            Toast.makeText(getApplicationContext(), "No tienes una imagen", Toast.LENGTH_SHORT).show();
        }

    }

    public void logout(View v){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("logged", false);
        editor.apply();
        Intent i=new Intent(getApplicationContext(), Login2.class);
        startActivity(i);
        finish();
    }

    public void irDashboard(View v){
        Intent i = new Intent(getApplicationContext(), dashboard.class);
        startActivity(i);
        finish();
    }
}