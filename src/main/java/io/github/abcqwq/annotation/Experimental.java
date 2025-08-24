package io.github.abcqwq.annotation;

import java.lang.annotation.*;

/**
 * Marks an API as experimental.
 * <p>
 * Experimental APIs are subject to change, may be removed in future
 * versions, and should not be relied on for production stability.
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({
        ElementType.TYPE,
        ElementType.METHOD,
        ElementType.CONSTRUCTOR,
        ElementType.FIELD,
        ElementType.PACKAGE
})
public @interface Experimental {
    /**
     * Optional: version or reason for being experimental.
     */
    String value() default "";
}
