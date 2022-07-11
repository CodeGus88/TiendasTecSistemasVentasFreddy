
package tools;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 *
 * @author Gustavo Abasto Argote
 */
public class ScreenDimensions {
    //Dimensiones automáticas a escala

    private final int TamPanX;
    private final int TamPanY;
    private final float a;
    private final float b;
    private final int x;
    private final int y;
    private int X;
    private int Y;

    private final Toolkit screenDimension;
    private Dimension dimensions;

    public ScreenDimensions() {
        //DIMENSIONES
        screenDimension = Toolkit.getDefaultToolkit();
        dimensions = screenDimension.getScreenSize();//Le pasamos las dimensiones de pantalla a dimensiones

        TamPanX = dimensions.width;//IOptenemos las dimensiones de la pantalla (X = Horizontal)
        TamPanY = dimensions.height;//OPtenemos las dimensiones de la pantalla (Y = Vertical)
        a = ((float) TamPanX) * 1F;//Tamaño horizontal en float *0.25F
        b = ((float) (TamPanY)) * 1F;//Tamaño Vertical en float *0.731489F
        x = (int) Math.round(a);//Se enviarán para la interfaz
        y = (int) Math.round(b);//Se enviarán para la interfaz
    }

    public int horizontal() {  //DEVUELVE LA DIMENSION EN X EQUIVALENTE DEL DISEÑO
        return x;
    }

    public int vertical() {//DEVUELVE LA DIMENSION EN Y EQUIVALENTE DEL DISEÑO
        return y;
    }

    public int pInX(float porcentaje) {//DEVUELVE LAS DIMENSIONES EQUIVALENTES A LOS PORCENTAJES INGRESADOS (X)
        float aux = porcentaje / 100;
        float valor = aux * x;
        X = (int) Math.round(valor);
        return X;
    }

    public int pInY(float porcentaje) {//DEVUELVE LAS DIMENSIONES EQUIVALENTES A LOS PORCENTAJES INGRESADOS (Y)
        float aux = porcentaje / 100;
        float valor = aux * y;
        Y = (int) Math.round(valor);
        return Y;
    }
}
