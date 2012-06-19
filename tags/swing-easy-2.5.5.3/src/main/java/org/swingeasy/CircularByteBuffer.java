package org.swingeasy;

/**
 * @author Jurgen
 */
public class CircularByteBuffer extends com.Ostermiller.util.CircularByteBuffer implements Stream {
    private CircularByteBuffer() {
        super(CircularByteBuffer.INFINITE_SIZE);
    }
}
