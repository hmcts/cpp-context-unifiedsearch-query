package uk.gov.moj.cpp.unifiedsearch.query.common.laa.transform;

public class LaaTransformException extends RuntimeException {

    public LaaTransformException() {
        super();
    }

    public LaaTransformException(final String message) {
        super(message);
    }

    public LaaTransformException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public LaaTransformException(final Throwable cause) {
        super(cause);
    }

    protected LaaTransformException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
