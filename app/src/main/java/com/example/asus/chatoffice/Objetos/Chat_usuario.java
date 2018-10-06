package com.example.asus.chatoffice.Objetos;

import java.io.Serializable;

public class Chat_usuario implements Serializable {

    String hora, nombre_chat, msj;

    public Chat_usuario() {
    }

    public Chat_usuario(String hora, String nombre_chat, String msj) {
        this.hora = hora;
        this.nombre_chat = nombre_chat;
        this.msj = msj;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getNombre_chat() {
        return nombre_chat;
    }

    public void setNombre_chat(String nombre_chat) {
        this.nombre_chat = nombre_chat;
    }

    public String getMsj() {
        return msj;
    }

    public void setMsj(String msj) {
        this.msj = msj;
    }
}
