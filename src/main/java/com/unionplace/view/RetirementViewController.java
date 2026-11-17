package com.unionplace.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** JSP 화면 라우팅. 화면 문구는 messages/result_ko.properties 의 코드로 렌더링한다. */
@Controller
public class RetirementViewController {

    @GetMapping("/")
    public String home() {
        return "redirect:/retirement";
    }

    /** 노후설계 소개 + 유의사항 (10165~10167) */
    @GetMapping("/retirement")
    public String intro() {
        return "retirement/intro";
    }

    /** 은퇴설계 입력·결과 */
    @GetMapping("/retirement/plan")
    public String plan() {
        return "retirement/plan";
    }

    /** AR회원 연동완료 (10000~10003). 실제 연동은 외부 AR 시스템이 수행하고 완료 시 이 화면으로 돌아온다. */
    @GetMapping("/ar-link/complete")
    public String arLinkComplete() {
        return "arlink/complete";
    }
}
