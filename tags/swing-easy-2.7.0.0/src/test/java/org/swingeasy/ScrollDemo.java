package org.swingeasy;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 * @author Jurgen
 */
public class ScrollDemo {
    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();
        JFrame f = new JFrame();
        JTextPane p = new JTextPane();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append(i).append("\n");
        }
        p.setText(sb.toString());
        JScrollPane jsp = new JScrollPane(p);

        int width = (int) jsp.getPreferredSize().getWidth();
        jsp.getHorizontalScrollBar().setUnitIncrement(width / 100);
        jsp.getHorizontalScrollBar().setBlockIncrement(width / 10);
        System.out.println(width);

        int height = (int) jsp.getPreferredSize().getHeight();
        jsp.getVerticalScrollBar().setUnitIncrement(height / 100);
        jsp.getVerticalScrollBar().setBlockIncrement(height / 10);
        System.out.println(height);

        f.getContentPane().add(jsp, BorderLayout.CENTER);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(400, 400);
        f.setVisible(true);
    }
}
