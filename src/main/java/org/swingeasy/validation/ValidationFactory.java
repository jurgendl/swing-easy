package org.swingeasy.validation;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.swingeasy.HasValue;
import org.swingeasy.Messages;
import org.swingeasy.ValueChangeListener;

/**
 * @author Jurgen
 */
public class ValidationFactory {
    protected static Translator defaultTranslator = null;

    public static Translator getDefaultTranslator() {
        if (ValidationFactory.defaultTranslator == null) {
            ValidationFactory.defaultTranslator = new Translator() {

                @Override
                public String getString(String key, Object... arguments) {
                    return Messages.getInstance().getString(key, arguments);
                }
            };
        }
        return ValidationFactory.defaultTranslator;
    }

    public static <I, T extends JComponent & HasValue<I>> EValidationMessageI install(final EValidationPane parentPane, final T textfield,
            final Translator translator, final List<Validator<I>> validators) {
        final Translator _translator = translator == null ? ValidationFactory.getDefaultTranslator() : translator;
        final EValidationMessageI message = new EValidationMessage(parentPane, textfield).stsi();

        ValueChangeListener<I> listener = new ValueChangeListener<I>() {
            @Override
            public void valueChanged(I value) {
                StringBuilder sb = new StringBuilder();
                boolean valid = true;
                for (Validator<I> validator : validators) {
                    if (!validator.isValid(null, value)) {
                        if (!valid) {
                            sb.insert(0, "\u2022 ");
                            sb.append("<br/>\u2022 ");
                        }
                        valid = false;
                        sb.append(_translator.getString(validator.getMessageKey(), validator.getArguments(value)));
                    }
                }
                if (!valid) {
                    message.setIsInvalid("<html>" + sb.toString() + "</html>");
                } else {
                    message.setIsValid();
                }
            }
        };

        textfield.addValueChangeListener(listener);

        return message;
    }

    public static <I, T extends JComponent & HasValue<I>> EValidationMessageI install(final EValidationPane parentPane, final T textfield,
            final Translator translator, final Validator<I> validator1) {
        List<Validator<I>> validators = new ArrayList<Validator<I>>();
        validators.add(validator1);
        return ValidationFactory.install(parentPane, textfield, translator, validators);
    }

    public static <I, T extends JComponent & HasValue<I>> EValidationMessageI install(final EValidationPane parentPane, final T textfield,
            final Translator translator, final Validator<I> validator1, final Validator<I> validator2) {
        List<Validator<I>> validators = new ArrayList<Validator<I>>();
        validators.add(validator1);
        validators.add(validator2);
        return ValidationFactory.install(parentPane, textfield, translator, validators);
    }

    public static <I, T extends JComponent & HasValue<I>> EValidationMessageI install(final EValidationPane parentPane, final T textfield,
            final Translator translator, final Validator<I> validator1, final Validator<I> validator2, final Validator<I> validator3) {
        List<Validator<I>> validators = new ArrayList<Validator<I>>();
        validators.add(validator1);
        validators.add(validator2);
        validators.add(validator3);
        return ValidationFactory.install(parentPane, textfield, translator, validators);
    }

    public static <I, T extends JComponent & HasValue<I>> EValidationMessageI install(final EValidationPane parentPane, final T textfield,
            final Translator translator, final Validator<I> validator1, final Validator<I> validator2, final Validator<I> validator3,
            final Validator<I> validator4) {
        List<Validator<I>> validators = new ArrayList<Validator<I>>();
        validators.add(validator1);
        validators.add(validator2);
        validators.add(validator3);
        validators.add(validator4);
        return ValidationFactory.install(parentPane, textfield, translator, validators);
    }
}
