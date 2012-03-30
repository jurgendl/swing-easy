package org.swingeasy.validation;

import org.apache.commons.lang.StringUtils;

/**
 * @author Jurgen
 */
public class NotNullValidator<T> implements Validator<T> {
    /**
     * @see org.swingeasy.ValidationDemo.Validator#getMessageKey()
     */
    @Override
    public String getMessageKey() {
        return "invalid.notnull";
    }

    /**
     * @see org.swingeasy.ValidationDemo.Validator#validate(java.lang.Object, java.lang.Object)
     */
    @Override
    public boolean validate(Object context, T value) {
        return (value != null) && (!(value instanceof String) || StringUtils.isNotBlank(String.class.cast(value)));
    }
}
