package org.swingeasy;

import java.awt.BorderLayout;

import javax.swing.JFrame;

/**
 * @author Jurgen
 */
public class RowTableDemo {
    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();
        JFrame f = new JFrame();
        ETable<String[]> table = new ETable<String[]>(new ETableConfig(true));
        table.setHeaders(new ETableHeaders<String[]>("A", "B", "C")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        table.addRecord(new ETableRecordArray<String>("1", "2", "3")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        table.addRecord(new ETableRecordArray<String>("4", "5", "6")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        table.addRecord(new ETableRecordArray<String>("7", "8", "9")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        table.addRecord(new ETableRecordArray<String>("10", "11", "12")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        f.getContentPane().add(new RowNumberTableWrapper(table), BorderLayout.CENTER);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(400, 400);
        f.setVisible(true);
    }
}
