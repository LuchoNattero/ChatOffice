package com.example.asus.chatoffice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

public class CrearUsuario extends AppCompatActivity {

    EditText nombre,apellido,codigoId;
    Switch tieneId;
    Button aceptar,cancelar,generarId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_usuario);

        nombre = findViewById(R.id.et_nombre);
        apellido = findViewById(R.id.et_apellido);
        codigoId = findViewById(R.id.et_id);

        tieneId= findViewById(R.id.sh_pregunta);

        aceptar= findViewById(R.id.bt_aceptar_usu);
        cancelar = findViewById(R.id.bt_cancelar);
        generarId = findViewById(R.id.bt_id);

        tieneId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tieneId.getTextOn().equals("On")){
                    codigoId.setEnabled(false);
                    generarId.setEnabled(true);
                }
                else {

                    codigoId.setEnabled(true);
                    generarId.setEnabled(false);
                }

            }
        });


    }
}
