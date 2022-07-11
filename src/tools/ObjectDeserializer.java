
package tools;

import statics.Message;
import java.awt.Rectangle;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gustavo Abasto Argote
 */
public class ObjectDeserializer<O extends Rectangle> {
    
    private FileInputStream file;
    private ObjectInputStream input;
    private String directory, fileName;
    private Logger logger = Logger.getLogger(Message.DESERIALIZER_ERROR_TITLE);
    private O object;
    
    public ObjectDeserializer(String directory, String fileName){
        this.directory = directory;
        this.fileName = fileName;
        object = null;
    }
    
    public O deserialicer(){
        open();
        read();
        close();
        return object;
    }
    
    private void open(){
        try {
            file = new FileInputStream(directory + "/" + fileName);
            input = new ObjectInputStream(file);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
    
    private void read(){
        try {
            if(input != null)
                object = (O) input.readObject();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
    
    private void close() {
        try {
            if (input != null) {
                input.close();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
    
}
