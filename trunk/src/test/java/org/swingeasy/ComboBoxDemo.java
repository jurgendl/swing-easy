package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class ComboBoxDemo {
    public static void main(String[] args) {
        EComboBoxConfig cfg = new EComboBoxConfig();
        cfg.setSortable(true);
        EComboBox<String> cc = new EComboBox<String>(cfg);
        {
            JFrame f = new JFrame();
            f.getContentPane().add(cc, BorderLayout.CENTER);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);
            cc = cc.stsi();
            final EComboBox<String> ccc = cc;
            f.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    EComboBoxRecord<String> selectedRecord = ccc.getSelectedRecord();
                    System.out.println(selectedRecord == null ? null : selectedRecord.getClass() + " " + selectedRecord);
                }
            });
            f.setSize(200, 80);
        }
        cc.addRecord(null);
        cc.addRecord(new EComboBoxRecord<String>("44"));
        cc.addRecord(new EComboBoxRecord<String>("222"));
        cc.addRecord(new EComboBoxRecord<String>("5555"));
        cc.addRecord(new EComboBoxRecord<String>("111"));
        EComboBoxRecord<String> record = new EComboBoxRecord<String>("333");
        cc.addRecord(record);
        cc.setSelectedRecord(record);
        EComboBoxRecord<String> selectedRecord = cc.getSelectedRecord();
        System.out.println(selectedRecord == null ? null : selectedRecord.getClass() + " " + selectedRecord);
    }
}
