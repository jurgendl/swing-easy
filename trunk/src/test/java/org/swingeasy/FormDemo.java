package org.swingeasy;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.swingeasy.form.FormBuilder;

/**
 * @author Jurgen
 */
public class FormDemo {
    private static void addComponents(Container container) {
        FormBuilder builder = new FormBuilder(container, 2);
        builder.setDebug(true);

        builder.addTitle("Somewhat longer than label - title 1", 2);
        builder.addComponent("Label1", new JTextField());
        builder.addComponent("Label2", new JTextField());

        builder.addTitle("Somewhat longer than label - title 2");
        builder.addTitle("Somewhat longer than label - title 3");
        builder.addComponent("Label3", new JTextField(), 2);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        FormDemo.addComponents(frame.getContentPane());
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);
        frame.setTitle("FormDemo");
        frame.setVisible(true);
    }
}
