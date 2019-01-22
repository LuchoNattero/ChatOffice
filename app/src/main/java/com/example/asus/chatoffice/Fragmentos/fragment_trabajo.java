package com.example.asus.chatoffice.Fragmentos;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.chatoffice.Adaptadores.adaptador_lista_trabajo;
import com.example.asus.chatoffice.FireBase.Reference_Fire_Base;
import com.example.asus.chatoffice.MainRaelizarTarea;
import com.example.asus.chatoffice.Nueva_Tarea;
import com.example.asus.chatoffice.Objetos.Organizacion;
import com.example.asus.chatoffice.Objetos.Proyecto;
import com.example.asus.chatoffice.Objetos.Tarea;
import com.example.asus.chatoffice.Objetos.Usuario;
import com.example.asus.chatoffice.R;
import com.example.asus.chatoffice.Reference.Reference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class fragment_trabajo extends Fragment {

    View miInflater;
    private ListView listView;
    private adaptador_lista_trabajo adaptador;
    private List<Tarea> lista_tareas = new ArrayList<>();
    private List<Proyecto> lista_proyecto = new ArrayList<>();

    FloatingActionButton addTarea;
    TextView tv_nptareas;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ProgressDialog progressDialog_tareas;
    boolean esJedeOrganizacion = false;
    public fragment_trabajo() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        miInflater = inflater.inflate(R.layout.activity_lista_trabajo, container, false);

        tv_nptareas = miInflater.findViewById(R.id.tv_nptareas_lista_trabajo);
        listView = miInflater.findViewById(R.id.lv_trabajos);
        addTarea = miInflater.findViewById(R.id.fl_add_trabajo);

        progressDialog_tareas = new ProgressDialog(this.getContext());

        progressDialog_tareas.setMessage("Cargando...");
        progressDialog_tareas.setCancelable(false);
        progressDialog_tareas.show();


        Handler hd_buscar_tareas = new Handler();
        hd_buscar_tareas.post(new Runnable() {
            @Override
            public void run() {

                database.getReference(Reference_Fire_Base.REFERENCE_DATABASE_TAREAS+"/"+ FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        lista_tareas.removeAll(lista_tareas);

                        for(DataSnapshot dataTare : dataSnapshot.getChildren()){

                            Tarea tarea = dataTare.getValue(Tarea.class);

                            lista_tareas.add(tarea);
                        }
                        poseeTarea();
                        progressDialog_tareas.dismiss();
                        adaptador.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                database.getReference(Reference_Fire_Base.REFERENCE_DATABASE_GUARDAR_USUARIO+"/"+FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Usuario usuario = dataSnapshot.getValue(Usuario.class);

                        database.getReference(Reference_Fire_Base.REFERENCE_DATABASE_ORGANIZACION+"/"+usuario.getSt_idEmpresa()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Organizacion org = dataSnapshot.getValue(Organizacion.class);

                                if (org.getJefe().equals(FirebaseAuth.getInstance().getUid())){
                                    esJedeOrganizacion = true;
                                }
                                else {
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

                database.getReference(Reference_Fire_Base.REFERENCE_DATABASE_PROYECTO+"/"+FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        lista_proyecto.removeAll(lista_proyecto);
                        for(DataSnapshot data : dataSnapshot.getChildren()){

                            Proyecto pro = data.getValue(Proyecto.class);

                            if (pro.getAdministrador().getSt_id().equals(FirebaseAuth.getInstance().getUid())){

                                lista_proyecto.add(pro);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        addTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lista_proyecto.size() > 0) {
                    Intent intent = new Intent(getActivity(), Nueva_Tarea.class);
                    startActivityForResult(intent, 0);
                }
                else {
                    Toast.makeText(getContext(),"Debe estar acargo de proyectos",Toast.LENGTH_SHORT).show();
                }
            }
        });

        poseeTarea();
        adaptador = new adaptador_lista_trabajo(this.getContext(),lista_tareas);
        listView.setAdapter(adaptador);
        registerForContextMenu(listView);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Tarea tarea = (Tarea) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getContext(), MainRaelizarTarea.class);
                intent.putExtra(Reference.TAREA_REALIZAR,tarea);
                startActivityForResult(intent,1);
                
            }
        });
        return miInflater;
    }
       @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 0){

            if(resultCode == RESULT_OK){

                Toast.makeText(getContext(),"Se creo la tarea",Toast.LENGTH_SHORT).show();
            }
            else if(resultCode == RESULT_CANCELED){

                Toast.makeText(getContext(), "Se cancelo la nueva tarea", Toast.LENGTH_SHORT).show();
            }

        }
        if(requestCode == 1)
        {

        }
           poseeTarea();
    }

    void poseeTarea(){
        if (lista_tareas.size() <= 0)
        {
            tv_nptareas.setVisibility(View.VISIBLE);
        }
        else
        {
            tv_nptareas.setVisibility(View.GONE);

        }

    }


}
