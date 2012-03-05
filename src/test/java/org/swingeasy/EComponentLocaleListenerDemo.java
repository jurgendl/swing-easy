package org.swingeasy;

import java.beans.PropertyChangeListener;
import java.util.Locale;

import javax.swing.JComponent;

import org.swingeasy.system.SystemSettings;

/**
 * @author Jurgen
 */
public class EComponentLocaleListenerDemo {
    public static void main(String[] args) {
        JComponent tester = new JComponent() {
            private static final long serialVersionUID = 438899386009488658L;

            @Override
            public void setLocale(Locale l) {
                super.setLocale(l);
                System.out.println("> " + l);
            }
        };
        UIUtils.registerLocaleChangeListener(tester);
        SystemSettings.getSingleton().firePropertyChange("boolean", false, true);
        System.out.println("l:");
        for (PropertyChangeListener l : SystemSettings.getSingleton().getPropertyChangeListeners(SystemSettings.LOCALE)) {
            System.out.println(l);
        }
        SystemSettings.setCurrentLocale(Locale.GERMANY);
        tester = null;
        Runtime.getRuntime().gc();
        System.out.println();
        SystemSettings.setCurrentLocale(Locale.ITALY);
        System.out.println("l:");
        for (PropertyChangeListener l : SystemSettings.getSingleton().getPropertyChangeListeners(SystemSettings.LOCALE)) {
            System.out.println(l);
        }
    }
}
