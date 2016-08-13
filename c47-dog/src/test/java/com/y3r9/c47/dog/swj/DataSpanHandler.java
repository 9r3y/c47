package com.y3r9.c47.dog.swj;

import com.y3r9.c47.dog.swj.model.parallel.spi.DispatchProc;
import com.y3r9.c47.dog.swj.model.parallel.spi.DispatchSpanHandler;

/**
 * The class DataSpanHandler.
 *
 * @version 1.0
 */
final class DataSpanHandler implements DispatchSpanHandler<Data> {
    @Override
    public void spanData(final Data data, final DispatchProc<Data> proc, final int partitionCount) {

    }
}
