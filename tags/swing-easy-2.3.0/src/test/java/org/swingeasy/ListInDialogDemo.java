package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * @author Jurgen
 */
public class ListInDialogDemo {
    static class DemoValue implements Comparable<DemoValue> {
        Number number;

        public DemoValue(Number number) {
            this.number = number;
        }

        @Override
        public int compareTo(final DemoValue other) {
            return new CompareToBuilder().append(this.number, other.number).toComparison();
        }

        @Override
        public String toString() {
            return String.valueOf(this.number);
        }
    }

    public static void main(String[] args) {
        try {
            UIUtils.niceLookAndFeel();

            JFrame parent = new JFrame();
            parent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            parent.setVisible(true);

            final EList<DemoValue> list = new EList<DemoValue>(new EListConfig());
            JPanel container = new JPanel(new BorderLayout());
            container.add(new JScrollPane(list), BorderLayout.CENTER);
            container.add(list.getSearchComponent(), BorderLayout.NORTH);

            final Random r = new Random(256955466579946l);
            for (int i = 0; i < 1000; i++) {
                list.addRecord(new EListRecord<DemoValue>(new DemoValue(r.nextInt(1000))));
            }

            ResultType returnValue = CustomizableOptionPane.showCustomDialog(parent, container, "Demo", MessageType.QUESTION, OptionType.OK_CANCEL,
                    parent.getIconImage() == null ? null : new ImageIcon(parent.getIconImage()), new OptionPaneCustomizer() {
                        @Override
                        public void customize(Component parentComponent, MessageType messageType, OptionType optionType, final JOptionPane pane,
                                final JDialog dialog) {
                            list.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    if ((e.getClickCount() == 2) && (e.getButton() == MouseEvent.BUTTON1)) {
                                        pane.setValue(new Integer(0));
                                        dialog.dispose();
                                    }
                                }
                            });
                        }
                    });
            System.out.println(returnValue + ": " + list.getSelectedRecord());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
