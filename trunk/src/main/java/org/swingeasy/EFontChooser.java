package org.swingeasy;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;

import net.miginfocom.swing.MigLayout;

import com.xenoage.util.gui.FontChooserComboBox;

/**
 * @author Jurgen
 */
public class EFontChooser extends JPanel implements EComponentI {
    private static final long serialVersionUID = 3686474630456833708L;

    public static Font showDialog() {
        return EFontChooser.showDialog(null);
    }

    public static Font showDialog(String defaultFont) {
        EFontChooser fc = new EFontChooser();
        if (defaultFont != null) {
            fc.fc.setSelectedItem(defaultFont);
        }
        if (ResultType.OK == CustomizableOptionPane.showCustomDialog(null, fc, Messages.getString(fc.getLocale(), "font-chooser-title"),
                MessageType.QUESTION, OptionType.OK_CANCEL, null, new OptionPaneCustomizer() {
                    @Override
                    public void customize(Component parentComponent, MessageType messageType, OptionType optionType, JOptionPane pane, JDialog dialog) {
                        dialog.setLocationRelativeTo(null);
                    }
                })) {
            Font font = new Font(fc.fc.getSelectedFontName(), Font.PLAIN, fc.size.get());
            return font;
        }
        return null;
    }

    protected FontChooserComboBox fc = new FontChooserComboBox();

    protected ESpinner<Integer> size = new ESpinner<Integer>(new SpinnerNumberModel(12, 4, 48, 2));

    private EFontChooser() {
        super(new MigLayout());
        this.add(this.fc);
        Dimension cbsize = new Dimension(200, 20);
        this.fc.setSize(cbsize);
        this.fc.setPreferredSize(cbsize);
        this.fc.setMaximumSize(cbsize);
        this.add(this.size);
        UIUtils.registerLocaleChangeListener((EComponentI) this);
    }
}
