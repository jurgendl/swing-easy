package org.swingeasy;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.JPanel;
import javax.swing.SpinnerDateModel;

import net.miginfocom.swing.MigLayout;

/**
 * @author Jurgen
 */
public class EDateTimeChooser extends EDateChooser {
    private static final long serialVersionUID = -8330885639416906143L;

    protected ESpinner<Date> timeSpinner;

    protected ELabel timeLabel;

    public EDateTimeChooser() {
        super();
    }

    public EDateTimeChooser(Date date) {
        super(date);
    }

    public EDateTimeChooser(Date date, Locale locale) {
        super(date, locale);
    }

    public EDateTimeChooser(Date date, Locale locale, TimeZone tz) {
        super(date, locale, tz);
    }

    /**
     * 
     * @see org.swingeasy.EDateChooser#getSpinnerPanel()
     */
    @Override
    protected JPanel getSpinnerPanel() {
        if (this.spinnerPanel == null) {
            this.spinnerPanel = new JPanel(new MigLayout("", "[]rel[]10px[]rel[]"));

            this.spinnerPanel.add(this.getTimeLabel(), ""); //$NON-NLS-1$
            this.spinnerPanel.add(this.getTimeSpinner(), "grow, wrap"); //$NON-NLS-1$

            this.spinnerPanel.add(this.getMonthLabel(), ""); //$NON-NLS-1$
            this.spinnerPanel.add(this.getMonthSpinner(), "grow"); //$NON-NLS-1$

            this.spinnerPanel.add(this.getYearLabel(), ""); //$NON-NLS-1$
            this.spinnerPanel.add(this.getYearSpinner(), "grow"); //$NON-NLS-1$
        }
        return this.spinnerPanel;
    }

    protected ELabel getTimeLabel() {
        if (this.timeLabel == null) {
            this.timeLabel = new ELabel();
        }
        return this.timeLabel;
    }

    protected ESpinner<Date> getTimeSpinner() {
        if (this.timeSpinner == null) {
            this.timeSpinner = new ESpinner<Date>(new SpinnerDateModel());
            ESpinner.DateEditor timeEditor = new ESpinner.DateEditor(this.timeSpinner, "HH:mm");
            this.timeSpinner.setEditor(timeEditor);
        }
        return this.timeSpinner;
    }

    /**
     * 
     * @see org.swingeasy.EDateChooser#setDate(java.util.Date)
     */
    @Override
    public void setDate(Date date) {
        super.setDate(date);
        this.getTimeSpinner().setValue(date);
    };

    /**
     * 
     * @see org.swingeasy.EDateChooser#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        this.getTimeLabel().setText(Messages.getString(l, "EDateChooser.time")); //$NON-NLS-1$
    }

    /**
     * 
     * @see org.swingeasy.EDateChooser#updateValues()
     */
    @Override
    protected void updateValues() {
        super.updateValues();
        this.getTimeSpinner().setValue(this.calendar.getTime());
    }
}
