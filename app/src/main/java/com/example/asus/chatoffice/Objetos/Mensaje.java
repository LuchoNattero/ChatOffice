package com.example.asus.chatoffice.Objetos;

import java.io.Serializable;

public class Mensaje implements Serializable {

    String mensaje,hora;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
