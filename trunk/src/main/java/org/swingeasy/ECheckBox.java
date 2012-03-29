package org.swingeasy;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.ToolTipManager;

/**
 * @author Jurgen
 */
public class ECheckBox extends JCheckBox {
    private static final long serialVersionUID = -7050606626337213461L;

    public ECheckBox() {
        this.init();
    }

    public ECheckBox(Action a) {
        super(a);
        this.init();
    }

    public ECheckBox(Icon icon) {
        super(icon);
        this.init();
    }

    public ECheckBox(Icon icon, boolean selected) {
        super(icon, selected);
        this.init();
    }

    public ECheckBox(String text) {
        super(text);
        this.init();
    }

    public ECheckBox(String text, boolean selected) {
        super(text, selected);
        this.init();
    }

    public ECheckBox(String text, Icon icon) {
        super(text, icon);
        this.init();
    }

    public ECheckBox(String text, Icon icon, boolean selected) {
        super(text, icon, selected);
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
    }
}
