package org.swingeasy.wizard;

import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * @author Jurgen
 */
public class WizardPage {
    private String title;

    private String description;

    public WizardPage() {
        super();
    }

    public WizardPage(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public JComponent createComponent() {
        JPanel jp = new JPanel(new FlowLayout());
        return jp;
    }

    public String getDescription() {
        return this.description;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.title;
    }
}
