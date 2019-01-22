package com.example.asus.chatoffice.Fragmentos;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.asus.chatoffice.FireBase.Reference_Fire_Base;
import com.example.asus.chatoffice.MainRaelizarTarea;
import com.example.asus.chatoffice.Objetos.Tarea;
import com.example.asus.chatoffice.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Fragment_Comentarios extends Fragment {

    View vw_inflater;
    Tarea tarea;
    ListView lv_comentarios;
    EditText et_comentario;
    FloatingActionButton fl_agregarComentario;
    List<String> list_comentarios = new ArrayList<>();
    ArrayAdapter<String> adaptador_comentarios;
FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        vw_inflater = inflater.inflate(R.layout.fragment_fragment__comentarios, container, false);
        tarea = MainRaelizarTarea.getTarea();

//        list_comentarios = tarea.getLista_comentarios();
        lv_comentarios = vw_inflater.findViewById(R.id.lv_comentarios_fragment_comentarios);
        et_comentario = vw_inflater.findViewById(R.id.et_comentario_fragment_comentarios);
        fl_agregarComentario = vw_inflater.findViewById(R.id.fl_enviar_comentario_fragment_comentarios);

        database.getReference(Reference_Fire_Base.REFERENCE_DATABASE_TAREAS+"/"+FirebaseAuth.getInstance().getUid()+"/"+tarea.getId()+"/"+Reference_Fire_Base.REFERENCE_DATABASE_COMENTARIOS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_comentarios.removeAll(list_comentarios);
                for (DataSnapshot data : dataSnapshot.getChildren()){

                    String comentario = data.getValue(String.class);
                    list_comentarios.add(comentario);
                }
                adaptador_comentarios.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adaptador_comentarios = new ArrayAdapter<String>(vw_inflater.getContext(), android.R.layout.simple_list_item_1,list_comentarios );
        lv_comentarios.setAdapter(adaptador_comentarios);

        fl_agregarComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!et_comentario.getText().toString().isEmpty())
                {


                    Map<String, Object> childUpdate = new HashMap<>();
                    childUpdate.put(String.valueOf(list_comentarios.size()),et_comentario.getText().toString());
                    database.getReference().child(Reference_Fire_Base.REFERENCE_DATABASE_TAREAS+"/"+ FirebaseAuth.getInstance().getUid()+"/"+tarea.getId()+"/"+Reference_Fire_Base.REFERENCE_DATABASE_COMENTARIOS).updateChildren(childUpdate);

                    et_comentario.setText("");
                }
            }
        });


        return vw_inflater;
    }
}
