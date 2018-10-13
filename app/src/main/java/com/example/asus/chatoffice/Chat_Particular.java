package com.example.asus.chatoffice;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.asus.chatoffice.Objetos.Chat_usuario;
import com.example.asus.chatoffice.Reference.Reference;

import java.util.ArrayList;
import java.util.List;

public class Chat_Particular extends AppCompatActivity {

    ListView lv_historial;
    List<String>lista_historia = new ArrayList<>();
    Intent intent;
    EditText editMensaje;
    FloatingActionButton fl_enviar_msj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__particular);

        intent = getIntent();

        editMensaje = findViewById(R.id.et_chat_particular);
        fl_enviar_msj = findViewById(R.id.fl_enviar_msj);


        Chat_usuario usuario = (Chat_usuario) intent.getSerializableExtra(Reference.CHAT);
        lista_historia = usuario.getHistorial();


        final ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista_historia);

        lv_historial = findViewById(R.id.lv_chat_particular);
        lv_historial.setAdapter(itemsAdapter);

        fl_enviar_msj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!editMensaje.getText().toString().isEmpty())
                {
                    lista_historia.add(editMensaje.getText().toString());
                    itemsAdapter.notifyDataSetChanged();
                    editMensaje.setText("");
                }
            }
        });

    }
}
