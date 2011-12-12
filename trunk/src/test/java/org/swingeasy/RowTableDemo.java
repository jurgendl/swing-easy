package org.swingeasy;

import java.awt.BorderLayout;

import javax.swing.JFrame;

/**
 * @author Jurgen
 */
public class RowTableDemo {
    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();
        JFrame f = new JFrame();
        ETable<String[]> table = new ETable<String[]>(new ETableConfig(true));
        table.setHeaders(new ETableHeaders<String[]>("A", "B", "C"));
        table.addRecord(new ETableRecordArray<String>("1", "2", "3"));
        table.addRecord(new ETableRecordArray<String>("4", "5", "6"));
        table.addRecord(new ETableRecordArray<String>("7", "8", "9"));
        table.addRecord(new ETableRecordArray<String>("10", "11", "12"));
        f.getContentPane().add(new RowNumberTableWrapper(table), BorderLayout.CENTER);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(400, 400);
        f.setVisible(true);
    }
}
