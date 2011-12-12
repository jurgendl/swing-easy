package org.swingeasy;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTable;

/**
 * @author Jurgen
 */
public class RowTableDemo {
    public static void main(String[] args) {
        UIUtils.niceLookAndFeel();
        JFrame f = new JFrame();
        Object[][] data = { { "0", "0" }, { "0", "0" } };
        String[] names = { "0", "0" };
        JTable mainTable = new JTable(data, names);
        f.getContentPane().add(new RowNumberTableWrapper(mainTable), BorderLayout.CENTER);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(400, 400);
        f.setVisible(true);
    }
}
