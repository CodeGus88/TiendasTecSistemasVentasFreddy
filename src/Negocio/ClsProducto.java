/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;

import Conexion.*;
import Entidad.*;
import Entidad.dtos.ProductDto;
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

public class ClsProducto {
    
    private Connection connection=new ClsConexion().getConection();

    public void agregarProducto(ClsEntidadProducto product){
        try{
            CallableStatement statement = connection.prepareCall("{call SP_I_Producto(?,?,?,?,?,?,?,?,?,?,?,?)}");
            statement.setString("pcodigo",product.getStrCodigoProducto());
            statement.setString("pnombre",product.getStrNombreProducto());
            statement.setString("pdescripcion",product.getStrDescripcionProducto());
            statement.setString("pstock",product.getStrStockProducto());
            statement.setString("pstockmin",product.getStrStockMinProducto());
            statement.setString("ppreciocosto",product.getStrPrecioCostoProducto());
            statement.setString("pprecioventa",product.getStrPrecioVentaProducto());
            statement.setString("putilidad",product.getStrUtilidadProducto());
            statement.setString("pestado",product.getStrEstadoProducto());
            statement.setString("pidcategoria",product.getStrIdCategoria());
            statement.setBlob("pimagen", product.getImagen().getFileInputStream(), product.getImagen().getByteLength());
            if(product.getFechaVencimiento() != null)
                statement.setDate("pfechavencimiento", new java.sql.Date(product.getFechaVencimiento().getTime()));
            else
                statement.setDate("pfechavencimiento", null);
            statement.execute();
            
            System.out.println("Producto: " + product.getStrNombreProducto());

            JOptionPane.showMessageDialog(null,"¡Producto Agregado con éxito!","Mensaje del Sistema",1);
            
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }    
    public void modificarProducto(String codigo,ClsEntidadProducto product){
        try{
            CallableStatement statement;
            if (product.getImagen() == null) {
                statement = connection.prepareCall("{call SP_U_Producto(?,?,?,?,?,?,?,?,?,?,?,?)}");
            } else {
                statement = connection.prepareCall("{call 003_SP_U_Producto(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
                if (product.getImagen().getFileInputStream() != null) {
                    statement.setBlob("pimagen", product.getImagen().getFileInputStream(), product.getImagen().getByteLength());
                } else {
                    statement.setBlob("pimagen", null, 0);
                }
            }
            statement.setString("pidproducto",codigo);
            statement.setString("pcodigo",product.getStrCodigoProducto());
            statement.setString("pnombre",product.getStrNombreProducto());
            statement.setString("pdescripcion",product.getStrDescripcionProducto());
            statement.setString("pstock",product.getStrStockProducto());
            statement.setString("pstockmin",product.getStrStockMinProducto());
            statement.setString("ppreciocosto",product.getStrPrecioCostoProducto());
            statement.setString("pprecioventa",product.getStrPrecioVentaProducto());
            statement.setString("putilidad",product.getStrUtilidadProducto());
            statement.setString("pestado",product.getStrEstadoProducto());
            statement.setString("pidcategoria",product.getStrIdCategoria());  
            if (product.getFechaVencimiento() != null)
                statement.setDate("pfechavencimiento", new java.sql.Date(product.getFechaVencimiento().getTime()));
            else
                statement.setDate("pfechavencimiento", null);
            statement.executeUpdate();
            
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        JOptionPane.showMessageDialog(null,"¡Producto Actualizado!","Mensaje del Sistema",1);
    }
    
    public ProductDto findById(int id){
        try {
            CallableStatement statement = connection.prepareCall("{call 003_Find_By_Id_Product(?)}");
            statement.setInt("pid", id);
            ResultSet resultSet = statement.executeQuery();
            ProductDto productDto = null;
            while(resultSet.next()){
                productDto = new ProductDto();
                productDto.setId(resultSet.getInt("IdProducto"));
                productDto.setCode(resultSet.getString("Codigo"));
                productDto.setName(resultSet.getString("Nombre"));
                productDto.setDescription(resultSet.getString("Descripcion"));
                productDto.setStock(resultSet.getString("Stock"));
                productDto.setMinStock(resultSet.getString("StockMin"));
                productDto.setCoste(resultSet.getString("PrecioCosto"));
                productDto.setPrice(resultSet.getString("PrecioVenta"));
                productDto.setUtility(resultSet.getString("Utilidad"));
                productDto.setState(resultSet.getString("Estado"));
                productDto.setCategoryId(resultSet.getInt("IdCategoria"));
                productDto.setCategory(resultSet.getString("NombreCategoria"));
                productDto.setCategory(resultSet.getString("NombreCategoria"));
                productDto.setExpiration(resultSet.getDate("FechaVencimiento"));
                Blob blob = resultSet.getBlob("Imagen");
                if (blob != null) {
                    byte[] data = blob.getBytes(1, (int) blob.length());
                    BufferedImage img = null;
                    try {
                        img = ImageIO.read(new ByteArrayInputStream(data));
                    } catch (IOException e) {
                        e.printStackTrace();
                        Message.LOGGER.log(Level.SEVERE, e.getMessage());
                    }
                    if(img != null){
                        ImageIcon icon = new ImageIcon(img);
                        productDto.setImageIcon(icon);
                    }
                }
            }
            return productDto;
        } catch (Exception e) {
            e.printStackTrace();
            Message.LOGGER.log(Level.SEVERE, e.getMessage());
            return null;
        }
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
//                producto.setStrImagen(resultSet.getString("imagen"));
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
