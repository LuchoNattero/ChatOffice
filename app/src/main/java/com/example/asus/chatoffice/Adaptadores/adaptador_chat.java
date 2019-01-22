package com.example.asus.chatoffice.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asus.chatoffice.Objetos.Proyecto;
import com.example.asus.chatoffice.R;

import java.util.List;
import java.util.Map;


public class adaptador_chat extends BaseAdapter {

    Context context;
    List<Proyecto> lista_chats;

    public adaptador_chat(Context c, List<Proyecto> lista){

        context = c;
        lista_chats = lista;
    }
    @Override
    public int getCount() {
        return lista_chats.size();
    }

    @Override
    public Proyecto getItem(int i) {
        return lista_chats.get(i);
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
            view = inflater.inflate(R.layout.fragment_chat_min, viewGroup, false);
        }

        Proyecto chat =  getItem(i);

        TextView titulo, msj, hora;
        titulo = view.findViewById(R.id.tv_titulo_chat_min);
        hora = view.findViewById(R.id.tv_chat_hora_min);
        msj = view.findViewById(R.id.tv_chat_ultimo_msj);

        titulo.setText(chat.getTitulo());
//        hora.setText(chat.getHistorial().get(chat.getHistorial().size()).getHora());
//        msj.setText(chat.getHistorial().get(chat.getHistorial().size()).getMensaje());
        return view;
    }
}
