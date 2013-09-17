package org.swingeasy;

import javax.swing.Icon;

/**
 * @author Jurgen
 */
public interface EListExporter<T> {
    public void export(EList<T> table);

    public String getAction();

    public String getFileExtension();

    public Icon getIcon();
}
