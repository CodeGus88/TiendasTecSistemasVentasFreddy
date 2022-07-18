

import Conexion.ClsConexion;
import com.jtattoo.plaf.texture.TextureLookAndFeel;
import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import statics.Message;
/**
 *
 * @author Gustavo Abasto Argote
 */
public class Main {
    public static void main(String ... arg){
        try {
            Presentacion.FrmLogin f = new Presentacion.FrmLogin(); 
            UIManager.setLookAndFeel(new TextureLookAndFeel());
            SwingUtilities.updateComponentTreeUI(f);
            
        } catch (Exception e){
            Message.LOGGER.log(Level.WARNING, e.getMessage());
        }
    }
    
}
