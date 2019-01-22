package com.example.asus.chatoffice.Objetos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Usuario implements Serializable {

    String st_nombre, st_apallido, st_id, st_idEmpresa;



    public Usuario() {
    }

    public Usuario(String st_nombre, String st_apallido, String st_id, String st_idEmpresa) {
        this.st_nombre = st_nombre;
        this.st_apallido = st_apallido;
        this.st_id = st_id;
        this.st_idEmpresa = st_idEmpresa;
    }


    public String getSt_nombre() {
        return st_nombre;
    }

    public void setSt_nombre(String st_nombre) {
        this.st_nombre = st_nombre;
    }

    public String getSt_apallido() {
        return st_apallido;
    }

    public void setSt_apallido(String st_apallido) {
        this.st_apallido = st_apallido;
    }

    public String getSt_id() {
        return st_id;
    }

    public void setSt_id(String st_id) {
        this.st_id = st_id;
    }

    public String getSt_idEmpresa() {
        return st_idEmpresa;
    }

    public void setSt_idEmpresa(String st_idEmpresa) {
        this.st_idEmpresa = st_idEmpresa;
    }


    @Override
    public String toString() {
        return  st_apallido+", " +
                st_nombre
               ;
    }
}
