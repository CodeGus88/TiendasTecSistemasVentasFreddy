package statics;

import java.awt.Color;
import java.awt.Component;
import java.util.Hashtable;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Gustavo Abasto Argote
 */
public class TableConfigurator {

    // Tablas de ventas, ventas a crédito y cotizaciónes
    public static final int[] TABLE_COLUMN_ALIGEMENT_1 = {
        SwingConstants.CENTER // colunma 0
        ,
         SwingConstants.CENTER // columna 1
        ,
         SwingConstants.RIGHT // Columna 2
        ,
         SwingConstants.RIGHT // columna 3
        ,
         SwingConstants.CENTER // columna 4
        ,
         SwingConstants.CENTER // columna 5
        ,
         SwingConstants.CENTER // columna 6
        ,
         SwingConstants.CENTER // columna 7
    };

    public static TableCellRenderer configureTableItem() {
        return render(null, null, -1);
    }

    public static TableCellRenderer configureTableItem(int defaultHorizontalAligement) {
        if(!isValidAligementData(defaultHorizontalAligement)) 
            defaultHorizontalAligement = -1;
        return render(null, null, defaultHorizontalAligement);
    }

    public static TableCellRenderer configureTableItem(int[] aligements) {
        return render(aligements, null, -1);
    }

    public static TableCellRenderer configureTableItem(int[] aligements, int defaultHorizontalAligement) {
        if (!isValidAligementData(defaultHorizontalAligement))
            defaultHorizontalAligement = -1;
        if (!isValidAligementData(defaultHorizontalAligement))
            defaultHorizontalAligement = -1;
        return render(aligements, null, defaultHorizontalAligement);
    }

    public static TableCellRenderer configureTableItem(Hashtable<Integer, Integer> aligements) {
        return render(null, aligements, -1);
    }

    public static TableCellRenderer configureTableItem(Hashtable<Integer, Integer> aligements, int defaultHorizontalAligement) {
        if (!isValidAligementData(defaultHorizontalAligement))
            defaultHorizontalAligement = -1;
        return render(null, aligements, defaultHorizontalAligement);
    }
    
    private static boolean isValidAligementData(int i){
        boolean isValid = i == -1
        || i == SwingConstants.LEFT
        || i == SwingConstants.CENTER
        || i == SwingConstants.RIGHT;
        return isValid;
    }

    private static TableCellRenderer render(int[] aligements, Hashtable<Integer, Integer> hash, final int defaultHorizontalAligement) {
        return new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                //Determinar Alineaciones   
                if (aligements != null) {
                    if (column < aligements.length) {
                        l.setHorizontalAlignment(aligements[column]);
                    } else if (defaultHorizontalAligement != -1) {
                        l.setHorizontalAlignment(defaultHorizontalAligement);
                    }
                } else if (hash != null) {
                    if (hash.containsKey(column)) {
                        l.setHorizontalAlignment(hash.get(column));
                    } else if (defaultHorizontalAligement != -1) {
                        l.setHorizontalAlignment(defaultHorizontalAligement);
                    }
                } else if (defaultHorizontalAligement != -1) {
                    l.setHorizontalAlignment(defaultHorizontalAligement);
                }
                //Colores en Jtable        
                if (isSelected) {
                    l.setBackground(Design.COLOR_PRIMARY);
                    l.setForeground(Color.WHITE);
                } else {
                    l.setForeground(Color.BLACK);
                    if (row % 2 == 0) {
                        l.setBackground(Color.WHITE);
                    } else {
                        l.setBackground(Design.COLOR_SECONDARY);
                    }
                }
                return l;
            }
        };
    }
}
