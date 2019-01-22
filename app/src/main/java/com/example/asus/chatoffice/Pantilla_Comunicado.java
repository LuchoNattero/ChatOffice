package com.example.asus.chatoffice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.asus.chatoffice.Objetos.Mensaje;
import com.example.asus.chatoffice.Reference.Reference;

public class Pantilla_Comunicado extends AppCompatActivity {

    EditText et_comunicado;
    Button bt_enviar,bt_cancelar;
    Intent intent_comunicado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantilla__comunicado);

        et_comunicado = findViewById(R.id.et_comunicado_pantilla_comunicado);
        bt_enviar  = findViewById(R.id.bt_enviar_plantilla_comunicado);
        bt_cancelar = findViewById(R.id.bt_cancelar_plantilla_comunicado);

         intent_comunicado = getIntent();

        bt_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!et_comunicado.getText().toString().isEmpty()) {
                    Mensaje msj = new Mensaje();
                    msj.setMensaje(et_comunicado.getText().toString());

                    intent_comunicado.putExtra(Reference.COMUNICADO,msj);
                    setResult(RESULT_OK, intent_comunicado);
                    finish();

                }
            }
        });
    }
}
