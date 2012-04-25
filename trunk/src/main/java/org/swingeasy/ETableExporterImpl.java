package org.swingeasy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.Icon;

/**
 * @author Jurgen
 */
public abstract class ETableExporterImpl<T> implements ETableExporter<T> {
    protected ETableExporterFileChooserCustomizer customizer;

    public ETableExporterImpl() {
        this.customizer = new ETableExporterFileChooserCustomizer(this);
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
}
