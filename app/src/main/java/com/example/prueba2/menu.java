package com.example.prueba2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prueba2.popups.PopLlamada;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import java.util.Random;
import java.util.Set;


public class menu extends AppCompatActivity {

    TextView textViewNombre, tvNombre, tvPendiente;
    SharedPreferences sharedPreferences;
    LinearLayout linearLayoutCerrar, linearLayoutDashboard;
    ImageView imageViewListar, imageViewCrear, imageViewLlamada, imageViewGuia;
    private BitmapShader shader;
    private Paint paint;

    List<Uri> listaImagenes = new ArrayList<>();

    //PRUEBA
    private static final int PERMISSION_REQUEST_CODE = 2;
    private NetworkChangeReceiver networkChangeReceiver;
    LinearLayout llFirst, llSecond;
    ConstraintLayout clInformativa;
    ProgressBar pbInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        sharedPreferences = getSharedPreferences("sesion", MODE_PRIVATE);
        textViewNombre = findViewById(R.id.tvNombre);
        llFirst = findViewById(R.id.llFirst);
        llSecond = findViewById(R.id.llSecond);
        pbInternet = findViewById(R.id.pbInternet);
        tvPendiente = findViewById(R.id.tvPendiente);
        clInformativa = findViewById(R.id.clInformativa);

        if(checkStoragePermission()){
            Toast.makeText(getApplicationContext(), "Storage activo", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Storage activo", Toast.LENGTH_SHORT).show();
        }else{

            Toast.makeText(getApplicationContext(), "Storage sin activar", Toast.LENGTH_SHORT).show();
            requestStoragePermission();
        }

        tvNombre = findViewById(R.id.tvNombre);

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();

        Boolean bloqueado = sharedPreferences.getBoolean("bloqueado", false);
        int servicioxfinalizar = sharedPreferences.getInt("servicioxfinalizar", 0);
        int finalizarsinnet = sharedPreferences.getInt("finalizarsinnet",0);
        if (network != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Toast.makeText(getApplicationContext(), "HAY INTERNET", Toast.LENGTH_SHORT).show();
            levantarGuias();
            selectTecnico();
            obtenerCodigosNormalizacion();
            if (servicioxfinalizar == 1) {
                buscarEquipo();
                iniciarServicio2();
                editor.putBoolean("bloqueado", false);
                editor.putInt("servicioxfinalizar", 0);
                editor.apply();
                Intent i = new Intent(getApplicationContext(), menu.class);
                startActivity(i);
                finish();
            }
            int accede = sharedPreferences.getInt("accede", 0);
            if(accede==1){
                guiaPorCerrar();
                infogias();
                selectTecnico1();
            }
            if(finalizarsinnet == 1){
                finalizarServicio2();
                finalizarServicio2();
                Toast.makeText(getApplicationContext(),"YO YO, DESDE FINALIZARSININTERNET", Toast.LENGTH_SHORT).show();
                editor.putBoolean("finalizarbloqueado", false);
                editor.putInt("finalizarsinnet",0);
                editor.apply();
            }
        } else {
            Toast.makeText(getApplicationContext(), "NO HAY INTERNET", Toast.LENGTH_SHORT).show();
            obtenerServicios();
        }
        if (bloqueado == true) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Sin acceso");
            builder.setMessage("No puedes acceder a las guias porque tienes una guia por inciar pendiente, se requiere conexión a internet");
            builder.setPositiveButton("Confirmar",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        Boolean finalizarbloqueado = sharedPreferences.getBoolean("finalizarbloqueado", false);
        if (finalizarbloqueado == true) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Sin acceso");
            builder.setMessage("No puedes acceder a las guias porque tienes una guia por finalizar pendiente, se requiere conexión a internet");
            builder.setPositiveButton("Confirmar",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }



        String nombre = sharedPreferences.getString("nombre", "");
        nombre = nombre.substring(0, 1).toUpperCase() + nombre.substring(1).toLowerCase();
        String apellido = sharedPreferences.getString("apellido", "");
        apellido = apellido.substring(0, 1).toUpperCase() + apellido.substring(1).toLowerCase();
        tvNombre.setText(nombre + " " + apellido);

        //Verificar conexión a internet


        String imagen = sharedPreferences.getString("imagen", "");
        Log.d("Imagen drecha", imagen);
        if (imagen.equals("noimg.jpg") || imagen.equals("")) {
            Toast.makeText(getApplicationContext(), "No tienes una imagen", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        int servicioxfinalizar = sharedPreferences.getInt("servicioxfinalizar", 0);
        int finalizarsinnet = sharedPreferences.getInt("finalizarsinnet",0);
        networkChangeReceiver = new NetworkChangeReceiver(llFirst, llSecond, pbInternet, servicioxfinalizar, finalizarsinnet, tvPendiente, clInformativa);
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeReceiver);
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

    //PRUEBA METODOS
    private boolean checkStoragePermission(){
        int writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return writePermission == PackageManager.PERMISSION_GRANTED && readPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(this, "Necesitamos tu ubicación para proporcionar un mejor servicio.", Toast.LENGTH_SHORT).show();
        }

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "No se concedio el permiso de ubicacion", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void akak(View v) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_sorpresa_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void logout(View v) {
        //Inflar el diseño personalizado
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
       // builder.setTitle("¿Seguro deseas cerrar sesión?");
        builder.setPositiveButton("Cerrar sesión", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("logged", false);
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), Login2.class);
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

    public void irDashboard(View v) {
        Intent i = new Intent(getApplicationContext(), dashboard.class);
        startActivity(i);
        finish();
    }

    public void abrirGuia(View v) {
            Intent i = new Intent(getApplicationContext(), guiaServicio.class);
            startActivity(i);
            finish();

    }

    public void irConfig(View v) {
        Intent i = new Intent(getApplicationContext(), config_perfil.class);
        startActivity(i);
        finish();
    }

    //Metodos para cuando no hay conexión.

    public void levantarGuias() {
        String url = "http://172.16.32.50/gzapto/ajax/servicio.php?op=verificarsapandroid";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Se accedió al verificar", Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int accede = jsonObject.getInt("accede");
                            if (accede == 1) {
                                Toast.makeText(getApplicationContext(), "Es 1", Toast.LENGTH_SHORT).show();

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
                                editor.putInt("accede", 1);
                                editor.putString("servicioini", servicioini);
                                editor.putString("codigoEquipo", codigoEquipo);
                                editor.putString("tipoEquipo", tipoEquipo);
                                editor.putString("modeloEquipo", modeloEquipo);
                                editor.putString("cargoTecnico", cargoTecnico);
                                editor.putString("nombresTecnico", nombresTecnico);
                                editor.putString("apellidoTecnico", apellidoTecnico);
                                editor.putString("marcaEquipo", marcaEquipo);
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

                            } else if (accede == 2) {

                                Toast.makeText(getApplicationContext(), "Es 2", Toast.LENGTH_SHORT).show();

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("accede", 2);
                                editor.apply();
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
                int iduser = sharedPreferences.getInt("iduser", 0);
                Log.d("ID SAP SHARED2", String.valueOf(idSAP));
                params.put("idSAP_form", String.valueOf(idSAP));
                params.put("iduser", String.valueOf(iduser));
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

    public void selectTecnico() {
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

                        //Convertir listas a json
                        JSONArray tecnicosJsonArray = new JSONArray(tecnicos1);
                        JSONArray employeeIdsJsonArray = new JSONArray(employeeIds1);

                        //Convertir JSON a cadena
                        String tecnicosJsonString = tecnicosJsonArray.toString();
                        String employeeIdsJsonString = employeeIdsJsonArray.toString();

                        //Guardar las cadenas JSON en Shared
                        sharedPreferences = getSharedPreferences("sesion", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("nombre_tecnicos", tecnicosJsonString);
                        editor.putString("id_tecnicos", employeeIdsJsonString);
                        editor.apply();
                        Toast.makeText(getApplicationContext(), "Datos del tecnico agregados correctamente", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace
        );
        Volley.newRequestQueue(this).add(request);
    }

    private void obtenerServicios() {
        String url = "http://172.16.32.50/gzapto/ajax/ascensor.php?op=selecttipollamadaandroid";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d("EL RESPONSE DE SERVICIOS: ", String.valueOf(response));
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

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("opLlamadas", String.valueOf(opciones));
                        editor.apply();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(getApplicationContext(), "Error en la solicitud1", Toast.LENGTH_SHORT).show()
        );
        queue.add(stringRequest);
    }

    //Este metodo tiene que iniciarse una vez vuelva la conexion.
    private void obtenerCodigosNormalizacion() {
        String url = "http://172.16.32.50/gzapto/ajax/ascensor.php?op=selectascfiltroandroid";
        int idllamada = 5;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                //Se llena el spinner con la respuesta del servidor
                response -> llenarSpinnerCodigoN(response),
                error -> Toast.makeText(getApplicationContext(), "Error en la solicitud3", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //Parametros que se pasan al servidor como POST.
                int idrole = sharedPreferences.getInt("idrole", 0);
                int idSAP = sharedPreferences.getInt("idSAP", 0);
                params.put("idservicio", String.valueOf(idllamada));
                params.put("idrole", String.valueOf(idrole));
                params.put("idSAP_form", String.valueOf(idSAP));
                return params;
            }
        };
        queue.add(stringRequest);
    }


    //Metodo para llenar Spinner o combobox del codigo de servicio.
    private void llenarSpinnerCodigoN(String responsse) {
        List<JSONObject> opcionesJson = new ArrayList<>();
        Log.d("RESPONSE FROM SERVER: ", responsse);
        try {
            JSONArray jsonArray = new JSONArray(responsse);
            List<String> opciones = new ArrayList<>();
            List<String> opcionesvalue = new ArrayList<>();

            opcionesJson.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String text = jsonObject.getString("text");
                String value = jsonObject.getString("value");
                //Se añaden el campo text que llega al listado.
                opciones.add(text);
                opcionesvalue.add(value);
                opcionesJson.add(jsonObject);
            }

            List<String> opcionesJsonStrings = new ArrayList<>();
            for (JSONObject json : opcionesJson) {
                opcionesJsonStrings.add(json.toString());
            }

            JSONArray opcionesJsonArray = new JSONArray(opcionesJsonStrings);
            String opcionesJsonString = opcionesJsonArray.toString();

            JSONArray opcionesvalueJsonArray = new JSONArray(opcionesvalue);
            String opcionesvalueJsonString = opcionesvalueJsonArray.toString();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("all_services_normalizacion", opcionesJsonString);
            editor.putString("all_services_n_values", opcionesvalueJsonString);
            editor.apply();
            Toast.makeText(getApplicationContext(), "CODES NOMIES", Toast.LENGTH_SHORT).show();
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

    public void buscarEquipo() {
        String url;
        int valuellamadasel = sharedPreferences.getInt("valuellamadasel", 0);
        if (valuellamadasel == 5) {
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
                    Toast.makeText(getApplicationContext(), "Buscar equipo sin net correcto", Toast.LENGTH_SHORT).show();
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

            /* ARREGLAR ESTE DETALLE
            etCodCli.setText(nomen);

            if (nomenclatura.equals("null") || nomenclatura.isEmpty()) {
                etCodCli.setText("");
            } else {
                etCodCli.setText(nomenclatura);
            }

             */

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void iniciarServicio2() {
        String subject = sharedPreferences.getString("netiniobs", "");
        String nomen = ""; //Arreglar esto
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

        RequestQueue queue = Volley.newRequestQueue(menu.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", response);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("accede",0);
                        Toast.makeText(getApplicationContext(), "Se inicio el servicio correctamente", Toast.LENGTH_SHORT).show();
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

    public void guiaPorCerrar() {
        String actCodigo = sharedPreferences.getString("actCodigo", "");
        String url = "http://172.16.32.50/gzapto/ajax/servicio.php?op=guiaPorCerrar";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("RESPUESTA GUIAPORCERRAR", response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        int cerrarguia = jsonResponse.getInt("cerrarguia");
                        Toast.makeText(getApplicationContext(), "CAMBIANDO VALOR DE CERRARGUIA  A: "+cerrarguia, Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("cerrargianet", cerrarguia);
                        editor.apply();
                        if (cerrarguia == 1) {
                            Toast.makeText(getApplicationContext(), "Exito, se puede cerrar esta guia", Toast.LENGTH_SHORT).show();
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

    public void infogias() {
        String servicioini = sharedPreferences.getString("servicioini", "");
        String actCodigo = sharedPreferences.getString("actCodigo", "");
        String url3 = "http://172.16.32.50/gzapto/ajax/servicio.php?op=infoguiasap";
        StringRequest request3 = new StringRequest(Request.Method.POST, url3,
                response -> {
                    //Metodos que se iniciar con la respuesta valida
                    Log.d("RESPUESTA DE INFOGUIASAP sin net", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String codigoEquipo = jsonObject.getString("codigo");
                        String customerCode = jsonObject.getString("CustomerCode");
                        String idascensor = jsonObject.getString("idascensor");
                        String edificio = jsonObject.getString("edificio");
                        String nomenclatura = jsonObject.getString("nomenclatura");
                        String supervisorID = jsonObject.getString("supervisorID");
                        Toast.makeText(getApplicationContext(), "ESTAS EN INFOGIAS SIN NER", Toast.LENGTH_SHORT).show();


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
        Volley.newRequestQueue(menu.this).add(request3);
    }

    public void selectTecnico1() {
        String url = "http://172.16.32.50/gzapto/ajax/servicio.php?op=selectTecnicoandroid";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("Response de TECNICOS 1", response);
                    try {
                        JSONArray jsonArray1 = new JSONArray(response);
                        List<String> tecnicos1 = new ArrayList<>();
                        List<Integer> employeeIds1 = new ArrayList<>();

                        int numberOfTecnicos = jsonArray1.length();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("numberOfTecnicos", numberOfTecnicos);
                        editor.apply();

                        //Se recorre la respuesta y se asigna sus valores al listado
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            JSONObject tecicosJson1 = jsonArray1.getJSONObject(i);
                            String fullName = tecicosJson1.getString("FullName");
                            int employeeId = tecicosJson1.getInt("EmployeeID");
                            tecnicos1.add(fullName);
                            employeeIds1.add(employeeId);

                            editor.putInt("employeeIdNet"+i, employeeId);
                            editor.putString("fullNameNet"+i, fullName);
                            editor.apply();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace
        );
        Volley.newRequestQueue(this).add(request);
    }

    //Metodo para finalizar el servicio
    public void finalizarServicio2() {
            Toast.makeText(getApplicationContext(), "HO HOLA DESDE FINSERVICIO SIN NET", Toast.LENGTH_SHORT).show();
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
            String firma = sharedPreferences.getString("firma", "");
            if (oppre == 1) {
                Set<String> uriStrings = sharedPreferences.getStringSet("listaImagenes", new HashSet<>());
                List<Uri> listaImagenesRecuperadas = new ArrayList<>();
                for (String uriString : uriStrings) {
                    listaImagenesRecuperadas.add(Uri.parse(uriString));
                }
                menu.SubirImagenesTask subirImagenesTask = new menu.SubirImagenesTask("http://172.16.32.50/gzapto/ajax/uploadpres.php?op=interno");
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
                            Toast.makeText(getApplicationContext(), "Servicio sin NET finalizado", Toast.LENGTH_SHORT).show();
                            editor.remove("file01");
                            editor.remove("file02");
                            editor.remove("file03");
                            editor.apply();
                            Boolean firmaplicada = sharedPreferences.getBoolean("firmaaplicada", false);
                            if (firmaplicada == true) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                    }
                                }, 5000);
                            } else {

                                editor.remove("file01");
                                editor.remove("file02");
                                editor.remove("file03");
                                editor.apply();
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
    }

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


    //Metodo para subir imagenes de presupuesto al servidor con HTTP
    public void subirImagenesAlServidor(String serverUrl, Uri[] uris) { //Recibe 2 parametros, la url del servidor y la matriz de uris
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


}




