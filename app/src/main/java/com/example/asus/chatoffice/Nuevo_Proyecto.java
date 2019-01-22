package com.example.asus.chatoffice;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.example.asus.chatoffice.FireBase.Reference_Fire_Base;
import com.example.asus.chatoffice.Objetos.Proyecto;
import com.example.asus.chatoffice.Objetos.Usuario;
import com.example.asus.chatoffice.Reference.Reference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Nuevo_Proyecto extends AppCompatActivity implements AdapterView.OnItemClickListener{
    Intent intent;
    EditText et_nombre_proyecto;
    ListView lv_integrantes;
    List<Usuario> lista_integrantes = new ArrayList<>();

    ListView lv_administrador;
    ArrayAdapter<Usuario> adaptador_LV;
    ArrayAdapter<Usuario> adaptador_SP;
    Usuario administrador = new Usuario();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    List<String> lista_selecionado_usuarios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo__proyecto);

        intent = getIntent();
        Button bt_aceptar,bt_cancelar;



        et_nombre_proyecto = findViewById(R.id.et_nombre_proyecto_nuevo_proyecto);
        lv_integrantes = findViewById(R.id.lv_integrantes_nuevo_proyecto);
        lv_administrador = findViewById(R.id.lv_administrador_nuevo_proyecto);


        bt_aceptar = findViewById(R.id.bt_aceptar_nuevo_proyecto);
        bt_cancelar = findViewById(R.id.bt_cancelar_nuevo_proyecto);

//        Toast.makeText(getApplicationContext(),FirebaseAuth.getInstance().getUid(),Toast.LENGTH_SHORT).show();

//        buscarUsuario();
//        -------------------------------------------BUSCAR USUARIO-------------------------------------------------------------------------
        database.getReference().child(Reference_Fire_Base.REFERENCE_DATABASE_GUARDAR_USUARIO+"/"+FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getUid())){

                    Usuario usuario = dataSnapshot.getValue(Usuario.class); // al agregar el lis_proyectos se rompre, se tiene q ver como tomar el dato

                    database.getReference(Reference_Fire_Base.REFERENCE_DATABASE_ORGANIZACION).child(usuario.getSt_idEmpresa()).child(Reference_Fire_Base.LISTA_MIEMBROS).addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot data) {

                            lista_integrantes.removeAll(lista_integrantes);

                            for (DataSnapshot snapshot: data.getChildren()){

                                String us = snapshot.getValue().toString();

                                database.getReference().child(Reference_Fire_Base.REFERENCE_DATABASE_GUARDAR_USUARIO+"/"+us).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        lista_integrantes.add(dataSnapshot.getValue(Usuario.class));
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });
                }
                adaptador_LV.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        ------------------------------------------FIN BUSCAR USUARIO-------------------------------------------------------------------------


        adaptador_SP = new ArrayAdapter<>(this,android.R.layout.simple_list_item_single_choice,lista_integrantes);
        adaptador_LV = new ArrayAdapter<>(this,android.R.layout.simple_list_item_multiple_choice, lista_integrantes);

        lv_administrador.setAdapter(adaptador_SP);
        lv_administrador.setOnItemClickListener(this);
        lv_administrador.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        lv_integrantes.setAdapter(adaptador_LV);
        lv_integrantes.setOnItemClickListener(this);
        lv_integrantes.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        lv_administrador.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                administrador = (Usuario) adapterView.getItemAtPosition(i);
            }
        });



        bt_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(campos_completos())
                {
                    Proyecto nuevo_proyecto = new Proyecto();

                    nuevo_proyecto.setAdministrador(administrador);
                    nuevo_proyecto.setLista_usuarios(lista_selecionado_usuarios);
                    nuevo_proyecto.setTitulo(et_nombre_proyecto.getText().toString());

                    intent.putExtra(Reference.NUEVO_PROYECTO,nuevo_proyecto);
                    setResult(RESULT_OK,intent);
                    finish();

                }
            }
        });
        bt_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }

    private boolean campos_completos() {

        boolean correcto = true;

        if(et_nombre_proyecto.getText().toString().isEmpty()){
            correcto = false;
            et_nombre_proyecto.setError("Debe completar el campo");
        }
        if(lista_integrantes.size() == 0){

            correcto = false;
            Toast.makeText(getBaseContext(),"Debe agregar integrantes",Toast.LENGTH_LONG).show();
        }
        if (administrador == null){

            correcto = false;
            Toast.makeText(getBaseContext(),"Debe agregar el lider",Toast.LENGTH_LONG).show();
        }

        return correcto;
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Usuario usuario_seleccionado = (Usuario) lv_integrantes.getItemAtPosition(i);
        lista_selecionado_usuarios.add(usuario_seleccionado.getSt_id());

        // posible error, que se agreguen cuando se tilda y se destilda


    }


    void buscarUsuario(){

        database.getReference().child(Reference_Fire_Base.REFERENCE_DATABASE_GUARDAR_USUARIO+"/"+FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getUid())){



                    Usuario usuario = dataSnapshot.getValue(Usuario.class); // al agregar el lis_proyectos se rompre, se tiene q ver como tomar el dato


                    database.getReference(Reference_Fire_Base.REFERENCE_DATABASE_ORGANIZACION).child(usuario.getSt_idEmpresa()).child(Reference_Fire_Base.LISTA_MIEMBROS).addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot data) {

                            lista_integrantes.removeAll(lista_integrantes);

                            for (DataSnapshot snapshot: data.getChildren()){

                                String us = snapshot.getValue().toString();

                                database.getReference().child(Reference_Fire_Base.REFERENCE_DATABASE_GUARDAR_USUARIO+"/"+us).addListenerForSingleValueEvent(new ValueEventListener() {
                                         @Override
                                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                             lista_integrantes.add(dataSnapshot.getValue(Usuario.class));


                                         }

                                         @Override
                                         public void onCancelled(@NonNull DatabaseError databaseError) {

                                         }
                                     });

                            }
                            adaptador_LV.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
