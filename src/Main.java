

import com.jtattoo.plaf.texture.TextureLookAndFeel;
import java.util.logging.Level;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
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
