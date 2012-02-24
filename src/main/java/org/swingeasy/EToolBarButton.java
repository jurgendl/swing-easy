package org.swingeasy;

import java.awt.Dimension;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.KeyStroke;

/**
 * @author Jurgen
 */
public class EToolBarButton extends JButton implements EComponentI {
    /** serialVersionUID */
    private static final long serialVersionUID = -6193067407274776197L;

    public EToolBarButton(Dimension d) {
        super();
        this.init(d);
    }

    public EToolBarButton(Dimension d, Action a) {
        super(a);
        this.init(d);
    }

    public EToolBarButton(Dimension d, Icon icon) {
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
        if (this.getAction() != null) {
            KeyStroke key = KeyStroke.class.cast(this.getAction().getValue(Action.ACCELERATOR_KEY));
            String kst = EComponentPopupMenu.keyStroke2String(key).trim();
            Object description = this.getAction().getValue(Action.LONG_DESCRIPTION);
            if (description == null) {
                description = this.getAction().getValue(Action.SHORT_DESCRIPTION);
            }
            if (kst.length() > 0) {
                this.setToolTipText(String.valueOf(description) + " (" + kst + ")");
            } else {
                this.setToolTipText(String.valueOf(description));
            }
        }
    }
}
