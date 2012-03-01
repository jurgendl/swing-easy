package org.swingeasy;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * @author Jurgen
 * @see http://www.exampledepot.com/egs/javax.swing.text/style_HiliteWords.html
 * @see http://www.exampledepot.com/egs/javax.swing.text/style_HiliteWords2.html
 */
public class SearchDialog extends JDialog {
    private static final long serialVersionUID = -7658724511534306863L;

    protected final int gap = 4;

    protected final ETextArea textComponent;

    protected boolean replacing;

    protected EButton btnReplaceAll = new EButton("Replace all");

    protected EButton btnClose = new EButton("Close");

    protected JLabel lblFind = new JLabel("Find" + ": ");

    protected EButton btnHighlightAll = new EButton("Highlight all");

    protected JTextField tfFind = new JTextField();

    protected EButton btnFind = new EButton("Find");

    protected JLabel lblReplace = new JLabel("Replace by" + ": ");

    protected JTextField tfReplace = new JTextField();

    protected EButton btnReplace = new EButton("Replace");

    public SearchDialog(boolean replacing, ETextArea textComponent) {
        super((Window) SwingUtilities.getRoot(textComponent), ModalityType.MODELESS);
        this.textComponent = textComponent;
        this.init();
        this.setReplacing(replacing);
        this.setLocationRelativeTo(null);
    }

    protected void closed() {
        this.textComponent.removeHighlights();
    }

    protected void find(String find) {
        this.textComponent.find(find);
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

        int h = 22;
        int bw = 100;
        int lw = 80;
        int iw = 160;
        this.setLayout(null);
        this.setSize(484, 126);
        this.setResizable(false);

        {
            this.lblFind.setLocation(16, 16);
            this.lblFind.setSize(lw, h);
            this.add(this.lblFind);
        }
        {
            this.tfFind.setSize(iw, h);
            this.setRight(this.tfFind, this.lblFind);
            this.add(this.tfFind);
        }
        {
            this.lblReplace.setSize(lw, h);
            this.setBelow(this.lblReplace, this.lblFind);
            this.add(this.lblReplace);
        }
        {
            this.tfReplace.setSize(iw, h);
            this.setRight(this.tfReplace, this.lblReplace);
            this.add(this.tfReplace);
        }
        {
            this.btnFind.setSize(bw, h);
            this.setRight(this.btnFind, this.tfFind);
            this.add(this.btnFind);
            this.btnFind.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (SearchDialog.this.tfFind.getText().trim().length() > 0) {
                        try {
                            SearchDialog.this.find(SearchDialog.this.tfFind.getText().trim());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
        }
        {
            this.btnHighlightAll.setSize(bw, h);
            this.setRight(this.btnHighlightAll, this.btnFind);
            this.add(this.btnHighlightAll);
            this.btnHighlightAll.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (SearchDialog.this.tfFind.getText().trim().length() > 0) {
                        try {
                            SearchDialog.this.highlightAll(SearchDialog.this.tfFind.getText().trim());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
        }
        {
            this.btnReplace.setSize(bw, h);
            this.setRight(this.btnReplace, this.tfReplace);
            this.add(this.btnReplace);
            this.btnReplace.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (SearchDialog.this.tfFind.getText().trim().length() > 0) {
                        try {
                            SearchDialog.this.replace(SearchDialog.this.tfFind.getText().trim(), SearchDialog.this.tfReplace.getText().trim());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
        }
        {
            this.btnReplaceAll.setSize(bw, h);
            this.setRight(this.btnReplaceAll, this.btnReplace);
            this.add(this.btnReplaceAll);
            this.btnReplaceAll.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (SearchDialog.this.tfFind.getText().trim().length() > 0) {
                        try {
                            SearchDialog.this.replaceAll(SearchDialog.this.tfFind.getText().trim(), SearchDialog.this.tfReplace.getText().trim());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
        }
        {
            this.btnClose.setSize(bw, h);
            this.setBelow(this.btnClose, this.btnReplaceAll);
            this.add(this.btnClose);
            this.btnClose.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        SearchDialog.this.dispose();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
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

    protected void setBelow(JComponent component, JComponent relative) {
        component.setLocation(relative.getLocation().x, relative.getLocation().y + relative.getHeight() + this.gap);
    }

    public void setReplacing(boolean replacing) {
        this.replacing = replacing;
        this.tfReplace.setEnabled(replacing);
        this.btnReplace.setEnabled(replacing);
        this.btnReplaceAll.setEnabled(replacing);
    }

    protected void setRight(JComponent component, JComponent relative) {
        component.setLocation(relative.getLocation().x + relative.getWidth() + this.gap, relative.getLocation().y);
    }
}
