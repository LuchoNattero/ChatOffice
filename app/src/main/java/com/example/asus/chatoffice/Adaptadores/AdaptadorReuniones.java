package com.example.asus.chatoffice.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.asus.chatoffice.Objetos.Reunion;
import com.example.asus.chatoffice.R;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorReuniones extends BaseAdapter {
    List<Reunion> lista = new ArrayList<>();
    Context context;
    TextView motivo,hora,dia, lugar;

    Button bt_aceptar_reunion,bt_cancelar_reunion;
    public AdaptadorReuniones(Context context, List<Reunion> lista) {
        this.lista = lista;
        this.context = context;
    }


    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Reunion getItem(int i) {
        return lista.get(i);
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
            view = inflater.inflate(R.layout.frag_reunion, viewGroup, false);
        }

        Reunion reunion = getItem(i);

        bt_aceptar_reunion = view.findViewById(R.id.bt_aceptar_reunion);
        bt_cancelar_reunion = view.findViewById(R.id.bt_cancelar_reunion);

        bt_aceptar_reunion.setFocusable(false); bt_aceptar_reunion.setClickable(false);
        bt_cancelar_reunion.setFocusable(false); bt_cancelar_reunion.setClickable(false);

        motivo = view.findViewById(R.id.tv_motivo_reunion);
        lugar = view.findViewById(R.id.tv_lugar_reunion);
        hora = view.findViewById(R.id.tv_hora_reunion);
        dia = view.findViewById(R.id.tv_dia_reunion);

        motivo.setText(reunion.getMotivo());
        lugar.setText(reunion.getLugar());
        hora.setText(reunion.getHora().toString());
        dia.setText(String.valueOf(reunion.getDia().getDay())+"/"+String.valueOf(reunion.getDia().getMonth()));

        return view;
    }
}

