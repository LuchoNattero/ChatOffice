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

import com.example.asus.chatoffice.Adaptadores.Adaptador_mensajes;
import com.example.asus.chatoffice.FireBase.Reference_Fire_Base;
import com.example.asus.chatoffice.Objetos.Mensaje;
import com.example.asus.chatoffice.Objetos.Proyecto;
import com.example.asus.chatoffice.Objetos.Usuario;
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
    List<Mensaje>lista_historia = new ArrayList<>();
    Intent intent;
    Toolbar toolbar;
//    ArrayAdapter<String> itemsAdapter;
    Proyecto proyecto;
    FloatingActionButton fl_comunicado;
    Adaptador_mensajes adaptador;
    Usuario usuario;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__particular);

        intent = getIntent();
        toolbar = findViewById(R.id.tb_comunicado);

        fl_comunicado = findViewById(R.id.fl_comunicado_chat_particular);

        lv_historial = findViewById(R.id.lv_chat_particular);
        proyecto = (Proyecto) intent.getSerializableExtra(Reference.CHAT);

        adaptador = new Adaptador_mensajes(this.getApplicationContext(),lista_historia);
        lv_historial.setAdapter(adaptador);


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

                                lista_historia.add(msj);
                            }
                            adaptador.notifyDataSetChanged();
//                            itemsAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                  });
                  database.getReference(Reference_Fire_Base.REFERENCE_DATABASE_GUARDAR_USUARIO+"/"+FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                          usuario = dataSnapshot.getValue(Usuario.class);

                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError databaseError) {

                      }
                  });
            }
        });



        fl_comunicado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_chat = new Intent(getApplicationContext(),Pantilla_Comunicado.class);
                intent_chat.putExtra("usuario",usuario);
                startActivityForResult(intent_chat,1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1){

            if(resultCode == RESULT_OK){

                Mensaje comunicado = (Mensaje) data.getSerializableExtra(Reference.COMUNICADO);
                lista_historia.add(comunicado);

                DatabaseReference nuevoProyectoRef = database.getReference(Reference_Fire_Base.REFERENCE_DATABASE_MENSAJES+"/"+ proyecto.getTitulo()).child(Reference_Fire_Base.REFERENCE_DATABASE_HISTORIAL_MENSAJES);


                Map<String, Object> historalUpdate = new HashMap<>();

                historalUpdate.put(String.valueOf(lista_historia.size()),comunicado);
                nuevoProyectoRef.updateChildren(historalUpdate);

            }
            else if(resultCode == RESULT_CANCELED){



            }

        }

    }
}
