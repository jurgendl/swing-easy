package org.swingeasy;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class ComboBoxDemo {
    public static void main(String[] args) {
        EComboBox<String> cc = new EComboBox<String>(new EComboBoxConfig(), "111", "222", "333");
        JFrame f = new JFrame();
        f.getContentPane().add(cc, BorderLayout.CENTER);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
