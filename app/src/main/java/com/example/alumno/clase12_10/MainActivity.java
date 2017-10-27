package com.example.alumno.clase12_10;

import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

        parsearJson();
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

            List<Persona> listaPersonas = parserXml(msg.obj.toString());

            for (Persona p : listaPersonas){
                Log.d("persona", "nombre: " + p.getNombre() + " edad: " + p.getEdad());
            }
        }
        if(msg.arg1 == 1){
            byte[] bytes = (byte[])msg.obj;

            ImageView imageView = (ImageView) findViewById(R.id.img1);
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        }

        return false;
    }

    public List<Persona> parserXml (String xml){

        List<Persona> lista = new ArrayList<Persona>();

        XmlPullParser parser = Xml.newPullParser();

        int event = 0;//trae el primer evento que ejecuta el xml (ejemplo: start_document)

        try {
            parser.setInput(new StringReader(xml));
            event = parser.getEventType();
        }
        catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        while (event != XmlPullParser.END_DOCUMENT){

            switch (event){

                case XmlPullParser.START_TAG: //cada evento que necesitemos hacer algo en el lo tenemos que poner el el case

                    if(parser.getName().equals("Persona")){
                        lista.add(new Persona(parser.getAttributeValue(null, "nombre"), Integer.valueOf(parser.getAttributeValue(null, "edad"))));
                    }
                    break;
            }

            try {
                event = parser.next();//trae el siguiente evento que ejecuta el xml
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lista;
    }

    public void parsearJson(){

        try {
            //JSONObject jsonObject = new JSONObject("[ {'nombre':'Juan', 'edad':25}, {'nombre':'Pedro', edad:22}, {'nombre':'Roberto', 'edad':33} ]");
            //JSONObject tambien puede contener un array de json porque los arrays son objetos

            JSONArray personas = new JSONArray("[ {'nombre':'Juan', 'edad':25}, {'nombre':'Pedro', edad:22}, {'nombre':'Roberto', 'edad':33} ]");

            for(int i=0; i<personas.length(); i++){

                JSONObject persona = personas.getJSONObject(i);
                String nombre = persona.getString("nombre");
                Integer edad = persona.getInt("edad");

                Log.d("Json Persona", "nombre: " + nombre + " edad: " + edad);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
