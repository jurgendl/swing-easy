package org.swingeasy.validation;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JComponent;

import org.swingeasy.EButton;
import org.swingeasy.EIconButtonCustomizer;
import org.swingeasy.EventThreadSafeWrapper;
import org.swingeasy.Resources;

/**
 * @author Jurgen
 */
public class EValidationMessage extends EButton implements EValidationMessageI {
    private static final long serialVersionUID = 2641254029112205898L;

    protected final JComponent component;

    protected boolean installed = false;

    protected final Container parent;

    protected EValidationMessage() {
        this.component = null;
        this.parent = null;
    }

    public EValidationMessage(final EValidationPane parent, final JComponent component) {
        super(new EIconButtonCustomizer(new Dimension(20, 20)), Resources.getImageResource("bullet_red_small.png"));

        this.component = component;
        this.parent = parent.frontPanel;

        this.setOpaque(false);

        component.addComponentListener(new ComponentListener() {
            @Override
            public void componentHidden(ComponentEvent e) {
                EValidationMessage.this.hidden();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                EValidationMessage.this.place("moved");
            }

            @Override
            public void componentResized(ComponentEvent e) {
                EValidationMessage.this.place("resized");
            }

            @Override
            public void componentShown(ComponentEvent e) {
                EValidationMessage.this.shown();
            }
        });

        this.setVisible(false);
    }

    public JComponent getComponent() {
        return this.component;
    }

    /**
     * JDOC
     * 
     * @return
     */
    public EValidationMessage getSimpleThreadSafeInterface() {
        try {
            return EventThreadSafeWrapper.getSimpleThreadSafeInterface(EValidationMessage.class, this, EValidationMessageI.class);
        } catch (Exception ex) {
            System.err.println(ex);
            return this; // no javassist
        }
    }

    /**
     * hidden
     */
    protected void hidden() {
        this.setVisible(false);
    }

    /**
     * install
     */
    protected void install() {
        if (this.installed || (this.parent == null)) {
            return;
        }

        this.parent.add(this);
        this.installed = true;
    }

    private Point loc(Component c) {
        int x = 0;
        int y = 0;
        Component current = c;
        while (!(current instanceof Window)) {
            x += current.getLocation().x;
            y += current.getLocation().y;
            current = current.getParent();
        }
        return new Point(x, y);
    }

    protected void place(@SuppressWarnings("unused") String id) {
        if (!this.installed) {
            return;
        }
        Point p_comp = this.loc(this.component);
        Point p_this = this.loc(this.getParent());
        Dimension d_comp = this.component.getSize();
        int x = (p_comp.x + d_comp.width) - p_this.x;
        int y = (p_comp.y + d_comp.height) - p_this.y;
        int iw2 = this.getIcon().getIconWidth();
        int ih2 = this.getIcon().getIconHeight();
        int px = (x - iw2) + 2;
        int py = (y - ih2) + 2;
        Rectangle r = new Rectangle(px, py, iw2, ih2);
        this.setBounds(r);

    }

    /**
     * 
     * @see javax.swing.JLabel#setText(java.lang.String)
     */
    @Override
    public void setText(String text) {
        this.setToolTipText(text);
    }

    /**
     * 
     * @see javax.swing.JComponent#setToolTipText(java.lang.String)
     */
    @Override
    public void setToolTipText(String text) {
        this.install();
        super.setToolTipText(text);

        boolean aFlag = text != null;
        this.setVisible(aFlag);

        if (aFlag) {
            this.place("onset");
        }
    }

    /**
     * shown
     */
    protected void shown() {
        this.setVisible(true);
    }

    /**
     * @see #getSimpleThreadSafeInterface()
     */
    public EValidationMessage stsi() {
        return this.getSimpleThreadSafeInterface();
    }

    /**
     * @see #getSimpleThreadSafeInterface()
     */
    public EValidationMessage STSI() {
        return this.getSimpleThreadSafeInterface();
    }
}