package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.swingeasy.system.SystemSettings;

/**
 * @author Jurgen
 */
public class EDateEditor extends ELabeledTextFieldButtonComponent {
    /** serialVersionUID */
    private static final long serialVersionUID = 3275532427920825736L;

    protected DateFormat formatter;

    protected Date date;

    protected JComponent parentComponent = null;

    protected EDateChooser dateChooser;

    protected JPopupMenu popup;

    public EDateEditor() {
        this(new Date());
    }

    public EDateEditor(Date date) {
        this.formatter = DateFormat.getDateInstance(DateFormat.SHORT, this.getLocale());
        this.setDate(date);
    }

    /**
     * 
     * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#copy()
     */
    @Override
    public void copy() {
        EComponentPopupMenu.copyToClipboard(this.getInput().getText());
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

    public Date getDate() {
        return this.date;
    }

    protected EDateChooser getDateChooser() {
        if (this.dateChooser == null) {
            this.dateChooser = new EDateChooser(this.date);
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
            this.dateChooser.add(actions, BorderLayout.SOUTH);
        }
        return this.dateChooser;
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
            this.popup.add(this.getDateChooser());
        }
        return this.popup;
    }

    public void setDate(Date date) {
        this.date = date;
        this.getInput().setText(this.formatter.format(date));
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
        this.formatter = DateFormat.getDateInstance(DateFormat.SHORT, l == null ? SystemSettings.getCurrentLocale() : l);
    }

    public void setParentComponent(JComponent parentComponent) {
        this.parentComponent = parentComponent;
    }
}
