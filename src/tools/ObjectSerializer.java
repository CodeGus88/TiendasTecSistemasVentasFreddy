
package tools;

import statics.Message;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gustavo Abasto Argote
 */
public class ObjectSerializer<O extends Serializable> {
    
    private FileOutputStream file;
    private ObjectOutputStream output;
    private String path, fileName;
    private Logger logger;
    
    public ObjectSerializer(String path, String fileName){
        createDir(path);
        this.path = path;
        this.fileName = fileName;
        logger = Logger.getLogger(Message.SERIALIZER_ERROR_TITLE);
    }
    
    public void serilizer(O object){
        open();
        write(object);
        close();
    }
    
    private void open(){
        try {
            file = new FileOutputStream(path+ "/" + fileName);
            output = new ObjectOutputStream(file);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    private void close(){
        try {
            if(output != null)
                output.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
    
    private void write(O object){
        try {
            if (output != null)
                output.writeObject(object);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
    
    public static void createDir(String path){
        File d = new File(path);
        d.mkdir();
    }
}
