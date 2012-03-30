package org.swingeasy;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import org.swingeasy.form.FormBuilder;

/**
 * @author Jurgen
 */
public class FormDemo {
    private static void addComponents(Container container) {
        FormBuilder builder = new FormBuilder(container, 2);
        builder.setDebug(false);

        builder.addTitle("Somewhat longer than label - title 1", 2);
        builder.addComponent("Label1", new ETextField(new ETextFieldConfig()));
        builder.addComponent("Label2", new ETextField(new ETextFieldConfig()));

        builder.addTitle("Somewhat longer than label - title 2");
        builder.addTitle("Somewhat longer than label - title 3");
        builder.addComponent("Label3", new ETextField(new ETextFieldConfig()), 2);

        builder.addComponent("Label4", new JScrollPane(new ETextArea(new ETextAreaConfig())), 1, 3);
        builder.addTitle("Somewhat longer than label - title 4");
        builder.addComponent("Label5", new ETextField(new ETextFieldConfig()));
        builder.addComponent("Label6", new ETextField(new ETextFieldConfig()));
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
