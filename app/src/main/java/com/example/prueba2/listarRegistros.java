package com.example.prueba2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class listarRegistros extends AppCompatActivity {

    TextView textViewFetchUser;
    SharedPreferences sharedPreferences;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listar_registros);
        textViewFetchUser = findViewById(R.id.fetchResult);

        sharedPreferences = getSharedPreferences("MyAppName", MODE_PRIVATE);
        if(sharedPreferences.getString("logged", "false").equals("false")){
            Intent intent = new Intent(getApplicationContext(), Login2.class);
            startActivity(intent);
            finish();
        }
        Log.d("mensaje wea", "mensaje1");
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://172.16.32.50/login-android/profile.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        textViewFetchUser.setText(response);
                        Log.d("mensaje wea", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("mensaje wea", "mensaje 2");
            }
        }
        ){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("email",sharedPreferences.getString("email", ""));
                paramV.put("apiKey", sharedPreferences.getString("apiKey", ""));
                return paramV;
            }
        };
        queue.add(stringRequest);
    }
}