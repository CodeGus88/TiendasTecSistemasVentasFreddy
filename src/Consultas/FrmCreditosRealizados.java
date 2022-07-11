/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Consultas;

import Conexion.ClsConexion;
import Entidad.*;
import Negocio.*;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class FrmCreditosRealizados extends javax.swing.JInternalFrame {
    private Connection connection=new ClsConexion().getConection();
    static ResultSet rs=null;
    DefaultTableModel dtm=new DefaultTableModel();
    DefaultTableModel dtm1=new DefaultTableModel();
//    String id[]=new String[50];
//    static int intContador;
    Date fecha_ini,fecha_fin;
    String busqueda,Total; // documento,criterio,
//    boolean valor=true;
    int n=0;
    
    public FrmCreditosRealizados() {
        initComponents();
        lblIdCredito.setVisible(false);

        //---------------------ANCHO Y ALTO DEL FORM----------------------
        this.setSize(769, 338);

        //---------------------FECHA ACTUAL-------------------------------
        Date date=new Date();
        String format=new String("dd/MM/yyyy");
        SimpleDateFormat formato=new SimpleDateFormat(format);
        dcFechaini.setDate(date);
        dcFechafin.setDate(date);
        
        BuscarCredito();
        CrearTabla(); 
        CantidadTotal();
    }

//-----------------------------------------------------------------------------------------------
//--------------------------------------METODOS--------------------------------------------------
//-----------------------------------------------------------------------------------------------

    void CrearTabla(){
   //--------------------PRESENTACION DE JTABLE----------------------
      
        TableCellRenderer render = new DefaultTableCellRenderer() { 

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) { 
                //aqui obtengo el render de la calse superior 
                JLabel l = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
                //Determinar Alineaciones   
                    if(column==0 || column==2 || column==4 || column==5 || column==6 || column==7 || column==8){
                        l.setHorizontalAlignment(SwingConstants.CENTER); 
                    }else{
                        l.setHorizontalAlignment(SwingConstants.LEFT);
                    }

                //Colores en Jtable        
               if (isSelected) {
                    l.setBackground(new Color(203, 159, 41));
                    //l.setBackground(new Color(168, 198, 238));
                    l.setForeground(Color.WHITE); 
                }else{
                    l.setForeground(Color.BLACK);
                    if (row % 2 == 0) {
                        l.setBackground(Color.WHITE);
                    } else {
                        //l.setBackground(new Color(232, 232, 232));
                        l.setBackground(new Color(254, 227, 152));
                    }
                }     
                return l; 
            } 
        }; 
        
        //Agregar Render
        for (int i=0;i<tblCredito.getColumnCount();i++){
            tblCredito.getColumnModel().getColumn(i).setCellRenderer(render);
        }
      
        //Activar ScrollBar
        tblCredito.setAutoResizeMode(tblCredito.AUTO_RESIZE_OFF);

        //Anchos de cada columna
        int[] anchos = {50,160,70,120,80,40,60,60,80,80,80};
        for(int i = 0; i < tblCredito.getColumnCount(); i++) {
            tblCredito.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }

    }
  void BuscarCredito(){
//      String titulos[]={"ID","Cliente","Fecha","Empleado","Documento","Serie","Número","Estado","Valor Venta","Descuento","Total"};
        String titulos[]={"ID","Cliente","Fecha","Empleado","Documento","Serie","Número","Estado","Valor Venta C.","Descuento","Total"};
        dtm.setColumnIdentifiers(titulos);
        
        ClsCredito credito=new ClsCredito();

        fecha_ini=dcFechaini.getDate();
        fecha_fin=dcFechafin.getDate();

        try{
            rs=credito.listarCreditoPorFecha("consultar",fecha_ini,fecha_fin,"");
            boolean encuentra=false;
            String Datos[]=new String[11];
            int f,i;
            f=dtm.getRowCount();
            if(f>0){
                for(i=0;i<f;i++){
                    dtm.removeRow(0);
                }
            }
            while(rs.next()){
                Datos[0]=(String) rs.getString(1);
                Datos[1]=(String) rs.getString(2);
                Datos[2]=(String) rs.getString(3);
                Datos[3]=(String) rs.getString(4);
                Datos[4]=(String) rs.getString(5);
                Datos[5]=(String) rs.getString(6);
                Datos[6]=(String) rs.getString(7);
                Datos[7]=(String) rs.getString(8);
                Datos[8]=(String) rs.getString(9);
                Datos[9]=(String) rs.getString(10);
                Datos[10]=(String) rs.getString(11);

                dtm.addRow(Datos);
                encuentra=true;

            }
            if(encuentra=false){
                JOptionPane.showMessageDialog(null, "¡No se encuentra!");
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
        tblCredito.setModel(dtm);
    }
      
    void CrearTablaDetalle(){
   //--------------------PRESENTACION DE JTABLE DETALLE CREDITO----------------------
      
        TableCellRenderer render = new DefaultTableCellRenderer() { 

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) { 
                //aqui obtengo el render de la calse superior 
                JLabel l = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
                //Determinar Alineaciones   
                    if(column==0 || column==1 || column==2 || column==5 || column==6){
                        l.setHorizontalAlignment(SwingConstants.CENTER); 
                    }else{
                        l.setHorizontalAlignment(SwingConstants.LEFT);
                    }

                //Colores en Jtable        
                if (isSelected) {
                    l.setBackground(new Color(51, 152, 255));
                    //l.setBackground(new Color(168, 198, 238));
                    l.setForeground(Color.WHITE); 
                }else{
                    l.setForeground(Color.BLACK);
                    if (row % 2 == 0) {
                        l.setBackground(Color.WHITE);
                    } else {
                        //l.setBackground(new Color(232, 232, 232));
                        l.setBackground(new Color(229, 246, 245));
                    }
                }
                return l; 
            } 
        }; 
        
        //Agregar Render
        for (int i=0;i<tblDetalleCredito.getColumnCount();i++){
            tblDetalleCredito.getColumnModel().getColumn(i).setCellRenderer(render);
        }
      
        //Activar ScrollBar
        tblDetalleCredito.setAutoResizeMode(tblCredito.AUTO_RESIZE_OFF);

        //Anchos de cada columna
        int[] anchos = {50,60,80,200,200,60,60,60};
        for(int i = 0; i < tblDetalleCredito.getColumnCount(); i++) {
            tblDetalleCredito.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
        //Ocultar Columnas
        ocultarColumnas(tblDetalleCredito,new int[]{1});
    }
  void BuscarCreditoDetalle(){
        String titulos[]={"ID","ID Prod.","Cód Producto","Nombre","Descripción","Cantidad","Precio","Total"};
        dtm1.setColumnIdentifiers(titulos);
        ClsDetalleCredito detalleCredito=new ClsDetalleCredito ();
        busqueda=lblIdCredito.getText();

        try{
            rs=detalleCredito.listarDetalleCreditoPorParametro("id",busqueda);
            boolean encuentra=false;
            String Datos[]=new String[8];
            int f,i;
            f=dtm1.getRowCount();
            if(f>0){
                for(i=0;i<f;i++){
                    dtm1.removeRow(0);
                }
            }
            while(rs.next()){
                Datos[0]=(String) rs.getString(1);
                Datos[1]=(String) rs.getString(2);
                Datos[2]=(String) rs.getString(3);
                Datos[3]=(String) rs.getString(4);
                Datos[4]=(String) rs.getString(5);
                Datos[5]=(String) rs.getString(6);
                Datos[6]=(String) rs.getString(7);
                Datos[7]=(String) rs.getString(8);
                dtm1.addRow(Datos);
                encuentra=true;

            }
            if(encuentra=false){
                JOptionPane.showMessageDialog(null, "¡No se encuentra!");
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
        tblDetalleCredito.setModel(dtm1);
    }
    void CantidadTotal(){
        Total= String.valueOf(tblCredito.getRowCount());   
        lblEstado.setText("Se cargaron " + Total + " registros");      
    }
    void restablecerCantidades(){
        String strId;
        String idCredito=lblIdCredito.getText();
        ClsProducto productos=new ClsProducto();
        ClsEntidadProducto producto=new ClsEntidadProducto();
        int fila=0;
        double cant = 0,ncant,stock;   
        fila =tblDetalleCredito.getRowCount();
        for (int f=0; f<fila; f++){          
            try{
                ClsProducto oProducto=new ClsProducto();
                
                rs= oProducto.listarProductoActivoPorParametro("id",((String) tblDetalleCredito.getValueAt(f, 1)));
                while (rs.next()) {
                            cant=Double.parseDouble(rs.getString(5));
                }               

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,ex.getMessage());
                System.out.println(ex.getMessage());
            }        

            
            
        strId = ((String) tblDetalleCredito.getValueAt(f, 1));
        ncant=Double.parseDouble(String.valueOf(tblDetalleCredito.getModel().getValueAt(f, 5)));
        stock=cant+ncant;
        producto.setStrStockProducto(String.valueOf(stock));
        productos.actualizarProductoStock(strId, producto);

    }
    }
    private void ocultarColumnas(JTable tbl, int columna[]){
        for(int i=0;i<columna.length;i++)
        {
             tbl.getColumnModel().getColumn(columna[i]).setMaxWidth(0);
             tbl.getColumnModel().getColumn(columna[i]).setMinWidth(0);
             tbl.getTableHeader().getColumnModel().getColumn(columna[i]).setMaxWidth(0);
             tbl.getTableHeader().getColumnModel().getColumn(columna[i]).setMinWidth(0);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane5 = new javax.swing.JScrollPane();
        tblCredito = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        dcFechaini = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        dcFechafin = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        lblIdCredito = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        btnVerDetalle = new javax.swing.JButton();
        lblEstado = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblDetalleCredito = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setTitle("Reporte de Ventas a Crédito Realizadas");
        setToolTipText("");
        getContentPane().setLayout(null);

        tblCredito.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblCredito.setRowHeight(22);
        tblCredito.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCreditoMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblCredito);

        getContentPane().add(jScrollPane5);
        jScrollPane5.setBounds(10, 110, 730, 140);

        jPanel1.setBackground(new java.awt.Color(255, 153, 0));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Opciones de busqueda y anulación"));
        jPanel1.setLayout(null);

        dcFechaini.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPanel1.add(dcFechaini);
        dcFechaini.setBounds(20, 40, 100, 25);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("DESDE:");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(20, 20, 70, 20);

        dcFechafin.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPanel1.add(dcFechafin);
        dcFechafin.setBounds(130, 40, 100, 25);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("HASTA:");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(130, 20, 70, 20);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Buscar_32.png"))); // NOI18N
        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(250, 20, 110, 50);
        jPanel1.add(lblIdCredito);
        lblIdCredito.setBounds(320, 20, 40, 20);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/printer.png"))); // NOI18N
        jButton2.setText("Imprimir");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);
        jButton2.setBounds(380, 20, 110, 50);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(10, 10, 730, 90);

        btnVerDetalle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/busqueda_detallada.png"))); // NOI18N
        btnVerDetalle.setText("Ver Detalle");
        btnVerDetalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerDetalleActionPerformed(evt);
            }
        });
        getContentPane().add(btnVerDetalle);
        btnVerDetalle.setBounds(600, 260, 140, 40);
        getContentPane().add(lblEstado);
        lblEstado.setBounds(10, 250, 230, 20);

        tblDetalleCredito.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblDetalleCredito.setRowHeight(22);
        tblDetalleCredito.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDetalleCreditoMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tblDetalleCredito);

        getContentPane().add(jScrollPane6);
        jScrollPane6.setBounds(10, 310, 730, 140);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblCreditoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCreditoMouseClicked
        int fila;
        DefaultTableModel defaultTableModel = new DefaultTableModel();
        fila = tblCredito.getSelectedRow();

        if (fila == -1){
            JOptionPane.showMessageDialog(null, "Se debe seleccionar un registro");
        }else{
            defaultTableModel = (DefaultTableModel)tblCredito.getModel();
            //strCodigo =  ((String) defaultTableModel.getValueAt(fila, 0));
            lblIdCredito.setText((String) defaultTableModel.getValueAt(fila, 0));

        }
        BuscarCreditoDetalle();
        CrearTablaDetalle();
    }//GEN-LAST:event_tblCreditoMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        BuscarCredito();
        CrearTabla();
        CantidadTotal();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnVerDetalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerDetalleActionPerformed
        if(tblCredito.getSelectedRows().length > 0 ) {

            if(n==0){
                this.setSize(769, 507);
                n=1;
                btnVerDetalle.setText("Ocultar Detalle");
            }else if(n==1){
                this.setSize(769, 338);
                n=0;
                btnVerDetalle.setText("Ver Detalle");
            }
            BuscarCreditoDetalle();
            CrearTablaDetalle();
        }else{
            JOptionPane.showMessageDialog(null, "¡Se debe seleccionar un registro de venta a crédito!");
        }

    }//GEN-LAST:event_btnVerDetalleActionPerformed

    private void tblDetalleCreditoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDetalleCreditoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDetalleCreditoMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Map p=new HashMap();

        Date fecha_inicial=(dcFechaini.getDate());
        Date fecha_final=(dcFechafin.getDate());

        p.put("fecha_ini" ,fecha_inicial);
        p.put("fecha_fin" ,fecha_final);

        JasperReport report;
        JasperPrint print;
        try{

            report=JasperCompileManager.compileReport(new File("").getAbsolutePath()+ "/src/Reportes/RptCreditosRealizados.jrxml");
            print=JasperFillManager.fillReport(report, p,connection);
            JasperViewer view=new JasperViewer(print,false);
            view.setTitle("Reporte General de Ventas a Crédito Realizadas");
            view.setVisible(true);
        }catch(JRException e){
            e.printStackTrace();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnVerDetalle;
    private com.toedter.calendar.JDateChooser dcFechafin;
    private com.toedter.calendar.JDateChooser dcFechaini;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblIdCredito;
    private javax.swing.JTable tblCredito;
    private javax.swing.JTable tblDetalleCredito;
    // End of variables declaration//GEN-END:variables
}