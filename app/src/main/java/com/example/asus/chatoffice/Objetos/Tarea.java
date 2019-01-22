package com.example.asus.chatoffice.Objetos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tarea implements Serializable {

    String titulo, prioridad, descripcion,id;
    List<Inciso> lista_incisos = new ArrayList();
    List<String> lista_comentarios = new ArrayList<>();


    public Tarea() {
    }

    public Tarea(String titulo, String prioridad, String descripcion, String id, List<Inciso> lista_incisos, List<String> lista_comentarios) {
        this.titulo = titulo;
        this.prioridad = prioridad;
        this.descripcion = descripcion;
        this.id = id;
        this.lista_incisos = lista_incisos;
        this.lista_comentarios = lista_comentarios;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getLista_comentarios() {
        return lista_comentarios;
    }

    public void setLista_comentarios(List<String> lista_comentarios) {
        this.lista_comentarios = lista_comentarios;
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

    public static class  Inciso implements Serializable{

        String inciso;
        boolean check;

        public Inciso() {
        }

        public void setCheck(boolean check) {
            this.check = check;
        }

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

        @Override
        public String toString() {
            return  inciso;
        }
    }
}
