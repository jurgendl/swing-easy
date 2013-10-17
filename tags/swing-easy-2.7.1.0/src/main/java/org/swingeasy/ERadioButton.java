package org.swingeasy;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JRadioButton;
import javax.swing.ToolTipManager;

/**
 * @author Jurgen
 */
public class ERadioButton extends JRadioButton {
    private static final long serialVersionUID = -1359464901174268318L;

    public ERadioButton() {
        this.init();
    }

    public ERadioButton(Action a) {
        super(a);
        this.init();
    }

    public ERadioButton(Icon icon) {
        super(icon);
        this.init();
    }

    public ERadioButton(Icon icon, boolean selected) {
        super(icon, selected);
        this.init();
    }

    public ERadioButton(String text) {
        super(text);
        this.init();
    }

    public ERadioButton(String text, boolean selected) {
        super(text, selected);
        this.init();
    }

    public ERadioButton(String text, Icon icon) {
        super(text, icon);
        this.init();
    }

    public ERadioButton(String text, Icon icon, boolean selected) {
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
        // if (cfg.isTooltips()) {
        ToolTipManager.sharedInstance().registerComponent(this);
        // }
    }
}
