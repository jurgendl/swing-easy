package org.swingeasy;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class TreeDemo {
    private static class DummyNode extends ETreeNode<String> {
        private static final long serialVersionUID = 4389106694997553842L;

        public DummyNode() {
            this("0");
        }

        public DummyNode(String userObject) {
            super(userObject);
        }

        @Override
        protected void initChildren(Vector<ETreeNode<String>> list) {
            System.out.println("lazy-init " + this);
            if (!this.getUserObject().toString().endsWith("5")) {
                for (int i = 0; i < 10; i++) {
                    String s = this.getUserObject() + "." + i;
                    list.add(new DummyNode(s));
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIUtils.lookAndFeel();
            final JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            ETree<String> tree = new ETree<String>(new DummyNode());
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
