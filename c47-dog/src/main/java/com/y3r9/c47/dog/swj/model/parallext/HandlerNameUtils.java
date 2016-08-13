package com.y3r9.c47.dog.swj.model.parallext;

/**
 * The Class HandlerNameUtils.
 * 
 * @version 1.0
 * @since project 3.0
 */
public final class HandlerNameUtils {

    /**
     * Gets the join handler name.
     * 
     * @param builderName the builder name
     * @return the join handler name
     */
    public static String getJoinHandlerName(final String builderName) {
        return String.format("%s-JOIN-HANDLER", builderName);
    }

    /**
     * Gets the work handler name.
     * 
     * @param builderName the builder name
     * @return the work handler name
     */
    public static String getWorkHandlerName(final String builderName) {
        return String.format("%s-WORK-HANDLER", builderName);
    }

    /**
     * Instantiates a new sole name utils.
     */
    private HandlerNameUtils() {
    }
}
