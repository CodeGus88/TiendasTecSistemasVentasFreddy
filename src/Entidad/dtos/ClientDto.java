
package Entidad.dtos;

import javax.swing.ImageIcon;

/**
 *
 * @author Gustavo Abasto Argote
 */
public class ClientDto {
    
    private int id;
    private String name;
    private String dni;
    private String address;
    private String phone;
    private String observation;
    private ImageIcon imageIcon;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCi() {
        return dni;
    }

    public void setCi(String ci) {
        this.dni = ci;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(ImageIcon image) {
        this.imageIcon = image;
    }
    
}
