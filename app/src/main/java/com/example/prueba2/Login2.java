package com.example.prueba2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

    private ImageView imageViewIndex;
    private EditText editTextEmail, editTextPassword;
    private TextView textViewError;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        //Integración con elementos de la activity
        imageViewIndex = findViewById(R.id.submit);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        textViewError = findViewById(R.id.error);
        progressBar = findViewById(R.id.progressBar);
        sharedPreferences = getSharedPreferences("sesion", MODE_PRIVATE);

        //Comprobación para pasar directo sin logearse
        if (sharedPreferences.getBoolean("logged", false)) {
            Intent intent = new Intent(getApplicationContext(), menu.class);
            startActivity(intent);
            finish();
        }

    }

    //Metodo que inicia el Logín
    public void loggearse(View v) {
        textViewError.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        validarUsuario("http://172.16.32.50/fabrimetal/gzapto/ajax/usuario.php?op=verificar");
    }

    //Metodo de Login
    private void validarUsuario(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Log.d("Respuesta desde el login2", String.valueOf(jsonResponse));
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        JSONObject userData = jsonResponse.getJSONObject("user_data");
                        int idSAP = jsonResponse.getInt("idSAP");
                        int iduser = jsonResponse.getInt("iduser");
                        int idrole = jsonResponse.getInt("idrole");
                        String nombre = userData.getString("nombre");
                        String apellido = userData.getString("apellido");
                        String imagen = userData.getString("imagen");

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("idSAP", idSAP);
                        editor.putInt("iduser", iduser);
                        editor.putInt("idrole", idrole);
                        editor.putString("nombre", nombre);
                        editor.putString("apellido", apellido);
                        editor.putString("imagen", imagen);
                        editor.putBoolean("logged", true);
                        editor.apply();
                        Log.d("ID SAP SHARED", String.valueOf(idSAP));
                        Log.d("ID role SHARED", String.valueOf(idrole));
                        Intent intent = new Intent(getApplicationContext(), menu.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String message = jsonResponse.getString("message");
                        textViewError.setText(message);
                        textViewError.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    textViewError.setText("Usuario o contraseña incorrectos.");
                    textViewError.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                textViewError.setText(error.getLocalizedMessage());
                textViewError.setVisibility(View.VISIBLE);
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("username_form", editTextEmail.getText().toString());
                parametros.put("password_form", editTextPassword.getText().toString());
                return parametros;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
