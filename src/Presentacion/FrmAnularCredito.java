/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Presentacion;

import Entidad.*;
import Negocio.*;
import java.awt.Color;
import java.awt.Component;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import statics.Message;
import tools.toast.Toast;

/**
 *
 * @author DAYPER PERU
 */
public class FrmAnularCredito extends javax.swing.JInternalFrame {

    static ResultSet rs=null;
    DefaultTableModel dtm=new DefaultTableModel();
    DefaultTableModel dtm1=new DefaultTableModel();
    String id[]=new String[50];
    static int intContador;
    Date fecha_ini,fecha_fin;
    String documento,criterio,busqueda,Total;
    boolean valor=true;
    int n=0;
    public FrmAnularCredito() {
        initComponents();
        //lblIdCredito.setVisible(false);
        cargarComboTipoDocumento();
        //---------------------ANCHO Y ALTO DEL FORM----------------------
        this.setSize(759, 494);

        //---------------------FECHA ACTUAL-------------------------------
        Date date=new Date();
        String format=new String("dd/MM/yyyy");
        SimpleDateFormat formato=new SimpleDateFormat(format);
        dcFechaini.setDate(date);
        dcFechafin.setDate(date);
        
        BuscarVentaCredito();
        CrearTabla(); 
        CantidadTotal();
    }

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
        for (int i=0;i<tblCredito.getColumnCount();i++){
            tblCredito.getColumnModel().getColumn(i).setCellRenderer(render);
        }
      
        //Activar ScrollBar
        tblCredito.setAutoResizeMode(tblCredito.AUTO_RESIZE_OFF);

        //Anchos de cada columna
        int[] anchos = {50,160,70,120,80,40,60,60,80};
        for(int i = 0; i < tblCredito.getColumnCount(); i++) {
            tblCredito.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }

    }
  void BuscarVentaCredito(){
        String titulos[]={"ID","Cliente","Fecha","Empleado","Documento","Serie","Número","Estado","Total"};
        dtm.setColumnIdentifiers(titulos);
        
        ClsCredito ventaCredito=new ClsCredito();

        fecha_ini=dcFechaini.getDate();
        fecha_fin=dcFechafin.getDate();
        documento=cboTipoDocumento.getSelectedItem().toString();
        try{
            rs=ventaCredito.listarCreditoPorFecha("anular",fecha_ini,fecha_fin,documento);
            boolean encuentra=false;
            String Datos[]=new String[9];
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
    void cargarComboTipoDocumento(){
       ClsTipoDocumento documentos=new ClsTipoDocumento();
       DefaultComboBoxModel DefaultComboBoxModel=new DefaultComboBoxModel();
       DefaultComboBoxModel.removeAllElements();
             
        try{
            rs = documentos.listarTipoDocumentoPorParametro("descripcion", "");
            boolean encuentra = false;
            String Datos[] = new String [2];
            

            while (rs.next()){
                id[intContador]=(String) rs.getString(1);
                Datos[0] = (String) rs.getString(1);
                Datos[1] = (String) rs.getString(2);
                DefaultComboBoxModel.addElement((String) rs.getString(2));
                encuentra = true;
                intContador++;
            }      
           cboTipoDocumento.setModel(DefaultComboBoxModel);

            if(encuentra=false){
                JOptionPane.showMessageDialog(null, "No se encuentra");
            }      
            
        }catch(Exception ex){
            ex.printStackTrace();
        }  
    }
    
    void CrearTablaDetalle(){
   //--------------------PRESENTACION DE JTABLE DETALLE VENTA A CREDITO----------------------
      
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
  void BuscarVentaCreditoDetalle(){
        String titulos[]={"ID","ID Prod.","Cód Producto","Nombre","Descripción","Cantidad","Precio","Total"};
        dtm1.setColumnIdentifiers(titulos);
        ClsDetalleCredito detalleVentaCredito=new ClsDetalleCredito();
        busqueda=lblIdCredito.getText();

        try{
            rs=detalleVentaCredito.listarDetalleCreditoPorParametro("id",busqueda);
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
//        String idventaCredito=lblIdCredito.getText();
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cboTipoDocumento = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        btnAnularVentaCredito = new javax.swing.JButton();
        lblIdCredito = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblDetalleCredito = new javax.swing.JTable();
        btnVerDetalle = new javax.swing.JButton();
        lblEstado = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Anular Ventas a Crédito");
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
        dcFechaini.setBounds(20, 40, 120, 40);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("DESDE:");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(20, 20, 70, 20);

        dcFechafin.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPanel1.add(dcFechafin);
        dcFechafin.setBounds(150, 40, 130, 40);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("DOCUMENTO:");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(300, 20, 100, 20);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("HASTA:");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(150, 20, 70, 20);

        cboTipoDocumento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(cboTipoDocumento);
        cboTipoDocumento.setBounds(300, 40, 170, 40);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Buscar_32.png"))); // NOI18N
        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(600, 30, 110, 50);

        btnAnularVentaCredito.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/anular.png"))); // NOI18N
        btnAnularVentaCredito.setText("Anular");
        btnAnularVentaCredito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnularVentaCreditoActionPerformed(evt);
            }
        });
        jPanel1.add(btnAnularVentaCredito);
        btnAnularVentaCredito.setBounds(480, 30, 110, 50);
        jPanel1.add(lblIdCredito);
        lblIdCredito.setBounds(320, 20, 40, 20);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(10, 10, 730, 90);

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
        jScrollPane6.setBounds(10, 320, 730, 140);

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

        getAccessibleContext().setAccessibleName("Anular Ventas a Crédito");

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
        BuscarVentaCreditoDetalle();
        CrearTablaDetalle();
    }//GEN-LAST:event_tblCreditoMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        BuscarVentaCredito();
        CrearTabla();
        CantidadTotal();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tblDetalleCreditoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDetalleCreditoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDetalleCreditoMouseClicked

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
        BuscarVentaCreditoDetalle();
        CrearTablaDetalle();
    }else{
        JOptionPane.showMessageDialog(null, "¡Se debe seleccionar un registro de venta!");
    }
        

    }//GEN-LAST:event_btnVerDetalleActionPerformed

    private void btnAnularVentaCreditoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnularVentaCreditoActionPerformed
    int fila_s;
    String est_venta_credito;
    fila_s = tblCredito.getSelectedRow();
    est_venta_credito=String.valueOf(tblCredito.getModel().getValueAt(fila_s, 7));
    if(!est_venta_credito.equals("ANULADO")){
        if(tblCredito.getSelectedRows().length > 0 ) {
            int result = JOptionPane.showConfirmDialog(this, "¿Desea anular la venta a crédito?", "Mensaje del Sistema", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                ClsCredito ventasCredito=new ClsCredito();
                    ClsEntidadCredito credito=new ClsEntidadCredito();
                    credito.setStrEstadoCredito("ANULADO");
                    if(ventasCredito.actualizarCreditoEstado(lblIdCredito.getText(), credito))
                        Toast.makeText(Toast.SUCCESS, Message.SUCCESS_MESSAGE, Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(Toast.UNSUCCESS, Message.UNSUCCESS_MESSAGE, Toast.LENGTH_SHORT).show();

                    BuscarVentaCredito();
                    CrearTabla();
                                  restablecerCantidades();
                    }
            if (result == JOptionPane.NO_OPTION) {
                JOptionPane.showMessageDialog(null, "Anulación Cancelada!");
            }
        }else{
            JOptionPane.showMessageDialog(null, "¡Se debe seleccionar un registro de venta a crédito!");
        }
    }else{
        JOptionPane.showMessageDialog(null, "¡Esta venta a crédito ya ha sido ANULADA!");
    }   
    
    }//GEN-LAST:event_btnAnularVentaCreditoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnularVentaCredito;
    private javax.swing.JButton btnVerDetalle;
    private javax.swing.JComboBox cboTipoDocumento;
    private com.toedter.calendar.JDateChooser dcFechafin;
    private com.toedter.calendar.JDateChooser dcFechaini;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
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
