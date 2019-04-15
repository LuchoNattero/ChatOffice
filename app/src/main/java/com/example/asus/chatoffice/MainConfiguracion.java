package com.example.asus.chatoffice;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.asus.chatoffice.FireBase.Reference_Fire_Base;
import com.example.asus.chatoffice.Objetos.Organizacion;
import com.example.asus.chatoffice.Objetos.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainConfiguracion extends AppCompatActivity {

//    EditText et_nombre,et_apellido;
//    AutoCompleteTextView    at_organizacion;
//    Button bt_cambiar_organizacion;
    List<Organizacion> lista_organizacion = new ArrayList<>();
    Usuario usuario;

    TextView tv_apellido,tv_nombre,tv_organizacion;

    FirebaseDatabase database;
    FirebaseAuth mAur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_configuracion);

        database = FirebaseDatabase.getInstance();
        mAur = FirebaseAuth.getInstance();

/*        et_apellido = findViewById(R.id.et_apellido_configuracion);
        et_nombre = findViewById(R.id.et_nombre_configuracion);
        at_organizacion = findViewById(R.id.at_organizacion_configuracion);
        bt_cambiar_organizacion = findViewById(R.id.bt_cambiar_organizacion_configuracion);*/


        tv_apellido = findViewById(R.id.tv_apellido_main_configuracion);
        tv_nombre = findViewById(R.id.tv_nombre_main_configuracion);
        tv_organizacion = findViewById(R.id.tv_organizacion_main_configuracion);


        database.getReference(Reference_Fire_Base.REFERENCE_DATABASE_ORGANIZACION).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_organizacion.removeAll(lista_organizacion);

                for(DataSnapshot data: dataSnapshot.getChildren()){

                    Organizacion org = data.getValue(Organizacion.class);
                    lista_organizacion.add(org);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        database.getReference(Reference_Fire_Base.REFERENCE_DATABASE_GUARDAR_USUARIO+"/"+mAur.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getKey().equals(mAur.getUid())){

                    usuario = dataSnapshot.getValue(Usuario.class);
                    tv_nombre.setText(usuario.getSt_nombre());
                    tv_apellido.setText(usuario.getSt_apallido());
                    tv_organizacion.setText(usuario.getSt_idEmpresa());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
/*        bt_cambiar_organizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createAdvertenciaCambiarOrganizacion();
            }
        });*/


//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,lista_organizacion);
//
//        at_organizacion.setThreshold(1);
//        at_organizacion.setAdapter(adapter);
    }
    private void sacarUsuarioOrganizacion(Usuario usuario) {
        database.getReference(Reference_Fire_Base.REFERENCE_DATABASE_ORGANIZACION+"/"+usuario.getSt_idEmpresa()+"/"+ Reference_Fire_Base.LISTA_MIEMBROS).child(usuario.getSt_id()).removeValue();
    }
    private void actualizarOrganizacion(Usuario usuario) {

            boolean bandera = false;
            int i = 0;
            while (!bandera) {
                if (lista_organizacion.get(i).getSt_id_organizacion().equals(tv_organizacion.getText().toString())) {

                    bandera = true;
                    Organizacion org = lista_organizacion.get(i);

                    List<String> list_aux = org.getLista_miembros_organizacion();
                    list_aux.add(usuario.getSt_id());
                    org.setLista_miembros_organizacion(list_aux);

                    crear_organizacion(org);
                }
                i++;
            }
    }
    private void actualizarUsuario(Usuario usuario){

        Map<String, Object> childUpdate = new HashMap<>();
        childUpdate.put(usuario.getSt_id(),usuario);

        database.getReference().child(Reference_Fire_Base.REFERENCE_DATABASE_GUARDAR_USUARIO).updateChildren(childUpdate);
    }
    private boolean existeOrganizacion(String organizacion) {

        boolean bandera = false;
        int i = 0;
        int tamanio = lista_organizacion.size();
//        Toast.makeText(getApplicationContext(),String.valueOf(lista_organizacion.size()),Toast.LENGTH_SHORT).show();
        while (!bandera && tamanio > i){

            if(lista_organizacion.get(i).getSt_id_organizacion().equals(organizacion)){

                bandera = true;
            }
            i++;
        }

        return bandera;
    }
    private void crear_organizacion(Organizacion org){


        Map<String, Object> childUpdate = new HashMap<>();
        childUpdate.put(org.getSt_id_organizacion(),org);

        database.getReference().child(Reference_Fire_Base.REFERENCE_DATABASE_ORGANIZACION).updateChildren(childUpdate);
    }
    public void createAdvertenciaCambiarOrganizacion() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        View v = inflater.inflate(R.layout.dialogo_verificacion, null);
        builder.setTitle("¡Advertencia!");
        builder.setMessage("¿Esta seguro de cambiar de organizacion?");
        builder.setCancelable(false);
        builder.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {

                        if(existeOrganizacion(tv_organizacion.getText().toString())){// se rompe cuando busca una organizacion que no esta
//                          Se qqueda colgado cuando se apreta "Si"
                            sacarUsuarioOrganizacion(usuario);
                            usuario.setSt_idEmpresa(tv_organizacion.getText().toString());
                            actualizarOrganizacion(usuario);
                            actualizarUsuario(usuario);
                        }
                        else {
                            createAdvertenciaMensaje();
                        }
                    }
                });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        builder.setView(v);


        builder.show();
    }
    public void createAdvertenciaMensaje() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        View v = inflater.inflate(R.layout.dialogo_verificacion, null);
        builder.setTitle("¡Atención!");
        builder.setMessage("La organización no existe");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                    }
                });

        builder.setView(v);


        builder.show();
    }
}
