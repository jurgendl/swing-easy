package org.swingeasy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Jurgen
 */
public class DefaultStream implements Stream {
    protected ByteArrayOutputStream out = new ByteArrayOutputStream();

    /**
     * 
     * @see org.swingeasy.Stream#getInputStream()
     */
    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.out.toByteArray());
    }

    /**
     * 
     * @see org.swingeasy.Stream#getOutputStream()
     */
    @Override
    public OutputStream getOutputStream() {
        return this.out;
    }
}
