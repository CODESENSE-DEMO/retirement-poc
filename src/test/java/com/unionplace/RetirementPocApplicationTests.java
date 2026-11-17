package com.unionplace;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Union Framework + JSP 연동 스모크 테스트: 메시지 번들 → 화면 라우팅 → 표준 응답/에러 코드. */
@SpringBootTest
@AutoConfigureMockMvc
class RetirementPocApplicationTests {

    private static final String VALID_BODY = "{\"currentAge\":40,\"retirementAge\":60,\"lifeExpectancy\":90,"
            + "\"monthlyRequiredAmount\":2000000,\"inflationRate\":0,\"expectedReturnRate\":0,"
            + "\"financialAssetAmount\":100000000,\"publicPensionMonthlyAmount\":1000000,"
            + "\"personalPensionMonthlyAmount\":0,\"retirementPensionMonthlyAmount\":0,\"realEstateCashAmount\":0}";

    @Autowired MockMvc mvc;
    @Autowired MessageSource messageSource;

    @Test
    void result_ko_메시지번들을_코드로_조회한다() {
        assertThat(messageSource.getMessage("10165", null, Locale.KOREAN)).isEqualTo("노후설계");
        assertThat(messageSource.getMessage("10162", null, Locale.KOREAN)).startsWith("로그인이 필요한 메뉴");
    }

    @Test
    void 화면은_비로그인으로_JSP로_포워드된다() throws Exception {
        mvc.perform(get("/retirement")).andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/views/retirement/intro.jsp"));
        mvc.perform(get("/retirement/plan")).andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/views/retirement/plan.jsp"));
        mvc.perform(get("/ar-link/complete")).andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/views/arlink/complete.jsp"));
    }

    @Test
    void 시뮬레이션은_ApiResponse로_래핑된다() throws Exception {
        mvc.perform(post("/api/v1/retirement-plans/simulations").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Trace-Id"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.shortageFund").value(260000000))
                .andExpect(jsonPath("$.data.achievementRate").value(63.9));
    }

    @Test
    void 연령_규칙_위반은_도메인_에러코드() throws Exception {
        mvc.perform(post("/api/v1/retirement-plans/simulations").contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY.replace("\"retirementAge\":60", "\"retirementAge\":30")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("RETIREMENT_001"));
    }

    @Test
    void 검증_실패는_필드오류_목록을_포함한다() throws Exception {
        mvc.perform(post("/api/v1/retirement-plans/simulations").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"currentAge\":10}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("COMMON_400"))
                .andExpect(jsonPath("$.errors").isArray());
    }
}
