package org.swingeasy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * @author Jurgen
 */
public abstract class ETableExporterImpl<T> implements ETableExporter<T> {
    protected ETableExporterFileChooserCustomizer customizer;

    public ETableExporterImpl() {
        this.customizer = new ETableExporterFileChooserCustomizer(this);
    }

    protected boolean canOverwrite(ETable<T> table) {
        if (ResultType.OK != CustomizableOptionPane.showCustomDialog(table,
                new JLabel(Messages.getString(null, "ETableExporter.overwrite.warning.message")),
                Messages.getString(null, "ETableExporter.overwrite.warning.title"), MessageType.WARNING, OptionType.OK_CANCEL, null,
                new CenteredOptionPaneCustomizer())) {
            return false;
        }
        return true;
    }

    /**
     * 
     * @see org.swingeasy.ETableExporter#export(org.swingeasy.ETable)
     */
    @Override
    public void export(ETable<T> table) {
        try {
            File exportTo = CustomizableOptionPane.showFileChooserDialog(table, FileChooserType.SAVE, this.customizer);
            if (exportTo != null) {
                final String ext = this.getFileExtension();
                if (!exportTo.getName().endsWith(ext)) {
                    exportTo = new File(exportTo.getParentFile(), exportTo.getName() + "." + ext);
                }
                if (exportTo.exists()) {
                    if (!this.canOverwrite(table)) {
                        return;
                    }
                }
                Stream stream = StreamFactory.create();
                this.exportStream(table, stream.getOutputStream());
                InputStream data = stream.getInputStream();
                FileOutputStream fout = new FileOutputStream(exportTo);
                byte[] buffer = new byte[1024 * 4];
                int read;
                while ((read = data.read(buffer)) != -1) {
                    fout.write(buffer, 0, read);
                }
                data.close();
                fout.close();
                ETableExporterFileChooserCustomizer.lastFile = exportTo;
                this.whenDone(table);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public abstract void exportStream(ETable<T> table, OutputStream out) throws IOException;

    /**
     * 
     * @see org.swingeasy.ETableExporter#getIcon()
     */
    @Override
    public Icon getIcon() {
        return UIUtils.getIconForFileType(this.getFileExtension());
    }

    protected void whenDone(ETable<T> table) {
        CustomizableOptionPane.showCustomDialog(table, new JLabel(Messages.getString(null, "ETableExporter.completion.message")),
                Messages.getString(null, "ETableExporter.completion.title"), MessageType.INFORMATION, OptionType.OK, null,
                new CenteredOptionPaneCustomizer());
    }
}
