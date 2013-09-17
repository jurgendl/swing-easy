package org.swingeasy;

import java.awt.event.ActionEvent;

import org.swingeasy.EComponentPopupMenu.CheckEnabled;
import org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction;

/**
 * @author Jurgen
 */
public class EListExporterAction<T> extends EComponentPopupMenuAction<EList<T>> {
    private static final long serialVersionUID = 5801982050032014321L;

    protected final EListExporter<T> exporter;

    public EListExporterAction(EListExporter<T> exporter, EList<T> table) {
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
    public boolean checkEnabled(CheckEnabled cfg) {
        this.setEnabled(cfg.hasText);
        return cfg.hasText;
    }
}
