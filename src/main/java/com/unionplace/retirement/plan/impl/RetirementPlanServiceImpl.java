package com.unionplace.retirement.plan.impl;

import com.unionplace.retirement.plan.spec.RetirementPlanErrorCode;
import com.unionplace.retirement.plan.spec.RetirementPlanRequest;
import com.unionplace.retirement.plan.spec.RetirementPlanResponse;
import com.unionplace.retirement.plan.spec.RetirementPlanService;
import io.union.core.util.Assert;
import org.springframework.stereotype.Service;

import java.time.Year;

/**
 * 은퇴설계 시뮬레이션.
 * <ul>
 *   <li>필요자금 = 월간필요금액 × (1+물가상승률)^준비기간 × 12 × 은퇴기간</li>
 *   <li>준비자금 = 금융자산 × (1+기대수익률)^준비기간 + 연금 월수령액 × 12 × 은퇴기간 + 부동산현금화</li>
 *   <li>월적립금액 = 부족자금을 준비기간 동안 월복리(기대수익률/12)로 모으는 적립액</li>
 * </ul>
 * ponytail: 은퇴기간 중 물가상승·운용수익과 세금은 반영하지 않는 단순 모델. 상품 제안 단계에서 연도별 현금흐름 모델로 교체.
 */
@Service
public class RetirementPlanServiceImpl implements RetirementPlanService {

    private static final int MONTHS_PER_YEAR = 12;
    private static final double PERCENT = 100.0;

    @Override
    public RetirementPlanResponse simulate(RetirementPlanRequest request) {
        Assert.isTrue(request.getRetirementAge() > request.getCurrentAge(),
                RetirementPlanErrorCode.INVALID_RETIREMENT_AGE);
        Assert.isTrue(request.getLifeExpectancy() > request.getRetirementAge(),
                RetirementPlanErrorCode.INVALID_LIFE_EXPECTANCY);

        int preparationYears = request.getRetirementAge() - request.getCurrentAge();
        int retirementYears = request.getLifeExpectancy() - request.getRetirementAge();
        double inflation = request.getInflationRate() / PERCENT;
        double expectedReturn = request.getExpectedReturnRate() / PERCENT;
        long retirementMonths = (long) retirementYears * MONTHS_PER_YEAR;

        long monthlyRequired = Math.round(request.getMonthlyRequiredAmount() * Math.pow(1 + inflation, preparationYears));
        long requiredFund = monthlyRequired * retirementMonths;

        long financialAsset = Math.round(request.getFinancialAssetAmount() * Math.pow(1 + expectedReturn, preparationYears));
        long publicPension = request.getPublicPensionMonthlyAmount() * retirementMonths;
        long personalPension = request.getPersonalPensionMonthlyAmount() * retirementMonths;
        long retirementPension = request.getRetirementPensionMonthlyAmount() * retirementMonths;
        long realEstate = request.getRealEstateCashAmount();
        long preparedFund = financialAsset + publicPension + personalPension + retirementPension + realEstate;

        long shortageFund = Math.max(0, requiredFund - preparedFund);
        double achievementRate = requiredFund == 0 ? PERCENT : Math.min(PERCENT, ratio(preparedFund, requiredFund));

        int currentYear = Year.now().getValue();
        return RetirementPlanResponse.builder()
                .currentYear(currentYear)
                .retirementYear(currentYear + preparationYears)
                .lifeExpectancyYear(currentYear + preparationYears + retirementYears)
                .preparationYears(preparationYears)
                .retirementYears(retirementYears)
                .monthlyRequiredAmount(monthlyRequired)
                .requiredFund(requiredFund)
                .preparedFund(preparedFund)
                .shortageFund(shortageFund)
                .achievementRate(achievementRate)
                .nonAchievementRate(round1(PERCENT - achievementRate))
                .monthlySavingAmount(monthlySaving(shortageFund, expectedReturn, preparationYears * MONTHS_PER_YEAR))
                .financialAssetAmount(financialAsset)
                .publicPensionAmount(publicPension)
                .personalPensionAmount(personalPension)
                .retirementPensionAmount(retirementPension)
                .realEstateCashAmount(realEstate)
                .financialAssetRatio(ratio(financialAsset, preparedFund))
                .publicPensionRatio(ratio(publicPension, preparedFund))
                .personalPensionRatio(ratio(personalPension, preparedFund))
                .retirementPensionRatio(ratio(retirementPension, preparedFund))
                .realEstateRatio(ratio(realEstate, preparedFund))
                .build();
    }

    /** 적립식 미래가치 FV = PMT × ((1+i)^n - 1) / i 를 PMT 로 푼 값. 원 단위 올림. */
    private static long monthlySaving(long shortageFund, double annualReturn, int months) {
        if (shortageFund == 0) {
            return 0;
        }
        double monthlyRate = annualReturn / MONTHS_PER_YEAR;
        if (monthlyRate == 0) {
            return (long) Math.ceil((double) shortageFund / months);
        }
        return (long) Math.ceil(shortageFund * monthlyRate / (Math.pow(1 + monthlyRate, months) - 1));
    }

    private static double ratio(long part, long total) {
        return total == 0 ? 0 : round1(part * PERCENT / total);
    }

    private static double round1(double value) {
        return Math.round(value * 10) / 10.0;
    }
}
