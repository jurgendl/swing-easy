package org.swingeasy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;

import com.lowagie.text.DocumentException;

/**
 * @author Jurgen
 */
public class ETablePdfExporter<T> extends ETableHtmlExporter<T> {
    /**
     * 
     * @see org.swingeasy.ETableHtmlExporter#exportStream(org.swingeasy.ETable)
     */
    @Override
    public InputStream exportStream(ETable<T> table) throws IOException {
        try {
            InputStream data = super.exportStream(table);
            ITextRenderer itextRenderer = new ITextRenderer();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            byte[] buffer = new byte[data.available()];

            org.w3c.dom.Document doc = builder.parse(new ByteArrayInputStream(buffer));
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
