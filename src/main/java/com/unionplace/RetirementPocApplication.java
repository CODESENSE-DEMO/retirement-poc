package com.unionplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * 노후(은퇴)설계 PoC 애플리케이션. 화면은 JSP, 문구는 messages/result_ko.properties.
 * 표준 응답 포맷, 예외 처리, TraceId, JWT 인증은 union-starter 자동 설정이 담당한다.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class RetirementPocApplication {

    public static void main(String[] args) {
        SpringApplication.run(RetirementPocApplication.class, args);
    }
}
