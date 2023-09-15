package com.example.prueba2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prueba2.popups.PopLlamada;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class guiaServicio extends AppCompatActivity {
    private String response;
    private EditText etCodigo, etTipo, etMarca, etModelo, etCodeCliente, etGarantia, etNomEd, etDireccion, etTipoSer, etEstado, etObservacion;
    private TextView tvServicio, tvCodigo, tvTipo, tvMarca, tvModelo, tvCargo, tvNomTec, tvApeTec, tvRutTec, tvInicio, tvNomEdi, tvDireccion, tvServiS, tvTipoServ, tvFechIni, tvHoraIni, tvObservacion, tvCantAyu, tvAyu1, tvAyu2;
    private ConstraintLayout clConfirma, clSeleccion, clCbo, clFinaliza;
    private List<JSONObject> opcionesJson = new ArrayList<>();
    private Spinner spCantAyud, spAyu1, spAyu2, spPresupueto;
    private ProgressBar pb5;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    AppBarLayout appBarLayout;
    View backgroundOverlay;
    boolean muestra = true;
    ImageView imgInicio, ivHamburgesa;
    Dialog llamada;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_servicio);
        clCbo = findViewById(R.id.clCbo);
        clSeleccion = findViewById(R.id.clSeleccion);
        clConfirma = findViewById(R.id.clConfirma);
        clFinaliza = findViewById(R.id.clFinaliza);
        llamada = new Dialog(this);
        backgroundOverlay = findViewById(R.id.background_overlay3);
        ivHamburgesa = findViewById(R.id.ivHamburgesa3);
        appBarLayout = findViewById(R.id.appBarLayout);

        drawerLayout = findViewById(R.id.drawer_layout);
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

        clCbo.setVisibility(View.GONE);
        clSeleccion.setVisibility(View.GONE);
        clConfirma.setVisibility(View.GONE);
        spCantAyud = findViewById(R.id.spCantAyud);
        tvCantAyu = findViewById(R.id.tvCanyAyu);
        tvAyu1 = findViewById(R.id.tvAyu1);
        tvAyu2 = findViewById(R.id.tvAyu2);
        spAyu1 = findViewById(R.id.spAyu1);
        spAyu2 = findViewById(R.id.spAyu2);
        imgInicio = findViewById(R.id.imgInicio);
        tvInicio = findViewById(R.id.tvInicio);
        spPresupueto = findViewById(R.id.spPresupueto);
        pb5 = findViewById(R.id.pb5);
        //estructurarInputs();
        sharedPreferences = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        int idSAP = sharedPreferences.getInt("idSAP", 0);
        int iduser = sharedPreferences.getInt("iduser", 0);
        int idrole = sharedPreferences.getInt("idrole", 0);
        Log.d("DESDE GUIA", "HOLA DESDE GUIA");
        Log.d("idsap de guia android: ", String.valueOf(idSAP));
        Log.d("iduser de guia android: ", String.valueOf(iduser));
        Log.d("idrole de guia android: ", String.valueOf(idrole));

        //obtenerServicios();
        //setupListeners();
        manageBlinkEffect();
        levantarGuias();

    }

    private void obtenerServicios() {
        String url = "http://172.16.32.50/fabrimetal/gzapto/ajax/ascensor.php?op=selecttipollamadaandroid";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("EL RESPONSE DE SERVICIOS: ", String.valueOf(response));
                        llenarSpinner(response);
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
    }

    private void llenarSpinner(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            List<String> opciones = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //ID para mostrar el segundo combobox
                int callTypeId = jsonObject.getInt("CallTypeID");
                String name = jsonObject.getString("Name");

                if (esValido(callTypeId)) {
                    opciones.add(name);
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, opciones);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner spinner = findViewById(R.id.tipollamada);
            spinner.setAdapter(adapter);

            this.response = response;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private boolean esValido(int callTypeID) {
        int[] listadoValido = {5, 17};
        for (int id : listadoValido) {
            if (callTypeID == id) {
                return true;
            }
        }
        return false;
    }

    private void setupListeners() {
        Spinner spinnerTipollamada = findViewById(R.id.tipollamada);
        spinnerTipollamada.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedOption = adapterView.getItemAtPosition(i).toString();
                int selectedCallTypeID = getCallTypeIDByName(selectedOption);
                if (esValido(selectedCallTypeID)) {
                    obtenerCodigos(selectedCallTypeID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(), "No se selecciono ningun servicio", Toast.LENGTH_SHORT).show();
            }
        });

        Spinner spinnerCodigo = findViewById(R.id.codigo);
        spinnerCodigo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                JSONObject selectedJson = opcionesJson.get(i);
                try {
                    String selectedValue = selectedJson.getString("value");
                    Log.d("VALUE SELECCIONADO", selectedValue);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("codigoSel", selectedValue);
                    editor.apply();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private int getCallTypeIDByName(String name) {
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

    private void obtenerCodigos(int callTypeID) {
        String url = "http://172.16.32.50/fabrimetal/gzapto/ajax/ascensor.php?op=selectascfiltroandroid";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        llenarSpinnerCodigo(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error en la solicitud", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
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

    private void llenarSpinnerCodigo(String responsse) {
        Log.d("RESPONSE FROM SERVER: ", responsse);
        try {
            JSONArray jsonArray = new JSONArray(responsse);
            List<String> opciones = new ArrayList<>();

            opcionesJson.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String value = jsonObject.getString("value");
                String text = jsonObject.getString("text");

                opciones.add(text);
                opcionesJson.add(jsonObject);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, opciones);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner spinnerCodigo = findViewById(R.id.codigo);
            spinnerCodigo.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void buscarEquipo(View view) {
        Spinner spinnerTipollamado = findViewById(R.id.tipollamada);
        String selectedOptionName = spinnerTipollamado.getSelectedItem().toString();
        int selectedCallTypeID = getCallTypeIDByName(selectedOptionName);
        Log.d("CallTypeID", String.valueOf(selectedCallTypeID));

        String url = "http://172.16.32.50/fabrimetal/gzapto/ajax/ascensor.php?op=llamadaservicionormalizacion";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("onResponse", response);
                        procesarRespuestaEquipo(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error en la solicitud", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                String codigoSeleccionado = obtenerCodigoSeleccionado();
                String codigoSel = sharedPreferences.getString("codigoSel", "");
                Log.d("CODIGO SELECCIONADO ", codigoSel);
                params.put("servicecall", codigoSel);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private String obtenerCodigoSeleccionado() {
        Spinner spinnerCodigo = findViewById(R.id.codigo);
        String codigoSeleccionado = spinnerCodigo.getSelectedItem().toString();
        return codigoSeleccionado;
    }

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
            String gps = "-33.385232,-70.776407";
            String nomen = jsonObject.getString("nomenclatura");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("servicecallID", servicecallID);
            editor.putString("customerCode", customerCode);
            editor.putString("itemcode", itemcode);
            editor.putString("fm", fm);
            editor.putString("estado", estado);
            editor.putString("gps", gps);
            editor.putString("codclisap", codclisap);
            editor.putString("nomen", nomen);

            editor.apply();


            Log.d("DIRECCION", direccion);
            etCodigo.setText(itemcode);
            etTipo.setText(itemName);
            etMarca.setText(Manufacturer);
            etModelo.setText(modelo);
            etNomEd.setText(edificio);
            etDireccion.setText(direccion);
            etTipoSer.setText(tipoServicio);
            etEstado.setText(status);
            etCodeCliente.setText(nomen);

            if (nomenclatura == "null" || nomenclatura.equals("null")) {
                etCodeCliente.setText("");
            } else {
                etCodeCliente.setText(nomenclatura);
            }
            if (garantia == "null" || garantia.equals("null")) {
                etGarantia.setText("");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void estructurarInputs() {

        etCodigo = findViewById(R.id.etCodigo);
        etCodigo.setEnabled(false);

        etTipo = findViewById(R.id.etTipo);
        etTipo.setEnabled(false);

        etMarca = findViewById(R.id.etMarca);
        etMarca.setEnabled(false);

        etModelo = findViewById(R.id.etModelo);
        etModelo.setEnabled(false);

        etCodeCliente = findViewById(R.id.etCodeCliente);

        etGarantia = findViewById(R.id.etGarantia);
        etGarantia.setEnabled(false);

        etNomEd = findViewById(R.id.etNomEd);
        etNomEd.setEnabled(false);

        etDireccion = findViewById(R.id.etDireccion);
        etDireccion.setEnabled(false);

        etTipoSer = findViewById(R.id.etTipoSer);
        etTipoSer.setEnabled(false);

        etEstado = findViewById(R.id.etEstado);
        etEstado.setEnabled(false);

        etObservacion = findViewById(R.id.etObservacion);

    }

    public void iniciarServicio(View v) {

        String subject = etObservacion.getText().toString();
        String nomen = etCodeCliente.getText().toString();
        String modnomenclatura = "";


        String servicecallID = sharedPreferences.getString("servicecallID", "");
        Log.d("ServicallID del iniciar servicio", servicecallID);
        String customerCode = sharedPreferences.getString("customerCode", "");
        String itemcode = sharedPreferences.getString("itemcode", "");
        String fm = sharedPreferences.getString("fm", "");
        String estado = sharedPreferences.getString("estado", "");
        String gps = sharedPreferences.getString("gps", "");
        String codclisap = sharedPreferences.getString("codclisap", "");
        int equipmentcardnum = sharedPreferences.getInt("equipmentcardnum", 0);
        if (codclisap != nomen) {
            Toast.makeText(this, "SALUDA A MI AMIGITO!", Toast.LENGTH_SHORT).show();
            modnomenclatura = "&nomen=" + nomen + "&codascen=" + equipmentcardnum;
        }

        Log.d("Iniciar sesion prueba 01", servicecallID + ' ' + customerCode + ' ' + itemcode + ' ' + fm + ' ' + estado + ' ' + gps + ' ' + subject + ' ' + nomen);

        String url = "http://172.16.32.50/fabrimetal/gzapto/ajax/servicio.php" +
                "?op=iniciarsapnormalizacion" +
                "&servicecallID=" + servicecallID +
                "&customerCode=" + customerCode +
                "&itemcode=" + itemcode +
                "&fm=" + fm +
                "&estado=" + estado +
                modnomenclatura +
                "&gps=" + gps +
                "&subject=" + subject;

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error en la solicitud", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(stringRequest);

    }

    public void levantarGuias() {
        String url = "http://172.16.32.50/fabrimetal/gzapto/ajax/servicio.php?op=verificarsapandroid";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Se accedió al verificar", Toast.LENGTH_SHORT).show();
                        Log.d("Response de levate de guias: ", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("Response mardito", response);
                            int accede = jsonObject.getInt("accede");

                            if (accede == 1) {
                                Toast.makeText(getApplicationContext(), "Es 1 carajo", Toast.LENGTH_SHORT).show();
                                pb5.setVisibility(View.GONE);
                               // clConfirma.setVisibility(View.VISIBLE);
                                tvServicio = findViewById(R.id.tvServicio);
                                tvCodigo = findViewById(R.id.tvCodigo);
                                tvTipo = findViewById(R.id.tvTipo);
                                tvMarca = findViewById(R.id.tvMarca);
                                tvModelo = findViewById(R.id.tvModelo);
                                tvCargo = findViewById(R.id.tvCargo);
                                tvNomTec = findViewById(R.id.tvNomTec);
                                tvApeTec = findViewById(R.id.tvApeTec);
                                tvRutTec = findViewById(R.id.tvRutTec);
                                tvNomEdi = findViewById(R.id.tvNomEdi);
                                tvDireccion = findViewById(R.id.tvDireccion);
                                tvServiS = findViewById(R.id.tvServiS);
                                tvTipoServ = findViewById(R.id.tvTipoServ);
                                tvFechIni = findViewById(R.id.tvFechIni);
                                tvHoraIni = findViewById(R.id.tvHoraIni);
                                tvObservacion = findViewById(R.id.tvObservacion);


                                String servicioini = jsonObject.getString("servicioini");
                                String codigoEquipo = jsonObject.getString("codigoEquipo");
                                String tipoEquipo = jsonObject.getString("tipoEquipo");
                                String marcaEquipo = jsonObject.getString("marcaEquipo");
                                String modeloEquipo = jsonObject.getString("modeloEquipo");
                                String cargoTecnico = jsonObject.getString("cargoTecnico");
                                String nombresTecnico = jsonObject.getString("nombresTecnico");
                                String apellidoTecnico = jsonObject.getString("apellidoTecnico");
                                String rutTecnico = jsonObject.getString("rutTecnico");
                                String nombreEdificio = jsonObject.getString("nombreEdificio");
                                String direccionEdificio = jsonObject.getString("direccionEdificio");
                                String servicioServicio = jsonObject.getString("servcioServicio");
                                String tipoServicio = jsonObject.getString("tipoServicio");
                                String fechainicioServicio = jsonObject.getString("fechainicioServicio");
                                String horainicioServicio = jsonObject.getString("horainicioServicio");
                                String observacionServicio = jsonObject.getString("observacionServicio");
                                String servillo = jsonObject.getString("servillo");
                                String actCodigo = jsonObject.getString("actCodigo");
                                String swBtnFin = jsonObject.getString("swBtnFin");

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("servicioini", servicioini);
                                editor.putString("codigoEquipo", codigoEquipo);
                                editor.putString("tipoEquipo", tipoEquipo);
                                editor.putString("modeloEquipo", modeloEquipo);
                                editor.putString("cargoTecnico", cargoTecnico);
                                editor.putString("nombresTecnico", nombresTecnico);
                                editor.putString("apellidoTecnico", apellidoTecnico);
                                editor.putString("marcaEquipo",marcaEquipo);
                                editor.putString("rutTecnico", rutTecnico);
                                editor.putString("nombreEdificio", nombreEdificio);
                                editor.putString("direccionEdificio", direccionEdificio);
                                editor.putString("tipoServicio", tipoServicio);
                                editor.putString("servicioServicio", servicioServicio);
                                editor.putString("fechainicioServicio", fechainicioServicio);
                                editor.putString("horainicioServicio", horainicioServicio);
                                editor.putString("observacionServicio", observacionServicio);
                                editor.putString("servillo", servillo);
                                editor.putString("actCodigo", actCodigo);
                                editor.putString("swBtnFin", swBtnFin);
                                editor.apply();

                                tvServicio.setText("Servicio: # " + servicioini);
                                tvCodigo.setText(codigoEquipo);
                                tvTipo.setText(tipoEquipo);
                                tvMarca.setText(marcaEquipo);
                                tvModelo.setText(modeloEquipo);
                                tvCargo.setText(cargoTecnico);
                                tvNomTec.setText(nombresTecnico);
                                tvApeTec.setText(apellidoTecnico);
                                tvRutTec.setText(rutTecnico);
                                tvNomEdi.setText(nombreEdificio);
                                tvDireccion.setText(direccionEdificio);
                                tvServiS.setText(servicioServicio);
                                tvTipoServ.setText(tipoServicio);
                                tvFechIni.setText(fechainicioServicio);
                                tvHoraIni.setText(horainicioServicio);
                                tvObservacion.setText(observacionServicio);

                                AlertDialog dialogo = new AlertDialog.Builder(guiaServicio.this)
                                        .setPositiveButton("Si, proseguir", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = new Intent(getApplicationContext(), inicioServicio.class);
                                                startActivity(i);
                                                finish();
                                                /*
                                                Slide slide = new Slide();
                                                slide.setSlideEdge(Gravity.END);
                                                slide.setDuration(500);
                                                TransitionManager.beginDelayedTransition(clBuscar, slide);
                                                clBuscar.setVisibility(View.GONE);

                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        clAnimada.setVisibility(View.VISIBLE);
                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                clAnimada.setVisibility(View.GONE);
                                                            }
                                                        }, 3000);
                                                    }
                                                }, 500);
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Intent i = new Intent(getApplicationContext(), menu.class);
                                                        startActivity(i);
                                                        finish();
                                                    }
                                                }, 800);*/
                                            }
                                        })
                                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .setTitle("Tienes un servicio iniciado")
                                        .setMessage("¿Deseas proseguir con este servicio?")
                                        .create();
                                dialogo.show();


                            } else if (accede == 2) {
                                pb5.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Es 2 coñooo", Toast.LENGTH_SHORT).show();
                                //clCbo.setVisibility(View.VISIBLE);
                                //clSeleccion.setVisibility(View.VISIBLE);
                                //estructurarInputs();
                                //obtenerServicios();
                                //setupListeners();
                                imgInicio.setVisibility(View.VISIBLE);
                                tvInicio.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "error: " + error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                int idSAP = sharedPreferences.getInt("idSAP", 0);
                Log.d("ID SAP SHARED2", String.valueOf(idSAP));
                params.put("idSAP_form", String.valueOf(idSAP));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        queue.add(stringRequest);
    }

    public void terminarServicio(View v) {
        //servicioini(idservicio), codigoEquipo(idacensor), servillo(tipoencuesta), actCodigo(idactividad), swBtnFin
        String servicioini = sharedPreferences.getString("servicioini", "");
        String codigoEquipo = sharedPreferences.getString("codigoEquipo", "");
        String servillo = sharedPreferences.getString("servillo", "");
        String actCodigo = sharedPreferences.getString("actCodigo", "");
        String swBtnFin = sharedPreferences.getString("swBtnFin", "");
        Log.d("COMPROBACION COMPRAVACION", servicioini + "CodEq: " + codigoEquipo + "servillo: " + servillo + " actCod: " + actCodigo + " swBtnFin: " + swBtnFin);

        clConfirma.setVisibility(View.GONE);
        clFinaliza.setVisibility(View.VISIBLE);

        String url = "http://172.16.32.50/fabrimetal/gzapto/ajax/servicio.php?op=guiaPorCerrar";
        String url2 = "http://172.16.32.50/fabrimetal/gzapto/ajax/servicio.php?op=selecttestadossapandroid";
        String url3 = "http://172.16.32.50/fabrimetal/gzapto/ajax/servicio.php?op=infoguiasap";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPUESTA GUIAPORCERRAR", response);
                        try {
                            Log.d("Respuesta de guiaPorCerrar: ", response);
                            JSONObject jsonResponse = new JSONObject(response);
                            int cerrarguia = jsonResponse.getInt("cerrarguia");
                            if (cerrarguia == 1) {
                                Toast.makeText(getApplicationContext(), "Exito, se puede cerrar esta guia", Toast.LENGTH_SHORT).show();

                                StringRequest request2 = new StringRequest(Request.Method.POST, url2,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Log.d("El resultado del selectestados", response);
                                                try {
                                                    JSONArray jsonArray = new JSONArray(response);
                                                    List<String> estados = new ArrayList<>();
                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        JSONObject estadoJson = jsonArray.getJSONObject(i);
                                                        String value = estadoJson.getString("value");
                                                        String name = estadoJson.getString("name");
                                                        estados.add(name);
                                                    }
                                                    Spinner spEstadoF = findViewById(R.id.spEstadoF);

                                                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(guiaServicio.this, android.R.layout.simple_spinner_item, estados);
                                                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                    spEstadoF.setAdapter(spinnerAdapter);
                                                } catch (JSONException e) {
                                                    Log.d("Error en selectestados", e.toString());
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.d("EROR DE VOLEY", error.toString());
                                            }
                                        }
                                );
                                Volley.newRequestQueue(guiaServicio.this).add(request2);

                                StringRequest request3 = new StringRequest(Request.Method.POST, url3,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                setupAyudantes();
                                                setupCantAyudantes();
                                                Log.d("RESPUESTA DE INFOGUIASAP", response);
                                                EditText etCodF, etTipAs, etMarF, etModF, etNomEdF, etDirF, etFecF, etHorF, etTipSerF, etEstadoF, etObsF;
                                                etCodF = findViewById(R.id.etCodF);
                                                etTipAs = findViewById(R.id.etTipAs);
                                                etMarF = findViewById(R.id.etMarF);
                                                etModF = findViewById(R.id.etModF);
                                                etNomEdF = findViewById(R.id.etNomEdF);
                                                etDirF = findViewById(R.id.etDirF);
                                                etFecF = findViewById(R.id.etFecF);
                                                etHorF = findViewById(R.id.etHorF);
                                                etTipSerF = findViewById(R.id.etTipSerF);
                                                etEstadoF = findViewById(R.id.etEstadoF);
                                                etObsF = findViewById(R.id.etObsF);

                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    String codigoEquipo = jsonObject.getString("codigo");
                                                    String tascensorfi = jsonObject.getString("tipoequipo");
                                                    String marcafi = jsonObject.getString("Manufacturer");
                                                    String modelofi = jsonObject.getString("modelo");
                                                    String edificiofi = jsonObject.getString("edificio");
                                                    String direccionfi = jsonObject.getString("direccion");
                                                    String fechainifi = jsonObject.getString("fecha");
                                                    String horainifi = jsonObject.getString("hora");
                                                    String tserviciofi = jsonObject.getString("CallType");
                                                    String estadoinifi = jsonObject.getString("status");
                                                    String observacioniniso = jsonObject.getString("Subject");
                                                    etCodF.setText(codigoEquipo);
                                                    etTipAs.setText(tascensorfi);
                                                    etMarF.setText(marcafi);
                                                    etModF.setText(modelofi);
                                                    etNomEdF.setText(edificiofi);
                                                    etDirF.setText(direccionfi);
                                                    etFecF.setText(fechainifi);
                                                    etHorF.setText(horainifi);
                                                    etTipSerF.setText(tserviciofi);
                                                    etEstadoF.setText(estadoinifi);
                                                    etObsF.setText(observacioniniso);
                                                } catch (JSONException e) {
                                                    Log.d("Error en INFOGUIASAP", e.toString());
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.d("Error en INFOGUIASAP", error.toString());
                                            }
                                        }
                                ) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<>();
                                        params.put("idserviciofi", servicioini);
                                        params.put("idactividad", actCodigo);
                                        return params;
                                    }
                                };
                                Volley.newRequestQueue(guiaServicio.this).add(request3);
                            } else {
                                Toast.makeText(getApplicationContext(), "Esta guia no se puede cerrar", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error en GUIAPORCERRAR", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("idactividad", actCodigo);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);

    }

    public void selectTecnico(Spinner spTecnico) {
        String url = "http://172.16.32.50/fabrimetal/gzapto/ajax/servicio.php?op=selectTecnicoandroid";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response de TECNICOS", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            List<String> tecnicos = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject tecicosJson = jsonArray.getJSONObject(i);
                                String employeeID = tecicosJson.getString("EmployeeID");
                                String fullName = tecicosJson.getString("FullName");
                                tecnicos.add(fullName);
                            }


                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, tecnicos);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spTecnico.setAdapter(spinnerAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        Volley.newRequestQueue(this).add(request);
    }

    public void setupAyudantes() {

        List<String> opciones = new ArrayList<>();
        opciones.add("SELECCIONE OPCIÓN");
        opciones.add("Si");
        opciones.add("No");

        Spinner spOpayu;
        spOpayu = findViewById(R.id.spOpayu);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOpayu.setAdapter(spinnerAdapter);

        spOpayu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String seleccion = adapterView.getItemAtPosition(position).toString();
                if (seleccion.equals("Si")) {

                    Toast.makeText(getApplicationContext(), "OJO", Toast.LENGTH_SHORT).show();
                    tvCantAyu.setVisibility(View.VISIBLE);
                    spCantAyud.setVisibility(View.VISIBLE);

                } else {
                    spCantAyud.setVisibility(View.GONE);
                    tvCantAyu.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setupCantAyudantes() {

        List<String> opciones = new ArrayList<>();
        opciones.add("Seleccione cantidad de ayudantes");
        opciones.add("1");
        opciones.add("2");


        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCantAyud.setAdapter(spinnerAdapter);

        spCantAyud.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String seleccion = adapterView.getItemAtPosition(position).toString();
                if (seleccion.equals("1")) {
                    Toast.makeText(getApplicationContext(), "Marcaste 1", Toast.LENGTH_SHORT).show();
                    tvAyu1.setVisibility(View.VISIBLE);
                    spAyu1.setVisibility(View.VISIBLE);
                    selectTecnico(spAyu1);
                } else if (seleccion.equals("2")) {
                    Toast.makeText(getApplicationContext(), "Marcaste 2", Toast.LENGTH_SHORT).show();
                    tvAyu1.setVisibility(View.VISIBLE);
                    spAyu1.setVisibility(View.VISIBLE);
                    tvAyu2.setVisibility(View.VISIBLE);
                    spAyu2.setVisibility(View.VISIBLE);
                    selectTecnico(spAyu1);
                    selectTecnico(spAyu2);
                } else {
                    tvAyu1.setVisibility(View.GONE);
                    spAyu1.setVisibility(View.GONE);
                    tvAyu2.setVisibility(View.GONE);
                    spAyu2.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setupPresupuesto() {
        List<String> opciones = new ArrayList<>();
        opciones.add("SELECCIONE OPCIÓN");
        opciones.add("SI");
        opciones.add("NO");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPresupueto.setAdapter(spinnerAdapter);

        spPresupueto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String seleccion = adapterView.getItemAtPosition(position).toString();
                if (seleccion.equals("SI")) {

                } else if (seleccion.equals("NO")) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void irPop(View v) {
        Intent view = new Intent(getApplicationContext(), PopLlamada.class);
        startActivity(view);
    }

    public void manageBlinkEffect(){
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(tvInicio, "scaleX", 1.0f, 1.2f, 1.0f);
        scaleX.setDuration(800);
        scaleX.setRepeatMode(ValueAnimator.REVERSE);
        scaleX.setRepeatCount(Animation.INFINITE);

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(tvInicio, "scaleY", 1.0f, 1.2f, 1.0f);
        scaleX.setDuration(800);
        scaleX.setRepeatMode(ValueAnimator.REVERSE);
        scaleX.setRepeatCount(Animation.INFINITE);


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.start();
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

    /*
    public void irPop(View v) {
        TextView txtclose;
        Button btnFollow;
        llamada.setContentView(R.layout.popllamada);
        txtclose = (TextView) llamada.findViewById(R.id.txtclose);
        //Button = (Button) llamada.findViewById(R.id.)
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamada.dismiss();
            }
        });
        llamada.show();
    }
*/
}