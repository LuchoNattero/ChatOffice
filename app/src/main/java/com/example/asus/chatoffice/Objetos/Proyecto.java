package com.example.asus.chatoffice.Objetos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Proyecto implements Serializable {


    String titulo,idOrganizacion;
    Usuario administrador;
    List<String> lista_usuarios;



    public Proyecto() {
    }

    public Proyecto(String titulo, String idOrganizacion, Usuario administrador, List<String> lista_usuarios) {
        this.titulo = titulo;
        this.idOrganizacion = idOrganizacion;
        this.administrador = administrador;
        this.lista_usuarios = lista_usuarios;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void agregarAListaUsuario(String usuario){
        this.lista_usuarios.add(usuario);
    }
    public Usuario getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Usuario administrador) {
        this.administrador = administrador;
    }

    public List<String> getLista_usuarios() {
        return lista_usuarios;
    }

    public void setLista_usuarios(List<String> lista_usuarios) {
        this.lista_usuarios = lista_usuarios;
    }

    @Override
    public String toString() {
        return
                titulo ;
    }
}
