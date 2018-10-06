package com.example.asus.chatoffice.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.asus.chatoffice.Objetos.Tarea;
import com.example.asus.chatoffice.R;

import java.util.ArrayList;
import java.util.List;

public class adaptador_incisos extends BaseAdapter {

    Context context;
    List<Tarea.Inciso>lista_inciso = new ArrayList<>();

    public adaptador_incisos(Context c, List<Tarea.Inciso>lista) {
        context = c;
        lista_inciso = lista;

    }

    @Override
    public int getCount() {
        return lista_inciso.size();
    }

    @Override
    public List<Tarea.Inciso> getItem(int i) {
        return (List<Tarea.Inciso>) lista_inciso.get(i);
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
            view = inflater.inflate(R.layout.personalizado_tarea, viewGroup, false);
        }

        Tarea.Inciso inciso = (Tarea.Inciso) getItem(i);

        CheckBox item = view.findViewById(R.id.ck_tarea);
        item.setText(inciso.getInciso());
        return view;
    }
}
