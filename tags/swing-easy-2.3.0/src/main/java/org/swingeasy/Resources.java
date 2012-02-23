package org.swingeasy;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;

/**
 * @author Jurgen
 */
public class Resources {
    private static ClassLoader defaultClassLoader;

    public static byte[] getDataResource(ClassLoader cl, String key) throws IOException {
        String path = Resources.class.getPackage().getName().replace('.', '/') + "/resources/" + key;//$NON-NLS-1$
        InputStream resource = cl.getResourceAsStream(path);
        byte[] data = new byte[resource.available()];
        resource.read(data);
        resource.close();
        return data;
    }

    public static byte[] getDataResource(String key) throws IOException {
        return Resources.getDataResource(Resources.getDefaultClassLoader(), key);
    }

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
