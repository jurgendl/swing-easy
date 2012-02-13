package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * @author Jurgen
 */
public class ETabbedPaneHeader extends JComponent implements ActionListener {
    /** serialVersionUID */
    private static final long serialVersionUID = -1987585120165138408L;

    public ETabbedPaneHeader(String title, Icon icon, ETabbedPaneConfig config) {
        this.setLayout(new BorderLayout());

        if (config.getRotation() == Rotation.DEFAULT) {
            this.add(new JLabel(title, icon, SwingConstants.LEADING), BorderLayout.CENTER);
            if (config.isClosable()) {
                this.makeClosable(BorderLayout.EAST);
            }
        } else if (config.getRotation() == Rotation.CLOCKWISE) {
            this.add(new RotatedLabel(title, icon, true), BorderLayout.CENTER);
            if (config.isClosable()) {
                this.makeClosable(BorderLayout.NORTH);
            }
        } else {
            this.add(new RotatedLabel(title, icon, false), BorderLayout.CENTER);
            if (config.isClosable()) {
                this.makeClosable(BorderLayout.SOUTH);
            }
        }
    }

    /**
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("wants to close");
    }

    private void makeClosable(String loc) {
        EIconButton closeButton = new EIconButton(new Dimension(16, 16), Resources.getImageResource("cross-small.png"));
        closeButton.addActionListener(this);
        this.add(closeButton, loc);
    }
}
