/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Presentacion;

import Consultas.FrmProductInformation;
import Conexion.ClsConexion;
import Consultas.ImageSelector;
import Entidad.*;
import Entidad.dtos.ProductDto;
import Negocio.*;
import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

//--------------CODIGO DE BARRAS------------
//import java.awt.Image;
import Entidad.Image;
import java.awt.Cursor;
import javax.swing.ImageIcon;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeImageHandler;
import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import net.sourceforge.barbecue.output.OutputException;
import statics.Message;
import statics.TableConfigurator;
import tools.toast.Toast;

public class FrmProducto extends javax.swing.JInternalFrame {

    private Connection connection = new ClsConexion().getConection();
    String Total;
    String strCodigo;
    String accion;

    int registros;
    String id[] = new String[50];
    static int intContador;
    public String codigo;
    static Connection conn = null;
    static ResultSet rs = null;
    String criterio, busqueda;
//    private DefaultTableModel dtm = new DefaultTableModel();
    private DefaultTableModel dtm;
    private final String[] titulos = {"ID", "CÓDIGO", "NOMBRE", "DESCRIPCIÓN", "STOCK", "STOCK MIN", "COSTO", "PRECIO", "UTILIDAD", "ESTADO", "CATEGORÍA", "VENCIMIENTO"};
    private int[] anchos = {40, 100, 150, 200, 60, 60, 60, 60, 60, 80, 100, 80};
    
    private ImageSelector imageSelector;
    private ClsEntidadProducto product;
    private ProductDto productDto;
            
    public FrmProducto() {
        initComponents();
        tabProducto.setIconAt(tabProducto.indexOfComponent(pBuscar), new ImageIcon("src/iconos/busca_p1.png"));
        tabProducto.setIconAt(tabProducto.indexOfComponent(pNuevo), new ImageIcon("src/iconos/nuevo1.png"));
        cargarComboCategoria();
        buttonGroup1.add(rbtnCodigo);
        buttonGroup1.add(rbtnNombre);
        buttonGroup1.add(rbtnDescripcion);
        buttonGroup1.add(rbtnCategoria);
        buttonGroup2.add(rbtnActivo);
        buttonGroup2.add(rbtnInactivo);

        mirar();
        actualizarTabla();

        //---------------------ANCHO Y ALTO DEL FORM----------------------
        this.setSize(965, 600);
        CrearTabla();
        CantidadTotal();
        
//        dtm = new DefaultTableModel(null, titulos) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false;
//            }
//        };

    }

    public void duplicateProduct(ClsEntidadProducto producto) {

        // escribir en el formulario
    }
//-----------------------------------------------------------------------------------------------
//--------------------------------------METODOS--------------------------------------------------
//-----------------------------------------------------------------------------------------------

    void CrearTabla() {
        //Agregar Render
        for (int i = 0; i < tblProducto.getColumnCount(); i++) {
            TableCellRenderer render = TableConfigurator.configureTableItem();
            tblProducto.getColumnModel().getColumn(i).setCellRenderer(render);
        }

        //Activar ScrollBar
        tblProducto.setAutoResizeMode(tblProducto.AUTO_RESIZE_OFF);

        //Anchos de cada columna
//        int[] anchos = {40, 100, 150, 200, 60, 60, 60, 60, 60, 80, 100, 50, 80};
        for (int i = 0; i < tblProducto.getColumnCount(); i++) {
            tblProducto.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
    }

    void CantidadTotal() {
        Total = String.valueOf(tblProducto.getRowCount());
        lblEstado.setText("Se cargaron " + Total + " registros");
    }

    void limpiarCampos() {
        txtId.setText("");
        txtCodigoBar.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtStock.setText("");
        txtStockMin.setText("");
        txtPrecioCosto.setText("");
        txtPrecioVenta.setText("");
        jLabelPhoto.setIcon(null);
        txtUtilidad.setText("");
        txtCodigoBar.requestFocus();
        rbtnActivo.setSelected(true);
        rbtnInactivo.setSelected(false);
        txtPrecioCosto.setText("0.0");
        txtPrecioVenta.setText("0.0");
        txtUtilidad.setText("0.0");

        rbtnCodigo.setSelected(false);
        rbtnNombre.setSelected(false);
        rbtnDescripcion.setSelected(false);
        rbtnCategoria.setSelected(false);
        txtBusqueda.setText("");
        limpiarList();
    }

    void mirar() {
        tblProducto.setEnabled(true);
        btnNuevo.setEnabled(true);
        btnModificar.setEnabled(true);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnSalir1.setEnabled(true);
//        btnSeleccionarImagen.setEnabled(false);

        txtCodigoBar.setEnabled(false);
        txtNombre.setEnabled(false);
        txtDescripcion.setEnabled(false);
        txtStock.setEnabled(false);
        txtStockMin.setEnabled(false);
        txtPrecioCosto.setEnabled(false);
        txtPrecioVenta.setEnabled(false);
        cboCategoria.setEnabled(false);
        rbtnActivo.setEnabled(false);
        rbtnInactivo.setEnabled(false);

        btnActualizar.setEnabled(false);
        lstCodigos.setEnabled(false);
        cboTipoCodificacion.setEnabled(false);
        btnGenerar.setEnabled(false);
        dcFechaVencimiento.setEnabled(false);
        
        btnRevert.setVisible(false);
        btnSelectPhoto.setVisible(false);
        btnRemovePhoto.setVisible(false);
        jLabelPhoto.setIcon(null);

    }

    void modificar() {
        tblProducto.setEnabled(false);
        btnNuevo.setEnabled(false);
        btnModificar.setEnabled(false);
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
//        btnSeleccionarImagen.setEnabled(true);
        btnSalir1.setEnabled(false);

        txtCodigoBar.setEnabled(true);
        txtNombre.setEnabled(true);
        txtDescripcion.setEnabled(true);
        txtStock.setEnabled(true);
        txtStockMin.setEnabled(true);
        txtPrecioCosto.setEnabled(true);
        txtPrecioVenta.setEnabled(true);
        cboCategoria.setEnabled(true);
        rbtnActivo.setEnabled(true);
        rbtnInactivo.setEnabled(true);

        btnActualizar.setEnabled(true);
        lstCodigos.setEnabled(true);
        cboTipoCodificacion.setEnabled(true);
        btnGenerar.setEnabled(true);
        txtCodigoBar.requestFocus();
        dcFechaVencimiento.setEnabled(true);
        
        btnSelectPhoto.setVisible(true);
        btnRemovePhoto.setVisible(true);

    }

    void cargarComboCategoria() {
        ClsCategoria tipodocumento = new ClsCategoria();
        ArrayList<ClsEntidadCategoria> categorias = tipodocumento.listarCategoria();
        Iterator iterator = categorias.iterator();
        DefaultComboBoxModel DefaultComboBoxModel = new DefaultComboBoxModel();
        DefaultComboBoxModel.removeAllElements();

        cboCategoria.removeAll();
        String fila[] = new String[2];
        intContador = 0;

        while (iterator.hasNext()) {
            ClsEntidadCategoria Categoria = new ClsEntidadCategoria();
            Categoria = (ClsEntidadCategoria) iterator.next();
            id[intContador] = Categoria.getStrIdCategoria();
            fila[0] = Categoria.getStrIdCategoria();
            fila[1] = Categoria.getStrDescripcionCategoria();
            DefaultComboBoxModel.addElement(Categoria.getStrDescripcionCategoria());
            intContador++;
        }
        cboCategoria.setModel(DefaultComboBoxModel);
    }

    void actualizarTabla() {
        ClsProducto productos = new ClsProducto();
        ArrayList<ClsEntidadProducto> producto = productos.listarProducto();
        Iterator iterator = producto.iterator();

        dtm = new DefaultTableModel(null, titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String fila[] = new String[12];
        while (iterator.hasNext()) {
            ClsEntidadProducto Producto = new ClsEntidadProducto();
            Producto = (ClsEntidadProducto) iterator.next();
            fila[0] = Producto.getStrIdProducto();
            fila[1] = Producto.getStrCodigoProducto();
            fila[2] = Producto.getStrNombreProducto();
            fila[3] = Producto.getStrDescripcionProducto();
            fila[4] = Producto.getStrStockProducto();
            fila[5] = Producto.getStrStockMinProducto();
            fila[6] = Producto.getStrPrecioCostoProducto();
            fila[7] = Producto.getStrPrecioVentaProducto();
            fila[8] = Producto.getStrUtilidadProducto();
            fila[9] = Producto.getStrEstadoProducto();
            fila[10] = Producto.getStrDescripcionCategoria();
            fila[11] = Producto.getFechaVencimiento() == null ? "" : Producto.getFechaVencimiento().toString();
            dtm.addRow(fila);
        }
        tblProducto.setModel(dtm);
    }

    void BuscarProducto() {

        ClsProducto categoria = new ClsProducto();
        busqueda = txtBusqueda.getText();
        if (rbtnCodigo.isSelected()) {
            criterio = "codigo";
        } else if (rbtnNombre.isSelected()) {
            criterio = "nombre";
        } else if (rbtnDescripcion.isSelected()) {
            criterio = "descripcion";
        } else if (rbtnCategoria.isSelected()) {
            criterio = "categoria";
        }
        try {
            rs = categoria.listarProductoPorParametro(criterio, busqueda);
            boolean encuentra = false;
            String Datos[] = new String[12];
            int f, i;
            f = dtm.getRowCount();
            if (f > 0) {
                for (i = 0; i < f; i++) {
                    dtm.removeRow(0);
                }
            }
            while (rs.next()) {
                Datos[0] = (String) rs.getString(1);
                Datos[1] = (String) rs.getString(2);
                Datos[2] = (String) rs.getString(3);
                Datos[3] = (String) rs.getString(4);
                Datos[4] = (String) rs.getString(5);
                Datos[5] = (String) rs.getString(6);
                Datos[6] = (String) rs.getString(7);
                Datos[7] = (String) rs.getString(8);
                Datos[8] = (String) rs.getString(9);
                Datos[9] = (String) rs.getString(10);
                Datos[10] = (String) rs.getString(11);
                Datos[11] = (String) rs.getString(12);
                dtm.addRow(Datos);
                encuentra = true;
            }
            if (encuentra = false) {
                JOptionPane.showMessageDialog(null, "¡No se encuentra!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        tblProducto.setModel(dtm);
    }

//    void listardatos() {
//        String estado;
//        DefaultTableModel defaultTableModel = new DefaultTableModel();
//        if (registros == -1) {
//            JOptionPane.showMessageDialog(null, "Se debe seleccionar un registro");
//        } else {
//            defaultTableModel = (DefaultTableModel) tblProducto.getModel();
//            strCodigo = ((String) defaultTableModel.getValueAt(registros, 0));
//            txtCodigo.setText((String) defaultTableModel.getValueAt(registros, 0));
//            txtCodigoBar.setText((String) defaultTableModel.getValueAt(registros, 1));
//            txtNombre.setText((String) defaultTableModel.getValueAt(registros, 2));
//            txtDescripcion.setText((String) defaultTableModel.getValueAt(registros, 3));
//            txtStock.setText((String) defaultTableModel.getValueAt(registros, 4));
//            txtStockMin.setText((String) defaultTableModel.getValueAt(registros, 5));
//            txtPrecioCosto.setText((String) defaultTableModel.getValueAt(registros, 6));
//            txtPrecioVenta.setText((String) defaultTableModel.getValueAt(registros, 7));
//            txtUtilidad.setText((String) defaultTableModel.getValueAt(registros, 8));
//            if ("ACTIVO".equals((String) defaultTableModel.getValueAt(registros, 9))) {
//                rbtnActivo.setSelected(true);
//            } else if ("INACTIVO".equals((String) defaultTableModel.getValueAt(registros, 9))) {
//                rbtnInactivo.setSelected(true);
//            }
//            cboCategoria.setSelectedItem((String) defaultTableModel.getValueAt(registros, 10));
//            imagen = (String) defaultTableModel.getValueAt(registros, 11);
//            String ruta = "src/Images/" + imagen;
//            ImageIcon fot = new ImageIcon(ruta);
//            Icon icono = new ImageIcon(fot.getImage().getScaledInstance(lblImagen.getWidth(), lblImagen.getHeight(), Image.SCALE_DEFAULT));
//            lblImagen.setIcon(icono);
//            lblImagen.repaint();
//
//            tblProducto.setRowSelectionInterval(registros, registros);
//        }
//
//    }

    void CalcularUtilidad() {
        double pre_costo = 0, pre_venta = 0, utilidad = 0, t_utilidad;
        pre_costo = Double.parseDouble(txtPrecioCosto.getText());
        pre_venta = Double.parseDouble(txtPrecioVenta.getText());
        utilidad = pre_venta - pre_costo;
        t_utilidad = Math.rint(utilidad * 100) / 100;
        txtUtilidad.setText(String.valueOf(t_utilidad));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane2 = new javax.swing.JTabbedPane();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPopupMenu = new javax.swing.JPopupMenu();
        duplicateItem = new javax.swing.JMenuItem();
        btnModificar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        tabProducto = new javax.swing.JTabbedPane();
        pBuscar = new javax.swing.JPanel();
        lblEstado = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        txtBusqueda = new javax.swing.JTextField();
        rbtnCodigo = new javax.swing.JRadioButton();
        rbtnNombre = new javax.swing.JRadioButton();
        rbtnCategoria = new javax.swing.JRadioButton();
        rbtnDescripcion = new javax.swing.JRadioButton();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProducto = new javax.swing.JTable();
        pNuevo = new javax.swing.JPanel();
        txtId = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCodigoBar = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtStockMin = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtPrecioVenta = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtPrecioCosto = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        rbtnActivo = new javax.swing.JRadioButton();
        rbtnInactivo = new javax.swing.JRadioButton();
        jLabel11 = new javax.swing.JLabel();
        txtStock = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtUtilidad = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        cboCategoria = new javax.swing.JComboBox();
        btnGenerar = new javax.swing.JButton();
        panel = new javax.swing.JPanel();
        lblprueba = new javax.swing.JLabel();
        cboTipoCodificacion = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        btnImprimir = new javax.swing.JButton();
        jLabelPhoto = new javax.swing.JLabel();
        btnActualizar = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstCodigos = new javax.swing.JList();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        dcFechaVencimiento = new com.toedter.calendar.JDateChooser();
        jLabel14 = new javax.swing.JLabel();
        btnRevert = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnSelectPhoto = new javax.swing.JButton();
        btnRemovePhoto = new javax.swing.JButton();
        btnSalir1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        jPopupMenu.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPopupMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        duplicateItem.setText("Duplicar");
        duplicateItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                duplicateItemActionPerformed(evt);
            }
        });
        jPopupMenu.add(duplicateItem);

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Productos");
        getContentPane().setLayout(null);

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/editar.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnModificar.setIconTextGap(0);
        btnModificar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        getContentPane().add(btnModificar);
        btnModificar.setBounds(780, 190, 160, 70);

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/cancelar.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancelar.setIconTextGap(0);
        btnCancelar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCancelar);
        btnCancelar.setBounds(780, 260, 160, 70);

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/agregar.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNuevo.setIconTextGap(0);
        btnNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        getContentPane().add(btnNuevo);
        btnNuevo.setBounds(780, 50, 160, 70);

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGuardar.setIconTextGap(0);
        btnGuardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        getContentPane().add(btnGuardar);
        btnGuardar.setBounds(780, 120, 160, 70);

        tabProducto.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabProductoStateChanged(evt);
            }
        });

        pBuscar.setBackground(new java.awt.Color(255, 255, 255));
        pBuscar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pBuscar.add(lblEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 200, 20));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/report.png"))); // NOI18N
        jButton3.setText("Reporte");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        pBuscar.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 30, 120, 50));

        txtBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBusquedaKeyReleased(evt);
            }
        });
        pBuscar.add(txtBusqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 440, 30));

        rbtnCodigo.setText("Cód. Producto");
        rbtnCodigo.setOpaque(false);
        rbtnCodigo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbtnCodigoStateChanged(evt);
            }
        });
        pBuscar.add(rbtnCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 110, -1));

        rbtnNombre.setText("Nombre");
        rbtnNombre.setOpaque(false);
        rbtnNombre.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbtnNombreStateChanged(evt);
            }
        });
        pBuscar.add(rbtnNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(143, 30, 80, -1));

        rbtnCategoria.setText("Categoría");
        rbtnCategoria.setOpaque(false);
        pBuscar.add(rbtnCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 30, -1, -1));

        rbtnDescripcion.setText("Descripción");
        rbtnDescripcion.setOpaque(false);
        pBuscar.add(rbtnDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 30, -1, -1));

        jLabel13.setBackground(new java.awt.Color(255, 153, 0));
        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Criterios de Búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jLabel13.setOpaque(true);
        pBuscar.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 720, 90));

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
        tblProducto.setComponentPopupMenu(jPopupMenu);
        tblProducto.setRowHeight(22);
        tblProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblProducto);

        pBuscar.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 720, 340));

        tabProducto.addTab("Buscar", pBuscar);

        pNuevo.setBackground(new java.awt.Color(255, 255, 255));
        pNuevo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtId.setEnabled(false);
        txtId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdActionPerformed(evt);
            }
        });
        pNuevo.add(txtId, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 70, 40));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("ID Producto:");
        pNuevo.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 90, 20));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Código de Barras:");
        pNuevo.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 30, 120, 20));

        txtCodigoBar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodigoBarKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoBarKeyTyped(evt);
            }
        });
        pNuevo.add(txtCodigoBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 20, 160, 40));

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Nombre del producto:");
        pNuevo.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 140, 20));

        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNombreKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreKeyTyped(evt);
            }
        });
        pNuevo.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 110, 310, 30));

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Stock: ");
        pNuevo.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 90, 30));

        txtStockMin.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtStockMin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtStockMinKeyReleased(evt);
            }
        });
        pNuevo.add(txtStockMin, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 260, 80, 30));

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Stock Mínimo: ");
        pNuevo.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 260, 100, 30));

        txtPrecioVenta.setBackground(new java.awt.Color(254, 254, 241));
        txtPrecioVenta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPrecioVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPrecioVentaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioVentaKeyTyped(evt);
            }
        });
        pNuevo.add(txtPrecioVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 310, 80, 30));

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Descripción:");
        pNuevo.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 140, 20));

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Precio Costo: ");
        pNuevo.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 90, 30));

        txtPrecioCosto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPrecioCosto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPrecioCostoKeyReleased(evt);
            }
        });
        pNuevo.add(txtPrecioCosto, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 310, 80, 30));

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Precio Venta: ");
        pNuevo.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 310, 100, 30));

        rbtnActivo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rbtnActivo.setText("ACTIVO");
        rbtnActivo.setOpaque(false);
        pNuevo.add(rbtnActivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 140, 80, -1));

        rbtnInactivo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rbtnInactivo.setText("INACTIVO");
        rbtnInactivo.setOpaque(false);
        pNuevo.add(rbtnInactivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 140, 90, -1));

        jLabel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Estado"));
        pNuevo.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 120, 200, 50));

        txtStock.setBackground(new java.awt.Color(242, 253, 253));
        txtStock.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtStock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtStockKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStockKeyTyped(evt);
            }
        });
        pNuevo.add(txtStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 260, 80, 30));

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Fecha de vencimiento: ");
        pNuevo.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 410, 210, 30));

        txtUtilidad.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtUtilidad.setEnabled(false);
        pNuevo.add(txtUtilidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 360, 270, 30));

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        jScrollPane3.setViewportView(txtDescripcion);

        pNuevo.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, 310, 60));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Categoría:");
        pNuevo.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 140, 30));

        cboCategoria.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        pNuevo.add(cboCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 310, 30));

        btnGenerar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Generar.png"))); // NOI18N
        btnGenerar.setText("Generar cod. barras");
        btnGenerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarActionPerformed(evt);
            }
        });
        pNuevo.add(btnGenerar, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 10, 0, 10));

        panel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panel.add(lblprueba, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 0, 0));

        pNuevo.add(panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 90, -1, -1));

        cboTipoCodificacion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Code 128" }));
        cboTipoCodificacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTipoCodificacionActionPerformed(evt);
            }
        });
        pNuevo.add(cboTipoCodificacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 0, -1, -1));

        jLabel9.setText("Tipo de Codificación:");
        pNuevo.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 40, 10, 0));

        jLabel18.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        pNuevo.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 30, 0, 0));

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 51, 153));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("*");
        pNuevo.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 40, 20, 20));

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 51, 153));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("*");
        pNuevo.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 110, 20, 20));

        btnImprimir.setText("Imprimir codigo de barras");
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });
        pNuevo.add(btnImprimir, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 60, 160, 40));

        jLabelPhoto.setBackground(new java.awt.Color(255, 255, 153));
        jLabelPhoto.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabelPhoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelPhoto.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pNuevo.add(jLabelPhoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 250, 220, 170));

        btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Refresh.png"))); // NOI18N
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        pNuevo.add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 10, 40, 30));

        jLabel16.setText("Generar codigos aleatorios:");
        pNuevo.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 10, 150, 30));

        lstCodigos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lstCodigos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstCodigosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(lstCodigos);

        pNuevo.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 40, 200, 70));

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Utilidad: ");
        pNuevo.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 90, 30));

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Utilidad: ");
        pNuevo.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 90, 30));

        dcFechaVencimiento.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        pNuevo.add(dcFechaVencimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 410, 120, 30));

        jLabel14.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos del Producto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pNuevo.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 480, 240));

        btnRevert.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Refresh.png"))); // NOI18N
        btnRevert.setText("REVERTIR ORIGINAL");
        btnRevert.setMaximumSize(new java.awt.Dimension(300, 30));
        btnRevert.setMinimumSize(new java.awt.Dimension(300, 30));
        btnRevert.setPreferredSize(new java.awt.Dimension(300, 30));
        btnRevert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRevertActionPerformed(evt);
            }
        });
        pNuevo.add(btnRevert, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 220, 220, 30));

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel1.setMaximumSize(new java.awt.Dimension(300, 32767));
        jPanel1.setMinimumSize(new java.awt.Dimension(300, 27));
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(300, 27));
        jPanel1.setLayout(new java.awt.GridLayout(1, 2, 1, 1));

        btnSelectPhoto.setText("SELECCIONAR");
        btnSelectPhoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectPhotoActionPerformed(evt);
            }
        });
        jPanel1.add(btnSelectPhoto);

        btnRemovePhoto.setText("QUITAR");
        btnRemovePhoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemovePhotoActionPerformed(evt);
            }
        });
        jPanel1.add(btnRemovePhoto);

        pNuevo.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 420, 220, 30));

        tabProducto.addTab("Nuevo / Modificar", pNuevo);

        getContentPane().add(tabProducto);
        tabProducto.setBounds(10, 10, 750, 510);

        btnSalir1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/principal.png"))); // NOI18N
        btnSalir1.setText("Salir");
        btnSalir1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSalir1.setIconTextGap(0);
        btnSalir1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSalir1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalir1ActionPerformed(evt);
            }
        });
        getContentPane().add(btnSalir1);
        btnSalir1.setBounds(780, 330, 160, 100);

        jLabel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Mantenimiento"));
        getContentPane().add(jLabel1);
        jLabel1.setBounds(770, 30, 180, 490);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        if (tblProducto.getSelectedRows().length > 0) {
            accion = "Modificar";
//            loadProduct();
            modificar();
            btnRevert.setVisible(true);
            tabProducto.setSelectedIndex(tabProducto.indexOfComponent(pNuevo));
        } else {
            JOptionPane.showMessageDialog(null, "¡Se debe seleccionar un registro!");
        }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        mirar();
        tabProducto.setSelectedIndex(tabProducto.indexOfComponent(pBuscar));
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        product = new ClsEntidadProducto();
        accion = "Nuevo";
        modificar();
        limpiarCampos();
        tblProducto.setEnabled(false);
        tabProducto.setSelectedIndex(tabProducto.indexOfComponent(pNuevo));
    }//GEN-LAST:event_btnNuevoActionPerformed
//----------------------VALIDACIÓN DE DATOS-------------------------------------

    public boolean validardatos() {
        if (txtCodigoBar.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Especifique un código de barras para el Producto");
            txtCodigoBar.requestFocus();
            txtCodigoBar.setBackground(Color.YELLOW);
            return false;
        } else if (txtNombre.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Ingrese un nombre para el Producto");
            txtNombre.requestFocus();
            txtNombre.setBackground(Color.YELLOW);
            return false;
        } else {
            return true;
        }

    }
    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        if (validardatos() == true) {
            if (accion.equals("Nuevo")) {
                ClsProducto productos = new ClsProducto();
//                ClsEntidadProducto product = new ClsEntidadProducto();
                product.setStrCodigoProducto(txtCodigoBar.getText());
                product.setStrNombreProducto(txtNombre.getText());
                product.setStrDescripcionProducto(txtDescripcion.getText());
                if (txtStock.getText().equals("")) {
                    product.setStrStockProducto("0");
                } else {
                    product.setStrStockProducto(txtStock.getText());
                }
                if (txtStockMin.getText().equals("")) {
                    product.setStrStockMinProducto("0");
                } else {
                    product.setStrStockMinProducto(txtStockMin.getText());
                }
                if (txtPrecioCosto.getText().equals("")) {
                    product.setStrPrecioCostoProducto("0");
                } else {
                    product.setStrPrecioCostoProducto(txtPrecioCosto.getText());
                }
                if (txtPrecioVenta.getText().equals("")) {
                    product.setStrPrecioVentaProducto("0");
                } else {
                    product.setStrPrecioVentaProducto(txtPrecioVenta.getText());
                }
                if (txtUtilidad.getText().equals("")) {
                    product.setStrUtilidadProducto("0");
                } else {
                    product.setStrUtilidadProducto(txtUtilidad.getText());
                }
                if (rbtnActivo.isSelected()) {
                    product.setStrEstadoProducto("ACTIVO");
                } else if (rbtnInactivo.isSelected()) {
                    product.setStrEstadoProducto("INACTIVO");
                }
                product.setStrIdCategoria(id[cboCategoria.getSelectedIndex()]);
                if (product.getImagen() == null) {
                    product.setImagen(new Image(null, 0));
                }
                if (dcFechaVencimiento != null) {
                    try {
                        product.setFechaVencimiento(dcFechaVencimiento.getDate());
                    } catch (Exception e) {
                        e.getStackTrace();
                        product.setFechaVencimiento(null);
                    }
                }
                productos.agregarProducto(product);
                actualizarTabla();
                CantidadTotal();
            }
            if (accion.equals("Modificar")) {
                ClsProducto productos = new ClsProducto();
                product.setStrCodigoProducto(txtCodigoBar.getText());
                product.setStrNombreProducto(txtNombre.getText());
                product.setStrDescripcionProducto(txtDescripcion.getText());
                if (txtStock.getText().equals("")) {
                    product.setStrStockProducto("0");
                } else {
                    product.setStrStockProducto(txtStock.getText());
                }
                if (txtStockMin.getText().equals("")) {
                    product.setStrStockMinProducto("0");
                } else {
                    product.setStrStockMinProducto(txtStockMin.getText());
                }
                if (txtPrecioCosto.getText().equals("")) {
                    product.setStrPrecioCostoProducto("0");
                } else {
                    product.setStrPrecioCostoProducto(txtPrecioCosto.getText());
                }
                if (txtPrecioVenta.getText().equals("")) {
                    product.setStrPrecioVentaProducto("0");
                } else {
                    product.setStrPrecioVentaProducto(txtPrecioVenta.getText());
                }
                if (txtUtilidad.getText().equals("")) {
                    product.setStrUtilidadProducto("0");
                } else {
                    product.setStrUtilidadProducto(txtUtilidad.getText());
                }
                if (rbtnActivo.isSelected()) {
                    product.setStrEstadoProducto("ACTIVO");
                } else if (rbtnInactivo.isSelected()) {
                    product.setStrEstadoProducto("INACTIVO");
                }
                product.setStrIdCategoria(id[cboCategoria.getSelectedIndex()]);
                
                
                if (dcFechaVencimiento != null) {
                    try {
                        product.setFechaVencimiento(dcFechaVencimiento.getDate());
                    } catch (Exception e) {
                        e.getStackTrace();
                        product.setFechaVencimiento(null);
                    }
                }
                productos.modificarProducto(strCodigo, product);
                actualizarTabla();
                modificar();
                limpiarCampos();
                CantidadTotal();
            }
            CrearTabla();
            mirar();
            tabProducto.setSelectedIndex(tabProducto.indexOfComponent(pBuscar));

        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void txtBusquedaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaKeyReleased
        BuscarProducto();
        CrearTabla();
        CantidadTotal();
    }//GEN-LAST:event_txtBusquedaKeyReleased

    private void rbtnCodigoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rbtnCodigoStateChanged
        txtBusqueda.setText("");
    }//GEN-LAST:event_rbtnCodigoStateChanged

    private void rbtnNombreStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rbtnNombreStateChanged
        txtBusqueda.setText("");
    }//GEN-LAST:event_rbtnNombreStateChanged

    private void tblProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductoMouseClicked
        if(evt.getClickCount() == 2){
            tblProducto.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            int row = tblProducto.getSelectedRow();
            DefaultTableModel defaultTableModel = (DefaultTableModel) tblProducto.getModel();
//            productDto.setStrIdProducto();
            ClsProducto productService = new ClsProducto();
            ProductDto productDto = productService.findById( Integer.parseInt((String) defaultTableModel.getValueAt(row, 0)) );
            FrmProductInformation productInf = new FrmProductInformation(productDto);
            tblProducto.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            Presentacion.FrmPrincipal.Escritorio.add(productInf);
            productInf.toFront();
        }
    }//GEN-LAST:event_tblProductoMouseClicked

    private void loadProduct() {
        int fila;
        DefaultTableModel defaultTableModel = new DefaultTableModel();
        fila = tblProducto.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Se debe seleccionar un registro");
        } else {
            defaultTableModel = (DefaultTableModel) tblProducto.getModel();
            strCodigo = ((String) defaultTableModel.getValueAt(fila, 0));
            txtId.setText((String) defaultTableModel.getValueAt(fila, 0));
            txtCodigoBar.setText((String) defaultTableModel.getValueAt(fila, 1));
            txtNombre.setText((String) defaultTableModel.getValueAt(fila, 2));
            txtDescripcion.setText((String) defaultTableModel.getValueAt(fila, 3));
            txtStock.setText((String) defaultTableModel.getValueAt(fila, 4));
            txtStockMin.setText((String) defaultTableModel.getValueAt(fila, 5));
            txtPrecioCosto.setText((String) defaultTableModel.getValueAt(fila, 6));
            txtPrecioVenta.setText((String) defaultTableModel.getValueAt(fila, 7));
            txtUtilidad.setText((String) defaultTableModel.getValueAt(fila, 8));
            if ("ACTIVO".equals((String) defaultTableModel.getValueAt(fila, 9))) {
                rbtnActivo.setSelected(true);
            } else if ("INACTIVO".equals((String) defaultTableModel.getValueAt(fila, 9))) {
                rbtnInactivo.setSelected(true);
            }
            cboCategoria.setSelectedItem((String) defaultTableModel.getValueAt(fila, 10));
            try {
                if(defaultTableModel.getValueAt(fila, 12) != null & !defaultTableModel.getValueAt(fila, 12).toString().isEmpty()){
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                    dcFechaVencimiento.setDate(
                            formatter.parse(defaultTableModel.getValueAt(fila, 12).toString().replace("-", "/"))
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mirar();
    }

    private void txtCodigoBarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoBarKeyTyped
        txtCodigoBar.setBackground(Color.WHITE);
        //        char car = evt.getKeyChar();
        //        if((car<'a' || car>'z') && (car<'A' || car>'Z')) evt.consume();
    }//GEN-LAST:event_txtCodigoBarKeyTyped

    private void txtPrecioCostoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioCostoKeyReleased
        CalcularUtilidad();
        int keyCode = evt.getKeyCode();
        if (keyCode == KeyEvent.VK_ENTER)
            txtPrecioVenta.requestFocus();
    }//GEN-LAST:event_txtPrecioCostoKeyReleased

    private void txtPrecioVentaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioVentaKeyReleased
        CalcularUtilidad();
        int keyCode = evt.getKeyCode();
        if (keyCode == KeyEvent.VK_ENTER)
            btnGuardar.requestFocus();
    }//GEN-LAST:event_txtPrecioVentaKeyReleased
//    void verificarCodigoBar() {
//        String busqueda = null;
//        int sen = 2;
//        busqueda = txtCodigoBar.getText();
//
//        try {
//            ClsProducto oProducto = new ClsProducto();
//
//            rs = oProducto.verificarCodigoBar(busqueda);
//            while (rs.next()) {
//                if (!rs.getString(2).equals("")) {
//
//                    sen = 1;
//                } else {
//
//                    sen = 2;
//                }
//                break;
//            }
//
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this, ex.getMessage());
//            System.out.println(ex.getMessage());
//        }
//
//        if (sen == 1) {
//            JOptionPane.showMessageDialog(null, "Codigo No Disponible");
//        } else if (sen == 2) {
//            JOptionPane.showMessageDialog(null, "Codigo Disponible");
//        } else if (rs == null) {
//            JOptionPane.showMessageDialog(null, "no hay");
//        }
//
//    }

    void GeneraAleatorio() {

        String codbar;
        int sen = 2;
        DefaultListModel modelo = new DefaultListModel();

        for (int i = 1; i <= 4; i++) {
            codbar = String.valueOf((int) (Math.random() * (500000 - 100000 + 1) + 100000));
            //codbar=String.valueOf((int)(Math.random()*(9-1+1)+1));    
            try {
                ClsProducto oProducto = new ClsProducto();

                rs = oProducto.verificarCodigoBar(codbar);
                while (rs.next()) {
                    if (!rs.getString(2).equals("")) {

                        sen = 1;
                    } else {

                        sen = 2;
                    }
                    break;
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
                System.out.println(ex.getMessage());
            }

            if (sen == 1) {
                //JOptionPane.showMessageDialog(null, "Codigo No Disponible");
                i = i - 1;
                sen = 2;
            } else if (sen == 2) {
                //JOptionPane.showMessageDialog(null, "Codigo Disponible");
                modelo.addElement(codbar);
                //sen=2;

            }

        }

        lstCodigos.setModel(modelo);
    }

    void limpiarList() {
        DefaultListModel model = new DefaultListModel();
        lstCodigos.setModel(model);
    }


    private void btnGenerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarActionPerformed
        String codigobar;
        ImageIcon imagen = null;
        codigobar = txtCodigoBar.getText();
        File file = new File("C:\\001.jpg");

        Barcode barcode = null;
        try {
            if (cboTipoCodificacion.getSelectedItem().equals("Code 128")) {
                barcode = BarcodeFactory.createCode128(codigobar);
            } else if (cboTipoCodificacion.getSelectedItem().equals("Code 128A")) {
                barcode = BarcodeFactory.createCode128A(codigobar);
            } else if (cboTipoCodificacion.getSelectedItem().equals("Code 128B")) {
                barcode = BarcodeFactory.createCode128B(codigobar);
            }

            barcode.setBarHeight(60);
            barcode.setBarWidth(1);
            barcode.setDrawingText(false);

            BarcodeImageHandler.saveJPEG(barcode, file);

        } catch (BarcodeException e) {
            e.printStackTrace();
        } catch (OutputException e) {
            e.printStackTrace();
        }
        imagen = new ImageIcon("C:\\001.jpg");
        imagen.getImage().flush();
        lblprueba.setIcon(imagen);

    }//GEN-LAST:event_btnGenerarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        GeneraAleatorio();


    }//GEN-LAST:event_btnActualizarActionPerformed

    private void lstCodigosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstCodigosMouseClicked
        //int seleccion = lstCodigos.getSelectedIndex();
        //txtCodigoBar.setText(String.valueOf(seleccion));
        txtCodigoBar.setText(lstCodigos.getSelectedValue().toString());

    }//GEN-LAST:event_lstCodigosMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Map p = new HashMap();
        p.put("busqueda", txtBusqueda.getText());
        if (rbtnCodigo.isSelected()) {
            p.put("criterio", "codigo");
        } else if (rbtnNombre.isSelected()) {
            p.put("criterio", "nombre");
        } else if (rbtnDescripcion.isSelected()) {
            p.put("criterio", "descripcion");
        } else if (rbtnCategoria.isSelected()) {
            p.put("criterio", "categoria");
        } else {
            p.put("criterio", "");
        }
        JasperReport report;
        JasperPrint print;
        try {
            report = JasperCompileManager.compileReport(new File("").getAbsolutePath() + "/src/Reportes/RptProducto.jrxml");
            print = JasperFillManager.fillReport(report, p, connection);
            JasperViewer view = new JasperViewer(print, false);
            view.setTitle("Reporte General de Productos");
            view.setVisible(true);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped
        txtNombre.setBackground(Color.WHITE);
    }//GEN-LAST:event_txtNombreKeyTyped

    private void txtStockKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStockKeyTyped
        txtStock.setBackground(Color.WHITE);
    }//GEN-LAST:event_txtStockKeyTyped

    private void txtPrecioVentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioVentaKeyTyped
        txtPrecioVenta.setBackground(Color.WHITE);
    }//GEN-LAST:event_txtPrecioVentaKeyTyped

    private void txtCodigoBarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoBarKeyReleased
        int keyCode = evt.getKeyCode();
        if (keyCode == KeyEvent.VK_ENTER)
            txtNombre.requestFocus();
    }//GEN-LAST:event_txtCodigoBarKeyReleased

    private void txtNombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyReleased
        int keyCode = evt.getKeyCode();
        if (keyCode == KeyEvent.VK_ENTER)
            txtDescripcion.requestFocus();
    }//GEN-LAST:event_txtNombreKeyReleased

    private void txtStockKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStockKeyReleased
        int keyCode = evt.getKeyCode();
        if (keyCode == KeyEvent.VK_ENTER)
            txtStockMin.requestFocus();
    }//GEN-LAST:event_txtStockKeyReleased

    private void txtStockMinKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStockMinKeyReleased
        int keyCode = evt.getKeyCode();
        if (keyCode == KeyEvent.VK_ENTER)
            txtPrecioCosto.requestFocus();
    }//GEN-LAST:event_txtStockMinKeyReleased

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        Map p = new HashMap();
        String cod = txtCodigoBar.getText();
        p.put("codigo", cod);

        JasperReport report;
        JasperPrint print;
        if (cboTipoCodificacion.getSelectedItem().equals("Code 128")) {
            try {
                report = JasperCompileManager.compileReport(new File("").getAbsolutePath() + "/src/Reportes/RptProducto_Code128.jrxml");
                print = JasperFillManager.fillReport(report, p, new JREmptyDataSource());
                JasperViewer view = new JasperViewer(print, false);
                view.setTitle("Código de Barras - CODE128");
                view.setVisible(true);
            } catch (JRException e) {
                e.printStackTrace();
            }
        } else if (cboTipoCodificacion.getSelectedItem().equals("Code 128A")) {
            try {
                report = JasperCompileManager.compileReport(new File("").getAbsolutePath() + "/src/Reportes/RptProducto_Code128A.jrxml");
                print = JasperFillManager.fillReport(report, p, new JREmptyDataSource());
                JasperViewer view = new JasperViewer(print, false);
                view.setTitle("Código de Barras - CODE128A");
                view.setVisible(true);
            } catch (JRException e) {
                e.printStackTrace();
            }
        } else if (cboTipoCodificacion.getSelectedItem().equals("Code 128B")) {
            try {
                report = JasperCompileManager.compileReport(new File("").getAbsolutePath() + "/src/Reportes/RptProducto_Code128B.jrxml");
                print = JasperFillManager.fillReport(report, p, new JREmptyDataSource());
                JasperViewer view = new JasperViewer(print, false);
                view.setTitle("Código de Barras - CODE128B");
                view.setVisible(true);
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void btnSalir1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalir1ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnSalir1ActionPerformed

    private void cboTipoCodificacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTipoCodificacionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboTipoCodificacionActionPerformed

    private void duplicateItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_duplicateItemActionPerformed

        if (tblProducto.getSelectedRows().length > 0) {
            accion = "Nuevo";
            loadProduct();
            modificar();
            txtId.setText("");
            txtCodigoBar.setText("");
            tabProducto.setSelectedIndex(tabProducto.indexOfComponent(pNuevo));
        } else {
            JOptionPane.showMessageDialog(null, "¡Se debe seleccionar un registro!");
        }
    }//GEN-LAST:event_duplicateItemActionPerformed

    private void txtIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdActionPerformed

    private void btnRevertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRevertActionPerformed
        if (productDto != null) {
            if (productDto.getImageIcon() != null) {
                Icon icon = new ImageIcon(productDto.getImageIcon().getImage().getScaledInstance(
                    jLabelPhoto.getWidth(),
                    jLabelPhoto.getHeight(),
                    java.awt.Image.SCALE_FAST)
            );
            jLabelPhoto.setText(null);
            jLabelPhoto.setIcon(icon);
            //                jLabelPhoto.repaint();
            } else {
                jLabelPhoto.setIcon(null);
                jLabelPhoto.setText("FOTO");
            }
        }
    }//GEN-LAST:event_btnRevertActionPerformed

    private void btnSelectPhotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectPhotoActionPerformed
        imageSelector = new ImageSelector(jLabelPhoto);
        imageSelector.select();
        if (imageSelector.getImageIcon() != null)
        if (imageSelector.getByteLength() <= 1000000) {
            jLabelPhoto.setText(null);
            jLabelPhoto.setIcon(imageSelector.getImageIcon());
            product.setImagen(new Image(imageSelector.getFileInputStream(), imageSelector.getByteLength()));
        } else {
            Toast.makeText(Toast.WARNING, "Imagen demasiado grande", Toast.LENGTH_LONG).show();
        }
    }//GEN-LAST:event_btnSelectPhotoActionPerformed

    private void btnRemovePhotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemovePhotoActionPerformed
        product.setImagen(new Image(null, 0));
        jLabelPhoto.setIcon(null);
        jLabelPhoto.setText("FOTO");
    }//GEN-LAST:event_btnRemovePhotoActionPerformed

    private void tabProductoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabProductoStateChanged
        if (tabProducto.getSelectedIndex() == 1) {
            if (tblProducto.getSelectedRows().length > 0) {
                int fila;
                DefaultTableModel defaultTableModel = new DefaultTableModel();
                fila = tblProducto.getSelectedRow();
                product = new ClsEntidadProducto();
                defaultTableModel = (DefaultTableModel) tblProducto.getModel();
                ClsProducto productService = new ClsProducto();
                productDto = productService.findById(
                        Integer.parseInt(
                                (String) defaultTableModel.getValueAt(fila, 0)
                        )
                );
                loadForm(productDto);
            }
        }
    }//GEN-LAST:event_tabProductoStateChanged
    private void loadForm(ProductDto productDto) {
        if (productDto != null) {
            strCodigo = String.valueOf(productDto.getId());
            txtId.setText(String.valueOf(productDto.getId()));
            txtCodigoBar.setText(productDto.getCode());
            txtNombre.setText(productDto.getName());
            txtDescripcion.setText(productDto.getDescription());
            txtStock.setText(productDto.getStock());
            txtStockMin.setText(productDto.getMinStock());
            txtPrecioCosto.setText(productDto.getCoste());
            txtPrecioVenta.setText(productDto.getPrice());
            txtUtilidad.setText(productDto.getUtility());
            if ("ACTIVO".equalsIgnoreCase(productDto.getState())) {
                rbtnActivo.setSelected(true);
            } else if ("INACTIVO".equalsIgnoreCase( productDto.getState() )) {
                rbtnInactivo.setSelected(true);
            }
            cboCategoria.setSelectedItem(productDto.getCategory());
            if (productDto.getImageIcon() != null) {
                Icon icon = new ImageIcon(productDto.getImageIcon().getImage().getScaledInstance(
                        jLabelPhoto.getWidth(),
                        jLabelPhoto.getHeight(),
                        java.awt.Image.SCALE_FAST)
                );
                jLabelPhoto.setText(null);
                jLabelPhoto.setIcon(icon);
            } else {
                jLabelPhoto.setText("FOTO");
                jLabelPhoto.setIcon(null);
            }
            try {
                dcFechaVencimiento.setDate(productDto.getExpiration());
            } catch (Exception e) {
                Message.LOGGER.log(Level.SEVERE, e.getMessage());
            }
        } else {
            mirar();
            Toast.makeText(Toast.WARNING, "El producto no se encuentra en la base de datos", Toast.LENGTH_SHORT).show();
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGenerar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnRemovePhoto;
    private javax.swing.JButton btnRevert;
    private javax.swing.JButton btnSalir1;
    private javax.swing.JButton btnSelectPhoto;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox cboCategoria;
    private javax.swing.JComboBox cboTipoCodificacion;
    private com.toedter.calendar.JDateChooser dcFechaVencimiento;
    private javax.swing.JMenuItem duplicateItem;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelPhoto;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu jPopupMenu;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblprueba;
    private javax.swing.JList lstCodigos;
    private javax.swing.JPanel pBuscar;
    private javax.swing.JPanel pNuevo;
    private javax.swing.JPanel panel;
    private javax.swing.JRadioButton rbtnActivo;
    private javax.swing.JRadioButton rbtnCategoria;
    private javax.swing.JRadioButton rbtnCodigo;
    private javax.swing.JRadioButton rbtnDescripcion;
    private javax.swing.JRadioButton rbtnInactivo;
    private javax.swing.JRadioButton rbtnNombre;
    private javax.swing.JTabbedPane tabProducto;
    private javax.swing.JTable tblProducto;
    private javax.swing.JTextField txtBusqueda;
    private javax.swing.JTextField txtCodigoBar;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecioCosto;
    private javax.swing.JTextField txtPrecioVenta;
    private javax.swing.JTextField txtStock;
    private javax.swing.JTextField txtStockMin;
    private javax.swing.JTextField txtUtilidad;
    // End of variables declaration//GEN-END:variables
}
