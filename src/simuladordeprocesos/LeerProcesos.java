/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simuladordeprocesos;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author dayton
 */
public class LeerProcesos extends Thread {
    
    public void LimpiarTabla(){
        try {
            int filas=Home.TablaProcesos.getRowCount();
            for (int i = 0;filas>i; i++) {
                Home.modelo.removeRow(0);
                Home.TablaProcesos.repaint();
            }
        } catch (Exception e) {

        }
    }
    public void run()
   {
       while (true){
           LimpiarTabla();
         FileReader f = null;
        try {
            // Aquí el código pesado que tarda mucho
            String cadena;
            f = new FileReader("/proc/DEGM");
            try (BufferedReader b = new BufferedReader(f)) {
                
                while((cadena = b.readLine())!=null) {
                    
                    String vector[] = cadena.split(",");
                    Nodo n = new Nodo(vector[0], Integer.parseInt(vector[1].trim()),Integer.parseInt(vector[2].trim()));
                    String state;
                    if (n.getEstado()==0)
                        state ="running";
                    else if (n.getEstado()<0)
                        state = "unrunning";
                    else
                        state = "stop";
                    Home.modelo.addRow(new Object[]{n.getNombre(), n.getpId(), state});
                    
                } } catch (IOException ex) {
               Logger.getLogger(Leer.class.getName()).log(Level.SEVERE, null, ex);
           }
}       catch (FileNotFoundException ex) {
            Logger.getLogger(Leer.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally 
        {
            try {
                f.close();
            } catch (IOException ex) {
                Logger.getLogger(Leer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
           try {
               Thread.sleep(1500);
           } catch (InterruptedException ex) {
               Logger.getLogger(Leer.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
        
   
   } 
    
}
