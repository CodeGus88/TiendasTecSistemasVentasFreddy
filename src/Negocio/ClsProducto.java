/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;

import Conexion.*;
import Entidad.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class ClsProducto {
private Connection connection=new ClsConexion().getConection();
    //--------------------------------------------------------------------------------------------------
    //-----------------------------------------METODOS--------------------------------------------------
    //-------------------------------------------------------------------------------------------------- 
    public void agregarProducto(ClsEntidadProducto producto){
        try{
            CallableStatement statement=connection.prepareCall("{call SP_I_Producto(?,?,?,?,?,?,?,?,?,?,?,?)}");
            statement.setString("pcodigo",producto.getStrCodigoProducto());
            statement.setString("pnombre",producto.getStrNombreProducto());
            statement.setString("pdescripcion",producto.getStrDescripcionProducto());
            statement.setString("pstock",producto.getStrStockProducto());
            statement.setString("pstockmin",producto.getStrStockMinProducto());
            statement.setString("ppreciocosto",producto.getStrPrecioCostoProducto());
            statement.setString("pprecioventa",producto.getStrPrecioVentaProducto());
            statement.setString("putilidad",producto.getStrUtilidadProducto());
            statement.setString("pestado",producto.getStrEstadoProducto());
            statement.setString("pidcategoria",producto.getStrIdCategoria());
            statement.setString("pimagen",producto.getStrImagen());
            if(producto.getFechaVencimiento() != null)
                statement.setDate("pfechavencimiento",new java.sql.Date(producto.getFechaVencimiento().getTime()));
            else
                statement.setDate("pfechavencimiento", null);
            statement.execute();
            
            System.out.println("Producto: " + producto.getStrNombreProducto());

            JOptionPane.showMessageDialog(null,"¡Producto Agregado con éxito!","Mensaje del Sistema",1);
            
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        
    }    
    public void modificarProducto(String codigo,ClsEntidadProducto producto){
        try{
            CallableStatement statement=connection.prepareCall("{call SP_U_Producto(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            statement.setString("pidproducto",codigo);
            statement.setString("pcodigo",producto.getStrCodigoProducto());
            statement.setString("pnombre",producto.getStrNombreProducto());
            statement.setString("pdescripcion",producto.getStrDescripcionProducto());
            statement.setString("pstock",producto.getStrStockProducto());
            statement.setString("pstockmin",producto.getStrStockMinProducto());
            statement.setString("ppreciocosto",producto.getStrPrecioCostoProducto());
            statement.setString("pprecioventa",producto.getStrPrecioVentaProducto());
            statement.setString("putilidad",producto.getStrUtilidadProducto());
            statement.setString("pestado",producto.getStrEstadoProducto());
            statement.setString("pidcategoria",producto.getStrIdCategoria());           
            statement.setString("pimagen",producto.getStrImagen());
            if (producto.getFechaVencimiento() != null)
                statement.setDate("pfechavencimiento", new java.sql.Date(producto.getFechaVencimiento().getTime()));
            else
                statement.setDate("pfechavencimiento", null);
            statement.executeUpdate();
            
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        JOptionPane.showMessageDialog(null,"¡Producto Actualizado!","Mensaje del Sistema",1);
    }
    public void actualizarProductoStock(String codigo,ClsEntidadProducto Producto){
        try{
            CallableStatement statement=connection.prepareCall("{call SP_U_ActualizarProductoStock(?,?)}");
            statement.setString("pidproducto",codigo);
            statement.setString("pstock",Producto.getStrStockProducto());        
            statement.executeUpdate();
            
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    public ArrayList<ClsEntidadProducto> listarProducto(){
        ArrayList<ClsEntidadProducto> productos=new ArrayList<ClsEntidadProducto>();
        try{
            CallableStatement statement=connection.prepareCall("{call SP_S_Producto}");
            ResultSet resultSet=statement.executeQuery();
            
            while (resultSet.next()){
                ClsEntidadProducto producto=new ClsEntidadProducto();
                producto.setStrIdProducto(resultSet.getString("IdProducto"));
                producto.setStrCodigoProducto(resultSet.getString("Codigo"));
                producto.setStrNombreProducto(resultSet.getString("Nombre"));
                producto.setStrDescripcionProducto(resultSet.getString("Descripcion"));
                producto.setStrStockProducto(resultSet.getString("Stock"));
                producto.setStrStockMinProducto(resultSet.getString("StockMin"));
                producto.setStrPrecioCostoProducto(resultSet.getString("PrecioCosto"));
                producto.setStrPrecioVentaProducto(resultSet.getString("PrecioVenta"));
                producto.setStrUtilidadProducto(resultSet.getString("Utilidad"));
                producto.setStrEstadoProducto(resultSet.getString("Estado"));
                producto.setStrDescripcionCategoria(resultSet.getString("categoria"));
                producto.setStrImagen(resultSet.getString("imagen"));
                producto.setFechaVencimiento(resultSet.getDate("FechaVencimiento"));
                productos.add(producto);
            }
            return productos;
         }catch(SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }
    public ArrayList<ClsEntidadProducto> listarProductoActivo(){
        ArrayList<ClsEntidadProducto> productos=new ArrayList<ClsEntidadProducto>();
        try{
            CallableStatement statement=connection.prepareCall("{call SP_S_ProductoActivo}");
            ResultSet resultSet=statement.executeQuery();
            
            while (resultSet.next()){
                ClsEntidadProducto producto=new ClsEntidadProducto();
                producto.setStrIdProducto(resultSet.getString("IdProducto"));
                producto.setStrCodigoProducto(resultSet.getString("Codigo"));
                producto.setStrNombreProducto(resultSet.getString("Nombre"));
                producto.setStrDescripcionProducto(resultSet.getString("Descripcion"));
                producto.setStrStockProducto(resultSet.getString("Stock"));
                producto.setStrStockMinProducto(resultSet.getString("StockMin"));
                producto.setStrPrecioCostoProducto(resultSet.getString("PrecioCosto"));
                producto.setStrPrecioVentaProducto(resultSet.getString("PrecioVenta"));
                producto.setStrUtilidadProducto(resultSet.getString("Utilidad"));
                producto.setStrEstadoProducto(resultSet.getString("Estado"));
                producto.setStrDescripcionCategoria(resultSet.getString("categoria"));
                producto.setStrImagen(resultSet.getString("imagen"));
                producto.setFechaVencimiento(resultSet.getDate("FechaVencimiento"));
                productos.add(producto);
            }
            return productos;
         }catch(SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }
    public ResultSet listarProductoPorParametro(String criterio, String busqueda) throws Exception{
        ResultSet rs = null;
        try{
            CallableStatement statement = connection.prepareCall("{call SP_S_ProductoPorParametro(?,?)}");
            statement.setString("pcriterio", criterio);
            statement.setString("pbusqueda", busqueda);
            rs = statement.executeQuery();
            return rs;
        }catch(SQLException SQLex){
            throw SQLex;            
        }        
    }
    public ResultSet listarProductoActivoPorParametro(String criterio, String busqueda) throws Exception{
        ResultSet rs = null;
        try{
            CallableStatement statement = connection.prepareCall("{call SP_S_ProductoActivoPorParametro(?,?)}");
            statement.setString("pcriterio", criterio);
            statement.setString("pbusqueda", busqueda);
            rs = statement.executeQuery();
            return rs;
        }catch(SQLException SQLex){
            throw SQLex;            
        }        
    }
    
    public ResultSet consultaStock() throws Exception{
        ResultSet rs = null;
        try{
            CallableStatement statement = connection.prepareCall("{call SP_S_ConsultaStock()}");
            rs = statement.executeQuery();
            return rs;
        }catch(SQLException SQLex){
            throw SQLex;            
        }        
    }
    
    
     public ResultSet kardexValorizado() throws Exception{
        ResultSet rs = null;
        try{
            CallableStatement statement = connection.prepareCall("{call SP_S_KardexValorizado()}");
            rs = statement.executeQuery();
            return rs;
        }catch(SQLException SQLex){
            throw SQLex;            
        }        
    }
    
    
    
    public ResultSet verificarCodigoBar(String busqueda) throws Exception{
        ResultSet rs = null;
        try{
            CallableStatement statement = connection.prepareCall("{call SP_S_ProductoVerificarCodigoBar(?)}");
            statement.setString("pbusqueda", busqueda);
            rs = statement.executeQuery();
            return rs;
        }catch(SQLException SQLex){
            throw SQLex;            
        }        
    }
}
