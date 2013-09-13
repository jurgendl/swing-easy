package org.swingeasy;

import java.text.Format;
import java.util.Locale;

import javax.swing.JFormattedTextField;
import javax.swing.ToolTipManager;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;

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
        this(new DefaultFormatterFactory(factory, factory, factory, factory));
    }

    public EFormattedTextField(DefaultFormatter factory, Object currentValue) {
        this(new DefaultFormatterFactory(factory, factory, factory, factory));
        this.setValue(currentValue);
    }

    public EFormattedTextField(EFormatBuilder factory) {
        super();
        this.setFormat(factory.build(SystemSettings.getCurrentLocale()));
        this.factory = factory;
        this.init();
    }

    public EFormattedTextField(EFormatBuilder factory, Object currentValue) {
        super();
        this.setFormat(factory.build(SystemSettings.getCurrentLocale()));
        this.setValue(currentValue);
    }

    public EFormattedTextField(Object currentValue) {
        super(currentValue);
    }

    public void addDocumentKeyListener(DocumentKeyListener listener) {
        this.getDocument().addDocumentListener(listener);
        this.addKeyListener(listener);
    }

    /**
     * 
     * @see javax.swing.JComponent#getToolTipText()
     */
    @Override
    public String getToolTipText() {
        String toolTipText = super.getToolTipText();
        if (toolTipText == null) {
            String text = this.getText();
            if (text.trim().length() == 0) {
                text = null;
            }
            return text;
        }
        return toolTipText;
    }

    protected void init() {
        // if (cfg.isTooltips()) {
        ToolTipManager.sharedInstance().registerComponent(this);
        // }
        EComponentPopupMenu.installTextComponentPopupMenu(this);
        UIUtils.registerLocaleChangeListener((EComponentI) this);
        this.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
    }

    public void removeDocumentKeyListener(DocumentKeyListener listener) {
        this.getDocument().removeDocumentListener(listener);
        this.removeKeyListener(listener);
    }

    protected void setFormat(Format format) {
        AbstractFormatterFactory ff;
        try {
            // thank you for making this private
            ff = MethodInvoker.invoke(this, "getDefaultFormatterFactory", Object.class, format, AbstractFormatterFactory.class);

            if (ff instanceof DefaultFormatterFactory) {
                DefaultFormatterFactory defaultFormatterFactory = DefaultFormatterFactory.class.cast(ff);
                defaultFormatterFactory.setDisplayFormatter(defaultFormatterFactory.getDefaultFormatter());
                defaultFormatterFactory.setEditFormatter(defaultFormatterFactory.getDefaultFormatter());
                defaultFormatterFactory.setNullFormatter(defaultFormatterFactory.getDefaultFormatter());
            }
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
