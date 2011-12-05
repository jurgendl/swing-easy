package org.swingeasy;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JList;

/**
 * @author Jurgen
 */
public class ColorListCellRenderer extends DefaultListCellRenderer.UIResource {
    /** serialVersionUID */
    private static final long serialVersionUID = -7605301072046365348L;

    protected Icon emptyIcon;

    public ColorListCellRenderer() {
        BufferedImage bi = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        this.emptyIcon = new ImageIcon(bi);
    }

    private Icon createIcon(Color color) {
        BufferedImage bi = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        g2d.setColor(color);
        g2d.fillRoundRect(0, 0, 14, 14, 4, 4);
        return new ImageIcon(bi);
    }

    /**
     * 
     * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value == null) {
            this.setIcon(this.emptyIcon);
            this.setText(""); //$NON-NLS-1$
            return this;
        }
        Color color = Color.class.cast(value);
        String red = Integer.toHexString(color.getRed());
        if (red.length() == 1) {
            red = "0" + red; //$NON-NLS-1$
        }
        String green = Integer.toHexString(color.getGreen());
        if (green.length() == 1) {
            green = "0" + green; //$NON-NLS-1$
        }
        String blue = Integer.toHexString(color.getBlue());
        if (blue.length() == 1) {
            blue = "0" + blue; //$NON-NLS-1$
        }
        String alpha = Integer.toHexString(color.getAlpha());
        if (alpha.length() == 1) {
            alpha = "0" + alpha; //$NON-NLS-1$
        }
        if (alpha.equals("ff")) { //$NON-NLS-1$
            this.setText(("#" + red + green + blue).toUpperCase()); //$NON-NLS-1$
            this.setIcon(this.createIcon(color));
        } else {
            this.setText(("#" + red + green + blue + "(" + alpha + ")").toUpperCase()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            this.setIcon(this.createIcon(color));
        }
        return this;
    }
}
