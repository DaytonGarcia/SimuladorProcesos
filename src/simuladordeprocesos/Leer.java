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
import simuladordeprocesos.Home;
/**
 *
 * @author dayton
 */
public class Leer extends Thread {
    
    public void run()
   {
       while (true){
         FileReader f = null;
        try {
            // Aquí el código pesado que tarda mucho
            String cadena;
            f = new FileReader("/proc/meminfo");
            try (BufferedReader b = new BufferedReader(f)) {
                int linea = 0;
                while((cadena = b.readLine())!=null) {
                    if (linea==1)
                    {
                        int ramLibre = Integer.parseInt(cadena.substring(10).replace("kB", " ").trim());
                        Home.txtRAM.setText(Integer.toString(ramLibre));
                        Home.txtRAM2.setText(Double.toString((ramLibre*100/Home.ram)));
                    }
                    else if (linea==15)
                    {
                        int swapLibre = Integer.parseInt(cadena.substring(10).replace("kB", " ").trim());
                        Home.txtSWAP.setText(Integer.toString(swapLibre));
                        Home.txtSWAP2.setText(Double.toString((swapLibre*100/Home.swap)));
                    }
                    linea++;
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
               Thread.sleep(700);
           } catch (InterruptedException ex) {
               Logger.getLogger(Leer.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
        
   
   } 
    
}
