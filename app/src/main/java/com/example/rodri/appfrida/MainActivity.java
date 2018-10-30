package com.example.rodri.appfrida;

import android.content.Intent;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.text.Normalizer;


public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final int RECONOCEDOR_VOZ =15;
    private TextView escuchando;
    private  TextView respuesta;
    private ArrayList<Respuestas> respuest;
    private TextToSpeech leer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== RESULT_OK && requestCode == RECONOCEDOR_VOZ){
            ArrayList<String> reconocido= data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String escuchado = reconocido.get(0);
            escuchando.setText(escuchado);
            prepararRespuesta(escuchado);
        }
    }
    private void prepararRespuesta(String escuchado) {
        String normalizar = Normalizer.normalize(escuchado, Normalizer.Form.NFD);
        String sintilde = normalizar.replaceAll("[^\\p{ASCII}]", "");

        int resultado;
        String respuesta = respuest.get(0).getRespuestas();
        for (int i = 0; i < respuest.size(); i++) {
            resultado = sintilde.toLowerCase().indexOf(respuest.get(i).getCuestion());
            if(resultado != -1){
                respuesta = respuest.get(i).getRespuestas();
            }
        }
        responder(respuesta);
    }

    private void responder(String respuestita) {
        respuesta.setText(respuestita);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            leer.speak(respuestita, TextToSpeech.QUEUE_FLUSH, null, null);
        }else {
            leer.speak(respuestita, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    public void inicializar(){
        escuchando = (TextView)findViewById(R.id.tvEscuchado);
        respuesta = (TextView)findViewById(R.id.tvRespuesta);
        respuest = proveerDatos();
        leer = new TextToSpeech(this,this);
    }

    public void hablar(View view) {
        Intent hablar = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        hablar.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,"es-MX");
        startActivityForResult(hablar,RECONOCEDOR_VOZ);

    }
    public ArrayList<Respuestas> proveerDatos(){
        ArrayList<Respuestas> respuestas = new ArrayList<>();
        respuestas.add(new Respuestas("defecto", "¡Aun no estoy programada para responder eso, lo siento!"));
        respuestas.add(new Respuestas("hola", "hola que tal"));
        respuestas.add(new Respuestas("chiste", "¿Qué le dice una piedra a otra piedra?\n" + "-La vida es dura."));
        respuestas.add(new Respuestas("adios", "que descanses "));
        respuestas.add(new Respuestas("como estas", "esperando serte de ayuda"));
        respuestas.add(new Respuestas("nesecito","hay restaurantes en tu zona en los que puedes ir "));
        respuestas.add(new Respuestas("nombre", "mis amigos me llaman Frida"));
        return respuestas;
    }

    @Override
    public void onInit(int status) {

    }

}
