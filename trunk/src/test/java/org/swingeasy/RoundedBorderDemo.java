package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

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
        w.setSize(400, 200);
        w.setLocationByPlatform(true);
        w.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                w.dispose();
            }
        });
        UIUtils.rounded(w);
        UIUtils.translucent(w);
        w.setVisible(true);
    }
}
