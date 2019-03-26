package com.example.asus.chatoffice;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.asus.chatoffice.Adaptadores.adaptador_chat;
import com.example.asus.chatoffice.FireBase.Reference_Fire_Base;
import com.example.asus.chatoffice.Objetos.Organizacion;
import com.example.asus.chatoffice.Objetos.Proyecto;
import com.example.asus.chatoffice.Objetos.Usuario;
import com.example.asus.chatoffice.Reference.Reference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class Listado_Proyectos extends Fragment{

    TextView tv_nop_proyecto;
    adaptador_chat adapter;
    View miInflater;
    ListView listViewChat;
    List<Proyecto> lista_proyectos = new ArrayList<>();
    FloatingActionButton fl_add_chat;
    boolean esJedeOrganizacion = false;
    FirebaseUser user;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog_principal;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    public Listado_Proyectos() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        miInflater = inflater.inflate(R.layout.activity_chat, container, false);

        listViewChat = miInflater.findViewById(R.id.lv_chats);
        fl_add_chat = miInflater.findViewById(R.id.fl_add_chat);
        tv_nop_proyecto = miInflater.findViewById(R.id.tv_no_posee_proyecto);

        user = FirebaseAuth.getInstance().getCurrentUser();



        adapter = new adaptador_chat(getContext(), lista_proyectos);
        listViewChat.setAdapter(adapter);
        registerForContextMenu(listViewChat);
        listViewChat.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewChat.setClickable(true);


        progressDialog_principal = new ProgressDialog(this.getContext());
        progressDialog_principal.setMessage("Cargando...");
        progressDialog_principal.setCancelable(false);
        progressDialog_principal.show();

        Handler hd_buscar_jefe = new Handler();
        hd_buscar_jefe.post(new Runnable() {
            @Override
            public void run() {

                database.getReference(Reference_Fire_Base.REFERENCE_DATABASE_GUARDAR_USUARIO + "/" + FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Usuario usuario = dataSnapshot.getValue(Usuario.class);

                        database.getReference(Reference_Fire_Base.REFERENCE_DATABASE_ORGANIZACION + "/" + usuario.getSt_idEmpresa()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Organizacion org = dataSnapshot.getValue(Organizacion.class);

                                if (org.getJefe().equals(FirebaseAuth.getInstance().getUid())) {
                                    esJedeOrganizacion = true;
                                } else {
                                    esJedeOrganizacion = false;
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                database.getReference().child(Reference_Fire_Base.REFERENCE_DATABASE_PROYECTO + "/" + FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        lista_proyectos.removeAll(lista_proyectos);
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            Proyecto pro = snapshot.getValue(Proyecto.class);

                            lista_proyectos.add(pro);
                        }
                        poseeProyectos();
                        adapter.notifyDataSetChanged();
                        progressDialog_principal.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

        fl_add_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(esJedeOrganizacion) {
                    Intent intent = new Intent(getContext(), Nuevo_Proyecto.class);
                    startActivityForResult(intent, 1);
                }
                else{
                    Toast.makeText(getContext(),"Debe ser el administrador de la organizacion",Toast.LENGTH_SHORT).show();
                }
            }
        });



        listViewChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Proyecto item = (Proyecto) parent.getItemAtPosition(position);

                Intent intent = new Intent(getContext(), Proyecto_Particular.class);
                intent.putExtra(Reference.CHAT, item);
                startActivityForResult(intent, 0);
            }
        });

        return miInflater;
    }

    void poseeProyectos(){

        if (lista_proyectos.size() <= 0)
        {
            tv_nop_proyecto.setVisibility(View.VISIBLE);
        }
        else
        {
            tv_nop_proyecto.setVisibility(View.GONE);

        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1){

            if(resultCode == RESULT_OK){

                Proyecto proyecto = (Proyecto) data.getSerializableExtra(Reference.NUEVO_PROYECTO);

                asignar_proyecto(proyecto);

            }
            else if(resultCode == RESULT_CANCELED){

                Toast.makeText(getContext(), "Se cancelo el proyecto", Toast.LENGTH_SHORT).show();

            }

        }
        poseeProyectos();

    }
    void asignar_proyecto(Proyecto proyecto){

        for(String us : proyecto.getLista_usuarios()) {

            DatabaseReference nuevoProyectoRef = database.getReference(Reference_Fire_Base.REFERENCE_DATABASE_PROYECTO+"/"+us);

            // Se separo los proyectos del usuario, estan identificados por el id de los usuarios

            Map<String, Object> proyectoUpdate = new HashMap<>();

            proyectoUpdate.put(proyecto.getTitulo(),proyecto);
            nuevoProyectoRef.updateChildren(proyectoUpdate);

        }
    }
//    public String getJefe(){return this.esJedeOrganizacion;}

    @Override
    public void onSaveInstanceState(@Nullable Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // El bundle sera guardado y enviado al onCreate() de la Activity.
//        savedInstanceState.putString("esJefe", esJedeOrganizacion);

        savedInstanceState.putSerializable("lista_proyecto", (Serializable) lista_proyectos);
    }

/*    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
//            esJedeOrganizacion = savedInstanceState.getString("esJefe");
            lista_proyectos = (List<Proyecto>) savedInstanceState.getSerializable("lista_proyecto");

        }
    }*/
}
