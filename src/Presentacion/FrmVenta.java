/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Presentacion;

import Conexion.ClsConexion;
import Entidad.*;
import Negocio.*;
import enums.EFormState;
import interfaces.ClientInterface;
import interfaces.FrameState;
import interfaces.ProductoVentaInterface;
import java.awt.BorderLayout;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.*;
import java.util.HashMap;
import java.util.Map;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import statics.Design;
import statics.Message;
import tools.ObjectDeserializer;
import tools.ObjectSerializer;
import statics.Paths;
import statics.ScreenUses;
import statics.TableConfigurator;
import tools.toast.Toast;

public class FrmVenta extends javax.swing.JInternalFrame implements ClientInterface, ProductoVentaInterface, FrameState {

    private Connection connection = new ClsConexion().getConection();
    String accion;
    String numVenta, tipoDocumento;
    String id[] = new String[50];
    private Consultas.FrmBuscarCliente client;
    private Consultas.FrmBuscarProducto product;
    
    private EFormState eFormState;
    
    static int intContador;
    public String IdEmpleado, NombreEmpleado;
    int idventa;
    String idventa_print;
    public String codigo;

    static ResultSet rs = null;
    private DefaultTableModel dtmDetalle;
    
    private final ClsEntidadCliente garzon1;
    public static final byte GARZON_1 = 1;
    private final ClsEntidadCliente garzon2;
    public static final byte GARZON_2 = 2;
    private final String titulos[] = {"ID", "CÓDIGO", "PRODUCTO", "DESCRIPCIÓN", "CANTIDAD", "COSTO", "PRECIO", "TOTAL"};

    public FrmVenta() {
        initComponents();
        //---------------------FECHA ACTUAL-------------------------------
        Date date = new Date();
        txtFecha.setDate(date);
        //---------------------GENERA NUMERO DE VENTA---------------------
        numVenta = generaNumVenta();
        txtNumero.setText(numVenta);
        //---------------------ANCHO Y ALTO DEL FORM----------------------
        cargarComboTipoDocumento();

        lblIdProducto.setVisible(false);
//        lblIdCliente.setVisible(false);
        txtDescripcionProducto.setVisible(false);
        txtCostoProducto.setVisible(false);
        mirar();
        //--------------------JTABLE - DETALLEPRODUCTO--------------------
        dtmDetalle = new DefaultTableModel(null, titulos){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // inicializar garzones
        garzon1 = new ClsEntidadCliente();
        garzon2 = new ClsEntidadCliente();
        
        tblDetalleProducto.setModel(dtmDetalle);
        CrearTablaDetalleProducto();
        panelsConfigurator();
        eFormState = EFormState.DISABLE;
        
        autoLoadWindows();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        readFrameRectanble();
        design();
        invisibleComponents();
    }
    
    // por el momento no interesa que se muestren estos
    private void invisibleComponents(){
        jLabel7.setVisible(false);
        jLabel9.setVisible(false);
        txtSubTotal.setVisible(false);
        txtIGV.setVisible(false);
    }
    
    private void design(){
        getContentPane().setBackground(Design.COLOR_PRIMARY_DARK);
        btnGuardar.setBackground(Design.COLOR_ACCENT);
        btnGuardar.setBackground(Design.COLOR_ACCENT);
        btnAgregarProducto.setBackground(Design.COLOR_ACCENT);
        btnBuscarProducto.setBackground(Design.COLOR_ACCENT);
        btnCancelar.setBackground(Design.COLOR_ACCENT);
        btnEliminarProducto.setBackground(Design.COLOR_ACCENT);
        btnImporte.setBackground(Design.COLOR_ACCENT);
        btnImprimir.setBackground(Design.COLOR_ACCENT);
        btnLimpiarTabla.setBackground(Design.COLOR_ACCENT);
        btnNuevo.setBackground(Design.COLOR_ACCENT);
        btnSalir.setBackground(Design.COLOR_ACCENT);
        btnBuscarCliente.setBackground(Design.COLOR_ACCENT);
        
        btnGuardar.setBorder(Design.BORDER_BUTTON);
        btnAgregarProducto.setBorder(Design.BORDER_BUTTON);
        btnBuscarProducto.setBorder(Design.BORDER_BUTTON);
        btnCancelar.setBorder(Design.BORDER_BUTTON);
        btnEliminarProducto.setBorder(Design.BORDER_BUTTON);
        btnImporte.setBorder(Design.BORDER_BUTTON);
        btnImprimir.setBorder(Design.BORDER_BUTTON);
        btnLimpiarTabla.setBorder(Design.BORDER_BUTTON);
        btnNuevo.setBorder(Design.BORDER_BUTTON);
        btnSalir.setBorder(Design.BORDER_BUTTON);
        btnBuscarCliente.setBorder(Design.BORDER_BUTTON);
        
        jPanelMenu.setBorder(BorderFactory.createEmptyBorder(10,0,10,10));
        footer.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panelMenuAndTable.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
//        tblDetalleProducto.setDragEnabled(true);
    }
    
    /**
     * Carga las ventanas para agregar cliente y producto
     */
    private void autoLoadWindows(){
        try {
            btnNuevo.doClick();
            btnBuscarCliente.doClick();
            btnBuscarProducto.doClick();
            client.setSelected(true);
            product.setSelected(true);
        } catch (Exception e) {
            Message.LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    private void panelsConfigurator(){
        panelMenuAndTable.setLayout(new BorderLayout());
        panelMenuAndTable.add(panelMenu, BorderLayout.NORTH);
        panelMenuAndTable.add(panelTable, BorderLayout.CENTER);
        panelTable.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                Component c = (Component) evt.getSource(); //........ }
                float[] columnSize = {0F, 8.333F, 28.667F, 39.668F, 7.778F, 0, 7.778F, 7.778F};
                for (int i = 0; i < tblDetalleProducto.getColumnCount(); i++) {
                    tblDetalleProducto.getColumnModel().getColumn(i).setPreferredWidth(ScreenUses.getPinTotal(c.getWidth(), columnSize[i]));
                }
            }
        });
    }
    
    public String generaNumVenta() {

        ClsVenta oVenta = new ClsVenta();
        try {

            rs = oVenta.obtenerUltimoIdVenta();
            while (rs.next()) {
                if (rs.getString(1) != null) {
                    Scanner s = new Scanner(rs.getString(1));
                    int c = s.useDelimiter("C").nextInt() + 1;

                    if (c < 10) {
                        return "C0000" + c;
                    }
                    if (c < 100) {
                        return "C000" + c;
                    }
                    if (c < 1000) {
                        return "C00" + c;
                    }
                    if (c < 10000) {
                        return "C0" + c;
                    } else {
                        return "C" + c;
                    }
                }
            }

        } catch (Exception ex) {
            Message.LOGGER.log(Level.SEVERE, ex.getMessage());
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                Message.LOGGER.log(Level.SEVERE, ex.getMessage());
            }
        }
        return "C00001";
    }

    void CrearTablaDetalleProducto() {
        //Agregar Render
        for (int i = 0; i < tblDetalleProducto.getColumnCount(); i++) {
            TableCellRenderer render = TableConfigurator.configureTableItem(TableConfigurator.TABLE_COLUMN_ALIGEMENT_1);
            tblDetalleProducto.getColumnModel().getColumn(i).setCellRenderer(render);
        }

        //Activar ScrollBar
        tblDetalleProducto.setAutoResizeMode(tblDetalleProducto.AUTO_RESIZE_OFF);
        //Ocultar columa
        setOcultarColumnasJTable(tblDetalleProducto, new int[]{0, 5});
    }
    
    private void clearClients(){
        garzon1.setStrIdCliente(null);
        garzon1.setStrNombreCliente(null);
        garzon2.setStrIdCliente(null);
        garzon2.setStrNombreCliente(null);
    }

    void limpiarCampos() {

        txtTotalVenta.setText("0.0");
        txtDescuento.setText("0.0");
        txtSubTotal.setText("0.0");
        txtIGV.setText("0.0");
        txtTotalPagar.setText("0.0");

        lblIdProducto.setText("");
        txtCodigoProducto.setText("");
        txtNombreProducto.setText("");
        txtStockProducto.setText("");
        txtPrecioProducto.setText("");
        txtCantidadProducto.setText("");
        txtTotalProducto.setText("");
        txtCodigoProducto.requestFocus();
    }

    void mirar() {
        btnNuevo.setEnabled(true);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnSalir.setEnabled(true);

        cboTipoDocumento.setEnabled(false);
        txtCodigoProducto.setEnabled(false);
        txtSerie.setEnabled(false);
        txtCantidadProducto.setEnabled(false);
        txtFecha.setEnabled(false);
        txtNumero.setEnabled(false);

        btnBuscarCliente.setEnabled(false);
        btnBuscarProducto.setEnabled(false);
        btnAgregarProducto.setEnabled(false);
        btnEliminarProducto.setEnabled(false);
        btnLimpiarTabla.setEnabled(false);
        chkCambiarNumero.setEnabled(false);
        chkCambiarNumero.setSelected(false);

        txtNombreGarzon1.setText(null);
        txtNombreGarzon2.setText(null);
        txtTotalVenta.setText("0.0");
        txtDescuento.setText("0.0");
        txtSubTotal.setText("0.0");
        txtIGV.setText("0.0");
        txtTotalPagar.setText("0.0");
        lblIdProducto.setText("");
        txtCodigoProducto.setText("");
        txtNombreProducto.setText("");
        txtStockProducto.setText("");
        txtPrecioProducto.setText("");
        txtCantidadProducto.setText("");
        txtTotalProducto.setText("");
        txtCodigoProducto.requestFocus();

    }

    void modificar() {

        btnNuevo.setEnabled(false);

        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnSalir.setEnabled(false);

        cboTipoDocumento.setEnabled(true);
        txtCodigoProducto.setEnabled(true);
        txtSerie.setEnabled(true);
        txtCantidadProducto.setEnabled(true);
        txtFecha.setEnabled(true);

        btnBuscarCliente.setEnabled(true);
        btnBuscarProducto.setEnabled(true);
        btnAgregarProducto.setEnabled(true);
        btnEliminarProducto.setEnabled(true);
        btnLimpiarTabla.setEnabled(true);
        chkCambiarNumero.setEnabled(true);

        txtCodigoProducto.requestFocus();
    }

    void cargarComboTipoDocumento() {
        ClsTipoDocumento tipodocumento = new ClsTipoDocumento();
        ArrayList<ClsEntidadTipoDocumento> tipodocumentos = tipodocumento.listarTipoDocumento();
        Iterator iterator = tipodocumentos.iterator();
        DefaultComboBoxModel DefaultComboBoxModel = new DefaultComboBoxModel();
        DefaultComboBoxModel.removeAllElements();

        cboTipoDocumento.removeAll();
        String fila[] = new String[2];
        intContador = 0;

        while (iterator.hasNext()) {
            ClsEntidadTipoDocumento TipoDocumento = new ClsEntidadTipoDocumento();
            TipoDocumento = (ClsEntidadTipoDocumento) iterator.next();
            id[intContador] = TipoDocumento.getStrIdTipoDocumento();
            fila[0] = TipoDocumento.getStrIdTipoDocumento();
            fila[1] = TipoDocumento.getStrDescripcionTipoDocumento();
            DefaultComboBoxModel.addElement(TipoDocumento.getStrDescripcionTipoDocumento());
            intContador++;
        }
        cboTipoDocumento.setModel(DefaultComboBoxModel);
    }

    void BuscarProductoPorCodigo() {
        String busqueda = null;
        busqueda = txtCodigoProducto.getText();
        try {
            ClsProducto oProducto = new ClsProducto();

            rs = oProducto.listarProductoActivoPorParametro("codigo", busqueda);
            while (rs.next()) {
                if (rs.getString(2).equals(busqueda)) {

                    lblIdProducto.setText(rs.getString(1));
                    txtNombreProducto.setText(rs.getString(3));
                    txtDescripcionProducto.setText(rs.getString(4));
                    txtStockProducto.setText(rs.getString(5));
                    txtCostoProducto.setText(rs.getString(7));
                    txtPrecioProducto.setText(rs.getString(8));
                }
                break;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            System.out.println(ex.getMessage());
        }

    }

//    void BuscarClientePorDefecto() {
//        try {
//            ClsCliente oCliente = new ClsCliente();
//            rs = oCliente.listarClientePorParametro("id", "1");
//            while (rs.next()) {
//                garzon1.setText(rs.getString(1));
//                txtNombreGarzon1.setText(rs.getString(2));
//                break;
//            }
//
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this, ex.getMessage());
//            System.out.println(ex.getMessage());
//        }
//
//    }

    private void setOcultarColumnasJTable(JTable tbl, int columna[]) {
        for (int i = 0; i < columna.length; i++) {
            tbl.getColumnModel().getColumn(columna[i]).setMaxWidth(0);
            tbl.getColumnModel().getColumn(columna[i]).setMinWidth(0);
            tbl.getTableHeader().getColumnModel().getColumn(columna[i]).setMaxWidth(0);
            tbl.getTableHeader().getColumnModel().getColumn(columna[i]).setMinWidth(0);
        }
    }

    void obtenerUltimoIdVenta_print() {
        try {
            ClsVenta oVenta = new ClsVenta();
            rs = oVenta.obtenerUltimoIdVenta();
            while (rs.next()) {
                idventa_print = String.valueOf(rs.getInt(1));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            Message.LOGGER.log(Level.SEVERE, ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelMenuAndTable = new javax.swing.JPanel();
        panelMenu = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        txtCantidadProducto = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtTotalProducto = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        btnLimpiarTabla = new javax.swing.JButton();
        btnEliminarProducto = new javax.swing.JButton();
        btnAgregarProducto = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        txtCodigoProducto = new javax.swing.JTextField();
        btnBuscarProducto = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        txtNombreProducto = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtDescripcionProducto = new javax.swing.JLabel();
        txtStockProducto = new javax.swing.JTextField();
        txtPrecioProducto = new javax.swing.JTextField();
        lblIdProducto = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txtCostoProducto = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnBuscarCliente = new javax.swing.JButton();
        txtNombreGarzon1 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        cboTipoDocumento = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        txtFecha = new com.toedter.calendar.JDateChooser();
        txtNombreGarzon2 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtNumero = new javax.swing.JTextField();
        txtSerie = new javax.swing.JTextField();
        chkCambiarNumero = new javax.swing.JCheckBox();
        panelTable = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblDetalleProducto = new javax.swing.JTable();
        jPanelMenu = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnImporte = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        footer = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtImporte = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTotalVenta = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtSubTotal = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtTotalPagar = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtCambio = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtDescuento = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtIGV = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Ventas");
        setToolTipText("");
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(900, 601));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        panelMenuAndTable.setOpaque(false);
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout();
        flowLayout1.setAlignOnBaseline(true);
        panelMenuAndTable.setLayout(flowLayout1);

        panelMenu.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panelMenu.setForeground(new java.awt.Color(204, 204, 0));
        panelMenu.setMinimumSize(new java.awt.Dimension(800, 250));
        panelMenu.setOpaque(false);

        jPanel5.setBackground(new java.awt.Color(34, 81, 249));
        jPanel5.setMinimumSize(new java.awt.Dimension(530, 275));
        jPanel5.setOpaque(false);
        jPanel5.setPreferredSize(new java.awt.Dimension(530, 275));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(208, 218, 247));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Pre transacción", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel3.setForeground(new java.awt.Color(255, 255, 255));
        jPanel3.setName(""); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setLayout(null);

        jLabel21.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Cantidad: ");
        jPanel3.add(jLabel21);
        jLabel21.setBounds(10, 20, 70, 30);

        txtCantidadProducto.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtCantidadProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCantidadProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCantidadProductoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadProductoKeyTyped(evt);
            }
        });
        jPanel3.add(txtCantidadProducto);
        txtCantidadProducto.setBounds(80, 20, 60, 30);

        jLabel24.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("Total: ");
        jPanel3.add(jLabel24);
        jLabel24.setBounds(150, 20, 50, 30);

        txtTotalProducto.setBackground(new java.awt.Color(204, 255, 204));
        txtTotalProducto.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtTotalProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTotalProducto.setDisabledTextColor(new java.awt.Color(0, 102, 204));
        txtTotalProducto.setEnabled(false);
        jPanel3.add(txtTotalProducto);
        txtTotalProducto.setBounds(200, 20, 80, 30);

        jPanel6.setMinimumSize(new java.awt.Dimension(170, 20));
        jPanel6.setOpaque(false);
        jPanel6.setPreferredSize(new java.awt.Dimension(170, 20));
        jPanel6.setLayout(new java.awt.GridLayout(1, 3, 10, 10));

        btnLimpiarTabla.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiarTabla.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/nuevo1.png"))); // NOI18N
        btnLimpiarTabla.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        btnLimpiarTabla.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpiarTabla.setOpaque(false);
        btnLimpiarTabla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarTablaActionPerformed(evt);
            }
        });
        jPanel6.add(btnLimpiarTabla);

        btnEliminarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Remove.png"))); // NOI18N
        btnEliminarProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        btnEliminarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarProducto.setOpaque(false);
        btnEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProductoActionPerformed(evt);
            }
        });
        jPanel6.add(btnEliminarProducto);

        btnAgregarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Agregar_p1.png"))); // NOI18N
        btnAgregarProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        btnAgregarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregarProducto.setOpaque(false);
        btnAgregarProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAgregarProductoMouseClicked(evt);
            }
        });
        btnAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProductoActionPerformed(evt);
            }
        });
        btnAgregarProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                btnAgregarProductoKeyReleased(evt);
            }
        });
        jPanel6.add(btnAgregarProducto);

        jPanel3.add(jPanel6);
        jPanel6.setBounds(310, 13, 200, 50);

        jPanel5.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 530, 70));
        jPanel3.getAccessibleContext().setAccessibleName("Proceso");

        jPanel2.setBackground(new java.awt.Color(208, 218, 247));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos del producto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.setOpaque(false);
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel22.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("Código de barras: ");
        jPanel2.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 240, 30));

        txtCodigoProducto.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtCodigoProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodigoProductoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoProductoKeyTyped(evt);
            }
        });
        jPanel2.add(txtCodigoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, 130, 30));

        btnBuscarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Buscar_p.png"))); // NOI18N
        btnBuscarProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        btnBuscarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscarProducto.setOpaque(false);
        btnBuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProductoActionPerformed(evt);
            }
        });
        jPanel2.add(btnBuscarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 20, 30, 30));

        jLabel17.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Nombre: ");
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 170, 30));

        txtNombreProducto.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtNombreProducto.setEnabled(false);
        jPanel2.add(txtNombreProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 60, 230, 30));

        jLabel19.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Stock:");
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 10, -1, 20));
        jPanel2.add(txtDescripcionProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 30, 20));

        txtStockProducto.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtStockProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtStockProducto.setEnabled(false);
        jPanel2.add(txtStockProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 30, 80, 20));

        txtPrecioProducto.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtPrecioProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPrecioProducto.setEnabled(false);
        jPanel2.add(txtPrecioProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 70, 80, 20));
        jPanel2.add(lblIdProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 20, 20));

        jLabel23.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Precio:");
        jPanel2.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 50, 50, 20));
        jPanel2.add(txtCostoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 80, 90, 20));

        jPanel5.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 530, 100));

        jPanel1.setBackground(new java.awt.Color(208, 218, 247));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos de la venta", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setOpaque(false);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Garzón 2: ");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 90, 30));

        btnBuscarCliente.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Buscar_p.png"))); // NOI18N
        btnBuscarCliente.setAlignmentY(1.0F);
        btnBuscarCliente.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        btnBuscarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscarCliente.setOpaque(false);
        btnBuscarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarClienteActionPerformed(evt);
            }
        });
        jPanel1.add(btnBuscarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 20, 30, 60));

        txtNombreGarzon1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtNombreGarzon1.setEnabled(false);
        txtNombreGarzon1.setSelectionColor(new java.awt.Color(255, 0, 255));
        jPanel1.add(txtNombreGarzon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 170, 30));

        jLabel13.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Documento: ");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 20, 90, 30));

        cboTipoDocumento.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cboTipoDocumento.setForeground(Design.CONTENT_TEXT);
        cboTipoDocumento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboTipoDocumento.setAutoscrolls(true);
        jPanel1.add(cboTipoDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 20, 130, 30));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Fecha: ");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 50, 90, 30));

        txtFecha.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.add(txtFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 50, 130, 30));

        txtNombreGarzon2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtNombreGarzon2.setEnabled(false);
        txtNombreGarzon2.setSelectionColor(new java.awt.Color(255, 0, 255));
        jPanel1.add(txtNombreGarzon2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, 170, 30));

        jLabel10.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Garzón 1: ");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 90, 30));

        jPanel5.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 530, 100));

        panelMenu.add(jPanel5);

        jPanel10.setOpaque(false);
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/bar_code_22177 (2).png"))); // NOI18N
        jPanel10.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 230, 140));

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Serie");
        jPanel10.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 60, 20));

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Nº de venta");
        jPanel10.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 170, 110, 20));

        txtNumero.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtNumero.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel10.add(txtNumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 190, 110, -1));

        txtSerie.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtSerie.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSerie.setText("001");
        jPanel10.add(txtSerie, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 59, -1));

        chkCambiarNumero.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        chkCambiarNumero.setForeground(new java.awt.Color(255, 255, 255));
        chkCambiarNumero.setText("Cambiar Número");
        chkCambiarNumero.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        chkCambiarNumero.setOpaque(false);
        chkCambiarNumero.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                chkCambiarNumeroStateChanged(evt);
            }
        });
        jPanel10.add(chkCambiarNumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 210, -1));

        panelMenu.add(jPanel10);

        panelMenuAndTable.add(panelMenu);

        panelTable.setLayout(new java.awt.GridLayout(1, 0, 0, 20));

        jScrollPane3.setPreferredSize(getPreferredSize());

        tblDetalleProducto.setAutoCreateRowSorter(true);
        tblDetalleProducto.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tblDetalleProducto.setMaximumSize(new java.awt.Dimension(5000, 5000));
        tblDetalleProducto.setRowHeight(22);
        tblDetalleProducto.setRowMargin(2);
        tblDetalleProducto.setShowGrid(true);
        jScrollPane3.setViewportView(tblDetalleProducto);

        panelTable.add(jScrollPane3);

        panelMenuAndTable.add(panelTable);

        getContentPane().add(panelMenuAndTable, java.awt.BorderLayout.CENTER);

        jPanelMenu.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanelMenu.setOpaque(false);
        jPanelMenu.setLayout(new java.awt.GridLayout(5, 1, 5, 5));

        btnNuevo.setForeground(new java.awt.Color(255, 255, 255));
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/money_22144 (1).png"))); // NOI18N
        btnNuevo.setText("NUEVO");
        btnNuevo.setToolTipText("");
        btnNuevo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNuevo.setIconTextGap(0);
        btnNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        jPanelMenu.add(btnNuevo);

        btnImporte.setForeground(new java.awt.Color(255, 255, 255));
        btnImporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/calculator_22175.png"))); // NOI18N
        btnImporte.setText("IMPORTE");
        btnImporte.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImporte.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnImporte.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnImporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImporteActionPerformed(evt);
            }
        });
        jPanelMenu.add(btnImporte);

        btnImprimir.setForeground(new java.awt.Color(255, 255, 255));
        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/printer_print_22133.png"))); // NOI18N
        btnImprimir.setText("RECIBO");
        btnImprimir.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnImprimir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImprimir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnImprimir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });
        jPanelMenu.add(btnImprimir);

        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/cancelar.png"))); // NOI18N
        btnCancelar.setText("CANCELAR");
        btnCancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancelar.setIconTextGap(0);
        btnCancelar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        jPanelMenu.add(btnCancelar);

        btnSalir.setForeground(new java.awt.Color(255, 255, 255));
        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/principal.png"))); // NOI18N
        btnSalir.setText("SALIR");
        btnSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSalir.setIconTextGap(0);
        btnSalir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanelMenu.add(btnSalir);

        getContentPane().add(jPanelMenu, java.awt.BorderLayout.LINE_END);

        footer.setBackground(new java.awt.Color(255, 51, 0));
        footer.setForeground(new java.awt.Color(255, 255, 255));
        footer.setOpaque(false);
        footer.setLayout(new java.awt.GridLayout(2, 8, 5, 5));

        jLabel8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Importe:");
        jLabel8.setAutoscrolls(true);
        jLabel8.setMaximumSize(new java.awt.Dimension(10000, 30));
        jLabel8.setMinimumSize(new java.awt.Dimension(0, 30));
        jLabel8.setPreferredSize(new java.awt.Dimension(0, 30));
        footer.add(jLabel8);

        txtImporte.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtImporte.setForeground(java.awt.Color.darkGray);
        txtImporte.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtImporte.setEnabled(false);
        txtImporte.setMaximumSize(new java.awt.Dimension(10000, 30));
        txtImporte.setMinimumSize(new java.awt.Dimension(0, 30));
        txtImporte.setPreferredSize(new java.awt.Dimension(0, 30));
        footer.add(txtImporte);

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Valor venta:");
        jLabel3.setAutoscrolls(true);
        jLabel3.setMaximumSize(new java.awt.Dimension(10000, 30));
        jLabel3.setMinimumSize(new java.awt.Dimension(0, 30));
        jLabel3.setPreferredSize(new java.awt.Dimension(0, 30));
        footer.add(jLabel3);

        txtTotalVenta.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtTotalVenta.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalVenta.setEnabled(false);
        txtTotalVenta.setMaximumSize(new java.awt.Dimension(10000, 30));
        txtTotalVenta.setMinimumSize(new java.awt.Dimension(0, 30));
        txtTotalVenta.setPreferredSize(new java.awt.Dimension(0, 30));
        footer.add(txtTotalVenta);

        jLabel7.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Sub total:");
        jLabel7.setAutoscrolls(true);
        jLabel7.setMaximumSize(new java.awt.Dimension(10000, 30));
        jLabel7.setMinimumSize(new java.awt.Dimension(0, 30));
        jLabel7.setPreferredSize(new java.awt.Dimension(0, 30));
        footer.add(jLabel7);

        txtSubTotal.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtSubTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSubTotal.setEnabled(false);
        txtSubTotal.setMaximumSize(new java.awt.Dimension(10000, 30));
        txtSubTotal.setMinimumSize(new java.awt.Dimension(0, 30));
        txtSubTotal.setPreferredSize(new java.awt.Dimension(0, 30));
        footer.add(txtSubTotal);

        jLabel12.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Total a pagar:");
        jLabel12.setAutoscrolls(true);
        jLabel12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel12.setMaximumSize(new java.awt.Dimension(10000, 30));
        jLabel12.setMinimumSize(new java.awt.Dimension(0, 30));
        jLabel12.setPreferredSize(new java.awt.Dimension(0, 30));
        footer.add(jLabel12);

        txtTotalPagar.setBackground(new java.awt.Color(51, 51, 0));
        txtTotalPagar.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtTotalPagar.setForeground(new java.awt.Color(255, 255, 102));
        txtTotalPagar.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalPagar.setDisabledTextColor(new java.awt.Color(0, 255, 102));
        txtTotalPagar.setEnabled(false);
        txtTotalPagar.setMaximumSize(new java.awt.Dimension(10000, 30));
        txtTotalPagar.setMinimumSize(new java.awt.Dimension(0, 30));
        txtTotalPagar.setPreferredSize(new java.awt.Dimension(0, 30));
        txtTotalPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalPagarActionPerformed(evt);
            }
        });
        footer.add(txtTotalPagar);

        jLabel11.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Cambio:");
        jLabel11.setAutoscrolls(true);
        jLabel11.setMaximumSize(new java.awt.Dimension(10000, 30));
        jLabel11.setMinimumSize(new java.awt.Dimension(0, 30));
        jLabel11.setPreferredSize(new java.awt.Dimension(0, 30));
        footer.add(jLabel11);

        txtCambio.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtCambio.setForeground(java.awt.Color.darkGray);
        txtCambio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCambio.setEnabled(false);
        txtCambio.setMaximumSize(new java.awt.Dimension(10000, 30));
        txtCambio.setMinimumSize(new java.awt.Dimension(0, 30));
        txtCambio.setPreferredSize(new java.awt.Dimension(0, 30));
        footer.add(txtCambio);

        jLabel15.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Descuento:");
        jLabel15.setAutoscrolls(true);
        jLabel15.setMaximumSize(new java.awt.Dimension(10000, 30));
        jLabel15.setMinimumSize(new java.awt.Dimension(0, 30));
        jLabel15.setPreferredSize(new java.awt.Dimension(0, 30));
        footer.add(jLabel15);

        txtDescuento.setBackground(new java.awt.Color(255, 255, 204));
        txtDescuento.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtDescuento.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDescuento.setMaximumSize(new java.awt.Dimension(10000, 30));
        txtDescuento.setMinimumSize(new java.awt.Dimension(0, 30));
        txtDescuento.setPreferredSize(new java.awt.Dimension(0, 30));
        txtDescuento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDescuentoKeyReleased(evt);
            }
        });
        footer.add(txtDescuento);

        jLabel9.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("I.G.V.:");
        jLabel9.setAutoscrolls(true);
        jLabel9.setMaximumSize(new java.awt.Dimension(10000, 30));
        jLabel9.setMinimumSize(new java.awt.Dimension(0, 30));
        jLabel9.setPreferredSize(new java.awt.Dimension(0, 30));
        footer.add(jLabel9);

        txtIGV.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtIGV.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtIGV.setEnabled(false);
        txtIGV.setMaximumSize(new java.awt.Dimension(10000, 30));
        txtIGV.setMinimumSize(new java.awt.Dimension(0, 30));
        txtIGV.setPreferredSize(new java.awt.Dimension(0, 30));
        footer.add(txtIGV);

        jPanel12.setMaximumSize(new java.awt.Dimension(10000, 30));
        jPanel12.setMinimumSize(new java.awt.Dimension(0, 30));
        jPanel12.setOpaque(false);
        jPanel12.setPreferredSize(new java.awt.Dimension(0, 30));
        footer.add(jPanel12);

        btnGuardar.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("PAGAR");
        btnGuardar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGuardar.setIconTextGap(0);
        btnGuardar.setMaximumSize(new java.awt.Dimension(10000, 30));
        btnGuardar.setMinimumSize(new java.awt.Dimension(0, 30));
        btnGuardar.setPreferredSize(new java.awt.Dimension(0, 30));
        btnGuardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        footer.add(btnGuardar);

        getContentPane().add(footer, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
//        BuscarClientePorDefecto();
        cargarComboTipoDocumento();
    }//GEN-LAST:event_formComponentShown

    private void btnBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProductoActionPerformed
        if (product != null){ 
            product.dispose(); 
            System.gc();
        }
        product = new Consultas.FrmBuscarProducto(this);
        Presentacion.FrmPrincipal.Escritorio.add(product);
        product.toFront();
    }//GEN-LAST:event_btnBuscarProductoActionPerformed

    private void btnBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarClienteActionPerformed
        if(client != null){
            client.dispose();
            System.gc();
        } 
        client = new Consultas.FrmBuscarCliente(this);
        Presentacion.FrmPrincipal.Escritorio.add(client);
        client.toFront();
    }//GEN-LAST:event_btnBuscarClienteActionPerformed

    void CalcularTotal() {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        DecimalFormat formateador = new DecimalFormat("####.##", simbolos);
        double precio_prod = 0, cant_prod = 0, total_prod = 0;
        precio_prod = Double.parseDouble(txtPrecioProducto.getText());
        cant_prod = Double.parseDouble(txtCantidadProducto.getText());
        total_prod = precio_prod * cant_prod;
        txtTotalProducto.setText(String.valueOf(formateador.format(total_prod)));
    }

    public int recorrer(int id) {
        int fila = 0, valor = -1;

        fila = tblDetalleProducto.getRowCount();

        for (int f = 0; f < fila; f++) {
            if (Integer.parseInt(String.valueOf(dtmDetalle.getValueAt(f, 0))) == id) {

                valor = f;
                break;

            } else {
                valor = -1;
            }
        }
        return valor;
    }

    void agregardatos(int item, String cod, String nom, String descrip, double cant, double cost, double pre, double tot) {

        int p = recorrer(item);
        double n_cant, n_total;
        if (p > -1) {

            n_cant = Double.parseDouble(String.valueOf(tblDetalleProducto.getModel().getValueAt(p, 4))) + cant;
            tblDetalleProducto.setValueAt(n_cant, p, 4);

            n_total = Double.parseDouble(String.valueOf(tblDetalleProducto.getModel().getValueAt(p, 4))) * Double.parseDouble(String.valueOf(tblDetalleProducto.getModel().getValueAt(p, 5)));
            tblDetalleProducto.setValueAt(n_total, p, 7);

        } else {
            String Datos[] = {String.valueOf(item), cod, nom, descrip, String.valueOf(cant), String.valueOf(cost), String.valueOf(pre), String.valueOf(tot)};
            dtmDetalle.addRow(Datos);
        }
        tblDetalleProducto.setModel(dtmDetalle);
    }

    void CalcularValor_Venta() {
        int fila = 0;
        double valorVenta = 0;
        fila = dtmDetalle.getRowCount();
        for (int f = 0; f < fila; f++) {
            valorVenta += Double.parseDouble(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 7)));
        }
        txtTotalVenta.setText(String.valueOf(valorVenta));
    }

    void CalcularSubTotal() {
        double subtotal = 0;
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        DecimalFormat formateador = new DecimalFormat("####.##", simbolos);
        subtotal = Double.parseDouble(txtTotalPagar.getText()) / 1.18;
        txtSubTotal.setText(String.valueOf(formateador.format(subtotal)));
    }

    void CalcularIGV() {
        double igv = 0;
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        DecimalFormat formateador = new DecimalFormat("####.##", simbolos);
        igv = Double.parseDouble(txtSubTotal.getText()) * 0.18;
        txtIGV.setText(String.valueOf(formateador.format(igv)));
    }

    void CalcularTotal_Pagar() {
        double totalpagar = 0;
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        DecimalFormat formateador = new DecimalFormat("####.##", simbolos);
        totalpagar = Double.parseDouble(txtTotalVenta.getText()) - Double.parseDouble(txtDescuento.getText());
        txtTotalPagar.setText(String.valueOf(formateador.format(totalpagar)));
    }

    void limpiarTabla() {
        try {
            int filas = tblDetalleProducto.getRowCount();
            for (int i = 0; filas > i; i++) {
                dtmDetalle.removeRow(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al limpiar la tabla.");
        }
    }

    private void btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProductoActionPerformed
        double stock, cant;

        if (!txtCantidadProducto.getText().equals("")) {
            if (txtCantidadProducto.getText().equals("")) {
                txtCantidadProducto.setText("0");
                cant = Double.parseDouble(txtCantidadProducto.getText());
            } else {
                cant = Double.parseDouble(txtCantidadProducto.getText());
            }

            if (cant > 0) {
                stock = Double.parseDouble(txtStockProducto.getText());
                cant = Double.parseDouble(txtCantidadProducto.getText());
                if (cant <= stock) {
                    int d1 = Integer.parseInt(lblIdProducto.getText());
                    String d2 = txtCodigoProducto.getText();
                    String d3 = txtNombreProducto.getText();
                    String d4 = txtDescripcionProducto.getText();
                    double d5 = Double.parseDouble(txtCantidadProducto.getText());
                    double d6 = Double.parseDouble(txtCostoProducto.getText());
                    double d7 = Double.parseDouble(txtPrecioProducto.getText());
                    double d8 = Double.parseDouble(txtTotalProducto.getText());
                    agregardatos(d1, d2, d3, d4, d5, d6, d7, d8);

                    CalcularValor_Venta();
                    CalcularTotal_Pagar();
                    CalcularSubTotal();
                    CalcularIGV();

                    txtCantidadProducto.setText("");
                    txtTotalProducto.setText("");

                    txtCodigoProducto.setText("");
                    txtNombreProducto.setText("");
                    txtStockProducto.setText("");
                    txtPrecioProducto.setText("");
                    txtCodigoProducto.requestFocus();
                } else {
                    JOptionPane.showMessageDialog(null, "Stock Insuficiente");
                    txtCantidadProducto.requestFocus();
                }

            } else {
                JOptionPane.showMessageDialog(null, "Ingrese Cantidad mayor a 0");
                txtCantidadProducto.requestFocus();
            }

        } else {
            JOptionPane.showMessageDialog(null, "Ingrese cantidad");
            txtCantidadProducto.requestFocus();
        }

    }//GEN-LAST:event_btnAgregarProductoActionPerformed

    private void txtCantidadProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadProductoKeyReleased
        CalcularTotal();
        int keyCode = evt.getKeyCode();
        if (keyCode == KeyEvent.VK_ENTER) {
            btnAgregarProducto.requestFocus();
        }
    }//GEN-LAST:event_txtCantidadProductoKeyReleased

    private void btnEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProductoActionPerformed
        int fila = tblDetalleProducto.getSelectedRow();
        if (fila > 0) {
            dtmDetalle.removeRow(fila);
            CalcularValor_Venta();
            CalcularSubTotal();
            CalcularIGV();
        } else if (fila == 0) {
            dtmDetalle.removeRow(fila);

            txtTotalVenta.setText("0.0");
            txtDescuento.setText("0.0");
            txtSubTotal.setText("0.0");
            txtIGV.setText("0.0");
            txtTotalPagar.setText("0.0");
            CalcularValor_Venta();
            CalcularTotal_Pagar();
            CalcularSubTotal();
            CalcularIGV();
        }
        CalcularValor_Venta();
        CalcularTotal_Pagar();
        CalcularSubTotal();
        CalcularIGV();
        txtCodigoProducto.requestFocus();
    }//GEN-LAST:event_btnEliminarProductoActionPerformed

    private void txtCodigoProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoProductoKeyReleased
        BuscarProductoPorCodigo();
        int keyCode = evt.getKeyCode();
        if (keyCode == KeyEvent.VK_ENTER) {
            txtCantidadProducto.requestFocus();
        }
    }//GEN-LAST:event_txtCodigoProductoKeyReleased

    private void txtDescuentoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescuentoKeyReleased
        CalcularTotal_Pagar();
        CalcularSubTotal();
        CalcularIGV();
    }//GEN-LAST:event_txtDescuentoKeyReleased

    private void btnLimpiarTablaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarTablaActionPerformed
        limpiarTabla();
        txtTotalVenta.setText("0.0");
        txtDescuento.setText("0.0");
        txtSubTotal.setText("0.0");
        txtIGV.setText("0.0");
        txtTotalPagar.setText("0.0");
        txtCodigoProducto.requestFocus();
    }//GEN-LAST:event_btnLimpiarTablaActionPerformed

    private void chkCambiarNumeroStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_chkCambiarNumeroStateChanged
        if (chkCambiarNumero.isSelected()) {
            txtNumero.setText("");
            txtNumero.setEnabled(true);
        } else {
            txtNumero.setEnabled(false);
            numVenta = generaNumVenta();
            txtNumero.setText(numVenta);
        }
    }//GEN-LAST:event_chkCambiarNumeroStateChanged

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        mirar();
        limpiarTabla();
        eFormState = EFormState.DISABLE;
        clearClients();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        Presentacion.FrmVentaRecibo VentaRecibo = new Presentacion.FrmVentaRecibo();
        Presentacion.FrmPrincipal.Escritorio.add(VentaRecibo);
        Presentacion.FrmVentaRecibo.txtDocumentoVenta.setText(tipoDocumento);
        VentaRecibo.toFront();
        VentaRecibo.setVisible(true);
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void btnImporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImporteActionPerformed
        // TODO add your handling code here:
        String ingreso = JOptionPane.showInputDialog(null, "Ingrese Importe a Cancelar", "0.0");
        Double importe, cambio;
        if (ingreso.compareTo("") != 0) {
            importe = Double.parseDouble(ingreso);
            txtImporte.setText(String.valueOf(importe));
            cambio = Double.parseDouble(txtImporte.getText()) - Double.parseDouble(txtTotalPagar.getText());
            txtCambio.setText(String.valueOf(cambio));
        } else {
            txtImporte.setText("0.0");
        }
    }//GEN-LAST:event_btnImporteActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        int result = JOptionPane.showConfirmDialog(this, "¿Desea Generar la Venta?", "Mensaje del Sistema", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            if (accion.equals("Nuevo")) {
                ClsVenta ventas = new ClsVenta();
                ClsEntidadVenta venta = new ClsEntidadVenta();
                venta.setStrIdTipoDocumento(id[cboTipoDocumento.getSelectedIndex()]);
                venta.setStrIdGarzon1(garzon1.getStrIdCliente());
                venta.setStrIdGarzon2(garzon2.getStrIdCliente());
                venta.setStrIdEmpleado(IdEmpleado);
                venta.setStrSerieVenta(txtSerie.getText());
                venta.setStrNumeroVenta(txtNumero.getText());
                venta.setStrFechaVenta(txtFecha.getDate());
                venta.setStrTotalVenta(txtTotalVenta.getText());
                venta.setStrDescuentoVenta(txtDescuento.getText());
                venta.setStrSubTotalVenta(txtSubTotal.getText());
                venta.setStrIgvVenta(txtIGV.getText());
                venta.setStrTotalPagarVenta(txtTotalPagar.getText());
                venta.setStrEstadoVenta("EMITIDO");
                if(ventas.agregarVenta(venta)){
                    guardarDetalle();
                    Toast.makeText(Toast.SUCCESS, Message.SUCCESS_MESSAGE, Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(Toast.UNSUCCESS, Message.SUCCESS_MESSAGE, Toast.LENGTH_SHORT).show();
            }

            mirar();
            tipoDocumento = cboTipoDocumento.getSelectedItem().toString();
            limpiarTabla();
            clearClients();
            numVenta = generaNumVenta();
            txtNumero.setText(numVenta);
//            BuscarClientePorDefecto();
//------------ Imprimir Venta --------------            
            if (cboTipoDocumento.getSelectedItem().equals("TICKET")) {
                //Dese imprimir el Comprobante?
                int imprime = JOptionPane.showConfirmDialog(this, "¿Desea Imprimir el Ticket?", "Mensaje del Sistema", JOptionPane.YES_NO_OPTION);
                if (imprime == JOptionPane.YES_OPTION) {

                    obtenerUltimoIdVenta_print();
                    Map p = new HashMap();
                    p.put("busqueda", idventa_print);

                    JasperReport report;
                    JasperPrint print;
                    try {

                        report = JasperCompileManager.compileReport(new File("").getAbsolutePath() + "/src/Reportes/RptVentaTicket.jrxml");
                        print = JasperFillManager.fillReport(report, p, connection);
                        JasperViewer view = new JasperViewer(print, false);
                        JasperPrintManager.printReport(print, false);

                    } catch (JRException e) {
                        e.printStackTrace();
                    }
                }
            }
            //fin imprimir            

        }

        if (result == JOptionPane.NO_OPTION) {
            Toast.makeText("¡Venta anulada!", Toast.LENGTH_SHORT).show();
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        accion = "Nuevo";
        eFormState = EFormState.ENABLE;
        modificar();
        limpiarCampos();
//        BuscarClientePorDefecto();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void txtCantidadProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadProductoKeyTyped
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9'))
            evt.consume();
    }//GEN-LAST:event_txtCantidadProductoKeyTyped

    private void txtCodigoProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoProductoKeyTyped
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9'))
            evt.consume();
    }//GEN-LAST:event_txtCodigoProductoKeyTyped

    private void btnAgregarProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAgregarProductoKeyReleased

    }//GEN-LAST:event_btnAgregarProductoKeyReleased

    private void btnAgregarProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAgregarProductoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAgregarProductoMouseClicked

    private void txtTotalPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalPagarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalPagarActionPerformed
    void obtenerUltimoIdVenta() {
        try {
            ClsVenta oVenta = new ClsVenta();
            rs = oVenta.obtenerUltimoIdVenta();
            while (rs.next()) {
                idventa = rs.getInt(1);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            System.out.println(ex.getMessage());
        }
    }

    void guardarDetalle() {
        obtenerUltimoIdVenta();
        ClsDetalleVenta detalleventas = new ClsDetalleVenta();
        ClsEntidadDetalleVenta detalleventa = new ClsEntidadDetalleVenta();
        ClsProducto productos = new ClsProducto();
        String strId;
        ClsEntidadProducto producto = new ClsEntidadProducto();
        int fila = 0;

        double cant = 0, ncant = 0, stock = 0;
        fila = tblDetalleProducto.getRowCount();
        for (int f = 0; f < fila; f++) {
            detalleventa.setStrIdVenta(String.valueOf(idventa));
            detalleventa.setStrIdProducto(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 0)));
            detalleventa.setStrCantidadDet(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 4)));
            detalleventa.setStrCostoDet(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 5)));
            detalleventa.setStrPrecioDet(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 6)));
            detalleventa.setStrTotalDet(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 7)));
            detalleventas.agregarDetalleVenta(detalleventa);

            try {
                ClsProducto oProducto = new ClsProducto();

                rs = oProducto.listarProductoActivoPorParametro("id", ((String) tblDetalleProducto.getValueAt(f, 0)));
                while (rs.next()) {
                    cant = Double.parseDouble(rs.getString(5));
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
                System.out.println(ex.getMessage());
            }

            strId = ((String) tblDetalleProducto.getValueAt(f, 0));

            ncant = Double.parseDouble(String.valueOf(tblDetalleProducto.getModel().getValueAt(f, 4)));

            stock = cant - ncant;
            producto.setStrStockProducto(String.valueOf(stock));
            productos.actualizarProductoStock(strId, producto);

        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JButton btnBuscarCliente;
    private javax.swing.JButton btnBuscarProducto;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminarProducto;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnImporte;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnLimpiarTabla;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox cboTipoDocumento;
    private javax.swing.JCheckBox chkCambiarNumero;
    private javax.swing.JPanel footer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanelMenu;
    private javax.swing.JScrollPane jScrollPane3;
    public static javax.swing.JLabel lblIdProducto;
    private javax.swing.JPanel panelMenu;
    private javax.swing.JPanel panelMenuAndTable;
    private javax.swing.JPanel panelTable;
    private javax.swing.JTable tblDetalleProducto;
    private javax.swing.JTextField txtCambio;
    private javax.swing.JTextField txtCantidadProducto;
    public static javax.swing.JTextField txtCodigoProducto;
    public static javax.swing.JLabel txtCostoProducto;
    public static javax.swing.JLabel txtDescripcionProducto;
    private javax.swing.JTextField txtDescuento;
    private com.toedter.calendar.JDateChooser txtFecha;
    private javax.swing.JTextField txtIGV;
    private javax.swing.JTextField txtImporte;
    public static javax.swing.JTextField txtNombreGarzon1;
    public static javax.swing.JTextField txtNombreGarzon2;
    public static javax.swing.JTextField txtNombreProducto;
    private javax.swing.JTextField txtNumero;
    public static javax.swing.JTextField txtPrecioProducto;
    private javax.swing.JTextField txtSerie;
    public static javax.swing.JTextField txtStockProducto;
    private javax.swing.JTextField txtSubTotal;
    private javax.swing.JTextField txtTotalPagar;
    private javax.swing.JTextField txtTotalProducto;
    private javax.swing.JTextField txtTotalVenta;
    // End of variables declaration//GEN-END:variables

    @Override
    public void loadClient(ClsEntidadCliente client, final byte GARZON) {
        if(GARZON == GARZON_1){
            garzon1.setStrIdCliente(client.getStrIdCliente());
            garzon1.setStrNombreCliente(client.getStrNombreCliente());
            txtNombreGarzon1.setText(client.getStrNombreCliente());
            Toast.makeText(Toast.SUCCESS, "Se agregó el elemento en el primer campo", Toast.LENGTH_MICRO).show();
        }else if(GARZON == GARZON_2){
            garzon2.setStrIdCliente(client.getStrIdCliente());
            garzon2.setStrNombreCliente(client.getStrNombreCliente());
            txtNombreGarzon2.setText(client.getStrNombreCliente());
            Toast.makeText(Toast.SUCCESS, "Se agregó el elemento, en el segundo campo", Toast.LENGTH_MICRO).show();
        }else{
            Toast.makeText(Toast.SUCCESS, "¡Imcompatible!", Toast.LENGTH_MICRO).show();
        }
    }
    
    @Override
    public boolean loadClient(ClsEntidadCliente client) {
        if(eFormState.equals(EFormState.ENABLE)){
            if(garzon1.getStrIdCliente() == null || garzon1.getStrIdCliente().isEmpty()){
                garzon1.setStrIdCliente(client.getStrIdCliente());
                garzon1.setStrNombreCliente(client.getStrNombreCliente());
                txtNombreGarzon1.setText(client.getStrNombreCliente());
                Toast.makeText(Toast.SUCCESS, "Se agregó el elemento", Toast.LENGTH_MICRO).show();
                return true;
            }else if(garzon2.getStrIdCliente() == null || garzon2.getStrIdCliente().isEmpty()){
                garzon2.setStrIdCliente(client.getStrIdCliente());
                garzon2.setStrNombreCliente(client.getStrNombreCliente());
                txtNombreGarzon2.setText(client.getStrNombreCliente());
                Toast.makeText(Toast.SUCCESS, "Se agregó el elemento", Toast.LENGTH_MICRO).show();
                return true;
            }else{
                return false;
            }
            
        }else{
            Toast.makeText(Toast.UNSUCCESS, "Formulario inactivo", Toast.LENGTH_MICRO).show();
            return true;
        }
    }

    @Override
    public void loadProduct(ClsEntidadProducto product) {
        if(eFormState.equals(EFormState.ENABLE)){
            lblIdProducto.setText(product.getStrIdProducto());
            txtCodigoProducto.setText(product.getStrCodigoProducto());
            txtNombreProducto.setText(product.getStrNombreProducto());
            txtDescripcionProducto.setText(product.getStrDescripcionProducto());
            txtStockProducto.setText(product.getStrStockProducto());
            txtCostoProducto.setText(product.getStrPrecioCostoProducto());  // producto.preciocosto 
            txtPrecioProducto.setText(product.getStrPrecioVentaProducto()); // producto.precioVenta
            Toast.makeText(Toast.SUCCESS, "Se agregó el producto", Toast.LENGTH_MICRO).show();
            txtCantidadProducto.setText("1");
            CalcularTotal();
        }else{
            Toast.makeText(Toast.UNSUCCESS, "Fornulario inactivo, no se agregó el producto", Toast.LENGTH_MICRO).show();
        }
    }

    @Override
    public void dispose() {
        try {
            if (client != null) {
                client.dispose();
            }
            if (product != null) {
                product.dispose();
            }
            writeFrameRectangle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.dispose();
    }

    @Override
    public void readFrameRectanble() {
        ObjectDeserializer<Rectangle> deserializer = new ObjectDeserializer<>(Paths.SERIAL_DIRECTORY_DATA, Paths.SALE_RECTANGLE_NAME);
        Rectangle rectangle = deserializer.deserialicer();
        if(rectangle != null)
            setBounds(rectangle);
    }

    @Override
    public void writeFrameRectangle() {
        ObjectSerializer<Rectangle> serializer = new ObjectSerializer<>(Paths.SERIAL_DIRECTORY_DATA, Paths.SALE_RECTANGLE_NAME);
        serializer.serilizer(getBounds());
    }
    
}
