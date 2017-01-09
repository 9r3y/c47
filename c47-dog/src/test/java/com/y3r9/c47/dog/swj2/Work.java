package com.y3r9.c47.dog.swj2;

/**
 * The class Work.
 *
 * @version 1.0
 */
final class Work implements WorkHandler<Data, Object, Data> {
    @Override
    public Data handle(final Data input, final Object context) {
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += 1;
        }
        return input;
    }
}
