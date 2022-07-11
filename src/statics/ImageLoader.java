
package statics;

//import java.awt.Image;

import java.awt.Image;
import java.util.logging.Level;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import statics.Message;

/**
 *
 * @author Gustavo
 */
public class ImageLoader {
    
    public static void setImage(JLabel lblImage, String path){
        proccess(lblImage, path, Image.SCALE_DEFAULT);
    }
    
    public static void setImage(JLabel lblImage, String path, int scaleType) {
        proccess(lblImage, path, scaleType);
    }
    
    private static void proccess(JLabel lblImage, String path, int scaleType){
    
        try {
            ImageIcon imageIcon = new ImageIcon(path);
            Icon icon = new ImageIcon(
                    imageIcon.getImage().getScaledInstance(
                            lblImage.getWidth(),
                           lblImage.getHeight(),
                            scaleType
                    )
            );
            lblImage.setIcon(icon);
            lblImage.repaint();
        } catch (Exception e) {
            Message.LOGGER.log(Level.SEVERE, e.getMessage());
        }
        
    }
    
}
