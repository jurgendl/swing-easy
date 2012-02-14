package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * @author Jurgen
 */
public class ETabbedPaneHeader extends JComponent {
    /** serialVersionUID */
    private static final long serialVersionUID = -1987585120165138408L;

    public static final String ACTION_MINIMIZE = "minimize";

    public static final String ACTION_CLOSE = "close";

    protected final String title;

    protected final Icon icon;

    protected final String tip;

    public ETabbedPaneHeader(String title, Icon icon, String tip, ETabbedPaneConfig config, ActionListener actionlistener) {
        this.title = title;
        this.icon = icon;
        this.tip = tip;

        this.setLayout(new BorderLayout());

        JPanel container = new JPanel(new FlowLayout());
        container.setOpaque(false);

        if (config.getRotation() == Rotation.DEFAULT) {
            JLabel label = new JLabel(title, icon, SwingConstants.LEADING);
            label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            this.add(label, BorderLayout.CENTER);
            this.add(container, BorderLayout.EAST);
            if (config.isClosable()) {
                this.makeClosable(container, actionlistener);
            }
            if (config.isMinimizable()) {
                this.makeMinimizable(container, actionlistener);
            }
        } else if (config.getRotation() == Rotation.CLOCKWISE) {
            RotatedLabel label = new RotatedLabel(title, icon, true);
            label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            this.add(label, BorderLayout.CENTER);
            this.add(container, BorderLayout.SOUTH);
            if (config.isClosable()) {
                this.makeClosable(container, actionlistener);
            }
            if (config.isMinimizable()) {
                this.makeMinimizable(container, actionlistener);
            }
        } else {
            RotatedLabel label = new RotatedLabel(title, icon, false);
            label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            this.add(label, BorderLayout.CENTER);
            this.add(container, BorderLayout.NORTH);
            if (config.isClosable()) {
                this.makeClosable(container, actionlistener);
            }
            if (config.isMinimizable()) {
                this.makeMinimizable(container, actionlistener);
            }
        }
    }

    private void makeClosable(Container container, ActionListener actionlistener) {
        EIconButton closeButton = new EIconButton(new Dimension(10, 10), Resources.getImageResource("cross-small.png"));
        closeButton.setActionCommand(ETabbedPaneHeader.ACTION_CLOSE);
        closeButton.addActionListener(actionlistener);
        container.add(closeButton);
    }

    private void makeMinimizable(Container container, ActionListener actionlistener) {
        EIconButton minimizeButton = new EIconButton(new Dimension(10, 10), Resources.getImageResource("bullet_arrow_down_small.png"));
        minimizeButton.setActionCommand(ETabbedPaneHeader.ACTION_MINIMIZE);
        minimizeButton.addActionListener(actionlistener);
        container.add(minimizeButton);
    }
}
