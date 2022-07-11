
package Presentacion;

import Negocio.*;
import Entidad.*;
import javax.swing.*;
import Presentacion.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import statics.Design;
import statics.Paths;
import statics.ScreenUses;
import statics.ImageLoader;
import tools.toast.Toast;

public class FrmLogin extends javax.swing.JFrame {
    
    //algoritmos
    public static String MD2 = "MD2";
    public static String MD5 = "MD5";
    public static String SHA1 = "SHA-1";
    public static String SHA256 = "SHA-256";
    public static String SHA384 = "SHA-384";
    public static String SHA512 = "SHA-512";
    
    String id[]=new String[50];
    static int intContador;
    int intentos=0;
    public String codigo;
    String usu,pass;
    static Connection conn=null;
    static ResultSet rs=null;
    static ResultSet rs1=null;
    DefaultTableModel dtm=new DefaultTableModel();
        
    public FrmLogin() {
        initComponents();
        cargarComboCargo();
        generarAleatorio();
        lblIntentos.setVisible(false);
        setVisible(true);
        setExtendedState(this.MAXIMIZED_BOTH);
        getContentPane().setBackground(Design.COLOR_PRIMARY_DARK);
        design();
    }
    
    private void design(){
        ImageLoader.setImage(image, Paths.IMGE_ICON_PATH + "/login.png", Image.SCALE_SMOOTH);
        ImageLoader.setImage(storeIcon, Paths.IMGE_ICON_PATH + "/store_icon.png", Image.SCALE_DEFAULT);
        btnCancelar.setBackground(Design.COLOR_PRIMARY_DARK);
        btnD0.setBackground(Design.COLOR_PRIMARY_DARK);
        btnD1.setBackground(Design.COLOR_PRIMARY_DARK);
        btnD2.setBackground(Design.COLOR_PRIMARY_DARK);
        btnD3.setBackground(Design.COLOR_PRIMARY_DARK);
        btnD4.setBackground(Design.COLOR_PRIMARY_DARK);
        btnD5.setBackground(Design.COLOR_PRIMARY_DARK);
        btnD6.setBackground(Design.COLOR_PRIMARY_DARK);
        btnD7.setBackground(Design.COLOR_PRIMARY_DARK);
        btnD8.setBackground(Design.COLOR_PRIMARY_DARK);
        btnD9.setBackground(Design.COLOR_PRIMARY_DARK);
        btnIniciar.setBackground(Design.COLOR_PRIMARY_DARK);
        btnLimpiar.setBackground(Design.COLOR_PRIMARY_DARK);
        panelRight.setBackground(Design.COLOR_SECONDARY);
    }
    
    void cargarComboCargo(){
       ClsTipoUsuario tipousuarios=new ClsTipoUsuario();
       ArrayList<ClsEntidadTipoUsuario> operacion=tipousuarios.listarTipoUsuario();
       Iterator iterator=operacion.iterator();
       DefaultComboBoxModel DefaultComboBoxModel=new DefaultComboBoxModel();
       DefaultComboBoxModel.removeAllElements();
       
       cboCargo.removeAll();
       String fila[]=new String[2];
       intContador=0;
       
       while(iterator.hasNext()){
           ClsEntidadTipoUsuario TipoUsuario=new ClsEntidadTipoUsuario();
           TipoUsuario=(ClsEntidadTipoUsuario) iterator.next();
           id[intContador]=TipoUsuario.getStrIdTipoUsuario();
           fila[0]=TipoUsuario.getStrIdTipoUsuario();
           fila[1]=TipoUsuario.getStrDescripcionTipoUsuario();

           DefaultComboBoxModel.addElement(TipoUsuario.getStrDescripcionTipoUsuario());
           intContador++;
              
       }
       cboCargo.setModel(DefaultComboBoxModel);
   }
 
    void generarAleatorio(){
       int n=10;  //numeros aleatorios
       int k=n;  //auxiliar;
        int[] numeros=new int[n];
        int[] resultado=new int[n];
        Random rnd=new Random();
        int res;
        
        //Rellenamos la matriz
        for(int i=0;i<n;i++){
            //numeros[i]=i+1; //----Empieza en 1
            numeros[i]=numeros[i]+i; //----Empieza en 0
        }
        
        for(int i=0;i<n;i++){
            res=rnd.nextInt(k);            
            resultado[i]=numeros[res];
            numeros[res]=numeros[k-1];
            k--;
        }
        
        btnD1.setText(String.valueOf(resultado[0]));
        btnD2.setText(String.valueOf(resultado[1]));
        btnD3.setText(String.valueOf(resultado[2]));
        btnD4.setText(String.valueOf(resultado[3]));
        btnD5.setText(String.valueOf(resultado[4]));
        btnD6.setText(String.valueOf(resultado[5]));
        btnD7.setText(String.valueOf(resultado[6]));
        btnD8.setText(String.valueOf(resultado[7]));
        btnD9.setText(String.valueOf(resultado[8]));
        btnD0.setText(String.valueOf(resultado[9]));

    }
    
     private static String toHexadecimal(byte[] digest)
    {
        String hash = "";
        for(byte aux : digest) 
        {
            int b = aux & 0xff;
            if (Integer.toHexString(b).length() == 1) hash += "0";
            hash += Integer.toHexString(b);
        }
        return hash;
    }
    public static String getStringMessageDigest(String message, String algorithm)
    {
        byte[] digest = null;
        byte[] buffer = message.getBytes();
        try 
        {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.reset();
            messageDigest.update(buffer);
            digest = messageDigest.digest();
        }
        catch (NoSuchAlgorithmException ex) 
        {
            System.out.println("Error creando Digest");
        }
        return toHexadecimal(digest);
    } 
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelLeft = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        storeIcon = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        panelRight = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnD1 = new javax.swing.JButton();
        btnD2 = new javax.swing.JButton();
        btnD3 = new javax.swing.JButton();
        btnD6 = new javax.swing.JButton();
        btnD5 = new javax.swing.JButton();
        btnD4 = new javax.swing.JButton();
        btnD7 = new javax.swing.JButton();
        btnD8 = new javax.swing.JButton();
        btnD9 = new javax.swing.JButton();
        btnD0 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnLimpiar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtContraseña = new javax.swing.JPasswordField();
        cboCargo = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        btnIniciar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        image = new javax.swing.JLabel();
        lblIntentos = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema de Ventas");
        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(1030, 650));
        getContentPane().setLayout(new java.awt.GridLayout(1, 2, -100, 0));

        panelLeft.setOpaque(false);
        panelLeft.setLayout(new java.awt.GridLayout(2, 1));

        jPanel6.setOpaque(false);
        jPanel6.setLayout(new java.awt.GridBagLayout());

        jPanel8.setOpaque(false);

        storeIcon.setMaximumSize(new java.awt.Dimension(200, 200));
        storeIcon.setMinimumSize(new java.awt.Dimension(200, 200));
        storeIcon.setPreferredSize(new java.awt.Dimension(200, 200));
        jPanel8.add(storeIcon);

        jPanel6.add(jPanel8, new java.awt.GridBagConstraints());

        panelLeft.add(jPanel6);

        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(500, 500));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 120)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Tiendas");
        jPanel3.add(jLabel6);

        jLabel5.setFont(new java.awt.Font("Arial", 1, 120)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 153, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("TEC");
        jPanel3.add(jLabel5);

        panelLeft.add(jPanel3);

        getContentPane().add(panelLeft);

        panelRight.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setPreferredSize(new java.awt.Dimension(550, 600));
        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("ACCESO AL SISTEMA");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(0, 20, 550, 29);

        jPanel4.setLayout(new java.awt.GridLayout(4, 3, 5, 5));

        btnD1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btnD1.setForeground(new java.awt.Color(255, 255, 255));
        btnD1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnD1ActionPerformed(evt);
            }
        });
        jPanel4.add(btnD1);

        btnD2.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btnD2.setForeground(new java.awt.Color(255, 255, 255));
        btnD2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnD2ActionPerformed(evt);
            }
        });
        jPanel4.add(btnD2);

        btnD3.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btnD3.setForeground(new java.awt.Color(255, 255, 255));
        btnD3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnD3ActionPerformed(evt);
            }
        });
        jPanel4.add(btnD3);

        btnD6.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btnD6.setForeground(new java.awt.Color(255, 255, 255));
        btnD6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnD6ActionPerformed(evt);
            }
        });
        jPanel4.add(btnD6);

        btnD5.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btnD5.setForeground(new java.awt.Color(255, 255, 255));
        btnD5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnD5ActionPerformed(evt);
            }
        });
        jPanel4.add(btnD5);

        btnD4.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btnD4.setForeground(new java.awt.Color(255, 255, 255));
        btnD4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnD4ActionPerformed(evt);
            }
        });
        jPanel4.add(btnD4);

        btnD7.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btnD7.setForeground(new java.awt.Color(255, 255, 255));
        btnD7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnD7ActionPerformed(evt);
            }
        });
        jPanel4.add(btnD7);

        btnD8.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btnD8.setForeground(new java.awt.Color(255, 255, 255));
        btnD8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnD8ActionPerformed(evt);
            }
        });
        jPanel4.add(btnD8);

        btnD9.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btnD9.setForeground(new java.awt.Color(255, 255, 255));
        btnD9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnD9ActionPerformed(evt);
            }
        });
        jPanel4.add(btnD9);

        btnD0.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btnD0.setForeground(new java.awt.Color(255, 255, 255));
        btnD0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnD0ActionPerformed(evt);
            }
        });
        jPanel4.add(btnD0);
        jPanel4.add(jPanel2);

        btnLimpiar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnLimpiar.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });
        jPanel4.add(btnLimpiar);

        jPanel1.add(jPanel4);
        jPanel4.setBounds(20, 340, 210, 220);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel5.setLayout(null);

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Usuario:");
        jPanel5.add(jLabel2);
        jLabel2.setBounds(0, 17, 100, 20);

        txtUsuario.setBackground(new java.awt.Color(255, 255, 204));
        txtUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtUsuario.setText("admin");
        txtUsuario.setToolTipText("");
        txtUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioActionPerformed(evt);
            }
        });
        jPanel5.add(txtUsuario);
        txtUsuario.setBounds(110, 10, 170, 30);

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Constraseña:");
        jPanel5.add(jLabel3);
        jLabel3.setBounds(0, 57, 100, 20);

        txtContraseña.setBackground(new java.awt.Color(255, 255, 204));
        txtContraseña.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtContraseña.setText("123");
        txtContraseña.setEnabled(false);
        jPanel5.add(txtContraseña);
        txtContraseña.setBounds(110, 50, 170, 30);

        cboCargo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cboCargo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel5.add(cboCargo);
        cboCargo.setBounds(110, 90, 170, 30);

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Cargo:");
        jPanel5.add(jLabel4);
        jLabel4.setBounds(0, 97, 100, 20);

        btnIniciar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnIniciar.setForeground(new java.awt.Color(255, 255, 255));
        btnIniciar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/iniciar.png"))); // NOI18N
        btnIniciar.setText("Iniciar");
        btnIniciar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIniciar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarActionPerformed(evt);
            }
        });
        jPanel5.add(btnIniciar);
        btnIniciar.setBounds(60, 130, 100, 80);

        btnCancelar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/cancelar2.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancelar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        jPanel5.add(btnCancelar);
        btnCancelar.setBounds(180, 130, 100, 80);

        jPanel1.add(jPanel5);
        jPanel5.setBounds(240, 340, 290, 220);

        image.setBackground(new java.awt.Color(204, 204, 0));
        jPanel1.add(image);
        image.setBounds(130, 60, 290, 270);

        lblIntentos.setFont(new java.awt.Font("Arial", 2, 14)); // NOI18N
        lblIntentos.setForeground(new java.awt.Color(255, 255, 255));
        lblIntentos.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lblIntentos.setOpaque(true);
        jPanel1.add(lblIntentos);
        lblIntentos.setBounds(170, 570, 250, 20);

        panelRight.add(jPanel1, new java.awt.GridBagConstraints());

        getContentPane().add(panelRight);

        setSize(new java.awt.Dimension(1203, 682));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsuarioActionPerformed
        txtContraseña.requestFocus();
    }//GEN-LAST:event_txtUsuarioActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarActionPerformed
        String algorithm="SHA-512";  
        String resultado1,resultado2;
        String usuario=null;
        String contraseña=null;
        String descripcion=null;
        int sen=1;int tu=1;
        
        usuario=txtUsuario.getText();
        contraseña=txtContraseña.getText();
        descripcion= String.valueOf(cboCargo.getSelectedItem());
        resultado1=getStringMessageDigest(contraseña,algorithm);   
       
        boolean encuentra=false;
        
        if(usuario.length()==0 ||contraseña.length()==0) {
            JOptionPane.showMessageDialog(this, "!Ingrese Usuario y/o Constraseña!");
            txtUsuario.setText("");
            txtContraseña.setText("");
            txtUsuario.requestFocus();
            intentos=intentos+1;
            lblIntentos.setText(Integer.toString(intentos));

        }
        else 
        {
            try{
               ClsEmpleado oUsuario=new ClsEmpleado();
                
               rs= oUsuario.LoginEmpleados(usuario,resultado1,descripcion);
           
               while (rs.next()&& sen==1) {
                    if(!rs.getString(14).equals("")){
                        if(rs.getString(14).equals(usuario)&&rs.getString(15).equals(resultado1)&&rs.getString(16).equals(descripcion)) { 
                            if(rs.getString(13).equals("ACTIVO")) { 
//                                JOptionPane.showMessageDialog(null,"Bienvenidos: Sr."+rs.getString(2)+" "+rs.getString(3),"Login Usuarios",1);
                                Toast.makeText("Bienvenido...", Toast.LENGTH_LONG).show();
                                sen=2;
                            }else{
                                JOptionPane.showMessageDialog(this, "Usuario Inactivo");
                                sen=3;
                            }     
                       }
                    }else{
                        JOptionPane.showMessageDialog(this, "Usuario no existe");
                        sen=3;
                    }
                        
                        

                   encuentra=true;   
                   break;
                }
               
  
                if(sen==1) {
                    JOptionPane.showMessageDialog(this, "¡Usuario y/o Contraseña incorrectos!");
                    txtUsuario.setText("");
                    txtContraseña.setText("");
                    txtUsuario.requestFocus();
                    intentos=intentos+1;
                    lblIntentos.setText(Integer.toString(intentos));
                    
                } else if(sen==3){
                    txtUsuario.setText("");
                    txtContraseña.setText("");
                    txtUsuario.requestFocus();
                }else{

                    FrmPrincipal mdi=new FrmPrincipal();
                    mdi.show();
                    this.dispose();                    
                    mdi.strUsuario=rs.getString(14);
                    mdi.strIdEmpleado=rs.getString(1);
                    mdi.strNombreEmpleado = rs.getString(2) +" "+rs.getString(3);
                    mdi.strTipo=cboCargo.getSelectedItem().toString();
                    mdi.intEstado=1; 
                    
                    
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,ex.getMessage());
                System.out.println(ex.getMessage());
            }
        }
        
        if(intentos==3){
            this.dispose();
        }
    }//GEN-LAST:event_btnIniciarActionPerformed

    private void btnD9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnD9ActionPerformed
        String btnueve = txtContraseña.getText() + btnD9.getText();
        txtContraseña.setText(btnueve);
    }//GEN-LAST:event_btnD9ActionPerformed

    private void btnD8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnD8ActionPerformed
        String btnocho = txtContraseña.getText() + btnD8.getText();
        txtContraseña.setText(btnocho);
    }//GEN-LAST:event_btnD8ActionPerformed

    private void btnD7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnD7ActionPerformed
        String btnsiete = txtContraseña.getText() + btnD7.getText();
        txtContraseña.setText(btnsiete);
    }//GEN-LAST:event_btnD7ActionPerformed

    private void btnD0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnD0ActionPerformed
        String btncero = txtContraseña.getText() + btnD0.getText();
        txtContraseña.setText(btncero);
    }//GEN-LAST:event_btnD0ActionPerformed

    private void btnD4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnD4ActionPerformed
        String btncuatro = txtContraseña.getText() + btnD4.getText();
        txtContraseña.setText(btncuatro);
    }//GEN-LAST:event_btnD4ActionPerformed

    private void btnD5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnD5ActionPerformed
        String btncinco = txtContraseña.getText() + btnD5.getText();
        txtContraseña.setText(btncinco);
    }//GEN-LAST:event_btnD5ActionPerformed

    private void btnD2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnD2ActionPerformed
        String btndos = txtContraseña.getText() + btnD2.getText();
        txtContraseña.setText(btndos);
    }//GEN-LAST:event_btnD2ActionPerformed

    private void btnD3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnD3ActionPerformed
        String btntres = txtContraseña.getText() + btnD3.getText();
        txtContraseña.setText(btntres);
    }//GEN-LAST:event_btnD3ActionPerformed

    private void btnD6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnD6ActionPerformed
        String btnsies = txtContraseña.getText() + btnD6.getText();
        txtContraseña.setText(btnsies);
    }//GEN-LAST:event_btnD6ActionPerformed

    private void btnD1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnD1ActionPerformed
        String btnuno = txtContraseña.getText()+ btnD1.getText();
        txtContraseña.setText(btnuno);
        
    }//GEN-LAST:event_btnD1ActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
       txtContraseña.setText("");
    }//GEN-LAST:event_btnLimpiarActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//   
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
//            java.util.logging.Logger.getLogger(FrmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(FrmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(FrmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(FrmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//         Presentacion.FrmLogin f = new Presentacion.FrmLogin();
//         try {
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//
//            SwingUtilities.updateComponentTreeUI(f);
//            f.setVisible(true);
//            //new FrmLogin().setVisible(true);
//        } catch (UnsupportedLookAndFeelException ex) {}
//          catch (ClassNotFoundException ex) {}
//          catch (InstantiationException ex) {}
//          catch (IllegalAccessException ex) {}
//            }
//        });
//        
//    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnD0;
    private javax.swing.JButton btnD1;
    private javax.swing.JButton btnD2;
    private javax.swing.JButton btnD3;
    private javax.swing.JButton btnD4;
    private javax.swing.JButton btnD5;
    private javax.swing.JButton btnD6;
    private javax.swing.JButton btnD7;
    private javax.swing.JButton btnD8;
    private javax.swing.JButton btnD9;
    private javax.swing.JButton btnIniciar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JComboBox cboCargo;
    private javax.swing.JLabel image;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel lblIntentos;
    private javax.swing.JPanel panelLeft;
    private javax.swing.JPanel panelRight;
    private javax.swing.JLabel storeIcon;
    private javax.swing.JPasswordField txtContraseña;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
