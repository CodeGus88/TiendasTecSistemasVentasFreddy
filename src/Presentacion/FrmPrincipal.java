/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Presentacion;

import Consultas.FrmConsultaStock;
import Consultas.FrmKardexValorizado;
import Negocio.*;
import interfaces.FrameState;
import java.awt.Desktop;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.util.Date;
import java.text.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import statics.Design;
import statics.Message;
import tools.ObjectDeserializer;
import tools.ObjectSerializer;
import statics.Paths;

public class FrmPrincipal extends javax.swing.JFrame implements FrameState {

    public int intEstado;
    public String strUsuario;
    public String strIdEmpleado;
    public String strNombreEmpleado;
    public String strTipo;
    public String idAcceso;
    static ResultSet rs = null;
//   private boolean mFullScreen = false;
    //---------------------Privilegios--------------------
    String p_venta, p_compra, p_producto, p_proveedor, p_empleado;
    String p_cliente, p_categoria, p_tipodoc, p_tipouser, p_anularv, p_anularc;
    String p_estadoprod, p_ventare, p_ventade, p_estadistica, p_comprare, p_comprade, p_pass, p_respaldar, p_restaurar, p_caja;

// Ventanas
    private Presentacion.FrmVenta venta;

    // Error printer
    private Logger logger;

    public FrmPrincipal() {
        logger = Logger.getLogger(Message.PRINCIPAL_FRAME_ERROR);
        initComponents();
        JMIniciarSesion.setEnabled(false);

        Date date = new Date();
        String format = new String("dd/MM/yyyy");
        SimpleDateFormat formato = new SimpleDateFormat(format);
        lblFecha.setText(formato.format(date));
        //---------------------ANCHO Y ALTO DEL FORM----------------------
        this.setExtendedState(MAXIMIZED_BOTH);
        //TODA LA PANTALLA 
        lblIdEmpleado.setVisible(false);
        lblUsuarioEmpleado.setVisible(false);
        lblEstado.setVisible(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        readFrameRectanble();
        
        design();
    }
    
    private void design(){
        this.setBackground(Design.COLOR_PRIMARY_DARK); // no funciona con themas
        TBPrincipal.setBackground(Design.COLOR_PRIMARY);
        Escritorio.setBackground(Design.COLOR_SECONDARY);
    }

    void BuscarPermisos() {
        String usuario = null;
        String tipo = null;
        int sen = 1;
        int tu = 1;
        usuario = strUsuario;
        tipo = strTipo;
        boolean encuentra = false;

        try {
            ClsTipoUsuario oTipoUsuario = new ClsTipoUsuario();

            rs = oTipoUsuario.consultarLoginPermisos(usuario, tipo);
            while (rs.next() && sen == 1) {
                if (rs.getString(2).equals(strUsuario) && rs.getString(3).equals(strTipo)) {
                    p_venta = rs.getString(4);
                    p_compra = rs.getString(5);
                    p_producto = rs.getString(6);
                    p_proveedor = rs.getString(7);
                    p_empleado = rs.getString(8);
                    p_cliente = rs.getString(9);
                    p_categoria = rs.getString(10);
                    p_tipodoc = rs.getString(11);
                    p_tipouser = rs.getString(12);
                    p_anularv = rs.getString(13);
                    p_anularc = rs.getString(14);
                    p_estadoprod = rs.getString(15);
                    p_ventare = rs.getString(16);
                    p_ventade = rs.getString(17);
                    p_estadistica = rs.getString(18);
                    p_comprare = rs.getString(19);
                    p_comprade = rs.getString(20);
                    p_pass = rs.getString(21);
                    p_respaldar = rs.getString(22);
                    p_restaurar = rs.getString(23);
                    p_caja = rs.getString(24);
                    sen = 2;
                }
                encuentra = true;
                break;
            }
            if (sen == 1) {
                JOptionPane.showMessageDialog(this, "¡Código de Asistente no Registrado!");

            } else {

            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            System.out.println(ex.getMessage());
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTipo = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        lblUsuarioEmpleado = new javax.swing.JLabel();
        lblEstado = new javax.swing.JLabel();
        lblNombreEmpleado = new javax.swing.JLabel();
        lblIdEmpleado = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        TBPrincipal = new javax.swing.JToolBar();
        mbtnEmpleado = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        mbtnCliente = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        mbtnProducto = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        mbtnCompra = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        mbtnVenta = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        mbtnCaja = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        mbtnEstado = new javax.swing.JButton();
        Escritorio = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mProducto = new javax.swing.JMenuItem();
        mCategoria = new javax.swing.JMenuItem();
        mnuRegistro = new javax.swing.JMenu();
        mCompra = new javax.swing.JMenuItem();
        mProveedor = new javax.swing.JMenuItem();
        mnuOperaciones = new javax.swing.JMenu();
        mVenta = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        mEstado = new javax.swing.JMenuItem();
        mCliente = new javax.swing.JMenuItem();
        mnuInformes = new javax.swing.JMenu();
        mVentare = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        mVentade = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        mEstadistica = new javax.swing.JMenuItem();
        mComprare = new javax.swing.JMenuItem();
        mComprade = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        mnuHerramientas = new javax.swing.JMenu();
        mCambiarpass = new javax.swing.JMenuItem();
        mCalculadora = new javax.swing.JMenuItem();
        mRespaldar = new javax.swing.JMenuItem();
        mRestaurar = new javax.swing.JMenuItem();
        mnuAnulaciones = new javax.swing.JMenu();
        mAnularv = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        mAnularc = new javax.swing.JMenuItem();
        mnuMantenimiento = new javax.swing.JMenu();
        mEmpleado = new javax.swing.JMenuItem();
        mTipodoc = new javax.swing.JMenuItem();
        mTipouser = new javax.swing.JMenuItem();
        mnuAyuda = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        mnuArchivo = new javax.swing.JMenu();
        JMIniciarSesion = new javax.swing.JMenuItem();
        JMCerrarSesion = new javax.swing.JMenuItem();
        JMSalir = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Punto de Venta TiendasTec");
        setBackground(new java.awt.Color(51, 255, 102));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        lblTipo.setText("Tipo");

        lblFecha.setText("Fecha");

        lblUsuarioEmpleado.setText("Usuario");

        lblEstado.setText("Estado");

        lblNombreEmpleado.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblNombreEmpleado.setText("Nombres: ");

        lblIdEmpleado.setText("ID");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Nombre:");

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Cargo: ");

        jLabel10.setText("Fecha:");

        TBPrincipal.setBackground(new java.awt.Color(255, 255, 255));
        TBPrincipal.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        TBPrincipal.setFloatable(false);
        TBPrincipal.setForeground(new java.awt.Color(255, 255, 255));
        TBPrincipal.setOrientation(javax.swing.SwingConstants.VERTICAL);
        TBPrincipal.setRollover(true);
        TBPrincipal.setMaximumSize(new java.awt.Dimension(67, 647));
        TBPrincipal.setOpaque(false);
        TBPrincipal.setPreferredSize(new java.awt.Dimension(67, 633));

        mbtnEmpleado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/user_22110 (1).png"))); // NOI18N
        mbtnEmpleado.setText("Personal");
        mbtnEmpleado.setFocusable(false);
        mbtnEmpleado.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        mbtnEmpleado.setPreferredSize(new java.awt.Dimension(55, 65));
        mbtnEmpleado.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        mbtnEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mbtnEmpleadoActionPerformed(evt);
            }
        });
        TBPrincipal.add(mbtnEmpleado);

        jLabel1.setText("   ");
        TBPrincipal.add(jLabel1);

        mbtnCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/admin_user_man_22187.png"))); // NOI18N
        mbtnCliente.setText("Clientes");
        mbtnCliente.setFocusable(false);
        mbtnCliente.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        mbtnCliente.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        mbtnCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mbtnClienteActionPerformed(evt);
            }
        });
        TBPrincipal.add(mbtnCliente);

        jLabel7.setText("   ");
        TBPrincipal.add(jLabel7);

        mbtnProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/shipping_products_22121.png"))); // NOI18N
        mbtnProducto.setText("Productos");
        mbtnProducto.setFocusable(false);
        mbtnProducto.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        mbtnProducto.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        mbtnProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mbtnProductoActionPerformed(evt);
            }
        });
        TBPrincipal.add(mbtnProducto);

        jLabel2.setText("   ");
        TBPrincipal.add(jLabel2);

        mbtnCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/shopping_22120.png"))); // NOI18N
        mbtnCompra.setText("Comprar");
        mbtnCompra.setFocusable(false);
        mbtnCompra.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        mbtnCompra.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        mbtnCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mbtnCompraActionPerformed(evt);
            }
        });
        TBPrincipal.add(mbtnCompra);

        jLabel3.setText("   ");
        TBPrincipal.add(jLabel3);

        mbtnVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/shopping_bag_22119.png"))); // NOI18N
        mbtnVenta.setText("Vender");
        mbtnVenta.setFocusable(false);
        mbtnVenta.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        mbtnVenta.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        mbtnVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mbtnVentaActionPerformed(evt);
            }
        });
        TBPrincipal.add(mbtnVenta);

        jLabel4.setText("   ");
        TBPrincipal.add(jLabel4);

        mbtnCaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/dollar_22165.png"))); // NOI18N
        mbtnCaja.setText("Ver caja");
        mbtnCaja.setFocusable(false);
        mbtnCaja.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        mbtnCaja.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        mbtnCaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mbtnCajaActionPerformed(evt);
            }
        });
        TBPrincipal.add(mbtnCaja);

        jLabel6.setText("   ");
        TBPrincipal.add(jLabel6);

        mbtnEstado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/project_plan_22131.png"))); // NOI18N
        mbtnEstado.setText("Control P");
        mbtnEstado.setFocusable(false);
        mbtnEstado.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        mbtnEstado.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        mbtnEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mbtnEstadoActionPerformed(evt);
            }
        });
        TBPrincipal.add(mbtnEstado);

        Escritorio.setBackground(new java.awt.Color(204, 255, 153));

        jMenuBar1.setBackground(new java.awt.Color(204, 255, 0));
        jMenuBar1.setOpaque(false);

        jMenu1.setForeground(new java.awt.Color(255, 255, 255));
        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/box_2.png"))); // NOI18N
        jMenu1.setText("Almacén");
        jMenu1.setOpaque(false);

        mProducto.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        mProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/productos_m.png"))); // NOI18N
        mProducto.setText("Producto");
        mProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mProductoActionPerformed(evt);
            }
        });
        jMenu1.add(mProducto);

        mCategoria.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        mCategoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/categoria.png"))); // NOI18N
        mCategoria.setText("Categoria");
        mCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCategoriaActionPerformed(evt);
            }
        });
        jMenu1.add(mCategoria);

        jMenuBar1.add(jMenu1);

        mnuRegistro.setForeground(new java.awt.Color(255, 255, 255));
        mnuRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/compras_addd.png"))); // NOI18N
        mnuRegistro.setText("Compras");
        mnuRegistro.setOpaque(false);

        mCompra.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
        mCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/compra_m.png"))); // NOI18N
        mCompra.setText("Compra");
        mCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCompraActionPerformed(evt);
            }
        });
        mnuRegistro.add(mCompra);

        mProveedor.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        mProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/proveedores_m.png"))); // NOI18N
        mProveedor.setText("Proveedor");
        mProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mProveedorActionPerformed(evt);
            }
        });
        mnuRegistro.add(mProveedor);

        jMenuBar1.add(mnuRegistro);

        mnuOperaciones.setForeground(new java.awt.Color(255, 255, 255));
        mnuOperaciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/ventas2_1.png"))); // NOI18N
        mnuOperaciones.setText("Ventas");
        mnuOperaciones.setOpaque(false);

        mVenta.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
        mVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/ventas_m.png"))); // NOI18N
        mVenta.setText("Venta");
        mVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mVentaActionPerformed(evt);
            }
        });
        mnuOperaciones.add(mVenta);

        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/documento.png"))); // NOI18N
        jMenuItem4.setText("Venta a Crédito");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        mnuOperaciones.add(jMenuItem4);

        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/importe.png"))); // NOI18N
        jMenuItem9.setText("Cotización");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        mnuOperaciones.add(jMenuItem9);

        mEstado.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F7, 0));
        mEstado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/estado_m.png"))); // NOI18N
        mEstado.setText("Verificar Producto");
        mEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mEstadoActionPerformed(evt);
            }
        });
        mnuOperaciones.add(mEstado);

        mCliente.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F8, 0));
        mCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/cliente_m.png"))); // NOI18N
        mCliente.setText("Cliente");
        mCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mClienteActionPerformed(evt);
            }
        });
        mnuOperaciones.add(mCliente);

        jMenuBar1.add(mnuOperaciones);

        mnuInformes.setForeground(new java.awt.Color(255, 255, 255));
        mnuInformes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/consultas_1.png"))); // NOI18N
        mnuInformes.setText("Reportes");
        mnuInformes.setOpaque(false);

        mVentare.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/ventasrealizadas.png"))); // NOI18N
        mVentare.setText("Ventas Realizadas");
        mVentare.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mVentareActionPerformed(evt);
            }
        });
        mnuInformes.add(mVentare);

        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/ventasrealizadas.png"))); // NOI18N
        jMenuItem6.setText("Ventas Realizadas a Crédito");
        jMenuItem6.setToolTipText("");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        mnuInformes.add(jMenuItem6);

        mVentade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/ventasdetalladas.png"))); // NOI18N
        mVentade.setText("Ventas Detalladas");
        mVentade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mVentadeActionPerformed(evt);
            }
        });
        mnuInformes.add(mVentade);

        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/ventasdetalladas.png"))); // NOI18N
        jMenuItem8.setText("Ventas a Crédito Detalladas");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        mnuInformes.add(jMenuItem8);

        mEstadistica.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/estadisticamensual.png"))); // NOI18N
        mEstadistica.setText("Estadística Mensual");
        mEstadistica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mEstadisticaActionPerformed(evt);
            }
        });
        mnuInformes.add(mEstadistica);

        mComprare.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/comprasrealizadas.png"))); // NOI18N
        mComprare.setText("Compras Realizadas");
        mComprare.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mComprareActionPerformed(evt);
            }
        });
        mnuInformes.add(mComprare);

        mComprade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/comprasdetalladas.png"))); // NOI18N
        mComprade.setText("Compras Detalladas");
        mComprade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCompradeActionPerformed(evt);
            }
        });
        mnuInformes.add(mComprade);

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/report.png"))); // NOI18N
        jMenuItem2.setText("Stock Mínimo");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        mnuInformes.add(jMenuItem2);

        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/coins_add.png"))); // NOI18N
        jMenuItem3.setText("Kardex Valorizado");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        mnuInformes.add(jMenuItem3);

        jMenuBar1.add(mnuInformes);

        mnuHerramientas.setForeground(new java.awt.Color(255, 255, 255));
        mnuHerramientas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/herramientas_2.png"))); // NOI18N
        mnuHerramientas.setText("Herramientas");
        mnuHerramientas.setOpaque(false);

        mCambiarpass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/group_key.png"))); // NOI18N
        mCambiarpass.setText("Cambiar contraseña");
        mCambiarpass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCambiarpassActionPerformed(evt);
            }
        });
        mnuHerramientas.add(mCambiarpass);

        mCalculadora.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/calculator.png"))); // NOI18N
        mCalculadora.setText("Calculadora");
        mCalculadora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCalculadoraActionPerformed(evt);
            }
        });
        mnuHerramientas.add(mCalculadora);

        mRespaldar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/database_save.png"))); // NOI18N
        mRespaldar.setText("Respaldar BD");
        mRespaldar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mRespaldarActionPerformed(evt);
            }
        });
        mnuHerramientas.add(mRespaldar);

        mRestaurar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/database_refresh.png"))); // NOI18N
        mRestaurar.setText("Restaurar BD");
        mRestaurar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mRestaurarActionPerformed(evt);
            }
        });
        mnuHerramientas.add(mRestaurar);

        jMenuBar1.add(mnuHerramientas);

        mnuAnulaciones.setForeground(new java.awt.Color(255, 255, 255));
        mnuAnulaciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/mantenimiento_1.png"))); // NOI18N
        mnuAnulaciones.setText("Anular registro");
        mnuAnulaciones.setOpaque(false);

        mAnularv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/anular-proceso.png"))); // NOI18N
        mAnularv.setText("Anular Venta");
        mAnularv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mAnularvActionPerformed(evt);
            }
        });
        mnuAnulaciones.add(mAnularv);

        jMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/anular-proceso.png"))); // NOI18N
        jMenuItem7.setText("Anular Venta a Crédito");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        mnuAnulaciones.add(jMenuItem7);

        mAnularc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/anular-proceso.png"))); // NOI18N
        mAnularc.setText("Anular Compra");
        mAnularc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mAnularcActionPerformed(evt);
            }
        });
        mnuAnulaciones.add(mAnularc);

        jMenuBar1.add(mnuAnulaciones);

        mnuMantenimiento.setForeground(new java.awt.Color(255, 255, 255));
        mnuMantenimiento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/manten.png"))); // NOI18N
        mnuMantenimiento.setText("Permisos");
        mnuMantenimiento.setOpaque(false);

        mEmpleado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/empleados_m.png"))); // NOI18N
        mEmpleado.setText("Empleado");
        mEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mEmpleadoActionPerformed(evt);
            }
        });
        mnuMantenimiento.add(mEmpleado);

        mTipodoc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/documento.png"))); // NOI18N
        mTipodoc.setText("Tipo de Documento");
        mTipodoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mTipodocActionPerformed(evt);
            }
        });
        mnuMantenimiento.add(mTipodoc);

        mTipouser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/User2.png"))); // NOI18N
        mTipouser.setText("Tipo de Usuario");
        mTipouser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mTipouserActionPerformed(evt);
            }
        });
        mnuMantenimiento.add(mTipouser);

        jMenuBar1.add(mnuMantenimiento);

        mnuAyuda.setForeground(new java.awt.Color(255, 255, 255));
        mnuAyuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/ayuda_1.png"))); // NOI18N
        mnuAyuda.setText("Soporte");
        mnuAyuda.setOpaque(false);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/help.png"))); // NOI18N
        jMenuItem1.setText("Manual de usuario");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        mnuAyuda.add(jMenuItem1);

        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Info.png"))); // NOI18N
        jMenuItem5.setText("Contacto");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        mnuAyuda.add(jMenuItem5);

        jMenuBar1.add(mnuAyuda);

        mnuArchivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/archivo_1.png"))); // NOI18N
        mnuArchivo.setText("Salir");
        mnuArchivo.setOpaque(false);

        JMIniciarSesion.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        JMIniciarSesion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/connect.png"))); // NOI18N
        JMIniciarSesion.setText("Iniciar sesión");
        JMIniciarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMIniciarSesionActionPerformed(evt);
            }
        });
        mnuArchivo.add(JMIniciarSesion);

        JMCerrarSesion.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        JMCerrarSesion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/disconnect.png"))); // NOI18N
        JMCerrarSesion.setText("Cerrar sesión");
        JMCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMCerrarSesionActionPerformed(evt);
            }
        });
        mnuArchivo.add(JMCerrarSesion);

        JMSalir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        JMSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/door_in.png"))); // NOI18N
        JMSalir.setText("Salir de la aplicación");
        JMSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMSalirActionPerformed(evt);
            }
        });
        mnuArchivo.add(JMSalir);

        jMenuBar1.add(mnuArchivo);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(TBPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblIdEmpleado)
                        .addGap(66, 66, 66)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblNombreEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                        .addComponent(lblUsuarioEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(lblEstado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 213, Short.MAX_VALUE)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(Escritorio)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Escritorio)
                    .addComponent(TBPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 778, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(lblTipo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblUsuarioEmpleado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblEstado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblNombreEmpleado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblIdEmpleado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(1417, 919));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        BuscarPermisos();
        lblEstado.setText("Desconectado");
        if (intEstado == 1) {
            lblIdEmpleado.setText(strIdEmpleado);
            lblNombreEmpleado.setText(strNombreEmpleado);
            lblUsuarioEmpleado.setText(strUsuario);
            lblTipo.setText(strTipo);
            lblEstado.setText("Conectado");
        }
        if (lblEstado.getText().equals("Desconectado")) {
            mnuArchivo.setVisible(false);
            mnuMantenimiento.setVisible(false);
            TBPrincipal.setVisible(false);
        }
        if (lblUsuarioEmpleado.getText().equals("Coordinador")) {
            mnuRegistro.setVisible(false);
            mnuMantenimiento.setVisible(false);

        }

        //-------------------BLOQUEAR SERVICIOS DEL SISTEMA----------------
        if (p_venta.equals("1")) {
            mVenta.setEnabled(true);
            mbtnVenta.setEnabled(true);
        } else {
            mVenta.setEnabled(false);
            mbtnVenta.setEnabled(false);
        }
        if (p_compra.equals("1")) {
            mCompra.setEnabled(true);
            mbtnCompra.setEnabled(true);
        } else {
            mCompra.setEnabled(false);
            mbtnCompra.setEnabled(false);
        }
        if (p_producto.equals("1")) {
            mProducto.setEnabled(true);
            mbtnProducto.setEnabled(true);
        } else {
            mProducto.setEnabled(false);
            mbtnProducto.setEnabled(false);
        }
        if (p_proveedor.equals("1")) {
            mProveedor.setEnabled(true);
        } else {
            mProveedor.setEnabled(false);
        }
        if (p_empleado.equals("1")) {
            mEmpleado.setEnabled(true);
            mbtnEmpleado.setEnabled(true);
        } else {
            mEmpleado.setEnabled(false);
            mbtnEmpleado.setEnabled(false);
        }
        if (p_cliente.equals("1")) {
            mCliente.setEnabled(true);
            mbtnCliente.setEnabled(true);
        } else {
            mCliente.setEnabled(false);
            mbtnCliente.setEnabled(false);
        }
        if (p_categoria.equals("1")) {
            mCategoria.setEnabled(true);
        } else {
            mCategoria.setEnabled(false);
        }
        if (p_tipodoc.equals("1")) {
            mTipodoc.setEnabled(true);
        } else {
            mTipodoc.setEnabled(false);
        }
        if (p_tipouser.equals("1")) {
            mTipouser.setEnabled(true);
        } else {
            mTipouser.setEnabled(false);
        }
        if (p_anularv.equals("1")) {
            mAnularv.setEnabled(true);
        } else {
            mAnularv.setEnabled(false);
        }
        if (p_anularc.equals("1")) {
            mAnularc.setEnabled(true);
        } else {
            mAnularc.setEnabled(false);
        }
        if (p_estadoprod.equals("1")) {
            mEstado.setEnabled(true);
            mbtnEstado.setEnabled(true);
        } else {
            mEstado.setEnabled(false);
            mbtnEstado.setEnabled(false);
        }
        if (p_ventare.equals("1")) {
            mVentare.setEnabled(true);
        } else {
            mVentare.setEnabled(false);
        }
        if (p_ventade.equals("1")) {
            mVentade.setEnabled(true);
        } else {
            mVentade.setEnabled(false);
        }
        if (p_estadistica.equals("1")) {
            mEstadistica.setEnabled(true);
        } else {
            mEstadistica.setEnabled(false);
        }
        if (p_comprare.equals("1")) {
            mComprare.setEnabled(true);
        } else {
            mComprare.setEnabled(false);
        }
        if (p_comprade.equals("1")) {
            mComprade.setEnabled(true);
        } else {
            mComprade.setEnabled(false);
        }
        if (p_pass.equals("1")) {
            mCambiarpass.setEnabled(true);
        } else {
            mCambiarpass.setEnabled(false);
        }
        if (p_respaldar.equals("1")) {
            mRespaldar.setEnabled(true);
        } else {
            mRespaldar.setEnabled(false);
        }
        if (p_restaurar.equals("1")) {
            mRestaurar.setEnabled(true);
        } else {
            mRestaurar.setEnabled(false);
        }
        if (p_caja.equals("1")) {
            mbtnCaja.setEnabled(true);
        } else {
            mbtnCaja.setEnabled(false);
        }
    }//GEN-LAST:event_formComponentShown

    private void mbtnProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mbtnProductoActionPerformed
        Presentacion.FrmProducto Producto = new Presentacion.FrmProducto();
        Escritorio.add(Producto);
        Producto.show();
    }//GEN-LAST:event_mbtnProductoActionPerformed

    private void mbtnClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mbtnClienteActionPerformed
        Presentacion.FrmCliente Cliente = new Presentacion.FrmCliente();
        Escritorio.add(Cliente);
        Cliente.show();
    }//GEN-LAST:event_mbtnClienteActionPerformed

    private void mbtnEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mbtnEmpleadoActionPerformed
        Presentacion.FrmEmpleado Empleado = new Presentacion.FrmEmpleado();
        Escritorio.add(Empleado);
        Empleado.show();
    }//GEN-LAST:event_mbtnEmpleadoActionPerformed

    private void mbtnVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mbtnVentaActionPerformed
        Presentacion.FrmVenta venta = new Presentacion.FrmVenta();
        Escritorio.add(venta);
        venta.show();
        venta.IdEmpleado = lblIdEmpleado.getText();
        venta.NombreEmpleado = lblNombreEmpleado.getText();
    }//GEN-LAST:event_mbtnVentaActionPerformed

    private void mbtnCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mbtnCompraActionPerformed
        Presentacion.FrmCompra compra = new Presentacion.FrmCompra();
        Escritorio.add(compra);
        compra.show();
        compra.IdEmpleado = lblIdEmpleado.getText();
        compra.NombreEmpleado = lblNombreEmpleado.getText();
    }//GEN-LAST:event_mbtnCompraActionPerformed

    private void mbtnEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mbtnEstadoActionPerformed
        Consultas.FrmProductoEstado ProductoEstado = new Consultas.FrmProductoEstado();
        Escritorio.add(ProductoEstado);
        ProductoEstado.show();
    }//GEN-LAST:event_mbtnEstadoActionPerformed

    private void mbtnCajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mbtnCajaActionPerformed
        Presentacion.FrmCaja Caja = new Presentacion.FrmCaja();
        Escritorio.add(Caja);
        Caja.show();
    }//GEN-LAST:event_mbtnCajaActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
        FrmAcercaDe form = new FrmAcercaDe();
        Escritorio.add(form);
        form.show();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        try {
            File path = new File("Manual.pdf");
            Desktop.getDesktop().open(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void mTipouserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mTipouserActionPerformed
        Presentacion.FrmTipoUsuario Usuario = new Presentacion.FrmTipoUsuario();
        Escritorio.add(Usuario);
        Usuario.show();
    }//GEN-LAST:event_mTipouserActionPerformed

    private void mTipodocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mTipodocActionPerformed
        Presentacion.FrmTipoDocumento Documento = new Presentacion.FrmTipoDocumento();
        Escritorio.add(Documento);
        Documento.show();
    }//GEN-LAST:event_mTipodocActionPerformed

    private void mEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mEmpleadoActionPerformed
        Presentacion.FrmEmpleado Empleado = new Presentacion.FrmEmpleado();
        Escritorio.add(Empleado);
        Empleado.show();
    }//GEN-LAST:event_mEmpleadoActionPerformed

    private void mAnularcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mAnularcActionPerformed
        Presentacion.FrmAnularCompra AnularCompra = new Presentacion.FrmAnularCompra();
        Escritorio.add(AnularCompra);
        AnularCompra.show();
    }//GEN-LAST:event_mAnularcActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        Presentacion.FrmAnularCredito AnularVentaCredito = new Presentacion.FrmAnularCredito();
        Escritorio.add(AnularVentaCredito);
        AnularVentaCredito.show();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void mAnularvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mAnularvActionPerformed
        Presentacion.FrmAnularVenta AnularVenta = new Presentacion.FrmAnularVenta();
        Escritorio.add(AnularVenta);
        AnularVenta.show();
    }//GEN-LAST:event_mAnularvActionPerformed

    private void mRestaurarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mRestaurarActionPerformed
        Backup.FrmRestaurarDB Restaurar = new Backup.FrmRestaurarDB();
        Escritorio.add(Restaurar);
        Restaurar.show();
    }//GEN-LAST:event_mRestaurarActionPerformed

    private void mRespaldarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mRespaldarActionPerformed
        Backup.FrmRespaldarDB Respaldar = new Backup.FrmRespaldarDB();
        Escritorio.add(Respaldar);
        Respaldar.show();
    }//GEN-LAST:event_mRespaldarActionPerformed

    private void mCalculadoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCalculadoraActionPerformed
        try {
            Runtime obj = Runtime.getRuntime();
            obj.exec("C:\\WINDOWS\\system32\\CALC.EXE");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_mCalculadoraActionPerformed

    private void mCambiarpassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCambiarpassActionPerformed
        Presentacion.FrmCambiarContrasenia Cambiar = new Presentacion.FrmCambiarContrasenia();
        Escritorio.add(Cambiar);
        Cambiar.show();
        Cambiar.IdEmpleado = lblIdEmpleado.getText();

    }//GEN-LAST:event_mCambiarpassActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        FrmKardexValorizado form = new FrmKardexValorizado();
        Escritorio.add(form);
        form.show();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        FrmConsultaStock form = new FrmConsultaStock();
        Escritorio.add(form);
        form.show();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void mCompradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCompradeActionPerformed
        Consultas.FrmComprasRealizadas_Detallado Compras = new Consultas.FrmComprasRealizadas_Detallado();
        Escritorio.add(Compras);
        Compras.show();
    }//GEN-LAST:event_mCompradeActionPerformed

    private void mComprareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mComprareActionPerformed
        Consultas.FrmComprasRelizadas ComprasRelizadas = new Consultas.FrmComprasRelizadas();
        Escritorio.add(ComprasRelizadas);
        ComprasRelizadas.show();
    }//GEN-LAST:event_mComprareActionPerformed

    private void mEstadisticaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mEstadisticaActionPerformed
        Consultas.FrmVentasMensuales Ventas = new Consultas.FrmVentasMensuales();
        Escritorio.add(Ventas);
        Ventas.show();
    }//GEN-LAST:event_mEstadisticaActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        Consultas.FrmCreditosRealizados_Detallado ventasCreditoDetalladas = new Consultas.FrmCreditosRealizados_Detallado();
        Escritorio.add(ventasCreditoDetalladas);
        ventasCreditoDetalladas.show();
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void mVentadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mVentadeActionPerformed
        Consultas.FrmVentasRealizadas_Detallado ventasdetalladas = new Consultas.FrmVentasRealizadas_Detallado();
        Escritorio.add(ventasdetalladas);
        ventasdetalladas.show();
    }//GEN-LAST:event_mVentadeActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        Consultas.FrmCreditosRealizados VentasCreditoRealizadas = new Consultas.FrmCreditosRealizados();
        Escritorio.add(VentasCreditoRealizadas);
        VentasCreditoRealizadas.show();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void mVentareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mVentareActionPerformed
        Consultas.FrmVentasRealizadas VentasRealizadas = new Consultas.FrmVentasRealizadas();
        Escritorio.add(VentasRealizadas);
        VentasRealizadas.show();
    }//GEN-LAST:event_mVentareActionPerformed

    private void mClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mClienteActionPerformed
        Presentacion.FrmCliente Cliente = new Presentacion.FrmCliente();
        Escritorio.add(Cliente);
        Cliente.show();
    }//GEN-LAST:event_mClienteActionPerformed

    private void mEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mEstadoActionPerformed
        Consultas.FrmProductoEstado ProductoEstado = new Consultas.FrmProductoEstado();
        Escritorio.add(ProductoEstado);
        ProductoEstado.show();
    }//GEN-LAST:event_mEstadoActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        Presentacion.FrmCotizacion cotizacion = new Presentacion.FrmCotizacion();
        Escritorio.add(cotizacion);
        cotizacion.show();
        cotizacion.IdEmpleado = lblIdEmpleado.getText();
        cotizacion.NombreEmpleado = lblNombreEmpleado.getText();
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        Presentacion.FrmVentaCredito credito = new Presentacion.FrmVentaCredito();
        Escritorio.add(credito);
        credito.show();
        credito.IdEmpleado = lblIdEmpleado.getText();
        credito.NombreEmpleado = lblNombreEmpleado.getText();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void mVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mVentaActionPerformed
        if (venta != null) {
            venta.dispose();
            System.gc();
        }
        venta = new Presentacion.FrmVenta();
        Escritorio.add(venta);
        venta.show();
        venta.IdEmpleado = lblIdEmpleado.getText();
        venta.NombreEmpleado = lblNombreEmpleado.getText();
    }//GEN-LAST:event_mVentaActionPerformed

    private void mProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mProveedorActionPerformed
        Presentacion.FrmProveedor Proveedor = new Presentacion.FrmProveedor();
        Escritorio.add(Proveedor);
        Proveedor.show();
    }//GEN-LAST:event_mProveedorActionPerformed

    private void mCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCompraActionPerformed
        Presentacion.FrmCompra compra = new Presentacion.FrmCompra();
        Escritorio.add(compra);
        compra.show();
        compra.IdEmpleado = lblIdEmpleado.getText();
        compra.NombreEmpleado = lblNombreEmpleado.getText();
    }//GEN-LAST:event_mCompraActionPerformed

    private void mCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCategoriaActionPerformed
        Presentacion.FrmCategoria Categoria = new Presentacion.FrmCategoria();
        Escritorio.add(Categoria);
        Categoria.show();
    }//GEN-LAST:event_mCategoriaActionPerformed

    private void mProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mProductoActionPerformed
        Presentacion.FrmProducto Producto = new Presentacion.FrmProducto();
        Escritorio.add(Producto);
        Producto.show();
    }//GEN-LAST:event_mProductoActionPerformed

    private void JMSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMSalirActionPerformed

        int salir = JOptionPane.showConfirmDialog(this, "¿Desea realmente cerrar la aplicación?", "Mensaje del Sistema", 0, 3);
        if (salir == JOptionPane.OK_OPTION) {
            closeConfiguration();
            System.exit(0);
        }

    }//GEN-LAST:event_JMSalirActionPerformed

    private void JMCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMCerrarSesionActionPerformed
        JMCerrarSesion.setEnabled(false);
        mnuRegistro.setEnabled(false);
        mnuOperaciones.setEnabled(false);
        mnuMantenimiento.setEnabled(false);
        mnuInformes.setEnabled(false);
        mnuHerramientas.setEnabled(false);
        mnuAnulaciones.setEnabled(false);
        TBPrincipal.setEnabled(false);
        mbtnEmpleado.setEnabled(false);
        mbtnCliente.setEnabled(false);
        mbtnProducto.setEnabled(false);
        mbtnCompra.setEnabled(false);
        mbtnVenta.setEnabled(false);
        mbtnCaja.setEnabled(false);
        mbtnEstado.setEnabled(false);
        JMIniciarSesion.setEnabled(true);
        closeConfiguration();

    }//GEN-LAST:event_JMCerrarSesionActionPerformed

    private void JMIniciarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMIniciarSesionActionPerformed
        this.dispose();
        Presentacion.FrmLogin Login = new Presentacion.FrmLogin();

        Login.show();

    }//GEN-LAST:event_JMIniciarSesionActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new FrmPrincipal().setVisible(true);
//            }
//        });
//    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JDesktopPane Escritorio;
    private javax.swing.JMenuItem JMCerrarSesion;
    private javax.swing.JMenuItem JMIniciarSesion;
    private javax.swing.JMenuItem JMSalir;
    private javax.swing.JToolBar TBPrincipal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblIdEmpleado;
    private javax.swing.JLabel lblNombreEmpleado;
    private javax.swing.JLabel lblTipo;
    private javax.swing.JLabel lblUsuarioEmpleado;
    private javax.swing.JMenuItem mAnularc;
    private javax.swing.JMenuItem mAnularv;
    private javax.swing.JMenuItem mCalculadora;
    private javax.swing.JMenuItem mCambiarpass;
    private javax.swing.JMenuItem mCategoria;
    private javax.swing.JMenuItem mCliente;
    private javax.swing.JMenuItem mCompra;
    private javax.swing.JMenuItem mComprade;
    private javax.swing.JMenuItem mComprare;
    private javax.swing.JMenuItem mEmpleado;
    private javax.swing.JMenuItem mEstadistica;
    private javax.swing.JMenuItem mEstado;
    private javax.swing.JMenuItem mProducto;
    private javax.swing.JMenuItem mProveedor;
    private javax.swing.JMenuItem mRespaldar;
    private javax.swing.JMenuItem mRestaurar;
    private javax.swing.JMenuItem mTipodoc;
    private javax.swing.JMenuItem mTipouser;
    private javax.swing.JMenuItem mVenta;
    private javax.swing.JMenuItem mVentade;
    private javax.swing.JMenuItem mVentare;
    private javax.swing.JButton mbtnCaja;
    private javax.swing.JButton mbtnCliente;
    private javax.swing.JButton mbtnCompra;
    private javax.swing.JButton mbtnEmpleado;
    private javax.swing.JButton mbtnEstado;
    private javax.swing.JButton mbtnProducto;
    private javax.swing.JButton mbtnVenta;
    private javax.swing.JMenu mnuAnulaciones;
    private javax.swing.JMenu mnuArchivo;
    private javax.swing.JMenu mnuAyuda;
    private javax.swing.JMenu mnuHerramientas;
    private javax.swing.JMenu mnuInformes;
    private javax.swing.JMenu mnuMantenimiento;
    private javax.swing.JMenu mnuOperaciones;
    private javax.swing.JMenu mnuRegistro;
    // End of variables declaration//GEN-END:variables

    @Override
    public void dispose() {
        if (JOptionPane.showConfirmDialog(
                this,
                "¿Desea realmente cerrar la aplicación?",
                "Mensaje del Sistema",
                0,
                3)
                == JOptionPane.OK_OPTION) {
            closeConfiguration();
            super.dispose();
        }
    }

    /**
     * Cierra correctamente el resto de las ventanas
     */
    private void closeConfiguration() {
        try {
            if (venta != null) {
                venta.dispose();
            }
            writeFrameRectangle();
            // Agregar dispose del resto de las ventanas
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public void readFrameRectanble() {
        ObjectDeserializer<Rectangle> deserializer = new ObjectDeserializer<>(Paths.SERIAL_DIRECTORY_DATA, Paths.MAIN_RECTANGLE_NAME);
        Rectangle rectangle = deserializer.deserialicer();
        if (rectangle != null) {
            setBounds(rectangle);
        }
    }

    @Override
    public void writeFrameRectangle() {
        ObjectSerializer<Rectangle> serializer = new ObjectSerializer<>(Paths.SERIAL_DIRECTORY_DATA, Paths.MAIN_RECTANGLE_NAME);
        serializer.serilizer(getBounds());
    }
}
