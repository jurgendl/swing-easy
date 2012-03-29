package org.swingeasy;

import java.text.Format;
import java.util.Locale;

import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatter;

import org.swingeasy.MethodInvoker.InvocationException;
import org.swingeasy.formatters.EFormatBuilder;
import org.swingeasy.system.SystemSettings;

/**
 * @author Jurgen
 */
public class EFormattedTextField extends JFormattedTextField implements EComponentI {
    private static final long serialVersionUID = 3962285208926281649L;

    protected EFormatBuilder factory;

    public EFormattedTextField(DefaultFormatter factory) {
        super(factory);
    }

    public EFormattedTextField(DefaultFormatter factory, Object currentValue) {
        super(factory);
        this.setValue(currentValue);
    }

    public EFormattedTextField(EFormatBuilder factory) {
        super(factory.build(SystemSettings.getCurrentLocale()));
        this.factory = factory;
        this.init();
    }

    public EFormattedTextField(EFormatBuilder factory, Object currentValue) {
        this(factory);
        this.setValue(currentValue);
    }

    public EFormattedTextField(Object currentValue) {
        super(currentValue);
    }

    protected void init() {
        EComponentPopupMenu.installTextComponentPopupMenu(this);
        UIUtils.registerLocaleChangeListener((EComponentI) this);
        this.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
    }

    protected void setFormat(Format format) {
        AbstractFormatterFactory ff;
        try {
            // thank you for making this private
            ff = (AbstractFormatterFactory) MethodInvoker.invoke(this, "getDefaultFormatterFactory", Object.class, format);
        } catch (InvocationException ex) {
            throw new RuntimeException(ex);
        }
        this.setFormatterFactory(ff);
    }

    /**
     * 
     * @see java.awt.Component#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        if (this.factory != null) {
            this.setFormat(this.factory.build(l));
        }
    }
}
