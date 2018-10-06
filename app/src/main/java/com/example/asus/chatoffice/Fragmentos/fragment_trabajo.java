package com.example.asus.chatoffice.Fragmentos;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.asus.chatoffice.Adaptadores.adaptador_lista_trabajo;
import com.example.asus.chatoffice.Nueva_Tarea;
import com.example.asus.chatoffice.Objetos.Tarea;
import com.example.asus.chatoffice.R;
import com.example.asus.chatoffice.Reference.Reference;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class fragment_trabajo extends Fragment {

    View miInflater;
    private ListView listView;
    private adaptador_lista_trabajo adaptador;
    private List<Tarea> lista_tareas = new ArrayList<>();
    FloatingActionButton addTarea;

    public fragment_trabajo() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        miInflater = inflater.inflate(R.layout.activity_lista_trabajo, container, false);

        listView = miInflater.findViewById(R.id.lv_trabajos);
        addTarea = miInflater.findViewById(R.id.fl_add_trabajo);

        addTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),Nueva_Tarea.class);
                startActivityForResult(intent,0);
            }
        });


        adaptador = new adaptador_lista_trabajo(this.getContext(),lista_tareas);
        listView.setAdapter(adaptador);
        registerForContextMenu(listView);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setClickable(true);
        return miInflater;
    }
       @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 0){

            if(resultCode == RESULT_OK){
                Tarea tarea = (Tarea) data.getSerializableExtra(Reference.TAREA);
                lista_tareas.add(tarea);
                adaptador.notifyDataSetChanged();
                Toast.makeText(getContext(), "Se acepto la nueva tarea", Toast.LENGTH_SHORT).show();
            }
            else if(requestCode == RESULT_CANCELED){

                Toast.makeText(getContext(), "Se cancelo la nueva tarea", Toast.LENGTH_SHORT).show();


            }


        }



    }


}
