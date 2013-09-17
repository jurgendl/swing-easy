package org.swingeasy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * @author Jurgen
 */
public abstract class EComponentExporterImpl<T extends JComponent & EComponentI> {
    protected EComponentExporterFileChooserCustomizer<T> customizer = null;

    protected String overwriteWarningMessage = "EComponentExporter.overwrite.warning.message";

    protected String overwriteWarningTitle = "EComponentExporter.overwrite.warning.title";

    protected String completionMessage = "EComponentExporter.completion.message";

    protected String completionTitle = "EComponentExporter.completion.title";

    public EComponentExporterImpl() {
        super();
    }

    protected boolean canOverwrite(T component) {
        if (ResultType.OK != CustomizableOptionPane.showCustomDialog(component,
                new JLabel(Messages.getString((Locale) null, this.overwriteWarningMessage)),
                Messages.getString((Locale) null, this.overwriteWarningTitle), MessageType.WARNING, OptionType.OK_CANCEL, null,
                new CenteredOptionPaneCustomizer())) {
            return false;
        }
        return true;
    }

    public void export(T component) {
        try {
            File exportTo = CustomizableOptionPane.showFileChooserDialog(component, FileChooserType.SAVE, this.customizer);
            if (exportTo != null) {
                final String ext = this.getFileExtension();
                if (!exportTo.getName().endsWith(ext)) {
                    exportTo = new File(exportTo.getParentFile(), exportTo.getName() + "." + ext);
                }
                if (exportTo.exists()) {
                    if (!this.canOverwrite(component)) {
                        return;
                    }
                }
                Stream stream = StreamFactory.create();
                this.exportStream(component, stream.getOutputStream());
                InputStream data = stream.getInputStream();
                FileOutputStream fout = new FileOutputStream(exportTo);
                byte[] buffer = new byte[1024 * 4];
                int read;
                while ((read = data.read(buffer)) != -1) {
                    fout.write(buffer, 0, read);
                }
                data.close();
                fout.close();
                EComponentExporterFileChooserCustomizer.lastFile = exportTo;
                this.whenDone(component);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public abstract void exportStream(T component, OutputStream out) throws IOException;

    public abstract String getFileExtension();

    public Icon getIcon() {
        return UIUtils.getIconForFileType(this.getFileExtension());
    }

    protected void whenDone(T component) {
        CustomizableOptionPane.showCustomDialog(component, new JLabel(Messages.getString((Locale) null, this.completionMessage)),
                Messages.getString((Locale) null, this.completionTitle), MessageType.INFORMATION, OptionType.OK, null,
                new CenteredOptionPaneCustomizer());
    }
}
