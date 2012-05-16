package org.swingeasy;

import javax.swing.Icon;

/**
 * @author Jurgen
 */
public interface ETableExporter<T> {
    public void export(ETable<T> table);

    public String getAction();

    public String getFileExtension();

    public Icon getIcon();
}
