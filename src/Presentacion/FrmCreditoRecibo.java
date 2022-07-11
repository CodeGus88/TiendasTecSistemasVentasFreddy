/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Presentacion;
import Conexion.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.*;
import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import Negocio.*;
import java.io.File;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class FrmCreditoRecibo extends javax.swing.JInternalFrame {
private Connection connection=new ClsConexion().getConection();

    static ResultSet rs=null;
    static ResultSet rsTotal=null;
    DefaultTableModel dtm=new DefaultTableModel();
    String criterio,busqueda;
    String Total;
    String idcredito;
    String id[]=new String[50];
    static int intContador;
    
    public FrmCreditoRecibo() {
        initComponents();
        txtDocumentoCredito.setVisible(false);
        //---------------------ANCHO Y ALTO DEL FORM----------------------
        this.setSize(397, 111);
    }

//-----------------------------------------------------------------------------------------------
//--------------------------------------METODOS--------------------------------------------------
//-----------------------------------------------------------------------------------------------
    void obtenerUltimoIdCredito(){
        try{
        ClsCredito oCredito=new ClsCredito(); 
        rs= oCredito.obtenerUltimoIdCredito();
            while (rs.next()) {
                idcredito=String.valueOf(rs.getInt(1));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,ex.getMessage());
            System.out.println(ex.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnVerRecibo = new javax.swing.JButton();
        txtDocumentoCredito = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        getContentPane().setLayout(null);

        btnVerRecibo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Ver_recibo.png"))); // NOI18N
        btnVerRecibo.setText("Ver Recibo");
        btnVerRecibo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerReciboActionPerformed(evt);
            }
        });
        getContentPane().add(btnVerRecibo);
        btnVerRecibo.setBounds(60, 10, 130, 60);
        getContentPane().add(txtDocumentoCredito);
        txtDocumentoCredito.setBounds(20, 20, 20, 30);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/printer.png"))); // NOI18N
        jButton1.setText("Imprimir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(210, 10, 130, 60);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnVerReciboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerReciboActionPerformed
        if(txtDocumentoCredito.getText().equals("FACTURA")){
            Map p=new HashMap();
            p.put("busqueda", idcredito);
//            String imagen="C:/Users/Edgar/Desktop/Proyecto_Casita/Sistema_Casita/src/Iconos/casita.png";
//            p.put("imagen", this.getClass().getResourceAsStream(imagen)); 
           
            JasperReport report;
            JasperPrint print;
    
            try{
                report=JasperCompileManager.compileReport(new File("").getAbsolutePath()+ "/src/Reportes/RptCreditoFactura.jrxml");
                print=JasperFillManager.fillReport(report, p,connection);
                JasperViewer view=new JasperViewer(print,false);
                view.setTitle("Reporte del Crédito");
                view.setVisible(true);
            }catch(JRException e){
                e.printStackTrace();
            }
        }else if(txtDocumentoCredito.getText().equals("BOLETA")){
            Map p=new HashMap();
            p.put("busqueda", idcredito);
            JasperReport report;
            JasperPrint print;
            try{
                report=JasperCompileManager.compileReport(new File("").getAbsolutePath()+ "/src/Reportes/RptCreditoBoleta.jrxml");
                print=JasperFillManager.fillReport(report, p,connection);
                JasperViewer view=new JasperViewer(print,false);
                view.setTitle("Reporte del Crédito");
                view.setVisible(true);
            }catch(JRException e){
                e.printStackTrace();
            }
          }else if(txtDocumentoCredito.getText().equals("TICKET")){
            Map p=new HashMap();
            p.put("busqueda", idcredito);
            JasperReport report;
            JasperPrint print;
            try{
                report=JasperCompileManager.compileReport(new File("").getAbsolutePath()+ "/src/Reportes/RptCreditoTicket.jrxml");
                print=JasperFillManager.fillReport(report, p,connection);
                JasperViewer view=new JasperViewer(print,false);
                view.setTitle("Reporte del Crédito");
                view.setVisible(true);
            }catch(JRException e){
                e.printStackTrace();
            }
        }
        

    }//GEN-LAST:event_btnVerReciboActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
obtenerUltimoIdCredito();
    }//GEN-LAST:event_formComponentShown

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(txtDocumentoCredito.getText().equals("FACTURA")){
            Map p=new HashMap();
            p.put("busqueda", idcredito);
           
            JasperReport report;
            JasperPrint print;
    
            try{
                report=JasperCompileManager.compileReport(new File("").getAbsolutePath()+ "/src/Reportes/RptCreditoFactura.jrxml");
                print=JasperFillManager.fillReport(report, p,connection);
                JasperViewer view=new JasperViewer(print,false);
                view.setTitle("Reporte del Crédito");
                view.setVisible(true);
            }catch(JRException e){
                e.printStackTrace();
            }
        }else if(txtDocumentoCredito.getText().equals("BOLETA")){
            Map p=new HashMap();
            p.put("busqueda", idcredito);
            JasperReport report;
            JasperPrint print;
            try{
                report=JasperCompileManager.compileReport(new File("").getAbsolutePath()+ "/src/Reportes/RptCreditoBoleta.jrxml");
                print=JasperFillManager.fillReport(report, p,connection);
                JasperViewer view=new JasperViewer(print,false);
                view.setTitle("Reporte del Crédito");
                view.setVisible(true);
            }catch(JRException e){
                e.printStackTrace();
            }
          }else if(txtDocumentoCredito.getText().equals("TICKET")){
            Map p=new HashMap();
            p.put("busqueda", idcredito);
            JasperReport report;
            JasperPrint print;
            try{
                report=JasperCompileManager.compileReport(new File("").getAbsolutePath()+ "/src/Reportes/RptCreditoTicket.jrxml");
                print=JasperFillManager.fillReport(report, p,connection);
                JasperViewer view=new JasperViewer(print,false);
//                view.setTitle("Reporte del Crédito");
//                view.setVisible(true);
              
                
                JasperPrintManager.printReport(print, false);
                
            }catch(JRException e){
                e.printStackTrace();
            }
        }
    
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnVerRecibo;
    private javax.swing.JButton jButton1;
    public static javax.swing.JTextField txtDocumentoCredito;
    // End of variables declaration//GEN-END:variables
}
