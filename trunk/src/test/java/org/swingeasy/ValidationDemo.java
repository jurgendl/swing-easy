package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * @author Jurgen
 */
public class ValidationDemo {
    public static class EValidationMessage extends EButton implements EValidationMessageI {
        
        private static final long serialVersionUID = 2641254029112205898L;

        protected final JComponent component;

        protected boolean installed = false;

        protected final Container parent;

        protected EValidationMessage() {
            this.component = null;
            this.parent = null;
        }

        public EValidationMessage(final EValidationPane parent, final JComponent component) {
            super(new EIconButtonCustomizer(new Dimension(20, 20)), Resources.getImageResource("bullet_red_small.png"));

            this.component = component;
            this.parent = parent.frontPanel;

            this.setOpaque(false);

            component.addComponentListener(new ComponentListener() {
                @Override
                public void componentHidden(ComponentEvent e) {
                    EValidationMessage.this.hidden();
                }

                @Override
                public void componentMoved(ComponentEvent e) {
                    EValidationMessage.this.place("moved");
                }

                @Override
                public void componentResized(ComponentEvent e) {
                    EValidationMessage.this.place("resized");
                }

                @Override
                public void componentShown(ComponentEvent e) {
                    EValidationMessage.this.shown();
                }
            });

            this.setVisible(false);
        }

        public JComponent getComponent() {
            return this.component;
        }

        /**
         * JDOC
         * 
         * @return
         */
        public EValidationMessage getSimpleThreadSafeInterface() {
            try {
                return EventThreadSafeWrapper.getSimpleThreadSafeInterface(EValidationMessage.class, this, EValidationMessageI.class);
            } catch (Exception ex) {
                System.err.println(ex);
                return this; // no javassist
            }
        }

        /**
         * hidden
         */
        protected void hidden() {
            this.setVisible(false);
        }

        /**
         * install
         */
        protected void install() {
            if (this.installed || (this.parent == null)) {
                return;
            }

            this.parent.add(this);
            this.installed = true;
        }

        protected void place(@SuppressWarnings("unused") String id) {
            if (!this.installed) {
                return;
            }

            Rectangle l = this.component.getBounds();
            int x = l.x + l.width;
            int y = l.y + l.height;
            int iw2 = this.getIcon().getIconWidth() / 2;
            int ih2 = this.getIcon().getIconHeight() / 2;
            int px = x - iw2;
            int py = y - ih2;
            Rectangle r = new Rectangle(px, py, iw2 * 2, ih2 * 2);
            this.setBounds(r);
        }

        /**
         * 
         * @see javax.swing.JLabel#setText(java.lang.String)
         */
        @Override
        public void setText(String text) {
            this.setToolTipText(text);
        }

        /**
         * 
         * @see javax.swing.JComponent#setToolTipText(java.lang.String)
         */
        @Override
        public void setToolTipText(String text) {
            this.install();
            super.setToolTipText(text);

            boolean aFlag = text != null;
            this.setVisible(aFlag);

            if (aFlag) {
                this.place("onset");
            }
        }

        /**
         * shown
         */
        protected void shown() {
            this.setVisible(true);
        }

        /**
         * @see #getSimpleThreadSafeInterface()
         */
        public EValidationMessage stsi() {
            return this.getSimpleThreadSafeInterface();
        }

        /**
         * @see #getSimpleThreadSafeInterface()
         */
        public EValidationMessage STSI() {
            return this.getSimpleThreadSafeInterface();
        }
    }

    public static interface EValidationMessageI extends EComponentI {
        public void setText(String text);

        public void setToolTipText(String text);

        public void setVisible(boolean aFlag);
    }

    public static class EValidationPane extends JLayeredPane {
        private static final long serialVersionUID = 1570586590646022420L;

        protected JPanel frontPanel = new JPanel(null);

        public EValidationPane(Container validates) {
            this.frontPanel.setOpaque(false);
            this.add(validates, new Integer(0), 0);
            this.add(this.frontPanel, new Integer(1), 0);
            this.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    for (int i = 0; i < EValidationPane.this.getComponentCount(); i++) {
                        EValidationPane.this.getComponent(i).setBounds(0, 0, EValidationPane.this.getWidth(), EValidationPane.this.getHeight());
                    }
                }
            });
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel contents = new JPanel(new BorderLayout());

        EValidationPane parent = new EValidationPane(contents); // !
        frame.getContentPane().add(parent, BorderLayout.CENTER); // !

        JTextField invalid1 = new JTextField("invalid1");
        JTextField invalid2 = new JTextField("invalid2");

        JPanel inner = new JPanel(new GridLayout(2, 1));
        inner.add(invalid1);
        inner.add(invalid2);

        contents.add(inner, BorderLayout.CENTER);
        contents.add(new JLabel("     "), BorderLayout.NORTH);
        contents.add(new JLabel("     "), BorderLayout.EAST);
        contents.add(new JLabel("     "), BorderLayout.SOUTH);
        contents.add(new JLabel("     "), BorderLayout.WEST);

        frame.setSize(200, 100);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Layer & Validation Demo");

        EValidationMessage vm1 = new EValidationMessage(parent, invalid1).stsi(); // !
        EValidationMessage vm2 = new EValidationMessage(parent, invalid2).stsi(); // !

        frame.setVisible(true);

        vm1.setText("validation text 1"); // !
        vm2.setText("validation text 2"); // !
    }
}
