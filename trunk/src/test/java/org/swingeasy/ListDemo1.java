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

import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * @author Jurgen
 */
public class ListDemo1 {
    static class DemoValue implements Comparable<DemoValue> {
        Number number;

        public DemoValue(Number number) {
            this.number = number;
        }

        @Override
        public int compareTo(final DemoValue other) {
            return new CompareToBuilder().append(this.number, other.number).toComparison();
        }

        @Override
        public String toString() {
            return String.valueOf(this.number);
        }
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        UIUtils.niceLookAndFeel();
        EListConfig cfg = new EListConfig();
        cfg.setSortable(true);
        cfg.setFilterable(true);
        EList<DemoValue> cc = new EList<DemoValue>(cfg);
        JFrame f = new JFrame();
        {
            f.getContentPane().add(
                    new JScrollPane(cc, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED),
                    BorderLayout.CENTER);
            f.getContentPane().add(cc.getFiltercomponent(), BorderLayout.NORTH);
            f.getContentPane().add(cc.getSearchComponent(), BorderLayout.SOUTH);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            final EList<DemoValue> ccc = cc;
            f.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    Collection<EListRecord<DemoValue>> selectedRecord = ccc.getSelectedRecords();
                    System.out.println(selectedRecord == null ? null : selectedRecord.getClass() + " " + selectedRecord); //$NON-NLS-1$
                }
            });
            cc = cc.stsi();
            f.setSize(200, 200);
        }

        final Random r = new Random(256955466579946l);
        for (int i = 0; i < 1000; i++) {
            EListRecord<DemoValue> record = new EListRecord<DemoValue>(new DemoValue(r.nextInt(1000)));
            cc.addRecord(record);
        }

        EListRecord<DemoValue> record0 = new EListRecord<DemoValue>(new DemoValue(111));
        cc.addRecord(record0);
        EListRecord<DemoValue> record = new EListRecord<DemoValue>(new DemoValue(333));
        cc.addRecord(record);
        try {
            cc.setSelectedRecord(new EListRecord<DemoValue>(new DemoValue(666)));
        } catch (IllegalArgumentException ex) {
            System.out.println("expected: " + ex); //$NON-NLS-1$
        }
        cc.setSelectedRecords(Arrays.asList(record, record0));
        f.setVisible(true);
        cc.scrollToVisibleRecord(record);
    }
}
