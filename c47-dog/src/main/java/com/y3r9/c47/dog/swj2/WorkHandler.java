package com.y3r9.c47.dog.swj2;

/**
 * The interface WorkHandler.
 *
 * @version 1.0
 */
interface WorkHandler<I,C,O> {

    O handle(I input, C context);

}
