package org.swingeasy.system;

import java.awt.datatransfer.Clipboard;
import java.io.File;
import java.util.Locale;

import org.swingeasy.PropertyChangeParent;

/**
 * @author Jurgen
 */
public class SystemSettings extends PropertyChangeParent {
    public static final String LOCALE = "locale";

    public static final String TMPDIR = "tmpdir";

    public static final String NEWLINE = "newline";

    public static final String CLIPBOARD = "clipboard";

    private static final SystemSettings singleton = new SystemSettings();

    private static String newline = System.getProperty("line.separator");

    private static Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();

    private static File tmpdir = new File(System.getProperty("java.io.tmpdir"));

    static {
        Locale defaultLocale;
        try {
            // (1) Java 1.7 compilable in Java 1.6 but gives Exception at runtimee so we can fall back to (2)
            @SuppressWarnings("rawtypes")
            Class type = Class.forName("java.util.Locale$Category");
            @SuppressWarnings("unchecked")
            Object enumvalue = Enum.valueOf(type, "FORMAT");
            defaultLocale = Locale.class.cast(Locale.class.getMethod("getDefault", type).invoke(null, enumvalue));
        } catch (Exception ex) {
            // (2) Java 1.6 (gives wrong info in Java 1.7)
            defaultLocale = Locale.getDefault();
        }
        Locale.setDefault(defaultLocale);
    }

    public static Clipboard getClipboard() {
        return SystemSettings.clipboard;
    }

    /**
     * {@link Locale#getDefault()}
     */
    public static Locale getCurrentLocale() {
        return Locale.getDefault();
    }

    public static String getNewline() {
        return SystemSettings.newline;
    }

    public static SystemSettings getSingleton() {
        return SystemSettings.singleton;
    }

    public static File getTmpdir() {
        return SystemSettings.tmpdir;
    }

    public static void setClipboard(Clipboard clipboard) {
        Clipboard old = SystemSettings.clipboard;
        SystemSettings.clipboard = clipboard;
        SystemSettings.singleton.firePropertyChange(SystemSettings.CLIPBOARD, old, clipboard);
    }

    public static void setCurrentLocale(Locale currentLocale) {
        Locale old = Locale.getDefault();
        Locale.setDefault(currentLocale);
        SystemSettings.singleton.firePropertyChange(SystemSettings.LOCALE, old, currentLocale);
    }

    public static void setNewline(String newline) {
        String old = SystemSettings.newline;
        SystemSettings.newline = newline;
        SystemSettings.singleton.firePropertyChange(SystemSettings.NEWLINE, old, newline);
    }

    public static void setTmpdir(File tmpdir) {
        File old = SystemSettings.tmpdir;
        SystemSettings.tmpdir = tmpdir;
        SystemSettings.singleton.firePropertyChange(SystemSettings.TMPDIR, old, tmpdir);
    }

    private SystemSettings() {
        super();
    }
}
