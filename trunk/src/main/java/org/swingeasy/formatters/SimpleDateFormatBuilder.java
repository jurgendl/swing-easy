package org.swingeasy.formatters;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.swingeasy.EFormatBuilder;

/**
 * @author Jurgen
 */
public class SimpleDateFormatBuilder implements EFormatBuilder {
    protected String pattern;

    private SimpleDateFormatBuilder(String pattern) {
        this.pattern = pattern;
    }

    /**
     * 
     * @see org.swingeasy.EFormatBuilder#build(java.util.Locale)
     */
    @Override
    public Format build(Locale locale) {
        return new SimpleDateFormat(this.pattern, locale);
    }
}
