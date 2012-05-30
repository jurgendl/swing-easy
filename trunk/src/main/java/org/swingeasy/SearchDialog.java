package org.swingeasy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

/**
 * @author Jurgen
 */
public class SearchDialog extends JDialog implements EComponentI {
    private static final long serialVersionUID = -7658724511534306863L;

    protected final ETextArea textComponent;

    protected boolean replacing;

    protected EButton btnReplaceAll;

    protected EButton btnClose;

    protected ELabel lblFind;

    protected EButton btnHighlightAll;

    protected JTextField tfFind;

    protected EButton btnFind;

    protected ELabel lblReplace;

    protected JTextField tfReplace;

    protected EButton btnReplace;

    public SearchDialog(boolean replacing, ETextArea textComponent) {
        super(UIUtils.getRootWindow(textComponent), Messages.getString((Locale) null, "SearchDialog.title"), ModalityType.MODELESS);
        this.textComponent = textComponent;
        this.init();
        this.setLocationRelativeTo(null);
        this.setReplacing(replacing);
        UIUtils.registerLocaleChangeListener((EComponentI) this);
    }

    protected void closed() {
        this.textComponent.removeHighlights();
    }

    protected void find(String find) {
        this.textComponent.find(find);
    }

    protected EButton getBtnClose() {
        if (this.btnClose == null) {
            this.btnClose = new EButton();
        }
        return this.btnClose;
    }

    protected EButton getBtnFind() {
        if (this.btnFind == null) {
            this.btnFind = new EButton();
        }
        return this.btnFind;
    }

    protected EButton getBtnHighlightAll() {
        if (this.btnHighlightAll == null) {
            this.btnHighlightAll = new EButton();
        }
        return this.btnHighlightAll;
    }

    protected EButton getBtnReplace() {
        if (this.btnReplace == null) {
            this.btnReplace = new EButton();
        }
        return this.btnReplace;
    }

    protected EButton getBtnReplaceAll() {
        if (this.btnReplaceAll == null) {
            this.btnReplaceAll = new EButton();
        }
        return this.btnReplaceAll;
    }

    protected ELabel getLblFind() {
        if (this.lblFind == null) {
            this.lblFind = new ELabel();
        }
        return this.lblFind;
    }

    protected ELabel getLblReplace() {
        if (this.lblReplace == null) {
            this.lblReplace = new ELabel();
        }
        return this.lblReplace;
    }

    protected JTextField getTfFind() {
        if (this.tfFind == null) {
            this.tfFind = new JTextField();
        }
        return this.tfFind;
    }

    protected JTextField getTfReplace() {
        if (this.tfReplace == null) {
            this.tfReplace = new JTextField();
        }
        return this.tfReplace;
    }

    protected void highlightAll(String find) {
        this.textComponent.highlightAll(find);
    }

    protected void init() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    SearchDialog.this.closed();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        this.setLayout(new MigLayout("wrap 4", "[right,fill]rel[grow,fill,220::]14px[fill,sg grp1]14px[fill,sg grp1]", ""));

        this.add(this.getLblFind());
        this.add(this.getTfFind());
        this.add(this.getBtnFind());
        this.add(this.getBtnHighlightAll(), "wrap");

        this.add(this.getLblReplace());
        this.add(this.getTfReplace());
        this.add(this.getBtnReplace());
        this.add(this.getBtnReplaceAll(), "wrap");

        this.add(new JLabel(), "span 3");
        this.add(this.getBtnClose());

        ActionListener findAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SearchDialog.this.getTfFind().getText().trim().length() > 0) {
                    try {
                        SearchDialog.this.find(SearchDialog.this.getTfFind().getText().trim());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        this.tfFind.addActionListener(findAction);
        this.getBtnFind().addActionListener(findAction);
        this.getBtnHighlightAll().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SearchDialog.this.getTfFind().getText().trim().length() > 0) {
                    try {
                        SearchDialog.this.highlightAll(SearchDialog.this.getTfFind().getText().trim());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        ActionListener replaceAtion = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SearchDialog.this.getTfFind().getText().trim().length() > 0) {
                    try {
                        SearchDialog.this.replace(SearchDialog.this.getTfFind().getText().trim(), SearchDialog.this.getTfReplace().getText().trim());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        this.getTfReplace().addActionListener(replaceAtion);
        this.getBtnReplace().addActionListener(replaceAtion);
        this.getBtnReplaceAll().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SearchDialog.this.getTfFind().getText().trim().length() > 0) {
                    try {
                        SearchDialog.this.replaceAll(SearchDialog.this.getTfFind().getText().trim(), SearchDialog.this.getTfReplace().getText()
                                .trim());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        this.getBtnClose().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SearchDialog.this.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        this.pack();
        this.setResizable(false);
    }

    public boolean isReplacing() {
        return this.replacing;
    }

    protected void replace(String find, String replace) {
        this.textComponent.replace(find, replace);
    }

    protected void replaceAll(String find, String replace) {
        this.textComponent.replaceAll(find, replace);
    }

    /**
     * 
     * @see java.awt.Component#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        this.setTitle(Messages.getString(l, "SearchDialog.title"));
        this.getBtnReplaceAll().setText(Messages.getString(l, "SearchDialog.replace-all"));
        this.getBtnClose().setText(Messages.getString(l, "SearchDialog.cancelButtonText"));
        this.getLblFind().setText(Messages.getString(l, "SearchDialog.find") + ": ");
        this.getBtnHighlightAll().setText(Messages.getString(l, "SearchDialog.highlight-all"));
        this.getBtnFind().setText(Messages.getString(l, "SearchDialog.find"));
        this.getLblReplace().setText(Messages.getString(l, "SearchDialog.replace-by") + ": ");
        this.getBtnReplace().setText(Messages.getString(l, "SearchDialog.replace"));
    }

    public void setReplacing(boolean replacing) {
        this.replacing = replacing;
        this.getTfReplace().setEnabled(replacing);
        this.getBtnReplace().setEnabled(replacing);
        this.getBtnReplaceAll().setEnabled(replacing);
    }
}
