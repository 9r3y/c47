package com.y3r9.c47.annot;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The annotation Decoder.
 *
 * @version 1.0
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Decoder {

    String value();

}
