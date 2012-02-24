package org.swingeasy;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * @author Jurgen
 */
public class PopupMenuDemo {
    private static void addComponents(Container container) {
        container.setLayout(new GridLayout(-1, 1));
        JTextField jtf = new JTextField("popup menu");
        EComponentPopupMenu.installTextComponentPopupMenu(jtf);
        container.add(jtf);
    }

    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        PopupMenuDemo.addComponents(frame.getContentPane());
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Demo");
        frame.setVisible(true);
    }
}
