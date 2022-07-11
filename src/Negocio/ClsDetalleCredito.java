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

public class ClsDetalleCredito {
private Connection connection=new ClsConexion().getConection();
    //--------------------------------------------------------------------------------------------------
    //-----------------------------------------METODOS--------------------------------------------------
    //-------------------------------------------------------------------------------------------------- 
    public void agregarDetalleCredito(ClsEntidadDetalleCredito DetalleCredito){
        try{
            CallableStatement statement=connection.prepareCall("{call 001_SP_I_DetalleCredito(?,?,?,?,?,?)}");
            statement.setInt("pidcredito",Integer.parseInt(DetalleCredito.getStrIdCredito()));
            statement.setInt("pidproducto",Integer.parseInt(DetalleCredito.getStrIdProducto()));
            statement.setString("pcantidad",DetalleCredito.getStrCantidadDet());
            statement.setString("pcosto",DetalleCredito.getStrCostoDet());
            statement.setString("pprecio",DetalleCredito.getStrPrecioDet());
            statement.setString("ptotal",DetalleCredito.getStrTotalDet());
            statement.execute();

        }catch(SQLException ex){
            ex.printStackTrace();
        }
        
    }    
    public void modificarDetalleCredito(String codigo,ClsEntidadDetalleCredito DetalleCredito){
        try{
            CallableStatement statement=connection.prepareCall("{call 001_SP_U_DetalleCredito(?,?,?,?,?,?)}");
            statement.setString("pidcredito",codigo);
            statement.setString("pidproducto",DetalleCredito.getStrIdProducto());
            statement.setString("pcantidad",DetalleCredito.getStrCantidadDet());
            statement.setString("pcosto",DetalleCredito.getStrCostoDet());
            statement.setString("pprecio",DetalleCredito.getStrPrecioDet());
            statement.setString("ptotal",DetalleCredito.getStrTotalDet());
            statement.executeUpdate();
            
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    public ResultSet listarDetalleCreditoPorParametro(String criterio, String busqueda) throws Exception{
        ResultSet rs = null;
        try{
            CallableStatement statement = connection.prepareCall("{call 001_SP_S_DetalleCreditoPorParametro(?,?)}");
            statement.setString("pcriterio", criterio);
            statement.setString("pbusqueda", busqueda);
            rs = statement.executeQuery();
            return rs;
        }catch(SQLException SQLex){
            throw SQLex;            
        }        
    }  

     
}
