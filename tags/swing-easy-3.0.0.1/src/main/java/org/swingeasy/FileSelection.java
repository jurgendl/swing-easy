package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.filechooser.FileFilter;

/**
 * @author Jurgen
 */
public class FileSelection extends JPanel implements ActionListener, FileChooserCustomizer {
    private static final long serialVersionUID = 8303141891829028529L;

    private String[] ext;

    private ETextField textfield;

    private FileFilter filter;

    private boolean directory;

    public FileSelection(String... ext) {
        super(new BorderLayout());
        this.ext = ext;
        this.textfield = new ETextField(new ETextFieldConfig().setSelectAllOnFocus(true));
        this.textfield.setDragEnabled(true);
        this.textfield.setTransferHandler(new TransferHandler("file") {
            private static final long serialVersionUID = 76844729202962516L;

            @Override
            public boolean canImport(TransferSupport support) {
                return Arrays.asList(support.getDataFlavors()).contains(DataFlavor.javaFileListFlavor);
            }

            @Override
            public boolean importData(TransferSupport support) {
                try {
                    @SuppressWarnings("unchecked")
                    List<File> data = (List<File>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    ETextField component = (ETextField) support.getComponent();
                    for (File f : data) {
                        if ((FileSelection.this.directory == f.isDirectory()) && FileSelection.this.filter.accept(f)) {
                            component.setText(f.getAbsolutePath());
                            break;
                        }
                    }
                } catch (UnsupportedFlavorException ex) {
                    return false;
                } catch (IOException ex) {
                    return false;
                }
                return false;
            }
        });
        this.add(this.textfield);
        Icon icon;
        StringBuilder tooltip = new StringBuilder("<html>");
        if (ext.length == 0) {
            icon = UIUtils.getIconForDirectory();
            tooltip.append(UIUtils.getDescriptionForDirectory());
            this.filter = new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isDirectory();
                }

                @Override
                public String getDescription() {
                    return UIUtils.getDescriptionForDirectory();
                }

            };
            this.directory = true;
        } else if (ext.length == 1) {
            icon = UIUtils.getIconForFileType(ext[0]);
            tooltip.append(UIUtils.getDescriptionForFileType(ext[0])).append(" (").append(ext[0]).append(")");
            this.filter = new ExtensionFileFilter(UIUtils.getDescriptionForFileType(this.ext[0]) + " (" + this.ext[0] + ")", this.ext[0]);
            this.directory = false;
        } else {
            icon = UIUtils.getIconForFile();
            tooltip.append("<ul>");
            for (String e : ext) {
                tooltip.append("<li>").append(UIUtils.getDescriptionForFileType(e)).append(" (").append(e).append(")").append("</li>");
            }
            tooltip.append("</ul>");
            this.filter = new ExtensionFileFilter(Arrays.asList(this.ext));
            this.directory = false;
        }
        EButton select = new EButton(new EButtonConfig(new EIconButtonCustomizer(icon)));
        select.setToolTipText(tooltip.append("</html>").toString());
        select.addActionListener(this);
        this.add(select, BorderLayout.EAST);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.textfield.grabFocus();
        File file = CustomizableOptionPane.showFileChooserDialog(this, FileChooserType.OPEN, this);
        if (file != null) {
            this.textfield.setText(file.getAbsolutePath());
        }
    }

    /**
     * @see org.swingeasy.FileChooserCustomizer#customize(java.awt.Component, javax.swing.JDialog)
     */
    @Override
    public void customize(Component parentComponent, JDialog dialog) {
        //
    }

    /**
     * @see org.swingeasy.FileChooserCustomizer#customize(javax.swing.JFileChooser)
     */
    @Override
    public void customize(JFileChooser fileChooser) {
        fileChooser.resetChoosableFileFilters();
        fileChooser.addChoosableFileFilter(this.filter);
        fileChooser.setFileSelectionMode(this.directory ? JFileChooser.DIRECTORIES_ONLY : JFileChooser.FILES_ONLY);
    }
}
