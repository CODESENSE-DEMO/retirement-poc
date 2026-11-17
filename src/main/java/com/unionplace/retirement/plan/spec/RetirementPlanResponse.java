package com.unionplace.retirement.plan.spec;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/** 은퇴설계 시뮬레이션 결과. 금액은 원(명목, 은퇴 시점 기준), 비율은 %. */
@Getter
@Builder
@AllArgsConstructor
public class RetirementPlanResponse {

    private int currentYear;
    private int retirementYear;
    private int lifeExpectancyYear;
    /** 준비기간(년) = 은퇴연령 - 현재연령 */
    private int preparationYears;
    /** 은퇴기간(년) = 기대수명 - 은퇴연령 */
    private int retirementYears;

    /** 은퇴 시점 물가로 환산한 월간필요금액 */
    private long monthlyRequiredAmount;
    private long requiredFund;
    private long preparedFund;
    private long shortageFund;
    private double achievementRate;
    private double nonAchievementRate;
    /** 부족자금을 준비기간 동안 모으기 위한 월적립금액 */
    private long monthlySavingAmount;

    private long financialAssetAmount;
    private long publicPensionAmount;
    private long personalPensionAmount;
    private long retirementPensionAmount;
    private long realEstateCashAmount;

    private double financialAssetRatio;
    private double publicPensionRatio;
    private double personalPensionRatio;
    private double retirementPensionRatio;
    private double realEstateRatio;
}
