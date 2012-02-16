package org.swingeasy;

/**
 * @author Jurgen
 */
public class ETableHtmlExporter<T> extends ETableExporterImpl<T> {
    /**
     * 
     * @see org.swingeasy.ETableExporterImpl#exportString(org.swingeasy.ETable)
     */
    @Override
    public String exportString(ETable<T> table) {
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
        return sb.toString();
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
}
