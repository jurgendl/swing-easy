package org.swingeasy;

import java.awt.BorderLayout;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import org.swingeasy.ETreeTable.CheckMode;

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

            ETreeTableRecordNode child1 = new ETreeTableRecordNode(Arrays.asList(new Object[] { "11", "12", "13" })); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            root.add(child1);

            ETreeTableRecordNode child2 = new ETreeTableRecordNode(Arrays.asList(new Object[] { "21", "22", "23" })); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            root.add(child2);

            ETreeTableRecordNode child3 = new ETreeTableRecordNode(Arrays.asList(new Object[] { "31", "32", "33" })); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            child2.add(child3);

            ETreeTableRecordNode child4 = new ETreeTableRecordNode(Arrays.asList(new Object[] { "41", "42", "43" })); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            child2.add(child4);

            ETreeTableRecordNode child5 = new ETreeTableRecordNode(Arrays.asList(new Object[] { "51", "52", "53" })); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            child4.add(child5);

            ETreeTableRecordNode child6 = new ETreeTableRecordNode(Arrays.asList(new Object[] { "61", "62", "63" })); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            child4.add(child6);

            final ETreeTable treetable = new ETreeTable(root, new ETreeTableHeaders("col1", "col2", "col3")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            treetable.setCheckMode(CheckMode.NODE_AND_CHILDREN);

            frame.getContentPane().add(new JScrollPane(treetable), BorderLayout.CENTER);
            frame.setSize(400, 200);
            frame.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
