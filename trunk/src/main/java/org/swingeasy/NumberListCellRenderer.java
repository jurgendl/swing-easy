package org.swingeasy;

import java.awt.Component;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

// javax.swing.text.NumberFormatter
/**
 * @author Jurgen
 */
public class NumberListCellRenderer extends DefaultListCellRenderer.UIResource {
    private static final long serialVersionUID = 5169127745067354714L;

    protected NumberFormat formatter;

    public NumberListCellRenderer() {
        this.newFormatter();
    }

    /**
     * 
     * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        return super.getListCellRendererComponent(list, this.getValue(value), index, isSelected, cellHasFocus);
    }

    protected String getValue(Object value) {
        return ((value == null) ? "" : this.formatter.format(value));
    }

    protected void newFormatter() {
        this.formatter = NumberFormat.getInstance(this.getLocale());
    }

    /**
     * 
     * @see java.awt.Component#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        this.newFormatter();
    }
}