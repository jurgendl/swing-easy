package org.swingeasy;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * @author Jurgen
 */
public class TableDemoBooleanOrdering {
    public static void main(String[] args) {
        UIUtils.niceLookAndFeel();
        ETableConfig configuration = new ETableConfig(true);
        configuration.setVertical(false);
        final JFrame frame = new JFrame();

        ETable<Object[]> table = new ETable<Object[]>(configuration);
        ETableHeaders<Object[]> headers = new ETableHeaders<Object[]>();
        headers.add("string");
        headers.add("boolean", Boolean.class);
        headers.add("info");
        table.setHeaders(headers);

        table.addRecord(new ETableRecordArray<Object>("kol.a", true, "1(a)"));
        table.addRecord(new ETableRecordArray<Object>("kol.b", false, "0(b)"));
        table.addRecord(new ETableRecordArray<Object>("kol.c", true, "1(c)"));
        table.addRecord(new ETableRecordArray<Object>("kol.d", false, "0(d)"));

        frame.getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }
}
