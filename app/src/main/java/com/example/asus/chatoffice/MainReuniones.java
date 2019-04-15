package com.example.asus.chatoffice;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.chatoffice.Adaptadores.AdaptadorReuniones;
import com.example.asus.chatoffice.FireBase.Reference_Fire_Base;
import com.example.asus.chatoffice.Objetos.Organizacion;
import com.example.asus.chatoffice.Objetos.Reunion;
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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class MainReuniones extends Fragment {
    View miInflater;
    ListView lv_reunion;
    FloatingActionButton fl_agregar_reunion;
    List<Reunion> lista_reunion= new ArrayList<>();
    AdaptadorReuniones adaptadorReuniones;
    Intent intent;
    TextView tv_nopReuniones;
    boolean esJedeOrganizacion = false;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ProgressDialog progressDialog_reuniones;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        miInflater = inflater.inflate(R.layout.activity_main_reuniones, container, false);

        fl_agregar_reunion = miInflater.findViewById(R.id.fl_add_reunion);
        tv_nopReuniones = miInflater.findViewById(R.id.tv_npreunion_main_reuniones);

        progressDialog_reuniones = new ProgressDialog(this.getContext());

        progressDialog_reuniones.setMessage("Cargando...");
        progressDialog_reuniones.setCancelable(false);
        progressDialog_reuniones.show();

        Handler hd_buscar_reuniones = new Handler();
        hd_buscar_reuniones.post(new Runnable() {
            @Override
            public void run() {
                database.getReference(Reference_Fire_Base.REFERENCE_DATABASE_GUARDAR_USUARIO+"/"+ FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                database.getReference(Reference_Fire_Base.REFERENCE_DATABASE_REUNIONES+"/"+ FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        lista_reunion.removeAll(lista_reunion);

                        for(DataSnapshot dataReuniones : dataSnapshot.getChildren()){

                            Reunion reunion = dataReuniones.getValue(Reunion.class);

                            lista_reunion.add(reunion);
                        }
                        poseReuniones();
                        adaptadorReuniones.notifyDataSetChanged();
                        progressDialog_reuniones.dismiss();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        fl_agregar_reunion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

/*                if(esJedeOrganizacion) {
                    intent = new Intent(getContext(), NuevaReunion.class);
                    startActivityForResult(intent, 0);
                }
                else {
                    intent = new Intent(getContext(),NuevaReunionPeticion.class);
                    startActivityForResult(intent, 0);
                }*/
                intent = new Intent(getContext(), NuevaReunion.class);
                startActivityForResult(intent, 0);
            }
        });
        adaptadorReuniones = new AdaptadorReuniones(getContext(), lista_reunion);

        lv_reunion = miInflater.findViewById(R.id.lv_reuniones);
        lv_reunion.setAdapter(adaptadorReuniones);
        registerForContextMenu(lv_reunion);
        lv_reunion.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv_reunion.setClickable(true);

        lv_reunion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final Reunion reunion = (Reunion) adapterView.getItemAtPosition(i);
                Button bt_aceptar_reunion = view.findViewById(R.id.bt_aceptar_reunion);
                Button bt_cancelar_reunion = view.findViewById(R.id.bt_cancelar_reunion);
                bt_aceptar_reunion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        createAdvertenciaDialogoReunion("Usted a aceptado la reunión",false,reunion);
                        adaptadorReuniones.notifyDataSetChanged();
                    }
                });

                bt_cancelar_reunion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        createAdvertenciaDialogoReunion("¿Estas seguro de rechazar la reunión?",true,reunion);
                        adaptadorReuniones.notifyDataSetChanged();
                    }
                });
            }
        });

        return miInflater;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 0)
        {
            if (resultCode == RESULT_OK){

                Reunion reunion = (Reunion) data.getSerializableExtra(Reference.NUEVA_REUNION);
                List<Usuario> usuarios_reunion = (List<Usuario>) data.getSerializableExtra(Reference.USUARIO);

                for (Usuario us : usuarios_reunion) {
                    DatabaseReference nuevoProyectoRef = database.getReference(Reference_Fire_Base.REFERENCE_DATABASE_REUNIONES + "/" + us.getSt_id());

                    Map<String, Object> ReunionUpdate = new HashMap<>();

                    reunion.setId(String.valueOf(reunion.getMotivo().hashCode() + reunion.getLugar().hashCode()));

                    ReunionUpdate.put(reunion.getId(), reunion);
                    nuevoProyectoRef.updateChildren(ReunionUpdate);

                    adaptadorReuniones.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Se acepto la nueva tarea", Toast.LENGTH_SHORT).show();
                }
            }
            else if(requestCode == RESULT_CANCELED)
            {
                Toast.makeText(getContext(), "Se cancelo la nueva tarea", Toast.LENGTH_SHORT).show();
            }
        }

        poseReuniones();
    }
    void poseReuniones(){
        if (lista_reunion.size() <= 0)
        {
            tv_nopReuniones.setVisibility(View.VISIBLE);
        }
        else
        {
            tv_nopReuniones.setVisibility(View.GONE);

        }

    }
    void actualizarEstadoReunion(Reunion reunion){

        DatabaseReference nuevoProyectoRef = database.getReference(Reference_Fire_Base.REFERENCE_DATABASE_REUNIONES+"/"+FirebaseAuth.getInstance().getUid());

        Map<String, Object> ReunionUpdate = new HashMap<>();

        ReunionUpdate.put(reunion.getId(),reunion);
        nuevoProyectoRef.updateChildren(ReunionUpdate);
    }
    public void createAdvertenciaDialogoReunion(String msj,boolean rechazar,final Reunion reunion) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());

        LayoutInflater inflater = this.getLayoutInflater();

        View v = inflater.inflate(R.layout.dialogo_advertencia_reunion, null);
        builder.setTitle("¡Advertencia!");
        builder.setMessage(msj);

        if(!rechazar) {
            builder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            reunion.setConfirmo();
                            actualizarEstadoReunion(reunion);
                            dialog.cancel();
                        }
                    });
        }
        else {
            builder.setPositiveButton("Si",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            reunion.setNoIra("Motivo");
                            actualizarEstadoReunion(reunion);
                            dialog.cancel();
                        }
                    });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        }

        builder.setView(v);


        builder.show();
    }
}