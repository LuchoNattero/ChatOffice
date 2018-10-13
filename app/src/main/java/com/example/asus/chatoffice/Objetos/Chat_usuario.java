package com.example.asus.chatoffice.Objetos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Chat_usuario implements Serializable {


//    String administrador;
    String hora, ultimo_msj, msj;
    List<String> historial = new ArrayList<>();

    public Chat_usuario() {
    }

    public Chat_usuario(String hora, String nombre_chat, String msj) {
        this.hora = hora;
        this.ultimo_msj = nombre_chat;
        this.msj = msj;
    }
    public void agregarAlHistorial(String m){

        this.historial.add(m);

    }

    public List<String> getHistorial() {
        return historial;
    }

    public void setHistorial(List<String> historial) {
        this.historial = historial;
    }
    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getUltimo_msj() {
        return ultimo_msj;
    }

    public void setUltimo_msj(String ultimo_msj) {
        this.ultimo_msj = ultimo_msj;
    }

    public String getMsj() {
        return msj;
    }

    public void setMsj(String msj) {
        this.msj = msj;
    }
}
