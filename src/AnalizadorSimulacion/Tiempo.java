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
public class Tiempo {
    
    int id;
    ArrayList<ProcesoS> proceso = new ArrayList<ProcesoS>();
    
    Tiempo (int id)
    {
        this.id = id;
    }
    
    
}
