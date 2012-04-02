package org.swingeasy;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToggleButton;

/**
 * @author Jurgen
 */
public class EToggleButton extends JToggleButton implements EComponentI {
    private static final long serialVersionUID = -6193067407274776197L;

    public EToggleButton() {
        super();
    }

    public EToggleButton(Action a) {
        super(a);
    }

    public EToggleButton(EButtonCustomizer ebc) {
        super();
        ebc.customize(this);
    }

    public EToggleButton(EButtonCustomizer ebc, Action a) {
        super(a);
        ebc.customize(this);
    }

    public EToggleButton(EButtonCustomizer ebc, Icon icon) {
        super(icon);
        ebc.customize(this);
    }

    public EToggleButton(EButtonCustomizer ebc, String text) {
        super(text);
        ebc.customize(this);
    }

    public EToggleButton(EButtonCustomizer ebc, String text, Icon icon) {
        super(text, icon);
        ebc.customize(this);
    }

    public EToggleButton(Icon icon) {
        super(icon);
    }

    public EToggleButton(String text) {
        super(text);
    }

    public EToggleButton(String text, Icon icon) {
        super(text, icon);
    }
}
