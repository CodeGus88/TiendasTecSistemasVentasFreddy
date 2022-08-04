/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Presentacion;

import Consultas.ImageSelector;
import Conexion.ClsConexion;
import Entidad.*;
import Entidad.dtos.ClientDto;
import Negocio.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import tools.toast.Toast;

public class FrmCliente extends javax.swing.JInternalFrame {

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
    DefaultTableModel dtm;
    String criterio, busqueda;

    private ImageSelector imageSelector;

//    private String titulos[] = {"ID", "NOMBRE", "TEFÉFONO 2", "CI", "DIRECCIÓN", "TELÉFONO", "OBSERVACIÓN"};
    private String titulos[] = {"ID", "NOMBRE", "CI", "DIRECCIÓN", "TELÉFONO", "OBSERVACIÓN"};
//    private int[] anchos = {50, 200, 80, 80, 150, 80, 200};
    private int[] anchos = {50, 280, 80, 150, 80, 200};
    private ClsEntidadCliente client;
    private ClientDto clientDto;

    public FrmCliente() {
        initComponents();

        tabCliente.setIconAt(tabCliente.indexOfComponent(pBuscar), new ImageIcon("src/iconos/busca_p1.png"));
        tabCliente.setIconAt(tabCliente.indexOfComponent(pNuevo), new ImageIcon("src/iconos/nuevo1.png"));
        buttonGroup1.add(rbtnCodigo);
        buttonGroup1.add(rbtnNombre);
        buttonGroup1.add(rbtnDni);

        mirar();
        this.setSize(966, 412);
        actualizarTabla();
        CrearTabla();
        CantidadTotal();

    }

    void CrearTabla() {
        TableCellRenderer render = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                //aqui obtengo el render de la calse superior 
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                //Determinar Alineaciones   
                if (column == 0 || column == 2 || column == 3 || column == 5) {
                    l.setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    l.setHorizontalAlignment(SwingConstants.LEFT);
                }

                //Colores en Jtable        
                if (isSelected) {
                    l.setBackground(new Color(203, 159, 41));
                    l.setForeground(Color.WHITE);
                } else {
                    l.setForeground(Color.BLACK);
                    if (row % 2 == 0) {
                        l.setBackground(Color.WHITE);
                    } else {
                        l.setBackground(new Color(254, 227, 152));
                    }
                }
                return l;
            }
        };

        //Agregar Render
        for (int i = 0; i < tblCliente.getColumnCount(); i++) {
            tblCliente.getColumnModel().getColumn(i).setCellRenderer(render);
        }

        //Activar ScrollBar
        tblCliente.setAutoResizeMode(tblCliente.AUTO_RESIZE_OFF);

        //Anchos de cada columna
        for (int i = 0; i < tblCliente.getColumnCount(); i++) {
            tblCliente.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
    }

    void CantidadTotal() {
        Total = String.valueOf(tblCliente.getRowCount());
        lblEstado.setText("Se cargaron " + Total + " registros");
    }

    void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtDni.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtObservacion.setText("");

        rbtnCodigo.setSelected(false);
        rbtnNombre.setSelected(false);
        rbtnDni.setSelected(false);
        txtBusqueda.setText("");
    }

    void mirar() {
        tblCliente.setEnabled(true);
        btnNuevo.setEnabled(true);
        btnModificar.setEnabled(true);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnSalir.setEnabled(true);

        txtNombre.setEnabled(false);
        txtDni.setEnabled(false);
        txtDireccion.setEnabled(false);
        txtTelefono.setEnabled(false);
        txtObservacion.setEnabled(false);

        btnSelectPhoto.setEnabled(false);
        btnRemovePhoto.setEnabled(false);
        btnSelectPhoto.setVisible(false);
        btnRemovePhoto.setVisible(false);
        jLabelPhoto.setIcon(null);
        
        btnRevert.setVisible(false);
        btnSelectPhoto.setVisible(false);
        btnRemovePhoto.setVisible(false);

        jLabelPhoto.setIcon(null);

    }

    void modificar() {
        tblCliente.setEnabled(false);
        btnNuevo.setEnabled(false);
        btnModificar.setEnabled(false);
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnSalir.setEnabled(false);

        txtNombre.setEnabled(true);
        txtDni.setEnabled(true);
        txtDireccion.setEnabled(true);
        txtTelefono.setEnabled(true);
        txtObservacion.setEnabled(true);
        txtNombre.requestFocus();

        btnSelectPhoto.setEnabled(true);
        btnRemovePhoto.setEnabled(true);
        btnSelectPhoto.setVisible(true);
        btnRemovePhoto.setVisible(true);
    }

    void actualizarTabla() {
        ClsCliente clientes = new ClsCliente();
        ArrayList<ClsEntidadCliente> cliente = clientes.listarCliente();
        Iterator iterator = cliente.iterator();
        dtm = new DefaultTableModel(null, titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        String fila[] = new String[6];
        while (iterator.hasNext()) {
            ClsEntidadCliente Cliente = new ClsEntidadCliente();
            Cliente = (ClsEntidadCliente) iterator.next();
            fila[0] = Cliente.getStrIdCliente();
            fila[1] = Cliente.getStrNombreCliente();
            fila[2] = Cliente.getStrDniCliente();
            fila[3] = Cliente.getStrDireccionCliente();
            fila[4] = Cliente.getStrTelefonoCliente();
            fila[5] = Cliente.getStrObsvCliente();
            dtm.addRow(fila);
        }
        tblCliente.setModel(dtm);
    }

    void BuscarCliente() {

        ClsCliente categoria = new ClsCliente();
        busqueda = txtBusqueda.getText();
        if (rbtnCodigo.isSelected()) {
            criterio = "id";
        } else if (rbtnNombre.isSelected()) {
            criterio = "nombre";
        } else if (rbtnDni.isSelected()) {
            criterio = "dni";
        }
        try {
            rs = categoria.listarClientePorParametro(criterio, busqueda);
            boolean encuentra = false;
            String Datos[] = new String[6];
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
                Datos[2] = (String) rs.getString(4);
                Datos[3] = (String) rs.getString(5);
                Datos[4] = (String) rs.getString(6);
                Datos[5] = (String) rs.getString(7);
                dtm.addRow(Datos);
                encuentra = true;
            }
            if (encuentra = false) {
                JOptionPane.showMessageDialog(null, "¡No se encuentra!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        tblCliente.setModel(dtm);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        lblEstado = new javax.swing.JLabel();
        btnModificar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        tabCliente = new javax.swing.JTabbedPane();
        pBuscar = new javax.swing.JPanel();
        txtBusqueda = new javax.swing.JTextField();
        rbtnCodigo = new javax.swing.JRadioButton();
        rbtnNombre = new javax.swing.JRadioButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCliente = new javax.swing.JTable();
        rbtnDni = new javax.swing.JRadioButton();
        jLabel10 = new javax.swing.JLabel();
        pNuevo = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnSelectPhoto = new javax.swing.JButton();
        btnRemovePhoto = new javax.swing.JButton();
        jLabelPhoto = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtDni = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtObservacion = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        btnRevert = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Garzones");
        getContentPane().setLayout(null);

        lblEstado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(lblEstado);
        lblEstado.setBounds(10, 420, 760, 20);

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
        btnModificar.setBounds(770, 170, 180, 70);

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
        btnCancelar.setBounds(770, 240, 180, 70);

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
        btnNuevo.setBounds(770, 30, 180, 70);

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/principal.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSalir.setIconTextGap(0);
        btnSalir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        getContentPane().add(btnSalir);
        btnSalir.setBounds(770, 310, 180, 70);

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
        btnGuardar.setBounds(770, 100, 180, 70);

        jLabel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Mantenimiento"));
        getContentPane().add(jLabel1);
        jLabel1.setBounds(770, 10, 180, 370);

        tabCliente.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabClienteStateChanged(evt);
            }
        });

        pBuscar.setBackground(new java.awt.Color(255, 255, 255));
        pBuscar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        pBuscar.add(txtBusqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 360, -1));

        rbtnCodigo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbtnCodigo.setText("ID");
        rbtnCodigo.setOpaque(false);
        rbtnCodigo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbtnCodigoStateChanged(evt);
            }
        });
        rbtnCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnCodigoActionPerformed(evt);
            }
        });
        pBuscar.add(rbtnCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 80, -1));

        rbtnNombre.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbtnNombre.setText("Nombre");
        rbtnNombre.setOpaque(false);
        rbtnNombre.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbtnNombreStateChanged(evt);
            }
        });
        pBuscar.add(rbtnNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, 100, -1));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/report.png"))); // NOI18N
        jButton3.setText("Reporte");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        pBuscar.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 30, 220, 50));

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
        jScrollPane1.setViewportView(tblCliente);

        pBuscar.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 740, 240));

        rbtnDni.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rbtnDni.setText("CI");
        rbtnDni.setOpaque(false);
        pBuscar.add(rbtnDni, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 30, 150, -1));

        jLabel10.setBackground(new java.awt.Color(255, 153, 0));
        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Criterios de Búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jLabel10.setOpaque(true);
        pBuscar.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 740, 90));

        tabCliente.addTab("Buscar", pBuscar);

        pNuevo.setBackground(new java.awt.Color(255, 255, 255));
        pNuevo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        pNuevo.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(513, 250, 220, 40));

        jLabelPhoto.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabelPhoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelPhoto.setText("FOTO");
        jLabelPhoto.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelPhoto.setMaximumSize(new java.awt.Dimension(5000, 5000));
        jLabelPhoto.setMinimumSize(new java.awt.Dimension(300, 300));
        jLabelPhoto.setPreferredSize(new java.awt.Dimension(300, 300));
        pNuevo.add(jLabelPhoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(513, 55, 220, 190));

        txtCodigo.setEnabled(false);
        txtCodigo.setMaximumSize(new java.awt.Dimension(10000, 30));
        txtCodigo.setMinimumSize(new java.awt.Dimension(0, 30));
        txtCodigo.setPreferredSize(new java.awt.Dimension(0, 30));
        pNuevo.add(txtCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, 100, -1));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("ID Cliente:");
        jLabel3.setMaximumSize(new java.awt.Dimension(10000, 30));
        jLabel3.setMinimumSize(new java.awt.Dimension(0, 30));
        jLabel3.setPreferredSize(new java.awt.Dimension(0, 30));
        pNuevo.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 120, 30));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Nombre Garzon:");
        jLabel2.setMaximumSize(new java.awt.Dimension(10000, 30));
        jLabel2.setMinimumSize(new java.awt.Dimension(0, 30));
        jLabel2.setPreferredSize(new java.awt.Dimension(0, 30));
        pNuevo.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 130, 30));

        txtNombre.setMaximumSize(new java.awt.Dimension(10000, 30));
        txtNombre.setMinimumSize(new java.awt.Dimension(0, 30));
        txtNombre.setPreferredSize(new java.awt.Dimension(0, 30));
        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNombreKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreKeyTyped(evt);
            }
        });
        pNuevo.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, 350, -1));

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Dirección:");
        jLabel6.setMaximumSize(new java.awt.Dimension(10000, 30));
        jLabel6.setMinimumSize(new java.awt.Dimension(0, 30));
        jLabel6.setPreferredSize(new java.awt.Dimension(0, 30));
        pNuevo.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 120, 30));

        txtDireccion.setMaximumSize(new java.awt.Dimension(10000, 30));
        txtDireccion.setMinimumSize(new java.awt.Dimension(0, 30));
        txtDireccion.setPreferredSize(new java.awt.Dimension(0, 30));
        txtDireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDireccionKeyReleased(evt);
            }
        });
        pNuevo.add(txtDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 160, 350, -1));

        jLabel7.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Teléfono:");
        jLabel7.setMaximumSize(new java.awt.Dimension(10000, 30));
        jLabel7.setMinimumSize(new java.awt.Dimension(0, 30));
        jLabel7.setPreferredSize(new java.awt.Dimension(0, 30));
        pNuevo.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 120, 30));

        txtTelefono.setMaximumSize(new java.awt.Dimension(10000, 30));
        txtTelefono.setMinimumSize(new java.awt.Dimension(0, 30));
        txtTelefono.setPreferredSize(new java.awt.Dimension(0, 30));
        txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTelefonoKeyReleased(evt);
            }
        });
        pNuevo.add(txtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 200, 160, -1));

        jLabel8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("CI:");
        jLabel8.setMaximumSize(new java.awt.Dimension(10000, 30));
        jLabel8.setMinimumSize(new java.awt.Dimension(0, 30));
        jLabel8.setPreferredSize(new java.awt.Dimension(0, 30));
        pNuevo.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 130, 30));

        txtDni.setMaximumSize(new java.awt.Dimension(10000, 30));
        txtDni.setMinimumSize(new java.awt.Dimension(0, 30));
        txtDni.setPreferredSize(new java.awt.Dimension(0, 30));
        txtDni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDniKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDniKeyTyped(evt);
            }
        });
        pNuevo.add(txtDni, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 120, 160, -1));

        jLabel9.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Observación:");
        pNuevo.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 120, 30));

        txtObservacion.setColumns(20);
        txtObservacion.setRows(5);
        jScrollPane2.setViewportView(txtObservacion);

        pNuevo.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 240, 350, 50));

        jLabel4.setForeground(new java.awt.Color(0, 51, 153));
        jLabel4.setText("Los campos marcado con un asterísco (*) son obligatorios, tamaño máximo de la foto 1 Mgb");
        pNuevo.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 315, 740, 40));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 51, 153));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("*");
        pNuevo.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(488, 70, 20, 20));

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
        pNuevo.add(btnRevert, new org.netbeans.lib.awtextra.AbsoluteConstraints(513, 20, 220, 30));

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos del garzón", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        pNuevo.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 740, 310));

        tabCliente.addTab("Nuevo / Modificar", pNuevo);

        getContentPane().add(tabCliente);
        tabCliente.setBounds(10, 10, 760, 400);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClienteMouseClicked
    }//GEN-LAST:event_tblClienteMouseClicked

    private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped
        txtNombre.setBackground(Color.WHITE);
    }//GEN-LAST:event_txtNombreKeyTyped

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        if (tblCliente.getSelectedRows().length > 0) {
            tabCliente.setSelectedIndex(tabCliente.indexOfComponent(pNuevo));
            accion = "Modificar";
            btnRevert.setVisible(true);
            modificar();
        } else {
            JOptionPane.showMessageDialog(null, "¡Se debe seleccionar un registro!");
        }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void loadForm(ClientDto clientDto) {
        if (clientDto != null) {
            strCodigo = String.valueOf(clientDto.getId());
            txtCodigo.setText(String.valueOf(clientDto.getId()));
            txtNombre.setText(clientDto.getName());
            txtDni.setText(clientDto.getCi());
            txtDireccion.setText(clientDto.getAddress());
            txtTelefono.setText(clientDto.getPhone());
            txtObservacion.setText(clientDto.getObservation());
            if (clientDto.getImageIcon() != null) {
                Icon icon = new ImageIcon(clientDto.getImageIcon().getImage().getScaledInstance(
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
        } else {
            mirar();
            Toast.makeText(Toast.WARNING, "El usuario no se encuentra en la base de datos", Toast.LENGTH_SHORT).show();
        }
    }

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        mirar();
        tabCliente.setSelectedIndex(tabCliente.indexOfComponent(pBuscar));
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        client = new ClsEntidadCliente();
        accion = "Nuevo";
        modificar();
        limpiarCampos();
        tblCliente.setEnabled(false);
        tabCliente.setSelectedIndex(tabCliente.indexOfComponent(pNuevo));
        btnRevert.setVisible(false);
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed
    public boolean validardatos() {
        if (txtNombre.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Ingrese el nombre del garzón");
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
                ClsCliente clientes = new ClsCliente();
                client.setStrNombreCliente(txtNombre.getText());
                client.setStrDniCliente(txtDni.getText());
                client.setStrDireccionCliente(txtDireccion.getText());
                client.setStrTelefonoCliente(txtTelefono.getText());
                client.setStrObsvCliente(txtObservacion.getText());
                if (client.getImagen() == null) {
                    client.setImagen(new Image(null, 0));
                }
                clientes.agregarCliente(client);
                actualizarTabla();
                CantidadTotal();
            }
            if (accion.equals("Modificar")) {
                ClsCliente clientes = new ClsCliente();
                client.setStrNombreCliente(txtNombre.getText());
                client.setStrDniCliente(txtDni.getText());
                client.setStrDireccionCliente(txtDireccion.getText());
                client.setStrTelefonoCliente(txtTelefono.getText());
                client.setStrObsvCliente(txtObservacion.getText());
                clientes.modificarCliente(strCodigo, client);
                actualizarTabla();
                limpiarCampos();
                modificar();
                CantidadTotal();
            }
            CrearTabla();
            mirar();
            tabCliente.setSelectedIndex(tabCliente.indexOfComponent(pBuscar));
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void txtBusquedaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaKeyReleased
        BuscarCliente();
        CrearTabla();
        CantidadTotal();
    }//GEN-LAST:event_txtBusquedaKeyReleased

    private void rbtnCodigoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rbtnCodigoStateChanged
        txtBusqueda.setText("");
    }//GEN-LAST:event_rbtnCodigoStateChanged

    private void rbtnNombreStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rbtnNombreStateChanged
        txtBusqueda.setText("");
    }//GEN-LAST:event_rbtnNombreStateChanged

    private void txtNombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyReleased
        String cadena = (txtNombre.getText()).toUpperCase();
        txtNombre.setText(cadena);
    }//GEN-LAST:event_txtNombreKeyReleased

    private void txtDniKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDniKeyTyped
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9')) {
            evt.consume();
        }
        int i = txtDni.getText().length();
        if (txtDni.getText().trim().length() < 8) {
        } else {
            i = 10;
            String com = txtDni.getText().substring(0, 7);
            txtDni.setText("");
            txtDni.setText(com);
        }
    }//GEN-LAST:event_txtDniKeyTyped

    private void txtDniKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDniKeyReleased
        int keyCode = evt.getKeyCode();
        if (keyCode == KeyEvent.VK_ENTER)
            txtDireccion.requestFocus();
    }//GEN-LAST:event_txtDniKeyReleased

    private void txtDireccionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionKeyReleased
        int keyCode = evt.getKeyCode();
        if (keyCode == KeyEvent.VK_ENTER)
            txtTelefono.requestFocus();
    }//GEN-LAST:event_txtDireccionKeyReleased

    private void txtTelefonoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoKeyReleased
        int keyCode = evt.getKeyCode();
        if (keyCode == KeyEvent.VK_ENTER)
            btnGuardar.requestFocus();
    }//GEN-LAST:event_txtTelefonoKeyReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Map p = new HashMap();
        p.put("busqueda", txtBusqueda.getText());
        if (rbtnCodigo.isSelected()) {
            p.put("criterio", "id");
        } else if (rbtnNombre.isSelected()) {
            p.put("criterio", "nombre");
        } else if (rbtnDni.isSelected()) {
            p.put("criterio", "dni");
        } else {
            p.put("criterio", "");
        }
        JasperReport report;
        JasperPrint print;
        try {
            report = JasperCompileManager.compileReport(new File("").getAbsolutePath() + "/src/Reportes/RptCliente.jrxml");
            print = JasperFillManager.fillReport(report, p, connection);
            JasperViewer view = new JasperViewer(print, false);
            view.setTitle("Reporte de Garzones");
            view.setVisible(true);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusquedaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBusquedaActionPerformed

    private void btnSelectPhotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectPhotoActionPerformed
        imageSelector = new ImageSelector(jLabelPhoto);
        imageSelector.select();
        if (imageSelector.getImageIcon() != null)
            if (imageSelector.getByteLength() <= 1000000) {
                jLabelPhoto.setText(null);
                jLabelPhoto.setIcon(imageSelector.getImageIcon());
                client.setImagen(new Image(imageSelector.getFileInputStream(), imageSelector.getByteLength()));
            } else {
                Toast.makeText(Toast.WARNING, "Imagen demasiado grande", Toast.LENGTH_LONG).show();
            }
    }//GEN-LAST:event_btnSelectPhotoActionPerformed

    private void rbtnCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnCodigoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtnCodigoActionPerformed

    private void btnRemovePhotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemovePhotoActionPerformed
        client.setImagen(new Image(null, 0));
        jLabelPhoto.setIcon(null);
        jLabelPhoto.setText("FOTO");
    }//GEN-LAST:event_btnRemovePhotoActionPerformed

    private void btnRevertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRevertActionPerformed
        if (clientDto != null) {
            if (clientDto.getImageIcon() != null) {
                Icon icon = new ImageIcon(clientDto.getImageIcon().getImage().getScaledInstance(
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

    private void tabClienteStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabClienteStateChanged
        if (tabCliente.getSelectedIndex() == 1) {
            tabCliente.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            if (tblCliente.getSelectedRows().length > 0) {
                int fila;
                DefaultTableModel defaultTableModel = new DefaultTableModel();
                fila = tblCliente.getSelectedRow();
                client = new ClsEntidadCliente();
                ClsCliente clientService = new ClsCliente();
                defaultTableModel = (DefaultTableModel) tblCliente.getModel();
                clientDto = clientService.findById(
                        Integer.parseInt(
                                (String) defaultTableModel.getValueAt(fila, 0)
                        )
                );
                loadForm(clientDto);
            }
            tabCliente.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_tabClienteStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnRemovePhoto;
    private javax.swing.JButton btnRevert;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnSelectPhoto;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelPhoto;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JPanel pBuscar;
    private javax.swing.JPanel pNuevo;
    private javax.swing.JRadioButton rbtnCodigo;
    private javax.swing.JRadioButton rbtnDni;
    private javax.swing.JRadioButton rbtnNombre;
    private javax.swing.JTabbedPane tabCliente;
    private javax.swing.JTable tblCliente;
    private javax.swing.JTextField txtBusqueda;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtDni;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextArea txtObservacion;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
