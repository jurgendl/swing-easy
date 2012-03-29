package org.swingeasy.list.renderer;

import java.awt.Component;
import java.text.DateFormat;
import java.util.Locale;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.swingeasy.EComponentI;

// javax.swing.text.DateFormatter
/**
 * @author Jurgen
 */
public class DateListCellRenderer extends DefaultListCellRenderer.UIResource implements EComponentI {
    public enum Type {
        DATE, TIME, DATE_TIME;
    }

    
    private static final long serialVersionUID = -1237135359072682865L;

    protected DateFormat formatter;

    protected Type type;

    public DateListCellRenderer() {
        this(Type.DATE_TIME);
    }

    public DateListCellRenderer(Type type) {
        this.type = type;
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
        return ((value == null) ? "" : this.formatter.format(value)); //$NON-NLS-1$
    }

    protected void newFormatter() {
        switch (this.type) {
            case DATE:
                this.formatter = DateFormat.getDateInstance(DateFormat.LONG, this.getLocale());
                break;
            case DATE_TIME:
                this.formatter = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, this.getLocale());
                break;
            case TIME:
                this.formatter = DateFormat.getTimeInstance(DateFormat.LONG, this.getLocale());
                break;
        }
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
