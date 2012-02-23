package org.swingeasy;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Jurgen
 */
public class Messages {
    private static final String BUNDLE_NAME = "org.swingeasy.resources.swing-easy"; //$NON-NLS-1$

    private static final Map<Locale, ResourceBundle> RESOURCE_BUNDLES = new HashMap<Locale, ResourceBundle>();

    private static ResourceBundle getResourceBundle(Locale locale) {
        if (locale == null) {
            locale = UIUtils.getCurrentLocale();
        }
        ResourceBundle resourceBundle = Messages.RESOURCE_BUNDLES.get(locale);
        if (resourceBundle == null) {
            resourceBundle = ResourceBundle.getBundle(Messages.BUNDLE_NAME, locale);
        }
        return resourceBundle;
    }

    public static String getString(Locale locale, String key) {
        try {
            return Messages.getResourceBundle(locale).getString(key);
        } catch (MissingResourceException e) {
            System.out.println(key);
            return key;
        }
    }

    private Messages() {
        super();
    }
}
