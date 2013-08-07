package org.swingeasy.wizard;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * @author Jurgen
 */
public class WizardDemo {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            //
        }

        final JFrame f = new JFrame();
        EWizard comp = new EWizard();
        comp.addWizardPage(new WizardPage(
                "page 1",
                "A display area for a short text string or an image, or both. A label does not react to input events. As a result, it cannot get the keyboard focus. A label can, however, display a keyboard alternative as a convenience for a nearby component that has a keyboard alternative but can't display it. A display area for a short text string or an image, or both. A label does not react to input events. As a result, it cannot get the keyboard focus. A label can, however, display a keyboard alternative as a convenience for a nearby component that has a keyboard alternative but can't display it."));
        comp.addWizardPage(new WizardPage(
                "page 2",
                "A display area for a short text string or an image, or both. A label does not react to input events. As a result, it cannot get the keyboard focus. A label can, however, display a keyboard alternative as a convenience for a nearby component that has a keyboard alternative but can't display it."));
        comp.addWizardPage(new WizardPage("page 3", ""));
        comp.setCancelAction(new AbstractAction("cancel") {
            private static final long serialVersionUID = -3249259735298003295L;

            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
            }
        });
        comp.init();
        f.getContentPane().add(comp);
        f.pack();
        f.setSize(800, 600);
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
