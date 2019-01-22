package com.example.asus.chatoffice.Objetos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Organizacion  implements Serializable{

    String st_id_organizacion;
    List<String> lista_miembros_organizacion = new ArrayList<>(),lista_peticiones = new ArrayList<>();
    String jefe;


    public Organizacion() {
    }

    public Organizacion(String jefe, List<String> lista_miembros_organizacion, String st_id_organizacion) {
        this.st_id_organizacion = st_id_organizacion;
        this.lista_miembros_organizacion = lista_miembros_organizacion;
        this.jefe = jefe;
    }

    public Organizacion(String st_id_organizacion, List<String> lista_miembros_organizacion, List<String> lista_peticiones, String jefe) {
        this.st_id_organizacion = st_id_organizacion;
        this.lista_miembros_organizacion = lista_miembros_organizacion;
        this.lista_peticiones = lista_peticiones;
        this.jefe = jefe;
    }

    public List<String> getLista_peticiones() {
        return lista_peticiones;
    }

    public void setLista_peticiones(List<String> lista_peticiones) {
        this.lista_peticiones = lista_peticiones;
    }

    public String getSt_id_organizacion() {
        return st_id_organizacion;
    }

    public void setSt_id_organizacion(String st_id_organizacion) {
        this.st_id_organizacion = st_id_organizacion;
    }

    public List<String> getLista_miembros_organizacion() {
        return lista_miembros_organizacion;
    }

    public void setLista_miembros_organizacion(List<String> lista_miembros_organizacion) {
        this.lista_miembros_organizacion = lista_miembros_organizacion;
    }

    public String getJefe() {
        return jefe;
    }

    public void setJefe(String jefe) {
        this.jefe = jefe;
    }

    @Override
    public String toString() {
        return "Organizacion{" +
                "st_id_organizacion='" + st_id_organizacion + '\'' +
                ", lista_miembros_organizacion=" + lista_miembros_organizacion +
                ", Jefe=" + jefe +
                '}';
    }
}
