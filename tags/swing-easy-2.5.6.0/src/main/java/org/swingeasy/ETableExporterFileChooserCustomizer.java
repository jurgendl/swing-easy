package org.swingeasy;

import java.awt.Component;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

/**
 * @author Jurgen
 */
public class ETableExporterFileChooserCustomizer implements FileChooserCustomizer {
    protected final ETableExporterImpl<?> etableExporter;

    protected static File lastFile = null;

    public ETableExporterFileChooserCustomizer(ETableExporterImpl<?> etableExporter) {
        this.etableExporter = etableExporter;
    }

    /**
     * 
     * @see org.swingeasy.FileChooserCustomizer#customize(java.awt.Component, javax.swing.JDialog)
     */
    @Override
    public void customize(Component parentComponent, JDialog dialog) {
        dialog.setLocationRelativeTo(null);
    }

    /**
     * 
     * @see org.swingeasy.FileChooserCustomizer#customize(javax.swing.JFileChooser)
     */
    @Override
    public void customize(JFileChooser jfc) {
        if (ETableExporterFileChooserCustomizer.lastFile != null) {
            jfc.setCurrentDirectory(ETableExporterFileChooserCustomizer.lastFile.isDirectory() ? ETableExporterFileChooserCustomizer.lastFile
                    : ETableExporterFileChooserCustomizer.lastFile.getParentFile());
        }
        jfc.resetChoosableFileFilters();
        jfc.addChoosableFileFilter(new ExtensionFileFilter(UIUtils.getDescriptionForFileType(this.etableExporter.getFileExtension()) + " ("
                + this.etableExporter.getFileExtension() + ")", this.etableExporter.getFileExtension()));
    }
}
