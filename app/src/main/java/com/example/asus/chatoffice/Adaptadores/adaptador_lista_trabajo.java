package com.example.asus.chatoffice.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asus.chatoffice.Objetos.Tarea;
import com.example.asus.chatoffice.R;

import java.util.ArrayList;
import java.util.List;

public class adaptador_lista_trabajo extends BaseAdapter {

    Context context;
    List<Tarea> lista_tareas = new ArrayList<>();


    public adaptador_lista_trabajo(Context c, List<Tarea> lista) {

        lista_tareas = lista;
        context = c;
    }

    @Override
    public int getCount() {
        return lista_tareas.size();
    }

    @Override
    public Tarea getItem(int i) {
        return lista_tareas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fragment_trabajo_min, viewGroup, false);
        }

        Tarea tarea =  getItem(i);

        TextView titulo, descripcion, prioridad;
        titulo = view.findViewById(R.id.tv_titulo_trabajo);
        descripcion = view.findViewById(R.id.tv_resumen_tarea);
        prioridad = view.findViewById(R.id.tv_prioridad_trabajo);

        titulo.setText(tarea.getTitulo());
        prioridad.setText(tarea.getPrioridad());
        descripcion.setText(tarea.getDescripcion());

        return view;
    }
}
