package org.swingeasy;

import java.awt.Color;

/**
 * @author Jurgen
 */
public class ETextAreaDefaultHighlightPainter extends javax.swing.text.DefaultHighlighter.DefaultHighlightPainter implements
        ETextAreaHighlightPainter {
    protected Color localColor;

    public ETextAreaDefaultHighlightPainter(Color color) {
        super(color);
        this.localColor = color;
    }

    /**
     * 
     * @see javax.swing.text.DefaultHighlighter.DefaultHighlightPainter#getColor()
     */
    @Override
    public Color getColor() {
        return this.localColor;
    }

    public void setColor(Color color) {
        this.localColor = color;
    }
}
