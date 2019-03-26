package com.example.asus.chatoffice;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.asus.chatoffice.Adaptadores.adaptador_incisos;
import com.example.asus.chatoffice.FireBase.Reference_Fire_Base;
import com.example.asus.chatoffice.Objetos.Proyecto;
import com.example.asus.chatoffice.Objetos.Tarea;
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

public class Nueva_Tarea extends AppCompatActivity implements AdapterView.OnItemClickListener{
    Button aceptar,cancelar;
    EditText titulo, descripcion, item;
    FloatingActionButton fl_addItem;

    ListView lv_items, lv_usuarios;
    Tarea tarea;

    Spinner sp_prioridad,sp_proyecto;

    ArrayAdapter<Usuario> adaptador_SP_INT;
    ArrayAdapter<Proyecto> adaptador_SP_PRO;
    ArrayAdapter<Tarea.Inciso> adaptador_items;

    List<Proyecto>lista_proyectos = new ArrayList<>();
    List<Usuario>lista_integrantes = new ArrayList<>();
    List<Tarea.Inciso> incisos = new ArrayList<>();
    Usuario usuarioTarea = new Usuario();

    Toolbar tb_nueva_tarea;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private Intent intent;

    public Nueva_Tarea() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva__tarea);


        intent = getIntent();

        titulo = findViewById(R.id.et_tituo_nueva_tarea);
        descripcion = findViewById(R.id.et_descripcion_nueva_tarea);
        item = findViewById(R.id.et_item_nueva_tarea);
        lv_items = findViewById(R.id.lv_items_nueva_tarea);
//        aceptar = findViewById(R.id.bt_aceptar_nueva_tarea);
        fl_addItem = findViewById(R.id.fl_add_nueva_tarea);
        sp_prioridad = findViewById(R.id.sp_prioridad_nueva_tarea);
        lv_usuarios = findViewById(R.id.lv_usuario_tarea_nueva_tarea);
        sp_proyecto = findViewById(R.id.sp_asignar_proyecto_nueva_tarea);

        tb_nueva_tarea = findViewById(R.id.tb_nueva_tarea);
        setSupportActionBar(tb_nueva_tarea);

        adaptador_SP_PRO =  new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,lista_proyectos);
        adaptador_SP_INT = new ArrayAdapter<>(this,android.R.layout.simple_list_item_single_choice,lista_integrantes);
        adaptador_items = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,incisos);

        ArrayAdapter<CharSequence> adaptador_prioridad = ArrayAdapter.createFromResource(this, R.array.prioridad, android.R.layout.simple_spinner_item);


        sp_prioridad.setAdapter(adaptador_prioridad);
        sp_proyecto.setAdapter(adaptador_SP_PRO);


        buscarProyecto();

        sp_proyecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Proyecto proyecto = (Proyecto) sp_proyecto.getItemAtPosition(i);

                database.getReference().child(Reference_Fire_Base.REFERENCE_DATABASE_PROYECTO+"/"+FirebaseAuth.getInstance().getUid()+"/"+proyecto.getTitulo()).child(Reference_Fire_Base.REFERENCE_DATABASE_LISTA_USUARIOS).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        lista_integrantes.removeAll(lista_integrantes);

                        for (DataSnapshot snapshot: dataSnapshot.getChildren()){

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
                        adaptador_SP_INT.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
            });

        fl_addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(item.getText().toString().isEmpty()) {

                   item.setError("Debe completar el campo");
               }
               else {
                   Tarea.Inciso inciso = new Tarea.Inciso(item.getText().toString(), false);
                   incisos.add(inciso);
                   item.setText("");
                   lv_items.setAdapter(adaptador_items);

               }
            }
        });

        lv_usuarios.setAdapter(adaptador_SP_INT);
        lv_usuarios.setOnItemClickListener(this);
        lv_usuarios.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_action_nueva_reunion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.act_tool_aceptar:

                if (campos_completos()){

                    tarea = new Tarea(titulo.getText().toString(), "Prioridad: "+sp_prioridad.getSelectedItem().toString(),descripcion.getText().toString(), (String.valueOf(titulo.getText().toString().hashCode()+sp_prioridad.getSelectedItem().toString().hashCode())), incisos, null);

                    String usuario = usuarioTarea.getSt_id();
                    DatabaseReference nuevaTareaRef = database.getReference(Reference_Fire_Base.REFERENCE_DATABASE_TAREAS+"/"+ usuario);

                    Map<String, Object> tareaUpdate = new HashMap<>();

                    tareaUpdate.put(tarea.getId(),tarea);
                    nuevaTareaRef.updateChildren(tareaUpdate);

                    setResult(RESULT_OK, intent);
                    finish();

                }

                return true;
            case R.id.act_tool_cancelar:

                setResult(RESULT_CANCELED);
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        if(usuarioTarea.equals(null)){

            correcto = false;
            Toast.makeText(getBaseContext(),"Debe seleccionar un usuario",Toast.LENGTH_LONG).show();
        }

        return correcto;
    }


    void buscarProyecto(){

        database.getReference().child(Reference_Fire_Base.REFERENCE_DATABASE_PROYECTO+"/"+FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lista_proyectos.removeAll(lista_proyectos);

                for (DataSnapshot dataProyect : dataSnapshot.getChildren()){

                    lista_proyectos.add(dataProyect.getValue(Proyecto.class));
                }
                adaptador_SP_PRO.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        usuarioTarea = (Usuario) lv_usuarios.getItemAtPosition(i);

    }
}
