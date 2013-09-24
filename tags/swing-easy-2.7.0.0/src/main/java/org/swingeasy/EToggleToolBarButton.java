package org.swingeasy;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToggleButton;

/**
 * @author Jurgen
 */
public class EToggleToolBarButton extends JToggleButton implements EComponentI {
    private static final long serialVersionUID = -6193067407274776197L;

    public EToggleToolBarButton(EButtonCustomizer ebc) {
        super();
        ebc.customize(this);
    }

    public EToggleToolBarButton(EButtonCustomizer ebc, Action a) {
        super(a);
        ebc.customize(this);
        this.setName(String.valueOf(a.getValue(Action.NAME)));
    }

    public EToggleToolBarButton(EButtonCustomizer ebc, Icon icon) {
        super(icon);
        ebc.customize(this);
    }
}
