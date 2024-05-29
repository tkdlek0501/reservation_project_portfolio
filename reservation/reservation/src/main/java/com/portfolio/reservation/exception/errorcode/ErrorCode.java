package com.portfolio.reservation.exception.errorcode;

import lombok.Getter;

@Getter
public enum ErrorCode {

    ALREADY_EXISTS_USER("USR001", "이미 해당 아이디의 회원이 있습니다."),
    NOT_LOGIN_USER("USR002", "로그인 정보가 없습니다. 다시 로그인 해주세요."),
    NOT_MATCHED_PASSWORD("USR003", "비밀번호가 일치하지 않습니다."),
    NOT_FOUND_USER("USR004", "회원이 조회되지 않습니다."),

    ALREADY_EXISTS_STORE("STR001", "이미 해당 이름의 매장이 있습니다."),
    NOT_FOUND_STORE("STR002", "매장이 조회되지 않습니다."),

    INVALID_ENUM_VALUE("SYS001", "변환할 수 없는 값이 들어왔습니다. : "),

    NOT_FOUND_SCHEDULE("SCD001", "스케줄이 조회되지 않습니다."),

    EXCEED_DATEOPERATION_SIZE("DO001", "기본 운영 방식은 3개까지 등록 가능합니다."),

    OVERLAP_DATEOPERATION("DO002", "기존 운영 방식과 일자가 중복됩니다."),

    NOT_FOUND_DATEOPERATION("DO003", "운영 날짜가 존재하지 않습니다."),

    EXCEED_OPERATION_DATE("OPR001", "기본 운영 방식은 시작일과 종료일의 차이가 30일이 넘으면 안됩니다."),

    OVERLAP_DATES_OPERATION("OPR002", "각 운영 방식의 일자가 중복됩니다."),

    OVERLAP_TIME_OPERATION("OPR003", "같은 날짜에 겹치는 예약 시간이 존재합니다.")
    ;

    private final String code;

    private final String msg;

    ErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
