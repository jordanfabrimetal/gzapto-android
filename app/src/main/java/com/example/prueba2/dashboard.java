package com.example.prueba2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class dashboard extends AppCompatActivity {

    private TextView textViewAnnio, textViewMes, textViewDia;
    HorizontalBarChart barChart;
    SharedPreferences sharedPreferences;
    ImageView imgShine, imgShine2, imgShine3, ivHamburgesa;
    LinearLayout llAnio, llMes, llDia;
    ProgressBar pb1, pb2, pb3, pbGrafico;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    AppBarLayout appBarLayout;
    View backgroundOverlay;
    boolean muestra = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        textViewAnnio = findViewById(R.id.annio2);
        textViewMes = findViewById(R.id.mes);
        textViewDia = findViewById(R.id.dia);
        imgShine = findViewById(R.id.imgShine);
        imgShine2 = findViewById(R.id.imgShine2);
        imgShine3 = findViewById(R.id.imgShine3);
        llAnio = findViewById(R.id.llAnnio);
        llMes = findViewById(R.id.llMes);
        pbGrafico = findViewById(R.id.pbGrafico);
        llDia = findViewById(R.id.llDia);
        backgroundOverlay = findViewById(R.id.background_overlay);
        pb1 = findViewById(R.id.pb1);
        pb2 = findViewById(R.id.pb2);
        pb3 = findViewById(R.id.pb3);
        ivHamburgesa = findViewById(R.id.ivHamburgesa);
        appBarLayout = findViewById(R.id.appBarLayout);

        // Obtener las cadenas JSON desde las preferencias compartidas
        sharedPreferences = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        String tecnicosJsonString = sharedPreferences.getString("nombre_tecnicos", "");
        String employeeIdsJsonString = sharedPreferences.getString("id_tecnicos", "");

// Convertir las cadenas JSON de nuevo a listas
        List<String> tecnicos1 = new ArrayList<>();
        List<Integer> employeeIds1 = new ArrayList<>();

        try {
            JSONArray tecnicosJsonArray = new JSONArray(tecnicosJsonString);
            JSONArray employeeIdsJsonArray = new JSONArray(employeeIdsJsonString);

            for (int i = 0; i < tecnicosJsonArray.length(); i++) {
                String fullName = tecnicosJsonArray.getString(i);
                int employeeId = employeeIdsJsonArray.getInt(i);
                tecnicos1.add(fullName);
                employeeIds1.add(employeeId);
            }

            Log.d("Nombre de tecnicos: ", String.valueOf(tecnicos1));
            Log.d("Los ID de los tecnicos: ", String.valueOf(employeeIds1));
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
            }

            drawerLayout.closeDrawers();
            return true;
        });

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.bot);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        barChart = (HorizontalBarChart) findViewById(R.id.chart);
        sharedPreferences = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        int idSAP = sharedPreferences.getInt("idSAP",0);
        Log.d("ID SAP SHARED2", String.valueOf(idSAP));
        obtenerDatosDesdeServidor("http://172.16.32.50/gzapto/ajax/estado.php?op=traerdatosandroid");
        ScheduledExecutorService scheduledExecutorService =
                Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shineStart();
                    }
                });
            }
        },5, 2, TimeUnit.SECONDS);
    }

    //Metodo para animaciones
    private void shineStart(){
        Animation animation = new TranslateAnimation(
                0, llAnio.getWidth()+imgShine.getWidth(),0,0
        );
        Animation animation2 = new TranslateAnimation(
                0, llMes.getWidth()+imgShine2.getWidth(),0,0
        );
        Animation animation3 = new TranslateAnimation(
                0, llDia.getWidth()+imgShine3.getWidth(),0,0
        );

        animation.setDuration(1050);
        animation.setFillAfter(false);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        imgShine.startAnimation(animation);

        animation2.setDuration(1050);
        animation2.setFillAfter(false);
        animation2.setInterpolator(new AccelerateDecelerateInterpolator());
        imgShine2.startAnimation(animation2);

        animation3.setDuration(1050);
        animation3.setFillAfter(false);
        animation3.setInterpolator(new AccelerateDecelerateInterpolator());
        imgShine3.startAnimation(animation3);
    }

    //Metodo principal para graficar  obtener datos
    private void obtenerDatosDesdeServidor(String URL){
        final int MY_SOCKET_TIMEOUT_MS = 60000;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbGrafico.setVisibility(View.GONE);
                    barChart.setVisibility(View.VISIBLE);
                    Log.d("response", response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject datosObject = jsonObject.getJSONObject("datos");

                    int anio = datosObject.getInt("anio");
                    int mes = datosObject.getInt("mes");
                    int dia = datosObject.getInt("dia");
                    pb1.setVisibility(View.GONE);
                    pb2.setVisibility(View.GONE);
                    pb3.setVisibility(View.GONE);
                    llAnio.setVisibility(View.VISIBLE);
                    llMes.setVisibility(View.VISIBLE);
                    llDia.setVisibility(View.VISIBLE);
                    textViewAnnio.setText(String.valueOf(anio));
                    textViewMes.setText(String.valueOf(mes));
                    textViewDia.setText(String.valueOf(dia));

                    JSONObject jsonGrafico = jsonObject.getJSONObject("grafico");
                    int totalServicios = jsonGrafico.getInt("Totalservicios");
                    Log.d("jsongrafico: ", String.valueOf(jsonGrafico));
                    JSONArray aaData = jsonGrafico.getJSONArray("aaData");
                    Log.d("aaDATA: ", String.valueOf(aaData));

                    //Comienza del Grafico.
                    //Variable que almacena las entradas de datos de la grafica de barras. Las entradas representan una barra en el grafico
                    ArrayList<BarEntry> entradas = new ArrayList<>();

                    //Arreglo de meses
                    final String[] mesesDelAnio = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

                    //Variable que almacena las etiquetas que se mostraran en el eje X, los meses del año
                    ArrayList<String> etiquetas = new ArrayList<>();

                    //Variable que se usa para almacenar la cantidad de servicios correspondientes a cada mes. Se inicia en 0 para todos los meses
                    ArrayList<Integer> valoresServicios = new ArrayList<>();

                    //Se recorre los meses del arreglo mesesDelAnio y se inicializan ambos arreglos
                    for (int i = mesesDelAnio.length -1; i >= 0; i--) {
                        etiquetas.add(mesesDelAnio[i]);
                        valoresServicios.add(0);
                    }

                    //Se itera sobre aaData, estructura que contiene la info sobre la cantidad de servicios por mes
                    for (int i = 0; i<aaData.length(); i++){
                        JSONObject dataPoint = aaData.getJSONObject(i);
                        //Se extraen los numeros del mes
                        int numeroMes = Integer.parseInt(dataPoint.getString("x"));

                        //Se extrae la cantidad de servicios del mes
                        int cantidadServicios = dataPoint.getInt("y");

                        //Se añaden ambas al arreglo valoresServicios
                        valoresServicios.set(numeroMes - 1, cantidadServicios);
                    }

                    //Se crean barEntry a partir del arreglo valoresServicios. Los barEntry tienen el indice del mes en ele x, y la cantidad de servicios en el eje y
                    for (int i = mesesDelAnio.length - 1; i >= 0; i--) {
                        entradas.add(new BarEntry(mesesDelAnio.length -1 -i, valoresServicios.get(i)));
                    }

                    //Se configura el conjunto de datos con un BarDataSet. Este agrupa las barras y aplica ciertas configuraciones
                    BarDataSet dataSet = new BarDataSet(entradas, "Servicios por mes");

                    //Color verde para todas las barras
                    int colorVerde = Color.parseColor("#009929");
                    dataSet.setColors(colorVerde);

                    //Creación y configuración del grafico en barData con el conjunto de datos de dataSet
                    BarData barData = new BarData(dataSet);

                    barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                        @Override
                        public void onValueSelected(Entry e, Highlight h) {

                                BarEntry barEntry = (BarEntry) e;
                                int index = Math.round(barEntry.getX());
                                float cantidadServicios = barEntry.getY();
                                String mes = etiquetas.get(index);
                                String mensaje = (int) cantidadServicios + " servicio(s) en "+ mes;
                                Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();


                        }

                        @Override
                        public void onNothingSelected() {

                        }
                    });

                    //Se asigna el grafico creado al grafico que existe en la activity
                    barChart.setData(barData);

                    //Ajusta automaticamente el ancho de las abrras para que se ajusten al grafico
                    barChart.setFitBars(true);

                    //Se llama para que el grafico se actualice y muestre nuevos datos
                    barChart.invalidate();

                    //Habilitacion de interacciones en el grafico, incluido el zoom
                    barChart.setTouchEnabled(true);
                    barChart.setPinchZoom(true);

                    //Habilutar scroll horizontal y vertical despues del zoom
                    barChart.setDragEnabled(true);
                    barChart.setScaleEnabled(true);
                    barChart.setDrawGridBackground(false);

                    //Animacion de la barra
                    barChart.animateY(1000);

                    //Agrega un pequeño espacio en la parte inferios
                    //barChart.setExtraBottomOffset(5);

                    //Configuracionde ejes
                    //Se obtiene el objeto xAxis que representa el eje X
                    XAxis xAxis = barChart.getXAxis();

                    //Se formatea los valores de X usando las etiquetas de los meses
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(etiquetas));

                    //Se coloca el eje X en la parte superior del grafico, se configura el tamaño del texto, la granularidaad y las lineas cuadricuas
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setTextSize(10);
                    xAxis.setLabelCount(etiquetas.size());
                    xAxis.setGranularity(1);
                    xAxis.setGranularityEnabled(true);
                    xAxis.setDrawGridLines(false);

                    //Se obtiene eje Y del grafico
                    YAxis yAxis = barChart.getAxisLeft();

                    //Eliminan cuadracular
                    yAxis.setDrawGridLines(false);

                    //Se formatean digitos a enteros
                    yAxis.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            return String.valueOf((int) value);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Respuesta no valida del servidor", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
           @Override
           protected Map<String, String> getParams(){
               Map<String, String> params = new HashMap<>();
               int idSAP = sharedPreferences.getInt("idSAP",0);
               params.put("idSAP_form", String.valueOf(idSAP));
               return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
}