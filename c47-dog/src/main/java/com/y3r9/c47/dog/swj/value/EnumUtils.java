package com.y3r9.c47.dog.swj.value;

/**
 * The Class EnumUtils. Reference to {@link org.apache.commons.lang3.EnumUtils} class.
 * 
 * @version 1.0
 * @since project 2.4
 */
public final class EnumUtils {

    /**
     * <p>
     * Gets the enum for the class, returning {@code null} if not found.
     * </p>
     * 
     * <p>
     * This method differs from {@link Enum#valueOf} in that it does not throw an exception for an
     * invalid enum name.
     * </p>
     * 
     * @param <E> the type of the enumeration
     * @param enumClass the class of the enum to query, not null
     * @param enumName the enum name, null returns null
     * @return the enum, null if not found
     */
    public static <E extends Enum<E>> E getEnum(final Class<E> enumClass, final String enumName) {
        if (enumName == null) {
            return null;
        }
        try {
            return Enum.valueOf(enumClass, enumName);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    /**
     * Check enumeration.
     * 
     * @param <E> the element type
     * @param enumClass the enumeration class
     * @param value the value
     */
    public static <E extends Enum<E>> void checkEnum(final Class<E> enumClass,
            final String value) {
        if (EnumUtils.getEnum(enumClass, value) != null) {
            return;
        }
        throw new IllegalArgumentException(value + " is not value of " + enumClass);
    }

    /**
     * Gets the enumeration.
     * 
     * @param <E> the element type
     * @param enumClass the enumeration class
     * @param value the value
     * @return the enumeration
     */
    public static <E extends Enum<E>> E toEnum(final Class<E> enumClass, final String value) {
        checkEnum(enumClass, value);
        return EnumUtils.getEnum(enumClass, value);
    }

    /** The Constant ENUM_HELP_SEPARATOR. */
    public static final String ENUM_HELP_SEPARATOR = ", ";

    /**
     * Gets the names.
     * 
     * @param separator the separator
     * @return the names
     */
    public static <E extends Enum<E>> String getNames(final Class<E> enumClass) {
        return getNames(enumClass, ENUM_HELP_SEPARATOR);
    }

    /**
     * Gets the names.
     * 
     * @param separator the separator
     * @return the names
     */
    public static <E extends Enum<E>> String getNames(final Class<E> enumClass,
            final String separator) {
        final StringBuilder builder = new StringBuilder();
        for (Enum<E> enumVal : enumClass.getEnumConstants()) {
            if (builder.length() > 0) {
                builder.append(separator);
            }
            builder.append(enumVal.name());
        }
        return builder.toString();
    }

    /**
     * Instantiates a new enumeration utilities.
     */
    private EnumUtils() {
    }
}
