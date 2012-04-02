package org.swingeasy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import javax.swing.text.View;

/**
 * @see javax.swing.text.html.HTMLEditorKit.NavigateLinkAction.FocusHighlightPainter
 */
public class ETextAreaFocusHighlightPainter extends javax.swing.text.DefaultHighlighter.DefaultHighlightPainter implements ETextAreaHighlightPainter {
    protected Color localColor;

    public ETextAreaFocusHighlightPainter(Color color) {
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

    /**
     * 
     * @see javax.swing.text.DefaultHighlighter.DefaultHighlightPainter#paintLayer(java.awt.Graphics, int, int, java.awt.Shape,
     *      javax.swing.text.JTextComponent, javax.swing.text.View)
     */
    @Override
    public Shape paintLayer(Graphics g, int offs0, int offs1, Shape bounds, JTextComponent c, View view) {
        Color color = this.getColor();

        if (color == null) {
            g.setColor(c.getSelectionColor());
        } else {
            g.setColor(color);
        }
        if ((offs0 == view.getStartOffset()) && (offs1 == view.getEndOffset())) {
            // Contained in view, can just use bounds.
            Rectangle alloc;
            if (bounds instanceof Rectangle) {
                alloc = (Rectangle) bounds;
            } else {
                alloc = bounds.getBounds();
            }
            g.drawRect(alloc.x, alloc.y, alloc.width - 1, alloc.height);
            return alloc;
        }
        // Should only render part of View.
        try {
            // --- determine locations ---
            Shape shape = view.modelToView(offs0, Position.Bias.Forward, offs1, Position.Bias.Backward, bounds);
            Rectangle r = (shape instanceof Rectangle) ? (Rectangle) shape : shape.getBounds();
            g.drawRect(r.x, r.y, r.width - 1, r.height);
            return r;
        } catch (BadLocationException e) {
            // can't render
        }
        // Only if exception
        return null;
    }

    public void setColor(Color color) {
        this.localColor = color;
    }
}