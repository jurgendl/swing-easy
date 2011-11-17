package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class ListDemo1 {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        UIUtils.lookAndFeel();
        EListConfig cfg = new EListConfig();
        cfg.setSortable(true);
        EList<Integer> cc = new EList<Integer>(cfg);
        JFrame f = new JFrame();
        {
            f.getContentPane().add(
                    new JScrollPane(cc, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED),
                    BorderLayout.CENTER);
            f.getContentPane().add(cc.getFiltercomponent(), BorderLayout.NORTH);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            final EList<Integer> ccc = cc;
            f.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    Collection<EListRecord<Integer>> selectedRecord = ccc.getSelectedRecords();
                    System.out.println(selectedRecord == null ? null : selectedRecord.getClass() + " " + selectedRecord); //$NON-NLS-1$
                }
            });
            cc = cc.stsi();
            f.setSize(200, 200);
        }

        final Random r = new Random(256955466579946l);
        for (int i = 0; i < 1000; i++) {
            EListRecord<Integer> record = new EListRecord<Integer>(r.nextInt(1000));
            cc.addRecord(record);
        }

        EListRecord<Integer> record0 = new EListRecord<Integer>(111);
        cc.addRecord(record0);
        EListRecord<Integer> record = new EListRecord<Integer>(333);
        cc.addRecord(record);
        try {
            cc.setSelectedRecord(new EListRecord<Integer>(666));
        } catch (IllegalArgumentException ex) {
            System.out.println("expected: " + ex); //$NON-NLS-1$
        }
        cc.setSelectedRecords(Arrays.asList(record, record0));
        f.setVisible(true);
        cc.scrollToVisibleRecord(record);
    }
}
