/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalizadorSimulacion;

/**
 *
 * @author dayton
 */
public class Recurso {
    
    private int id;
    private int asigando;
    private int estado;
    
    Recurso(String pId)
    {
        this.id = Integer.parseInt(pId);
        this.asigando = 0;
        this.estado=0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAsigando() {
        return asigando;
    }

    public void setAsigando(int asigando) {
        this.asigando = asigando;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
    
}
