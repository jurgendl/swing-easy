package org.swingeasy;

import javax.swing.Icon;
import javax.swing.JComponent;

public interface EComponentExporter<T extends JComponent & EComponentI> {
    public void export(T component);

    public String getAction();

    public String getFileExtension();

    public Icon getIcon();
}
