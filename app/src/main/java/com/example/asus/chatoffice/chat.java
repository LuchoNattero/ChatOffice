package com.example.asus.chatoffice;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.asus.chatoffice.Adaptadores.adaptador_chat;
import com.example.asus.chatoffice.NuevoChat;
import com.example.asus.chatoffice.Objetos.Chat_usuario;

import java.util.ArrayList;
import java.util.List;

public class chat extends Fragment{

    adaptador_chat adapter;
    View miInflater;
    ListView listViewChat;
    List<Chat_usuario> lista_chat;
    FloatingActionButton fl_add_chat;

    public chat(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        miInflater = inflater.inflate(R.layout.activity_chat, container, false);

        listViewChat = miInflater.findViewById(R.id.lv_chats);
        fl_add_chat = miInflater.findViewById(R.id.fl_add_chat);
        lista_chat = new ArrayList<>();

        final Chat_usuario chat1 = new Chat_usuario("20:00","Oficina","Este es un msj de prueba");
        Chat_usuario chat2 = new Chat_usuario("07:00","RRHH","Este es un msj de prueba");

        fl_add_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), NuevoChat.class);
//                intent.putExtra("chat",chat1);
                startActivityForResult(intent, 1);
            }
        });



        lista_chat.add(chat1);
        lista_chat.add(chat2);

        adapter = new adaptador_chat(getContext(),lista_chat);
        listViewChat.setAdapter(adapter);
        registerForContextMenu(listViewChat);
        listViewChat.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewChat.setClickable(true);

        return miInflater;
    }
}
