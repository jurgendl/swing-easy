package org.swingeasy;

import java.awt.Component;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

/**
 * @author Jurgen
 */
public class EListExporterFileChooserCustomizer implements FileChooserCustomizer {
    protected final EListExporterImpl<?> EListExporter;

    protected static File lastFile = null;

    public EListExporterFileChooserCustomizer(EListExporterImpl<?> EListExporter) {
        this.EListExporter = EListExporter;
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
        if (EListExporterFileChooserCustomizer.lastFile != null) {
            jfc.setCurrentDirectory(EListExporterFileChooserCustomizer.lastFile.isDirectory() ? EListExporterFileChooserCustomizer.lastFile
                    : EListExporterFileChooserCustomizer.lastFile.getParentFile());
        }
        jfc.resetChoosableFileFilters();
        jfc.addChoosableFileFilter(new ExtensionFileFilter(UIUtils.getDescriptionForFileType(this.EListExporter.getFileExtension()) + " ("
                + this.EListExporter.getFileExtension() + ")", this.EListExporter.getFileExtension()));
    }
}
