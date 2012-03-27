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
public abstract class AbstractELabeledTextFieldButtonComponent<LABEL extends JComponent, INPUT extends JComponent, BUTTON extends JComponent> extends
        JComponent implements EComponentI, ReadableComponent {
    private static final long serialVersionUID = 3916693177023150847L;

    protected INPUT input;

    protected BUTTON button;

    protected LABEL label;

    protected Border defaultBorder = new JTextField().getBorder();

    public AbstractELabeledTextFieldButtonComponent() {
        this.createComponent();
        UIUtils.registerLocaleChangeListener((EComponentI) this);
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

    @SuppressWarnings("unchecked")
    public BUTTON getButton() {
        if (this.button == null) {
            EButton _button = new EButton(new EIconButtonCustomizer(new Dimension(20, 20)), this.getIcon());
            _button.setActionCommand(this.getAction());
            _button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    AbstractELabeledTextFieldButtonComponent.this.doAction();
                }
            });
            this.button = (BUTTON) _button;
        }
        return this.button;
    }

    protected abstract Icon getIcon();

    @SuppressWarnings("unchecked")
    public INPUT getInput() {
        if (this.input == null) {
            JTextField _input = new JTextField();
            _input.setBorder(null);
            _input.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        AbstractELabeledTextFieldButtonComponent.this.doAction();
                    }
                }
            });
            this.input = (INPUT) _input;
        }
        return this.input;
    }

    @SuppressWarnings("unchecked")
    public LABEL getLabel() {
        if (this.label == null) {
            JLabel _label = new JLabel();
            _label.setLabelFor(this.getInput());
            this.label = (LABEL) _label;
        }
        return this.label;
    }

    public void setButton(BUTTON button) {
        this.button = button;
    }

    public void setInput(INPUT input) {
        this.input = input;
    }

    public void setLabel(LABEL label) {
        this.label = label;
    }
}
