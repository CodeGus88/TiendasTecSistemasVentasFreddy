/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package statics;

import tools.ScreenDimensions;

/**
 *
 * @author Gustavo Abasto Argote
 */
public class ScreenUses {
    public static int getPinX(float p){
        ScreenDimensions screenDimensions = new ScreenDimensions();
        return screenDimensions.pInX(p);
    }
    
    public static int getPinY(float p) {
        ScreenDimensions screenDimensions = new ScreenDimensions();
        return screenDimensions.pInY(p);
    }
    
    public static int getPinTotal(int total, float percentage){
        return (int) Math.round(total * (percentage / 100));
    }
    
    public static int getVertical(){
        ScreenDimensions screenDimensions = new ScreenDimensions();
        return screenDimensions.vertical();
    }
    
    public static int getHorizontal() {
        ScreenDimensions screenDimensions = new ScreenDimensions();
        return screenDimensions.horizontal();
    }
}
