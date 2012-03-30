package org.swingeasy.validation;

import org.swingeasy.EComponentI;

/**
 * @author Jurgen
 */
public interface EValidationMessageI extends EComponentI {
    public void setText(String text);

    public void setToolTipText(String text);

    public void setVisible(boolean aFlag);
}
