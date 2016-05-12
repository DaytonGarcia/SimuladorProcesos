/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simuladordeprocesos;

import AnalizadorEjecucion.Proceso;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author dayton
 */
public class Dibujar extends JPanel{

    public Dibujar() {
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
       super.paintComponents(g);
       
       Proceso pivote = Home.cola.getCabeza();
       
       while (pivote!=null)
       {
           if(pivote.getId()==1)
            g.setColor(Color.red);
           else if (pivote.getId()==2)
               g.setColor(Color.BLUE);
           else if (pivote.getId()==3)
               g.setColor(Color.CYAN);
           else if (pivote.getId()==4)
               g.setColor(Color.GREEN);
           else if (pivote.getId()==5)
               g.setColor(Color.MAGENTA);
           else if (pivote.getId()==6)
               g.setColor(Color.ORANGE);
           else if (pivote.getId()==7)
               g.setColor(Color.PINK);
           else 
               g.setColor(Color.YELLOW);
           
           g.fillRect(pivote.getInicioReal()*20,pivote.getId()*65, pivote.getDuracion()*20, 50);
           g.drawString("Proceso "+pivote.getId(), pivote.getInicioReal()*20, pivote.getId()*65+62);
           if(pivote.getInicio()!=pivote.getInicioReal())
           {
               g.setColor(Color.BLACK);
               g.fillRect(pivote.getInicio()*20,pivote.getId()*65, (pivote.getInicioReal()-pivote.getInicio())*20, 50);
           }
           
           pivote=pivote.getSig();
       }
       
       for(int i = 0; i<=23;i++)
       {
         g.setColor(Color.LIGHT_GRAY);
         g.drawRect(i*20, 10, (i*20+20), 470);
         g.setColor(Color.RED);
         g.drawString(Integer.toString(i), i*20-5, 490);
       }
       
       Home.lbMetodo.setText("FCFS");
       Home.lbMetodo.setVisible(true);
       
    }
    
    
}
