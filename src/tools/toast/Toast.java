package tools.toast;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import tools.ScreenDimensions;

/**
 * 
 * @author Gustavo Abasto Argote
 */
public class Toast extends Thread{
    
    public static final int LENGTH_LONG = 5;
    public static final int LENGTH_SHORT = 3;
    public static final int LENGTH_MICRO = 2;
    
    public static final byte DANGER = 0;
    public static final byte WARNING = 1;
    public static final byte INFORMATION = 2;
    public static final byte SUCCESS = 3;
    public static final byte UNSUCCESS = 4;
    
    private static int time;
    private static byte messageType;
    private static String message;
    private static Toast toast;
    
    public static Toast makeText(String message, int duration){
        messageType = INFORMATION;
        return defaultMessage(message, duration);
    }
    
    public static Toast makeText(byte type, String message, int duration) {
        messageType = type;
        return defaultMessage(message, duration);
    }
    
    private static Toast defaultMessage(String message, int duration){
        Toast.message = message;
        time = duration * 1000;
        toast = new Toast();
        return toast;
    }
    
    public static void show(){
        toast.start();
    }

    @Override
    public void run(){
        JFrame frame = new JFrame();
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.DARK_GRAY);
        frame.setUndecorated(true);
        frame.setAlwaysOnTop(true); // siempre visible
        frame.setLayout(new GridLayout());
        ScreenDimensions dimensions = new ScreenDimensions();
        frame.setBackground(new Color(0, 0, 0, 75));
        frame.setSize(dimensions.pInX(40), dimensions.pInY(7));
        frame.setLocationRelativeTo(null);
        frame.setLocation(frame.getX(), dimensions.pInY(85));
        JLabel textMessage = new JLabel(message);
        textMessage.setHorizontalAlignment(SwingConstants.CENTER);
        textMessage.setForeground(Color.WHITE);
        textMessage.setOpaque(false);
        textMessage.setFont(new Font("Arial", Font.PLAIN, 30));
        if(messageType == INFORMATION)
            textMessage.setForeground(Color.WHITE);
        else if(messageType == WARNING)
            textMessage.setForeground(Color.ORANGE);
        else if(messageType == DANGER)
            textMessage.setForeground(Color.RED);
        else if(messageType == SUCCESS)
            textMessage.setForeground(new Color(178, 255, 89));
        else if(messageType == UNSUCCESS)
            textMessage.setForeground(new Color(238, 255, 65));
        frame.add(textMessage);
        frame.setVisible(true);
        try {
            Thread.sleep(time);
            frame.dispose();
            System.gc();
        } catch (InterruptedException ex) {
            frame.dispose();
            Logger.getLogger(Toast.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}