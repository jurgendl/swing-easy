package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * @author Jurgen
 */
public class DateDemo {
    private static void addComponents(Container container) {
        container.setLayout(new GridLayout(-1, 1));
        final EDateEditor ede0 = new EDateEditor();
        final EDateTimeEditor edte0 = new EDateTimeEditor();
        {
            container.add(ede0, BorderLayout.NORTH);
            container.add(edte0, BorderLayout.SOUTH);
        }
        final EDateEditor ede = new EDateEditor();
        final EDateTimeEditor edte = new EDateTimeEditor();
        {
            ede.setDate(null);
            container.add(ede, BorderLayout.NORTH);
            edte.setDate(null);
            container.add(edte, BorderLayout.SOUTH);
        }
        JButton log = new JButton("log");
        log.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(ede0.getDate());
                System.out.println(edte0.getDate());
                System.out.println(ede.getDate());
                System.out.println(edte.getDate());
            }
        });
        container.add(log);
    }

    public static void main(String[] args) {
        // SystemSettings.setCurrentLocale(Locale.US);
        UIUtils.niceLookAndFeel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        DateDemo.addComponents(frame.getContentPane());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setTitle("DateDemo");
        frame.setVisible(true);
    }
}
