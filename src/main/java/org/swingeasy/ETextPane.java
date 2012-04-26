package org.swingeasy;

import java.awt.Component;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.rtf.RTFEditorKit;

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

    protected static class FontAction extends EComponentPopupMenuAction<ETextPane> {
        private static final long serialVersionUID = -5278759554929664861L;

        public FontAction(ETextPane component) {
            super(component, "font-chooser", Resources.getImageResource("font.png"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO fontchooser
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

    protected static class OpenAction extends EComponentPopupMenuAction<ETextPane> {
        private static final long serialVersionUID = 649772388750665266L;

        public OpenAction(ETextPane component) {
            super(component, "open", Resources.getImageResource("folder_page_white.png"));
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            File file = CustomizableOptionPane.showFileChooserDialog(null, FileChooserType.OPEN, new FileChooserCustomizer() {
                @Override
                public void customize(Component parentComponent, JDialog dialog) {
                    dialog.setLocationRelativeTo(null);
                }

                @Override
                public void customize(JFileChooser jfc) {
                    jfc.resetChoosableFileFilters();
                    jfc.addChoosableFileFilter(new ExtensionFileFilter(UIUtils.getDescriptionForFileType(ETextPane.FILE_EXT) + " ("
                            + ETextPane.FILE_EXT + ")", ETextPane.FILE_EXT));
                }
            });
            if (file == null) {
                return;
            }
            try {
                FileInputStream in = new FileInputStream(file);
                Document doc = this.delegate.getDocument();
                EditorKit kit = this.delegate.getEditorKit();
                kit.read(in, doc, 0);
                in.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            }
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

    protected static class SaveAction extends EComponentPopupMenuAction<ETextPane> {
        private static final long serialVersionUID = 344984528281010531L;

        public SaveAction(ETextPane component) {
            super(component, "save", Resources.getImageResource("bullet_disk.png"));
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            File file = CustomizableOptionPane.showFileChooserDialog(null, FileChooserType.SAVE, new FileChooserCustomizer() {
                @Override
                public void customize(Component parentComponent, JDialog dialog) {
                    dialog.setLocationRelativeTo(null);
                }

                @Override
                public void customize(JFileChooser jfc) {
                    jfc.resetChoosableFileFilters();
                    jfc.addChoosableFileFilter(new ExtensionFileFilter(UIUtils.getDescriptionForFileType(ETextPane.FILE_EXT) + " ("
                            + ETextPane.FILE_EXT + ")", ETextPane.FILE_EXT));
                }
            });
            if (file == null) {
                return;
            }
            if (file.exists()) {
                if (ResultType.OK != CustomizableOptionPane.showCustomDialog(null,
                        new JLabel(Messages.getString(null, "SaveAction.overwrite.warning.message")),
                        Messages.getString(null, "SaveAction.overwrite.warning.title"), MessageType.WARNING, OptionType.OK_CANCEL, null,
                        new OptionPaneCustomizer() {
                            @Override
                            public void customize(Component parentComponent, MessageType messageType, OptionType optionType, JOptionPane pane,
                                    JDialog dialog) {
                                dialog.setLocationRelativeTo(null);
                            }
                        })) {
                    return;
                }
            }
            try {
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                Document doc = this.delegate.getDocument();
                EditorKit kit = this.delegate.getEditorKit();
                kit.write(out, doc, doc.getStartPosition().getOffset(), doc.getLength());
                out.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            }
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

    public static final String FILE_EXT = "rtf";

    private static final long serialVersionUID = -2601772157437823356L;

    protected Action[] actions;

    public ETextPane() {
        StyledEditorKit kit = new RTFEditorKit();
        this.setEditorKit(kit);
        UIUtils.registerLocaleChangeListener((EComponentI) this);
        JPopupMenu popupMenu = EComponentPopupMenu.installTextComponentPopupMenu(this);
        popupMenu.addSeparator();
        this.actions = new Action[] {
                new OpenAction(this),
                new SaveAction(this),
                null,
                new FontAction(this),
                new BoldAction(this),
                new ItalicAction(this),
                new UnderlineAction(this),
                new LeftJustifyAction(this),
                new CenterJustifyAction(this),
                new RightJustifyAction(this) };
        for (Action action : this.actions) {
            if (action == null) {
                popupMenu.addSeparator();
            } else {
                popupMenu.add(action);
            }
        }
    }

    /**
     * @see org.swingeasy.HasParentComponent#getParentComponent()
     */
    @Override
    public JComponent getParentComponent() {
        return this;
    }

    public JToolBar getToolbar() {
        JToolBar tools = new JToolBar();
        for (Action action : this.actions) {
            if (action == null) {
                tools.addSeparator();
            } else {
                tools.add(action);
            }
        }
        return tools;
    }
}
