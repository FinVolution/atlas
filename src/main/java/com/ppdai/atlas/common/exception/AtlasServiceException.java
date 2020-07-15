package com.ppdai.atlas.common.exception;

public class AtlasServiceException extends RuntimeException {
    public AtlasServiceException(String message) {
        super(message);
    }

    public AtlasServiceException(Throwable cause) {
        super(cause);
    }

    public AtlasServiceException(Throwable cause, String message) {
        super(message, cause);
    }

    public static AtlasServiceException newAtlasException(String message, Object... params) {
        if (params != null && params.length > 0) {
            String format = String.format(message, params);
            if (params[params.length - 1] instanceof Throwable) {
                return new AtlasServiceException((Throwable) params[params.length - 1], format);
            } else {
                return new AtlasServiceException(format);
            }
        } else {
            return new AtlasServiceException(message);
        }
    }
}
