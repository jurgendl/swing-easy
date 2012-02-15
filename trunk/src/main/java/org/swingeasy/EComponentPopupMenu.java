package org.swingeasy;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/**
 * @see http://java-swing-tips.blogspot.com/2010/11/jtable-celleditor-popupmenu.html
 * @author jdlandsh
 */
public class EComponentPopupMenu {
    public static interface PopupMenuInterface {
        public void addUndoableEditListener(UndoManager manager);

        public boolean canEdit();

        public JComponent getComponent();
    }

    private static class RedoAction extends AbstractAction {
        private static final long serialVersionUID = 700221902961828425L;

        private final UndoManager undoManager;

        public RedoAction(UndoManager manager) {
            super(EComponentPopupMenu.REDO);
            this.undoManager = manager;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                this.undoManager.redo();
            } catch (CannotRedoException cre) {
                // Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    public static class TextComponentPopupMenuInterface implements PopupMenuInterface {
        protected final JTextComponent component;

        public TextComponentPopupMenuInterface(JTextComponent component) {
            this.component = component;
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.PopupMenuInterface#addUndoableEditListener(javax.swing.undo.UndoManager)
         */
        @Override
        public void addUndoableEditListener(UndoManager manager) {
            this.component.getDocument().addUndoableEditListener(manager);
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.PopupMenuInterface#canEdit()
         */
        @Override
        public boolean canEdit() {
            return true;
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.PopupMenuInterface#getComponent()
         */
        @Override
        public JComponent getComponent() {
            return this.component;
        }
    }

    private static class UndoAction extends AbstractAction {

        private static final long serialVersionUID = -2639363038955484287L;

        private final UndoManager undoManager;

        public UndoAction(UndoManager manager) {
            super(EComponentPopupMenu.UNDO);
            this.undoManager = manager;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                this.undoManager.undo();
            } catch (CannotUndoException cue) {
                // Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    public static final String DELETE = "delete";

    public static final String REDO = "redo";

    public static final String UNDO = "undo";

    private static Action selectAllAction = null;

    private static Action unselectAction = null;

    private static Action createSelectAllAction() {
        if (EComponentPopupMenu.selectAllAction == null) {
            for (Action action : new javax.swing.text.DefaultEditorKit().getActions()) {
                if (action.getValue(Action.NAME).equals(javax.swing.text.DefaultEditorKit.selectAllAction)) {
                    EComponentPopupMenu.selectAllAction = action;
                    return EComponentPopupMenu.selectAllAction;
                }
            }
        }
        return EComponentPopupMenu.selectAllAction;
    }

    private static Action createUnselectAction() {
        if (EComponentPopupMenu.unselectAction == null) {
            for (Action action : new javax.swing.text.DefaultEditorKit().getActions()) {
                if (action.getValue(Action.NAME).equals("unselect")) {
                    EComponentPopupMenu.unselectAction = action;
                    return EComponentPopupMenu.unselectAction;
                }
            }
        }
        return EComponentPopupMenu.unselectAction;
    }

    public static JPopupMenu installTextComponentPopupMenu(final PopupMenuInterface component) {
        if (!component.canEdit()) {
            final Action copyAction = new DefaultEditorKit.CopyAction();
            JPopupMenu popup = new JPopupMenu();
            popup.add(copyAction).setIcon(Resources.getImageResource("page_copy.png"));
            component.getComponent().addAncestorListener(new AncestorListener() {
                @Override
                public void ancestorAdded(AncestorEvent e) {
                    component.getComponent().requestFocusInWindow();
                }

                @Override
                public void ancestorMoved(AncestorEvent e) {
                    //
                }

                @Override
                public void ancestorRemoved(AncestorEvent e) {
                    //
                }
            });
            popup.addPopupMenuListener(new PopupMenuListener() {
                @Override
                public void popupMenuCanceled(PopupMenuEvent e) {
                    //
                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    //
                }

                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    copyAction.setEnabled(true);
                }
            });
            component.getComponent().setComponentPopupMenu(popup);
            return popup;
        }

        final UndoManager manager = new UndoManager();
        final Action copyAction = new DefaultEditorKit.CopyAction();
        final Action undoAction = new UndoAction(manager);
        final Action redoAction = new RedoAction(manager);
        final Action cutAction = new DefaultEditorKit.CutAction();
        final Action pasteAction = new DefaultEditorKit.PasteAction();
        final Action deleteAction = new AbstractAction(EComponentPopupMenu.DELETE) {
            private static final long serialVersionUID = 8188465232565268116L;

            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu pop = (JPopupMenu) e.getSource();
                ((JTextComponent) pop.getInvoker()).replaceSelection(null);
            }
        };
        final Action _selectAllAction = EComponentPopupMenu.createSelectAllAction();
        final Action _unselectAction = EComponentPopupMenu.createUnselectAction();

        component.getComponent().addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent e) {
                manager.discardAllEdits();
                component.getComponent().requestFocusInWindow();
            }

            @Override
            public void ancestorMoved(AncestorEvent e) {
                //
            }

            @Override
            public void ancestorRemoved(AncestorEvent e) {
                //
            }
        });

        component.addUndoableEditListener(manager);

        component.getComponent().getActionMap().put(EComponentPopupMenu.UNDO, undoAction);
        component.getComponent().getActionMap().put(EComponentPopupMenu.REDO, redoAction);
        InputMap imap = component.getComponent().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK), EComponentPopupMenu.UNDO);
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK), EComponentPopupMenu.REDO);

        JPopupMenu popup = new JPopupMenu();
        popup.add(undoAction).setIcon(Resources.getImageResource("arrow_undo.png"));
        popup.add(redoAction).setIcon(Resources.getImageResource("arrow_redo.png"));
        popup.addSeparator();
        popup.add(cutAction).setIcon(Resources.getImageResource("cut.png"));
        popup.add(copyAction).setIcon(Resources.getImageResource("page_copy.png"));
        popup.add(pasteAction).setIcon(Resources.getImageResource("page_paste.png"));
        popup.add(deleteAction).setIcon(Resources.getImageResource("bin_closed.png"));
        popup.addSeparator();
        popup.add(_selectAllAction);
        popup.add(_unselectAction);

        popup.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                //
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                undoAction.setEnabled(true);
                redoAction.setEnabled(true);
            }

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                JPopupMenu pop = (JPopupMenu) e.getSource();
                JTextComponent field = (JTextComponent) pop.getInvoker();
                boolean flg = field.getSelectedText() != null;
                cutAction.setEnabled(flg);
                copyAction.setEnabled(flg);
                deleteAction.setEnabled(flg);
                undoAction.setEnabled(manager.canUndo());
                redoAction.setEnabled(manager.canRedo());
                _selectAllAction.setEnabled(true);
            }
        });
        component.getComponent().setComponentPopupMenu(popup);
        return popup;
    }
}
