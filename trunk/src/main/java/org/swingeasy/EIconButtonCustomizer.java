package org.swingeasy;

import java.awt.Dimension;

import javax.swing.AbstractButton;

/**
 * @author Jurgen
 */
public class EIconButtonCustomizer extends EToolBarButtonCustomizer {
    public EIconButtonCustomizer() {
        super();
    }

    public EIconButtonCustomizer(Dimension defaultDimension) {
        super(defaultDimension);
    }

    /**
     * 
     * @see org.swingeasy.EToolBarButtonCustomizer#customize(javax.swing.AbstractButton, java.awt.Dimension)
     */
    @Override
    public void customize(AbstractButton button) {
        super.customize(button);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
    }
}
