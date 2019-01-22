package com.example.asus.chatoffice.Adaptadores;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.chatoffice.Objetos.Tarea;
import com.example.asus.chatoffice.R;
import com.example.asus.chatoffice.Reference.Reference;

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
        TextView titulo, descripcion, prioridad;

        Tarea tarea =  getItem(i);
        RelativeLayout relativeLayout = view.findViewById(R.id.rl_fragment_trabajo_min);
        titulo = view.findViewById(R.id.tv_titulo_tarea_realizar);
        descripcion = view.findViewById(R.id.tv_resumen_tarea);
        prioridad = view.findViewById(R.id.tv_prioridad_tarea_realizar);

        titulo.setText(tarea.getTitulo());
        prioridad.setText(tarea.getPrioridad());
        descripcion.setText(tarea.getDescripcion());

        if (prioridad.getText().toString().equals(Reference.PRIORIDAD_ALTA)){

            relativeLayout.setBackgroundResource(R.drawable.contenedor_tarea_alta);

        }
        else if (prioridad.getText().toString().equals(Reference.PRIORIDAD_MEDIA)){

            relativeLayout.setBackgroundResource(R.drawable.contenedor_tarea_media);

        }
        else if (prioridad.getText().toString().equals(Reference.PRIORIDAD_BAJA)){

            relativeLayout.setBackgroundResource(R.drawable.contenedor_tarea_baja);

        }
        return view;
    }
}
