package org.swingeasy;

import java.awt.BorderLayout;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * @author Jurgen
 */
public class TreeTableDemo {
    public static void main(String[] args) {
        try {
            UIUtils.niceLookAndFeel();
            final JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            ETreeTableRecordNode root = new ETreeTableRecordNode();

            ETreeTableRecordNode child1 = new ETreeTableRecordNode(Arrays.asList(new Object[] { "11", "12", "13" }));
            root.add(child1);

            ETreeTableRecordNode child2 = new ETreeTableRecordNode(Arrays.asList(new Object[] { "21", "22", "23" }));
            root.add(child2);

            ETreeTableRecordNode child3 = new ETreeTableRecordNode(Arrays.asList(new Object[] { "31", "32", "33" }));
            child2.add(child3);

            ETreeTableRecordNode child4 = new ETreeTableRecordNode(Arrays.asList(new Object[] { "41", "42", "43" }));
            child2.add(child4);

            ETreeTableRecordNode child5 = new ETreeTableRecordNode(Arrays.asList(new Object[] { "51", "52", "53" }));
            child4.add(child5);

            ETreeTableRecordNode child6 = new ETreeTableRecordNode(Arrays.asList(new Object[] { "61", "62", "63" }));
            child4.add(child6);

            ETreeTable treetable = new ETreeTable(new ETreeTableModel(root, new ETreeTableHeaders("col1", "col2", "col3")));

            frame.getContentPane().add(new JScrollPane(treetable), BorderLayout.CENTER);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    frame.setSize(400, 200);
                    frame.setVisible(true);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
