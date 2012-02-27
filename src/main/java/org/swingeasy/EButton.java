package org.swingeasy;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * @author Jurgen
 */
public class EButton extends JButton implements EComponentI {
    private static final long serialVersionUID = -6193067407274776197L;

    public EButton(EButtonCustomizer ebc) {
        super();
        ebc.customize(this);
    }

    public EButton(EButtonCustomizer ebc, Action a) {
        super(a);
        ebc.customize(this);
    }

    public EButton(EButtonCustomizer ebc, Icon icon) {
        super(icon);
        ebc.customize(this);
    }
}
