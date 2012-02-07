package org.swingeasy;

import java.util.Locale;

/**
 * @see http://www.excelsior-usa.com/articles/localization.html
 */
public class Wtf {
    public static void main(String[] args) {
        System.out.println("\"windows\".toUpperCase().equals(\"WINDOWS\"): " + UIUtils.getCurrentLocale() + ": "
                + "windows".toUpperCase().equals("WINDOWS") + " = OK");
        UIUtils.setCurrentLocale(Locale.UK);
        System.out.println("\"windows\".toUpperCase().equals(\"WINDOWS\"): " + UIUtils.getCurrentLocale() + ": "
                + "windows".toUpperCase().equals("WINDOWS") + " = OK");
        UIUtils.setCurrentLocale(new Locale("tr", "TR"));
        System.out.println("\"windows\".toUpperCase().equals(\"WINDOWS\"): " + UIUtils.getCurrentLocale() + ": "
                + "windows".toUpperCase().equals("WINDOWS") + " = WTF!");
    }
}
