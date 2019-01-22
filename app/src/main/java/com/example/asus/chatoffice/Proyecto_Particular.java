package com.example.asus.chatoffice;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.asus.chatoffice.FireBase.Reference_Fire_Base;
import com.example.asus.chatoffice.Objetos.Mensaje;
import com.example.asus.chatoffice.Objetos.Proyecto;
import com.example.asus.chatoffice.Reference.Reference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Proyecto_Particular extends AppCompatActivity {

    ListView lv_historial;
    List<String>lista_historia = new ArrayList<>();
    Intent intent;
    Toolbar toolbar;
    Button bt_comunicado;
    ArrayAdapter<String> itemsAdapter;
    Proyecto proyecto;
    ProgressDialog progressDialog_principal;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__particular);

        intent = getIntent();
        toolbar = findViewById(R.id.tb_comunicado);
        bt_comunicado = findViewById(R.id.bt_comunicado_chat_particular);
        lv_historial = findViewById(R.id.lv_chat_particular);
        proyecto = (Proyecto) intent.getSerializableExtra(Reference.CHAT);

        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista_historia);
        lv_historial.setAdapter(itemsAdapter);

//        progressDialog_principal = new ProgressDialog(this.getApplicationContext());
//        progressDialog_principal.setMessage("Cargando...");
//        progressDialog_principal.setCancelable(false);
//        progressDialog_principal.show();

        toolbar.setTitle(proyecto.getTitulo());
        toolbar.setTitleTextColor(R.color.colorBlanco);

        Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {

                  database.getReference(Reference_Fire_Base.REFERENCE_DATABASE_MENSAJES+"/"+ proyecto.getTitulo()).child(Reference_Fire_Base.REFERENCE_DATABASE_HISTORIAL_MENSAJES).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            lista_historia.removeAll(lista_historia);

                            for(DataSnapshot dataMSJ : dataSnapshot.getChildren()){

                                Mensaje msj = dataMSJ.getValue(Mensaje.class);

                                lista_historia.add(msj.getMensaje());
                            }

                            itemsAdapter.notifyDataSetChanged();
//                            progressDialog_principal.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                  });
            }
        });



        bt_comunicado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent_chat = new Intent(getApplicationContext(),Pantilla_Comunicado.class);
                startActivityForResult(intent_chat,1);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1){

            if(resultCode == RESULT_OK){

                Mensaje comunicado = (Mensaje) data.getSerializableExtra(Reference.COMUNICADO);
                lista_historia.add(comunicado.getMensaje());

                DatabaseReference nuevoProyectoRef = database.getReference(Reference_Fire_Base.REFERENCE_DATABASE_MENSAJES+"/"+ proyecto.getTitulo()).child(Reference_Fire_Base.REFERENCE_DATABASE_HISTORIAL_MENSAJES);

                // Se separo los proyectos del usuario, estan identificados por el id de los usuarios


                Map<String, Object> historalUpdate = new HashMap<>();

                historalUpdate.put(String.valueOf(lista_historia.size()),comunicado);
                nuevoProyectoRef.updateChildren(historalUpdate);

            }
            else if(resultCode == RESULT_CANCELED){



            }

        }

    }
}
