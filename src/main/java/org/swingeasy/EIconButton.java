package org.swingeasy;

import java.awt.Dimension;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * @author Jurgen
 */
public class EIconButton extends JButton implements EComponentI {
    /** serialVersionUID */
    private static final long serialVersionUID = -6193067407274776197L;

    public EIconButton(Dimension d) {
        super();
        this.init(d);
    }

    public EIconButton(Dimension d, Action a) {
        super(a);
        this.init(d);
    }

    public EIconButton(Dimension d, Icon icon) {
        super(icon);
        this.init(d);
    }

    protected void init(Dimension d) {
        this.setMaximumSize(d);
        this.setPreferredSize(d);
        this.setSize(d);
        this.setContentAreaFilled(false);
        this.setHideActionText(true);
        this.setBorderPainted(false);
        this.setOpaque(false);
        UIUtils.registerLocaleChangeListener(this);
    }
}
