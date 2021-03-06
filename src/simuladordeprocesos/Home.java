/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simuladordeprocesos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileInputStream;
import java.io.FileWriter;
import AnalizadorEjecucion.Proceso;
import AnalizadorSimulacion.ProcesoS;
import AnalizadorSimulacion.Recurso;
import AnalizadorSimulacion.Tiempo;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author dayton
 */
public  class Home extends javax.swing.JFrame {

    public static int ram, swap;
    public static int estrategia, metodo;
    public static DefaultTableModel modelo;
    public static FCFS cola = new FCFS();
    public int t1,t2,t3,t4;
    private ArrayList<Proceso> procesos = new ArrayList<Proceso>();
    public ArrayList<Tiempo> tiempos = new ArrayList<Tiempo>();
    public ArrayList<Recurso> recursos = new ArrayList<Recurso>();
   
    
    /*
    Area de grafico funcion
    Ancho 421
    Alto 493
    */
    void GenerarDot1(Tiempo t, String nombre)
    {
        System.out.println("Mi tiempo es "+t.id);
         try
        {
            BufferedWriter ficheroSalida = new BufferedWriter(
                new FileWriter(new File("src/Iconos/"+nombre+".dot")));
 
            ficheroSalida.write("digraph G {\n");
            
            ficheroSalida.write("node[shape=box]; ");
            int c =0;
            for(Recurso r: recursos)
            {
                if(c==0)
                    ficheroSalida.write("R"+r.getId());
                else
                    ficheroSalida.write(",R"+r.getId());
                c++;
            }
            ficheroSalida.write(";\n");    
            ficheroSalida.write("node[shape=circle]; ");
            int d =0;
            for(ProcesoS p:t.proceso)
            {
                if(d==0)
                    ficheroSalida.write("P"+p.id);
                else
                    ficheroSalida.write(",P"+p.id);
                d++;
            }
            ficheroSalida.write(";\n\n"); 
            
            //Si los recursos en espera son mayor a cero no aplica el modelo 1

            for(ProcesoS p:t.proceso)
            {
                int espera = 0;
                if(p.recursos.size()>0)
                {
                    for (int i=0;i<p.recursos.size();i++)
                    {
                        if(p.recursos.get(i)!=0)
                        {
                            espera++;
                        }
                    }
                }
                
                //if(espera==0){
                if(p.asignados.size()>0)
                {
                    for (int i=0;i<p.asignados.size();i++)
                    {
                        if(p.asignados.get(i)!=0)
                        {
                            ficheroSalida.write("R"+p.asignados.get(i)+"->P"+p.id+";\n");
                        }
                    }
                }
                
                if(p.recursos.size()>0)
                {
                    for (int i=0;i<p.recursos.size();i++)
                    {
                        if(p.recursos.get(i)!=0)
                        {
                            ficheroSalida.write("P"+p.id+"->R"+p.recursos.get(i)+";\n");
                        }
                    }
                }//}
            }
            ficheroSalida.write("}\n");
            ficheroSalida.close();
            Process p = Runtime.getRuntime().exec("dot -Tpng ~/NetBeansProjects/SimuladorDeProcesos/dot1.dot > ~/NetBeansProjects/SimuladorDeProcesos/dot1.png"); 
            
        }
        catch (IOException errorDeFichero)
        {
            System.out.println(
                "Ha habido problemas: " +
                errorDeFichero.getMessage() );
        }
    }
    void CompilarProcesos()
    {
        if(!"".equals(txtEjecucion.getText()) )
        {
            EscribirArchivo(this.txtEjecucion.getText(),"Ejecucion.txt");
            AnalizarEjecucion("Ejecucion.txt");
        }

    }
    void AnalizarSimulacion(String path)
    {
        AnalizadorSimulacion.Sintactico pars;
        try {
            pars=new AnalizadorSimulacion.Sintactico(new AnalizadorSimulacion.Lexico(new FileInputStream(path)));
            pars.parse();
            tiempos = pars.tiempos;
            recursos =pars.recursos;
        } catch (Exception ex) {
            System.out.println("Error fatal en compilación de entrada.");
            System.out.println("Causa: "+ex.getCause());
        } 
    }
    
    void AnalizarEjecucion(String path)
    {
        AnalizadorEjecucion.Sintactico pars;
        try {
            pars=new AnalizadorEjecucion.Sintactico(new AnalizadorEjecucion.Lexico(new FileInputStream(path)));
            pars.parse(); 
            this.procesos = pars.procesos;
            
        } catch (Exception ex) {
            System.out.println("Error fatal en compilación de entrada.");
            System.out.println("Causa: "+ex.getCause());
        } 
    }
    
    void EscribirArchivo(String cadena, String nombre){
         try
        {
            BufferedWriter ficheroSalida = new BufferedWriter(
                new FileWriter(new File(nombre)));
 
            ficheroSalida.write(cadena);
            ficheroSalida.close();
        }
        catch (IOException errorDeFichero)
        {
            System.out.println(
                "Ha habido problemas: " +
                errorDeFichero.getMessage() );
        }
    }
  
    void CalcularMemoria() throws FileNotFoundException, IOException{
      String cadena;
      FileReader f = new FileReader("/proc/meminfo");
        try (BufferedReader b = new BufferedReader(f)) {
            int linea = 0;
            
            while((cadena = b.readLine())!=null) {
                if(linea==0){
                    String cadRam = cadena.substring(10).replace("kB", " ").trim();
                    ram = Integer.parseInt(cadRam);
                }
                
                if(linea==14){
                    String cadSWAP = cadena.substring(11).replace("kB", " ").trim();
                    swap = Integer.parseInt(cadSWAP);
                }
                linea++;
            } }
    }
    

    /**
     * Creates new form Home
     */
    public Home() {
        initComponents();
        Home.lbMetodo.setVisible(false);
        modelo = (DefaultTableModel)Home.TablaProcesos.getModel();
        try {
            CalcularMemoria();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Leer elHilo = new Leer();
        elHilo.start();
        LeerProcesos hilo = new LeerProcesos();
        hilo.start();
        
        
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Estrategias = new javax.swing.ButtonGroup();
        Algoritmos = new javax.swing.ButtonGroup();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtSimulaciom = new javax.swing.JTextArea();
        radioEstrategia1 = new javax.swing.JRadioButton();
        radioEstrategia2 = new javax.swing.JRadioButton();
        radioEstrategia3 = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        rFCFS = new javax.swing.JRadioButton();
        rSCF = new javax.swing.JRadioButton();
        rPrioridad = new javax.swing.JRadioButton();
        rRoudRobin = new javax.swing.JRadioButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtEjecucion = new javax.swing.JTextArea();
        AreaDibujo = new javax.swing.JPanel();
        lbMetodo = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtSWAP = new javax.swing.JTextField();
        txtSWAP2 = new javax.swing.JTextField();
        txtRAM = new javax.swing.JTextField();
        txtRAM2 = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        TablaProcesos = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simulador De Procesos ");
        setIconImages(null);

        jTabbedPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane2MouseClicked(evt);
            }
        });

        txtSimulaciom.setColumns(20);
        txtSimulaciom.setRows(5);
        jScrollPane1.setViewportView(txtSimulaciom);

        Estrategias.add(radioEstrategia1);
        radioEstrategia1.setText("1er. Estrategia");
        radioEstrategia1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioEstrategia1ActionPerformed(evt);
            }
        });

        Estrategias.add(radioEstrategia2);
        radioEstrategia2.setText("2da. Estrategia");
        radioEstrategia2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioEstrategia2ActionPerformed(evt);
            }
        });

        Estrategias.add(radioEstrategia3);
        radioEstrategia3.setText("3er. Estrategia");
        radioEstrategia3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioEstrategia3ActionPerformed(evt);
            }
        });

        jButton1.setText("Tiempos");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setText("Tiempo");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(124, 124, 124)
                        .addComponent(jLabel5))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(radioEstrategia1)
                        .addGap(26, 26, 26)
                        .addComponent(radioEstrategia2)
                        .addGap(18, 18, 18)
                        .addComponent(radioEstrategia3)
                        .addGap(35, 35, 35)
                        .addComponent(jButton1)))
                .addContainerGap(173, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(radioEstrategia3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(radioEstrategia1)
                        .addComponent(radioEstrategia2)))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPane2.addTab("Simulacion", jPanel1);

        Algoritmos.add(rFCFS);
        rFCFS.setText("FCFS");
        rFCFS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rFCFSActionPerformed(evt);
            }
        });

        Algoritmos.add(rSCF);
        rSCF.setText("SCF");
        rSCF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSCFActionPerformed(evt);
            }
        });

        Algoritmos.add(rPrioridad);
        rPrioridad.setText("Prioridad");
        rPrioridad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rPrioridadActionPerformed(evt);
            }
        });

        Algoritmos.add(rRoudRobin);
        rRoudRobin.setText("RoundRobin");
        rRoudRobin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rRoudRobinActionPerformed(evt);
            }
        });

        txtEjecucion.setColumns(20);
        txtEjecucion.setLineWrap(true);
        txtEjecucion.setRows(5);
        txtEjecucion.setWrapStyleWord(true);
        jScrollPane2.setViewportView(txtEjecucion);

        javax.swing.GroupLayout AreaDibujoLayout = new javax.swing.GroupLayout(AreaDibujo);
        AreaDibujo.setLayout(AreaDibujoLayout);
        AreaDibujoLayout.setHorizontalGroup(
            AreaDibujoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        AreaDibujoLayout.setVerticalGroup(
            AreaDibujoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 486, Short.MAX_VALUE)
        );

        lbMetodo.setBackground(java.awt.Color.red);
        lbMetodo.setFont(new java.awt.Font("Ubuntu", 1, 36)); // NOI18N
        lbMetodo.setText("METODO");
        lbMetodo.setOpaque(true);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(rFCFS)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rSCF)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rPrioridad)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rRoudRobin)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addComponent(lbMetodo)
                        .addContainerGap(170, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(AreaDibujo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rFCFS)
                            .addComponent(rSCF)
                            .addComponent(rPrioridad)
                            .addComponent(rRoudRobin)))
                    .addComponent(lbMetodo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(AreaDibujo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane2.addTab("Ejecucion", jPanel2);

        jLabel1.setText("Memoria SWAP Libre:");

        jLabel2.setText("% Memoria SWAP Libre:");

        jLabel3.setText("Memoria RAM Libre:");

        jLabel4.setText("% Memoria RAM Libre:");

        txtSWAP.setEditable(false);

        txtSWAP2.setEditable(false);

        txtRAM.setEditable(false);

        txtRAM2.setEditable(false);

        TablaProcesos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Identificador", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        TablaProcesos.setEnabled(false);
        jScrollPane3.setViewportView(TablaProcesos);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtSWAP2, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                            .addComponent(txtSWAP))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtRAM)
                            .addComponent(txtRAM2, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE))
                        .addGap(38, 38, 38))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 727, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(txtSWAP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRAM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(txtSWAP2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRAM2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Reportes", jPanel3);

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Run");

        jMenuItem1.setText("Run Simulacion");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuItem2.setText("Run Procesos");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 797, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTabbedPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane2MouseClicked

    }//GEN-LAST:event_jTabbedPane2MouseClicked

    private void radioEstrategia2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioEstrategia2ActionPerformed
        // TODO add your handling code here:
        estrategia = 2;
  
    }//GEN-LAST:event_radioEstrategia2ActionPerformed

    private void radioEstrategia1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioEstrategia1ActionPerformed
        // TODO add your handling code here:
        GenerarDot1(tiempos.get(t1),"t"+t1);
        t1=1;
    }//GEN-LAST:event_radioEstrategia1ActionPerformed

    private void radioEstrategia3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioEstrategia3ActionPerformed
        // TODO add your handling code here:
        EscribirArchivo(this.txtSimulaciom.getText(),"Simulacion.txt");
    }//GEN-LAST:event_radioEstrategia3ActionPerformed

    private void rFCFSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rFCFSActionPerformed
        // TODO add your handling code here:
        CompilarProcesos();
        System.out.println("Hay "+procesos.size()+" Procesos dados de alta");
        cola = new FCFS();
        for (int i = 0; i<procesos.size(); i++)
        {
            cola.InsertarEnOrden(procesos.get(i));
        }
        Proceso n = cola.getCabeza();
        
        while (n!=null)
        {
            System.out.println(n.getId()+", "+n.getInicio()+", "+n.getDuracion()+", "+n.getPrioridad());
            n=n.getSig();
        }
        
        cola.CalcularTiempos();
        cola.InfoProcesos();
        
        Dibujar nuevo = new Dibujar();
        nuevo.setBounds(0,0,421,493);
        nuevo.setOpaque(false);
        
        this.AreaDibujo.add(nuevo);
        this.AreaDibujo.repaint();
    }//GEN-LAST:event_rFCFSActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        if(!"".equals(txtSimulaciom.getText()) )
        {
            EscribirArchivo(this.txtSimulaciom.getText(),"Simulacion.txt");
            AnalizarSimulacion("Simulacion.txt");
        }
        
        System.out.println("Se han dado de alta "+ tiempos.size() +" Tiempos");
        for(int i=0; i<tiempos.size();i++)
        {
            System.out.println("El tiempo "+tiempos.get(i).id+" y los procesos que tiene son "+tiempos.get(i).proceso.size());
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        if(!"".equals(txtEjecucion.getText()) )
        {
            EscribirArchivo(this.txtEjecucion.getText(),"Ejecucion.txt");
            AnalizarEjecucion("Ejecucion.txt");
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void rSCFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSCFActionPerformed
        // TODO add your handling code here
        CompilarProcesos();
        System.out.println("Hay "+procesos.size()+" Procesos dados de alta");
        cola = new FCFS();
        for (int i = 0; i<procesos.size(); i++)
        {
            cola.InsertarEnOrden(procesos.get(i));
        }
        Proceso n = cola.getCabeza();
        
        while (n!=null)
        {
            System.out.println(n.getId()+", "+n.getInicio()+", "+n.getDuracion()+", "+n.getPrioridad());
            n=n.getSig();
        }
        
        cola.CalcularTiemposSJF();
        cola.InfoProcesos();
        
        Dibujar nuevo = new Dibujar();
        nuevo.setBounds(0,0,421,493);
        nuevo.setOpaque(false);
        
        this.AreaDibujo.add(nuevo);
        this.AreaDibujo.repaint();
    }//GEN-LAST:event_rSCFActionPerformed

    private void rPrioridadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rPrioridadActionPerformed
        // TODO add your handling code here:
        CompilarProcesos();
        System.out.println("Hay "+procesos.size()+" Procesos dados de alta");
        cola = new FCFS();
        for (int i = 0; i<procesos.size(); i++)
        {
            cola.InsertarEnOrden(procesos.get(i));
        }
        Proceso n = cola.getCabeza();
        
        while (n!=null)
        {
            System.out.println(n.getId()+", "+n.getInicio()+", "+n.getDuracion()+", "+n.getPrioridad());
            n=n.getSig();
        }
        
        cola.CalcularTiemposPrioridad();
        cola.InfoProcesos();
        
        Dibujar nuevo = new Dibujar();
        nuevo.setBounds(0,0,421,493);
        nuevo.setOpaque(false);
        
        this.AreaDibujo.add(nuevo);
        this.AreaDibujo.repaint();
    }//GEN-LAST:event_rPrioridadActionPerformed

    private void rRoudRobinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rRoudRobinActionPerformed
        // TODO add your handling code here:
        metodo = 4;
        System.out.println("Hay "+procesos.size()+" Procesos dados de alta");
    }//GEN-LAST:event_rRoudRobinActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (t1<tiempos.size())
        {
        GenerarDot1(tiempos.get(t1),"t"+t1);
        this.jLabel5.setText("Tiempo: "+(t1+1));
        t1++;
        }
        else
        {
            t1=0;
            GenerarDot1(tiempos.get(t1),"t"+t1);
        this.jLabel5.setText("Tiempo: "+(t1+1));
        t1++;
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String args[]) throws IOException {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Home().setVisible(true);
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup Algoritmos;
    private javax.swing.JPanel AreaDibujo;
    private javax.swing.ButtonGroup Estrategias;
    public static javax.swing.JTable TablaProcesos;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane2;
    public static javax.swing.JLabel lbMetodo;
    private javax.swing.JRadioButton rFCFS;
    private javax.swing.JRadioButton rPrioridad;
    private javax.swing.JRadioButton rRoudRobin;
    private javax.swing.JRadioButton rSCF;
    private javax.swing.JRadioButton radioEstrategia1;
    private javax.swing.JRadioButton radioEstrategia2;
    private javax.swing.JRadioButton radioEstrategia3;
    private javax.swing.JTextArea txtEjecucion;
    public static javax.swing.JTextField txtRAM;
    public static javax.swing.JTextField txtRAM2;
    public static javax.swing.JTextField txtSWAP;
    public static javax.swing.JTextField txtSWAP2;
    private javax.swing.JTextArea txtSimulaciom;
    // End of variables declaration//GEN-END:variables
}
