
package Consultas;

import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;
import statics.Message;

/**
 *
 * @author Gustavo Abasto Argote
 */
public class ImageSelector {
    
    private FileInputStream fileInputStream;
    private int byteLength;
    private ImageIcon icon;
    private JLabel label;
    
    public ImageSelector(JLabel label){
        this.label = label;
    }
    
    public void select(){
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Only files (jpg, png)", "jpg", "png");
        fileChooser.setFileFilter(filter);
        fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter()); // desactiva mostrar todos llos archivos
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int estado = fileChooser.showOpenDialog(null);
        if (estado == JFileChooser.APPROVE_OPTION) {
            try {
                fileInputStream = new FileInputStream(fileChooser.getSelectedFile());
                this.byteLength = (int) fileChooser.getSelectedFile().length();
                Image icono = ImageIO.read(fileChooser.getSelectedFile()).getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_FAST);
                icon = new ImageIcon(icono);
            } catch (FileNotFoundException e) {
                Message.LOGGER.log(Level.SEVERE, e.getMessage());
            } catch (IOException e) {
                Message.LOGGER.log(Level.SEVERE, e.getMessage());
            }
        }else{
            System.out.println("No se seleccionó nigún archivo");
        }
    }
    
    public ImageIcon getImageIcon(){
        return icon;
    }
    
    public int getByteLength(){
        return byteLength;
    }
    
    public FileInputStream getFileInputStream(){
        return fileInputStream;
    }

}
