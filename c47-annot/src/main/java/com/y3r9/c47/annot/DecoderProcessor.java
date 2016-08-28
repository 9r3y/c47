package com.y3r9.c47.annot;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * The class DecoderProcessor.
 *
 * @version 1.0
 */
@SupportedAnnotationTypes("com.y3r9.c47.annot.Decoder")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public final class DecoderProcessor extends AbstractProcessor {

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        System.out.println(roundEnv);
        System.out.println(annotations);
        for (Element elem : roundEnv.getElementsAnnotatedWith(Decoder.class)) {
            Decoder decoder = elem.getAnnotation(Decoder.class);
            String message = String.format("Annotation found in %s with decoder %s.", elem.getSimpleName(), decoder.value());
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
        }
        return true;
    }

    /**
     * Instantiates a new Decoder processor.
     */
    public DecoderProcessor() {
        super();
    }
}
