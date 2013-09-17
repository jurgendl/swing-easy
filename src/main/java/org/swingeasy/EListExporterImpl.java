package org.swingeasy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * @author Jurgen
 */
public abstract class EListExporterImpl<T> implements EListExporter<T> {
    protected EListExporterFileChooserCustomizer customizer;

    public EListExporterImpl() {
        this.customizer = new EListExporterFileChooserCustomizer(this);
    }

    protected boolean canOverwrite(EList<T> table) {
        if (ResultType.OK != CustomizableOptionPane.showCustomDialog(table,
                new JLabel(Messages.getString((Locale) null, "EListExporter.overwrite.warning.message")),
                Messages.getString((Locale) null, "EListExporter.overwrite.warning.title"), MessageType.WARNING, OptionType.OK_CANCEL, null,
                new CenteredOptionPaneCustomizer())) {
            return false;
        }
        return true;
    }

    /**
     * 
     * @see org.swingeasy.EListExporter#export(org.swingeasy.EList)
     */
    @Override
    public void export(EList<T> component) {
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
                EListExporterFileChooserCustomizer.lastFile = exportTo;
                this.whenDone(component);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public abstract void exportStream(EList<T> table, OutputStream out) throws IOException;

    /**
     * 
     * @see org.swingeasy.EListExporter#getIcon()
     */
    @Override
    public Icon getIcon() {
        return UIUtils.getIconForFileType(this.getFileExtension());
    }

    protected void whenDone(EList<T> table) {
        CustomizableOptionPane.showCustomDialog(table, new JLabel(Messages.getString((Locale) null, "EListExporter.completion.message")),
                Messages.getString((Locale) null, "EListExporter.completion.title"), MessageType.INFORMATION, OptionType.OK, null,
                new CenteredOptionPaneCustomizer());
    }
}
