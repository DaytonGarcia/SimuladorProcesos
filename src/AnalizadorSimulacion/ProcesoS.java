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
    
    int id;
    ArrayList<Recurso> recursos = new ArrayList<Recurso>();
    
    ProcesoS(int id){
        this.id = id;
    }

    ProcesoS() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
