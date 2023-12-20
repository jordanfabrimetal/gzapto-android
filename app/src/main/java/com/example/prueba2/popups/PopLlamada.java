package com.example.prueba2.popups;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.transition.TransitionManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prueba2.R;
import com.example.prueba2.dashboard;
import com.example.prueba2.guiaServicio;
import com.example.prueba2.menu;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopLlamada extends AppCompatActivity {

    //Definición de objetos y variables
    private String response;
    private Spinner tipollamada, spinnerCodigo;

    private EditText etCodCli, etObservacionIni;
    private ProgressBar progressBar2, p77;
    private ConstraintLayout clLlamada, clCodigoo, clAnimada, clBuscar;
    private TextView tvCliOb;
    private ImageButton imageButton4, imageButton3, imageButton5;
    ImageView ivHamburgesa, imageButton, imageButton99, imageButtonGg;
    private final List<JSONObject> opcionesJson = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    AppBarLayout appBarLayout;
    View backgroundOverlay;
    boolean muestra = true;


    //Metodo init principal
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popllamada);

        //Asignación de objetos de la activity con la definición de variables
        tipollamada = findViewById(R.id.tipollamada);
        progressBar2 = findViewById(R.id.progressBar2);
        p77 = findViewById(R.id.p77);
        etObservacionIni = findViewById(R.id.etObservacionIni);
        etCodCli = findViewById(R.id.etCodCli);
        tvCliOb = findViewById(R.id.tvCliOb);
        backgroundOverlay = findViewById(R.id.background_overlay4);
        ivHamburgesa = findViewById(R.id.ivHamburgesa4);
        appBarLayout = findViewById(R.id.appBarLayout);
        imageButton = findViewById(R.id.imageButton);
        imageButton99 = findViewById(R.id.imageButton99);
        imageButtonGg = findViewById(R.id.imageButtonGg);
        drawerLayout = findViewById(R.id.llPop7);
        navigationView = findViewById(R.id.nav_view);
        imageButton4 = findViewById(R.id.imageButton4);
        imageButton3 = findViewById(R.id.imageButton3);
        imageButton5 = findViewById(R.id.imageButton5);
        clLlamada = findViewById(R.id.clLlamada);
        clCodigoo = findViewById(R.id.clCodigoo);
        etCodCli = findViewById(R.id.etCodCli);
        clBuscar = findViewById(R.id.clBuscar);
        clAnimada = findViewById(R.id.clAnimation);
        spinnerCodigo = findViewById(R.id.codigo);
        sharedPreferences = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        //Configuración de menú desplegable
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        //Navegación del menú desplegable
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

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();

        if (network != null) {
            //Primeras funciones en iniciarse
            obtenerServicios();
            setupListeners();
        } else {
            mostrarOpcionesLlmadaSinInternet();
        }

    }

    //Metodo para obtener servicios(tipos de llamada)
    private void obtenerServicios() {
        String url = "http://172.16.32.50/gzapto/ajax/ascensor.php?op=selecttipollamadaandroid";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d("EL RESPONSE DE SERVICIOS: ", String.valueOf(response));
                    llenarSpinnerLlamadas(response);
                    progressBar2.setVisibility(View.GONE);
                    imageButton.setVisibility(View.VISIBLE);
                    tipollamada.setVisibility(View.VISIBLE);
                },
                error -> Toast.makeText(getApplicationContext(), "Error en la solicitud", Toast.LENGTH_SHORT).show()
        );
        queue.add(stringRequest);
    }

    //Metodo para llenar Spinner o combobox con tipo de llamadas
    private void llenarSpinnerLlamadas(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            List<String> opciones = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //ID para mostrar el segundo combobox
                int callTypeId = jsonObject.getInt("CallTypeID");
                String name = jsonObject.getString("Name");

                //Mostrar solo las llamadas validas, normalizacion y visita
                if (esValido(callTypeId)) {
                    opciones.add(name);
                }
            }
            //Llenado del spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    R.layout.spinner_items, opciones);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            Spinner spinner = findViewById(R.id.tipollamada);
            spinner.setAdapter(adapter);

            this.response = response;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Metodo para generar llamadas validas(Normalizacion 5 y visita 17)
    private boolean esValido(int callTypeID) {
        int[] listadoValido = {5, 17};
        for (int id : listadoValido) {
            if (callTypeID == id) {
                return true;
            }
        }
        return false;
    }

    //Metodo para obtener la llamada seleccionada por el nombre de la misma.
    private int obtenLlamadaPorNombre(String name) {
        try {
            JSONArray jsonArray = new JSONArray(this.response);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String optionName = jsonObject.getString("Name");
                if (optionName.equals(name)) {
                    return jsonObject.getInt("CallTypeID");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    //Metodo para llenar Spinner o combobox del codigo de servicio.
    private void llenarSpinnerCodigo(String responsse) {
        Log.d("RESPONSE FROM SERVER: ", responsse);
        try {
            JSONArray jsonArray = new JSONArray(responsse);
            List<String> opciones = new ArrayList<>();
            imageButtonGg.setVisibility(View.VISIBLE);
            imageButton99.setVisibility(View.VISIBLE);

            opcionesJson.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String text = jsonObject.getString("text");
                //Se añaden el campo text que llega al listado.
                opciones.add(text);
                opcionesJson.add(jsonObject);
            }
            //Se llena el spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    R.layout.spinner_items, opciones);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

            spinnerCodigo.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Metodo para obtener los Codigos de servicios.
    private void obtenerCodigos(int callTypeID) {
        String url = "http://172.16.32.50/gzapto/ajax/ascensor.php?op=selectascfiltroandroid";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                //Se llena el spinner con la respuesta del servidor
                response -> llenarSpinnerCodigo(response),
                error -> Toast.makeText(getApplicationContext(), "Error en la solicitud", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //Parametros que se pasan al servidor como POST.
                int idrole = sharedPreferences.getInt("idrole", 0);
                int idSAP = sharedPreferences.getInt("idSAP", 0);
                params.put("idservicio", String.valueOf(callTypeID));
                params.put("idrole", String.valueOf(idrole));
                params.put("idSAP_form", String.valueOf(idSAP));
                return params;
            }
        };
        queue.add(stringRequest);
    }

    //Metodo para identificar cambios en los spinner.
    private void setupListeners() {

        //Listener al spinner de tipo de llamadas
        tipollamada.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedOption = adapterView.getItemAtPosition(i).toString();
                int selectedCallTypeID = obtenLlamadaPorNombre(selectedOption);
                if (esValido(selectedCallTypeID)) {
                    obtenerCodigos(selectedCallTypeID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(), "No se selecciono ningun servicio", Toast.LENGTH_SHORT).show();
            }
        });

        //Listener al spinner de los codigos
        spinnerCodigo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                JSONObject selectedJson = opcionesJson.get(i);
                try {
                    String selectedValue = selectedJson.getString("value");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("codigoSel", selectedValue);
                    editor.apply();
                    Toast.makeText(getApplicationContext(), "CCODIGO SELECCIONADO: "+selectedValue, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //Metodo para cambiar de Layout con animación ida hacia la derecha
    public void slideRight(View v) {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.END);
        slide.setDuration(500);
        TransitionManager.beginDelayedTransition(clLlamada, slide);
        clLlamada.setVisibility(View.GONE);
        new Handler().postDelayed(() -> clCodigoo.setVisibility(View.VISIBLE), 800);
    }

    //Metodo para cambiar de layout a uno anterior con animación ida hacia la izquierda
    public void reverse(View v) {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.START);
        slide.setDuration(500);
        TransitionManager.beginDelayedTransition(clLlamada, slide);
        clCodigoo.setVisibility(View.GONE);
        clLlamada.setVisibility(View.VISIBLE);

    }

    //Metodo igual al anterior, pero con ciertos cambios
    public void reverse2(View v) {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.START);
        slide.setDuration(500);
        TransitionManager.beginDelayedTransition(clBuscar, slide);
        clBuscar.setVisibility(View.GONE);
        clCodigoo.setVisibility(View.VISIBLE);
        p77.setVisibility(View.GONE);
        spinnerCodigo.setVisibility(View.VISIBLE);


    }

    public void decideEquipo(View v){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        if (network != null) {
            buscarEquipo();
        }else{
            buscarSinInternet();
        }
    }

    //Metodo para busar servicio de normalización
    public void buscarEquipo() {
            imageButtonGg.setVisibility(View.GONE);
            imageButton99.setVisibility(View.GONE);
            Spinner spinnerTipollamado = findViewById(R.id.tipollamada);
            p77.setVisibility(View.VISIBLE);
            spinnerCodigo.setVisibility(View.GONE);
            String selectedOptionName = spinnerTipollamado.getSelectedItem().toString();
            int selectedCallTypeID = obtenLlamadaPorNombre(selectedOptionName);
            Log.d("CallTypeID", String.valueOf(selectedCallTypeID));
            String url;
            if (selectedCallTypeID == 5) {
                url = "http://172.16.32.50/gzapto/ajax/ascensor.php?op=llamadaservicionormalizacion";
                Toast.makeText(getApplicationContext(), "Llamado de NORMALIZACION", Toast.LENGTH_SHORT).show();
            } else {
                url = "http://172.16.32.50/gzapto/ajax/ascensor.php?op=llamadaserviciovisita";
                Toast.makeText(getApplicationContext(), "Llamado de VISITA", Toast.LENGTH_SHORT).show();
            }

            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        Log.d("onResponse", response);
                        Slide slide = new Slide();
                        slide.setSlideEdge(Gravity.END);
                        slide.setDuration(500);
                        TransitionManager.beginDelayedTransition(clCodigoo, slide);
                        clCodigoo.setVisibility(View.GONE);

                        new Handler().postDelayed(() -> clBuscar.setVisibility(View.VISIBLE), 800);

                        procesarRespuestaEquipo(response);
                    },
                    error -> Toast.makeText(getApplicationContext(), "Error en la solicitud", Toast.LENGTH_SHORT).show()
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    //Datos que se envian como POST al servidor.
                    String codigoSel = sharedPreferences.getString("codigoSel", "");
                    params.put("servicecall", codigoSel);
                    return params;
                }
            };
            queue.add(stringRequest);


    }

    public void buscarSinInternet(){

        FrameLayout frameLayout = findViewById(R.id.frameLayout);
        frameLayout.setVisibility(View.GONE);
        TextView tvCodeNet = findViewById(R.id.tvCodeNet);
        TextView tvSinInfo = findViewById(R.id.tvSinInfo);
        tvCodeNet.setVisibility(View.VISIBLE);
        tvSinInfo.setVisibility(View.VISIBLE);

        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.END);
        slide.setDuration(500);
        TransitionManager.beginDelayedTransition(clCodigoo, slide);
        clCodigoo.setVisibility(View.GONE);

        new Handler().postDelayed(() -> clBuscar.setVisibility(View.VISIBLE), 800);
        String codeFm = sharedPreferences.getString("codigoSel", "");

        tvCodeNet.setText("SERVICIO SELECCIONADO: "+codeFm);
    }



    //Metodo para traer los datos del equipo o servicio y manipularlos
    private void procesarRespuestaEquipo(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            Log.d("finalresponse", String.valueOf(jsonObject));
            String customerCode = jsonObject.getString("CustomerCode");
            String itemName = jsonObject.getString("ItemName");
            String itemcode = jsonObject.getString("ItemCode");
            String edificio = jsonObject.getString("edificio");
            String direccion = jsonObject.getString("direccion");
            String Manufacturer = jsonObject.getString("Manufacturer");
            String modelo = jsonObject.getString("modelo");
            String nomenclatura = jsonObject.getString("nomenclatura");
            String garantia = jsonObject.getString("garantiaF");
            String tipoServicio = jsonObject.getString("CallType");
            String status = jsonObject.getString("status");
            String servicecallID = jsonObject.getString("ServiceCallID");
            String fm = jsonObject.getString("ItemCode");
            int equipmentcardnum = jsonObject.getInt("equipmentcardnum");
            String estado = jsonObject.getString("status");
            String codclisap = jsonObject.getString("nomenclatura");
            //Asignación automaticaica del gps, se debe mejorar
            String gps = "-33.385232,-70.776407";
            String nomen = jsonObject.getString("nomenclatura");

            //Datos del total que deben ser mostrados al usuario, se incluten en un HashMap
            HashMap<String, String> equipo = new HashMap<>();
            equipo.put("FM asociado", itemcode);
            equipo.put("Tipo de Ascensor", itemName);
            equipo.put("Marca", Manufacturer);
            equipo.put("Modelo", modelo);
            equipo.put("Garantia", garantia);
            equipo.put("Nombre del Edificio", edificio);
            equipo.put("Dirección del Edificio", direccion);
            equipo.put("Tipo de Servicio", tipoServicio);
            equipo.put("Estado del Ascensor", status);

            //Variables de sesión ingresadas
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("servicecallID", servicecallID);
            editor.putString("customerCode", customerCode);
            editor.putString("itemcode", itemcode);
            editor.putString("fm", fm);
            editor.putString("estado", estado);
            editor.putString("gps", gps);
            editor.putString("codclisap", codclisap);
            editor.putInt("equipmentcardnum", equipmentcardnum);
            editor.putString("nomen", nomen);

            //Variables para finalizar el servicio
            editor.putString("servicecallIDfi", servicecallID);
            editor.apply();

            etCodCli.setText(nomen);

            if (nomenclatura.equals("null") || nomenclatura.isEmpty()) {
                etCodCli.setText("");
            } else {
                etCodCli.setText(nomenclatura);
            }

            //Sección para el uso del Carrousel de datos
            ViewPager2 viewPager2 = findViewById(R.id.viewPager);
            CarouselAdapter adapter = new CarouselAdapter(this, equipo);
            viewPager2.setAdapter(adapter);
            int delayMillis = 3000;
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                int currentItem = 0;

                @Override
                public void run() {
                    if (currentItem >= adapter.getItemCount()) {
                        currentItem = 0;
                    }
                    viewPager2.setCurrentItem(currentItem++, true);
                    handler.postDelayed(this, delayMillis);
                }
            };
            handler.postDelayed(runnable, delayMillis);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Metodo de carrousel de datos para el servicio iniciado
    public static class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.ViewHolder> {
        private final Context context;
        private final List<Map.Entry<String, String>> dataList;

        public CarouselAdapter(Context context, HashMap<String, String> data) {
            this.context = context;
            this.dataList = new ArrayList<>(data.entrySet());
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.carousel_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Map.Entry<String, String> entry = dataList.get(position);
            holder.textKey.setText(entry.getKey());
            holder.textValue.setText(entry.getValue());
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView textKey;
            TextView textValue;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textKey = itemView.findViewById(R.id.textKey);
                textValue = itemView.findViewById(R.id.textValue);
            }
        }
    }



    //Primer botón para continuar
    @SuppressLint("SetTextI18n")
    public void iniciarServicio(View v) {
        tvCliOb.setText("Observaciones de inicio de servicio");
        etCodCli.setVisibility(View.GONE);
        etObservacionIni.setVisibility(View.VISIBLE);
        imageButton3.setVisibility(View.GONE);
        imageButton5.setVisibility(View.VISIBLE);

    }

    //Segundo y ultimo botón para continuar e iniciar el servicio
    public void iniciarServicio2(View v) {

        String subject = etObservacionIni.getText().toString();
        if (subject.equals("")) {
            //Mensaje Toast personalizado para mostrar errores
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("InflateParams") View customToastView = inflater.inflate(R.layout.custom_toast, null);
            TextView textView = customToastView.findViewById(R.id.custom_toast_text);
            textView.setText("Debes ingresar una observación para continuar.");
            Toast customToast = new Toast(getApplicationContext());
            customToast.setDuration(Toast.LENGTH_SHORT);
            customToast.setView(customToastView);
            customToast.show();
            int colorErrorHint = Color.RED;
            etObservacionIni.setHintTextColor(colorErrorHint);
        } else {
            AlertDialog dialogo = new AlertDialog.Builder(PopLlamada.this).setPositiveButton("Si, confirmar", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            ConnectivityManager connectivityManager =
                                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            Network network = connectivityManager.getActiveNetwork();

                            if (network != null) {

                                String nomen = etCodCli.getText().toString();
                                String modnomenclatura = "";

                                String servicecallID = sharedPreferences.getString("servicecallID", "");
                                Log.d("ServicallID del iniciar servicio", servicecallID);
                                String customerCode = sharedPreferences.getString("customerCode", "");
                                String itemcode = sharedPreferences.getString("itemcode", "");
                                String fm = sharedPreferences.getString("fm", "");
                                String estado = sharedPreferences.getString("estado", "");
                                String gps = sharedPreferences.getString("gps", "");
                                String codclisap = sharedPreferences.getString("codclisap", "");
                                int idSAP = sharedPreferences.getInt("idSAP", 0);
                                int equipmentcardnum = sharedPreferences.getInt("equipmentcardnum", 0);
                                if (codclisap != nomen) {
                                    Toast.makeText(getApplicationContext(), "campos de cliente no coinciden", Toast.LENGTH_SHORT).show();
                                    modnomenclatura = "&nomen=" + nomen + "&codascen=" + equipmentcardnum;
                                }

                                Log.d("Iniciar sesion prueba 01", servicecallID + ' ' + customerCode + ' ' + itemcode + ' ' + fm + ' ' + estado + ' ' + gps + ' ' + subject + ' ' + nomen);

                                String url = "http://172.16.32.50/gzapto/ajax/servicio.php" +
                                        "?op=iniciarsapnormalizacion" +
                                        "&servicecallID=" + servicecallID +
                                        "&customerCode=" + customerCode +
                                        "&itemcode=" + itemcode +
                                        "&fm=" + fm +
                                        "&estado=" + estado +
                                        modnomenclatura +
                                        "&gps=" + gps +
                                        "&subject=" + subject +
                                        "&idSAP=" + idSAP;

                                Log.d("URL DEL INICIO SERVCIO: ", url);

                                RequestQueue queue = Volley.newRequestQueue(PopLlamada.this);
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Log.d("RESPONSE", response);
                                                Toast.makeText(getApplicationContext(), "Se inicio el servicio correctamente", Toast.LENGTH_SHORT).show();
                                                moveFoward(clBuscar, clAnimada);
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Intent intent = new Intent(getApplicationContext(), menu.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }, 5000);
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(getApplicationContext(), "Error en la solicitud", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                );
                                queue.add(stringRequest);

                            } else {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("netiniobs",subject);
                                editor.apply();
                                Toast.makeText(getApplicationContext(), "Se inicio el servicio correctamente", Toast.LENGTH_SHORT).show();
                                moveFoward(clBuscar, clAnimada);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(getApplicationContext(), menu.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, 5000);
                                editor.putBoolean("bloqueado", true);
                                editor.putInt("servicioxfinalizar", 1);
                                editor.apply();
                            }

                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(PopLlamada.this, "Cancelado", Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                    })
                    .setTitle("¿Estas seguro de iniciar el servicio")
                    .create();
            dialogo.show();
        }
    }


    public void mostrarOpcionesLlmadaSinInternet() {
        progressBar2.setVisibility(View.GONE);
        imageButton.setVisibility(View.VISIBLE);
        tipollamada.setVisibility(View.VISIBLE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //Mapa para los servicios posibles
        Map<String, String> opcionesMapa = new HashMap<>();
        opcionesMapa.put("05", "NORMALIZACION");
        opcionesMapa.put("17", "VISITA");

        //Convertir el mapa en un ArrayList
        List<String> opciones = new ArrayList<>(opcionesMapa.values());

        //Crear ArrayAdapter y configurar el Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_items, opciones);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.tipollamada);
        spinner.setAdapter(adapter);

       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            String selectedOption = (String) parentView.getItemAtPosition(position);

            if(selectedOption.equals("NORMALIZACION")){
                Toast.makeText(getApplicationContext(), "NORMA", Toast.LENGTH_SHORT).show();
                llenarSpinnerCodigoN();
                editor.putInt("valuellamadasel", 5);

            }else{
                Toast.makeText(getApplicationContext(), "Visita", Toast.LENGTH_SHORT).show();
                editor.putInt("valuellamadasel", 17);
            }
            editor.apply();
           }



           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });

        //spinnerCodigo spCodigo = findViewById(R.id.codigo);


    }


    //Metodo para llenar Spinner o combobox del codigo de servicio.
    private void llenarSpinnerCodigoN() {
        String opcionesJsonString = sharedPreferences.getString("all_services_normalizacion","");
        Toast.makeText(getApplicationContext(),"Resultado de all_services: "+opcionesJsonString, Toast.LENGTH_SHORT).show();
        Log.d("all_services:", opcionesJsonString);
        List<String> opciones = new ArrayList<>();
        List<String> valores = new ArrayList<>();
        try {
            JSONArray opcionesJsonArray = new JSONArray(opcionesJsonString);

            for(int i = 0; i<opcionesJsonArray.length(); i++){
                String jsonString = opcionesJsonArray.getString(i);
                JSONObject jsonObject = new JSONObject(jsonString);
                String text = jsonObject.getString("text");
                String value = jsonObject.getString("value");
                opciones.add(text);
                valores.add(value);
                imageButtonGg.setVisibility(View.VISIBLE);
                imageButton99.setVisibility(View.VISIBLE);
            }

            Spinner spinnerCodigo = findViewById(R.id.codigo);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    R.layout.spinner_items, opciones);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

            spinnerCodigo.setAdapter(adapter);

            spinnerCodigo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String selectedValue = valores.get(i);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("codigoSel", selectedValue);
                        editor.apply();
                        Toast.makeText(getApplicationContext(), "CCODIGO SELECCIONADO: "+selectedValue, Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    //Metodo asociada a la "equis" de los Popups
    public void salirPopUp(View v) {
        Intent i = new Intent(getApplicationContext(), guiaServicio.class);
        startActivity(i);
        finish();
    }


    public void moveFoward(ConstraintLayout cl1, ConstraintLayout cl2) {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.END);
        slide.setDuration(500);
        TransitionManager.beginDelayedTransition(cl1, slide);
        cl1.setVisibility(View.GONE);

        new Handler().postDelayed(() -> {
            cl2.setVisibility(View.VISIBLE);
            new Handler().postDelayed(() -> {

            }, 3000);
        }, 500);
    }

    //Metodo del menú desplegable, selección de opciones.
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

    //Metodo del menú desplegable, actionBar incluido.
    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    //Metodo del menú desplegable, mostrar el menu y sus elementos.
    public void mostrarMenu(View v) {

        if (muestra) {
            appBarLayout.setVisibility(View.VISIBLE);
            muestra = false;
            backgroundOverlay.setVisibility(View.VISIBLE);
        } else {
            appBarLayout.setVisibility(View.GONE);
            muestra = true;
            backgroundOverlay.setVisibility(View.GONE);
        }
    }


}



