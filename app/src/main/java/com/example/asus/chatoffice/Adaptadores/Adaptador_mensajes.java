package com.example.asus.chatoffice.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.chatoffice.Objetos.Mensaje;
import com.example.asus.chatoffice.Objetos.Proyecto;
import com.example.asus.chatoffice.Objetos.Tarea;
import com.example.asus.chatoffice.R;

import java.util.ArrayList;
import java.util.List;

public class Adaptador_mensajes extends BaseAdapter {

    private Context context;
    private List<Mensaje> lista_mensajes = new ArrayList<>();

    public Adaptador_mensajes(Context c, List<Mensaje> lista) {

        this.context = c;
        this.lista_mensajes = lista;
    }

    @Override
    public int getCount() {
        return lista_mensajes.size();
    }

    @Override
    public Mensaje getItem(int i) {
       return lista_mensajes.get(i);
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
            view = inflater.inflate(R.layout.fragment_mensaje, viewGroup, false);
        }

        Mensaje mensaje =  getItem(i);

        TextView nombre, msj, hora;

        nombre = view.findViewById(R.id.tv_nombre_usuario_fragment_mensaje);
        msj = view.findViewById(R.id.tv_mensaje_fragmento_mensajes);

        nombre.setText(mensaje.getAutor());
        msj.setText(mensaje.getMensaje().toString());

        return view;
    }
}
