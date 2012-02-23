package org.swingeasy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Jurgen
 */
public class ETableHtmlExporter<T> extends ETableExporterImpl<T> {
    protected String createHtml(ETable<T> table) {
        StringBuilder sb = new StringBuilder(
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title></title></head><body><table border=\"1\"><tr>");
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
        sb.append("</table></body></html>");
        String html = sb.toString();
        return html;
    }

    /**
     * 
     * @see org.swingeasy.ETableExporterImpl#exportString(org.swingeasy.ETable)
     */
    @Override
    public InputStream exportStream(ETable<T> table) throws IOException {
        String html = this.createHtml(table);
        return new ByteArrayInputStream(html.getBytes());
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
