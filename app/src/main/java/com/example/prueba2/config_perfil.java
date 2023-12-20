package com.example.prueba2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prueba2.databinding.ActivityMainBinding;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.kyanogen.signatureview.SignatureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class config_perfil extends AppCompatActivity {
    ImageView ivHamburgesa, imgUsuario;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    AppBarLayout appBarLayout;
    View backgroundOverlay;
    boolean muestra = true;
    TextView tvCorreoApe;
    EditText etNameConf;
    SharedPreferences sharedPreferences;
    private static final int REQUEST_CODE_SELECT_IMAGE = 123;
    private static final int REQUEST_PERMISSION_CODE = 123;

    //PRUEBA FIRMA
    ActivityMainBinding mainBinding;
    private Bitmap signBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_perfil);
        Button saveFirma = findViewById(R.id.saveFirma);
        SignatureView signatureFirma = findViewById(R.id.signatureFirma);
        ImageView imageView22 = findViewById(R.id.imageView22);
        ConstraintLayout llFirma = findViewById(R.id.llFirma);
        tvCorreoApe = findViewById(R.id.tvCorreoApe);
        etNameConf = findViewById(R.id.etNameConf);
        informacionUsuario();

        saveFirma.setOnClickListener(view -> {
            Bitmap signBitmap = signatureFirma.getSignatureBitmap();

            if(signBitmap != null){
                imageView22.setImageBitmap(signBitmap);
                llFirma.setVisibility(View.GONE);

                //Convertir Bitmap en cadena base64
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                signBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Log.d("BASE 64 DE FIRMA: ", base64Image);

                if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE); //Solicitar permisos
                }else{
                    saveImageToExternalStorage(signBitmap);
                }

            }
        });
        backgroundOverlay = findViewById(R.id.background_overlay);
        ivHamburgesa = findViewById(R.id.ivHamburgesa);
        imgUsuario = findViewById(R.id.imgUsuario);
        appBarLayout = findViewById(R.id.appBarLayout);
        sharedPreferences = getSharedPreferences("sesion", MODE_PRIVATE);

        drawerLayout = findViewById(R.id.llPop7);
        navigationView = findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );


        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        //actionBarDrawerToggle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if(itemId == R.id.nav_dashboard){
                Intent i = new Intent(getApplicationContext(), dashboard.class);
                startActivity(i);
                finish();
            } else if (itemId == R.id.nav_menu) {
                Intent i = new Intent(getApplicationContext(), menu.class);
                startActivity(i);
                finish();
            } else if (itemId == R.id.nav_guia){
                Intent i = new Intent(getApplicationContext(), guiaServicio.class);
                startActivity(i);
                finish();
            } else if (itemId == R.id.nav_config) {
                Intent i = new Intent(getApplicationContext(), config_perfil.class);
                startActivity(i);
                finish();
            }else if (itemId == R.id.nav_cerrar) {
                logout();
            }

            drawerLayout.closeDrawers();
            return true;
        });

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.bot);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Llenado de información del usuario
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String nombreUsuario = sharedPreferences.getString("nombre", "");
        String apellidoUsuario = sharedPreferences.getString("apellido","");
        String correoUsuario = sharedPreferences.getString("email", "");
        String firma = sharedPreferences.getString("firmaA","");
        nombreUsuario = nombreUsuario.substring(0,1).toUpperCase() + nombreUsuario.substring(1).toLowerCase();
        apellidoUsuario = apellidoUsuario.substring(0,1).toUpperCase() + apellidoUsuario.substring(1).toLowerCase();
        etNameConf.setText(nombreUsuario+" "+apellidoUsuario);
        tvCorreoApe.setText(correoUsuario);

        if(firma != null || !firma.isEmpty()){
            String imageData = firma.split(",")[1];
            byte[] decodedString = Base64.decode(imageData, Base64.DEFAULT);
            Bitmap decodedBye = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);

            int width = 1200;
            int height = 800;
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(decodedBye,width, height, false);
            imageView22.setImageBitmap(resizedBitmap);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == REQUEST_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(signBitmap != null){
                    saveImageToExternalStorage(signBitmap);
                    Toast.makeText(this, "Se guarda se guarda", Toast.LENGTH_SHORT).show();
                }else{
                 Toast.makeText(this, "signBitmap esta vacio", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Permiso denegado de escritura", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void informacionUsuario(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://172.16.32.50/gzapto/ajax/usuario.php?op=mostar";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            String firmaBase64 = jsonResponse.optString("firma");
                            //byte[] firmaBytes = Base64.decode(firmaBase64, Base64.DEFAULT);
                            //Bitmap firmaBitmap = BitmapFactory.decodeByteArray(firmaBytes,0,firmaBytes.length);
                            if(firmaBase64 != null || !firmaBase64.isEmpty() ){

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("firmaA", firmaBase64);
                                editor.apply();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        Log.d("RESPONSE DEL USUARIO CONIO: ", String.valueOf(response));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Fallo en la info del usuario", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("iduser", String.valueOf(sharedPreferences.getInt("iduser", 0)));
                return paramV;
            }
        };
        queue.add(stringRequest);
    }



    private void saveImageToExternalStorage(Bitmap image){
        String fileName = "firma_fabrimetal.png"; //Nombre de la firma
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); //Obtener el directorio publico PICTURES
        File file = new File(directory, fileName); //Crear el archivo en el directorio de Pictures
        //Guradar la imagen base64 en el archivo
        try{
            FileOutputStream fos = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            Toast.makeText(getApplicationContext(), "FIRMA GUARDADA CON ÉXITO", Toast.LENGTH_SHORT).show();
        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "ERROR AL GUARDAR LA FIRMA EN LA GALERIA", Toast.LENGTH_SHORT).show();

        }
    }



    //FUNCIONES EXTERNAS
    public void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Seguro deseas cerrar sesión?");
        builder.setPositiveButton("Cerrar sesión", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("logged", false);
                editor.apply();
                Intent intent=new Intent(getApplicationContext(), Login2.class);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START);
            }else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    public void mostrarMenu(View v){

        if(muestra==true){
            appBarLayout.setVisibility(View.VISIBLE);
            muestra = false;
            backgroundOverlay.setVisibility(View.VISIBLE);
        }else if(muestra == false){
            appBarLayout.setVisibility(View.GONE);
            muestra = true;
            backgroundOverlay.setVisibility(View.GONE);
        }
    }

    public void modificarFirma(View v){
        ConstraintLayout llFirma = findViewById(R.id.llFirma);
        llFirma.setVisibility(View.VISIBLE);
    }
}