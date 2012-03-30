package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.swingeasy.validation.EValidationMessage;
import org.swingeasy.validation.EValidationPane;

/**
 * @author Jurgen
 */
public class ValidationDemo {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel contents = new JPanel(new BorderLayout());

        EValidationPane parent = new EValidationPane(contents); // !
        frame.getContentPane().add(parent, BorderLayout.CENTER); // !

        JTextField invalid1 = new JTextField("invalid1");
        JTextField invalid2 = new JTextField("invalid2");

        JPanel inner = new JPanel(new GridLayout(2, 1));
        inner.add(invalid1);
        inner.add(invalid2);

        contents.add(inner, BorderLayout.CENTER);
        contents.add(new JLabel("     "), BorderLayout.NORTH);
        contents.add(new JLabel("     "), BorderLayout.EAST);
        contents.add(new JLabel("     "), BorderLayout.SOUTH);
        contents.add(new JLabel("     "), BorderLayout.WEST);

        frame.setSize(200, 100);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Layer & Validation Demo");

        EValidationMessage vm1 = new EValidationMessage(parent, invalid1).stsi(); // !
        EValidationMessage vm2 = new EValidationMessage(parent, invalid2).stsi(); // !

        frame.setVisible(true);

        vm1.setText("validation text 1"); // !
        vm2.setText("validation text 2"); // !
    }
}
