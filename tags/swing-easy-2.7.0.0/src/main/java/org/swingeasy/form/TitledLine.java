package org.swingeasy.form;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.UIManager;

public class TitledLine extends JComponent {
    class Line extends JComponent {
        private static final long serialVersionUID = 7859403660647306243L;

        private int thickness = 2;

        /**
         * @see javax.swing.JComponent#getPreferredSize()
         */
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(this.thickness, this.thickness);
        }

        /**
         * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
         */
        @Override
        protected void paintComponent(Graphics _g) {
            Color c = UIManager.getColor("TitledBorder.titleColor");
            c = new Color(c.getRed(), c.getGreen(), c.getBlue(), 150);
            Graphics2D g = (Graphics2D) _g;
            g.setColor(c);
            g.setStroke(new BasicStroke(this.thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
            g.drawLine(this.thickness / 2, this.thickness / 2, this.getWidth() - (this.thickness / 2), this.thickness / 2);
            g.dispose();
        }
    }

    private static final long serialVersionUID = 5956004089983509968L;

    public TitledLine(String title) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(title);
        label.setFont(UIManager.getFont("TitledBorder.font"));
        Color color = UIManager.getColor("TitledBorder.titleColor");
        label.setForeground(color);
        this.add(label);
        Line comp = new Line();
        comp.setForeground(color);
        this.add(comp);
        this.setBorder(BorderFactory.createEmptyBorder(2/* top */, 0/* left */, 3/* bottom */, 5/* right */));
    }

    /**
     * @see javax.swing.JComponent#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(true);
    }
}
