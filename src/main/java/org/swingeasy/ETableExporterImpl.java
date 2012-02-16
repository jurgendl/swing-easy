package org.swingeasy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JFileChooser;

/**
 * @author Jurgen
 */
public abstract class ETableExporterImpl<T> implements ETableExporter<T> {
    protected JFileChooser fileChooser = null;

    /**
     * 
     * @see org.swingeasy.ETableExporter#export(org.swingeasy.ETable)
     */
    @Override
    public void export(ETable<T> table) {
        try {
            String exportString = this.exportString(table);
            if (exportString != null) {
                JFileChooser jfc = this.getFileChooser();
                if (JFileChooser.APPROVE_OPTION == jfc.showSaveDialog(table)) {
                    File exportTo = jfc.getSelectedFile();
                    if (exportTo != null) {
                        FileOutputStream fout = new FileOutputStream(exportTo);
                        fout.write(exportString.getBytes());
                        fout.close();
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public abstract String exportString(ETable<T> table);

    protected JFileChooser getFileChooser() {
        if (this.fileChooser == null) {
            this.fileChooser = new JFileChooser();
        }
        return this.fileChooser;
    }

    /**
     * 
     * @see org.swingeasy.ETableExporter#getIcon()
     */
    @Override
    public Icon getIcon() {
        return UIUtils.getIconForFileType(this.getFileExtension());
    }
}
