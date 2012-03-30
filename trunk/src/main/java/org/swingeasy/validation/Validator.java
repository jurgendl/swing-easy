package org.swingeasy.validation;

/**
 * @author Jurgen
 */
public interface Validator<T> {
    public String getMessageKey();

    public boolean validate(Object context, T value);
}