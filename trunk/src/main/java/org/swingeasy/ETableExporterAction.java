package org.swingeasy;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * @author Jurgen
 */
public class ETableExporterAction<T> extends AbstractAction {
    /** serialVersionUID */
    private static final long serialVersionUID = -4509106311973499954L;

    protected final ETableExporter<T> exporter;

    protected final ETable<T> table;

    public ETableExporterAction(ETableExporter<T> exporter, ETable<T> table) {
        super(exporter.getAction(), exporter.getIcon());
        this.exporter = exporter;
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.exporter.export(this.table);
    }
}
