package com.example.prueba2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class inicioServicio extends AppCompatActivity {
    //Inicialización de objetos, elementos y variables globales
    TextView tvIDServicio, tvCodiCli, tvCodTip, tvMarcaEq, tvModEqu;
    private Spinner spEstadoF, spCantAyud, spAyu1, spAyu2, spOpayu;
    TextView tvCargoTec, tvNombresTec, tvApellidosTec, tvRutTec, tvMax;
    GridView gvImagenes;
    TextView tvNombreEd, tvDireccionEd, tvCantAyu, tvAyu1, tvAyu2;
    TextView tvServiceID, tvTipoServ, tvFechaIni, tvHorIni, tvObservacionIni;
    Button button6, btnGaleria;
    List<Uri> listaImagenes = new ArrayList<>();
    List<String> listaBase64Imagenes = new ArrayList<>();
    GridViewAdapter baseAdapter;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switch1;
    private ProgressBar pbu, pbFin, pbF2;
    private DrawerLayout drawerLayout;

    private AppBarLayout appBarLayout;

    private boolean muestra = true;
    private LinearLayout ll5, ll1, ll3, ll4, ll6, ll7, ll2;
    private TextView tv2, tv4, tv5, tv3;
    ImageView img1, img2, img3, img4, ivHamburgesa, imageView15, imageView16, imageView11, imageView14, imageButton7, imageView17;

    NavigationView navigationView;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    private ConstraintLayout clPop, clPop2, clPop3, clPop4, clPop5;
    private SharedPreferences sharedPreferences;
    private View background_overlay2;
    private EditText etObservacionFin, etSolPresup;
    int PICK_IMAGE = 100;
    Uri imagenUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_servicio);
        //Vinculación de variables con elementos de la activity
        ll4 = findViewById(R.id.ll4);
        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);
        ll5 = findViewById(R.id.ll5);
        spAyu2 = findViewById(R.id.spAyu2);
        etSolPresup = findViewById(R.id.etSolPresup);
        gvImagenes = findViewById(R.id.gvImagenes);
        btnGaleria = findViewById(R.id.btnGaleria);
        ll6 = findViewById(R.id.ll6);
        ll7 = findViewById(R.id.ll7);
        tvMax = findViewById(R.id.tvMax);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tvAyu1 = findViewById(R.id.tvAyu1);
        tvAyu2 = findViewById(R.id.tvAyu2);
        spAyu1 = findViewById(R.id.spAyu1);
        pbF2 = findViewById(R.id.pbF2);
        imageView15 = findViewById(R.id.imageView15);
        imageView16 = findViewById(R.id.imageView16);
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
        drawerLayout = findViewById(R.id.drawer_layout);
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
        button6 = findViewById(R.id.btnGaleria);
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
            }
            drawerLayout.closeDrawers();
            return true;
        });
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.bot);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Se traen variables de sesión
        String servicioini = sharedPreferences.getString("servicioini", "");
        String codigoEquipo = sharedPreferences.getString("codigoEquipo", "");
        String tipoEquipo = sharedPreferences.getString("tipoEquipo", "");
        String marcaEquipo = sharedPreferences.getString("marcaEquipo", "");
        String modeloEquipo = sharedPreferences.getString("modeloEquipo","");
        String cargoTecnico = sharedPreferences.getString("cargoTecnico","");
        String nombresTecnico = sharedPreferences.getString("nombresTecnico", "");
        String apellidoTecnico = sharedPreferences.getString("apellidoTecnico", "");
        String rutTecnico = sharedPreferences.getString("rutTecnico", "");
        String nombreEdificio = sharedPreferences.getString("nombreEdificio","");
        String direccionEdificio = sharedPreferences.getString("direccionEdificio","");
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

        //Elementos a mostrar según estado del switch
        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
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
            }else{

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

        //Onclick sobre el boton par continuar
        imageButton7.setOnClickListener(view -> trasitionViews(clPop4, clPop3));


        //Onclick para el boton carga de imagenes
        btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirGaleria();
            }
        });


    }

    //Metodo para terminar el servicio, incluye validación de si se puede cerrar la guia y traer los estados disponibles.
    public void terminarServicio(View v) {
        clPop.setVisibility(View.VISIBLE);

        //servicioini(idservicio), codigoEquipo(idacensor), servillo(tipoencuesta), actCodigo(idactividad), swBtnFin
        String servicioini = sharedPreferences.getString("servicioini", "");
        String codigoEquipo = sharedPreferences.getString("codigoEquipo", "");
        String servillo = sharedPreferences.getString("servillo", "");
        String actCodigo = sharedPreferences.getString("actCodigo", "");
        String swBtnFin = sharedPreferences.getString("swBtnFin", "");
        Log.d("COMPROBACION COMPRAVACION", servicioini + "CodEq: " + codigoEquipo + "servillo: " + servillo + " actCod: " + actCodigo + " swBtnFin: " + swBtnFin);


        String url = "http://172.16.32.50/fabrimetal/gzapto/ajax/servicio.php?op=guiaPorCerrar";
        String url2 = "http://172.16.32.50/fabrimetal/gzapto/ajax/servicio.php?op=selecttestadossapandroid";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("RESPUESTA GUIAPORCERRAR", response);
                    try {

                        JSONObject jsonResponse = new JSONObject(response);
                        int cerrarguia = jsonResponse.getInt("cerrarguia");
                        if (cerrarguia == 1) {
                            Toast.makeText(getApplicationContext(), "Exito, se puede cerrar esta guia", Toast.LENGTH_SHORT).show();

                            StringRequest request2 = new StringRequest(Request.Method.POST, url2,
                                    response1 -> {
                                        Log.d("El resultado del selectestados", response1);
                                        try {
                                            imageView14.setVisibility(View.VISIBLE);
                                            spEstadoF.setVisibility(View.VISIBLE);
                                            pbu.setVisibility(View.GONE);
                                            JSONArray jsonArray = new JSONArray(response1);
                                            List<String> estados = new ArrayList<>();
                                            //Se asigna al listado las opciones que vienen de la respuesta basada en su nombre
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject estadoJson = jsonArray.getJSONObject(i);
                                                String name = estadoJson.getString("name");
                                                estados.add(name);
                                            }

                                            //Se puebla el spinner o combobox
                                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(inicioServicio.this, R.layout.spinner_items, estados);
                                            spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                                            spEstadoF.setAdapter(spinnerAdapter);
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
    public void infogias(){
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
        String url3 = "http://172.16.32.50/fabrimetal/gzapto/ajax/servicio.php?op=infoguiasap";
        StringRequest request3 = new StringRequest(Request.Method.POST, url3,
                response -> {
                    //Metodos que se iniciar con la respuesta valida
                    setupAyudantes();
                    setupCantAyudantes();
                    pbFin.setVisibility(View.GONE);
                    spOpayu.setVisibility(View.VISIBLE);
                    Log.d("RESPUESTA DE INFOGUIASAP", response);
                    //EditText etCodF, etTipAs, etMarF, etModF, etNomEdF, etDirF, etFecF, etHorF, etTipSerF, etEstadoF, etObsF;
                    //etCodF = findViewById(R.id.etCodF);
                    //etTipAs = findViewById(R.id.etTipAs);
                    //etMarF = findViewById(R.id.etMarF);
                    //etModF = findViewById(R.id.etModF);
                    //etNomEdF = findViewById(R.id.etNomEdF);
                    //etDirF = findViewById(R.id.etDirF);
                    //etFecF = findViewById(R.id.etFecF);
                    //etHorF = findViewById(R.id.etHorF);
                    //etTipSerF = findViewById(R.id.etTipSerF);
                    //etEstadoF = findViewById(R.id.etEstadoF);
                    //etObsF = findViewById(R.id.etObsF);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String codigoEquipo = jsonObject.getString("codigo");
                        Toast.makeText(getApplicationContext(), codigoEquipo,Toast.LENGTH_SHORT).show();
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
                if (seleccion.equals("Si")) {

                    Toast.makeText(getApplicationContext(), "OJO", Toast.LENGTH_SHORT).show();
                    tvCantAyu.setVisibility(View.VISIBLE);
                    spCantAyud.setVisibility(View.VISIBLE);
                    imageView11.setVisibility(View.GONE);

                } else if(seleccion.equals("No")) {
                    spCantAyud.setVisibility(View.GONE);
                    tvCantAyu.setVisibility(View.GONE);
                    tvAyu1.setVisibility(View.GONE);
                    tvAyu2.setVisibility(View.GONE);
                    spAyu1.setVisibility(View.GONE);
                    spAyu2.setVisibility(View.GONE);
                    imageView11.setVisibility(View.VISIBLE);

                }else{
                    imageView11.setVisibility(View.GONE);

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
                editor.apply();
                if (seleccion.equals("Si")) {
                    Toast.makeText(getApplicationContext(), "OJO", Toast.LENGTH_SHORT).show();
                    button6.setVisibility(View.VISIBLE);
                    gvImagenes.setVisibility(View.VISIBLE);
                    imageView17.setVisibility(View.VISIBLE);
                    tvMax.setVisibility(View.VISIBLE);

                } else if(seleccion.equals("No")) {
                    button6.setVisibility(View.GONE);
                    gvImagenes.setVisibility(View.GONE);
                    imageView17.setVisibility(View.GONE);
                    tvMax.setVisibility(View.GONE);
                    imageView17.setVisibility(View.VISIBLE);
                }else{
                    button6.setVisibility(View.GONE);
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

    public void moveFoward(View v){
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.END);
        slide.setDuration(500);
        TransitionManager.beginDelayedTransition(clPop, slide);
        clPop.setVisibility(View.GONE);

        new Handler().postDelayed(() -> {
            clPop2.setVisibility(View.VISIBLE);
            setupPresupuesto();
            new Handler().postDelayed(() -> {

            }, 3000);
        }, 500);
    }

    public void moveFoward4(View v){
        subirImagenes();
        String seleccionRecuperada = sharedPreferences.getString("seleccion", "");
        if(seleccionRecuperada.equals("Si")){
            Toast.makeText(getApplicationContext(), "YEA, WA", Toast.LENGTH_SHORT).show();
            int cantidadImagenes = sharedPreferences.getInt("cantidadImagenes", 0);

            if(cantidadImagenes >= 1 && cantidadImagenes <=3){
                Toast.makeText(getApplicationContext(), "Fotos correctamente subidas", Toast.LENGTH_SHORT).show();
                trasitionViews2(clPop4, clPop5);
            }else {
                Toast.makeText(getApplicationContext(), "de 1 a 3 no mas", Toast.LENGTH_SHORT).show();
            }
            Log.d("IMAGENES SUBIDAS", String.valueOf(cantidadImagenes));
        }
    }

    public void moveFoward3(View v){
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.END);
        slide.setDuration(500);
        TransitionManager.beginDelayedTransition(clPop3, slide);
        clPop3.setVisibility(View.GONE);

        new Handler().postDelayed(() -> {
            clPop4.setVisibility(View.VISIBLE);
            new Handler().postDelayed(() -> {

            }, 3000);
        }, 500);
    }

    //Metodo para moverse hacia la derecha con animacion. Se muestra el textarea de la observación final
    @SuppressLint("SetTextI18n")
    public void movefoward2(View v){

        String subject = etObservacionFin.getText().toString();
        if (subject.equals("")) {
            //Mensaje Toast de Error
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("InflateParams") View customToastView = inflater.inflate(R.layout.custom_toast, null);
            TextView textView = customToastView.findViewById(R.id.custom_toast_text);
            textView.setText("Debes ingresar una observación para continuar.");
            Toast customToast = new Toast(getApplicationContext());
            customToast.setDuration(Toast.LENGTH_SHORT);
            customToast.setView(customToastView);
            customToast.show();
            int colorErrorHint = Color.RED;
            etObservacionFin.setHintTextColor(colorErrorHint);
        }else{
            //Se llama al metodo infogias
            infogias();
        }
    }

    public void movefoward3(View v){

        String subject = etSolPresup.getText().toString();
        if (subject.equals("")) {
            //Mensaje Toast de Error
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("InflateParams") View customToastView = inflater.inflate(R.layout.custom_toast, null);
            TextView textView = customToastView.findViewById(R.id.custom_toast_text);
            textView.setText("Debes ingresar una solicitud para el prespuesto.");
            Toast customToast = new Toast(getApplicationContext());
            customToast.setDuration(Toast.LENGTH_SHORT);
            customToast.setView(customToastView);
            customToast.show();
            int colorErrorHint = Color.RED;
            etSolPresup.setHintTextColor(colorErrorHint);
        }else{

        }
    }

    //Metodo para retroceder un Layout con animación hacia la izquierda
    public void backBck(View v){
       // Slide slide = new Slide();
       // slide.setSlideEdge(Gravity.START);
       // slide.setDuration(500);
       // TransitionManager.beginDelayedTransition(clPop2, slide);
        //clPop2.setVisibility(View.GONE);
        //clPop.setVisibility(View.VISIBLE);
        //p77.setVisibility(View.GONE);
        //spinnerCodigo.setVisibility(View.VISIBLE);
        trasitionViews(clPop2, clPop);


    }
    public void backBck4(View v){
        trasitionViews(clPop5, clPop4);
    }
    public void trasitionViews(View hideView, View showView){
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.START);
        slide.setDuration(500);
        TransitionManager.beginDelayedTransition((ViewGroup) hideView.getParent(), slide);
        hideView.setVisibility(View.GONE);
        showView.setVisibility(View.VISIBLE);
    }
    public void trasitionViews2(View hideView, View showView){
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.END);
        slide.setDuration(500);
        TransitionManager.beginDelayedTransition((ViewGroup) hideView.getParent(), slide);
        hideView.setVisibility(View.GONE);
        showView.setVisibility(View.VISIBLE);
    }

    public void rightTrasitionViews(View hideView, View showView){
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.END);
        slide.setDuration(500);
        TransitionManager.beginDelayedTransition((ViewGroup) hideView.getParent(), slide);
        hideView.setVisibility(View.GONE);
        showView.setVisibility(View.VISIBLE);
    }

    //Mismo que el anterior
    public void backBck2(View v){
//        Slide slide = new Slide();
 //       slide.setSlideEdge(Gravity.START);
  //      slide.setDuration(500);
   //     TransitionManager.beginDelayedTransition(clPop3, slide);
       // clPop3.setVisibility(View.GONE);
       // clPop2.setVisibility(View.VISIBLE);
        //p77.setVisibility(View.GONE);
        //spinnerCodigo.setVisibility(View.VISIBLE);

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
                if (seleccion.equals("1")) {
                    pbF2.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Marcaste 1", Toast.LENGTH_SHORT).show();
                    tvAyu1.setVisibility(View.VISIBLE);
                    spAyu1.setVisibility(View.VISIBLE);
                    //Se llama al metodo selectTecnico para el spinner spAyu1
                    selectTecnico(spAyu1);
                    imageView11.setVisibility(View.GONE);
                } else if (seleccion.equals("2")) {
                    Toast.makeText(getApplicationContext(), "Marcaste 2", Toast.LENGTH_SHORT).show();
                    pbF2.setVisibility(View.VISIBLE);
                    tvAyu1.setVisibility(View.VISIBLE);
                    spAyu1.setVisibility(View.VISIBLE);
                    tvAyu2.setVisibility(View.VISIBLE);
                    spAyu2.setVisibility(View.VISIBLE);
                    //Se llama al metodo selectTecnico para el spinner spAyu1 y spAyu2
                    selectTecnico(spAyu1);
                    selectTecnico(spAyu2);
                    imageView11.setVisibility(View.GONE);
                } else {
                   tvAyu1.setVisibility(View.GONE);
                   spAyu1.setVisibility(View.GONE);
                   tvAyu2.setVisibility(View.GONE);
                   spAyu2.setVisibility(View.GONE);
                    imageView11.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //El metodo que trae a todos los tecnicos
    public void selectTecnico(Spinner spTecnico) {
        String url = "http://172.16.32.50/fabrimetal/gzapto/ajax/servicio.php?op=selectTecnicoandroid";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("Response de TECNICOS", response);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        List<String> tecnicos = new ArrayList<>();

                        //Se recorre la respuesta y se asigna sus valores al listado
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject tecicosJson = jsonArray.getJSONObject(i);
                            String fullName = tecicosJson.getString("FullName");
                            tecnicos.add(fullName);
                        }
                        pbF2.setVisibility(View.GONE);

                        //Se puebla el spinner
                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_items, tecnicos);
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

    //Metodo para el menú desplegable
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

    //Metodo para el menú desplegable
    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    //Metodo para el menú desplegable
    public void mostrarMenu(View v){

        if(muestra){
            appBarLayout.setVisibility(View.VISIBLE);
            muestra = false;
            background_overlay2.setVisibility(View.VISIBLE);
        }else{
            appBarLayout.setVisibility(View.GONE);
            muestra = true;
            background_overlay2.setVisibility(View.GONE);
        }
    }

    //Metodo para salir de los Poups
    public void salirPopUp(View v){
       clPop.setVisibility(View.GONE);
        clPop2.setVisibility(View.GONE);
        clPop3.setVisibility(View.GONE);
        clPop4.setVisibility(View.GONE);
        clPop5.setVisibility(View.GONE);
    }

    public void subirImagenes(){
        listaBase64Imagenes.clear();
        Context context = getApplicationContext();

        if(listaImagenes.size() >= 1 && listaImagenes.size() <= 3){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("cantidadImagenes", listaImagenes.size());
            editor.apply();


            //Mas de una imagen
            if(context != null){
                for(int i=0; i<listaImagenes.size(); i++){
                    try{
                        InputStream is = getContentResolver().openInputStream(listaImagenes.get(i));
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        String cadena = convertirUriToBase64(bitmap);
                        String nombreImagen = "imagen_" + System.currentTimeMillis()+"_"+i;
                        enviarImagen(nombreImagen, cadena);
                        listaBase64Imagenes.add(cadena);
                        bitmap.recycle();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }else{
                Toast.makeText(getApplicationContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(getApplicationContext(), "Se debe seleccionar entre 1 y 3 imagenes", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("cantidadImagenes", 0);
            editor.apply();
        }

    }

    public void subirImagenes2(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://172.16.32.50/fabrimetal/gzapto/ajax/uploadpres.php?op=interno";

    }

    public void enviarImagenes(final List<String> imagenes) {
        String url = "http://172.16.32.50/proyectoDieta/insertarAlimento.php?op=ingresarImagen";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar errores de respuesta aquí
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                // Aquí, convierte la lista de imágenes en una cadena separada por comas o algún otro formato que el servidor pueda manejar.
                // Luego, envía esta cadena como un solo parámetro al servidor.

                StringBuilder sb = new StringBuilder();
                for (String imagen : imagenes) {
                    sb.append(imagen).append(",");
                }

                // Elimina la última coma
                if (sb.length() > 0) {
                    sb.setLength(sb.length() - 1);
                }

                params.put("nombreImagen", "nomIma"); // Cambia esto según tus necesidades
                params.put("imagenes", sb.toString());

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    public String convertirUriToBase64(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        String encode = Base64.encodeToString(bytes, Base64.DEFAULT);

        return encode;
    }

    public void enviarImagen(final String nombre, final String cadena){
        String url = "http://172.16.32.50/proyectoDieta/insertarAlimento.php?op=ingresarImagen";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new Hashtable<>();
                params.put("nombreImagen", nombre);
                params.put("imagenes", cadena);

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    public void abrirGaleria(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona las imagenes"), PICK_IMAGE);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ClipData clipData = data.getClipData();
        if(resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE){
            //PARA CUANDO SOLO SE SELECCIONA UNA IMAGEN
            if(clipData == null){
                imagenUri = data.getData();
                listaImagenes.add(imagenUri);
            }else{
                //PARA CUANDO SE SELECCIONAN VARIAS IMAGENES
                for(int i =0; i< clipData.getItemCount(); i++){
                    listaImagenes.add(clipData.getItemAt(i).getUri());
                }
            }

            baseAdapter = new GridViewAdapter(getApplicationContext(), listaImagenes);
            gvImagenes.setAdapter(baseAdapter);
        }
    }

}
