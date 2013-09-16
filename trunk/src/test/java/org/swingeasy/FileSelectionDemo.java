package org.swingeasy;

import java.awt.GridLayout;

import javax.swing.JFrame;

/**
 * @author Jurgen
 */
public class FileSelectionDemo {
    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(300, 140);
        f.setLocationRelativeTo(null);
        f.getContentPane().setLayout(new GridLayout(-1, 1));
        f.getContentPane().add(new FileSelection());
        f.getContentPane().add(new FileSelection("pdf"));
        f.getContentPane().add(new FileSelection("pdf", "txt"));
        f.setVisible(true);
    }
}
