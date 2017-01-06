package com.y3r9.c47.dog.swj2;

/**
 * The class Work.
 *
 * @version 1.0
 */
final class Work implements WorkHandler<Data, Void> {
    @Override
    public Void handle(final Data input) {
        int sum = 1;
        for (int i = 0; i < 10; i++) {
            sum += 1;
        }
        return null;
    }
}
