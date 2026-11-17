package com.unionplace.retirement.plan.spec;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

/** 은퇴설계 시뮬레이션 요청. 금액은 원, 비율은 연 % 단위. */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetirementPlanRequest {

    @NotNull(message = "현재연령은 필수입니다.")
    @Min(value = 19, message = "현재연령은 19세 이상이어야 합니다.")
    @Max(value = 100, message = "현재연령은 100세 이하여야 합니다.")
    private Integer currentAge;

    @NotNull(message = "은퇴연령은 필수입니다.")
    @Max(value = 100, message = "은퇴연령은 100세 이하여야 합니다.")
    private Integer retirementAge;

    @NotNull(message = "기대수명은 필수입니다.")
    @Max(value = 120, message = "기대수명은 120세 이하여야 합니다.")
    private Integer lifeExpectancy;

    /** 은퇴 후 월간필요금액(현재가치) */
    @NotNull(message = "월간필요금액은 필수입니다.")
    @PositiveOrZero(message = "월간필요금액은 0 이상이어야 합니다.")
    private Long monthlyRequiredAmount;

    @NotNull(message = "물가상승률은 필수입니다.")
    @DecimalMin(value = "0", message = "물가상승률은 0 이상이어야 합니다.")
    @DecimalMax(value = "20", message = "물가상승률은 20 이하여야 합니다.")
    private Double inflationRate;

    @NotNull(message = "기대수익률은 필수입니다.")
    @DecimalMin(value = "0", message = "기대수익률은 0 이상이어야 합니다.")
    @DecimalMax(value = "30", message = "기대수익률은 30 이하여야 합니다.")
    private Double expectedReturnRate;

    @NotNull(message = "금융자산금액은 필수입니다.")
    @PositiveOrZero(message = "금융자산금액은 0 이상이어야 합니다.")
    private Long financialAssetAmount;

    @NotNull(message = "공적연금월간수령금액은 필수입니다.")
    @PositiveOrZero(message = "공적연금월간수령금액은 0 이상이어야 합니다.")
    private Long publicPensionMonthlyAmount;

    @NotNull(message = "개인연금월간수령금액은 필수입니다.")
    @PositiveOrZero(message = "개인연금월간수령금액은 0 이상이어야 합니다.")
    private Long personalPensionMonthlyAmount;

    @NotNull(message = "퇴직연금월간수령금액은 필수입니다.")
    @PositiveOrZero(message = "퇴직연금월간수령금액은 0 이상이어야 합니다.")
    private Long retirementPensionMonthlyAmount;

    @NotNull(message = "부동산현금화예상금액은 필수입니다.")
    @PositiveOrZero(message = "부동산현금화예상금액은 0 이상이어야 합니다.")
    private Long realEstateCashAmount;
}
