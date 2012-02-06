package org.swingeasy;

import javax.swing.ImageIcon;

/**
 * @author Jurgen
 */
public class Resources {
    private static ClassLoader defaultClassLoader;

    public static ClassLoader getDefaultClassLoader() {
        if (Resources.defaultClassLoader == null) {
            Resources.defaultClassLoader = ClassLoader.getSystemClassLoader();
        }
        return Resources.defaultClassLoader;
    }

    public static ImageIcon getImageResource(ClassLoader cl, String key) {
        String path = Resources.class.getPackage().getName().replace('.', '/') + "/resources/images/" + key;//$NON-NLS-1$
        return new ImageIcon(cl.getResource(path));
    }

    public static ImageIcon getImageResource(String key) {
        return Resources.getImageResource(Resources.getDefaultClassLoader(), key);
    }
}
