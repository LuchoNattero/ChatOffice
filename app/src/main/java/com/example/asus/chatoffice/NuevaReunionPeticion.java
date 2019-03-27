package com.example.asus.chatoffice;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.asus.chatoffice.FireBase.Reference_Fire_Base;
import com.example.asus.chatoffice.Objetos.Reunion;
import com.example.asus.chatoffice.Objetos.Usuario;
import com.example.asus.chatoffice.Reference.Reference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NuevaReunionPeticion extends AppCompatActivity implements AdapterView.OnItemClickListener{

    Intent intent;
    Spinner sp_horario;
    EditText motivo, dia, lugar;
    Button aceptar,cancelar;
    ListView lv_integrantes;
    Reunion reunion = new Reunion();
    ArrayAdapter<Usuario> adaptador_LV;
    List<Usuario> lista_integrantes = new ArrayList<>();
    List<Usuario> lista_selecionado_usuarios = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_reunion);

        intent = getIntent();

        lv_integrantes = findViewById(R.id.lv_integrantes_nueva_reunion);
        motivo = findViewById(R.id.et_motivo_nueva_reunion);
        dia = findViewById(R.id.et_dia_nueva_reunion);
        lugar = findViewById(R.id.et_lugar_nueva_reunion);
        sp_horario = findViewById(R.id.sp_horarios_nueva_reunion);

        ArrayAdapter<CharSequence> adaptador_horario = ArrayAdapter.createFromResource(this, R.array.horarios, android.R.layout.simple_spinner_item);
        sp_horario.setAdapter(adaptador_horario);

        aceptar = findViewById(R.id.bt_aceptar_nueva_reunion);
        cancelar = findViewById(R.id.bt_cancelar_nueva_reunion);

        adaptador_LV = new ArrayAdapter<>(this,android.R.layout.simple_list_item_multiple_choice, lista_integrantes);

        buscarUsuarios();
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(campos_completos()){


                    reunion.setMotivo(motivo.getText().toString());
                    reunion.setHora(sp_horario.getSelectedItem().toString());
                    reunion.setLugar(lugar.getText().toString());
                    intent.putExtra(Reference.NUEVA_REUNION,reunion);
                    intent.putExtra(Reference.USUARIO, (Serializable) lista_selecionado_usuarios);
                    setResult(RESULT_OK,intent);
                    finish();

                }


            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setResult(RESULT_CANCELED);
                finish();

            }
        });


        lv_integrantes.setAdapter(adaptador_LV);
        lv_integrantes.setOnItemClickListener(this);
        lv_integrantes.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }
    void  buscarUsuarios(){

        database.getReference(Reference_Fire_Base.REFERENCE_DATABASE_GUARDAR_USUARIO+"/"+ FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getUid())) {

                    Usuario usuario = dataSnapshot.getValue(Usuario.class);

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
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private boolean campos_completos() {

        boolean correcto = true;

        if(motivo.getText().toString().isEmpty()){
            correcto = false;
            motivo.setError("Debe completar el campo");
        }
        if(dia.getText().toString().isEmpty()){

            correcto = false;
            dia.setError("Debe completar el campo");
        }
        if(lugar.getText().toString().isEmpty()){

            correcto = false;
            lugar.setError("Debe completar el campo");
        }
        if(sp_horario.getSelectedItem().toString().isEmpty()){

            correcto = false;
            Toast.makeText(getApplicationContext(),"Debe selecionar un horario",Toast.LENGTH_LONG).show();
        }

        if (correcto){


            try{
                SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM");
                Date date = simpleDate.parse(dia.getText().toString());
                reunion.setDia(date);
            }catch(ParseException ex){
                dia.setError("El formato debe ser dd/mm");
                correcto=false;
            }
        }

        return correcto;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Usuario usuario_seleccionado = (Usuario) lv_integrantes.getItemAtPosition(i);

        lista_selecionado_usuarios.add(usuario_seleccionado);

    }
}
