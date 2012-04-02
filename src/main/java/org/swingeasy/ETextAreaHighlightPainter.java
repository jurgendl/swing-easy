package org.swingeasy;

import java.awt.Color;

import javax.swing.text.Highlighter.HighlightPainter;

/**
 * @author Jurgen
 */
public interface ETextAreaHighlightPainter extends HighlightPainter {
    public void setColor(Color color);
}
