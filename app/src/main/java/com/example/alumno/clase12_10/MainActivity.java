package com.example.alumno.clase12_10;

import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements Handler.Callback {

    Thread t1;
    Thread t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler handler = new Handler(this);//Para crear el Handler hay que implementar Callback
        //para pasar datos de un hilo al hilo del MainActivity hay que crear un handler

        Hilo hilo1 = new Hilo(handler, 0);
        Hilo hilo2 = new Hilo(handler, 1);
        t1 = new Thread(hilo1);
        t2 = new Thread(hilo2);
        t1.start();
        t2.start();
        //para obtener 2 mensajes distintos creo 2 hilos
    }

    @Override
    protected void onStop(){
        t1.interrupt();//ponemos interrupt() en el metodo onStop() para que cierre el hilo cuando cerremos la aplicacion
    }


    @Override
    public boolean handleMessage(Message msg) {

        //Log.d("texto", msg.obj.toString());
        if(msg.arg1 == 0) {
            TextView textView = (TextView) findViewById(R.id.txt1);
            textView.setText(msg.obj.toString());
        }
        if(msg.arg1 == 1){
            byte[] bytes = (byte[])msg.obj;

            ImageView imageView = (ImageView) findViewById(R.id.img1);
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        }

        return false;
    }
}
