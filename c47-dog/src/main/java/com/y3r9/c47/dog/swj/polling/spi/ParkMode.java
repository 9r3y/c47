package com.y3r9.c47.dog.swj.polling.spi;

/**
 * The enumeration ParkMode.
 * 
 * @version 1.2
 * @since project 3.1 change name from IdleMode to ParkMode
 */
public enum ParkMode {

    /** The sleep mode use TimeUnit.sleep. */
    sleep,

    /** The yield mode use Thread.yield. */
    yield,

    /** The busy mode use dead loop. */
    busy;
}
