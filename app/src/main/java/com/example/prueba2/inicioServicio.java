package com.example.prueba2;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prueba2.databinding.ActivityMainBinding;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.kyanogen.signatureview.SignatureView;
import com.mrudultora.colorpicker.ColorPickerPopUp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


public class inicioServicio extends AppCompatActivity {
    //Inicialización de objetos, elementos y variables globales
    TextView tvIDServicio, tvCodiCli, tvCodTip, tvMarcaEq, tvModEqu, tv2, tv4, tv5, tv3, tvServiceID, tvTipoServ, tvFechaIni, tvHorIni, tvObservacionIni, tvNombreEd, tvDireccionEd, tvCantAyu, tvAyu1, tvAyu2, tvCargoTec, tvNombresTec, tvApellidosTec, tvRutTec, tvMax;
    private Spinner spEstadoF, spCantAyud, spAyu1, spAyu2, spOpayu, spOpFirma;
    GridView gvImagenes;
    SignatureView signatureView;
    Button btnGaleria, clear;
    List<Uri> listaImagenes = new ArrayList<>();
    List<String> listaBase64Imagenes = new ArrayList<>();
    List<String> valoresValue = new ArrayList<>();
    GridViewAdapter baseAdapter;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switch1;
    private ProgressBar pbu, pbFin, pbF2;
    private DrawerLayout drawerLayout;

    private AppBarLayout appBarLayout;

    private boolean muestra = true;
    private LinearLayout ll5, ll1, ll3, ll4, ll6, ll7, ll2;
    ImageView img1, img2, img3, img4, ivHamburgesa, imageView15, imageView16, imageView19, imageView11, imageView14, imageView18, imageButton7, imageView17, imageView24;

    NavigationView navigationView;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    private ConstraintLayout clPop, clPop2, clPop3, clPop4, clPop5, clPop6, clPop7, clPop8, clAnimacion;
    ConstraintLayout frameLayout2;
    private SharedPreferences sharedPreferences;
    ImageButton imageButton8, imageButton9;
    private View background_overlay2;
    String padfirma;
    private EditText etObservacionFin, etSolPresup, etNombres, etApellidos, etCargo, etRut;
    int PICK_IMAGE = 100;
    private int cantidadAyudantesSeleccionados = 0, idtec1 = 0, idtec2 = 0;
    private static int REQUEST_CODE = 100;
    OutputStream outputStream;
    boolean firmaplicada = false;

    private Bitmap firmaBitmap;
    private boolean servicioFinalizado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_servicio);

        signatureView = findViewById(R.id.signatureView);
        clear = findViewById(R.id.clear);

        //Vinculación de variables con elementos de la activity
        ll4 = findViewById(R.id.ll4);
        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);
        ll5 = findViewById(R.id.ll5);
        spAyu2 = findViewById(R.id.spAyu2);
        etSolPresup = findViewById(R.id.etSolPresup);
        drawerLayout = findViewById(R.id.drawerLayout);
        imageView18 = findViewById(R.id.imageView18);
        gvImagenes = findViewById(R.id.gvImagenes);
        clPop8 = findViewById(R.id.llPop8);
        imageView24 = findViewById(R.id.imageView24);
        imageButton8 = findViewById(R.id.imageButton8);
        clAnimacion = findViewById(R.id.clAnimation);
        frameLayout2 = findViewById(R.id.frameLayout2);
        imageView14 = findViewById(R.id.imageView14);
        ll6 = findViewById(R.id.ll6);
        ll7 = findViewById(R.id.ll7);
        tvMax = findViewById(R.id.tvMax);
        imageView19 = findViewById(R.id.imageView19);
        spOpFirma = findViewById(R.id.spOpFirma);
        tv2 = findViewById(R.id.tv2);
        signatureView = findViewById(R.id.signatureView);
        clPop6 = findViewById(R.id.llPop6);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tvAyu1 = findViewById(R.id.tvAyu1);
        tvAyu2 = findViewById(R.id.tvAyu2);
        spAyu1 = findViewById(R.id.spAyu1);
        etApellidos = findViewById(R.id.etApellidos);
        etRut = findViewById(R.id.etRut);
        etNombres = findViewById(R.id.etNombres);
        etCargo = findViewById(R.id.etCargo);
        pbF2 = findViewById(R.id.pbF2);
        imageView15 = findViewById(R.id.imageView15);
        imageView16 = findViewById(R.id.imageView16);
        imageButton9 = findViewById(R.id.imageButton9);
        imageView11 = findViewById(R.id.imageView11);
        imageButton7 = findViewById(R.id.imageButton7);
        imageView17 = findViewById(R.id.imageView17);
        clPop4 = findViewById(R.id.clPop4);
        clPop5 = findViewById(R.id.llPop5);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        clPop3 = findViewById(R.id.llPop3);
        spOpayu = findViewById(R.id.spOpayu);
        pbFin = findViewById(R.id.pbFin);
        imageView14 = findViewById(R.id.imageView14);
        etObservacionFin = findViewById(R.id.etObservacionFin);
        img4 = findViewById(R.id.img4);
        clPop = findViewById(R.id.llPop);
        pbu = findViewById(R.id.pbu);
        spEstadoF = findViewById(R.id.spEstadoF3);
        tvCantAyu = findViewById(R.id.tvCantAyu);
        clPop2 = findViewById(R.id.llPop2);
        ivHamburgesa = findViewById(R.id.ivHamburgesa2);
        appBarLayout = findViewById(R.id.appBarLayout);
        clPop7 = findViewById(R.id.llPop7);
        spCantAyud = findViewById(R.id.spCantAyud);
        navigationView = findViewById(R.id.nav_view);
        tvIDServicio = findViewById(R.id.tvIDServicio);
        tvCodiCli = findViewById(R.id.tvCodiCli);
        tvCodTip = findViewById(R.id.tvCodTip);
        tvMarcaEq = findViewById(R.id.tvMarcaEq);
        tvModEqu = findViewById(R.id.tvModEqu);
        tvCargoTec = findViewById(R.id.tvCargoTec);
        tvNombresTec = findViewById(R.id.tvNombresTec);
        tvApellidosTec = findViewById(R.id.tvApellidosTec);
        tvRutTec = findViewById(R.id.tvRutTec);
        tvNombreEd = findViewById(R.id.tvNombreEd);
        tvDireccionEd = findViewById(R.id.tvDireccionEd);
        btnGaleria = findViewById(R.id.btnGaleria);
        tvServiceID = findViewById(R.id.tvServiceID);
        tvTipoServ = findViewById(R.id.tvTipoServ);
        tvFechaIni = findViewById(R.id.tvFechaIni);
        tvHorIni = findViewById(R.id.tvHorIni);
        tvObservacionIni = findViewById(R.id.tvComentarioIni);
        background_overlay2 = findViewById(R.id.background_overlay2);
        switch1 = findViewById(R.id.switch1);
        sharedPreferences = getSharedPreferences("sesion", Context.MODE_PRIVATE);

        //Inicialización del menú desplegable
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //Navegación del menú
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_dashboard) {
                Intent i = new Intent(getApplicationContext(), dashboard.class);
                startActivity(i);
                finish();
            } else if (itemId == R.id.nav_menu) {
                Intent i = new Intent(getApplicationContext(), menu.class);
                startActivity(i);
                finish();
            } else if (itemId == R.id.nav_guia) {
                Intent i = new Intent(getApplicationContext(), guiaServicio.class);
                startActivity(i);
                finish();
            }
            drawerLayout.closeDrawers();
            return true;
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.bot);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Se traen variables de sesión
        String servicioini = sharedPreferences.getString("servicioini", "");
        String codigoEquipo = sharedPreferences.getString("codigoEquipo", "");
        String tipoEquipo = sharedPreferences.getString("tipoEquipo", "");
        String marcaEquipo = sharedPreferences.getString("marcaEquipo", "");
        String modeloEquipo = sharedPreferences.getString("modeloEquipo", "");
        String cargoTecnico = sharedPreferences.getString("cargoTecnico", "");
        String nombresTecnico = sharedPreferences.getString("nombresTecnico", "");
        String apellidoTecnico = sharedPreferences.getString("apellidoTecnico", "");
        String rutTecnico = sharedPreferences.getString("rutTecnico", "");
        String nombreEdificio = sharedPreferences.getString("nombreEdificio", "");
        String direccionEdificio = sharedPreferences.getString("direccionEdificio", "");
        String servicioServicio = sharedPreferences.getString("servicioServicio", "");
        String tipoServicio = sharedPreferences.getString("tipoServicio", "");
        String fechainicioServicio = sharedPreferences.getString("fechainicioServicio", "");
        String horainicioServicio = sharedPreferences.getString("horainicioServicio", "");
        String observacionServicio = sharedPreferences.getString("observacionServicio", "");
        String servicioTitulo = getResources().getString(R.string.servicio, servicioini);
        //  tvIDServicio.setText("Servicio: # "+servicioini);

        //Se asignan los valores a los elementos correspondientes en la vista.
        tvIDServicio.setText(servicioTitulo);

        tvCodiCli.setText(codigoEquipo);
        tvCodTip.setText(tipoEquipo);
        tvMarcaEq.setText(marcaEquipo);
        tvModEqu.setText(modeloEquipo);
        tvCargoTec.setText(cargoTecnico);
        tvNombresTec.setText(nombresTecnico);
        tvApellidosTec.setText(apellidoTecnico);
        tvRutTec.setText(rutTecnico);
        tvNombreEd.setText(nombreEdificio);
        tvDireccionEd.setText(direccionEdificio);
        tvServiceID.setText(servicioServicio);
        tvTipoServ.setText(tipoServicio);
        tvFechaIni.setText(fechainicioServicio);
        tvHorIni.setText(horainicioServicio);
        tvObservacionIni.setText(observacionServicio);

        //Cambio del estado del switch luego de 5 segundos
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> switch1.setChecked(!switch1.isChecked()));
            }
        }, 5000);

        //Elementos del servicio iniciado a mostrar según estado del switch
        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                ll1.setVisibility(View.GONE);
                ll2.setVisibility(View.GONE);
                img1.setVisibility(View.GONE);
                tv2.setVisibility(View.GONE);
                img2.setVisibility(View.GONE);
                tv3.setVisibility(View.GONE);
                ll3.setVisibility(View.GONE);
                ll4.setVisibility(View.GONE);

                ll5.setVisibility(View.VISIBLE);
                img3.setVisibility(View.VISIBLE);
                tv4.setVisibility(View.VISIBLE);
                ll6.setVisibility(View.VISIBLE);
                img4.setVisibility(View.VISIBLE);
                tv5.setVisibility(View.VISIBLE);
                ll7.setVisibility(View.VISIBLE);
            } else {

                ll1.setVisibility(View.VISIBLE);
                ll2.setVisibility(View.VISIBLE);
                img1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                img2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
                ll3.setVisibility(View.VISIBLE);
                ll4.setVisibility(View.VISIBLE);


                ll5.setVisibility(View.GONE);
                img3.setVisibility(View.GONE);
                tv4.setVisibility(View.GONE);
                ll6.setVisibility(View.GONE);
                img4.setVisibility(View.GONE);
                tv5.setVisibility(View.GONE);
                ll7.setVisibility(View.GONE);
            }
        });
        setupFirmas();

        //Eventos Onclick sobre botones para continuar
        imageButton7.setOnClickListener(view -> trasitionViews(clPop4, clPop3));
        imageButton8.setOnClickListener(view -> trasitionViews(clPop6, clPop5));
        imageButton9.setOnClickListener(view -> trasitionViews(clPop7, clPop6));
        imageView11.setOnClickListener(view -> {
            int idayud2 = sharedPreferences.getInt("idayud2", 0);
            int idayud1 = sharedPreferences.getInt("idayud1", 0);
            Toast.makeText(getApplicationContext(), +idayud1 + " " + idayud2, Toast.LENGTH_SHORT).show();
            trasitionViews(clPop3, clPop4);
        });


        //Onclick para el boton carga de imagenes y borrar firma
        btnGaleria.setOnClickListener(view -> abrirGaleria());
        clear.setOnClickListener(view -> signatureView.clearCanvas());

        //Condifuracion pad de firma
        signatureView.setPenColor(Color.BLACK);
        signatureView.setPenSize(15);

        //Navegacion "siguiente" de popUps
        imageView14.setOnClickListener(view -> moveFoward(clPop, clPop2));
        imageView18.setOnClickListener(view -> {
            firmaplicada = false;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firmaaplicada", firmaplicada);
            editor.apply();
            finalizarServ();
        });

        //Onclick para información y validación del firmante
        imageView19.setOnClickListener(view -> {
            //Listado de Campos
            EditText[] camposTexto = {etCargo, etNombres, etApellidos, etRut};
            boolean camposVacios = false;

            for (EditText campo : camposTexto) {
                String texto = campo.getText().toString();
                campo.setTextColor(Color.parseColor("#958D8D")); //Restablece el color de texto a su valor predeterminado

                if (texto.isEmpty()) {
                    int colorErrorHint = Color.RED;
                    campo.setHintTextColor(colorErrorHint);
                    camposVacios = true;
                }

            }

            if (camposVacios) {
                mensajeError("Debes llenar todos los campos para continuar");
            } else {
                String cargo = etCargo.getText().toString();
                String nombres2 = etNombres.getText().toString();
                String apellidos = etApellidos.getText().toString();
                String rut = etRut.getText().toString();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("nombresfi", nombres2);
                editor.putString("apellidosfi", apellidos);
                editor.putString("rutfi", rut);
                editor.putString("cargofi", cargo);
                editor.apply();
                moveFoward(clPop7, clPop8);
            }
        });

        //Validación de observación fin de servicio
        imageView15.setOnClickListener(view -> {
            String subject = etObservacionFin.getText().toString();
            if (subject.equals("")) {
                //Mensaje Toast de Error
                mensajeError("Debes ingresar una observación para continuar");
                int colorErrorHint = Color.RED;
                etObservacionFin.setHintTextColor(colorErrorHint);
            } else {
                //Se llama al metodo infogias
                infogias();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("observacionfi", subject);
                editor.apply();
            }
        });

        //Validación para observación del presupuesto
        imageView24.setOnClickListener(view -> {
            String subject = etSolPresup.getText().toString();
            if (subject.equals("")) {
                mensajeError("Debes ingresar una observación para continuar");
                int colorErrorHint = Color.RED;
                etSolPresup.setHintTextColor(colorErrorHint);
            } else {
                moveFoward(clPop5, clPop6);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String descripcion = etSolPresup.getText().toString();
                editor.putString("descripcion", descripcion);
                editor.apply();
            }
        });

        //Validación de Imagenes de Presupuesto
        imageView17.setOnClickListener(view -> {
            //subirImagenes();
            String seleccionRecuperada = sharedPreferences.getString("seleccion", "");
            if (seleccionRecuperada.equals("Si")) {
                Toast.makeText(getApplicationContext(), "YEA, WA", Toast.LENGTH_SHORT).show();
                int cantidadImagenes = sharedPreferences.getInt("cantidadImagenes", 0);

                    moveFoward(clPop4, clPop5);

                Log.d("IMAGENES SUBIDAS", String.valueOf(cantidadImagenes));
            } else {
                moveFoward(clPop4, clPop6);
            }

        });


    }



    //Metodo para terminar el servicio, incluye validación de si se puede cerrar la guia y traer los estados disponibles.
    public void terminarServicio() {
        clPop.setVisibility(View.VISIBLE);

        //servicioini(idservicio), codigoEquipo(idacensor), servillo(tipoencuesta), actCodigo(idactividad), swBtnFin
        String servicioini = sharedPreferences.getString("servicioini", "");
        String codigoEquipo = sharedPreferences.getString("codigoEquipo", "");
        String servillo = sharedPreferences.getString("servillo", "");
        String actCodigo = sharedPreferences.getString("actCodigo", "");
        String swBtnFin = sharedPreferences.getString("swBtnFin", "");
        Log.d("COMPROBACION COMPRAVACION", servicioini + "CodEq: " + codigoEquipo + "servillo: " + servillo + " actCod: " + actCodigo + " swBtnFin: " + swBtnFin);


        String url = "http://172.16.32.50/gzapto/ajax/servicio.php?op=guiaPorCerrar";
        String url2 = "http://172.16.32.50/gzapto/ajax/servicio.php?op=selecttestadossapandroid";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("RESPUESTA GUIAPORCERRAR", response);
                    try {

                        JSONObject jsonResponse = new JSONObject(response);
                        int cerrarguia = jsonResponse.getInt("cerrarguia");
                        if (cerrarguia == 1) {
                            Toast.makeText(getApplicationContext(), "Exito, se puede cerrar esta guia", Toast.LENGTH_SHORT).show();

                            //Gestión para Opciones de estados de equipos
                            StringRequest request2 = new StringRequest(Request.Method.POST, url2,
                                    response1 -> {
                                        Log.d("El resultado del selectestados", response1);
                                        try {
                                            imageView14.setVisibility(View.VISIBLE);
                                            spEstadoF.setVisibility(View.VISIBLE);
                                            pbu.setVisibility(View.GONE);
                                            JSONArray jsonArray = new JSONArray(response1);
                                            List<String> estados = new ArrayList<>();
                                            //estados.add(0, "SELECCIONAR OPCIÓN");
                                            //Se asigna al listado las opciones que vienen de la respuesta basada en su nombre
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject estadoJson = jsonArray.getJSONObject(i);
                                                String name = estadoJson.getString("name");
                                                String value = estadoJson.getString("value");
                                                estados.add(name);
                                                valoresValue.add(value);
                                            }

                                            //Se puebla el spinner o combobox
                                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(inicioServicio.this, R.layout.spinner_items, estados);
                                            spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                                            spEstadoF.setAdapter(spinnerAdapter);

                                            //Conocer opción eleguida
                                            spEstadoF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                                    //Se obtiene el valor VALUE del elemento seleccioando
                                                    String valorSeleccionado = valoresValue.get(position);
                                                    String estadoSeleccionado = estados.get(position);
                                                    Toast.makeText(getApplicationContext(), "Valor seleccionado: " + valorSeleccionado, Toast.LENGTH_SHORT).show();
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("idestadofi", valorSeleccionado);
                                                    editor.putString("estadoascensor", estadoSeleccionado);
                                                    editor.apply();
                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> adapterView) {

                                                }
                                            });
                                        } catch (JSONException e) {
                                            Log.d("Error en selectestados", e.toString());
                                        }
                                    },
                                    error -> Log.d("EROR DE VOLEY", error.toString())
                            );
                            Volley.newRequestQueue(inicioServicio.this).add(request2);

                        } else {
                            Toast.makeText(getApplicationContext(), "Esta guia no se puede cerrar", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Log.d("Error en GUIAPORCERRAR", error.toString())
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //Variable que se pasa por POST para saber si la guia se puede cerrar.
                params.put("idactividad", actCodigo);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);

    }

    //Metodo para traer la información de la guia de servicio a terminar.
    public void infogias() {
        //Cambio en el Layout con animación
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.END);
        slide.setDuration(500);
        TransitionManager.beginDelayedTransition(clPop2, slide);
        clPop2.setVisibility(View.GONE);

        new Handler().postDelayed(() -> {
            clPop3.setVisibility(View.VISIBLE);
            new Handler().postDelayed(() -> {

            }, 3000);
        }, 500);
        String servicioini = sharedPreferences.getString("servicioini", "");
        String actCodigo = sharedPreferences.getString("actCodigo", "");
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();

        if (network != null) {
            String url3 = "http://172.16.32.50/gzapto/ajax/servicio.php?op=infoguiasap";
            StringRequest request3 = new StringRequest(Request.Method.POST, url3,
                    response -> {
                        //Metodos que se iniciar con la respuesta valida
                        setupAyudantes();
                        setupCantAyudantes();
                        pbFin.setVisibility(View.GONE);
                        spOpayu.setVisibility(View.VISIBLE);
                        Log.d("RESPUESTA DE INFOGUIASAP", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String codigoEquipo = jsonObject.getString("codigo");
                            String customerCode = jsonObject.getString("CustomerCode");
                            String idascensor = jsonObject.getString("idascensor");
                            String edificio = jsonObject.getString("edificio");
                            String nomenclatura = jsonObject.getString("nomenclatura");
                            String supervisorID = jsonObject.getString("supervisorID");

                            //Captura de datos traidos
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("CustomerCode", customerCode);
                            editor.putString("codigofmfi", codigoEquipo);
                            editor.putString("ascensorIDfi", idascensor);
                            editor.putString("edificiofi", edificio);
                            editor.putString("codclifi", nomenclatura);
                            editor.putString("supervisorID", supervisorID);
                            editor.apply();
                            Toast.makeText(getApplicationContext(), codigoEquipo, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Log.d("Error en INFOGUIASAP", e.toString());
                        }
                    },
                    error -> Log.d("Error en INFOGUIASAP", error.toString())
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    //Valores que se pasan por POST
                    params.put("idserviciofi", servicioini);
                    params.put("idactividad", actCodigo);
                    return params;
                }
            };
            Volley.newRequestQueue(inicioServicio.this).add(request3);

        } else {
            setupAyudantes();
            setupCantAyudantes();
            pbFin.setVisibility(View.GONE);
            spOpayu.setVisibility(View.VISIBLE);
        }
    }

    //Metodo para llenar el spinner de opciones de ayudantes
    public void setupAyudantes() {

        List<String> opciones = new ArrayList<>();
        opciones.add("SELECCIONE OPCIÓN");
        opciones.add("Si");
        opciones.add("No");

        Spinner spOpayu;
        spOpayu = findViewById(R.id.spOpayu);

        //Población del Spinner o combobox
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_items, opciones);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spOpayu.setAdapter(spinnerAdapter);

        //Listener para interactuar con opción eleguida
        spOpayu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String seleccion = adapterView.getItemAtPosition(position).toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (seleccion.equals("Si")) {
                    //Selecciona ayudantes
                    Toast.makeText(getApplicationContext(), "OJO", Toast.LENGTH_SHORT).show();
                    tvCantAyu.setVisibility(View.VISIBLE);
                    spCantAyud.setVisibility(View.VISIBLE);
                    imageView11.setVisibility(View.GONE);
                    String opayu = "S";
                    editor.putString("opayu", opayu);
                    editor.apply();

                } else if (seleccion.equals("No")) {
                    //No selecciona ayudantes
                    spCantAyud.setVisibility(View.GONE);
                    tvCantAyu.setVisibility(View.GONE);
                    tvAyu1.setVisibility(View.GONE);
                    tvAyu2.setVisibility(View.GONE);
                    spAyu1.setVisibility(View.GONE);
                    spAyu2.setVisibility(View.GONE);
                    imageView11.setVisibility(View.VISIBLE);
                    String opayu = "N";
                    editor.putString("opayu", opayu);
                    editor.putInt("cantayu", 0);
                    editor.putString("cantAyud", "0");
                    editor.putString("ayudante1", "");
                    editor.putInt("idayud1", 0);
                    editor.putString("ayudante2", "");
                    editor.putInt("idayud2", 0);
                    editor.apply();


                } else {

                    spCantAyud.setVisibility(View.GONE);
                    tvCantAyu.setVisibility(View.GONE);
                    tvAyu1.setVisibility(View.GONE);
                    tvAyu2.setVisibility(View.GONE);
                    spAyu1.setVisibility(View.GONE);
                    spAyu2.setVisibility(View.GONE);
                    imageView11.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //Poblacion de spinner para las opcioens de firma
    public void setupFirmas() {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nombresfi", "");
        editor.putString("apellidosfi", "");
        editor.putString("rutfi", "");
        editor.putString("cargofi", "");
        editor.apply();

        List<String> opciones = new ArrayList<>();
        opciones.add("SELECCIONE OPCIÓN");
        opciones.add("FIRMADA");
        opciones.add("POSPONER");
        opciones.add("NO REQUIERE FIRMA");

        //Población del Spinner o combobox
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_items, opciones);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spOpFirma.setAdapter(spinnerAdapter);

        //Listener para interactuar con opción eleguida
        spOpFirma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String seleccion = adapterView.getItemAtPosition(position).toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                int opfirma = 0;
                switch (seleccion) {
                    case "FIRMADA":
                        Toast.makeText(getApplicationContext(), "FIRMADA", Toast.LENGTH_SHORT).show();
                        clPop7.setVisibility(View.VISIBLE);
                        clPop6.setVisibility(View.GONE);
                        opfirma = 1;
                        editor.putInt("opfirma", opfirma);
                        editor.putBoolean("porfirmar", true);
                        editor.apply();
                        break;
                    case "POSPONER":
                        opfirma = 2;
                        editor.putInt("opfirma", opfirma);
                        editor.putBoolean("porfirmar", false);
                        editor.apply();
                        break;
                    case "NO REQUIERE FIRMA":
                        opfirma = 3;
                        editor.putInt("opfirma", opfirma);
                        editor.putBoolean("porfirmar", false);
                        editor.apply();
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    //Metodo para poblar cbo y listener para las opciones de presupuesto
    public void setupPresupuesto() {
        List<String> opciones = new ArrayList<>();
        opciones.add("SELECCIONE OPCIÓN");
        opciones.add("Si");
        opciones.add("No");


        Spinner spPresup;
        spPresup = findViewById(R.id.spPresup);
        //Población del Spinner o combobox
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_items, opciones);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spPresup.setAdapter(spinnerAdapter);

        //Listener para interactuar con opción eleguida
        spPresup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String seleccion = adapterView.getItemAtPosition(position).toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("seleccion", seleccion);

                if (seleccion.equals("Si")) {
                    Toast.makeText(getApplicationContext(), "OJO", Toast.LENGTH_SHORT).show();
                    btnGaleria.setVisibility(View.VISIBLE);
                    gvImagenes.setVisibility(View.VISIBLE);
                    imageView17.setVisibility(View.VISIBLE);
                    tvMax.setVisibility(View.VISIBLE);
                    int oppre = 1;
                    editor.putInt("oppre", oppre);
                    editor.apply();

                } else if (seleccion.equals("No")) {
                    btnGaleria.setVisibility(View.GONE);
                    gvImagenes.setVisibility(View.GONE);
                    imageView17.setVisibility(View.GONE);
                    tvMax.setVisibility(View.GONE);
                    imageView17.setVisibility(View.VISIBLE);
                    int oppre = 2;
                    editor.putInt("oppre", oppre);
                    editor.putString("descripcion", "");
                    editor.apply();
                } else {
                    btnGaleria.setVisibility(View.GONE);
                    gvImagenes.setVisibility(View.GONE);
                    imageView17.setVisibility(View.GONE);
                    tvMax.setVisibility(View.GONE);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void moveFoward(ConstraintLayout cl1, ConstraintLayout cl2) {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.END);
        slide.setDuration(500);
        TransitionManager.beginDelayedTransition(cl1, slide);
        cl1.setVisibility(View.GONE);

        new Handler().postDelayed(() -> {
            cl2.setVisibility(View.VISIBLE);
            setupPresupuesto();
            new Handler().postDelayed(() -> {

            }, 3000);
        }, 500);
    }

    //Metodo para retroceder un Layout con animación hacia la izquierda
    public void backBck(View v) {
        trasitionViews(clPop2, clPop);


    }

    public void backBck4(View v) {
        trasitionViews(clPop5, clPop4);
    }

    public void trasitionViews(View hideView, View showView) {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.START);
        slide.setDuration(500);
        TransitionManager.beginDelayedTransition((ViewGroup) hideView.getParent(), slide);
        hideView.setVisibility(View.GONE);
        showView.setVisibility(View.VISIBLE);
    }


    //Mismo que el anterior
    public void backBck2(View v) {
        trasitionViews(clPop3, clPop2);

    }

    //Metodo para el poblado del combobox de cantidad de ayudantes
    public void setupCantAyudantes() {

        //Opciones
        List<String> opciones = new ArrayList<>();
        opciones.add("Seleccione cantidad de ayudantes");
        opciones.add("1");
        opciones.add("2");


        //Poblamiento del spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_items, opciones);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spCantAyud.setAdapter(spinnerAdapter);

        //Listener para saber la opción eleguida e interactuar en base a ella.
        spCantAyud.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String seleccion = adapterView.getItemAtPosition(position).toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                Network network = connectivityManager.getActiveNetwork();

                if (seleccion.equals("1")) {
                    cantidadAyudantesSeleccionados = 1;
                    pbF2.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Marcaste 1", Toast.LENGTH_SHORT).show();
                    tvAyu1.setVisibility(View.VISIBLE);
                    spAyu1.setVisibility(View.VISIBLE);
                    //Se llama al metodo selectTecnico para el spinner spAyu1

                    if (network != null) {
                        selectTecnico1(spAyu1);
                    } else {
                        selectTecnico1SinInternet(spAyu1);
                        Toast.makeText(getApplicationContext(), " No hay net pero se trae igual, tamos o no?!", Toast.LENGTH_SHORT).show();
                    }

                    imageView11.setVisibility(View.GONE);
                    editor.putString("cantAyud", "1");
                    editor.putInt("cantayu", 1);
                    editor.apply();


                } else if (seleccion.equals("2")) {
                    Toast.makeText(getApplicationContext(), "Marcaste 2", Toast.LENGTH_SHORT).show();
                    cantidadAyudantesSeleccionados = 2;
                    pbF2.setVisibility(View.VISIBLE);
                    tvAyu1.setVisibility(View.VISIBLE);
                    spAyu1.setVisibility(View.VISIBLE);
                    tvAyu2.setVisibility(View.VISIBLE);
                    spAyu2.setVisibility(View.VISIBLE);
                    //Se llama al metodo selectTecnico para el spinner spAyu1 y spAyu2
                    if (network != null) {
                        selectTecnico1(spAyu1);
                        selectTecnico2(spAyu2);
                    } else {
                        selectTecnico1SinInternet(spAyu1);
                        selectTecnico2SinInternet(spAyu2);
                        Toast.makeText(getApplicationContext(), " No hay net pero se trae igual, tamos o no?!", Toast.LENGTH_SHORT).show();
                    }
                    imageView11.setVisibility(View.GONE);
                    editor.putString("cantAyud", "2");
                    editor.putInt("cantayu", 2);
                    editor.apply();
                } else {
                    tvAyu1.setVisibility(View.GONE);
                    spAyu1.setVisibility(View.GONE);
                    tvAyu2.setVisibility(View.GONE);
                    spAyu2.setVisibility(View.GONE);
                    imageView11.setVisibility(View.GONE);
                    editor.putString("cantAyud", "0");
                    editor.putInt("cantayu", 0);
                    editor.apply();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //El metodo que trae a todos los tecnicos
    public void selectTecnico1(Spinner spTecnico) {
        String url = "http://172.16.32.50/gzapto/ajax/servicio.php?op=selectTecnicoandroid";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("Response de TECNICOS 1", response);
                    try {
                        JSONArray jsonArray1 = new JSONArray(response);
                        List<String> tecnicos1 = new ArrayList<>();
                        List<Integer> employeeIds1 = new ArrayList<>();

                        //Se recorre la respuesta y se asigna sus valores al listado
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            JSONObject tecicosJson1 = jsonArray1.getJSONObject(i);
                            String fullName = tecicosJson1.getString("FullName");
                            int employeeId = tecicosJson1.getInt("EmployeeID");
                            tecnicos1.add(fullName);
                            employeeIds1.add(employeeId);

                        }
                        pbF2.setVisibility(View.GONE);

                        //Se puebla el spinner
                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_items, tecnicos1);
                        spTecnico.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                String fullNameSeleccionado = tecnicos1.get(position);
                                int employeeIdSeleccionado = employeeIds1.get(position);
                                idtec1 = employeeIdSeleccionado;
                                String nametec1 = fullNameSeleccionado;


                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("idayud1", idtec1);
                                editor.putString("ayudante1", nametec1);
                                editor.apply();
                                Toast.makeText(getApplicationContext(), "EmployeeIDSeleccionado: " + idtec1, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        spTecnico.setAdapter(spinnerAdapter);
                        imageView11.setVisibility(View.VISIBLE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace
        );
        Volley.newRequestQueue(this).add(request);
    }


    //El metodo que trae a todos los tecnicos para el otro cbo sin errores
    public void selectTecnico2(Spinner spTecnico) {
        String url = "http://172.16.32.50/gzapto/ajax/servicio.php?op=selectTecnicoandroid";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("Response de TECNICOS 2", response);
                    try {
                        JSONArray jsonArray2 = new JSONArray(response);
                        List<String> tecnicos2 = new ArrayList<>();
                        List<Integer> employeeIds2 = new ArrayList<>();

                        //Se recorre la respuesta y se asigna sus valores al listado
                        for (int i = 0; i < jsonArray2.length(); i++) {
                            JSONObject tecicosJson2 = jsonArray2.getJSONObject(i);
                            String fullName2 = tecicosJson2.getString("FullName");
                            int employeeId2 = tecicosJson2.getInt("EmployeeID");
                            tecnicos2.add(fullName2);
                            employeeIds2.add(employeeId2);

                        }
                        pbF2.setVisibility(View.GONE);

                        //Se puebla el spinner
                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_items, tecnicos2);
                        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        spTecnico.setAdapter(spinnerAdapter);
                        imageView11.setVisibility(View.VISIBLE);

                        spTecnico.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                String fullNameSeleccionado2 = tecnicos2.get(position);
                                int employeeIdSeleccionado2 = employeeIds2.get(position);
                                idtec2 = employeeIdSeleccionado2;
                                String nametec2 = fullNameSeleccionado2;


                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("idayud2", idtec2);
                                editor.putString("ayudante2", nametec2);
                                editor.apply();
                                Toast.makeText(getApplicationContext(), "EmployeeIDSeleccionado 2: " + idtec2, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace
        );
        Volley.newRequestQueue(this).add(request);
    }

    //Metodo para el menú desplegable
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Metodo para el menú desplegable
    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    //Metodo para el menú desplegable
    public void mostrarMenu(View v) {

        if (muestra) {
            appBarLayout.setVisibility(View.VISIBLE);
            muestra = false;
            background_overlay2.setVisibility(View.VISIBLE);
        } else {
            appBarLayout.setVisibility(View.GONE);
            muestra = true;
            background_overlay2.setVisibility(View.GONE);
        }
    }

    //Metodo para salir de los Poups
    public void salirPopUp(View v) {
        clPop.setVisibility(View.GONE);
        clPop2.setVisibility(View.GONE);
        clPop3.setVisibility(View.GONE);
        clPop4.setVisibility(View.GONE);
        clPop5.setVisibility(View.GONE);
        clPop6.setVisibility(View.GONE);
        clPop7.setVisibility(View.GONE);
        clPop8.setVisibility(View.GONE);
    }

    public void subirImagenes() {
        Toast.makeText(getApplicationContext(), "SubirImagenes sin mas", Toast.LENGTH_SHORT).show();
        listaBase64Imagenes.clear();
        Context context = getApplicationContext();

        if (listaImagenes.size() >= 1 && listaImagenes.size() <= 3) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("cantidadImagenes", listaImagenes.size());
            editor.apply();


            //Mas de una imagen
            if (context != null) {
                for (int i = 0; i < listaImagenes.size(); i++) {
                    try {
                        InputStream is = getContentResolver().openInputStream(listaImagenes.get(i));
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        String cadena = convertirUriToBase64(bitmap);
                        String nombreImagen = getFileNameFromUri(listaImagenes.get(i));
                        listaBase64Imagenes.add(cadena);


                        //Guardar imagenes en shared
                        editor.putString("file0" + (i + 1), nombreImagen);
                        editor.apply();

                        String fileName = sharedPreferences.getString("file01", "");
                        String fileName2 = sharedPreferences.getString("file02", "");
                        String fileName3 = sharedPreferences.getString("file03", "");
                        Log.d("Imagenes a shaco1", fileName);
                        Log.d("Imagenes a shaco2", fileName2);
                        Log.d("Imagenes a shaco3", fileName3);

                        bitmap.recycle();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show();
            }

        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("cantidadImagenes", 0);
            editor.apply();
        }


    }


    // Función para obtener el nombre del archivo desde la Uri
    private String getFileNameFromUri(Uri uri) {
        Cursor cursor = null;
        try {
            //String[] projection = {MediaStore.Images.Media.DISPLAY_NAME};
            cursor = getContentResolver().query(uri,null,null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);

                if(displayNameIndex != -1){
                    String fileName = cursor.getString(displayNameIndex);
                    return fileName;
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        Random random = new Random();
        int randomNumber = 100000000 + random.nextInt(90000000);
        return "imagen_" + randomNumber;
    }


    public String getRealPathFromURI(Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            filePath = cursor.getString(column_index);
            cursor.close();
        }
        return filePath;
    }


    @SuppressLint("StaticFieldLeak")
    public class SubirImagenesTask extends AsyncTask<Uri[], Void, Void> { //Clase para subir imagenes al servidor en segundo plano (Async). Los parametros: Un array de URIS con las fotos. Void para el progreso, Void tipo de datos que se devuelvan al completar la tarea
        private final String serverUrl;

        public SubirImagenesTask(String serverUrl) { //Constructor de la clase recibe URL del servidor (metodo interno) y se asigna a serverUrl
            this.serverUrl = serverUrl;
        }

        @Override
        protected Void doInBackground(Uri[]... urisArray) { //doInBackground es el trabajando en segundo plano propio de esta clase, recibe un parametro de matriz de Uris
            Uri[] uris = urisArray[0]; //se accede a la matriz de URIS que viene por parametro
            //Llamar al metodo subirImagenesAlServidor aqui pansado la lista de uris y serverUrl
            subirImagenesAlServidor(serverUrl, uris);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //Aqui se realizan acciones despues de que se haya suvido las imagenes
        }
    }


    //Metodo para subir imagenes de presupuesto al servidor con HTTP
    public void subirImagenesAlServidor(String serverUrl, Uri[] uris) { //Recibe 2 parametros, la url del servidor y la matriz de uris
       // Toast.makeText(getApplicationContext(), "SubirImagenesAlServidor activo", Toast.LENGTH_SHORT).show();
        int serverResponseCode;
        String lineEnd = "\r\n"; //Variable que se usa como salto de linea en solicitud HTTP.
        String boundary = "--MyBoundary123";  //Texto de boundary para separar diferentes partes de los datos
        String twoHyphens = "--"; //Tambien sirve para delimitar la solicitud HTTP

        try {
            URL url = new URL(serverUrl); //Con URL se esta almacenando una dirección web, en este caso la URL del servidor interno

            //Se establece una conexion HTTP a la url del servidor y se envian las imagenes como POST
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //Se establece conexión HTTP con el servidor web
            conn.setDoInput(true); //Se permite la entrada de datos desde el servidor, ncesario pues enviamos imagenes al server
            conn.setDoOutput(true); //Se permite la salida de datos hacia el servidor.
            conn.setUseCaches(false); //No se usan datos almacenados en el cache
            conn.setRequestMethod("POST"); //Se envian los datos como POST al server
            conn.setRequestProperty("Connection", "Keep-Alive"); //Mantiene la conexión viva para hacer mas rapida y eficiente en un futuro
            conn.setRequestProperty("ENCTYPE", "multipart/form-data"); //Se usa multipart/form-data para la codificacion para los datos
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary); //Especifica el tipo de contenido de la solicitud y se usa el boundary como delimitador

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream()); //Este DataOutputStream esta conectado a la salida de la contexion HTTP.

            for (int i = 0; i < uris.length; i++) { //Se recorren las URIs, osea las fotos
                Uri uri = uris[i]; //En cada itereacion la variable uri obtiene la URI especidfica del aray
                String filePath = getRealPathFromURI(uri); //Obtener la ruta real de la URI del archivo
                String nombreImagen = getFileNameFromUri(uri); //Obtiene el nombre del archivo
                File sourceFile = new File(filePath); //Mediante un File se representas el archivo del dispositivo. sourcefile apunto al archivo real que se desea cargar.

                if (!sourceFile.isFile()) { //El archivo existe?
                    //Manejar el caso en el que el archivo no existe
                    continue; //Esta sentencia hace que no se ejecute el resto del for
                }

                //Agregar encabezados para los archivos
                dos.writeBytes(twoHyphens + boundary + lineEnd); //Agrega una linea al cuerpo de la solicitud que conssite en el boundary
                dos.writeBytes("Content-Disposition: form-data; name=\"file[]\";filename=\"" + nombreImagen + "\"" + lineEnd); //Agrega una linea al cuerpo de la solicitud que indica como se debe tratar el archivo que se esta envando. : Esto establece el encabezado de disposición de contenido con dos parámetros. El primero, name, indica que el nombre del campo de formulario es "file[]". El segundo, filename, especifica el nombre del archivo que se está enviando, que se obtiene del objeto sourceFile utilizando sourceFile.getName().
                dos.writeBytes(lineEnd); //Se agrega un segundo salto de linea al cuerpo de la solicitud, que separa el encabezado de disposicion de contenido real del archivo que se enviara a continuacion

                FileInputStream fileInputStream = new FileInputStream(sourceFile); //El FileInputStream se usa para leer los bytes del archivo 'sourceFile' lo que permite que el programa lea el contenido del archivo byte por byte
                int bytesRead; //Variable que se usara mas adelante en el bucle para rastrrear la cantidad de bytes que se leen en cada iteracion
                byte[] buffer = new byte[4096]; //Se crea un bufer de bytes para almacenar temporalmente los bytes que se leen del archivo antes de enviarlos al servidor, el buffer tiene 4kb

                while ((bytesRead = fileInputStream.read(buffer)) != -1) { //Inicia un bucle while que se ejecuta mientras haya datos disponibles para leer del fileInputStream, la cantidad de datos leidos se almacena en la variable bytesRead. Entonces el bytesRead es el resultado de fileInputSream.read(buffer) que verifica si se llego al final del archivo -1 o si todavia hay datos para ller, se va a ejecutar mientras haya datos que leer
                    dos.write(buffer, 0, bytesRead); //Esta linea escribe los datos leidos del archivo alamcenados en el bufer en la solicitud HTTP que se esta construyendo(dos es el flujo de salida de datos asociado a la solicitud). 0 es la posicion inicial del bufer desde la cual se empezaran a leer los datos. bytesRead es la cantidad de bytes leidos en la ultima operacion de lectura, por lo que se especifica cuantos bytes se deben escribir en la solicitud.Esto aseura que solo se envien los datos leidos y o bytes adicionales
                }


                //Finalizar la parte del archivo en la solicitud
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            }

            dos.flush();
            dos.close();

            //Obtener la respuesta del servidor (codigo y mensaje)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();
            Log.i("appTag", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);


            //Cerrar conexion
            conn.disconnect();

            //Realizar acciones despues de una respuesta exitosa (codigo 200)
            if (serverResponseCode == 200) {
                // Toast.makeText(getApplicationContext(), "EXITO 24, MUY BIEN!!", Toast.LENGTH_SHORT).show();
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Log.d("appTag", "UL error: " + ex.getMessage(), ex);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Upload file to server Exception", "Exception: " + e.getMessage(), e);
        }
    }


    public String convertirUriToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        String encode = Base64.encodeToString(bytes, Base64.DEFAULT);

        return encode;
    }

    //Metodo para seleccionar imagenes de presupuesto desde la galeria
    public void abrirGaleria() {
        Intent intent = new Intent(); //Objeto Intent para abrir la galeria
        intent.setType("image/*"); //Intent solo maneja contenido de imagenes, el * es para pemitir JPEG, PNG, GIF
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); //Se añade un extra al Intent para manejar multiples archivos.
        intent.setAction(Intent.ACTION_GET_CONTENT); //Permite obtener contenido o las imagenes desde la galeria
        startActivityForResult(Intent.createChooser(intent, "Selecciona las imagenes"), PICK_IMAGE); //Al combinarse con la sentencia anterior, permite abrir la galeria para seleccionar imagenes y el resultado se camputa en onActivityResult
    }

    //Evento cuando se seleccionan imagenes desde la galeria
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> uriStrings = new HashSet<>();
        editor.putStringSet("listaImagenes", new HashSet<>());

        //ClipData clipData = data.getClipData();
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) { //Se selecciona correctamente alguna imagen
            Toast.makeText(getApplicationContext(), "ENTRASTE A IF RESULT OK", Toast.LENGTH_SHORT).show();
            if (data != null) { //Se seleccionan varias iamgenes
                ClipData clipData = data.getClipData(); //Clip data que srirve para representar los datos que vienen del Intent data (imagenes)

                if (clipData != null) { //Varias imagenes
                    if (clipData.getItemCount() > 3) { //Validacion maxima 3 fotos
                        mensajeError("No puedes subir mas de 3 imagenes");
                    } else {
                        for (int i = 0; i < clipData.getItemCount(); i++) { //Se recorre el el clipData que representa las imagenes seleccionadas
                            Uri uri = clipData.getItemAt(i).getUri(); //Se extrae la uri de cada una, la uri es como un identificador de los recursos
                            listaImagenes.add(uri); //Se agrefa la uri a la lista

                            String nombreImagen = getFileNameFromUri(uri); //Obtener nombre de la imagen
                            int posicion = i+1; //Determinar la posicion en función del indice
                            editor.putString("file0"+posicion, nombreImagen);

                        }
                    }
                } else { //Caso de subir solo una imagen
                    Uri imagenUri = data.getData(); //Se añade al listadp
                    listaImagenes.add(imagenUri);

                    String nombreImagen = getFileNameFromUri(imagenUri); //Obtener el nombre de la imagen
                    int posicion = listaImagenes.size(); //Determinar posicion e funcion del tamaño actual de la imagen
                    editor.putString("file0" + posicion, nombreImagen);
                }

                for (Uri uri : listaImagenes) {
                    uriStrings.add(uri.toString());
                }

                editor.putStringSet("listaImagenes", uriStrings);
                editor.apply();
                baseAdapter = new GridViewAdapter(getApplicationContext(), listaImagenes);
                gvImagenes.setAdapter(baseAdapter);
                Toast.makeText(getApplicationContext(), "URI STRING DICE: " + uriStrings, Toast.LENGTH_SHORT).show();
                //http://172.16.32.87/gzapto/ajax/uploadpres.php?op=interno
                //SubirImagenesTask subirImagenesTask = new SubirImagenesTask("http://172.16.32.87/gzapto/ajax/uploadpres.php?op=interno");
                //subirImagenesTask.execute(listaImagenes.toArray(new Uri[0]));

            }

        }
    }

    //Metodo que verifica si hay firma y la almacena
    public void validarFirma(View v) {
        firmaplicada = true;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("firmaaplicada", firmaplicada);
        editor.apply();
        if (signatureView.isBitmapEmpty()) {
            mensajeError("La firma no puede ser vacia");

        } else {
            //Se guarda la firma
            Bitmap firmaBitmap = signatureView.getSignatureBitmap();

            if(firmaBitmap != null){
                //Convertir Bitmap en cadena base64
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                firmaBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String base64Image = "data:image/png;base64,"+Base64.encodeToString(byteArray, Base64.DEFAULT);
                Log.d("BASE 64 DE FIRMA: ", base64Image);

                if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE); //Solicitar permisos
                }else{
                    Toast.makeText(getApplicationContext(), "El valor de la firmaBitmap: "+firmaBitmap, Toast.LENGTH_SHORT).show();
                    editor.putString("firma", base64Image);
                    editor.apply();
                    //guardarBase64EnMemoriaInterna(padfirma, "firma_archivo.png");
                    finalizarServ();

                    saveImageToExternalStorage(firmaBitmap);
                }
            }
            //Log.d("Valor del firmaBitmap: ", String.valueOf(firmaBitmap));
            //guardarFirma(firmaBitmap);
            //padfirma = convertirBitmapADataURL(firmaBitmap);
            //Log.d("EL VALOR DEL PAD FIRMA:", padfirma);
            //Toast.makeText(getApplicationContext(), "El valor del padfirma o de conversion o la firma en si: "+firmaBitmap, Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(firmaBitmap != null){
                    saveImageToExternalStorage(firmaBitmap);
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

    //Método para convertir un Bitmap a una cadena de datos URL
    private String convertirBitmapADataURL(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return "data:image/png;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    //Metodo para mostrar menasjes Toast con diseño de error.
    public void mensajeError(String mensaje) {

        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View customToastView = inflater.inflate(R.layout.custom_toast, null);
        TextView textView = customToastView.findViewById(R.id.custom_toast_text);
        textView.setText(mensaje);
        Toast customToast = new Toast(getApplicationContext());
        customToast.setDuration(Toast.LENGTH_SHORT);
        customToast.setView(customToastView);
        customToast.show();
    }

    //Mensaje de confirmación
    public void finalizarServ() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmación");
        builder.setMessage("¿Deseas dar por terminado el servicio pipipi?");

        builder.setPositiveButton("Si", (dialogInterface, i) -> {
            dialogInterface.dismiss();
            finalizarServicio2();
           // finalizarServicio2();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("file01");
            editor.remove("file02");
            editor.remove("file03");
            editor.apply();
        });

        builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());

        builder.show();
    }
    //editor.putInt("cerrargianet", cerrarguia);
    //Metodo para finalizar el servicio
    public void finalizarServicio2() {


        //Validación de Internet en el dispositivo
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();

        if(network != null){

            String fileName = sharedPreferences.getString("file01", "");
            String fileName2 = sharedPreferences.getString("file02", "");
            String fileName3 = sharedPreferences.getString("file03", "");
            mensajeError("OJO: " + fileName + fileName2 + fileName3);
            String latitudfi = "-33.3843782";
            String longitudfi = "-70.7761974";
            String terminado = "y";
            String attachments = "";
            String txtObsFin = "";
            String idserviciofi="";
            String idascensorfi="";

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("attachments", attachments);
            editor.putString("txtObsFin", txtObsFin);
            editor.putString("idserviciofi", idserviciofi);
            editor.putString("idascensorfi", "");
            editor.putString("latitudfi", latitudfi);
            editor.putString("longitudfi", longitudfi);
            editor.putString("terminado", terminado);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
            String currentDateAndTime = dateFormat.format(new Date());
            String fileNamee = sharedPreferences.getString("file01", "");
            String fileNamee2 = sharedPreferences.getString("file02", "");
            String fileNamee3 = sharedPreferences.getString("file03", "");
            editor.putString("dTime", currentDateAndTime);
            String servicecallIDfi = sharedPreferences.getString("servicioini", "");
            String actividadIDfi = sharedPreferences.getString("actCodigo", "");
            String customercodefi = sharedPreferences.getString("CustomerCode", "");
            String codigofmfi = sharedPreferences.getString("codigofmfi", "");
            String ascensorIDfi = sharedPreferences.getString("ascensorIDfi", "");
            String edificiofi = sharedPreferences.getString("edificiofi", "");
            String codclifi = sharedPreferences.getString("codclifi", "");
            String estadoascensor = sharedPreferences.getString("estadoascensor", "");
            String supervisorID = sharedPreferences.getString("supervisorID", "");
            String idestadofi = sharedPreferences.getString("idestadofi", "");
            String observacionfi = sharedPreferences.getString("observacionfi", "");
            String opayu = sharedPreferences.getString("opayu", "");
            String cantAyud = sharedPreferences.getString("cantAyud", "");
            String ayudante1 = sharedPreferences.getString("ayudante1", "");
            String ayudante2 = sharedPreferences.getString("ayudante2", "");
            int cantayu = sharedPreferences.getInt("cantayu", 0);
            int idayud1 = sharedPreferences.getInt("idayud1", 0);
            int idayud2 = sharedPreferences.getInt("idayud2", 0);
            int oppre = sharedPreferences.getInt("oppre", 0);
            int opfirma = sharedPreferences.getInt("opfirma", 0);
            String nombresfi = sharedPreferences.getString("nombresfi", "");
            String apellidosfi = sharedPreferences.getString("apellidosfi", "");
            String rutfi = sharedPreferences.getString("rutfi", "");
            int idSAP = sharedPreferences.getInt("idSAP", 0);
            String cargofi = sharedPreferences.getString("cargofi", "");
            String email = sharedPreferences.getString("email", "");
            Boolean porfirmar = sharedPreferences.getBoolean("porfirmar", false);
            String descripcion = sharedPreferences.getString("descripcion", "");
            String firma = sharedPreferences.getString("firmaA", "");
            if (oppre == 1) {
                Set<String> uriStrings = sharedPreferences.getStringSet("listaImagenes", new HashSet<>());
                List<Uri> listaImagenesRecuperadas = new ArrayList<>();
                for (String uriString : uriStrings) {
                    listaImagenesRecuperadas.add(Uri.parse(uriString));
                }
                SubirImagenesTask subirImagenesTask = new SubirImagenesTask("http://172.16.32.50/gzapto/ajax/uploadpres.php?op=interno");
                subirImagenesTask.execute(listaImagenes.toArray(new Uri[0]));
            }

            Log.d("file01: " + fileNamee + " file02: " + fileNamee2 + " file03: " + fileNamee3 + " dTime: ", currentDateAndTime + " serviceid: " + servicecallIDfi + " actividadID: " + actividadIDfi + " customercode: " + customercodefi + " codigofmfi:" + codigofmfi + " ascensorIDfi:" + ascensorIDfi + " edificiofi:" + edificiofi
                    + " codclifi: " + codclifi + " supervisorid:" + supervisorID + " lattitudfi: " + latitudfi + " longitudfi: " + longitudfi + " idestadofi: " + idestadofi + " observacionfi: " + observacionfi
                    + " terminado: " + terminado + " opayu: " + opayu + " cantAyud" + cantAyud + " idayud1: " + idayud1 + " idayud2" + idayud2 + " oppre" + oppre + " opfirma:" + opfirma + " estadoascensor: " + estadoascensor
                    + "ayudante1: " + ayudante1 + " ayudante2: " + ayudante2 + " cantayu: " + cantayu + " nombresfi: " + nombresfi
                    + "apellidosfi: " + apellidosfi + " rutfi: " + rutfi + " cargofi" + cargofi + " porfirmar: " + porfirmar + " email: " + email + " idSAP: " + idSAP);
            editor.apply();

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    "http://172.16.32.50/gzapto/ajax/servicio.php?op=finalizarsap",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("RESPUESTA DE FIN FIN SERVICIO: ", response);
                            Toast.makeText(getApplicationContext(), "Servicio finalizado", Toast.LENGTH_SHORT).show();

                            if (firmaplicada == true) {
                                moveFoward(clPop8, clAnimacion);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(getApplicationContext(), menu.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, 5000);
                            } else {

                                editor.remove("file01");
                                editor.remove("file02");
                                editor.remove("file03");
                                editor.apply();
                                moveFoward(clPop6, clAnimacion);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(getApplicationContext(), menu.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, 5000);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.d("RESPUESTA DE FIN FIN SERVICIO: ", String.valueOf(error));
                            Toast.makeText(getApplicationContext(), "Error en la finalizacion del servicio: "+error, Toast.LENGTH_SHORT).show();
                            editor.remove("file01");
                            editor.remove("file02");
                            editor.remove("file03");
                            editor.apply();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {

                    //return super.getParams();
                    Map<String, String> params = new HashMap<>();
                    params.put("file01", fileName);params.put("file02", fileName2);params.put("file03", fileName3);
                    params.put("latitudfi", latitudfi);
                    params.put("longitudfi", longitudfi);
                    params.put("terminado", terminado);
                    params.put("dTime", currentDateAndTime);
                    params.put("servicecallIDfi", servicecallIDfi);
                    params.put("actividadIDfi", actividadIDfi);
                    params.put("customercodefi", customercodefi);
                    params.put("codigofmfi", codigofmfi);
                    params.put("ascensorIDfi", ascensorIDfi);
                    params.put("edificiofi", edificiofi);
                    params.put("codclifi", codclifi);
                    params.put("estadoascensor", estadoascensor);
                    params.put("supervisorID", supervisorID);
                    params.put("idestadofi", idestadofi);
                    params.put("observacionfi", observacionfi);
                    params.put("opayu", opayu);
                    params.put("cantayu", String.valueOf(cantayu));
                    params.put("oppre", String.valueOf(oppre));
                    params.put("opfirma", String.valueOf(opfirma));
                    params.put("idSAP", String.valueOf(idSAP));
                    params.put("email", email);
                    params.put("attachments", attachments);
                    params.put("txtObsFin", txtObsFin);
                    params.put("idascensorfi", idascensorfi);
                    params.put("idserviciofi", idserviciofi);
                    if (opfirma == 1){
                        params.put("nombresfi", nombresfi);
                        params.put("apellidosfi", apellidosfi);
                        params.put("rutfi", rutfi);
                        params.put("cargofi", cargofi);
                        params.put("porfirmar", String.valueOf(porfirmar));
                        params.put("firma", firma);
                    }
                    if(oppre ==1 ){
                        params.put("descripcion", descripcion);
                    }
                    if(opayu.equals("S")){
                        if(cantayu == 1){
                            params.put("ayudante1", ayudante1);
                            params.put("idayud1", String.valueOf(idayud1));
                            params.put("cantAyud", cantAyud);
                        } else if (cantayu == 2) {
                            params.put("ayudante1", ayudante1);
                            params.put("idayud1", String.valueOf(idayud1));
                            params.put("ayudante2", ayudante2);
                            params.put("idayud2", String.valueOf(idayud2));
                            params.put("cantAyud", cantAyud);
                        }
                    }
                    return params;
                }
            };
            int timeout = 30000;
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    timeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES
            ));
            Volley.newRequestQueue(this).add(stringRequest);
        }else{
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putBoolean("finalizarbloqueado", true);
            editor.putInt("finalizarsinnet", 1);
            editor.apply();
            moveFoward(clPop6, clAnimacion);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), menu.class);
                    startActivity(intent);
                    finish();
                }
            }, 5000);
        }
    }

    //Inicio de metodos sin INTERNET
    public void estadosDeEquipo() {
        Spinner spEstadoF = findViewById(R.id.spEstadoF3);
        clPop.setVisibility(View.VISIBLE);
        imageView14.setVisibility(View.VISIBLE);
        spEstadoF.setVisibility(View.VISIBLE);
        pbu.setVisibility(View.GONE);
        int cerrarguia = sharedPreferences.getInt("cerrargianet", 0);
        Toast.makeText(getApplicationContext(), "CERRARGUIA VALOR: " + cerrarguia, Toast.LENGTH_SHORT).show();
        if (cerrarguia == 1) {
            Toast.makeText(getApplicationContext(), "CERRAR GUIA SIN NET ACTIVO", Toast.LENGTH_SHORT).show();
            List<String> estados = new ArrayList<>();
            List<String> valoresValue = new ArrayList<>();

            estados.add("OPERATIVO");
            valoresValue.add("01");

            estados.add("DETENIDO");
            valoresValue.add("07");

            //Se puebla el spinner o combobox
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(inicioServicio.this, R.layout.spinner_items, estados);
            spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spEstadoF.setAdapter(spinnerAdapter);

            //Conocer opción eleguida
            spEstadoF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    //Se obtiene el valor VALUE del elemento seleccioando
                    String valorSeleccionado = valoresValue.get(position);
                    String estadoSeleccionado = estados.get(position);
                    Toast.makeText(getApplicationContext(), "Valor seleccionado: " + valorSeleccionado, Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("idestadofi", valorSeleccionado);
                    editor.putString("estadoascensor", estadoSeleccionado);
                    editor.apply();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "CERRAR GUIA SIN NET NO ES POSIBLE", Toast.LENGTH_SHORT).show();
        }
    }

    public void decideTermino(View v) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();

        if (network != null) {
            terminarServicio();
        } else {
            estadosDeEquipo();
        }
    }

    public void selectTecnico1SinInternet(Spinner spTecnico) {

        int numberOfTecnicos = sharedPreferences.getInt("numberOfTecnicos", 0);
        Toast.makeText(getApplicationContext(), "NUMERO DE TECNICOS: " + numberOfTecnicos, Toast.LENGTH_SHORT).show();

        List<String> tecnicos1 = new ArrayList<>();
        List<Integer> employeeIds1 = new ArrayList<>();

        //Se recorre la respuesta y se asigna sus valores al listado
        for (int i = 0; i < numberOfTecnicos; i++) {
            int employeeId = sharedPreferences.getInt("employeeIdNet" + i, 0);
            String fullName = sharedPreferences.getString("fullNameNet" + i, "");

            tecnicos1.add(fullName);
            employeeIds1.add(employeeId);

        }
        pbF2.setVisibility(View.GONE);

        //Se puebla el spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_items, tecnicos1);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spTecnico.setAdapter(spinnerAdapter);
        imageView11.setVisibility(View.VISIBLE);

        spTecnico.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String fullNameSeleccionado = tecnicos1.get(position);
                int employeeIdSeleccionado = employeeIds1.get(position);
                idtec1 = employeeIdSeleccionado;
                String nametec1 = fullNameSeleccionado;


                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("idayud1", idtec1);
                editor.putString("ayudante1", nametec1);
                editor.apply();
                Toast.makeText(getApplicationContext(), "EmployeeIDSeleccionado: " + idtec1, Toast.LENGTH_SHORT).show();
                imageView11.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void selectTecnico2SinInternet(Spinner spTecnico) {
        int numberOfTecnicos = sharedPreferences.getInt("numberOfTecnicos", 0);
        Toast.makeText(getApplicationContext(), "NUMERO DE TECNICOS2: " + numberOfTecnicos, Toast.LENGTH_SHORT).show();

        List<String> tecnicos2 = new ArrayList<>();
        List<Integer> employeeIds2 = new ArrayList<>();

        //Se recorre la respuesta y se asigna sus valores al listado
        for (int i = 0; i < numberOfTecnicos; i++) {
            String fullName2 = sharedPreferences.getString("fullNameNet" + i, "");
            int employeeId2 = sharedPreferences.getInt("employeeIdNet" + i, 0);
            tecnicos2.add(fullName2);
            employeeIds2.add(employeeId2);

        }
        pbF2.setVisibility(View.GONE);

        //Se puebla el spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_items, tecnicos2);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spTecnico.setAdapter(spinnerAdapter);
        imageView11.setVisibility(View.VISIBLE);

        spTecnico.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String fullNameSeleccionado2 = tecnicos2.get(position);
                int employeeIdSeleccionado2 = employeeIds2.get(position);
                idtec2 = employeeIdSeleccionado2;
                String nametec2 = fullNameSeleccionado2;


                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("idayud2", idtec2);
                editor.putString("ayudante2", nametec2);
                editor.apply();
                Toast.makeText(getApplicationContext(), "EmployeeIDSeleccionado 2: " + idtec2, Toast.LENGTH_SHORT).show();
                imageView11.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
