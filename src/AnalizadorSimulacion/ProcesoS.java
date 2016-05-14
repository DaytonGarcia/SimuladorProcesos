/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalizadorSimulacion;

import java.util.ArrayList;

/**
 *
 * @author dayton
 */
public class ProcesoS {
    
    public int id;
    public ArrayList<Integer> recursos = new ArrayList<Integer>();
    public ArrayList<Integer> asignados = new ArrayList<Integer>();
    
    ProcesoS(int id){
        this.id = id;
    }

    ProcesoS() {
        this.id = 0;
    }
    
    
}
