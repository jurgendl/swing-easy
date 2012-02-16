package org.swingeasy;

import java.util.Locale;

import org.swingeasy.EComponentPopupMenu.ReadableComponent;

/**
 * @author Jurgen
 */
public interface EComponentI extends ReadableComponent {
    public abstract void setEnabled(boolean b);

    public abstract void setLocale(Locale l);
}
