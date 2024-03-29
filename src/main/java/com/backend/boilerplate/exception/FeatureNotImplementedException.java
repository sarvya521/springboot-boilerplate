package com.backend.boilerplate.exception;

import com.sp.boilerplate.commons.exception.ErrorDetails;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author sarvesh
 * @version 0.0.1
 * @since 0.0.1
 */
public class FeatureNotImplementedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private static final String ERROR = "Feature not yet implemented";

    /**
     * see {@link ErrorDetails}
     */
    @Getter
    private final ErrorDetails error;


    /**
     * @see RuntimeException#RuntimeException()
     */
    public FeatureNotImplementedException() {
        this.error = new ErrorDetails(String.valueOf(HttpStatus.NOT_IMPLEMENTED.value()), ERROR);
    }

}
