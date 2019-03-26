package com.example.asus.chatoffice.Objetos;

import java.io.Serializable;
import java.util.Date;

public class Reunion implements Serializable {

    String motivo, lugar, hora,id,respuesta= "No Contesto",motivo_inasistencia;
    Date dia;
    public Reunion() {
    }

    public Reunion(String motivo, String lugar, String hora, String id, Date dia) {
        this.motivo = motivo;
        this.lugar = lugar;
        this.hora = hora;
        this.id = id;
        this.dia = dia;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setConfirmo(){
        this.respuesta = "Asistirá";
    }
    public void setNoIra(String mot){
        this.respuesta = "No Asistirá";
        this.motivo_inasistencia = mot;
    }
}

