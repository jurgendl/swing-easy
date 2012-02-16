package org.swingeasy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * @author Jurgen
 */
public abstract class ETableExporterImpl<T> implements ETableExporter<T> {
    public static class ExtensionFileFilter extends FileFilter {

        protected static String descriptionFromExtensions(String[] exts) {
            if ((exts == null) || (exts.length == 0)) {
                return "";
            }

            StringBuilder sb = new StringBuilder();

            for (String extension : exts) {
                sb.append(extension).append(", ");
            }

            sb.deleteCharAt(sb.length() - 1);

            String string = sb.deleteCharAt(sb.length() - 1).toString();

            return string;
        }

        protected String description;

        protected String[] extensions;

        public ExtensionFileFilter(final List<String> extensions) {
            this.extensions = extensions.toArray(new String[extensions.size()]);
            this.description = ExtensionFileFilter.descriptionFromExtensions(this.extensions);
        }

        public ExtensionFileFilter(final String description, final List<String> extensions) {
            this.extensions = extensions.toArray(new String[extensions.size()]);
            this.description = description;
        }

        public ExtensionFileFilter(final String description, final String... extensions) {
            if (extensions.length == 0) {
                this.extensions = new String[] { description };
                this.description = ExtensionFileFilter.descriptionFromExtensions(extensions);
            } else {
                this.extensions = extensions;
                this.description = description;
            }
        }

        /**
         * 
         * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
         */
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            for (String extension : this.extensions) {
                String _extension = extension.replaceAll("\\[", "").replaceAll("\\]", "");
                if (f.getName().toLowerCase().endsWith("." + _extension.toLowerCase())) {
                    return true;
                }
            }

            return false;
        }

        /**
         * 
         * @see javax.swing.filechooser.FileFilter#getDescription()
         */
        @Override
        public String getDescription() {
            return this.description;
        }

        public String[] getExtensions() {
            return this.extensions;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setExtensions(String[] extensions) {
            this.extensions = extensions;
        }
    }

    protected static JFileChooser fileChooser = null;

    protected static File lastFile = null;

    protected static JFileChooser getFileChooser() {
        if (ETableExporterImpl.fileChooser == null) {
            try {
                ETableExporterImpl.lastFile = new File("tmp").getCanonicalFile().getParentFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            ETableExporterImpl.fileChooser = new JFileChooser();
        }
        return ETableExporterImpl.fileChooser;
    }

    public ETableExporterImpl() {
        super();
    }

    /**
     * 
     * @see org.swingeasy.ETableExporter#export(org.swingeasy.ETable)
     */
    @Override
    public void export(ETable<T> table) {
        try {
            InputStream data = this.exportStream(table);
            if (data != null) {
                JFileChooser jfc = ETableExporterImpl.getFileChooser();
                jfc.setCurrentDirectory(ETableExporterImpl.lastFile.isDirectory() ? ETableExporterImpl.lastFile : ETableExporterImpl.lastFile
                        .getParentFile());
                jfc.resetChoosableFileFilters();
                String ext = this.getFileExtension();
                String description = this.getFileExtensionDescription() + " (" + ext + ")";
                jfc.addChoosableFileFilter(new ExtensionFileFilter(description, ext));
                if (JFileChooser.APPROVE_OPTION == jfc.showSaveDialog(table)) {
                    File exportTo = jfc.getSelectedFile();
                    if (exportTo != null) {
                        if (!exportTo.getName().endsWith(ext)) {
                            exportTo = new File(exportTo.getParentFile(), exportTo.getName() + "." + ext);
                        }
                        ETableExporterImpl.lastFile = exportTo;
                        FileOutputStream fout = new FileOutputStream(exportTo);
                        byte[] buffer = new byte[1024 * 8];
                        int read;
                        while ((read = data.read(buffer)) != -1) {
                            fout.write(buffer, 0, read);
                        }
                        data.close();
                        fout.close();
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public abstract InputStream exportStream(ETable<T> table) throws IOException;

    protected String getFileExtensionDescription() {
        return UIUtils.getDescriptionForFileType(this.getFileExtension());
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
