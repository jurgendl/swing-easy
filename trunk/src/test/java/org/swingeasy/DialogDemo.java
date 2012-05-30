package org.swingeasy;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

/**
 * @author Jurgen
 */
public class DialogDemo {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setTitle("Demo");
        frame.setSize(400, 400);
        frame.setLocation(400, 400);
        frame.setVisible(true);
        JLabel l = new JLabel("center");
        frame.getContentPane().add(l);
        JLabel n = new JLabel("n");
        frame.getContentPane().add(n, BorderLayout.NORTH);
        CustomizableOptionPane.showCustomDialog(n, new JLabel("text"), "title", MessageType.QUESTION, OptionType.OK, null, null);
    }
}
