package org.swingeasy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Jurgen
 */
public class ETableHtmlExporter<T> extends ETableExporterImpl<T> {
    /**
     * 
     * @see org.swingeasy.ETableExporterImpl#exportString(org.swingeasy.ETable)
     */
    @Override
    public InputStream exportStream(ETable<T> table) throws IOException {
        StringBuilder sb = new StringBuilder("<html><body border=1><table><tr>");
        for (String name : table.getHeadernames()) {
            sb.append("<th>").append(name).append("</th>");
        }
        sb.append("</tr>");
        for (ETableRecord<T> record : table) {
            sb.append("<tr>");
            for (int column = 0; column < record.size(); column++) {
                sb.append("<td>").append(record.getStringValue(column)).append("</td>");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table></html></body>");
        return new ByteArrayInputStream(sb.toString().getBytes());
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
