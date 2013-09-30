package org.swingeasy;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.swingeasy.ECheckBoxList.ECheckBoxListRecord;

/**
 * @author Jurgen
 */
public class ListDemo3 {
    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final ECheckBoxList cbl = new ECheckBoxList(new EListConfig());
        for (int i = 0; i < 10; i++) {
            cbl.stsi().addRecord(new ECheckBoxListRecord("item " + i, true));
        }
        f.getContentPane().add(new JScrollPane(cbl));
        f.setSize(400, 400);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
