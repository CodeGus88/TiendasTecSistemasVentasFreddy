/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Consultas;
import Entidad.ClsEntidadProveedor;
import Negocio.ClsProveedor;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import statics.Design;
import statics.ScreenUses;
import statics.TableConfigurator;
/**
 *
 * @author DAYPER-PERU
 */
public class FrmBuscarProveedor_Compra extends javax.swing.JInternalFrame {

    static ResultSet rs=null;
    DefaultTableModel dtm=new DefaultTableModel();
    private String[] titles = {"ID", "NOMBRE O RAZÓN SOCIAL", "RUC", "DNI", "DIRECCIÓN", "TELÉFONO", "CELULAR", "EMAIL", "Nº CUENTA 1", "Nº CUENTA 2", "ESTADO", "OBSERVACIÓN"};
    private float[] widths = {5, 25, 7, 7, 25, 7, 7, 18, 10, 10, 6, 15};
    String criterio,busqueda,Total;
    public FrmBuscarProveedor_Compra() {
        initComponents();
        buttonGroup1.add(rbtnCodigo);
        buttonGroup1.add(rbtnNombre);
        buttonGroup1.add(rbtnRuc);
        buttonGroup1.add(rbtnDni);
        //--------------------PANEL - PRODUCTO----------------------------
        
        actualizarTablaProveedor();
        CrearTablaProveedor();
        //---------------------ANCHO Y ALTO DEL FORM----------------------
        CantidadTotal();
        
        tableConfigurator();
        design();
        panelsContigurator();
    }
    
    private void design(){
        panelBackgoud.setBackground(Design.COLOR_PRIMARY_DARK);
        btnSalir.setBackground(Design.COLOR_ACCENT);
        btnSalir.setBorder(Design.BORDER_BUTTON);
    }
    
    private void panelsContigurator(){
        panelBackgoud.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    }

    private void tableConfigurator() {
        panelBody.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                Component c = (Component) evt.getSource();
                for (int i = 0; i < tblProveedor.getColumnCount(); i++) {
                    tblProveedor.getColumnModel().getColumn(i).setPreferredWidth(ScreenUses.getPinTotal(c.getWidth(), widths[i]));
                }
            }
        });
    }
    
    void actualizarTablaProveedor(){
       ClsProveedor proveedores=new ClsProveedor();
       ArrayList<ClsEntidadProveedor> proveedor=proveedores.listarProveedor();
       Iterator iterator=proveedor.iterator();
//       DefaultTableModel defaultTableModel=new DefaultTableModel(null,titles);
        DefaultTableModel defaultTableModel = new DefaultTableModel(null, titles) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
       String fila[]=new String[12];
       while(iterator.hasNext()){
           ClsEntidadProveedor Proveedor=new ClsEntidadProveedor();
           Proveedor=(ClsEntidadProveedor) iterator.next();
           fila[0]=Proveedor.getStrIdProveedor();
           fila[1]=Proveedor.getStrNombreProveedor();       
           fila[2]=Proveedor.getStrRucProveedor();
           fila[3]=Proveedor.getStrDniProveedor();
           fila[4]=Proveedor.getStrDireccionProveedor();
           fila[5]=Proveedor.getStrTelefonoProveedor();
           fila[6]=Proveedor.getStrCelularProveedor();
           fila[7]=Proveedor.getStrEmailProveedor();
           fila[8]=Proveedor.getStrCuenta1Proveedor();
           fila[9]=Proveedor.getStrCuenta2Proveedor();
           fila[10]=Proveedor.getStrEstadoProveedor();
           fila[11]=Proveedor.getStrObsvProveedor();
           defaultTableModel.addRow(fila);               
       }
       tblProveedor.setModel(defaultTableModel);
    }
    void CrearTablaProveedor(){
        //Agregar Render
        for (int i=0;i<tblProveedor.getColumnCount();i++){
            Hashtable<Integer, Integer> hash = new Hashtable<>();
//            hash.put(0, 2, 35, 6, 89, 10, i)
            hash.put(0, SwingConstants.CENTER);
            hash.put(1, SwingConstants.CENTER);
            hash.put(2, SwingConstants.CENTER);
            hash.put(3, SwingConstants.CENTER);
            hash.put(4, SwingConstants.CENTER);
            hash.put(5, SwingConstants.CENTER);
            hash.put(6, SwingConstants.CENTER);
            TableCellRenderer render = TableConfigurator.configureTableItem(hash);
            tblProveedor.getColumnModel().getColumn(i).setCellRenderer(render);
        }
        //Activar ScrollBar
        tblProveedor.setAutoResizeMode(tblProveedor.AUTO_RESIZE_OFF);

    }
    void BuscarProveedorPanel(){
        dtm.setColumnIdentifiers(titles);
        
        ClsProveedor categoria=new ClsProveedor();
        busqueda=txtBusqueda.getText();
        if(rbtnCodigo.isSelected()){
            criterio="id";
        }else if(rbtnNombre.isSelected()){
            criterio="nombre";
        }else if(rbtnRuc.isSelected()){
            criterio="ruc";
        }else if(rbtnDni.isSelected()){
            criterio="dni";
        }
        try{
            rs=categoria.listarProveedorPorParametro(criterio,busqueda);
            boolean encuentra=false;
            String Datos[]=new String[12];
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
                Datos[11]=(String) rs.getString(12);

                dtm.addRow(Datos);
                encuentra=true;

            }
            if(encuentra=false){
                JOptionPane.showMessageDialog(null, "¡No se encuentra!");
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
        tblProveedor.setModel(dtm);
    }
    void CantidadTotal(){
        Total= String.valueOf(tblProveedor.getRowCount());   
        labelFooter.setText("Se cargaron " + Total + " registros");      
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        panelBackgoud = new javax.swing.JPanel();
        panelBody = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblProveedor = new javax.swing.JTable();
        panelHead = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        panel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        rbtnCodigo = new javax.swing.JRadioButton();
        rbtnNombre = new javax.swing.JRadioButton();
        rbtnRuc = new javax.swing.JRadioButton();
        rbtnDni = new javax.swing.JRadioButton();
        txtBusqueda = new javax.swing.JTextField();
        labelFooter = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Consultar Proveedor");
        setPreferredSize(new java.awt.Dimension(400, 300));
        getContentPane().setLayout(new java.awt.GridLayout());

        panelBackgoud.setLayout(new java.awt.BorderLayout());

        panelBody.setOpaque(false);
        panelBody.setLayout(new java.awt.GridLayout(1, 0));

        tblProveedor.setModel(new javax.swing.table.DefaultTableModel(
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
        tblProveedor.setRowHeight(22);
        tblProveedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProveedorMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblProveedor);

        panelBody.add(jScrollPane5);

        panelBackgoud.add(panelBody, java.awt.BorderLayout.CENTER);

        panelHead.setOpaque(false);
        panelHead.setLayout(new java.awt.BorderLayout(5, 5));

        btnSalir.setForeground(new java.awt.Color(255, 255, 255));
        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/door_in.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.setBorder(null);
        btnSalir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSalir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        panelHead.add(btnSalir, java.awt.BorderLayout.LINE_END);

        panel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Creiterio de búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11), new java.awt.Color(255, 255, 255))); // NOI18N
        panel.setMinimumSize(new java.awt.Dimension(450, 100));
        panel.setOpaque(false);
        panel.setPreferredSize(new java.awt.Dimension(660, 80));
        panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rbtnCodigo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbtnCodigo.setForeground(new java.awt.Color(255, 255, 255));
        rbtnCodigo.setText("ID Cliente");
        rbtnCodigo.setOpaque(false);
        rbtnCodigo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbtnCodigoStateChanged(evt);
            }
        });
        jPanel1.add(rbtnCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 100, -1));

        rbtnNombre.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbtnNombre.setForeground(new java.awt.Color(255, 255, 255));
        rbtnNombre.setText("Nombre o Razón Social");
        rbtnNombre.setOpaque(false);
        rbtnNombre.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbtnNombreStateChanged(evt);
            }
        });
        jPanel1.add(rbtnNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 0, 190, -1));

        rbtnRuc.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbtnRuc.setForeground(new java.awt.Color(255, 255, 255));
        rbtnRuc.setText("RUC");
        rbtnRuc.setOpaque(false);
        jPanel1.add(rbtnRuc, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 0, 70, -1));

        rbtnDni.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbtnDni.setForeground(new java.awt.Color(255, 255, 255));
        rbtnDni.setText("DNI");
        rbtnDni.setOpaque(false);
        jPanel1.add(rbtnDni, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 0, 90, -1));

        txtBusqueda.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        txtBusqueda.setMinimumSize(new java.awt.Dimension(5, 30));
        txtBusqueda.setPreferredSize(new java.awt.Dimension(500, 30));
        txtBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBusquedaKeyReleased(evt);
            }
        });
        jPanel1.add(txtBusqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 440, -1));

        panel.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 650, 50));

        panelHead.add(panel, java.awt.BorderLayout.CENTER);

        panelBackgoud.add(panelHead, java.awt.BorderLayout.NORTH);

        labelFooter.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelFooter.setForeground(new java.awt.Color(255, 255, 255));
        labelFooter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelFooter.setText("data");
        panelBackgoud.add(labelFooter, java.awt.BorderLayout.SOUTH);

        getContentPane().add(panelBackgoud);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblProveedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProveedorMouseClicked
        if(evt.getClickCount() == 2){
            int fila;
            DefaultTableModel defaultTableModel = new DefaultTableModel();
            fila = tblProveedor.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(null, "Se debe seleccionar un registro");
            } else {
                defaultTableModel = (DefaultTableModel) tblProveedor.getModel();

                Presentacion.FrmCompra.lblIdProveedor.setText((String) defaultTableModel.getValueAt(fila, 0));
                Presentacion.FrmCompra.txtNombreProveedor.setText((String) defaultTableModel.getValueAt(fila, 1));

            }
        }
    }//GEN-LAST:event_tblProveedorMouseClicked

    private void txtBusquedaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaKeyReleased
        BuscarProveedorPanel();
        CrearTablaProveedor();
        CantidadTotal();
    }//GEN-LAST:event_txtBusquedaKeyReleased

    private void rbtnNombreStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rbtnNombreStateChanged
        txtBusqueda.setText("");
    }//GEN-LAST:event_rbtnNombreStateChanged

    private void rbtnCodigoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rbtnCodigoStateChanged
        txtBusqueda.setText("");
    }//GEN-LAST:event_rbtnCodigoStateChanged

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel labelFooter;
    private javax.swing.JPanel panel;
    private javax.swing.JPanel panelBackgoud;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHead;
    private javax.swing.JRadioButton rbtnCodigo;
    private javax.swing.JRadioButton rbtnDni;
    private javax.swing.JRadioButton rbtnNombre;
    private javax.swing.JRadioButton rbtnRuc;
    private javax.swing.JTable tblProveedor;
    private javax.swing.JTextField txtBusqueda;
    // End of variables declaration//GEN-END:variables
}
