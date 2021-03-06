/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;

import Conexion.*;
import Entidad.*;
import java.awt.List;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class ClsCliente {
private Connection connection=new ClsConexion().getConection();
    //--------------------------------------------------------------------------------------------------
    //-----------------------------------------METODOS--------------------------------------------------
    //-------------------------------------------------------------------------------------------------- 
    public void agregarCliente(ClsEntidadCliente Cliente){
        try{
            CallableStatement statement=connection.prepareCall("{call SP_I_Cliente(?,?,?,?,?,?)}");
            statement.setString("pnombre",Cliente.getStrNombreCliente());
            statement.setString("pruc",Cliente.getStrRucCliente());
            statement.setString("pdni",Cliente.getStrDniCliente());
            statement.setString("pdireccion",Cliente.getStrDireccionCliente());
            statement.setString("ptelefono",Cliente.getStrTelefonoCliente());
            statement.setString("pobsv",Cliente.getStrObsvCliente());
            statement.execute();

            JOptionPane.showMessageDialog(null,"¡Cliente Agregado con éxito!","Mensaje del Sistema",1);           

        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    /**
     * Agrega un nuevo cliente y retorna el cliente con el id que se leasignó
     * @param cliente
     * @return cliente
     */
     public ClsEntidadCliente agregarClienteObtenerIdAsignado(ClsEntidadCliente cliente){
        try{
            CallableStatement statement=connection.prepareCall("{call SP_I_Cliente(?,?,?,?,?,?)}");
            statement.setString("pnombre",cliente.getStrNombreCliente());
            statement.setString("pruc",cliente.getStrRucCliente());
            statement.setString("pdni",cliente.getStrDniCliente());
            statement.setString("pdireccion",cliente.getStrDireccionCliente());
            statement.setString("ptelefono",cliente.getStrTelefonoCliente());
            statement.setString("pobsv",cliente.getStrObsvCliente());
            cliente.setStrIdCliente("0");
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()){
                cliente.setStrIdCliente(resultSet.getString("clienteId"));
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        
        return cliente;
    }

    public void modificarCliente(String codigo,ClsEntidadCliente Cliente){
        try{
            CallableStatement statement=connection.prepareCall("{call SP_U_Cliente(?,?,?,?,?,?,?)}");
            statement.setString("pidcliente",codigo);
            statement.setString("pnombre",Cliente.getStrNombreCliente());
            statement.setString("pruc",Cliente.getStrRucCliente());
            statement.setString("pdni",Cliente.getStrDniCliente());
            statement.setString("pdireccion",Cliente.getStrDireccionCliente());
            statement.setString("ptelefono",Cliente.getStrTelefonoCliente());
            statement.setString("pobsv",Cliente.getStrObsvCliente());
            statement.executeUpdate();
            
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        JOptionPane.showMessageDialog(null,"¡Cliente Actualizado!","Mensaje del Sistema",1);
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
