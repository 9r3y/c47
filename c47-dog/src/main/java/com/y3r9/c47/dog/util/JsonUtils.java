package com.y3r9.c47.dog.util;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * The class JsonUtils.
 *
 * @version 1.0
 */
public final class JsonUtils {

    /**
     * Object to json string.
     *
     * @param obj the obj
     * @return the string
     */
    public static byte[] toJsonBytes(final Object obj) {
        final Gson gson = new Gson();
        final String text = gson.toJson(obj);
        return text.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * To json bytes.
     *
     * @param obj the obj
     * @param clazz the clazz
     * @param typeAdapter the type adapter
     * @return the byte [ ]
     */
    public static byte[] toJsonBytes(final Object obj, final Class<?> clazz,
            final Object typeAdapter) {
        final Gson gson = new GsonBuilder().registerTypeAdapter(clazz, typeAdapter).create();
        final String text = gson.toJson(obj);
        return text.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * To json bytes.
     *
     * @param obj the obj
     * @param adaptedClazzs the adapted clazzs
     * @param typeAdapters the type adapters
     * @return the byte [ ]
     */
    public static byte[] toJsonBytes(final Object obj, final Class<?>[] adaptedClazzs,
            final Object[] typeAdapters) {
        final int size = Math.min(adaptedClazzs.length, typeAdapters.length);
        if (typeAdapters.length < adaptedClazzs.length) {
            throw new IllegalArgumentException("clazz size mismatches type adapter size: "
                    + adaptedClazzs.length + "," + typeAdapters.length);
        }
        final GsonBuilder gsonBuilder = new GsonBuilder();
        for (int i = 0; i < size; i++) {
            gsonBuilder.registerTypeAdapter(adaptedClazzs[i], typeAdapters[i]);
        }
        final Gson gson = new GsonBuilder().create();
        final String text = gson.toJson(obj);
        return text.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * From json.
     *
     * @param <T> the type parameter
     * @param text the text
     * @param clazz the clazz
     * @return the t
     */
    public static <T> T fromJson(final String text, final Class<T> clazz) {
        final Gson gson = new Gson();
        return gson.fromJson(text, clazz);
    }

    /**
     * From json.
     *
     * @param <T>   the type parameter
     * @param text the text
     * @param clazz the clazz
     * @param adaptedClazz the adapted clazz
     * @param typeAdapter the type adapter
     * @return the t
     */
    public static <T> T fromJson(final String text, final Class<T> clazz,
            final Class<?> adaptedClazz, final Object typeAdapter) {
        final Gson gson = new GsonBuilder().registerTypeAdapter(adaptedClazz, typeAdapter).create();
        return gson.fromJson(text, clazz);
    }

    /**
     * From json.
     *
     * @param <T> the type parameter
     * @param text the text
     * @param clazz the clazz
     * @param adaptedClazzs the adapted clazzs
     * @param typeAdapters the type adapters
     * @return the t
     */
    public static <T> T fromJson(final String text, final Class<T> clazz,
            final Class<?>[] adaptedClazzs,
            final Object[] typeAdapters) {
        final int size = Math.min(adaptedClazzs.length, typeAdapters.length);
        if (typeAdapters.length < adaptedClazzs.length) {
            throw new IllegalArgumentException("clazz size mismatches type adapter size: "
                    + adaptedClazzs.length + "," + typeAdapters.length);
        }
        final GsonBuilder gsonBuilder = new GsonBuilder();
        for (int i = 0; i < size; i++) {
            gsonBuilder.registerTypeAdapter(adaptedClazzs[i], typeAdapters[i]);
        }
        final Gson gson = new GsonBuilder().create();
        return gson.fromJson(text, clazz);
    }

    /**
     * The Class Date time type adapter.
     *
     * @version 1.0
     */
    public static final class DatePatternTypeAdapter implements JsonDeserializer<Date>,
            JsonSerializer<Date> {

        /** The Pattern. */
        private final String pattern;

        /**
         * Instantiates a new Date type adapter.
         *
         * @param pattern the pattern
         */
        public DatePatternTypeAdapter(final String pattern) {
            this.pattern = pattern;
        }

        @Override
        public Date deserialize(final JsonElement json, final Type type,
                final JsonDeserializationContext context) {
            final String text = json.getAsString();
            final DateFormat dateFormat = new SimpleDateFormat(pattern);
            try {
                return dateFormat.parse(text);
            } catch (ParseException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        @Override
        public JsonElement serialize(final Date date, final Type type,
                final JsonSerializationContext jsonSerializationContext) {
            final DateFormat dateFormat = new SimpleDateFormat(pattern);
            return new JsonPrimitive(dateFormat.format(date));
        }
    }

    /**
     * The Class Date type adapter.
     *
     * @version 1.0
     */
    public static final class DateTimeUnitTypeAdapter implements JsonDeserializer<Date>,
            JsonSerializer<Date> {

        /** The Time unit. */
        private final TimeUnit timeUnit;

        /**
         * Instantiates a new Date time unit type adapter.
         *
         * @param timeUnit the time unit
         */
        public DateTimeUnitTypeAdapter(final TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
        }

        @Override
        public Date deserialize(final JsonElement json, final Type type,
                final JsonDeserializationContext context) {
            final String text = json.getAsString();
            final long value = Long.parseLong(text);
            return new Date(timeUnit.toMillis(value));
        }

        @Override
        public JsonElement serialize(final Date date, final Type type,
                final JsonSerializationContext jsonSerializationContext) {
            final long value;
            switch (timeUnit) {
            case NANOSECONDS:
                value = TimeUnit.MILLISECONDS.toNanos(date.getTime());
                break;
            case MILLISECONDS:
                value = date.getTime();
                break;
            case SECONDS:
                value = TimeUnit.MILLISECONDS.toSeconds(date.getTime());
                break;
            default:
                throw new UnsupportedOperationException(timeUnit.toString());
            }
            return new JsonPrimitive(value);
        }
    }

    /**
     * Instantiates a new Json utils.
     */
    private JsonUtils() {
    }
}
