/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Consultas;

import Entidad.ClsEntidadCliente;
import Entidad.dtos.ClientDto;
import Negocio.ClsCliente;
import Presentacion.FrmVenta;
import interfaces.ClientInterface;
import interfaces.FrameState;
import java.awt.AWTException;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import statics.Design;
import statics.Dimensions;
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
public class FrmBuscarCliente extends javax.swing.JInternalFrame implements FrameState {

    static ResultSet rs = null;
    DefaultTableModel dtm = new DefaultTableModel();

    String criterio, busqueda, Total;

    private ClientInterface clientInterface;

    private final String titulos[] = {"ID", "NOMBRE", "CI", "DIRECCIÓN", "TELÉFONO", "OBSERVACIÓN"};
    private final float[] widths = {4.95F, 26.81F, 9.54F, 24.86F, 12.52F, 21.31F}; // pos 2:  9.52F

    public FrmBuscarCliente(ClientInterface clientInterface) {
        this.clientInterface = clientInterface;
        initComponents();
        buttonGroup1.add(rbtnCodigo);
        buttonGroup1.add(rbtnNombre);
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

    private void design() {
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

    void actualizarTablaCliente() {
        ClsCliente clientes = new ClsCliente();
        ArrayList<ClsEntidadCliente> cliente = clientes.listarCliente();
        Iterator iterator = cliente.iterator();
        DefaultTableModel defaultTableModel = new DefaultTableModel(null, titulos) {
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
//            fila[2] = Cliente.getStrRucCliente();
            fila[2] = Cliente.getStrDniCliente();
            fila[3] = Cliente.getStrDireccionCliente();
            fila[4] = Cliente.getStrTelefonoCliente();
            fila[5] = Cliente.getStrObsvCliente();
            defaultTableModel.addRow(fila);
        }
        tblCliente.setModel(defaultTableModel);
    }

    void CrearTablaCliente() {
        //Agregar Render
        for (int i = 0; i < tblCliente.getColumnCount(); i++) {
            Hashtable<Integer, Integer> map = new Hashtable<Integer, Integer>();
            map.put(0, SwingConstants.CENTER);
            TableCellRenderer render = TableConfigurator.configureTableItem(map);
            tblCliente.getColumnModel().getColumn(i).setCellRenderer(render);
        }
        //Activar ScrollBar
        tblCliente.setAutoResizeMode(tblCliente.AUTO_RESIZE_OFF);
    }

    void BuscarClientePanel() {
        dtm = new DefaultTableModel(null, titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ClsCliente cliente = new ClsCliente();
        busqueda = txtBusqueda.getText();
        if (rbtnCodigo.isSelected()) {
            criterio = "id";
        } else if (rbtnNombre.isSelected()) {
            criterio = "nombre";
        } else if (rbtnDni.isSelected()) {
            criterio = "dni";
        }
        try {
            rs = cliente.listarClientePorParametro(criterio, busqueda);
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
//                Datos[2] = (String) rs.getString(3);
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

    void CantidadTotal() {
        Total = String.valueOf(tblCliente.getRowCount());
        lblEstado.setText("Se cargaron " + Total + " registros");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPopupMenuGarzones = new javax.swing.JPopupMenu();
        jMenuItemGarzon1 = new javax.swing.JMenuItem();
        jMenuItemGarzon2 = new javax.swing.JMenuItem();
        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        rbtnCodigo = new javax.swing.JRadioButton();
        rbtnNombre = new javax.swing.JRadioButton();
        rbtnDni = new javax.swing.JRadioButton();
        txtBusqueda = new javax.swing.JTextField();
        btnSalir = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblCliente = new javax.swing.JTable();
        panelClientData = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabelImage = new javax.swing.JLabel();
        jLabelTitle = new javax.swing.JLabel();
        jLabelContent = new javax.swing.JLabel();
        lblEstado = new javax.swing.JLabel();

        jPopupMenuGarzones.setBackground(java.awt.Color.darkGray);
        jPopupMenuGarzones.setForeground(new java.awt.Color(255, 255, 255));
        jPopupMenuGarzones.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jMenuItemGarzon1.setText("Garzón 1");
        jMenuItemGarzon1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemGarzon1ActionPerformed(evt);
            }
        });
        jPopupMenuGarzones.add(jMenuItemGarzon1);

        jMenuItemGarzon2.setText("Garzón 2");
        jMenuItemGarzon2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemGarzon2ActionPerformed(evt);
            }
        });
        jPopupMenuGarzones.add(jMenuItemGarzon2);

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Consultar Garzones");
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
        rbtnCodigo.setText("ID Garzón");
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
        rbtnNombre.setText("Nombre de garzón");
        rbtnNombre.setOpaque(false);
        rbtnNombre.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbtnNombreStateChanged(evt);
            }
        });
        rbtnNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnNombreActionPerformed(evt);
            }
        });
        jPanel1.add(rbtnNombre);

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

        jPanel7.setOpaque(false);
        jPanel7.setLayout(new java.awt.BorderLayout(5, 5));

        jPanel5.setMinimumSize(new java.awt.Dimension(300, 300));
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
        tblCliente.setComponentPopupMenu(jPopupMenuGarzones);
        tblCliente.setRowHeight(22);
        tblCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClienteMouseClicked(evt);
            }
        });
        tblCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblClienteKeyReleased(evt);
            }
        });
        jScrollPane5.setViewportView(tblCliente);

        jPanel5.add(jScrollPane5);

        jPanel7.add(jPanel5, java.awt.BorderLayout.CENTER);

        panelClientData.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        panelClientData.setForeground(new java.awt.Color(255, 255, 255));
        panelClientData.setMaximumSize(new java.awt.Dimension(200, 500000));
        panelClientData.setMinimumSize(new java.awt.Dimension(100, 5000));
        panelClientData.setOpaque(false);
        panelClientData.setPreferredSize(new java.awt.Dimension(200, 200));
        panelClientData.setLayout(new java.awt.BorderLayout());

        jPanel8.setBackground(new java.awt.Color(204, 204, 204));
        jPanel8.setOpaque(false);
        jPanel8.setPreferredSize(new java.awt.Dimension(200, 200));
        jPanel8.setLayout(new java.awt.GridLayout(1, 1));

        jLabelImage.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabelImage.setForeground(new java.awt.Color(255, 255, 255));
        jLabelImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelImage.setText("PHOTO");
        jPanel8.add(jLabelImage);

        panelClientData.add(jPanel8, java.awt.BorderLayout.CENTER);

        jLabelTitle.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabelTitle.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTitle.setText("NAME");
        jLabelTitle.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        panelClientData.add(jLabelTitle, java.awt.BorderLayout.NORTH);

        jLabelContent.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabelContent.setForeground(new java.awt.Color(255, 255, 255));
        jLabelContent.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelContent.setText("UNDEFINED");
        jLabelContent.setToolTipText("CONTENT");
        jLabelContent.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        panelClientData.add(jLabelContent, java.awt.BorderLayout.SOUTH);

        jPanel7.add(panelClientData, java.awt.BorderLayout.EAST);

        jPanel6.add(jPanel7, java.awt.BorderLayout.CENTER);

        lblEstado.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblEstado.setForeground(new java.awt.Color(255, 255, 255));
        lblEstado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstado.setText("Cantidad");
        jPanel6.add(lblEstado, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(jPanel6);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClienteMouseClicked
        if (evt.getClickCount() == 2) {
            ClsEntidadCliente client = sendClient();
            if (client == null) {
                Toast.makeText("¡Selecciona un garzón!", Toast.INFORMATION).show();
            } else {
                if (!clientInterface.loadClient(client)) {
                    try {
                        Robot robot = new Robot();
                        robot.mousePress(InputEvent.BUTTON3_MASK);
                        robot.mouseRelease(InputEvent.BUTTON3_MASK);
                    } catch (AWTException ex) {
                        Logger.getLogger(FrmBuscarCliente.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }else{
            loadClientPhoto();
        }
        
    }//GEN-LAST:event_tblClienteMouseClicked

    private ClsEntidadCliente sendClient() {
        int fila;
        DefaultTableModel defaultTableModel = new DefaultTableModel();
        fila = tblCliente.getSelectedRow();

        if (fila == -1) {
            return null;
        } else {
            defaultTableModel = (DefaultTableModel) tblCliente.getModel();
            ClsEntidadCliente cliente = new ClsEntidadCliente();
            cliente.setStrIdCliente((String) defaultTableModel.getValueAt(fila, 0));
            cliente.setStrNombreCliente((String) defaultTableModel.getValueAt(fila, 1));
            return cliente;
        }
    }
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

    private void jMenuItemGarzon1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemGarzon1ActionPerformed
        ClsEntidadCliente cliente = sendClient();
        if (cliente == null) {
            Toast.makeText("¡Selecciona un garzón!", Toast.INFORMATION).show();
        } else {
            clientInterface.loadClient(cliente, FrmVenta.GARZON_1);
        }
    }//GEN-LAST:event_jMenuItemGarzon1ActionPerformed

    private void jMenuItemGarzon2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemGarzon2ActionPerformed
        ClsEntidadCliente cliente = sendClient();
        if (cliente == null) {
            Toast.makeText("¡Selecciona un garzón!", Toast.INFORMATION).show();
        } else {
            clientInterface.loadClient(cliente, FrmVenta.GARZON_2);
        }
    }//GEN-LAST:event_jMenuItemGarzon2ActionPerformed

    private void rbtnNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtnNombreActionPerformed

    private void tblClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblClienteKeyReleased
        loadClientPhoto();
    }//GEN-LAST:event_tblClienteKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabelContent;
    private javax.swing.JLabel jLabelImage;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JMenuItem jMenuItemGarzon1;
    private javax.swing.JMenuItem jMenuItemGarzon2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPopupMenu jPopupMenuGarzones;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JPanel panelClientData;
    private javax.swing.JRadioButton rbtnCodigo;
    private javax.swing.JRadioButton rbtnDni;
    private javax.swing.JRadioButton rbtnNombre;
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
        if (rectangle != null) {
            setBounds(rectangle);
        }
    }

    @Override
    public void writeFrameRectangle() {
        ObjectSerializer<Rectangle> serializer = new ObjectSerializer<Rectangle>(Paths.SERIAL_DIRECTORY_DATA, Paths.CLIENT_RECTANGLE_NAME);
        serializer.serilizer(getBounds());
    }

    private void loadClientPhoto() {
        ClsEntidadCliente client = sendClient();
        if (client != null) {
            ClientThread clientThread = new ClientThread(Integer.parseInt(client.getStrIdCliente()), panelClientData, jLabelImage, jLabelTitle, jLabelContent);
            clientThread.start();
        }else{
            Toast.makeText(Toast.DANGER, "No se encontró el registro", Toast.LENGTH_SHORT).show();
        }
    }
    
    class ClientThread extends Thread {
        private JPanel panel;
        private JLabel image, title, content;
        private int id;
        public ClientThread(int id, JPanel panel, JLabel image, JLabel title, JLabel content) {
            this.id = id;
            this.image = image;
            this.panel = panel;
            this.title = title;
            this.content = content;
        }
        @Override
        public void run() {
            ClsCliente cliente = new ClsCliente();
            ClientDto clientDto = cliente.findById(id);
            image.setText(null);
            image.setSize(Dimensions.REQUEST_IMAGE_DIMENSION_SIZE);
            if (clientDto != null) {
                title.setText(clientDto.getName());
                content.setText("Teléfono: " + clientDto.getPhone());
                if (clientDto.getImageIcon() != null) {
                    Icon icon = new ImageIcon(clientDto.getImageIcon().getImage().getScaledInstance(
                            Dimensions.REQUEST_IMAGE_DIMENSION_SIZE.width,
                            Dimensions.REQUEST_IMAGE_DIMENSION_SIZE.height,
                            Image.SCALE_FAST)
                    );
                    image.setIcon(icon);
                } else {
                    ImageLoader.setImageWithDim(image,
                            Dimensions.REQUEST_IMAGE_DIMENSION_SIZE.width,
                            Dimensions.REQUEST_IMAGE_DIMENSION_SIZE.height,
                            Paths.DEFAULT_USER_IMAGE_WHITE,
                            Image.SCALE_FAST
                    );
                }
//                panel.add(image, 0);
                panel.repaint();
            } else {
                title.setText("UNDEFINED");
                content.setText("UNDEFINED");
                ImageLoader.setImageWithDim(image,
                        Dimensions.REQUEST_IMAGE_DIMENSION_SIZE.width,
                        Dimensions.REQUEST_IMAGE_DIMENSION_SIZE.height,
                        Paths.DEFAULT_USER_IMAGE_WHITE,
                        Image.SCALE_FAST
                );
                Toast.makeText(Toast.DANGER, "¡El registro no existe en la base de datos!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
