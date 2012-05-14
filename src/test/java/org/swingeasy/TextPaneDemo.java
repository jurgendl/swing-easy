package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.text.html.HTMLEditorKit;

/**
 * @author Jurgen
 */
public class TextPaneDemo {
    private static void addComponents(Container container) {
        ETextPane pane = new ETextPane(new HTMLEditorKit());
        container.add(pane);
        container.add(pane.getToolbar(), BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        TextPaneDemo.addComponents(frame.getContentPane());
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setTitle("TextPaneDemo");
        frame.setVisible(true);
    }
}
