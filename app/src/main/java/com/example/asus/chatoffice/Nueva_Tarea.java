package com.example.asus.chatoffice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.asus.chatoffice.Adaptadores.adaptador_incisos;
import com.example.asus.chatoffice.Objetos.Tarea;
import com.example.asus.chatoffice.Reference.Reference;

import java.util.ArrayList;
import java.util.List;

public class Nueva_Tarea extends AppCompatActivity{

    Button addItem,aceptar,cancelar;
    EditText titulo, descripcion, item;
    List<Tarea.Inciso> incisos = new ArrayList<>();
    ListView lv_items ;
    Tarea tarea;
    adaptador_incisos adaptador;
    private Intent intent;

    public Nueva_Tarea() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva__tarea);


        intent = getIntent();

        titulo = findViewById(R.id.et_nueva_tarea);
        descripcion = findViewById(R.id.et_nueva_descripcion);
        item = findViewById(R.id.et_nuevo_items);
        lv_items = findViewById(R.id.lv_nuevo_items);
        aceptar = findViewById(R.id.bt_nuevo_aceptar);
        addItem = findViewById(R.id.bt_nuevo_item);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(item.getText().toString().isEmpty()) {

                   item.setError("Debe completar el campo");
               }
               else {
                   Tarea.Inciso inciso = new Tarea.Inciso(item.getText().toString(), false);
                   incisos.add(inciso);
                   item.setText("");
                   adaptador = new adaptador_incisos(getApplication().getApplicationContext(),incisos);
                   lv_items.setAdapter(adaptador);
//                   registerForContextMenu(lv_items);
//                   lv_items.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

               }
            }
        });


        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (campos_completos()){

                    tarea = new Tarea();
                    tarea.setTitulo(titulo.getText().toString());
                    tarea.setDescripcion(descripcion.getText().toString());
//                    tarea.setLista_incisos(incisos);
                    intent.putExtra(Reference.TAREA,tarea);
                    setResult(RESULT_OK, intent);
                    finish();

                }
            }
        });

    }



    private boolean campos_completos() {

        boolean correcto = true;

        if(titulo.getText().toString().isEmpty()){
            correcto = false;
            titulo.setError("Debe completar el campo");
        }
        if(descripcion.getText().toString().isEmpty()){

            correcto = false;
            descripcion.setError("Debe completar el campo");
        }
        if(incisos.size() == 0){

            correcto = false;
            Toast.makeText(getBaseContext(),"Debe agregar un items",Toast.LENGTH_LONG).show();
        }

        return correcto;
    }
}
