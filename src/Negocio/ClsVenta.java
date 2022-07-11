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
import java.util.Date;
import java.util.logging.Level;
import statics.Message;

public class ClsVenta {

    private Connection connection = new ClsConexion().getConection();

    public boolean agregarVenta(ClsEntidadVenta venta) {
        try {
            CallableStatement statement = connection.prepareCall("{call SP_I_Venta(?,?,?,?,?,?,?,?,?,?,?,?)}");
            statement.setString("pidtipodocumento", venta.getStrIdTipoDocumento());
            statement.setString("pidcliente", venta.getStrIdCliente());
            statement.setString("pidempleado", venta.getStrIdEmpleado());
            statement.setString("pserie", venta.getStrSerieVenta());
            statement.setString("pnumero", venta.getStrNumeroVenta());
            statement.setDate("pfecha", new java.sql.Date(venta.getStrFechaVenta().getTime()));
            statement.setString("ptotalventa", venta.getStrTotalVenta());
            statement.setString("pdescuento", venta.getStrDescuentoVenta());
            statement.setString("psubtotal", venta.getStrSubTotalVenta());
            statement.setString("pigv", venta.getStrIgvVenta());
            statement.setString("ptotalpagar", venta.getStrTotalPagarVenta());
            statement.setString("pestado", venta.getStrEstadoVenta());
            statement.execute();
            return true;
        } catch (SQLException ex) {
            Message.LOGGER.log(Level.SEVERE, ex.getMessage());
            return false;
        }

    }

    public boolean modificarVenta(String codigo, ClsEntidadVenta venta) {
        try {
            CallableStatement statement = connection.prepareCall("{call SP_U_Venta(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            statement.setString("pidventa", codigo);
            statement.setString("pidtipodocumento", venta.getStrIdTipoDocumento());
            statement.setString("pidcliente", venta.getStrIdCliente());
            statement.setString("pidempleado", venta.getStrIdEmpleado());
            statement.setString("pserie", venta.getStrSerieVenta());
            statement.setString("pnumero", venta.getStrNumeroVenta());
            statement.setDate("pfecha", new java.sql.Date(venta.getStrFechaVenta().getTime()));
            statement.setString("ptotalventa", venta.getStrTotalVenta());
            statement.setString("pdescuento", venta.getStrDescuentoVenta());
            statement.setString("psubtotal", venta.getStrSubTotalVenta());
            statement.setString("pigv", venta.getStrIgvVenta());
            statement.setString("ptotalpagar", venta.getStrTotalPagarVenta());
            statement.setString("pestado", venta.getStrEstadoVenta());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Message.LOGGER.log(Level.SEVERE, ex.getMessage());
            return false;
        }
    }

    public ArrayList<ClsEntidadVenta> listarVenta() {
        ArrayList<ClsEntidadVenta> ventas = new ArrayList<ClsEntidadVenta>();
        try {
            CallableStatement statement = connection.prepareCall("{call SP_S_Venta}");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ClsEntidadVenta venta = new ClsEntidadVenta();
                venta.setStrIdVenta(resultSet.getString("IdVenta"));
                venta.setStrTipoDocumento(resultSet.getString("TipoDocumento"));
                venta.setStrCliente(resultSet.getString("Cliente"));
                venta.setStrEmpleado(resultSet.getString("Empleado"));
                venta.setStrSerieVenta(resultSet.getString("Serie"));
                venta.setStrNumeroVenta(resultSet.getString("Numero"));
                venta.setStrFechaVenta(resultSet.getDate("Fecha"));
                venta.setStrTotalVenta(resultSet.getString("TotalVenta"));
                venta.setStrDescuentoVenta(resultSet.getString("Descuento"));
                venta.setStrSubTotalVenta(resultSet.getString("SubTotal"));
                venta.setStrIgvVenta(resultSet.getString("Igv"));
                venta.setStrTotalPagarVenta(resultSet.getString("TotalPagar"));
                venta.setStrEstadoVenta(resultSet.getString("Estado"));

                ventas.add(venta);
            }
            return ventas;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ResultSet listarVentaPorParametro(String criterio, String busqueda) throws Exception {
        ResultSet rs = null;
        try {
            CallableStatement statement = connection.prepareCall("{call SP_S_VentaPorParametro(?,?)}");
            statement.setString("pcriterio", criterio);
            statement.setString("pbusqueda", busqueda);
            rs = statement.executeQuery();
            return rs;
        } catch (SQLException SQLex) {
            throw SQLex;
        }
    }

    public ResultSet obtenerUltimoIdVenta() throws Exception {
        ResultSet rs = null;
        try {
            CallableStatement statement = connection.prepareCall("{call SP_S_UltimoIdVenta()}");
            rs = statement.executeQuery();
            return rs;
        } catch (SQLException SQLex) {
            throw SQLex;
        }
    }

    public ResultSet listarVentaPorFecha(String criterio, Date fechaini, Date fechafin, String doc) throws Exception {
        ResultSet rs = null;
        try {
            CallableStatement statement = connection.prepareCall("{call SP_S_VentaPorFecha(?,?,?,?)}");
            statement.setString("pcriterio", criterio);
            statement.setDate("pfechaini", new java.sql.Date(fechaini.getTime()));
            statement.setDate("pfechafin", new java.sql.Date(fechafin.getTime()));
            statement.setString("pdocumento", doc);
            rs = statement.executeQuery();
            return rs;
        } catch (SQLException SQLex) {
            throw SQLex;
        }
    }

    public boolean actualizarVentaEstado(String codigo, ClsEntidadVenta Venta) {
        try {
            CallableStatement statement = connection.prepareCall("{call SP_U_ActualizarVentaEstado(?,?)}");
            statement.setString("pidventa", codigo);
            statement.setString("pestado", Venta.getStrEstadoVenta());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Message.LOGGER.log(Level.SEVERE, ex.getMessage());
            return false;
        }
    }

    public ResultSet listarVentaPorDetalle(String criterio, Date fechaini, Date fechafin) throws Exception {
        ResultSet rs = null;
        try {
            CallableStatement statement = connection.prepareCall("{call SP_S_VentaPorDetalle(?,?,?)}");
            statement.setString("pcriterio", criterio);
            statement.setDate("pfechaini", new java.sql.Date(fechaini.getTime()));
            statement.setDate("pfechafin", new java.sql.Date(fechafin.getTime()));
            rs = statement.executeQuery();
            return rs;
        } catch (SQLException SQLex) {
            throw SQLex;
        }
    }

    public ResultSet listarVentaMensual(String criterio, String fecha_ini, String fecha_fin) throws Exception {
        ResultSet rs = null;
        try {
            CallableStatement statement = connection.prepareCall("{call SP_S_VentaMensual(?,?,?)}");
            statement.setString("pcriterio", criterio);
            statement.setString("pfecha_ini", fecha_ini);
            statement.setString("pfecha_fin", fecha_fin);
            rs = statement.executeQuery();
            return rs;
        } catch (SQLException SQLex) {
            throw SQLex;
        }
    }
}
