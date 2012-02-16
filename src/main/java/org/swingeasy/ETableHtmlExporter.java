package org.swingeasy;

import javax.swing.Icon;

/**
 * @author Jurgen
 */
public class ETableHtmlExporter<T> implements ETableExporter<T> {
    /**
     * 
     * @see org.swingeasy.ETableExporter#export(org.swingeasy.ETable)
     */
    @Override
    public void export(ETable<T> table) {
        StringBuilder sb = new StringBuilder("<html><body><th>");
        for (String name : table.getHeadernames()) {
            sb.append("<td>").append(name).append("</td>");
        }
        sb.append("</th>");
        for (ETableRecord<T> record : table) {
            sb.append("<tr>");
            for (int column = 0; column < record.size(); column++) {
                sb.append("<td>").append(record.getStringValue(column)).append("</td>");
            }
            sb.append("</tr>\n");
        }
        sb.append("<html><body>");
        EComponentPopupMenu.copy(sb.toString());
    }

    /**
     * 
     * @see org.swingeasy.ETableExporter#getAction()
     */
    @Override
    public String getAction() {
        return "html-export";
    }

    /**
     * 
     * @see org.swingeasy.ETableExporter#getFileExtension()
     */
    @Override
    public String getFileExtension() {
        return "html";
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
