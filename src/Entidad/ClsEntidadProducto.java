/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import java.util.Date;



/**
 *
 * @author DAYPER-PERU
 */
public class ClsEntidadProducto {
    
    private String idProducto;
    private String codigoProducto;
    private String nombreProducto;
    private String descripcionProducto;
    private String stockProducto;
    private String stockMinProducto;
    private double costoChica;
    private String precioCostoProducto;
    private String precioVentaProducto;
    private String utilidadProducto;
    private String estadoProducto;
    private String idCategoria;
    private String descripcionCategoria;
    private Image imagen;
    private Date fechaVencimiento;
    
    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String strIdProducto) {
        this.idProducto = strIdProducto;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String strCodigoProducto) {
        this.codigoProducto = strCodigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String strNombreProducto) {
        this.nombreProducto = strNombreProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String strDescripcionProducto) {
        this.descripcionProducto = strDescripcionProducto;
    }

    public String getStockProducto() {
        return stockProducto;
    }

    public void setStockProducto(String strStockProducto) {
        this.stockProducto = strStockProducto;
    }

    public String getStockMinProducto() {
        return stockMinProducto;
    }

    public void setStockMinProducto(String strStockMinProducto) {
        this.stockMinProducto = strStockMinProducto;
    }

    public String getPrecioCostoProducto() {
        return precioCostoProducto;
    }

    public void setPrecioCostoProducto(String strPrecioCostoProducto) {
        this.precioCostoProducto = strPrecioCostoProducto;
    }

    public String getPrecioVentaProducto() {
        return precioVentaProducto;
    }

    public void setPrecioVentaProducto(String strPrecioVentaProducto) {
        this.precioVentaProducto = strPrecioVentaProducto;
    }

    public String getUtilidadProducto() {
        return utilidadProducto;
    }

    public void setUtilidadProducto(String strUtilidadProducto) {
        this.utilidadProducto = strUtilidadProducto;
    }

    public String getEstadoProducto() {
        return estadoProducto;
    }

    public void setEstadoProducto(String strEstadoProducto) {
        this.estadoProducto = strEstadoProducto;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String strIdCategoria) {
        this.idCategoria = strIdCategoria;
    }

    public String getDescripcionCategoria() {
        return descripcionCategoria;
    }

    public void setDescripcionCategoria(String strDescripcionCategoria) {
        this.descripcionCategoria = strDescripcionCategoria;
    }

    public Image getImagen() {
        return imagen;
    }

    public void setImagen(Image imagen) {
        this.imagen = imagen;
    }

    
    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public double getCostoChica() {
        return costoChica;
    }

    public void setCostoChica(double costoChica) {
        this.costoChica = costoChica;
    }

}
