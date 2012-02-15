package org.swingeasy;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.swingeasy.EComponentPopupMenu.TextComponentPopupMenuInterface;

/**
 * @author Jurgen
 */
public class PopupMenuDemo {
    private static void addComponents(Container container) {
        container.setLayout(new GridLayout(-1, 1));
        JTextField jtf = new JTextField("popup menu");
        EComponentPopupMenu.installTextComponentPopupMenu(new TextComponentPopupMenuInterface(jtf));
        container.add(jtf);
        container.add(new JEditorPane());
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        PopupMenuDemo.addComponents(frame.getContentPane());
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Demo");
        frame.setVisible(true);
    }
}
