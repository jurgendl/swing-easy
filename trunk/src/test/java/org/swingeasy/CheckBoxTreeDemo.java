package org.swingeasy;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class CheckBoxTreeDemo {
    public static void main(String[] args) {
        try {
            UIUtils.lookAndFeel();
            final JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            ECheckBoxTreeNode<String> root = new ECheckBoxTreeNode<String>("on1", true);
            root.add(new ECheckBoxTreeNode<String>("on11", true));
            root.add(new ECheckBoxTreeNode<String>("on12", true));
            ECheckBoxTree<String> tree = new ECheckBoxTree<String>(root);
            frame.getContentPane().add(new JScrollPane(tree), BorderLayout.CENTER);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    frame.setSize(200, 200);
                    frame.setVisible(true);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
