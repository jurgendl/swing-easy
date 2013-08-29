package org.swingeasy;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.ToolTipManager;

/**
 * @author Jurgen
 */
public class ELabel extends JLabel {

    private static final long serialVersionUID = 8880462529209952297L;

    public ELabel() {
        super();
        this.init();
    }

    public ELabel(Icon image) {
        super(image);
        this.init();
    }

    public ELabel(Icon image, int horizontalAlignment) {
        super(image, horizontalAlignment);
        this.init();
    }

    public ELabel(String text) {
        super(text);
        this.init();
    }

    public ELabel(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
        this.init();
    }

    public ELabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
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
                return null;
            }
            return text.trim();
        }
        return toolTipText;
    }

    protected void init() {
        ToolTipManager.sharedInstance().registerComponent(this);
    }
}
