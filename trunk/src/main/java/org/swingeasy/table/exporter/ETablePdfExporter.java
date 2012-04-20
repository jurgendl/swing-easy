package org.swingeasy.table.exporter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.swingeasy.ETable;
import org.swingeasy.Resources;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;

import com.lowagie.text.DocumentException;

/**
 * @author Jurgen
 */
public class ETablePdfExporter<T> extends ETableHtmlExporter<T> {
    /**
     * 
     * @see org.swingeasy.table.exporter.ETableHtmlExporter#exportStream(org.swingeasy.ETable)
     */
    @Override
    public InputStream exportStream(ETable<T> table) throws IOException {
        try {
            String data = this.createHtml(table);
            String css = new String(Resources.getDataResource("exporter/css.txt"));
            data = data.replace("<head>", "<head><style type=\"text/css\">" + css + "</style>");

            ITextRenderer itextRenderer = new ITextRenderer();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            org.w3c.dom.Document doc = builder.parse(new ByteArrayInputStream(data.getBytes("UTF-8")));
            itextRenderer.setDocument(doc, null);

            ByteArrayOutputStream os = new ByteArrayOutputStream();

            itextRenderer.layout();
            itextRenderer.createPDF(os);
            os.close();

            return new ByteArrayInputStream(os.toByteArray());
        } catch (ParserConfigurationException ex) {
            throw new IOException(ex);
        } catch (DocumentException ex) {
            throw new IOException(ex);
        } catch (SAXException ex) {
            throw new IOException(ex);
        }
    }

    /**
     * 
     * @see org.swingeasy.ETableExporter#getAction()
     */
    @Override
    public String getAction() {
        return "pdf-export";
    }

    /**
     * 
     * @see org.swingeasy.ETableExporter#getFileExtension()
     */
    @Override
    public String getFileExtension() {
        return "pdf";
    }
}
