package org.swingeasy;

import java.awt.event.ActionEvent;

import org.swingeasy.EComponentPopupMenu.CheckEnabled;
import org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction;

/**
 * @author Jurgen
 */
public class ETableExporterAction<T> extends EComponentPopupMenuAction<ETable<T>> {
    private static final long serialVersionUID = -4509106311973499954L;

    protected final ETableExporter<T> exporter;

    public ETableExporterAction(ETableExporter<T> exporter, ETable<T> table) {
        super(table, exporter.getAction(), exporter.getIcon());
        this.exporter = exporter;
    }

    /**
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.exporter.export(this.delegate);
    }

    /**
     * 
     * @see org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction#checkEnabled(org.swingeasy.EComponentPopupMenu.CheckEnabled)
     */
    @Override
    public void checkEnabled(CheckEnabled cfg) {
        this.setEnabled(cfg.hasText);
    }
}
