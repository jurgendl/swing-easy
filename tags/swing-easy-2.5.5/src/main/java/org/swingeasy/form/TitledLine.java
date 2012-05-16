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
    private static final long serialVersionUID = 5956004089983509968L;

    public TitledLine(String title) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(title);
        label.setFont(UIManager.getFont("TitledBorder.font"));
        Color color = UIManager.getColor("TitledBorder.titleColor");
        label.setForeground(color);
        add(label);
        Line comp = new Line();
        comp.setForeground(color);
        add(comp);
        setBorder(BorderFactory.createEmptyBorder(2/* top */, 0/* left */, 3/* bottom */, 5/* right */));
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(true);
    }

    class Line extends JComponent {
        private static final long serialVersionUID = 7859403660647306243L;

        private int thickness = 2;

        private Color color;

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(thickness, thickness);
        }

        @Override
        protected void paintComponent(Graphics _g) {
            Color c = color;
            if (c == null) {
                c = UIManager.getColor("TitledBorder.titleColor");
                c = new Color(c.getRed(), c.getGreen(), c.getBlue(), 150);
            }
            Graphics2D g = (Graphics2D) _g;
            g.setColor(c);
            g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
            g.drawLine(thickness / 2, thickness / 2, getWidth() - (thickness / 2), thickness / 2);
            g.dispose();
        }
    }
}
