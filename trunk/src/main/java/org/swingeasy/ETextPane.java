package org.swingeasy;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;

import org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction;
import org.swingeasy.EComponentPopupMenu.ReadableComponent;

/**
 * @author Jurgen
 */
public class ETextPane extends JTextPane implements EComponentI, ReadableComponent {
    protected static class BoldAction extends EComponentPopupMenuAction<ETextPane> {
        private static final long serialVersionUID = -8361277540935938118L;

        public BoldAction(ETextPane component) {
            super(component, "font-bold", Resources.getImageResource("text_bold.png"));
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_B, Event.CTRL_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            StyledEditorKit kit = this.delegate.getStyledEditorKit();
            MutableAttributeSet attr = kit.getInputAttributes();
            boolean bold = (StyleConstants.isBold(attr)) ? false : true;
            SimpleAttributeSet sas = new SimpleAttributeSet();
            StyleConstants.setBold(sas, bold);
            this.delegate.setCharacterAttributes(sas, false);
        }
    }

    protected static class CenterJustifyAction extends EComponentPopupMenuAction<ETextPane> {
        private static final long serialVersionUID = 8808839600880301301L;

        public CenterJustifyAction(ETextPane component) {
            super(component, "center-justify", Resources.getImageResource("text_align_center.png"));
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, Event.CTRL_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            MutableAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setAlignment(attr, StyleConstants.ALIGN_CENTER);
            this.delegate.setParagraphAttributes(attr, false);
        }
    }

    protected static class ItalicAction extends EComponentPopupMenuAction<ETextPane> {
        private static final long serialVersionUID = -7754751631180942592L;

        public ItalicAction(ETextPane component) {
            super(component, "font-italic", Resources.getImageResource("text_italic.png"));
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_I, Event.CTRL_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            StyledEditorKit kit = this.delegate.getStyledEditorKit();
            MutableAttributeSet attr = kit.getInputAttributes();
            boolean italic = (StyleConstants.isItalic(attr)) ? false : true;
            SimpleAttributeSet sas = new SimpleAttributeSet();
            StyleConstants.setItalic(sas, italic);
            this.delegate.setCharacterAttributes(sas, false);
        }
    }

    protected static class LeftJustifyAction extends EComponentPopupMenuAction<ETextPane> {
        private static final long serialVersionUID = -6642449775102064584L;

        public LeftJustifyAction(ETextPane component) {
            super(component, "left-justify", Resources.getImageResource("text_align_left.png"));
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_L, Event.CTRL_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            MutableAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setAlignment(attr, StyleConstants.ALIGN_LEFT);
            this.delegate.setParagraphAttributes(attr, false);

        }
    }

    protected static class RightJustifyAction extends EComponentPopupMenuAction<ETextPane> {
        private static final long serialVersionUID = -9207009185034378663L;

        public RightJustifyAction(ETextPane component) {
            super(component, "right-justify", Resources.getImageResource("text_align_right.png"));
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, Event.CTRL_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            MutableAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setAlignment(attr, StyleConstants.ALIGN_RIGHT);
            this.delegate.setParagraphAttributes(attr, false);
        }
    }

    protected static class UnderlineAction extends EComponentPopupMenuAction<ETextPane> {
        private static final long serialVersionUID = 6387670559027157295L;

        public UnderlineAction(ETextPane component) {
            super(component, "font-underline", Resources.getImageResource("text_underline.png"));
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_U, Event.CTRL_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            StyledEditorKit kit = this.delegate.getStyledEditorKit();
            MutableAttributeSet attr = kit.getInputAttributes();
            boolean underline = (StyleConstants.isUnderline(attr)) ? false : true;
            SimpleAttributeSet sas = new SimpleAttributeSet();
            StyleConstants.setUnderline(sas, underline);
            this.delegate.setCharacterAttributes(sas, false);
        }
    }

    private static final long serialVersionUID = -2601772157437823356L;

    protected Action[] actions;

    public ETextPane() {
        StyledEditorKit kit = new StyledEditorKit();
        this.setEditorKit(kit);
        UIUtils.registerLocaleChangeListener((EComponentI) this);
        JPopupMenu popupMenu = EComponentPopupMenu.installTextComponentPopupMenu(this);
        popupMenu.addSeparator();
        this.actions = new Action[] {
                new BoldAction(this),
                new ItalicAction(this),
                new UnderlineAction(this),
                new LeftJustifyAction(this),
                new CenterJustifyAction(this),
                new RightJustifyAction(this) };
        for (Action action : this.actions) {
            popupMenu.add(action);
        }
    }

    /**
     * @see org.swingeasy.HasParentComponent#getParentComponent()
     */
    @Override
    public JComponent getParentComponent() {
        return this;
    }
}
