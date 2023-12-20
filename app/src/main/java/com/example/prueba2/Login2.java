package com.example.prueba2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login2 extends AppCompatActivity {

    //Definición de objetos y variables de la Activity.
    private ImageView imgSubmit;
    private EditText txtUsuario, txtPassword;
    private TextView tvError;
    private ProgressBar pbLogin;
    private SharedPreferences sharedPreferences;
    String servidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        //Variable local para configurar el servidor.
        servidor = getString(R.string.servidor);

        //Integración con elementos de la activity
        imgSubmit = findViewById(R.id.imgSubmit);
        txtUsuario = findViewById(R.id.txtUsuario);
        txtPassword = findViewById(R.id.txtPassword);
        tvError = findViewById(R.id.tvError);
        pbLogin = findViewById(R.id.pbLogin);
        sharedPreferences = getSharedPreferences("sesion", MODE_PRIVATE);

        //Ejecución de metodos no mas entrar a la actividad
        ingresarSinLogin();

    }

    //Metodo asociado al botón de ingreso.
    public void moverAlLogin(View v) {
        tvError.setVisibility(View.GONE);
        pbLogin.setVisibility(View.VISIBLE);
        validarUsuario(servidor+"/ajax/usuario.php?op=verificar");
    }

    //Metodo de Login+
    private void validarUsuario(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { //Hay respuesta
                pbLogin.setVisibility(View.GONE);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Log.d("Respuesta desde el login2[validarUsuario]: ", String.valueOf(jsonResponse));
                    boolean success = jsonResponse.getBoolean("success");

                    //Validación correcta
                    if (success) {
                        JSONObject userData = jsonResponse.getJSONObject("user_data");

                        //Se asignan campos a variables
                        int idSAP = jsonResponse.getInt("idSAP");
                        int iduser = jsonResponse.getInt("iduser");
                        int idrole = jsonResponse.getInt("idrole");
                        String nombre = userData.getString("nombre");
                        String apellido = userData.getString("apellido");
                        String imagen = userData.getString("imagen");
                        String email = userData.getString("email");

                        //Se guardan campos en memoria del celular(SharedPreference)
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("idSAP", idSAP);
                        editor.putInt("iduser", iduser);
                        editor.putInt("idrole", idrole);
                        editor.putString("nombre", nombre);
                        editor.putString("apellido", apellido);
                        editor.putString("imagen", imagen);
                        editor.putString("email", email);
                        editor.putBoolean("logged", true);
                        editor.apply();

                        Log.d("ID SAP SHARED", String.valueOf(idSAP));
                        Log.d("ID role SHARED", String.valueOf(idrole));

                        //Redireccionamiento a menú
                        Intent intent = new Intent(getApplicationContext(), menu.class);
                        startActivity(intent);
                        finish();
                    } else {
                        //Error al Loguearse
                        String message = jsonResponse.getString("message");
                        tvError.setText(message);
                        tvError.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    tvError.setText("Usuario o contraseña incorrectos.");
                    tvError.setVisibility(View.VISIBLE);
                }
            }
            //En caso de que no haya respuesta del servidor
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbLogin.setVisibility(View.GONE);
                tvError.setText(error.getLocalizedMessage());
                tvError.setVisibility(View.VISIBLE);
            }
        }
        ) {
            //Envio de parametros POST
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("username_form", txtUsuario.getText().toString());
                parametros.put("password_form", txtPassword.getText().toString());
                return parametros;
            }

            //Manejo de errores visualización y carga
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };
        //Envió de solicitud al servidor
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void ingresarSinLogin(){
        //Comprobación para pasar directo sin logearse
        if (sharedPreferences.getBoolean("logged", false)) {
            Intent intent = new Intent(getApplicationContext(), menu.class);
            startActivity(intent);
            finish();
        }
    }

}
