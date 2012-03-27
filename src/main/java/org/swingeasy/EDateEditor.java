package org.swingeasy;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.swingeasy.system.SystemSettings;

/**
 * @author Jurgen
 */
public class EDateEditor extends ELabeledTextFieldButtonComponent {
    /** serialVersionUID */
    private static final long serialVersionUID = 3275532427920825736L;

    protected DateFormat formatter;

    protected Date date;

    protected JComponent parentComponent = null;

    public EDateEditor() {
        this(new Date());
    }

    public EDateEditor(Date date) {
        this.formatter = DateFormat.getDateInstance(DateFormat.SHORT, this.getLocale());
        this.setDate(date);
    }

    /**
     * 
     * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#copy()
     */
    @Override
    public void copy() {
        EComponentPopupMenu.copyToClipboard(this.getInput().getText());
    }

    /**
     * 
     * @see org.swingeasy.ELabeledTextFieldButtonComponent#doAction()
     */
    @Override
    protected void doAction() {
        EDateChooser eDateChooser = new EDateChooser(this.date);
        eDateChooser.doShow();
        if (!eDateChooser.cancelled) {
            this.setDate(eDateChooser.getDate());
        }
    }

    /**
     * 
     * @see org.swingeasy.ELabeledTextFieldButtonComponent#getAction()
     */
    @Override
    protected String getAction() {
        return "pick-date";//$NON-NLS-1$
    }

    public Date getDate() {
        return this.date;
    }

    /**
     * 
     * @see org.swingeasy.ELabeledTextFieldButtonComponent#getIcon()
     */
    @Override
    protected Icon getIcon() {
        return Resources.getImageResource("date.png");//$NON-NLS-1$
    }

    /**
     * 
     * @see org.swingeasy.HasParentComponent#getParentComponent()
     */
    @Override
    public JComponent getParentComponent() {
        return this.parentComponent;
    }

    public void setDate(Date date) {
        this.date = date;
        this.getInput().setText(this.formatter.format(date));
    }

    /**
     * 
     * @see java.awt.Component#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        this.getButton().setToolTipText(Messages.getString(l, "EDateEditor.button.text"));//$NON-NLS-1$
        this.getLabel().setText(Messages.getString(l, "EDateEditor.label.text") + ": ");//$NON-NLS-1$ //$NON-NLS-2$
        this.formatter = DateFormat.getDateInstance(DateFormat.SHORT, l == null ? SystemSettings.getCurrentLocale() : l);
    }

    public void setParentComponent(JComponent parentComponent) {
        this.parentComponent = parentComponent;
    }
}
