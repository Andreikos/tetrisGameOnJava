package tetris;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StatisticsTable {

    JDialog dialog = null;
    JTable table = null;
    private int typesCount = 7;

    public StatisticsTable(String name, int[] answer, int max){
        dialog = new JDialog();
        dialog.setTitle(name);
        DefaultTableModel model = new DefaultTableModel
                (new Object[] {"Type", "Count"}, 0);
        table = new JTable(model);

        for(int i = 0; i < typesCount; i++){
            model.addRow(new Object[] {Notation.intToType(i), answer[i]});
        }

        model.addRow(new Object[] {"Most common: " + Notation.intToType(max), answer[max]});

        dialog.add(new JScrollPane(table));
        dialog.setSize(new Dimension(300, 200));
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

}
