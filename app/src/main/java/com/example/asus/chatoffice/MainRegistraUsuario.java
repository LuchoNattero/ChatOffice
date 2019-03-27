package com.example.asus.chatoffice;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.asus.chatoffice.FireBase.Reference_Fire_Base;
import com.example.asus.chatoffice.Objetos.Organizacion;
import com.example.asus.chatoffice.Objetos.Proyecto;
import com.example.asus.chatoffice.Objetos.Usuario;
import com.example.asus.chatoffice.Reference.Reference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainRegistraUsuario extends AppCompatActivity {
    EditText nombre,apellido,codigoId,et_email,et_pass,et_pass_repetir;
//    ListView lista_prueba;
//    ArrayAdapter<Organizacion> adaptador_SP_PRO;
    Switch tieneId;
    Button aceptar,cancelar;

    Boolean esAdministrador= false, correcto = true;
    List<Organizacion> list_organizaciones = new ArrayList<>();
    private ProgressDialog progressDialog;
    Usuario usuario;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    private FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_registra_usuario);

        database = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        database.getReference(Reference_Fire_Base.REFERENCE_DATABASE_ORGANIZACION).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_organizaciones.removeAll(list_organizaciones);

                for (DataSnapshot data : dataSnapshot.getChildren()){

                    Organizacion org = data.getValue(Organizacion.class);
                    list_organizaciones.add(org);
                }

//                adaptador_SP_PRO.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser usr = firebaseAuth.getCurrentUser();

                if(usr != null){

                    usuario = new Usuario(nombre.getText().toString(), apellido.getText().toString(), mAuth.getUid().toString(), codigoId.getText().toString());
                    if (esAdministrador) {

                        List<String> list_aux = new ArrayList<>();
                        list_aux.add(usuario.getSt_id());

                        Organizacion organizacion = new Organizacion(usuario.getSt_id(), list_aux, codigoId.getText().toString());
                        crear_organizacion(organizacion);


                    }
                    /*else {

                        String s = codigoId.getText().toString();
                        actualizar_miembros(s, usuario.getSt_id());

                    }*/
                    guardar_usuario(usuario);
                    createAdvertenciaVerificacion();

                }

            }

        };



        nombre = findViewById(R.id.et_nombre_registrar_usuario);
        apellido = findViewById(R.id.et_apellido_registrar_usuario);
        codigoId = findViewById(R.id.et_id_registrar_usuario);
        et_email = findViewById(R.id.et_email_registrar_usuario);
        et_pass = findViewById(R.id.et_contrasenia_registrar_usuario);
        et_pass_repetir = findViewById(R.id.et_contrasenia_repetir_registrar_usuario);

//       ----------------------------------------------
/*        lista_prueba = findViewById(R.id.lv_registrar_usuario);
        adaptador_SP_PRO =  new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,list_organizaciones);
        lista_prueba.setAdapter(adaptador_SP_PRO);*/

//        --------------------------------------------
        tieneId= findViewById(R.id.sh_pregunta_registrar_usuario);

        aceptar= findViewById(R.id.bt_aceptar_registrar_usuario);
        cancelar = findViewById(R.id.bt_cancelar_registrar_usuario);

        progressDialog = new ProgressDialog(this);

        tieneId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tieneId.isChecked()){

                    esAdministrador = true;
                }
                else{ esAdministrador= false;}

            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainRegistraUsuario.this,ActivityLogeoUsuario.class);
                startActivity(intent);
                finish();
            }
        });

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (campos_completos()) {

                    String st_email = et_email.getText().toString();
                    String st_pass = et_pass.getText().toString();

                    progressDialog.setMessage("Se esta creando su usuario");
                    progressDialog.show();

                    crearUsuarioFirebase(st_email,st_pass);

//                    if(esAdministrador){
//
//                        crearUsuarioFirebase(st_email,st_pass);
//
//                    }

/*                    else if(exiteOrganizacion(codigoId.getText().toString())) {

                        crearUsuarioFirebase(st_email,st_pass);

                    }else {
                        Toast.makeText(getApplicationContext(),"la organizacion no existe: "+ codigoId.getText().toString(),Toast.LENGTH_SHORT).show();
                    }*/
                    progressDialog.dismiss();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (authStateListener != null){
            mAuth.removeAuthStateListener(authStateListener);
        }

    }

    private boolean exiteOrganizacion(String s) {

        boolean bandera = false;
        int i = 0;
        while (!bandera && list_organizaciones.size() > i){

            if (list_organizaciones.get(i).getSt_id_organizacion().equals(codigoId.getText().toString())){
                bandera = true;
            }

            i++;
        }
        Toast.makeText(getApplicationContext(),String.valueOf(list_organizaciones.size()),Toast.LENGTH_SHORT).show();
        return bandera;
    }

    void actualizar_miembros(String s, String usuario) {
        boolean bandera = false;
        int i = 0;
        while (!bandera) {
            if (list_organizaciones.get(i).getSt_id_organizacion().equals(s)) {

                bandera = true;
                Organizacion org = list_organizaciones.get(i);

                List<String> list_aux = org.getLista_miembros_organizacion();
                list_aux.add(usuario);
                org.setLista_miembros_organizacion(list_aux);

                crear_organizacion(org);

            }
            i++;
        }


    }

    void guardar_usuario(Usuario usuario){

        Map<String, Object> childUpdate = new HashMap<>();
        childUpdate.put(usuario.getSt_id(),usuario);

        databaseReference.child(Reference_Fire_Base.REFERENCE_DATABASE_GUARDAR_USUARIO).updateChildren(childUpdate);
    }

    void crear_organizacion(Organizacion org){


        Map<String, Object> childUpdate = new HashMap<>();
        childUpdate.put(org.getSt_id_organizacion(),org);

        databaseReference.child(Reference_Fire_Base.REFERENCE_DATABASE_ORGANIZACION).updateChildren(childUpdate);
    }

    private boolean campos_completos() {

        boolean correcto = true;

        if(nombre.getText().toString().isEmpty()){
            correcto = false;
            nombre.setError("Debe completar el campo");
        }
        if(apellido.getText().toString().isEmpty()){

            correcto = false;
            apellido.setError("Debe completar el campo");
        }
        if(esAdministrador){
            if(codigoId.getText().toString().isEmpty()){

            correcto = false;
            codigoId.setError("Debe completar el campo");
            }
        }
        if(et_email.getText().toString().isEmpty()){

            correcto = false;
            et_email.setError("Debe completar el campo");
        }
        if(et_pass.getText().toString().isEmpty()){

            correcto = false;
            et_pass.setError("Debe completar el campo");
        }
        if (et_pass_repetir.getText().toString().isEmpty()){
            correcto = false;
            et_pass_repetir.setError("Debe completar el campo");
        }

        if (correcto){

            if (!et_pass.getText().toString().equals(et_pass.getText().toString())){
                correcto = false;
                et_pass.setError("Las contraseñas deben ser iguales");
                et_pass_repetir.setError("Las contraseñas deben ser iguales");

            }

        }


        return correcto;
    }

    public void createAdvertenciaVerificacion() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        View v = inflater.inflate(R.layout.dialogo_verificacion, null);
        builder.setTitle("¡Advertencia!");
        builder.setMessage("Primero tiene que verificar el correo");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {

                            Intent intent = new Intent(MainRegistraUsuario.this, MainActivity.class);
                            startActivity(intent);
                            dialog.cancel();
                            finish();

                    }
                });

        builder.setView(v);


        builder.show();
    }

    void crearUsuarioFirebase(String st_email,String st_pass){

        mAuth.createUserWithEmailAndPassword(st_email, st_pass)
                .addOnCompleteListener(MainRegistraUsuario.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {// si se produce una colision

                            et_email.setError("Ese email ya ha sido registrado");
                        } else {

                            if (!task.isSuccessful()) {
                                et_email.setError("Ingrese correctamente el email");
                            } else {

                                FirebaseUser usr = mAuth.getCurrentUser();
                                usr.sendEmailVerification();
                            }
                        }

                    }
                });

    }


}
