package demo.ddd.model;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ADD THIS ANNOTATION TO HAVE FIELD DO NOT SAVED INTO DATABASE OR INDEX INTO ELASTICSEARCH
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TransField {
}