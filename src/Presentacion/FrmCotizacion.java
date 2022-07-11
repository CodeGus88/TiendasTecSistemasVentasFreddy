/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Presentacion;

import Entidad.*;
import Negocio.*;
import enums.EFormState;
import interfaces.FrameState;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import interfaces.ProductoVentaInterface;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.logging.Level;
import statics.Design;
import statics.Message;
import statics.Paths;
import statics.ScreenUses;

import statics.TableConfigurator;
import tools.ObjectDeserializer;
import tools.ObjectSerializer;
import tools.toast.Toast;

public class FrmCotizacion extends javax.swing.JInternalFrame implements ProductoVentaInterface, FrameState{

//    private Connection connection = new ClsConexion().getConection();
//    String Total;
//    String strCodigo;
    String accion;
    String numVenta, tipoDocumento;
//    int registros;
    String id[] = new String[50];

    static int intContador;
    public String IdEmpleado, NombreEmpleado;
    int idventa; // , nidventa;
    String idventa_print;
    //-----------------------------------------------
    public String codigo;
//    static Connection conn = null;

    static ResultSet rs = null;
//    DefaultTableModel dtm = new DefaultTableModel();
    private DefaultTableModel dtmDetalle; 
    
    private EFormState state;
    
    private Consultas.FrmBuscarProducto product;
    
    private String[] titles = {"ID", "CÓDIGO", "PRODUCTO", "DESCRIPCIÓN", "CANT.", "COSTO", "PRECIO", "TOTAL"};
    private float[] widths = {0F, 8.333F, 28.667F, 39.668F, 7.778F, 0, 7.778F, 7.778F};
    
    public FrmCotizacion() {
        initComponents();
        //---------------------FECHA ACTUAL-------------------------------
        state = EFormState.DISABLE;
        Date date = new Date();
        String format = new String("dd/MM/yyyy");
        SimpleDateFormat formato = new SimpleDateFormat(format);
        //---------------------GENERA NUMERO DE VENTA---------------------
        numVenta = generaNumVenta();
        txtNumero.setText(numVenta);
        //---------------------ANCHO Y ALTO DEL FORM----------------------
        cargarComboTipoDocumento();

        lblIdProducto.setVisible(false);
        txtDescripcionProducto.setVisible(false);
        txtCostoProducto.setVisible(false);
        mirar();

        
        dtmDetalle = new DefaultTableModel(null, titles){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblDetalleProducto.setModel(dtmDetalle);
        
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        CrearTablaDetalleProducto();
        autoLoadWindows();
        design();
        readFrameRectanble();
        invisibleComponents();
    }
    
    // por el momento no interesa que se muestren estos
    private void invisibleComponents() {
        jLabel1.setVisible(false);
        jLabel2.setVisible(false);
        txtSubTotal.setVisible(false);
        txtIGV.setVisible(false);
    }
    
    private void autoLoadWindows(){
        btnNuevo.doClick();
        btnBuscarProducto.doClick();
    }
    
    private void design() {
        this.getContentPane().setBackground(Design.COLOR_PRIMARY_DARK);
        this.getContentPane().removeAll();
        this.getContentPane().add(center, BorderLayout.CENTER);
        this.getContentPane().add(footer, BorderLayout.SOUTH);
        this.getContentPane().add(menuButton, BorderLayout.EAST);
        footer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        menuButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        btnGuardar.setBackground(Design.COLOR_ACCENT);
        btnGuardar.setBackground(Design.COLOR_ACCENT);
        btnAgregarProducto.setBackground(Design.COLOR_ACCENT);
        btnBuscarProducto.setBackground(Design.COLOR_ACCENT);
        btnCancel.setBackground(Design.COLOR_ACCENT);
        btnEliminarProducto.setBackground(Design.COLOR_ACCENT);
        btnLimpiarTabla.setBackground(Design.COLOR_ACCENT);
        btnNuevo.setBackground(Design.COLOR_ACCENT);
        btnSalir.setBackground(Design.COLOR_ACCENT);

        btnGuardar.setBorder(Design.BORDER_BUTTON);
        btnAgregarProducto.setBorder(Design.BORDER_BUTTON);
        btnBuscarProducto.setBorder(Design.BORDER_BUTTON);
        btnCancel.setBorder(Design.BORDER_BUTTON);
        btnEliminarProducto.setBorder(Design.BORDER_BUTTON);
        btnLimpiarTabla.setBorder(Design.BORDER_BUTTON);
        btnNuevo.setBorder(Design.BORDER_BUTTON);
        btnSalir.setBorder(Design.BORDER_BUTTON);
        panelsConfigurator();
    }
    
    private void panelsConfigurator() {
        tablePanel.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                Component c = (Component) evt.getSource(); //........ }
                for (int i = 0; i < tblDetalleProducto.getColumnCount(); i++) {
                    tblDetalleProducto.getColumnModel().getColumn(i).setPreferredWidth(ScreenUses.getPinTotal(c.getWidth(), widths[i]));
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
            ex.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
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
        btnCancel.setEnabled(false);
        btnSalir.setEnabled(true);
        txtCodigoProducto.setEnabled(false);
        txtSerie.setEnabled(false);
        txtCantidadProducto.setEnabled(false);
        txtNumero.setEnabled(false);

        btnBuscarProducto.setEnabled(false);
        btnAgregarProducto.setEnabled(false);
        btnEliminarProducto.setEnabled(false);
        btnLimpiarTabla.setEnabled(false);
        chkCambiarNumero.setEnabled(false);
        chkCambiarNumero.setSelected(false);

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
        btnCancel.setEnabled(true);
        btnGuardar.setEnabled(true);

        txtCodigoProducto.setEnabled(true);
        txtSerie.setEnabled(true);
        txtCantidadProducto.setEnabled(true);

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

    private void setOcultarColumnasJTable(JTable tbl, int columna[]) {
        for (int i = 0; i < columna.length; i++) {
            tbl.getColumnModel().getColumn(columna[i]).setMaxWidth(0);
            tbl.getColumnModel().getColumn(columna[i]).setMinWidth(0);
            tbl.getTableHeader().getColumnModel().getColumn(columna[i]).setMaxWidth(0);
            tbl.getTableHeader().getColumnModel().getColumn(columna[i]).setMinWidth(0);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        center = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
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
        jPanel3 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        txtCantidadProducto = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtTotalProducto = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        btnAgregarProducto = new javax.swing.JButton();
        btnEliminarProducto = new javax.swing.JButton();
        btnLimpiarTabla = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtNumero = new javax.swing.JTextField();
        txtSerie = new javax.swing.JTextField();
        chkCambiarNumero = new javax.swing.JCheckBox();
        tablePanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblDetalleProducto = new javax.swing.JTable();
        footer = new javax.swing.JPanel();
        labelImporte = new javax.swing.JLabel();
        txtImporte = new javax.swing.JTextField();
        labelTotalVenta = new javax.swing.JLabel();
        txtTotalVenta = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtSubTotal = new javax.swing.JTextField();
        labelTotalAPagar = new javax.swing.JLabel();
        txtTotalPagar = new javax.swing.JTextField();
        labelCambio = new javax.swing.JLabel();
        txtCambio = new javax.swing.JTextField();
        labelDescuento = new javax.swing.JLabel();
        txtDescuento = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtIGV = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        menuButton = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();

        setBackground(new java.awt.Color(0, 0, 0));
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Cotización");
        setToolTipText("");
        setPreferredSize(new java.awt.Dimension(900, 601));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        getContentPane().setLayout(new java.awt.BorderLayout(5, 5));

        center.setOpaque(false);
        center.setLayout(new java.awt.BorderLayout());

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel5.setMinimumSize(new java.awt.Dimension(700, 150));
        jPanel5.setOpaque(false);
        jPanel5.setPreferredSize(new java.awt.Dimension(100, 205));

        jPanel7.setOpaque(false);
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos del Producto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.setOpaque(false);
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel22.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("Codigo de barras del producto: ");
        jPanel2.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 230, 30));

        txtCodigoProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodigoProductoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoProductoKeyTyped(evt);
            }
        });
        jPanel2.add(txtCodigoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, 130, 30));

        btnBuscarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Buscar_p.png"))); // NOI18N
        btnBuscarProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        btnBuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProductoActionPerformed(evt);
            }
        });
        jPanel2.add(btnBuscarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 20, 40, 30));

        jLabel17.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Nombre del producto: ");
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 170, 30));

        txtNombreProducto.setEnabled(false);
        jPanel2.add(txtNombreProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 60, 230, 30));

        jLabel19.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Stock:");
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 14, 80, 20));
        jPanel2.add(txtDescripcionProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 30, 20));

        txtStockProducto.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtStockProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtStockProducto.setEnabled(false);
        jPanel2.add(txtStockProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 30, 80, 20));

        txtPrecioProducto.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtPrecioProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPrecioProducto.setEnabled(false);
        jPanel2.add(txtPrecioProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 70, 80, 20));
        jPanel2.add(lblIdProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 20, 20));

        jLabel23.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Precio:");
        jPanel2.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 52, 80, 20));
        jPanel2.add(txtCostoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 80, 90, 20));

        jPanel7.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 560, 110));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pre transacción", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(540, 70));
        jPanel3.setLayout(null);

        jLabel21.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Cantidad: ");
        jPanel3.add(jLabel21);
        jLabel21.setBounds(0, 20, 80, 30);

        txtCantidadProducto.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
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
        jLabel24.setBounds(140, 20, 90, 30);

        txtTotalProducto.setBackground(new java.awt.Color(204, 255, 204));
        txtTotalProducto.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtTotalProducto.setForeground(new java.awt.Color(0, 102, 204));
        txtTotalProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTotalProducto.setDisabledTextColor(new java.awt.Color(0, 102, 204));
        txtTotalProducto.setEnabled(false);
        jPanel3.add(txtTotalProducto);
        txtTotalProducto.setBounds(230, 20, 80, 30);

        jPanel6.setOpaque(false);
        jPanel6.setLayout(new java.awt.GridLayout(1, 3, 5, 5));

        btnAgregarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Agregar_p1.png"))); // NOI18N
        btnAgregarProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
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

        btnEliminarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Remove.png"))); // NOI18N
        btnEliminarProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        btnEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProductoActionPerformed(evt);
            }
        });
        jPanel6.add(btnEliminarProducto);

        btnLimpiarTabla.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiarTabla.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/nuevo1.png"))); // NOI18N
        btnLimpiarTabla.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        btnLimpiarTabla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarTablaActionPerformed(evt);
            }
        });
        jPanel6.add(btnLimpiarTabla);

        jPanel3.add(jPanel6);
        jPanel6.setBounds(340, 10, 200, 50);

        jPanel7.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 560, -1));

        jPanel5.add(jPanel7);

        jPanel4.setOpaque(false);
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/bar_code_22177 (2).png"))); // NOI18N
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 230, 100));

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Serie");
        jPanel4.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 60, 20));

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Nº  de venta");
        jPanel4.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 120, 110, 20));

        txtNumero.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtNumero.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(txtNumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 140, 110, -1));

        txtSerie.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtSerie.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSerie.setText("001");
        jPanel4.add(txtSerie, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 59, -1));

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
        jPanel4.add(chkCambiarNumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 180, -1));

        jPanel5.add(jPanel4);

        center.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        tablePanel.setLayout(new java.awt.GridLayout(1, 0));

        jScrollPane3.setPreferredSize(new java.awt.Dimension(50, 403));

        tblDetalleProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblDetalleProducto.setModel(new javax.swing.table.DefaultTableModel(
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
        tblDetalleProducto.setRowHeight(22);
        jScrollPane3.setViewportView(tblDetalleProducto);

        tablePanel.add(jScrollPane3);

        center.add(tablePanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(center, java.awt.BorderLayout.CENTER);

        footer.setBackground(new java.awt.Color(247, 254, 255));
        footer.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        footer.setOpaque(false);
        footer.setLayout(new java.awt.GridLayout(2, 8, 5, 5));

        labelImporte.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelImporte.setForeground(new java.awt.Color(255, 255, 255));
        labelImporte.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelImporte.setText("Importe:");
        footer.add(labelImporte);

        txtImporte.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtImporte.setForeground(new java.awt.Color(255, 255, 255));
        txtImporte.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtImporte.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        txtImporte.setEnabled(false);
        footer.add(txtImporte);

        labelTotalVenta.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelTotalVenta.setForeground(new java.awt.Color(255, 255, 255));
        labelTotalVenta.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelTotalVenta.setText("Valor venta:");
        footer.add(labelTotalVenta);

        txtTotalVenta.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTotalVenta.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalVenta.setEnabled(false);
        footer.add(txtTotalVenta);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Subtotal:");
        footer.add(jLabel1);

        txtSubTotal.setEditable(false);
        txtSubTotal.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtSubTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        footer.add(txtSubTotal);

        labelTotalAPagar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelTotalAPagar.setForeground(new java.awt.Color(255, 255, 255));
        labelTotalAPagar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelTotalAPagar.setText("Total:");
        labelTotalAPagar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        footer.add(labelTotalAPagar);

        txtTotalPagar.setBackground(java.awt.Color.darkGray);
        txtTotalPagar.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtTotalPagar.setForeground(new java.awt.Color(0, 255, 102));
        txtTotalPagar.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalPagar.setDisabledTextColor(new java.awt.Color(0, 255, 102));
        txtTotalPagar.setEnabled(false);
        txtTotalPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalPagarActionPerformed(evt);
            }
        });
        footer.add(txtTotalPagar);

        labelCambio.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelCambio.setForeground(new java.awt.Color(255, 255, 255));
        labelCambio.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelCambio.setText("Cambio:");
        footer.add(labelCambio);

        txtCambio.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtCambio.setForeground(new java.awt.Color(255, 255, 0));
        txtCambio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCambio.setDisabledTextColor(new java.awt.Color(255, 255, 0));
        txtCambio.setEnabled(false);
        footer.add(txtCambio);

        labelDescuento.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelDescuento.setForeground(new java.awt.Color(255, 255, 255));
        labelDescuento.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelDescuento.setText("Descuento:");
        footer.add(labelDescuento);

        txtDescuento.setBackground(new java.awt.Color(255, 255, 204));
        txtDescuento.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtDescuento.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDescuento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDescuentoKeyReleased(evt);
            }
        });
        footer.add(txtDescuento);

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("I.G.V.:");
        footer.add(jLabel2);

        txtIGV.setEditable(false);
        txtIGV.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtIGV.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtIGV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIGVActionPerformed(evt);
            }
        });
        footer.add(txtIGV);

        jPanel1.setOpaque(false);
        footer.add(jPanel1);

        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("GENERAR");
        btnGuardar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        btnGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGuardar.setIconTextGap(0);
        btnGuardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        footer.add(btnGuardar);

        getContentPane().add(footer, java.awt.BorderLayout.PAGE_END);

        menuButton.setBackground(new java.awt.Color(255, 255, 255));
        menuButton.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        menuButton.setOpaque(false);
        menuButton.setLayout(new java.awt.GridLayout(3, 1, 5, 5));

        btnNuevo.setForeground(new java.awt.Color(255, 255, 255));
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/money_22144 (1).png"))); // NOI18N
        btnNuevo.setText("Nueva venta");
        btnNuevo.setToolTipText("");
        btnNuevo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        btnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNuevo.setIconTextGap(0);
        btnNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        menuButton.add(btnNuevo);

        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/anular-proceso.png"))); // NOI18N
        btnCancel.setText("Cancelar");
        btnCancel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancel.setIconTextGap(0);
        btnCancel.setMaximumSize(new java.awt.Dimension(95, 73));
        btnCancel.setMinimumSize(new java.awt.Dimension(95, 73));
        btnCancel.setPreferredSize(new java.awt.Dimension(95, 73));
        btnCancel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        menuButton.add(btnCancel);

        btnSalir.setForeground(new java.awt.Color(255, 255, 255));
        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/principal.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        btnSalir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSalir.setIconTextGap(0);
        btnSalir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        menuButton.add(btnSalir);

        getContentPane().add(menuButton, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        cargarComboTipoDocumento();
    }//GEN-LAST:event_formComponentShown

    private void btnBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProductoActionPerformed
        if(product != null){
            product.dispose();
            System.gc();
        } 
        product = new Consultas.FrmBuscarProducto(this);
        Presentacion.FrmPrincipal.Escritorio.add(product);
        product.toFront();
    }//GEN-LAST:event_btnBuscarProductoActionPerformed

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

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        Toast.makeText("Generando documento...", Toast.LENGTH_MICRO).show();
        ArrayList<ItemProductoCotizacion> lista = new ArrayList<>();
        
        for( int i = 0; i < tblDetalleProducto.getRowCount(); i++){
            ItemProductoCotizacion item = new ItemProductoCotizacion();
            item.setId(tblDetalleProducto.getValueAt(i, 0).toString());
            item.setCodigo(tblDetalleProducto.getValueAt(i, 1).toString());
            item.setNombre(tblDetalleProducto.getValueAt(i, 2).toString());
            item.setDescripcion(tblDetalleProducto.getValueAt(i, 3).toString());
            item.setCantidad(tblDetalleProducto.getValueAt(i, 4).toString());
            item.setCosto(tblDetalleProducto.getValueAt(i, 5).toString());
            item.setPrecio(tblDetalleProducto.getValueAt(i, 6).toString());
            item.setTotal(tblDetalleProducto.getValueAt(i, 7).toString());
            lista.add(item);
        }
        
        try {
            Map parametros = new HashMap();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            parametros.put("fecha", dtf.format(LocalDateTime.now()));
            parametros.put("total", txtTotalVenta.getText());
            parametros.put("descuento", txtDescuento.getText());
            parametros.put("totalPagar", txtTotalPagar.getText());
            
            JasperReport report = JasperCompileManager.compileReport(new File("").getAbsolutePath() + "/src/Reportes/RptCotizacion.jrxml");
            JasperPrint print = JasperFillManager.fillReport(report,  parametros, new JRBeanCollectionDataSource(lista) );
            JasperViewer visualiza = new JasperViewer(print, false);
            visualiza.setTitle("Cotización");
            visualiza.setVisible(true);
            Toast.makeText(Toast.SUCCESS, Message.SUCCESS_MESSAGE, Toast.LENGTH_MICRO).show();
        } catch (Exception e) {
            Toast.makeText(Toast.UNSUCCESS, Message.UNSUCCESS_MESSAGE , Toast.LENGTH_MICRO).show();
            Message.LOGGER.log(Level.WARNING, e.getMessage());
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        accion = "Nuevo";
        modificar();
        limpiarCampos();
        state = EFormState.ENABLE;
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

    private void txtIGVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIGVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIGVActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        mirar();
        limpiarTabla();
        state = EFormState.DISABLE;
    }//GEN-LAST:event_btnCancelActionPerformed
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
        String codigo, strId;
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
    private javax.swing.JButton btnBuscarProducto;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnEliminarProducto;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiarTabla;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JPanel center;
    private javax.swing.JCheckBox chkCambiarNumero;
    private javax.swing.JPanel footer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel labelCambio;
    private javax.swing.JLabel labelDescuento;
    private javax.swing.JLabel labelImporte;
    private javax.swing.JLabel labelTotalAPagar;
    private javax.swing.JLabel labelTotalVenta;
    public static javax.swing.JLabel lblIdProducto;
    private javax.swing.JPanel menuButton;
    private javax.swing.JPanel tablePanel;
    private javax.swing.JTable tblDetalleProducto;
    private javax.swing.JTextField txtCambio;
    private javax.swing.JTextField txtCantidadProducto;
    public static javax.swing.JTextField txtCodigoProducto;
    public static javax.swing.JLabel txtCostoProducto;
    public static javax.swing.JLabel txtDescripcionProducto;
    private javax.swing.JTextField txtDescuento;
    private javax.swing.JTextField txtIGV;
    private javax.swing.JTextField txtImporte;
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
    public void loadProduct(ClsEntidadProducto producto) {
        if(state.equals(EFormState.ENABLE)){
            lblIdProducto.setText(producto.getStrIdProducto());
            txtCodigoProducto.setText(producto.getStrCodigoProducto());
            txtNombreProducto.setText(producto.getStrNombreProducto());
            txtDescripcionProducto.setText(producto.getStrDescripcionProducto());
            txtStockProducto.setText(producto.getStrStockProducto());
            txtCostoProducto.setText(producto.getStrPrecioCostoProducto());  // producto.preciocosto 
            txtPrecioProducto.setText(producto.getStrPrecioVentaProducto()); // producto.precioVenta
            Toast.makeText(Toast.SUCCESS, "Se agregó el producto", Toast.LENGTH_MICRO).show();
        } else {
            Toast.makeText(Toast.UNSUCCESS, "Fornulario inactivo, no se agregó el producto", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void readFrameRectanble() {
        ObjectDeserializer<Rectangle> deserializer = new ObjectDeserializer<>(Paths.SERIAL_DIRECTORY_DATA, Paths.QUOTATION_RECTANGLE_NAME);
        Rectangle rectangle = deserializer.deserialicer();
        if(rectangle != null)
            this.setBounds( rectangle );
    }

    @Override
    public void writeFrameRectangle() {
        ObjectSerializer<Rectangle> serializer = new ObjectSerializer<>(Paths.SERIAL_DIRECTORY_DATA, Paths.QUOTATION_RECTANGLE_NAME);
        serializer.serilizer(this.getBounds());
    }
    
    //    @Override
//    public void dispose(){
//        try {
//            if (product != null) {
//                product.dispose();
//            }
//            writeFrameRectangle();
//        } catch (Exception e) {
//            Message.LOGGER.log(Level.SEVERE, e.getMessage());
//        }
//        super.dispose();
//    }

    @Override
    public void dispose() {
        try {
            if (product != null) {
                product.dispose();
            }
            writeFrameRectangle();
        } catch (Exception e) {
            Message.LOGGER.log(Level.SEVERE, e.getMessage());
        }
        super.dispose();
    }

   
    
}
