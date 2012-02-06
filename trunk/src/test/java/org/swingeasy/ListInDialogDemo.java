package org.swingeasy;

import java.awt.BorderLayout;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * @author Jurgen
 */
public class ListInDialogDemo {
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

    public static void main(String[] args) {
        try {
            UIUtils.niceLookAndFeel();

            JFrame parent = new JFrame();
            parent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            parent.setVisible(true);

            EList<DemoValue> list = new EList<DemoValue>(new EListConfig());
            JPanel container = new JPanel(new BorderLayout());
            container.add(new JScrollPane(list), BorderLayout.CENTER);
            container.add(list.getSearchComponent(), BorderLayout.NORTH);

            final Random r = new Random(256955466579946l);
            for (int i = 0; i < 1000; i++) {
                list.addRecord(new EListRecord<DemoValue>(new DemoValue(r.nextInt(1000))));
            }

            int returnValue = JOptionPane.showConfirmDialog(parent, container, "List selection", JOptionPane.WARNING_MESSAGE, JOptionPane.OK_OPTION);
            System.out.println(returnValue);
            System.out.println(list.getSelectedRecord());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
