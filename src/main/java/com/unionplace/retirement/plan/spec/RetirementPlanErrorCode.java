package com.unionplace.retirement.plan.spec;

import io.union.core.error.ErrorCode;

/** 은퇴설계 도메인 에러 코드. */
public enum RetirementPlanErrorCode implements ErrorCode {

    INVALID_RETIREMENT_AGE("RETIREMENT_001", "은퇴연령은 현재연령보다 커야 합니다.", 400),
    INVALID_LIFE_EXPECTANCY("RETIREMENT_002", "기대수명은 은퇴연령보다 커야 합니다.", 400);

    private final String code;
    private final String message;
    private final int status;

    RetirementPlanErrorCode(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override public String getCode() { return code; }
    @Override public String getMessage() { return message; }
    @Override public int getStatus() { return status; }
}
