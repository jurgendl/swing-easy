package org.swingeasy.validation;

import java.util.regex.Pattern;

/**
 * @author Jurgen
 */
public class RegexValidator implements Validator<String> {
    protected final Pattern pattern;

    public RegexValidator(Pattern pattern) {
        this.pattern = pattern;
    }

    public RegexValidator(String pattern) {
        this.pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
    }

    /**
     * @see org.swingeasy.ValidationDemo.Validator#getMessageKey()
     */
    @Override
    public String getMessageKey() {
        return "invalid.regex";
    }

    /**
     * @see org.swingeasy.ValidationDemo.Validator#validate(java.lang.Object, java.lang.Object)
     */
    @Override
    public boolean validate(Object context, String value) {
        return (value != null) || this.pattern.matcher(value).matches();
    }
}