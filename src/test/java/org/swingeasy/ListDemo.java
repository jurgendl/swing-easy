package org.swingeasy;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class ListDemo {
    public static void main(String[] args) {
        EListConfig cfg = new EListConfig();
        cfg.setSortable(true);
        EList<String> cc = new EList<String>(cfg);
        {
            JFrame f = new JFrame();
            f.getContentPane().add(new JScrollPane(cc), BorderLayout.CENTER);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);
            cc = cc.stsi();
            f.setSize(200, 200);
        }
        cc.addRecord(null);
        cc.addRecord(new EListRecord<String>("44"));
        cc.addRecord(new EListRecord<String>("222"));
        cc.addRecord(new EListRecord<String>("5555"));
        cc.addRecord(new EListRecord<String>("111"));
        cc.addRecord(new EListRecord<String>("333"));
    }
}
