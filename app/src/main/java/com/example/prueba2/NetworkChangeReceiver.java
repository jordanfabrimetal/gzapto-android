package com.example.prueba2;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

public class NetworkChangeReceiver extends BroadcastReceiver {

    LinearLayout llFirst, llSecond;
    ProgressBar pbInternet;
    Boolean bloqueado;
    int servicioxfinalizar, finalizarsinnet;

    TextView tvPendiente;
    ConstraintLayout clInformativa;

    public NetworkChangeReceiver(LinearLayout llFirst, LinearLayout llSecond, ProgressBar pbInternet, int servicioxfinalizar, int finalizarsinnet, TextView tvPendiente, ConstraintLayout clInformativa){
        this.llFirst = llFirst;
        this.llSecond = llSecond;
        this.pbInternet = pbInternet;
        this.servicioxfinalizar = servicioxfinalizar;
        this.finalizarsinnet = finalizarsinnet;
        this.tvPendiente = tvPendiente;
        this.clInformativa = clInformativa;
    }
    @Override
    public void onReceive(Context context, Intent intent){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            if (servicioxfinalizar == 1 || finalizarsinnet == 1){

                //Hay conexion
                Toast.makeText(context, "HAY CONEXION DIRECTA", Toast.LENGTH_SHORT).show();
                llFirst.setVisibility(View.GONE);
                llSecond.setVisibility(View.GONE);
                pbInternet.setVisibility(View.VISIBLE);
                tvPendiente.setVisibility(View.VISIBLE);
                clInformativa.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pbInternet.setVisibility(View.GONE);
                        llFirst.setVisibility(View.VISIBLE);
                        llSecond.setVisibility(View.VISIBLE);

                        if(servicioxfinalizar == 1){

                            new AlertDialog.Builder(context)
                                    .setTitle("Servicio pendiente iniciado éxitosamente")
                                    .setMessage("El servicio que habias dejado pendiente se ha iniciado en el sistema correctamente.")
                                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            Intent i = new Intent(context, menu.class);
                                            context.startActivity(i);
                                        }
                                    })
                                    .show();
                        } else if (finalizarsinnet == 1) {
                            new AlertDialog.Builder(context)
                                    .setTitle("Servicio pendiente finalizado éxitosamente")
                                    .setMessage("El servicio que habias dejado pendiente se ha finalizado en el sistema correctamente.")
                                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            Intent i = new Intent(context, menu.class);
                                            context.startActivity(i);
                                        }
                                    })
                                    .show();
                        }
                    }
                }, 4000);
            }
        }else{
            //No hay conexion
        }
    }
}
