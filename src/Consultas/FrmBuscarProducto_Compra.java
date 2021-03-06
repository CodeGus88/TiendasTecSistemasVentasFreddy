/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Consultas;

import Presentacion.*;
import Entidad.ClsEntidadProducto;
import Negocio.ClsProducto;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 * @author DAYPER-PERU
 */
public class FrmBuscarProducto_Compra extends javax.swing.JInternalFrame {


    static ResultSet rs=null;
    DefaultTableModel dtm=new DefaultTableModel();
    public String Total;
    String criterio,busqueda;
    public FrmBuscarProducto_Compra() {

        initComponents();
        buttonGroup1.add(rbtnCodigo);
        buttonGroup1.add(rbtnNombre);
        buttonGroup1.add(rbtnDescripcion);

        
        //--------------------PANEL - PRODUCTO----------------------------
        
        actualizarTablaProducto();
        CrearTablaProducto();
        //---------------------ANCHO Y ALTO DEL FORM----------------------
        CantidadTotal();
        this.setSize(836, 400);
        EventQueue.invokeLater(() -> txtBusqueda.requestFocusInWindow());

    }

//-----------------------------------------------------------------------------------------------
//----------------------------------PANEL - PRODUCTO---------------------------------------------
//-----------------------------------------------------------------------------------------------
    void actualizarTablaProducto(){
       String titulos[]={"ID","Cód. de Barras","Nombre","Descripción","Stock","P. Costo"};
              
       ClsProducto productos=new ClsProducto();
       ArrayList<ClsEntidadProducto> producto=productos.listarProductoActivo();
       Iterator iterator=producto.iterator();
       DefaultTableModel defaultTableModel =  new DefaultTableModel(null, titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {

                return false;

            }
        };
       String fila[]=new String[6];
       while(iterator.hasNext()){
           ClsEntidadProducto Producto=new ClsEntidadProducto();
           Producto=(ClsEntidadProducto) iterator.next();
           fila[0]=Producto.getStrIdProducto();
           fila[1]=Producto.getStrCodigoProducto();       
           fila[2]=Producto.getStrNombreProducto();
           fila[3]=Producto.getStrDescripcionProducto();
           fila[4]=Producto.getStrStockProducto();
           fila[5]=Producto.getStrPrecioCostoProducto();
           defaultTableModel.addRow(fila);               
       }
       tblProducto.setModel(defaultTableModel);
   }
    void CrearTablaProducto(){
   //--------------------PRESENTACION DE JTABLE PRODUCTO----------------------
      
        TableCellRenderer render = new DefaultTableCellRenderer() { 

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) { 
                //aqui obtengo el render de la calse superior 
                JLabel l = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
                //Determinar Alineaciones   
                    if(column==0 || column==1 || column==4 || column==5){
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
        for (int i=0;i<tblProducto.getColumnCount();i++){
            tblProducto.getColumnModel().getColumn(i).setCellRenderer(render);
        }
      
        //Activar ScrollBar
        tblProducto.setAutoResizeMode(tblProducto.AUTO_RESIZE_OFF);

        //Anchos de cada columna
        int[] anchos = {40,100,150,200,60,60};
        for(int i = 0; i < tblProducto.getColumnCount(); i++) {
            tblProducto.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
   }
    void BuscarProductoPanel(){
        String titulos[]={"ID","Cód. de Barras","Nombre","Descripción","Stock","P. Costo"};
//        dtm.setColumnIdentifiers(titulos);
        dtm = new DefaultTableModel(null, titulos){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ClsProducto categoria=new ClsProducto();
        busqueda=txtBusqueda.getText();
        if(rbtnCodigo.isSelected()){
            criterio="codigo";
        }else if(rbtnNombre.isSelected()){
            criterio="nombre";
        }else if(rbtnDescripcion.isSelected()){
            criterio="descripcion";
        }
        try{
            rs=categoria.listarProductoActivoPorParametro(criterio,busqueda);
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
                Datos[5]=(String) rs.getString(7);
                dtm.addRow(Datos);
                encuentra=true;

            }
            if(encuentra=false){
                JOptionPane.showMessageDialog(null, "¡No se encuentra!");
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
        tblProducto.setModel(dtm);
    }
    void CantidadTotal(){
        Total= String.valueOf(tblProducto.getRowCount());   
        lblEstado.setText("Se cargaron " + Total + " registros");      
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        rbtnCodigo = new javax.swing.JRadioButton();
        txtBusqueda = new javax.swing.JTextField();
        rbtnNombre = new javax.swing.JRadioButton();
        rbtnDescripcion = new javax.swing.JRadioButton();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblProducto = new javax.swing.JTable();
        btnSalir = new javax.swing.JButton();
        lblEstado = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setTitle("Consultar Productos");
        getContentPane().setLayout(null);

        rbtnCodigo.setText("Cód. Producto");
        rbtnCodigo.setOpaque(false);
        rbtnCodigo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbtnCodigoStateChanged(evt);
            }
        });
        getContentPane().add(rbtnCodigo);
        rbtnCodigo.setBounds(30, 22, 110, 23);

        txtBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBusquedaKeyReleased(evt);
            }
        });
        getContentPane().add(txtBusqueda);
        txtBusqueda.setBounds(30, 50, 370, 30);

        rbtnNombre.setText("Nombre");
        rbtnNombre.setOpaque(false);
        rbtnNombre.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbtnNombreStateChanged(evt);
            }
        });
        getContentPane().add(rbtnNombre);
        rbtnNombre.setBounds(140, 22, 80, 23);

        rbtnDescripcion.setText("Descripción");
        rbtnDescripcion.setOpaque(false);
        getContentPane().add(rbtnDescripcion);
        rbtnDescripcion.setBounds(230, 22, 100, 23);

        jLabel18.setBackground(new java.awt.Color(238, 240, 247));
        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel18.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Criterios de Búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jLabel18.setOpaque(true);
        getContentPane().add(jLabel18);
        jLabel18.setBounds(10, 11, 410, 80);

        tblProducto.setModel(new javax.swing.table.DefaultTableModel(
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
        tblProducto.setRowHeight(22);
        tblProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductoMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblProducto);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(10, 101, 750, 260);

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/door_in.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSalir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        getContentPane().add(btnSalir);
        btnSalir.setBounds(680, 10, 80, 80);
        getContentPane().add(lblEstado);
        lblEstado.setBounds(10, 250, 180, 20);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rbtnCodigoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rbtnCodigoStateChanged
        txtBusqueda.setText("");
    }//GEN-LAST:event_rbtnCodigoStateChanged

    private void txtBusquedaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaKeyReleased
        BuscarProductoPanel();
        CrearTablaProducto();
        CantidadTotal();
    }//GEN-LAST:event_txtBusquedaKeyReleased

    private void rbtnNombreStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rbtnNombreStateChanged
        txtBusqueda.setText("");
    }//GEN-LAST:event_rbtnNombreStateChanged

    private void tblProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductoMouseClicked

        if(evt.getClickCount() == 2){
        
            int fila;

            DefaultTableModel defaultTableModel = new DefaultTableModel();

            fila = tblProducto.getSelectedRow();

            if (fila == -1) {
                JOptionPane.showMessageDialog(null, "Se debe seleccionar un registro");
            } else {
                defaultTableModel = (DefaultTableModel) tblProducto.getModel();
                Presentacion.FrmCompra.lblIdProducto.setText((String) defaultTableModel.getValueAt(fila, 0));
                Presentacion.FrmCompra.txtCodigoProducto.setText((String) defaultTableModel.getValueAt(fila, 1));
                Presentacion.FrmCompra.txtNombreProducto.setText((String) defaultTableModel.getValueAt(fila, 2));
                Presentacion.FrmCompra.txtDescripcionProducto.setText((String) defaultTableModel.getValueAt(fila, 3));
                Presentacion.FrmCompra.txtStockProducto.setText((String) defaultTableModel.getValueAt(fila, 4));
                Presentacion.FrmCompra.txtPrecioProducto.setText((String) defaultTableModel.getValueAt(fila, 5));
            }
        }

    }//GEN-LAST:event_tblProductoMouseClicked

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JRadioButton rbtnCodigo;
    private javax.swing.JRadioButton rbtnDescripcion;
    private javax.swing.JRadioButton rbtnNombre;
    private javax.swing.JTable tblProducto;
    private javax.swing.JTextField txtBusqueda;
    // End of variables declaration//GEN-END:variables
}
