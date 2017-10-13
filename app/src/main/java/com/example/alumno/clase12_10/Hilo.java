package com.example.alumno.clase12_10;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by alumno on 12/10/2017.
 */

public class Hilo implements Runnable{

    Handler handler;
    Message message;

    public Hilo(Handler handler, int valor){
        this.handler = handler;
        this.message = new Message();
        message.arg1 = valor;
    }

    @Override
    public void run() {
        try {
            if(message.arg1 == 0){
                byte[] bytes = getBytesData("http://www.lslutnfra.com/alumnos/practicas/listaPersonas.xml");
                message.obj = new String(bytes);//crea un string a traves de un array de bytes
                handler.sendMessage(message);
            }
            if (message.arg1 == 1){
                byte[] bytes2 = getBytesData("http://as00.epimg.net/img/comunes/fotos/fichas/equipos/large/1879.png");
                message.obj = bytes2;
                handler.sendMessage(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getBytesData (String strUrl) throws IOException {
        URL url = new URL(strUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();
        int response = urlConnection.getResponseCode();
        if(response == 200){
            InputStream is = urlConnection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = 0;
            while((length = is.read(buffer)) != -1){ //es != a -1 porque el metodo read() retorna -1 si termino de leer los bytes
                baos.write(buffer, 0, length);
                //si por ejemplo tratamos de leer 1025 bytes va a leer primero los 1024 bytes
                //y al leer el byte restante va a pasar ademas 1023 bytes de basura (porque el dato primitivo va a pasar siempre 1024 bytes)
                //por lo tanto en el metodo write() ademas de buffer se va a pasar la cantidad que tiene que leer (0, length)
            }
            is.close();
            return baos.toByteArray();
        }
        else{
            throw new IOException();
        }
    }
}
