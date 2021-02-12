package com.cc.database.jaxrs.api.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ServiceErrorCode {
    SERVICE_ERROR(500),
    SERVICE_UNAVAILABLE(503),
    INVALID_CREDENTIALS(1000),
    ACCOUNT_LOCKED(1001),
    MALFORMED_REQUEST(1002),
    REGISTRATION_FAILED(1003),
    PARAMETER_VALIDATION_FAILED(1004),
    EMAIL_EXISTS(1005),
    PASSWORD_RESTRICTED(1007),
    PASSWORD_EXPIRED(1008),
    ITEM_NOT_FOUND(2000);

    @Getter
    private final int errorCode;
}
