package org.swingeasy;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * @author Jurgen
 */
public class DateDemo {
    private static void addComponents(Container container) {
        container.add(new EDateEditor());
    }

    public static void main(String[] args) {
        // SystemSettings.setCurrentLocale(Locale.US);
        UIUtils.niceLookAndFeel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        DateDemo.addComponents(frame.getContentPane());
        frame.setSize(400, 60);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Demo");
        frame.setVisible(true);
    }
}
