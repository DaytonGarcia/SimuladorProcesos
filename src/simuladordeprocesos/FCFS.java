/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simuladordeprocesos;

/**
 *
 * @author dayton
 */

import AnalizadorEjecucion.Proceso;
import java.util.ArrayList;

public class FCFS {
    
    private Proceso cabeza;
    private Proceso cola;
    
    FCFS()
    {
        cabeza = cola = null;
    }
    
    void InsertarEnOrden(Proceso pProceso){
        
        if(cabeza!=null)
        {
            /*
            * Si la cabeza y la cola son iguales
            * Quiere decir que solo hay un elemento en la lista
            * Por lo que solo se puede incerta al final o al inicio
            */
            if(cabeza == cola)
            {
               if(pProceso.getInicio()<cabeza.getInicio())
               {
                   /*
                   * Si solo hay un elemento y su inicio es mayor al del nuevo elemento
                   * Se coloca el nuevo elemento por delante 
                   */
                   cabeza.setAnt(pProceso);
                   pProceso.setAnt(null);
                   pProceso.setSig(cabeza);
                   cabeza = pProceso;
               }
               else
               {
                   /*
                   * Si solo hay un elemento y su inicio es menor al del nuevo elemento
                   * Se coloca el nuevo elemento detras 
                   */
                   cola.setSig(pProceso);
                   pProceso.setAnt(cola);
                   pProceso.setSig(null);
                   cola = pProceso;
               }
            }
            else if (pProceso.getInicio()<cabeza.getInicio())
            {
                /*
                * Si hay mas de un elemento, pero el primer elemento inicia despues del nuevo
                * Se coloca el nuevo elemento por delante 
                */
                cabeza.setAnt(pProceso);
                pProceso.setAnt(null);
                pProceso.setSig(cabeza);
                cabeza = pProceso;
            }
            else if (pProceso.getInicio()>=cola.getInicio())
            {
                /*
                * Si hay mas de un elemento y el ultimo elemento inicia antes que el nuevo
                * Se coloca el nuevo elemento por detras
                */
                cola.setSig(pProceso);
                pProceso.setAnt(cola);
                pProceso.setSig(null);
                cola = pProceso;
            }
            else
            {
                /*
                * Si no se dio ninguno de los casos anterirores, significa que hay mas de un elemento en la cola
                * y que la posicion del nuevo elemento es intermedia
                * Por lo que hay que recorrer la cola para encontrar su posicion exacta
                */
                Proceso tmp = cabeza;
                
                while (tmp.getInicio()<=pProceso.getInicio())
                {
                    if (tmp.getInicio()<=pProceso.getInicio() && tmp.getSig().getInicio()>pProceso.getInicio())
                    {
                        pProceso.setAnt(tmp);
                        pProceso.setSig(tmp.getSig());
                        tmp.setSig(pProceso);
                        tmp.getSig().setAnt(pProceso);
                        break;
                    }
                    else
                    {
                        tmp = tmp.getSig();
                    }
                }
            }
        } 
        else 
        { 
            /*
            * Esto solo ocurre cuando la lista esta vacia, 
            * Se procede a insertar el primer elemento en la lista
            * Por ser el primero la cola y la cabeza apuntan a el
            */
            cabeza = pProceso;
            cola = pProceso;
            cola.setAnt(cabeza);
            cola.setSig(null);
            cabeza.setAnt(null);
            cabeza.setSig(cola);
        }
        
    }

    public Proceso getCabeza() {
        return cabeza;
    }

    public void setCabeza(Proceso cabeza) {
        this.cabeza = cabeza;
    }

    public Proceso getCola() {
        return cola;
    }

    public void setCola(Proceso cola) {
        this.cola = cola;
    }
    
    public void CalcularTiempos()
    {
        int tiempo = 0;
        if(cabeza!=null)
        {
            Proceso pivote = cabeza;
            while(pivote!=null)
            {
                if(pivote.getInicio()<=tiempo)
                {
                    pivote.settEjecutado(pivote.gettEjecutado()+1);
                    Proceso n = pivote;
        
                    System.out.println("\n\nTIEMPO "+tiempo+"\n");
                    while (n!=null)
                    {
                        if(n.getInicio()<=tiempo)
                        {
                            System.out.print(n.getId()+", "+n.getInicio()+", "+n.getDuracion()+", "+n.getPrioridad()); 
                            if(n==pivote)
                                System.out.print("           Activo\n");
                            else
                                System.out.print("           Dormido\n");
                        }
                        
                        n=n.getSig();
                    }
                    
                    if((pivote.getDuracion()-pivote.gettEjecutado())<=0)
                    {
                        pivote.setInicioReal(tiempo+1-pivote.getDuracion());
                        pivote = pivote.getSig();
                        //cabeza.setAnt(null);
                    }
                }
                tiempo++;
            }
            
        }
        else
        {
            System.out.println("No puedo hacer el calculo porque no hay procesos en ejecucion");
        }
    }
    
    public void CalcularTiemposSJF()
    {
        ArrayList<Proceso> Listo = new ArrayList<>();
        int tiempo = 0;
        Proceso pivote = cabeza;
        Proceso aux = cabeza;
        
        while (aux!=null)
        {
            if(aux.getEstado()==0)
            {
                /*
                Agrego todos los que su tiempo sea menor a la lista de listo
                y cambio su estado a 1 que es que esta listo
                */
                while (pivote!=null)
                {
                    if(pivote.getInicio()<=tiempo && pivote.getEstado()==0)
                    {
                        pivote.setEstado(1);
                        Listo.add(pivote);
                        
                    }
                    pivote=pivote.getSig();
                }
                
                /*
                Tomo el primero de la lista de listo y lo uso como pivote
                para compararlo con todos los de la lista de listo
                y elegir al menor
                */

                if(Listo.size()>0)
                {
                    
                
                Proceso menor = Listo.get(0);

                for (int i=0; i <= Listo.size()-1; i++)
                {
                    if(menor.getDuracion()>Listo.get(i).getDuracion())
                        menor = Listo.get(i);
                }
                
                /*
                    Al menor seleccionado le seteo su tiempo de inicio real
                    Este tiempo es tiempo actual
                */
                menor.setInicioReal(tiempo);
                
                /*
                    El tiempo actual nuevo es igual al tiempo mas la duracion del proceso
                */
                tiempo = tiempo + menor.getDuracion();
                /*
                    Quito el proceso de la lista de listo porque ya se termino
                */
                Listo.remove(menor);
                }
                else
                    tiempo++;
            }
            
            if(aux.getEstado()==1)
            {
                aux = aux.getSig();
                
            }
            else
            {
                pivote = aux;
            }
            
                
        }
        
     
    }
    
    public void CalcularTiemposPrioridad()
    {
        ArrayList<Proceso> Listo = new ArrayList<>();
        int tiempo = 0;
        Proceso pivote = cabeza;
        Proceso aux = cabeza;
        
        while (aux!=null)
        {
            if(aux.getEstado()==0)
            {
                /*
                Agrego todos los que su tiempo sea menor a la lista de listo
                y cambio su estado a 1 que es que esta listo
                */
                while (pivote!=null)
                {
                    if(pivote.getInicio()<=tiempo && pivote.getEstado()==0)
                    {
                        pivote.setEstado(1);
                        Listo.add(pivote);
                        
                    }
                    pivote=pivote.getSig();
                }
                
                /*
                Tomo el primero de la lista de listo y lo uso como pivote
                para compararlo con todos los de la lista de listo
                y elegir al menor
                */

                if(Listo.size()>0)
                {
                    
                
                Proceso menor = Listo.get(0);

                for (int i=0; i <= Listo.size()-1; i++)
                {
                    if(menor.getPrioridad()<Listo.get(i).getPrioridad())
                        menor = Listo.get(i);
                }
                
                /*
                    Al menor seleccionado le seteo su tiempo de inicio real
                    Este tiempo es tiempo actual
                */
                menor.setInicioReal(tiempo);
                
                /*
                    El tiempo actual nuevo es igual al tiempo mas la duracion del proceso
                */
                tiempo = tiempo + menor.getDuracion();
                /*
                    Quito el proceso de la lista de listo porque ya se termino
                */
                Listo.remove(menor);
                }
                else
                    tiempo++;
            }
            
            if(aux.getEstado()==1)
            {
                aux = aux.getSig();
                
            }
            else
            {
                pivote = aux;
            }
            
                
        }
        
     
    }
    
    public void CalcularTiemposRR()
    {
        ArrayList<Proceso> Listo = new ArrayList<>();
        int tiempo = 0;
        Proceso pivote = cabeza;
        Proceso aux = cabeza;
        
        while (aux!=null)
        {
            if(aux.getEstado()==0)
            {
                /*
                Agrego todos los que su tiempo sea menor a la lista de listo
                y cambio su estado a 1 que es que esta listo
                */
                while (pivote!=null)
                {
                    if(pivote.getInicio()<=tiempo && pivote.getEstado()==0)
                    {
                        pivote.setEstado(1);
                        Listo.add(pivote);
                        
                    }
                    pivote=pivote.getSig();
                }
                
                /*
                Tomo el primero de la lista de listo y lo uso como pivote
                para compararlo con todos los de la lista de listo
                y elegir al menor
                */

                if(Listo.size()>0)
                {
                    
                
                Proceso menor = Listo.get(0);

                
                
                /*
                    Al menor seleccionado le seteo su tiempo de inicio real
                    Este tiempo es tiempo actual
                */
                menor.setInicioReal(tiempo);
                
                /*
                    El tiempo actual nuevo es igual al tiempo mas la duracion del proceso
                */
                tiempo = tiempo + menor.getDuracion();
                /*
                    Quito el proceso de la lista de listo porque ya se termino
                */
                Listo.remove(menor);
                }
                else
                    tiempo++;
            }
            
            if(aux.getEstado()==1)
            {
                aux = aux.getSig();
                
            }
            else
            {
                pivote = aux;
            }
            
                
        }
        
     
    }
    
    public void InfoProcesos()
    {
        if(cabeza!=null)
        {
            Proceso pivote = cabeza;
            
            while(pivote!=null)
            {
                System.out.println("Proceso: "+pivote.getId()+", Inicio: "+ pivote.getInicio()+", Inicio Real: "+pivote.getInicioReal()+", Duracion: "+pivote.getDuracion());
                pivote = pivote.getSig();
            }
        }
    }
}
