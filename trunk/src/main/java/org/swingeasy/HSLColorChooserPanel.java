package org.swingeasy;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.Icon;
import javax.swing.JSlider;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Jurgen
 */
public class HSLColorChooserPanel extends AbstractColorChooserPanel implements ChangeListener {
    private static final long serialVersionUID = -5185392436348052465L;

    protected boolean isAdjusting;

    // Hue: represents the color. Its value is specified in degrees from 0 – 360. Red is 0, green is 120 and blue is 240.
    // Saturation: represents the purity of the color. Its value is specified as a percentage. 100 percent is fully saturated and 0 percent is gray.
    // Luminance: represents the brighness of the color. Its value is specified as a percent. 100 percent is white and 0 percent is black.
    protected HSLColor hue_saturation_luminance;

    protected JSlider hueSlider;

    protected JSlider saturationSlider;

    protected JSlider luminanceSlider;

    /**
     * 
     * @see javax.swing.colorchooser.AbstractColorChooserPanel#buildChooser()
     */
    @Override
    protected void buildChooser() {
        this.hueSlider = new JSlider(0, 360);
        this.saturationSlider = new JSlider(0, 100);
        this.luminanceSlider = new JSlider(0, 100);
        this.setLayout(new GridLayout(3, 1));
        this.add(this.hueSlider);
        this.add(this.saturationSlider);
        this.add(this.luminanceSlider);
        this.setColorFromModel();
        this.hueSlider.addChangeListener(this);
        this.saturationSlider.addChangeListener(this);
        this.luminanceSlider.addChangeListener(this);
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

    protected void setColorFromModel() {
        this.hue_saturation_luminance = new HSLColor(this.getColorSelectionModel().getSelectedColor());
        this.hueSlider.setValue((int) this.hue_saturation_luminance.getHue());
        this.saturationSlider.setValue((int) this.hue_saturation_luminance.getSaturation());
        this.luminanceSlider.setValue((int) this.hue_saturation_luminance.getLuminance());
    }

    protected void setColorInModel() {
        this.hue_saturation_luminance.adjustHue(this.hueSlider.getValue());
        this.hue_saturation_luminance.adjustSaturation(this.saturationSlider.getValue());
        this.hue_saturation_luminance.adjustLuminance(this.luminanceSlider.getValue());
        Color color = this.hue_saturation_luminance.getRGB();
        this.getColorSelectionModel().setSelectedColor(color);
    }

    /**
     * 
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        this.setColorInModel();
    }

    /**
     * 
     * @see javax.swing.colorchooser.AbstractColorChooserPanel#updateChooser()
     */
    @Override
    public void updateChooser() {
        if (!this.isAdjusting) {
            this.isAdjusting = true;
            this.setColorFromModel();
            this.isAdjusting = false;
        }
    }
}
