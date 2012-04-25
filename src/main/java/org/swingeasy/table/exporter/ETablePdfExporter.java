package org.swingeasy.table.exporter;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.swingeasy.ETable;
import org.swingeasy.Resources;
import org.swingeasy.Stream;
import org.swingeasy.StreamFactory;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;

import com.lowagie.text.DocumentException;

/**
 * @author Jurgen
 */
public class ETablePdfExporter<T> extends ETableHtmlExporter<T> {
    protected volatile byte[] style;

    /**
     * 
     * @see org.swingeasy.ETableExporterImpl#exportStream(org.swingeasy.ETable, java.io.OutputStream)
     */
    @Override
    public void exportStream(ETable<T> table, OutputStream out) throws IOException {
        try {
            Stream stream = StreamFactory.create();
            super.exportStream(table, stream.getOutputStream());
            ITextRenderer itextRenderer = new ITextRenderer();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(stream.getInputStream());
            itextRenderer.setDocument(doc, null);
            itextRenderer.layout();
            itextRenderer.createPDF(out);
            itextRenderer.finishPDF();
            out.close();
        } catch (ParserConfigurationException ex) {
            throw new RuntimeException(ex);
        } catch (SAXException ex) {
            throw new RuntimeException(ex);
        } catch (DocumentException ex) {
            throw new RuntimeException(ex);
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

    protected byte[] getStyle() throws IOException {
        if (this.style == null) {
            this.style = Resources.getDataResource("exporter/css.txt");
        }
        return this.style;
    }

    /**
     * 
     * @see org.swingeasy.table.exporter.ETableHtmlExporter#postHeaderCreate(org.swingeasy.ETable, java.io.OutputStream)
     */
    @Override
    protected void postHeaderCreate(ETable<T> table, OutputStream out) throws IOException {
        out.write(this.getStyle());
    }
}
