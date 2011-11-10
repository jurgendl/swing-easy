package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class ListDemo {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        UIUtils.lookAndFeel();
        {
            EListConfig cfg = new EListConfig();
            cfg.setSortable(true);
            EList<String> cc = new EList<String>(cfg);
            {
                JFrame f = new JFrame();
                f.getContentPane().add(
                        new JScrollPane(cc, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED),
                        BorderLayout.CENTER);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setVisible(true);
                cc = cc.stsi();
                f.setSize(200, 200);
            }
            cc.addRecord(new EListRecord<String>("44"));
            cc.addRecord(new EListRecord<String>("222"));
            cc.addRecord(new EListRecord<String>("5555"));
            EListRecord<String> record0 = new EListRecord<String>("111");
            cc.addRecord(record0);
            EListRecord<String> record = new EListRecord<String>("333");
            cc.addRecord(record);
            try {
                cc.setSelectedRecord(new EListRecord<String>("666"));
            } catch (IllegalArgumentException ex) {
                System.out.println("expected: " + ex);
            }
            cc.setSelectedRecords(Arrays.asList(record, record0));
        }
        {
            EListConfig cfg = new EListConfig();
            cfg.setSortable(false);
            @SuppressWarnings("rawtypes")
            EList cc = new EList(cfg);
            {
                JFrame f = new JFrame();
                f.getContentPane().add(
                        new JScrollPane(cc, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED),
                        BorderLayout.CENTER);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setVisible(true);
                cc = cc.stsi();
                f.setSize(200, 200);
            }
            cc.addRecord(new EListRecord<Date>(new Date()));
            cc.addRecord(new EListRecord<Color>(Color.red));
            cc.addRecord(new EListRecord<Long>(1000l));
            cc.addRecord(new EListRecord<Integer>(100));
            cc.addRecord(new EListRecord<Float>(100.01f));
            cc.addRecord(new EListRecord<Double>(1000.001d));
            cc.addRecord(new EListRecord<Boolean>(true));
            System.out.println(cc.getCellRenderer());
        }
    }
}
