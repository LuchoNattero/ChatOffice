package com.example.asus.chatoffice.Objetos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tarea implements Serializable {

    String titulo, prioridad, descripcion;
    List<Inciso> lista_incisos = new ArrayList();

    public Tarea() {
    }

    public Tarea(String titulo, String prioridad, String descripcion,List<Inciso> lista) {
        this.titulo = titulo;
        this.prioridad = prioridad;
        this.descripcion = descripcion;
        this.lista_incisos = lista;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Inciso> getLista_incisos() {
        return lista_incisos;
    }

    public void setLista_incisos(List<Inciso> lista_incisos) {
        this.lista_incisos = lista_incisos;
    }

    public static class  Inciso{

        String inciso;
        boolean check;

        public String getInciso() {
            return inciso;
        }

        public boolean isCheck() {
            return check;
        }

        public Inciso(String i, boolean c){
            this.inciso = i;
            this.check = c;

        }

    }
}
