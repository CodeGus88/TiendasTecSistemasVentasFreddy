/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;

import Conexion.*;
import Entidad.*;
import Entidad.dtos.ProductDto;
import Entidad.dtos.ProductItemDto;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import statics.Message;

public class ClsProducto {
    
    private Connection connection=new ClsConexion().getConection();

    public void agregarProducto(ClsEntidadProducto product){
        try{
            CallableStatement statement = connection.prepareCall("{call SP_I_Producto(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            statement.setString("pcodigo",product.getCodigoProducto());
            statement.setString("pnombre",product.getNombreProducto());
            statement.setString("pdescripcion",product.getDescripcionProducto());
            statement.setString("pstock",product.getStockProducto());
            statement.setString("pstockmin",product.getStockMinProducto());
            statement.setString("ppreciocosto",product.getPrecioCostoProducto());
            statement.setDouble("pcostochica",product.getCostoChica());
            statement.setString("pprecioventa",product.getPrecioVentaProducto());
            statement.setString("putilidad",product.getUtilidadProducto());
            statement.setString("pestado",product.getEstadoProducto());
            statement.setString("pidcategoria",product.getIdCategoria());
            statement.setBlob("pimagen", product.getImagen().getFileInputStream(), product.getImagen().getByteLength());
            if(product.getFechaVencimiento() != null)
                statement.setDate("pfechavencimiento", new java.sql.Date(product.getFechaVencimiento().getTime()));
            else
                statement.setDate("pfechavencimiento", null);
            statement.execute();
        }catch(SQLException e){
            Message.LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }    
    public void modificarProducto(String codigo,ClsEntidadProducto product){
        try{
            CallableStatement statement;
            if (product.getImagen() == null) {
                statement = connection.prepareCall("{call SP_U_Producto(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            } else {
                statement = connection.prepareCall("{call 003_SP_U_Producto(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
                if (product.getImagen().getFileInputStream() != null) {
                    statement.setBlob("pimagen", product.getImagen().getFileInputStream(), product.getImagen().getByteLength());
                } else {
                    statement.setBlob("pimagen", null, 0);
                }
            }
            statement.setString("pidproducto",codigo);
            statement.setString("pcodigo",product.getCodigoProducto());
            statement.setString("pnombre",product.getNombreProducto());
            statement.setString("pdescripcion",product.getDescripcionProducto());
            statement.setString("pstock",product.getStockProducto());
            statement.setString("pstockmin",product.getStockMinProducto());
            statement.setString("ppreciocosto",product.getPrecioCostoProducto());
            statement.setDouble("pcostochica", product.getCostoChica()); // add
            statement.setString("pprecioventa",product.getPrecioVentaProducto());
            statement.setString("putilidad",product.getUtilidadProducto());
            statement.setString("pestado",product.getEstadoProducto());
            statement.setString("pidcategoria",product.getIdCategoria());  
            if (product.getFechaVencimiento() != null)
                statement.setDate("pfechavencimiento", new java.sql.Date(product.getFechaVencimiento().getTime()));
            else
                statement.setDate("pfechavencimiento", null);
            statement.executeUpdate();
            
        }catch(SQLException e){
            Message.LOGGER.log(Level.SEVERE, e.getMessage());
        }
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
                productDto.setGirlCost(resultSet.getString("CostoChica"));
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
            Message.LOGGER.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }
    
    public void actualizarProductoStock(String codigo, ClsEntidadProducto Producto){
        try{
            CallableStatement statement=connection.prepareCall("{call SP_U_ActualizarProductoStock(?,?)}");
            statement.setString("pidproducto",codigo);
            statement.setString("pstock",Producto.getStockProducto());        
            statement.executeUpdate();
        }catch(SQLException e){
            Message.LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }
    
    public ArrayList<ClsEntidadProducto> listarProducto(){
        ArrayList<ClsEntidadProducto> productos=new ArrayList<ClsEntidadProducto>();
        try{
            CallableStatement statement=connection.prepareCall("{call SP_S_Producto}");
            ResultSet resultSet=statement.executeQuery();
            while (resultSet.next()){
                ClsEntidadProducto producto=new ClsEntidadProducto();
                producto.setIdProducto(resultSet.getString("IdProducto"));
                producto.setCodigoProducto(resultSet.getString("Codigo"));
                producto.setNombreProducto(resultSet.getString("Nombre"));
                producto.setDescripcionProducto(resultSet.getString("Descripcion"));
                producto.setStockProducto(resultSet.getString("Stock"));
                producto.setStockMinProducto(resultSet.getString("StockMin"));
                producto.setPrecioCostoProducto(resultSet.getString("PrecioCosto"));
                producto.setCostoChica(Double.parseDouble(resultSet.getString("CostoChica")));
                producto.setPrecioVentaProducto(resultSet.getString("PrecioVenta"));
                producto.setUtilidadProducto(resultSet.getString("Utilidad"));
                producto.setEstadoProducto(resultSet.getString("Estado"));
                producto.setDescripcionCategoria(resultSet.getString("categoria"));
                producto.setFechaVencimiento(resultSet.getDate("FechaVencimiento"));
                productos.add(producto);
            }
            return productos;
         }catch(SQLException e){
             Message.LOGGER.log(Level.SEVERE, e.getMessage());
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
                producto.setIdProducto(resultSet.getString("IdProducto"));
                producto.setCodigoProducto(resultSet.getString("Codigo"));
                producto.setNombreProducto(resultSet.getString("Nombre"));
                producto.setDescripcionProducto(resultSet.getString("Descripcion"));
                producto.setStockProducto(resultSet.getString("Stock"));
                producto.setStockMinProducto(resultSet.getString("StockMin"));
                producto.setPrecioCostoProducto(resultSet.getString("PrecioCosto"));
                producto.setPrecioVentaProducto(resultSet.getString("PrecioVenta"));
                producto.setUtilidadProducto(resultSet.getString("Utilidad"));
                producto.setEstadoProducto(resultSet.getString("Estado"));
                producto.setDescripcionCategoria(resultSet.getString("categoria"));
//                producto.setStrImagen(resultSet.getString("imagen"));
                producto.setFechaVencimiento(resultSet.getDate("FechaVencimiento"));
                productos.add(producto);
            }
            return productos;
         }catch(SQLException e){
            Message.LOGGER.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }
    public List<ProductItemDto> listarProductoPorParametro(String criterio, String busqueda) throws Exception{
        ResultSet rs = null;
//        p.IdProducto,p.Codigo,p.Nombre,p.Descripcion,p.Stock,p.StockMin,p.PrecioCosto,p.CostoChica,p.PrecioVenta,p.Utilidad,p.Estado
//        ,c.Descripcion AS Categoria, p.FechaVencimiento
        try{
            CallableStatement statement = connection.prepareCall("{call SP_S_ProductoPorParametro(?,?)}");
            statement.setString("pcriterio", criterio);
            statement.setString("pbusqueda", busqueda);
            rs = statement.executeQuery();
            List<ProductItemDto> list = new ArrayList<>();
            while(rs.next()){
                ProductItemDto productItemDto = new ProductItemDto();
                productItemDto.setId(rs.getInt("IdProducto"));
                productItemDto.setCode(rs.getString("Codigo"));
                productItemDto.setName(rs.getString("Nombre"));
                productItemDto.setDescription(rs.getString("Descripcion"));
                productItemDto.setStock(rs.getString("Stock"));
                productItemDto.setMinStock(rs.getString("StockMin"));
                productItemDto.setCoste(rs.getString("PrecioCosto"));
                productItemDto.setGirlCost(rs.getString("CostoChica"));
                productItemDto.setPrice(rs.getString("PrecioVenta"));
                productItemDto.setUtility(rs.getString("Utilidad"));
                productItemDto.setState(rs.getString("Estado"));
                productItemDto.setCategory(rs.getString("Categoria"));
                productItemDto.setExpiration(rs.getDate("FechaVencimiento"));
                list.add(productItemDto);
            }
            return list;
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
