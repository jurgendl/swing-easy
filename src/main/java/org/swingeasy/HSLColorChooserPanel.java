package org.swingeasy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Polygon;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Jurgen
 */
public class HSLColorChooserPanel extends AbstractColorChooserPanel implements ChangeListener {
    private class Rainbow extends JPanel {
        private static final long serialVersionUID = 2312979126069515192L;

        public Rainbow() {
            this.setPreferredSize(new Dimension(360, 10));
            this.setSize(new Dimension(360, 10));
            this.setMinimumSize(new Dimension(360, 10));
            this.setBorder(BorderFactory.createEtchedBorder());
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            for (int i = 0; i < 360; i++) {
                HSLColor color = new HSLColor(i, HSLColorChooserPanel.this.hue_saturation_luminance.getSaturation(),
                        HSLColorChooserPanel.this.hue_saturation_luminance.getLuminance());
                Color rgb = color.calcRGB();
                g2d.setColor(rgb);
                g2d.drawLine(i, 0, i, this.getHeight());
            }
            g2d.setColor(Color.black);
            Polygon p = new Polygon();
            p.addPoint(-10, 0);
            p.addPoint(0, -10);
            p.addPoint(10, 0);
            g2d.translate((int) HSLColorChooserPanel.this.hue_saturation_luminance.getHue(), this.getHeight());
            g2d.fillPolygon(p);
            g2d.translate(-(int) HSLColorChooserPanel.this.hue_saturation_luminance.getHue(), -this.getHeight());
            g2d.dispose();
        }
    }

    private static final long serialVersionUID = -5185392436348052465L;

    protected boolean isAdjusting;

    // Hue: represents the color. Its value is specified in degrees from 0 – 360. Red is 0, green is 120 and blue is 240.
    // Saturation: represents the purity of the color. Its value is specified as a percentage. 100 percent is fully saturated and 0 percent is gray.
    // Luminance: represents the brighness of the color. Its value is specified as a percent. 100 percent is white and 0 percent is black.
    protected HSLColor hue_saturation_luminance;

    protected JSlider hueSlider;

    protected JSlider saturationSlider;

    protected JSlider luminanceSlider;

    protected Rainbow rainbow;

    /**
     * 
     * @see javax.swing.colorchooser.AbstractColorChooserPanel#buildChooser()
     */
    @SuppressWarnings("hiding")
    @Override
    protected void buildChooser() {
        this.setLayout(new GridLayout(4, 1));
        this.setBorder(BorderFactory.createRaisedBevelBorder());

        {
            this.rainbow = new Rainbow();
            this.add(new Rainbow(), null);
        }

        {
            JSlider hueSlider = new JSlider(0, 360);
            Dictionary<Integer, JComponent> labels = new Hashtable<Integer, JComponent>();
            labels.put(0, this.newLabel("Red", Color.RED));
            labels.put(120, this.newLabel("Green", Color.GREEN));
            labels.put(240, this.newLabel("Blue", Color.BLUE));
            labels.put(360, this.newLabel("Red", Color.RED));
            hueSlider.setLabelTable(labels);
            hueSlider.setMajorTickSpacing(90);
            hueSlider.setMinorTickSpacing(30);
            hueSlider.setPaintTicks(false);
            hueSlider.setPaintLabels(false);
            hueSlider.setPaintTrack(false);
            this.add(hueSlider, null);
            hueSlider.addChangeListener(this);
            this.hueSlider = hueSlider;
        }

        {
            JSlider saturationSlider = new JSlider(0, 100);
            @SuppressWarnings("unchecked")
            Dictionary<Integer, JComponent> labels = saturationSlider.createStandardLabels(10, 0);
            saturationSlider.setLabelTable(labels);
            saturationSlider.setMajorTickSpacing(10);
            saturationSlider.setMinorTickSpacing(5);
            saturationSlider.setPaintTicks(true);
            saturationSlider.setPaintLabels(true);
            saturationSlider.setPaintTrack(true);
            this.add(saturationSlider, null);
            saturationSlider.addChangeListener(this);
            this.saturationSlider = saturationSlider;
        }

        {
            JSlider luminanceSlider = new JSlider(0, 100);
            @SuppressWarnings("unchecked")
            Dictionary<Integer, JComponent> labels = luminanceSlider.createStandardLabels(10, 0);
            luminanceSlider.setLabelTable(labels);
            luminanceSlider.setMajorTickSpacing(10);
            luminanceSlider.setMinorTickSpacing(5);
            luminanceSlider.setPaintTicks(true);
            luminanceSlider.setPaintLabels(true);
            luminanceSlider.setPaintTrack(true);
            this.add(luminanceSlider, null);
            luminanceSlider.addChangeListener(this);
            this.luminanceSlider = luminanceSlider;
        }
    }

    /**
     * 
     * @see javax.swing.colorchooser.AbstractColorChooserPanel#getDisplayName()
     */
    @Override
    public String getDisplayName() {
        return "HSL";
    }

    /**
     * 
     * @see javax.swing.colorchooser.AbstractColorChooserPanel#getLargeDisplayIcon()
     */
    @Override
    public Icon getLargeDisplayIcon() {
        return null;
    }

    /**
     * 
     * @see javax.swing.colorchooser.AbstractColorChooserPanel#getSmallDisplayIcon()
     */
    @Override
    public Icon getSmallDisplayIcon() {
        return null;
    }

    protected JComponent newLabel(String string, Color color) {
        JLabel label = new JLabel(string);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        label.setForeground(color);
        return label;
    }

    protected void setColor(Color newColor) {
        this.hue_saturation_luminance = new HSLColor(newColor);

        this.hueSlider.setValue((int) this.hue_saturation_luminance.getHue());
        this.saturationSlider.setValue((int) this.hue_saturation_luminance.getSaturation());
        this.luminanceSlider.setValue((int) this.hue_saturation_luminance.getLuminance());

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                HSLColorChooserPanel.this.validate();
                HSLColorChooserPanel.this.repaint();
            }
        });
    }

    /**
     * 
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        if (!this.isAdjusting) {
            int h = this.hueSlider.getValue();
            int s = this.saturationSlider.getValue();
            int l = this.luminanceSlider.getValue();

            this.hue_saturation_luminance.setHue(h);
            this.hue_saturation_luminance.setSaturation(s);
            this.hue_saturation_luminance.setLuminance(l);

            Color color = this.hue_saturation_luminance.calcRGB();
            this.getColorSelectionModel().setSelectedColor(color);

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    HSLColorChooserPanel.this.validate();
                    HSLColorChooserPanel.this.repaint();
                }
            });
        }
    }

    /**
     * 
     * @see javax.swing.colorchooser.AbstractColorChooserPanel#updateChooser()
     */
    @Override
    public void updateChooser() {
        if (!this.isAdjusting) {
            this.isAdjusting = true;
            this.setColor(this.getColorFromModel());
            this.isAdjusting = false;
        }
    }
}
