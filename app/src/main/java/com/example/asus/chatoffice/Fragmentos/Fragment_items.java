package com.example.asus.chatoffice.Fragmentos;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.asus.chatoffice.FireBase.Reference_Fire_Base;
import com.example.asus.chatoffice.MainRaelizarTarea;
import com.example.asus.chatoffice.Objetos.Tarea;
import com.example.asus.chatoffice.Objetos.Usuario;
import com.example.asus.chatoffice.R;
import com.google.android.gms.flags.IFlagProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment_items extends Fragment implements AdapterView.OnItemClickListener{

    TextView tv_titulo,tv_prioridad;
    View vw_inflater;
    List<Tarea> lista_tarea = new ArrayList<>();
    List<Tarea.Inciso> lista_insiso = new ArrayList<>();
    ListView lv_lista_insisos;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ArrayAdapter<Tarea.Inciso> adaptador_insisos;
    List<Map<Integer,Object>> cambiar_insiso = new ArrayList<>();
    Tarea tarea;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vw_inflater = inflater.inflate(R.layout.fragment_fragment_items, container, false);
        tarea = MainRaelizarTarea.getTarea();

        tv_titulo = vw_inflater.findViewById(R.id.tv_titulo_tarea_realizar);
        tv_prioridad = vw_inflater.findViewById(R.id.tv_prioridad_tarea_realizar);
        lv_lista_insisos = vw_inflater.findViewById(R.id.lv_items_tarea_realizar);

        lista_insiso.removeAll(lista_insiso);
        lista_insiso.addAll(tarea.getLista_incisos());

        adaptador_insisos = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_multiple_choice,lista_insiso);

        lv_lista_insisos.setAdapter(adaptador_insisos);
        lv_lista_insisos.setOnItemClickListener(this);
        lv_lista_insisos.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        for(int i = 0; i < lista_insiso.size(); i++){

            lv_lista_insisos.setItemChecked(i,lista_insiso.get(i).isCheck());
        }

        tv_prioridad.setText(tarea.getPrioridad());
        tv_titulo.setText(tarea.getTitulo());

        return vw_inflater;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Tarea.Inciso insiso = (Tarea.Inciso) lv_lista_insisos.getItemAtPosition(i);


        insiso.setCheck(!insiso.isCheck());

        Map<String, Object> childUpdate = new HashMap<>();
        childUpdate.put(String.valueOf(i),insiso);
        database.getReference().child(Reference_Fire_Base.REFERENCE_DATABASE_TAREAS+"/"+FirebaseAuth.getInstance().getUid()+"/"+tarea.getId()+"/"+Reference_Fire_Base.REFERENCE_DATABASE_LISTA_INCISOS).updateChildren(childUpdate);

    }
}
