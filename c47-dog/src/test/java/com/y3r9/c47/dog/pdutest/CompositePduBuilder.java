package com.y3r9.c47.dog.pdutest;

/**
 * The class PduBuilder.
 *
 * @version 1.0
 */
final class CompositePduBuilder implements PduBuilder {

    /** The Pdu. */
    private CompositeBuf pdu = new CompositeBuf();

    @Override
    public Buf build(final Buf buf) {
        pdu.addComponent(buf);
        return pdu;
    }

    @Override
    public void clear() {
        pdu.clear();
    }

}
