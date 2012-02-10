package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;

/**
 * @author Jurgen
 */
public abstract class ELabeledTextFieldButtonComponent extends JComponent implements EComponentI {
    private static final long serialVersionUID = 3916693177023150847L;

    protected JTextComponent input;

    protected JButton commit;

    protected JLabel label;

    public ELabeledTextFieldButtonComponent() {
        this.createComponent();
        UIUtils.registerLocaleChangeListener(this);
    }

    protected void createComponent() {
        this.setLayout(new BorderLayout());
        this.input = new JTextField();
        Border border = this.input.getBorder();
        this.input.setBorder(null);
        this.input.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ELabeledTextFieldButtonComponent.this.doAction();
                }
            }
        });
        this.commit = new EIconButton(new Dimension(20, 20), this.getIcon());
        this.commit.setActionCommand(this.getAction());
        this.commit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ELabeledTextFieldButtonComponent.this.doAction();
            }
        });
        this.label = new JLabel();
        this.setLocale(null);
        JPanel internal = new JPanel(new BorderLayout());
        internal.setBorder(border);
        this.add(this.label, BorderLayout.WEST);
        this.add(internal, BorderLayout.CENTER);
        internal.add(this.input, BorderLayout.CENTER);
        internal.add(this.commit, BorderLayout.EAST);
        this.label.setLabelFor(this.input);
        this.setBackground(Color.WHITE);
        internal.setBackground(Color.WHITE);
    }

    protected abstract void doAction();

    protected abstract String getAction();

    /**
     * gets commit
     * 
     * @return Returns the commit.
     */
    public JButton getCommit() {
        return this.commit;
    }

    protected abstract Icon getIcon();

    /**
     * gets input
     * 
     * @return Returns the input.
     */
    public JTextComponent getInput() {
        return this.input;
    }

    /**
     * gets label
     * 
     * @return Returns the label.
     */
    public JLabel getLabel() {
        return this.label;
    }
}
