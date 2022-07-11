
package interfaces;

import Entidad.ClsEntidadVenta;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Gustavo
 */
public interface VentaInterface {
    
    void agregarVenta(ClsEntidadVenta venta);
    
    void modificarVenta(String codigo,ClsEntidadVenta venta);
    
    ArrayList<ClsEntidadVenta> listarVenta();
    
    ResultSet listarVentaPorParametro(String criterio, String busqueda) throws Exception;
    
    ResultSet listarVentaPorFecha(String criterio,Date fechaini, Date fechafin, String doc) throws Exception;
    
    void actualizarVentaEstado(String codigo,ClsEntidadVenta Venta);
    
    ResultSet obtenerUltimoIdVenta() throws Exception;
    
    ResultSet listarVentaPorDetalle(String criterio,Date fechaini, Date fechafin) throws Exception;
    
    ResultSet listarVentaMensual(String criterio,String fecha_ini,String fecha_fin) throws Exception;
}
