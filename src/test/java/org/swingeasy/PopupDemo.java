package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * @author Jurgen
 */
public class PopupDemo {
    private static void addComponents(Container container) {
        EListConfig cfg = new EListConfig();
        cfg.setFilterable(true);
        EList<String> options = new EList<String>(cfg);
        for (int i = 0; i < 26; i++) {
            options.addRecord(new EListRecord<String>((char) ('a' + i) + " option " + i));
        }
        options.setDefaultRenderer(Object.class, new DefaultListCellRenderer() {
            private static final long serialVersionUID = -2332228089292536681L;

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                value = "<html><i>" + value + "</i><html>";
                DefaultListCellRenderer listCellRendererComponent = (DefaultListCellRenderer) super.getListCellRendererComponent(list, value, index,
                        isSelected, cellHasFocus);
                return listCellRendererComponent;
            }
        });
        EListFilterComponent<String> filtercomponent = options.getFiltercomponent();
        filtercomponent.setLive(true);
        final ETextField input = filtercomponent.getInput();
        final JPopupMenu popup = new JPopupMenu();
        popup.setBorderPainted(false);
        popup.add(new JScrollPane(options));
        popup.setLightWeightPopupEnabled(true);
        container.add(input, BorderLayout.NORTH);
        input.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                popup.setVisible(true);
            }

            @Override
            public void focusLost(FocusEvent e) {
                popup.setVisible(false);
            }
        });
        input.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                //
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
                System.out.println("*moved");
                PopupDemo.reloc(input, popup);
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
                //
            }
        });
        input.addComponentListener(new ComponentListener() {
            @Override
            public void componentHidden(ComponentEvent e) {
                //
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                System.out.println("moved");
                PopupDemo.reloc(input, popup);
            }

            @Override
            public void componentResized(ComponentEvent e) {
                System.out.println("resized");
                PopupDemo.reloc(input, popup);
            }

            @Override
            public void componentShown(ComponentEvent e) {
                //
            }
        });
    }

    public static void main(String[] args) {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        PopupDemo.addComponents(frame.getContentPane());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setTitle("PopupDemo");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(true);
            }
        });
    }

    private static void reloc(ETextField input, final JPopupMenu popup) {
        try {
            Dimension dim = new Dimension(input.getWidth(), Math.max(100, popup.getPreferredSize().height));
            System.out.println(dim);
            popup.setSize(dim);
            popup.setPreferredSize(dim);
            int x = (input.getLocationOnScreen().x + input.getWidth()) - (int) popup.getPreferredSize().getWidth();
            int y = input.getLocationOnScreen().y + input.getHeight();
            Point loc = new Point(x, y);
            System.out.println(loc);
            popup.setLocation(loc);
            popup.setVisible(false);
            popup.setVisible(true);
        } catch (Exception ex) {
            //
        }
    }
}
