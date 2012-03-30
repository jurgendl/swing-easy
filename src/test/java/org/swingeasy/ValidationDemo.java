package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;

import org.apache.commons.lang.StringUtils;
import org.swingeasy.validation.EValidationMessage;
import org.swingeasy.validation.EValidationMessageI;
import org.swingeasy.validation.EValidationPane;

/**
 * @author Jurgen
 */
public class ValidationDemo {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel contents = new JPanel(new BorderLayout());

        // validation code ==>
        EValidationPane parent = new EValidationPane(contents);
        // <== validation code

        frame.getContentPane().add(parent, BorderLayout.CENTER);

        ETextField comp1 = new ETextField(new ETextFieldConfig(), "invalid1");
        ETextField comp2 = new ETextField(new ETextFieldConfig(), "invalid2");
        final ETextField comp3 = new ETextField(new ETextFieldConfig(), "");
        final ETextField comp4 = new ETextField(new ETextFieldConfig(), "");

        JPanel inner = new JPanel(new GridLayout(-1, 2));
        inner.add(new JLabel("invalid"));
        inner.add(comp1);

        inner.add(new JLabel("     "));
        inner.add(new JLabel("     "));

        inner.add(new JLabel("valid"));
        inner.add(comp2);

        inner.add(new JLabel("     "));
        inner.add(new JLabel("     "));

        inner.add(new JLabel("email (on focus lost)"));
        inner.add(comp3);

        inner.add(new JLabel("     "));
        inner.add(new JLabel("     "));

        inner.add(new JLabel("email (when typing)"));
        inner.add(comp4);

        contents.add(inner, BorderLayout.CENTER);
        contents.add(new JLabel("     "), BorderLayout.NORTH);
        contents.add(new JLabel("     "), BorderLayout.EAST);
        contents.add(new JLabel("     "), BorderLayout.SOUTH);
        contents.add(new JLabel("     "), BorderLayout.WEST);

        frame.setSize(340, 220);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Layer & Validation Demo");
        frame.setVisible(true);

        // validation code ==>

        final Pattern pattern = Pattern
                .compile("(^([a-zA-Z0-9]+([\\.+_-][a-zA-Z0-9]+)*)@(([a-zA-Z0-9]+((\\.|[-]{1,2})[a-zA-Z0-9]+)*)\\.[a-zA-Z]{2,6}))?$");
        final EValidationMessageI vm3 = new EValidationMessage(parent, comp3).stsi();

        comp3.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (StringUtils.isBlank(comp3.getText()) || pattern.matcher(comp3.getText()).matches()) {
                    vm3.setIsValid();
                } else {
                    vm3.setIsInvalid("email invalid");
                }
            }
        });

        final EValidationMessageI vm4 = new EValidationMessage(parent, comp4).stsi();
        vm4.setShowWhenValid(true);

        comp4.addDocumentKeyListener(new DocumentKeyListener() {
            @Override
            public void update(Type type, DocumentEvent e) {
                if (StringUtils.isBlank(comp4.getText()) || pattern.matcher(comp4.getText()).matches()) {
                    vm4.setIsValid();
                } else {
                    vm4.setIsInvalid("email invalid");
                }
            }
        });

        EValidationMessageI vm1 = new EValidationMessage(parent, comp1).stsi();
        EValidationMessageI vm2 = new EValidationMessage(parent, comp2).stsi();
        vm1.setIsInvalid("validation text 1"); // !
        vm2.setShowWhenValid(true);
        vm2.setIsValid(); // !

        // <== validation code
    }
}
