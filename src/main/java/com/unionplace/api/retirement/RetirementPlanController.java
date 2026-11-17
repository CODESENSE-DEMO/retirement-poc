package com.unionplace.api.retirement;

import com.unionplace.retirement.plan.spec.RetirementPlanRequest;
import com.unionplace.retirement.plan.spec.RetirementPlanResponse;
import com.unionplace.retirement.plan.spec.RetirementPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/retirement-plans")
@RequiredArgsConstructor
public class RetirementPlanController {

    private final RetirementPlanService retirementPlanService;

    /** 저장하지 않는 계산 명령이라 200 + envelope */
    @PostMapping("/simulations")
    public RetirementPlanResponse simulate(@Valid @RequestBody RetirementPlanRequest request) {
        return retirementPlanService.simulate(request);
    }
}
