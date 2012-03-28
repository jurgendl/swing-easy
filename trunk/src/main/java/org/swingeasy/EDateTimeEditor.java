package org.swingeasy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

/**
 * @author Jurgen
 */
public class EDateTimeEditor extends EDateEditor {
    private static final long serialVersionUID = 1682632931152108808L;

    /**
     * 
     * @see org.swingeasy.EDateEditor#createEDateChooser()
     */
    @Override
    protected EDateChooser createEDateChooser() {
        return new EDateTimeChooser();
    }

    /**
     * 
     * @see org.swingeasy.EDateEditor#getAction()
     */
    @Override
    protected String getAction() {
        return "pick-date-time";//$NON-NLS-1$
    }

    /**
     * 
     * @see org.swingeasy.EDateEditor#getInput()
     */
    @Override
    public ESpinner<Date> getInput() {
        if (this.input == null) {
            SpinnerDateModel model = new SpinnerDateModel();
            model.setValue(new Date());
            ESpinner<Date> spin = new ESpinner<Date>(model);
            spin.setEditor(new JSpinner.DateEditor(spin, SimpleDateFormat.class.cast(
                    DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault())).toPattern()));
            this.input = spin;
        }
        return this.input;
    }

    /**
     * 
     * @see org.swingeasy.EDateEditor#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        this.getLabel().setText(Messages.getString(l, "EDateTimeEditor.label.text") + ": ");//$NON-NLS-1$ //$NON-NLS-2$
    }
}
