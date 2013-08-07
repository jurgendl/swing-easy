package org.swingeasy;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 * @author Jurgen
 */
public class GradientPanel extends JPanel {
    public static enum GradientOrientation {
        HORIZONTAL, VERTICAL, DIAGONAL, OFF;
    }

    private static final long serialVersionUID = -3019198419458872568L;

    private Color gradientColorStart = Color.white;

    private Color gradientColorEnd = Color.LIGHT_GRAY;

    private GradientOrientation orientation = GradientOrientation.DIAGONAL;

    public Color getGradientColorEnd() {
        return this.gradientColorEnd;
    }

    public Color getGradientColorStart() {
        return this.gradientColorStart;
    }

    public GradientOrientation getOrientation() {
        return this.orientation;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.orientation != GradientOrientation.OFF) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            int w = this.getWidth();
            int h = this.getHeight();
            GradientPaint gp;
            switch (this.orientation) {
                case HORIZONTAL:
                    gp = new GradientPaint(0, 0, this.gradientColorStart, w, 0, this.gradientColorEnd);
                    break;
                case VERTICAL:
                    gp = new GradientPaint(0, 0, this.gradientColorStart, 0, h, this.gradientColorEnd);
                    break;
                default:
                case DIAGONAL:
                    gp = new GradientPaint(0, 0, this.gradientColorStart, w, h, this.gradientColorEnd);
                    break;
                case OFF:
                    gp = null;
                    break;
            }
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }

    public void setGradientColorEnd(Color gradientColorEnd) {
        this.gradientColorEnd = gradientColorEnd;
    }

    public void setGradientColorStart(Color gradientColorStart) {
        this.gradientColorStart = gradientColorStart;
    }

    public void setOrientation(GradientOrientation orientation) {
        this.orientation = orientation;
    }
}