package org.swingeasy;

import java.awt.Component;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import org.swingeasy.EComponentPopupMenu.CheckEnabled;
import org.swingeasy.EComponentPopupMenu.WritableComponent;

/**
 * @author Jurgen
 */
public class EToolBar extends JToolBar {
    private static final long serialVersionUID = -1257874888846884947L;

    public EToolBar(Action[] actions) {
        for (Action action : actions) {
            this.add(action);
        }
    }

    public EToolBar(final EComponentPopupMenu popup, final WritableComponent component) {
        this(popup);
        JTextComponent jtc = null;
        if (component instanceof JTextComponent) {
            jtc = (JTextComponent) component;
        }
        if (component.getParentComponent() instanceof JTextComponent) {
            jtc = (JTextComponent) component.getParentComponent();
        }
        if (jtc != null) {
            jtc.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void changedUpdate(DocumentEvent e) {
                    popup.checkEnabled(new CheckEnabled(component.hasSelection(), component.hasText(), true, true));
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    popup.checkEnabled(new CheckEnabled(component.hasSelection(), component.hasText(), true, true));
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    popup.checkEnabled(new CheckEnabled(component.hasSelection(), component.hasText(), true, true));
                }
            });
            jtc.addCaretListener(new CaretListener() {
                @Override
                public void caretUpdate(CaretEvent e) {
                    popup.checkEnabled(new CheckEnabled(component.hasSelection(), component.hasText(), true, true));
                }
            });
        }
        popup.checkEnabled(new CheckEnabled(component.hasSelection(), component.hasText(), true, true));
    }

    public EToolBar(JPopupMenu popup) {
        super(popup.getName());
        for (int i = 0; i < popup.getComponentCount(); i++) {
            Component component = popup.getComponent(i);
            if (component instanceof JMenuItem) {
                Action action = JMenuItem.class.cast(component).getAction();
                this.add(action);
            } else {
                this.addSeparator();
            }
        }
    }
}
