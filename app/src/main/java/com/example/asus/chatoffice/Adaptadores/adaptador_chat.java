package com.example.asus.chatoffice.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.asus.chatoffice.Objetos.Chat_usuario;
import com.example.asus.chatoffice.Objetos.Tarea;
import com.example.asus.chatoffice.R;

import java.util.List;


public class adaptador_chat extends BaseAdapter {

    Context context;
    List<Chat_usuario>lista_chats;

    public adaptador_chat(Context c, List<Chat_usuario> lista){

        context = c;
        lista_chats = lista;
    }
    @Override
    public int getCount() {
        return lista_chats.size();
    }

    @Override
    public Chat_usuario getItem(int i) {
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

        Chat_usuario chat =  getItem(i);

        TextView titulo, msj, hora;
        titulo = view.findViewById(R.id.tv_titulo_chat_min);
        hora = view.findViewById(R.id.tv_chat_hora_min);
        msj = view.findViewById(R.id.tv_chat_ultimo_msj);

        titulo.setText(chat.getNombre_chat());
        hora.setText(chat.getHora());
        msj.setText(chat.getMsj());

        return view;
    }
}
