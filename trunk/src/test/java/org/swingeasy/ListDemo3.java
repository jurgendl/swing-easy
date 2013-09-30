package org.swingeasy;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * @author Jurgen
 */
public class ListDemo3 {
    public static class CheckBoxList extends JList {
        protected class CellRenderer implements ListCellRenderer {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JCheckBox checkbox = (JCheckBox) value;
                checkbox.setBackground(isSelected ? CheckBoxList.this.getSelectionBackground() : CheckBoxList.this.getBackground());
                checkbox.setForeground(isSelected ? CheckBoxList.this.getSelectionForeground() : CheckBoxList.this.getForeground());
                checkbox.setEnabled(CheckBoxList.this.isEnabled());
                checkbox.setFont(CheckBoxList.this.getFont());
                checkbox.setFocusPainted(false);
                checkbox.setBorderPainted(true);
                checkbox.setBorder(isSelected ? UIManager.getBorder("List.focusCellHighlightBorder") : CheckBoxList.noFocusBorder);
                return checkbox;
            }
        }

        private static final long serialVersionUID = 7861263715673092368L;

        protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

        public CheckBoxList() {
            this.setCellRenderer(new CellRenderer());

            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int index = CheckBoxList.this.locationToIndex(e.getPoint());

                    if (index != -1) {
                        JCheckBox checkbox = (JCheckBox) CheckBoxList.this.getModel().getElementAt(index);
                        checkbox.setSelected(!checkbox.isSelected());
                        CheckBoxList.this.repaint();
                    }
                }
            });

            this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }

        public void addCheckbox(JCheckBox checkBox) {
            ListModel currentList = this.getModel();
            JCheckBox[] newList = new JCheckBox[currentList.getSize() + 1];
            for (int i = 0; i < currentList.getSize(); i++) {
                newList[i] = (JCheckBox) currentList.getElementAt(i);
            }
            newList[newList.length - 1] = checkBox;
            this.setListData(newList);
        }
    }

    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CheckBoxList cbl = new CheckBoxList();
        for (int i = 0; i < 10; i++) {
            cbl.addCheckbox(new JCheckBox("item " + i));
        }
        f.getContentPane().add(new JScrollPane(cbl));
        f.setSize(400, 400);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
