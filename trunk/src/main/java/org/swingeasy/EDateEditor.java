package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SpinnerDateModel;

/**
 * @author Jurgen
 */
public class EDateEditor extends AbstractELabeledTextFieldButtonComponent<ELabel, ESpinner<Date>, EButton> {
    /** serialVersionUID */
    private static final long serialVersionUID = 3275532427920825736L;

    protected JComponent parentComponent = null;

    protected EDateChooser dateChooser = null;

    protected JPopupMenu popup = null;

    protected JPanel datePanel = null;

    public EDateEditor() {
        this(new Date());
    }

    public EDateEditor(Date date) {
        this.setDate(date);
    }

    /**
     * 
     * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#copy()
     */
    @Override
    public void copy() {
        // FIXME
        // EComponentPopupMenu.copyToClipboard(this.getInput().getText());
    }

    protected EDateChooser createEDateChooser() {
        return new EDateChooser();
    }

    /**
     * 
     * @see org.swingeasy.ELabeledTextFieldButtonComponent#doAction()
     */
    @Override
    protected void doAction() {
        JPopupMenu _popup = this.getPopup();
        _popup.setLocation(new Point((this.getLocationOnScreen().x + this.getWidth()) - (int) _popup.getPreferredSize().getWidth(), this
                .getLocationOnScreen().y + this.getHeight()));
        Date date = this.getInput().get();
        this.getDateChooser().setDate(date);
        _popup.setVisible(true);
    }

    /**
     * 
     * @see org.swingeasy.ELabeledTextFieldButtonComponent#getAction()
     */
    @Override
    protected String getAction() {
        return "pick-date";//$NON-NLS-1$
    }

    /**
     * 
     * @see org.swingeasy.AbstractELabeledTextFieldButtonComponent#getButton()
     */
    @Override
    public EButton getButton() {
        if (this.button == null) {
            this.button = new EButton(new EIconButtonCustomizer(new Dimension(20, 20)), this.getIcon());
            this.button.setActionCommand(this.getAction());
            this.button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    EDateEditor.this.doAction();
                }
            });
        }
        return this.button;
    }

    public Date getDate() {
        return this.getInput().get();
    }

    protected EDateChooser getDateChooser() {
        if (this.dateChooser == null) {
            this.dateChooser = this.createEDateChooser();
        }
        return this.dateChooser;
    }

    protected JPanel getDatePanel() {
        if (this.datePanel == null) {
            this.datePanel = new JPanel(new BorderLayout());
            JPanel actions = new JPanel(new FlowLayout());
            JButton okbtn = new JButton(Messages.getString(this.getLocale(), "EDateEditor.OK"));//$NON-NLS-1$
            okbtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    EDateEditor.this.setDate(EDateEditor.this.getDateChooser().getDate());
                    EDateEditor.this.getPopup().setVisible(false);
                }
            });
            actions.add(okbtn);
            JButton cancelbtn = new JButton(Messages.getString(this.getLocale(), "EDateEditor.Cancel"));//$NON-NLS-1$
            cancelbtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    EDateEditor.this.getPopup().setVisible(false);
                }
            });
            actions.add(cancelbtn);
            this.datePanel.add(this.getDateChooser(), BorderLayout.CENTER);
            this.datePanel.add(actions, BorderLayout.SOUTH);
        }
        return this.datePanel;
    }

    /**
     * 
     * @see org.swingeasy.ELabeledTextFieldButtonComponent#getIcon()
     */
    @Override
    protected Icon getIcon() {
        return Resources.getImageResource("date.png");//$NON-NLS-1$
    }

    /**
     * 
     * @see org.swingeasy.AbstractELabeledTextFieldButtonComponent#getInput()
     */
    @Override
    public ESpinner<Date> getInput() {
        if (this.input == null) {
            SpinnerDateModel model = new SpinnerDateModel();
            model.setValue(new Date());
            this.input = new ESpinner<Date>(model);
            this.input.setEditor(new ESpinner.DateEditor(this.input, SimpleDateFormat.class.cast(
                    DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())).toPattern()));
            this.input.setBorder(null);
        }
        return this.input;
    }

    /**
     * 
     * @see org.swingeasy.AbstractELabeledTextFieldButtonComponent#getLabel()
     */
    @Override
    public ELabel getLabel() {
        if (this.label == null) {
            this.label = new ELabel();
            this.label.setLabelFor(this.getInput());
        }
        return this.label;
    }

    /**
     * 
     * @see org.swingeasy.HasParentComponent#getParentComponent()
     */
    @Override
    public JComponent getParentComponent() {
        return this.parentComponent;
    }

    protected JPopupMenu getPopup() {
        if (this.popup == null) {
            this.popup = new JPopupMenu();
            this.popup.setLightWeightPopupEnabled(true);
            this.popup.add(this.getDatePanel());
            UIUtils.getRootWindow(this.getButton()).addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    EDateEditor.this.popup.setVisible(false);
                }

                @Override
                public void windowIconified(WindowEvent e) {
                    EDateEditor.this.popup.setVisible(false);
                }
            });
        }
        return this.popup;
    }

    public void setDate(Date date) {
        this.getInput().setValue(date);
    }

    /**
     * 
     * @see java.awt.Component#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        this.getButton().setToolTipText(Messages.getString(l, "EDateEditor.button.text"));//$NON-NLS-1$
        this.getLabel().setText(Messages.getString(l, "EDateEditor.label.text") + ": ");//$NON-NLS-1$ //$NON-NLS-2$
        // TODO set local format spinner
    }

    public void setParentComponent(JComponent parentComponent) {
        this.parentComponent = parentComponent;
    }
}
