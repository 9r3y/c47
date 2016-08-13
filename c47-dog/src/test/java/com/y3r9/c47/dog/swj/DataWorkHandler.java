package com.y3r9.c47.dog.swj;

import com.y3r9.c47.dog.swj.model.parallext.AbstractWorkHandler;

/**
 * The class DataWorkHandler.
 *
 * @version 1.0
 */
final class DataWorkHandler extends AbstractWorkHandler<Data, DataContext, DataResult> {

    private static DataResult dataResult = new DataResult();
    /**
     * Instantiates a new abstract work handler.
     *
     * @param name the name
     */
    protected DataWorkHandler(final String name) {
        super(name);
    }

    @Override
    protected DataResult execute(final Data data, final DataContext context) {
        int sum = 1;
        for (int i = 0; i < data.getMs(); i++) {
            sum *= sum;
        }
//        try {
//            Thread.sleep(data.getMs());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        final DataResult result = new DataResult();
        result.setData(data);
        return result;
    }
}
