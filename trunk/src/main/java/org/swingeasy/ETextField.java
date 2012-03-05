package org.swingeasy;

import javax.swing.JTextField;

/**
 * @author Jurgen
 */
public class ETextField extends JTextField implements EComponentI {
    private static final long serialVersionUID = -7074333880526075392L;

    public ETextField(ETextFieldConfig cfg) {
        super(cfg.getColumns());
        this.setEnabled(cfg.isEnabled());
        this.init();
    }

    public ETextField(ETextFieldConfig cfg, String text) {
        super(text, cfg.getColumns());
        this.setEnabled(cfg.isEnabled());
        this.init();
    }

    protected void init() {
        EComponentPopupMenu.installTextComponentPopupMenu(this);
        UIUtils.registerLocaleChangeListener((EComponentI) this);
    }
}
