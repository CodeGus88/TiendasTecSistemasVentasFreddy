/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Consultas;

import Entidad.ClsEntidadCliente;
import Negocio.ClsCliente;
import interfaces.ClientInterface;
import interfaces.FrameState;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Rectangle;
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
import tools.ObjectDeserializer;
import tools.ObjectSerializer;
import statics.Paths;
import statics.ScreenUses;
import statics.TableConfigurator;
import tools.TextPrompt;

/**
 *
 * @author DAYPER-PERU
 */
public class FrmBuscarCliente extends javax.swing.JInternalFrame implements FrameState{

    static ResultSet rs=null;
    DefaultTableModel dtm=new DefaultTableModel();

    String criterio,busqueda,Total;
    
    private ClientInterface clientInterface;
    
    private String titulos[] = {"ID", "Nombre o Razón Social", "NIT", "CI", "Dirección", "Teléfono", "Observación"};
    private float[] widths = {4.95F, 23.81F, 9.52F, 9.52F, 21.86F, 9.52F, 20.81F};
    
    public FrmBuscarCliente(ClientInterface clientInterface) {
        this.clientInterface = clientInterface;
        initComponents();
        buttonGroup1.add(rbtnCodigo);
        buttonGroup1.add(rbtnNombre);
        buttonGroup1.add(rbtnRuc);
        buttonGroup1.add(rbtnDni);
        
        actualizarTablaCliente();
        CrearTablaCliente();
        readFrameRectanble();
        CantidadTotal();
        this.setVisible(true);
        EventQueue.invokeLater(() -> txtBusqueda.requestFocusInWindow());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        design();
        tableConfigurator();
    }
    
    private void design(){
        this.getContentPane().setBackground(Design.COLOR_PRIMARY_DARK);
        jPanel6.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        btnSalir.setBackground(Design.COLOR_ACCENT);
        btnSalir.setBorder(Design.BORDER_BUTTON);
        // place holder
        new TextPrompt("Buscar...", txtBusqueda);
    }
    
    private void tableConfigurator() {
        jPanel5.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                Component c = (Component) evt.getSource();
                for (int i = 0; i < tblCliente.getColumnCount(); i++) {
                    tblCliente.getColumnModel().getColumn(i).setPreferredWidth(ScreenUses.getPinTotal(c.getWidth(), widths[i]));
                }
            }
        });
    }
    
    void actualizarTablaCliente(){
       ClsCliente clientes=new ClsCliente();
       ArrayList<ClsEntidadCliente> cliente=clientes.listarCliente();
       Iterator iterator=cliente.iterator();
        DefaultTableModel defaultTableModel = new DefaultTableModel(null, titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
       
       String fila[]=new String[7];
       while(iterator.hasNext()){
           ClsEntidadCliente Cliente=new ClsEntidadCliente();
           Cliente=(ClsEntidadCliente) iterator.next();
           fila[0]=Cliente.getStrIdCliente();
           fila[1]=Cliente.getStrNombreCliente();       
           fila[2]=Cliente.getStrRucCliente();
           fila[3]=Cliente.getStrDniCliente();
           fila[4]=Cliente.getStrDireccionCliente();
           fila[5]=Cliente.getStrTelefonoCliente();
           fila[6]=Cliente.getStrObsvCliente();
           defaultTableModel.addRow(fila);               
       }
       tblCliente.setModel(defaultTableModel);
    }
    void CrearTablaCliente(){
        //Agregar Render
        for (int i=0;i<tblCliente.getColumnCount();i++){
            Hashtable<Integer, Integer> map = new Hashtable<Integer, Integer>();
            map.put(0, SwingConstants.CENTER);
            TableCellRenderer render = TableConfigurator.configureTableItem(map);
            tblCliente.getColumnModel().getColumn(i).setCellRenderer(render);
        }
      
        //Activar ScrollBar
        tblCliente.setAutoResizeMode(tblCliente.AUTO_RESIZE_OFF);

    }
    void BuscarClientePanel(){
        dtm = new DefaultTableModel(null, titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        ClsCliente cliente=new ClsCliente();
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
            rs=cliente.listarClientePorParametro(criterio,busqueda);
            boolean encuentra=false;
            String Datos[]=new String[7];
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

                dtm.addRow(Datos);
                encuentra=true;

            }
            if(encuentra=false){
                JOptionPane.showMessageDialog(null, "¡No se encuentra!");
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
        tblCliente.setModel(dtm);
    }
    void CantidadTotal(){
        Total= String.valueOf(tblCliente.getRowCount());   
        lblEstado.setText("Se cargaron " + Total + " registros");      
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        rbtnCodigo = new javax.swing.JRadioButton();
        rbtnNombre = new javax.swing.JRadioButton();
        rbtnRuc = new javax.swing.JRadioButton();
        rbtnDni = new javax.swing.JRadioButton();
        txtBusqueda = new javax.swing.JTextField();
        btnSalir = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblCliente = new javax.swing.JTable();
        lblEstado = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Consultar Clientes");
        setMinimumSize(new java.awt.Dimension(836, 400));
        setPreferredSize(new java.awt.Dimension(836, 400));
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jPanel6.setOpaque(false);
        jPanel6.setLayout(new java.awt.BorderLayout(5, 5));

        jPanel4.setOpaque(false);
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11), new java.awt.Color(255, 255, 255)), "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11), new java.awt.Color(255, 255, 255)), "Criterio de búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel3.setForeground(new java.awt.Color(255, 255, 255));
        jPanel3.setOpaque(false);
        jPanel3.setLayout(new java.awt.GridLayout(2, 1, 5, 5));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.GridLayout(1, 4, 5, 0));

        rbtnCodigo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbtnCodigo.setForeground(new java.awt.Color(255, 255, 255));
        rbtnCodigo.setText("ID Cliente");
        rbtnCodigo.setOpaque(false);
        rbtnCodigo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbtnCodigoStateChanged(evt);
            }
        });
        jPanel1.add(rbtnCodigo);

        rbtnNombre.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbtnNombre.setForeground(new java.awt.Color(255, 255, 255));
        rbtnNombre.setSelected(true);
        rbtnNombre.setText("Nombre o Razón Social");
        rbtnNombre.setOpaque(false);
        rbtnNombre.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbtnNombreStateChanged(evt);
            }
        });
        jPanel1.add(rbtnNombre);

        rbtnRuc.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbtnRuc.setForeground(new java.awt.Color(255, 255, 255));
        rbtnRuc.setText("NIT");
        rbtnRuc.setOpaque(false);
        jPanel1.add(rbtnRuc);

        rbtnDni.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbtnDni.setForeground(new java.awt.Color(255, 255, 255));
        rbtnDni.setText("CI");
        rbtnDni.setOpaque(false);
        rbtnDni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnDniActionPerformed(evt);
            }
        });
        jPanel1.add(rbtnDni);

        jPanel2.add(jPanel1);

        jPanel3.add(jPanel2);

        txtBusqueda.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtBusqueda.setMinimumSize(new java.awt.Dimension(5, 30));
        txtBusqueda.setPreferredSize(new java.awt.Dimension(0, 30));
        txtBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBusquedaActionPerformed(evt);
            }
        });
        txtBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBusquedaKeyReleased(evt);
            }
        });
        jPanel3.add(txtBusqueda);

        jPanel4.add(jPanel3, java.awt.BorderLayout.LINE_START);

        btnSalir.setForeground(new java.awt.Color(255, 255, 255));
        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/door_in.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSalir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel4.add(btnSalir, java.awt.BorderLayout.LINE_END);

        jPanel6.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        jPanel5.setLayout(new java.awt.GridLayout(1, 0));

        tblCliente.setModel(new javax.swing.table.DefaultTableModel(
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
        tblCliente.setRowHeight(22);
        tblCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClienteMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblCliente);

        jPanel5.add(jScrollPane5);

        jPanel6.add(jPanel5, java.awt.BorderLayout.CENTER);

        lblEstado.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblEstado.setForeground(new java.awt.Color(255, 255, 255));
        lblEstado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstado.setText("Cantidad");
        jPanel6.add(lblEstado, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(jPanel6);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClienteMouseClicked
        if(evt.getClickCount() == 2){
            int fila;
            DefaultTableModel defaultTableModel = new DefaultTableModel();
            fila = tblCliente.getSelectedRow();

            if (fila == -1) {
                JOptionPane.showMessageDialog(null, "Se debe seleccionar un registro");
            } else {
                defaultTableModel = (DefaultTableModel) tblCliente.getModel();
                ClsEntidadCliente cliente = new ClsEntidadCliente();
                cliente.setStrIdCliente((String) defaultTableModel.getValueAt(fila, 0));
                cliente.setStrNombreCliente((String) defaultTableModel.getValueAt(fila, 1));
                clientInterface.loadClient(cliente);
            }
        }
    }//GEN-LAST:event_tblClienteMouseClicked

    private void txtBusquedaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaKeyReleased
        BuscarClientePanel();
        CrearTablaCliente();
        CantidadTotal();
        for (int i = 0; i < tblCliente.getColumnCount(); i++) {
            tblCliente.getColumnModel().getColumn(i).setPreferredWidth(ScreenUses.getPinTotal(jPanel5.getWidth(), widths[i]));
        }
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

    private void rbtnDniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnDniActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtnDniActionPerformed

    private void txtBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusquedaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBusquedaActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JRadioButton rbtnCodigo;
    private javax.swing.JRadioButton rbtnDni;
    private javax.swing.JRadioButton rbtnNombre;
    private javax.swing.JRadioButton rbtnRuc;
    private javax.swing.JTable tblCliente;
    private javax.swing.JTextField txtBusqueda;
    // End of variables declaration//GEN-END:variables

        @Override
    public void dispose() {
        writeFrameRectangle();
        super.dispose();
    }
    
    @Override
    public void readFrameRectanble() {
        ObjectDeserializer<Rectangle> deserializer = new ObjectDeserializer<Rectangle>(Paths.SERIAL_DIRECTORY_DATA, Paths.CLIENT_RECTANGLE_NAME);
        Rectangle rectangle = deserializer.deserialicer();
        if(rectangle != null)
            setBounds(rectangle);
       
    }

    @Override
    public void writeFrameRectangle() {
        ObjectSerializer<Rectangle> serializer = new ObjectSerializer<Rectangle>(Paths.SERIAL_DIRECTORY_DATA, Paths.CLIENT_RECTANGLE_NAME);
        serializer.serilizer(getBounds());
    }
}
