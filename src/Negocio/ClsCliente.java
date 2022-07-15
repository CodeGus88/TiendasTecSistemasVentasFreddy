/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;

import Conexion.*;
import Entidad.*;
import Entidad.dtos.ClientDto;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import statics.Message;

public class ClsCliente {
    
private Connection connection=new ClsConexion().getConection();

    public void agregarCliente(ClsEntidadCliente client){
        try{
            CallableStatement statement=connection.prepareCall("{call SP_I_Cliente(?,?,?,?,?,?,?)}");
            statement.setString("pnombre",client.getStrNombreCliente());
            statement.setString("pruc",client.getStrRucCliente());
            statement.setString("pdni",client.getStrDniCliente());
            statement.setString("pdireccion",client.getStrDireccionCliente());
            statement.setString("ptelefono",client.getStrTelefonoCliente());
            statement.setString("pobsv",client.getStrObsvCliente());
            statement.setBlob("pimagen",client.getImagen().getFileInputStream(), client.getImagen().getByteLength());
            statement.execute();

            JOptionPane.showMessageDialog(null,"¡Cliente Agregado con éxito!","Mensaje del Sistema",1);           

        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
//    /**
//     * Agrega un nuevo cliente y retorna el cliente con el id que se leasignó
//     * @param cliente
//     * @return cliente
//     */
//     public ClsEntidadCliente agregarClienteObtenerIdAsignado(ClsEntidadCliente cliente){
//        try{
//            CallableStatement statement=connection.prepareCall("{call SP_I_Cliente(?,?,?,?,?,?)}");
//            statement.setString("pnombre",cliente.getStrNombreCliente());
//            statement.setString("pruc",cliente.getStrRucCliente());
//            statement.setString("pdni",cliente.getStrDniCliente());
//            statement.setString("pdireccion",cliente.getStrDireccionCliente());
//            statement.setString("ptelefono",cliente.getStrTelefonoCliente());
//            statement.setString("pobsv",cliente.getStrObsvCliente());
//            cliente.setStrIdCliente("0");
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()){
//                cliente.setStrIdCliente(resultSet.getString("clienteId"));
//            }
//        }catch(SQLException ex){
//            ex.printStackTrace();
//        }
//        return cliente;
//    }
     
    public void modificarCliente(String codigo,ClsEntidadCliente client){
        try{
            CallableStatement statement;
            if(client.getImagen() == null){
                statement = connection.prepareCall("{call SP_U_Cliente(?,?,?,?,?,?,?)}");
            }else{
                statement = connection.prepareCall("{call 002_SP_U_Cliente(?,?,?,?,?,?,?,?)}");
                if(client.getImagen().getFileInputStream() != null){
                    statement.setBlob("pimagen", client.getImagen().getFileInputStream(), client.getImagen().getByteLength());
                }else{
                    statement.setBlob("pimagen", null, 0);
                }
            }
            statement.setString("pidcliente",codigo);
            statement.setString("pnombre",client.getStrNombreCliente());
            statement.setString("pruc",client.getStrRucCliente());
            statement.setString("pdni",client.getStrDniCliente());
            statement.setString("pdireccion",client.getStrDireccionCliente());
            statement.setString("ptelefono",client.getStrTelefonoCliente());
            statement.setString("pobsv",client.getStrObsvCliente());
            statement.executeUpdate();
        }catch(SQLException ex){
            Message.LOGGER.log(Level.SEVERE, ex.getMessage());
        }
        JOptionPane.showMessageDialog(null,"¡Cliente Actualizado!","Mensaje del Sistema",1);
    }
    
    /**
     * Consulta un cliente o garzón
     *
     * @param id
     * @return clientDto
     */
    public ClientDto findById(int id) {
        try {
            CallableStatement statement = connection.prepareCall("{call 002_Find_By_Id_Client(?)}");
            statement.setInt("pid", id);
            ResultSet resultSet = statement.executeQuery();
            ClientDto clientDto = null;
            while (resultSet.next()) {
                clientDto = new ClientDto();
                clientDto.setId(resultSet.getInt("IdCliente"));
                clientDto.setName(resultSet.getString("nombre"));
                clientDto.setCi(resultSet.getString("dni"));
                clientDto.setAddress(resultSet.getString("direccion"));
                clientDto.setPhone(resultSet.getString("telefono"));
                clientDto.setObservation(resultSet.getString("obsv"));
                Blob blob = resultSet.getBlob("imagen");
                if (blob != null) {
                    byte[] data = blob.getBytes(1, (int) blob.length());
                    BufferedImage img = null;
                    try {
                        img = ImageIO.read(new ByteArrayInputStream(data));
                    } catch (IOException e) {
                        Message.LOGGER.log(Level.SEVERE, e.getMessage());
                    }
                    ImageIcon icon = new ImageIcon(img);
                    clientDto.setImageIcon(icon);
                }
            }
            return clientDto;
        } catch (SQLException ex) {
            Message.LOGGER.log(Level.SEVERE, ex.getMessage());
            return null;
        }
    }

    
    public ArrayList<ClsEntidadCliente> listarCliente(){
        ArrayList<ClsEntidadCliente> clienteusuarios=new ArrayList<ClsEntidadCliente>();
        try{
            CallableStatement statement=connection.prepareCall("{call SP_S_Cliente}");
            ResultSet resultSet=statement.executeQuery();
            while (resultSet.next()){
                ClsEntidadCliente cliente=new ClsEntidadCliente();
                cliente.setStrIdCliente(resultSet.getString("IdCliente"));
                cliente.setStrNombreCliente(resultSet.getString("nombre"));
                cliente.setStrRucCliente(resultSet.getString("ruc"));
                cliente.setStrDniCliente(resultSet.getString("dni"));
                cliente.setStrDireccionCliente(resultSet.getString("direccion"));
                cliente.setStrTelefonoCliente(resultSet.getString("telefono"));
                cliente.setStrObsvCliente(resultSet.getString("obsv"));
                clienteusuarios.add(cliente);
            }
            return clienteusuarios;
         }catch(SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }      
    public ResultSet listarClientePorParametro(String criterio, String busqueda) throws Exception{
        ResultSet rs = null;
        try{
            CallableStatement statement = connection.prepareCall("{call SP_S_ClientePorParametro(?,?)}");
            statement.setString("pcriterio", criterio);
            statement.setString("pbusqueda", busqueda);
            rs = statement.executeQuery();
            return rs;
        }catch(SQLException SQLex){
            throw SQLex;            
        }        
    }
    
}
