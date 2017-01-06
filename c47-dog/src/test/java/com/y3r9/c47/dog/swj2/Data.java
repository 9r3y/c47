package com.y3r9.c47.dog.swj2;

/**
 * The class Data.
 *
 * @version 1.0
 */
final class Data implements SwjData {
    @Override
    public void setToken(final int token) {
        this.token = token;
    }

    @Override
    public int getToken() {
        return token;
    }

    private int token;
}
