
package Entidad;

import java.io.FileInputStream;

/**
 *
 * @author Gustavo Abasto Argote
 */
public class Image {
    
    private FileInputStream fileInputStream;
    private int byteLength;
    
    public Image(FileInputStream fileInputStream, int byteLength){
        this.fileInputStream = fileInputStream;
        this.byteLength = byteLength;
    }

    public FileInputStream getFileInputStream() {
        return fileInputStream;
    }

    public void setFileInputStream(FileInputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
    }

    public int getByteLength() {
        return byteLength;
    }

    public void setByteLength(int byteLength) {
        this.byteLength = byteLength;
    }
    
}
