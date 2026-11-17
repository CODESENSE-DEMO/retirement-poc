package com.unionplace.retirement.plan.impl;

import com.unionplace.retirement.plan.spec.RetirementPlanErrorCode;
import com.unionplace.retirement.plan.spec.RetirementPlanRequest;
import com.unionplace.retirement.plan.spec.RetirementPlanResponse;
import io.union.core.exception.BusinessException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RetirementPlanServiceImplTest {

    private final RetirementPlanServiceImpl service = new RetirementPlanServiceImpl();

    private RetirementPlanRequest.RetirementPlanRequestBuilder base() {
        return RetirementPlanRequest.builder()
                .currentAge(40).retirementAge(60).lifeExpectancy(90)
                .monthlyRequiredAmount(2_000_000L)
                .inflationRate(0.0).expectedReturnRate(0.0)
                .financialAssetAmount(100_000_000L)
                .publicPensionMonthlyAmount(1_000_000L)
                .personalPensionMonthlyAmount(0L)
                .retirementPensionMonthlyAmount(0L)
                .realEstateCashAmount(0L);
    }

    @Test
    void 수익률_물가_0이면_단순합산으로_부족자금과_월적립금액을_계산한다() {
        RetirementPlanResponse result = service.simulate(base().build());

        assertThat(result.getPreparationYears()).isEqualTo(20);
        assertThat(result.getRetirementYears()).isEqualTo(30);
        assertThat(result.getRetirementYear() - result.getCurrentYear()).isEqualTo(20);
        assertThat(result.getRequiredFund()).isEqualTo(720_000_000L);       // 200만 × 12 × 30
        assertThat(result.getPreparedFund()).isEqualTo(460_000_000L);       // 1억 + 100만 × 12 × 30
        assertThat(result.getShortageFund()).isEqualTo(260_000_000L);
        assertThat(result.getAchievementRate()).isEqualTo(63.9);
        assertThat(result.getNonAchievementRate()).isEqualTo(36.1);
        assertThat(result.getMonthlySavingAmount()).isEqualTo(1_083_334L);  // 2.6억 / 240 올림
        assertThat(result.getFinancialAssetRatio()).isEqualTo(21.7);
        assertThat(result.getPublicPensionRatio()).isEqualTo(78.3);
    }

    @Test
    void 물가와_수익률은_준비기간_복리로_반영된다() {
        RetirementPlanResponse result = service.simulate(base()
                .inflationRate(2.0).expectedReturnRate(4.0).build());

        assertThat(result.getMonthlyRequiredAmount()).isEqualTo(Math.round(2_000_000 * Math.pow(1.02, 20)));
        assertThat(result.getFinancialAssetAmount()).isEqualTo(Math.round(100_000_000 * Math.pow(1.04, 20)));
        // 월적립액을 같은 조건으로 굴리면 부족자금 이상이 된다
        double i = 0.04 / 12;
        double fv = result.getMonthlySavingAmount() * (Math.pow(1 + i, 240) - 1) / i;
        assertThat(fv).isBetween((double) result.getShortageFund(), result.getShortageFund() + 1_000.0);
    }

    @Test
    void 준비자금이_충분하면_부족자금0_달성도100() {
        RetirementPlanResponse result = service.simulate(base().realEstateCashAmount(1_000_000_000L).build());

        assertThat(result.getShortageFund()).isZero();
        assertThat(result.getAchievementRate()).isEqualTo(100.0);
        assertThat(result.getNonAchievementRate()).isZero();
        assertThat(result.getMonthlySavingAmount()).isZero();
    }

    @Test
    void 은퇴연령이_현재연령_이하면_RETIREMENT_001() {
        assertThatThrownBy(() -> service.simulate(base().retirementAge(40).build()))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode").isEqualTo(RetirementPlanErrorCode.INVALID_RETIREMENT_AGE);
    }

    @Test
    void 기대수명이_은퇴연령_이하면_RETIREMENT_002() {
        assertThatThrownBy(() -> service.simulate(base().lifeExpectancy(60).build()))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode").isEqualTo(RetirementPlanErrorCode.INVALID_LIFE_EXPECTANCY);
    }
}
