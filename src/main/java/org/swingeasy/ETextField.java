package org.swingeasy;

import javax.swing.JTextField;
import javax.swing.ToolTipManager;

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
        ToolTipManager.sharedInstance().registerComponent(this);
        EComponentPopupMenu.installTextComponentPopupMenu(this);
        UIUtils.registerLocaleChangeListener((EComponentI) this);
    }
}
