package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Jurgen
 */
public class RoundedBorderDemo {
    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        final Window w = new Window(f);
        w.setLayout(new BorderLayout());
        JPanel main = new JPanel();
        w.add(main);
        w.setSize(400, 200);
        w.setLocationByPlatform(true);
        UIUtils.makeDraggable(main);
        UIUtils.rounded(w);
        UIUtils.translucent(w);
        w.setVisible(true);
    }
}
