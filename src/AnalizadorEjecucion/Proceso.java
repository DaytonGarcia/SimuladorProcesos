/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalizadorEjecucion;

/**
 *
 * @author dayton
 */
public class Proceso {
    
    private int id;
    private int inicio;
    private int duracion;
    private int prioridad;
    private Proceso sig, ant;
    private int tEjecutado;
    private int estado;

    public Proceso(int id, int inicio, int duracion, int prioridad) {
        this.id = id;
        this.inicio = inicio;
        this.duracion = duracion;
        this.prioridad = prioridad;
        this.sig = null;
        this.ant = null;
        this.tEjecutado = 0;
        this.estado = 0;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInicio() {
        return inicio;
    }

    public void setInicio(int inicio) {
        this.inicio = inicio;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public Proceso getSig() {
        return sig;
    }

    public void setSig(Proceso sig) {
        this.sig = sig;
    }

    public Proceso getAnt() {
        return ant;
    }

    public void setAnt(Proceso ant) {
        this.ant = ant;
    }

    public int gettEjecutado() {
        return tEjecutado;
    }

    public void settEjecutado(int tEjecutado) {
        this.tEjecutado = tEjecutado;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
    
    public void NullAnt(){
        this.ant = null;
    }
    
    public void NullSig()
    {
        this.sig = null;
    }
    
}
