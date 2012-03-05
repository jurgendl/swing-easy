package org.swingeasy;

import java.awt.Container;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

/**
 * @author Jurgen
 */
public class PopupMenuDemo {
    private static ETextArea addComponents(Container container) throws IOException {
        container.setLayout(new GridLayout(-1, 1));
        String text = new String(Resources.getResource("src/site/resources/version-history.txt"));
        ETextArea jtf = new ETextArea(new ETextAreaConfig(), text);
        container.add(new JScrollPane(jtf));
        return jtf;
    }

    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        @SuppressWarnings("unused")
        ETextArea tc = null;
        try {
            tc = PopupMenuDemo.addComponents(frame.getContentPane());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Demo");
        frame.setVisible(true);
    }
}
