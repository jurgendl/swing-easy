package org.swingeasy;

import java.beans.PropertyChangeListener;
import java.util.Locale;

import javax.swing.JComponent;

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
        UIUtils.singleton.firePropertyChange("boolean", false, true);
        System.out.println("l:");
        for (PropertyChangeListener l : UIUtils.singleton.getPropertyChangeListeners(UIUtils.LOCALE)) {
            System.out.println(l);
        }
        UIUtils.setCurrentLocale(Locale.GERMANY);
        tester = null;
        Runtime.getRuntime().gc();
        System.out.println();
        UIUtils.setCurrentLocale(Locale.ITALY);
        System.out.println("l:");
        for (PropertyChangeListener l : UIUtils.singleton.getPropertyChangeListeners(UIUtils.LOCALE)) {
            System.out.println(l);
        }
    }
}
