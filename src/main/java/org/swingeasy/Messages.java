package org.swingeasy;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.swingeasy.system.SystemSettings;
import org.swingeasy.validation.Translator;

/**
 * @author Jurgen
 */
public class Messages implements Translator {
    private static final String BUNDLE_NAME = "org.swingeasy.resources.swing-easy"; //$NON-NLS-1$

    private static final Map<Locale, ResourceBundle> RESOURCE_BUNDLES = new HashMap<Locale, ResourceBundle>();

    protected static final Messages instance = new Messages();

    public static Messages getInstance() {
        return Messages.instance;
    }

    private static ResourceBundle getResourceBundle(Locale locale) {
        if (locale == null) {
            locale = SystemSettings.getCurrentLocale();
        }
        ResourceBundle resourceBundle = Messages.RESOURCE_BUNDLES.get(locale);
        if (resourceBundle == null) {
            resourceBundle = ResourceBundle.getBundle(Messages.BUNDLE_NAME, locale);
        }
        return resourceBundle;
    }

    public static String getString(Locale locale, String key) {
        if (locale == null) {
            locale = SystemSettings.getCurrentLocale();
        }
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

    /**
     * 
     * @see org.swingeasy.validation.Translator#getString(java.lang.String, java.lang.Object[])
     */
    @Override
    public String getString(String key, Object... arguments) {
        return String.format(Messages.getString(null, key), arguments);
    }
}
