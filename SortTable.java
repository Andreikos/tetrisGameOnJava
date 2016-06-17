package tetris;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


/** Sorted game table */
public class SortTable {
    JDialog dialog = null;
    JTable table = null;

    public SortTable(String name, Notation info[], String time){
        dialog = new JDialog();
        dialog.setTitle(name);
        DefaultTableModel model = new DefaultTableModel(new Object[] {"Id", "Score"}, 0);
        table = new JTable(model);
        for(Notation notation : info) {
            model.addRow(new Object[] {(notation.getNumber()), notation.getScore()});
        }
        dialog.setLayout(new BorderLayout());
        dialog.add(new JLabel("Time: " + time), BorderLayout.NORTH);
        dialog.add(new JScrollPane(table), BorderLayout.CENTER);

        dialog.setTitle("Sort");
        dialog.pack();
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);

    }
}