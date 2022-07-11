/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Consultas;

import Entidad.ClsEntidadProducto;
import Negocio.ClsProducto;
import interfaces.FrameState;
import java.awt.Component;
import java.sql.ResultSet;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import interfaces.ProductoVentaInterface;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import statics.Design;
import statics.ImageLoader;
import tools.ObjectDeserializer;
import tools.ObjectSerializer;
import statics.Paths;
import statics.ScreenUses;
import statics.TableConfigurator;
import tools.TextPrompt;
import tools.toast.Toast;

/**
 *
 * @author DAYPER-PERU
 */
public class FrmBuscarProducto extends javax.swing.JInternalFrame implements FrameState {

    static ResultSet rs=null;
    DefaultTableModel dtm=new DefaultTableModel();
    public String Total;
    String criterio,busqueda;
    private ProductoVentaInterface productoVentaInterface;
    // int[] anchos = {50, 200, 80, 80, 150, 80, 200, 0};
    private String titulos[] = {"ID", "Cód. de Barras", "Nombre", "Descripción", "Stock", "P. Costo", "P. Venta", "Imagen"};
//    private float[] columnSize = {4.95F, 15.81F, 19.52F, 17.52F, 10.86F, 10.52F, 20.81F, 0};
    private float[] columnSize = {5F, 10F, 25F, 30F, 10F, 10F, 10F, 0};
    private ArrayList<ClsEntidadProducto> products;
    
    public FrmBuscarProducto(ProductoVentaInterface productoVentaInterface) {
        
        this.productoVentaInterface = productoVentaInterface;
        
        initComponents();
        buttonGroup1.add(rbtnCodigo);
        buttonGroup1.add(rbtnNombre);
        buttonGroup1.add(rbtnDescripcion);
        actualizarTablaProducto();
        CrearTablaProducto();
        this.setSize(836, 400);
        CantidadTotal();
        setVisible(true);
        EventQueue.invokeLater(() -> txtBusqueda.requestFocusInWindow());
        
        readFrameRectanble();
        tableConfigurator();
        design();
    }
    
    private void design(){
        getContentPane().setBackground(Design.COLOR_PRIMARY_DARK);
        jPanel4.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        btnSalir.setBackground(Design.COLOR_ACCENT);
        btnSalir.setBorder(Design.BORDER_BUTTON);
        // Place holder
        new TextPrompt("Buscar...", txtBusqueda);
    }
    
    private void tableConfigurator() {
        jPanel3.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                Component c = (Component) evt.getSource();
                for (int i = 0; i < tblProducto.getColumnCount(); i++) {
                    tblProducto.getColumnModel().getColumn(i).setPreferredWidth(ScreenUses.getPinTotal(c.getWidth(), columnSize[i]));
                }
            }
        });
    }

    void actualizarTablaProducto(){
       
       ClsProducto productos = new ClsProducto();
       this.products = productos.listarProductoActivo();
       Iterator iterator = this.products.iterator();
       DefaultTableModel defaultTableModel=new DefaultTableModel(null,titulos){
           @Override
           public boolean isCellEditable(int row, int column) {
               return false;
           }
       };
       
       String fila[]=new String[8];
       while(iterator.hasNext()){
           ClsEntidadProducto Producto=new ClsEntidadProducto();
           Producto=(ClsEntidadProducto) iterator.next();
           fila[0]=Producto.getStrIdProducto();
           fila[1]=Producto.getStrCodigoProducto();       
           fila[2]=Producto.getStrNombreProducto();
           fila[3]=Producto.getStrDescripcionProducto();
           fila[4]=Producto.getStrStockProducto();
           fila[5]=Producto.getStrPrecioCostoProducto();
           fila[6]=Producto.getStrPrecioVentaProducto();
           fila[7]=Producto.getStrImagen();
           defaultTableModel.addRow(fila);               
       }
       tblProducto.setModel(defaultTableModel);
   }
    void CrearTablaProducto(){
        //Agregar Render
        for (int i=0;i<tblProducto.getColumnCount();i++){
            Hashtable<Integer, Integer> hash = new Hashtable<>();
            hash.put(0, SwingConstants.CENTER);
            hash.put(1, SwingConstants.CENTER);
            hash.put(4, SwingConstants.CENTER);
            hash.put(5, SwingConstants.CENTER);
            hash.put(6, SwingConstants.CENTER);
            TableCellRenderer render = TableConfigurator.configureTableItem(hash);
            tblProducto.getColumnModel().getColumn(i).setCellRenderer(render);
        }
      
        //Activar ScrollBar
        tblProducto.setAutoResizeMode(tblProducto.AUTO_RESIZE_OFF);
        
//        int[] anchos = {50, 200, 80, 80, 150, 80, 200, 0};
//        for (int i = 0; i < tblProducto.getColumnCount(); i++) {
//            tblProducto.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
//        }
   }
    void BuscarProductoPanel(){
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
            boolean encuentra = false;
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
                Datos[5]=(String) rs.getString(7);
                Datos[6]=(String) rs.getString(8);
                dtm.addRow(Datos);
                encuentra=true;

            }
            if(encuentra=false){
                Toast.makeText("No se encontraron coincidencias", Toast.LENGTH_MICRO).show();
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
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        rbtnCodigo = new javax.swing.JRadioButton();
        rbtnNombre = new javax.swing.JRadioButton();
        rbtnDescripcion = new javax.swing.JRadioButton();
        txtBusqueda = new javax.swing.JTextField();
        jLabelImage = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblProducto = new javax.swing.JTable();
        lblEstado = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Consultar Productos");
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jPanel4.setOpaque(false);
        jPanel4.setLayout(new java.awt.BorderLayout(5, 5));

        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.BorderLayout(5, 5));

        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(150, 100));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Criterio de búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel5.setOpaque(false);
        jPanel5.setLayout(null);

        rbtnCodigo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbtnCodigo.setForeground(new java.awt.Color(255, 255, 255));
        rbtnCodigo.setText("Cód. Producto");
        rbtnCodigo.setOpaque(false);
        rbtnCodigo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbtnCodigoStateChanged(evt);
            }
        });
        jPanel5.add(rbtnCodigo);
        rbtnCodigo.setBounds(20, 20, 150, 25);

        rbtnNombre.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbtnNombre.setForeground(new java.awt.Color(255, 255, 255));
        rbtnNombre.setSelected(true);
        rbtnNombre.setText("Nombre");
        rbtnNombre.setOpaque(false);
        rbtnNombre.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbtnNombreStateChanged(evt);
            }
        });
        jPanel5.add(rbtnNombre);
        rbtnNombre.setBounds(170, 20, 120, 25);

        rbtnDescripcion.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbtnDescripcion.setForeground(new java.awt.Color(255, 255, 255));
        rbtnDescripcion.setText("Descripción");
        rbtnDescripcion.setOpaque(false);
        jPanel5.add(rbtnDescripcion);
        rbtnDescripcion.setBounds(290, 20, 170, 25);

        txtBusqueda.setMinimumSize(new java.awt.Dimension(5, 30));
        txtBusqueda.setPreferredSize(new java.awt.Dimension(5, 30));
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
        jPanel5.add(txtBusqueda);
        txtBusqueda.setBounds(20, 50, 450, 30);

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 490, 90));

        jLabelImage.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jLabelImage.setMaximumSize(new java.awt.Dimension(90, 90));
        jLabelImage.setMinimumSize(new java.awt.Dimension(90, 90));
        jLabelImage.setOpaque(true);
        jLabelImage.setPreferredSize(new java.awt.Dimension(90, 90));
        jPanel1.add(jLabelImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 0, 120, 100));

        jPanel2.add(jPanel1, java.awt.BorderLayout.CENTER);

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
        jPanel2.add(btnSalir, java.awt.BorderLayout.LINE_END);

        jPanel4.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

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
        tblProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblProductoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblProductoKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tblProducto);

        jPanel3.add(jScrollPane2);

        jPanel4.add(jPanel3, java.awt.BorderLayout.CENTER);

        lblEstado.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblEstado.setForeground(new java.awt.Color(255, 255, 255));
        lblEstado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstado.setText("Count");
        jPanel4.add(lblEstado, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(jPanel4);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rbtnCodigoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rbtnCodigoStateChanged
        txtBusqueda.setText("");
    }//GEN-LAST:event_rbtnCodigoStateChanged

    private void txtBusquedaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaKeyReleased
        BuscarProductoPanel();
        CrearTablaProducto();
        CantidadTotal();
        tableConfigurator();
        
        for (int i = 0; i < tblProducto.getColumnCount(); i++) {
            tblProducto.getColumnModel().getColumn(i).setPreferredWidth(ScreenUses.getPinTotal(jPanel3.getWidth(), columnSize[i]));
        }
    }//GEN-LAST:event_txtBusquedaKeyReleased

    private void rbtnNombreStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rbtnNombreStateChanged
        txtBusqueda.setText("");
    }//GEN-LAST:event_rbtnNombreStateChanged

    private void tblProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductoMouseClicked
        showImageItem();
        if(evt.getClickCount() == 2){
            DefaultTableModel defaultTableModel = new DefaultTableModel();
            int fila = tblProducto.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(null, "Se debe seleccionar un registro");
            } else {
                defaultTableModel = (DefaultTableModel) tblProducto.getModel();
                ClsEntidadProducto producto = new ClsEntidadProducto();
                producto.setStrIdProducto((String) defaultTableModel.getValueAt(fila, 0));
                producto.setStrCodigoProducto((String) defaultTableModel.getValueAt(fila, 1));
                producto.setStrNombreProducto((String) defaultTableModel.getValueAt(fila, 2));
                producto.setStrDescripcionProducto((String) defaultTableModel.getValueAt(fila, 3));
                producto.setStrStockProducto((String) defaultTableModel.getValueAt(fila, 4));
                producto.setStrPrecioCostoProducto((String) defaultTableModel.getValueAt(fila, 5));
                producto.setStrPrecioVentaProducto((String) defaultTableModel.getValueAt(fila, 6));
                producto.setStrImagen((String) defaultTableModel.getValueAt(fila, 7));
                productoVentaInterface.loadProduct(producto);
            }
        }
            
    }//GEN-LAST:event_tblProductoMouseClicked

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void txtBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusquedaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBusquedaActionPerformed

    private void tblProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblProductoKeyPressed

    }//GEN-LAST:event_tblProductoKeyPressed
    private void showImageItem(){
        DefaultTableModel defaultTableModel = new DefaultTableModel();
        defaultTableModel = (DefaultTableModel) tblProducto.getModel();
        int fila = tblProducto.getSelectedRow();
        if (fila > -1) {
            String image = (String) defaultTableModel.getValueAt(fila, 7);
            if (image != null) {
                if (!image.isEmpty()) {
                    ImageLoader.setImage(
                            jLabelImage,
                            Paths.IMAGES_PATH + "/" + image
                    );
                }
            }
        }
    }
    private void tblProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblProductoKeyReleased
        showImageItem();
    }//GEN-LAST:event_tblProductoKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabelImage;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JRadioButton rbtnCodigo;
    private javax.swing.JRadioButton rbtnDescripcion;
    private javax.swing.JRadioButton rbtnNombre;
    private javax.swing.JTable tblProducto;
    private javax.swing.JTextField txtBusqueda;
    // End of variables declaration//GEN-END:variables

    @Override
    public void dispose(){
        writeFrameRectangle();
        super.dispose();
    }
    
    @Override
    public void readFrameRectanble() {
        ObjectDeserializer<Rectangle> deserializer = new ObjectDeserializer<Rectangle>(Paths.SERIAL_DIRECTORY_DATA, Paths.PRODUCT_RECTANGLE_NAME);
        Rectangle rectangle = deserializer.deserialicer();
        if (rectangle != null) {
            setBounds(rectangle);
        }
    }

    @Override
    public void writeFrameRectangle() {
        ObjectSerializer<Rectangle> serializer = new ObjectSerializer<Rectangle>(Paths.SERIAL_DIRECTORY_DATA, Paths.PRODUCT_RECTANGLE_NAME);
        serializer.serilizer(getBounds());
    }
    
}
