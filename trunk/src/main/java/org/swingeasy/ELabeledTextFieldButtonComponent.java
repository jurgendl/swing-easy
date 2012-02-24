package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.swingeasy.EComponentPopupMenu.ReadableComponent;

/**
 * @author Jurgen
 */
public abstract class ELabeledTextFieldButtonComponent extends JComponent implements EComponentI, ReadableComponent {
    private static final long serialVersionUID = 3916693177023150847L;

    protected JComponent input;

    protected JComponent button;

    protected JComponent label;

    protected Border defaultBorder = new JTextField().getBorder();

    public ELabeledTextFieldButtonComponent() {
        this.createComponent();
        UIUtils.registerLocaleChangeListener(this);
    }

    protected void createComponent() {
        this.setLayout(new BorderLayout());
        this.setLocale(null);

        JPanel internal = new JPanel(new BorderLayout());
        this.add(this.getLabel(), BorderLayout.WEST);
        this.add(internal, BorderLayout.CENTER);
        internal.add(this.getInput(), BorderLayout.CENTER);
        internal.add(this.getButton(), BorderLayout.EAST);

        internal.setBorder(this.defaultBorder);
        this.setBackground(Color.WHITE);
        internal.setBackground(Color.WHITE);
    }

    protected abstract void doAction();

    protected abstract String getAction();

    protected JComponent getButton() {
        if (this.button == null) {
            EToolBarButton _button = new EToolBarButton(new EToolBarButtonCustomizer(new Dimension(20, 20)), this.getIcon());
            _button.setActionCommand(this.getAction());
            _button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ELabeledTextFieldButtonComponent.this.doAction();
                }
            });
            this.button = _button;
        }
        return this.button;
    }

    protected abstract Icon getIcon();

    protected JComponent getInput() {
        if (this.input == null) {
            JTextField _input = new JTextField();
            _input.setBorder(null);
            _input.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        ELabeledTextFieldButtonComponent.this.doAction();
                    }
                }
            });
            this.input = _input;
        }
        return this.input;
    }

    protected JComponent getLabel() {
        if (this.label == null) {
            JLabel _label = new JLabel();
            _label.setLabelFor(this.getInput());
            this.label = _label;
        }
        return this.label;
    }
}
