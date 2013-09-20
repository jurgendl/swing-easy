package org.swingeasy.treetable.exporter;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.swingeasy.ETreeTable;
import org.swingeasy.Resources;
import org.swingeasy.Stream;
import org.swingeasy.StreamFactory;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author Jurgen
 */
public class ETreeTablePdfExporter<T> extends ETreeTableHtmlExporter<T> {
    protected volatile byte[] style;

    /**
     * 
     * @see org.swingeasy.ETableExporterImpl#exportStream(org.swingeasy.ETable, java.io.OutputStream)
     */
    @Override
    public void exportStream(ETreeTable<T> table, OutputStream out) throws IOException {
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
        } catch (SAXParseException ex) {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            super.exportStream(table, bout);
            throw new RuntimeException("line " + ex.getLineNumber() + ", column " + ex.getColumnNumber() + "\n\n" + new String(bout.toByteArray()),
                    ex);
        } catch (SAXException ex) {
            throw new RuntimeException(ex);
        } catch (Exception ex) {
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
    protected void postHeaderCreate(ETreeTable<T> table, BufferedWriter writer) throws IOException {
        writer.write(new String(this.getStyle()));
    }
}
