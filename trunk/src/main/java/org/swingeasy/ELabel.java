package org.swingeasy;

import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * @author Jurgen
 */
public class ELabel extends JLabel {
    /** serialVersionUID */
    private static final long serialVersionUID = 8880462529209952297L;

    public ELabel() {
        super();
    }

    public ELabel(Icon image) {
        super(image);
    }

    public ELabel(Icon image, int horizontalAlignment) {
        super(image, horizontalAlignment);
    }

    public ELabel(String text) {
        super(text);
    }

    public ELabel(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
    }

    public ELabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
    }
}
