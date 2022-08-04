
package Consultas;

import Conexion.ClsConexion;
import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Gustavo Abasto Argote
 */
public class SheetConsultReports {
    
    
    
    public static void consultSale(int idVenta, String title) {
        Connection connection = new ClsConexion().getConection();
        Map p = new HashMap();
        p.put("busqueda", String.valueOf(idVenta));
        JasperReport report;
        JasperPrint print;
        try {
            report = JasperCompileManager.compileReport(new File("").getAbsolutePath() + File.separator + "src"+File.separator+"Reportes"+File.separator+"RptVentaBoleta.jrxml");
            print = JasperFillManager.fillReport(report, p, connection);
            JasperViewer view = new JasperViewer(print, false);
            view.setTitle(title);
            view.setVisible(true);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
