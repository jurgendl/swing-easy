package org.swingeasy.validation;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.swingeasy.HasValue;
import org.swingeasy.ValueChangeListener;

/**
 * @author Jurgen
 */
public class ValidationFactory {
    public static <I, T extends JComponent & HasValue<I>> EValidationMessageI install(final EValidationPane parentPane, final T textfield,
            final Translator translator, final List<Validator<I>> validators) {
        final EValidationMessageI message = new EValidationMessage(parentPane, textfield).stsi();

        ValueChangeListener<I> listener = new ValueChangeListener<I>() {
            @Override
            public void valueChanged(I value) {
                StringBuilder sb = new StringBuilder("<html><ul>");
                boolean valid = true;
                for (Validator<I> validator : validators) {
                    if (validator.validate(null, value)) {
                        valid = false;
                        sb.append("<li>").append(translator.getString(validator.getMessageKey(), value)).append("</li>");
                    }
                }
                sb.append("</ul></html>");
                if (!valid) {
                    message.setIsInvalid(sb.toString());
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
